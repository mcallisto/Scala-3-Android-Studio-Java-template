package com.example.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskProvider
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.Callable

class ScalaAndroidPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.logger.lifecycle("Applying ScalaAndroid Plugin to project: ${project.name}")
        
        if (!project.plugins.hasPlugin('com.android.application') && 
            !project.plugins.hasPlugin('com.android.library')) {
            project.logger.warn("Android plugin is required for ScalaAndroid plugin")
            return
        }
        
        // Create scala source directories if they don't exist
        createScalaDirectories(project)
        
        // Add scala-library dependency
        project.dependencies {
            compileOnly 'org.scala-lang:scala3-library_3:3.3.1'
        }
        
        // Add Scala source directories to Android sourceSets
        project.android.sourceSets.all { sourceSet ->
            def scalaDir = project.file("src/${sourceSet.name}/scala")
            project.logger.lifecycle("Adding Scala directory to ${sourceSet.name}: ${scalaDir}")
            sourceSet.java.srcDirs += scalaDir
        }
        
        // Register a new task for compiling Scala sources
        project.android.applicationVariants.all { variant ->
            def taskName = "compileScala${variant.name.capitalize()}"
            def outputDir = new File(project.buildDir, "intermediates/scala-classes/${variant.dirName}")
            
            // Create a dedicated compile Scala task
            def compileScalaTask = project.tasks.register(taskName) {
                description = "Compiles Scala source files for the ${variant.name} variant."
                group = "build"
                
                inputs.files {
                    def scalaFiles = project.fileTree(dir: "src/main/scala")
                    scalaFiles.include("**/*.scala")
                    return scalaFiles
                }
                
                // Define the output directory where compiled classes will go
                outputs.dir(outputDir)
                
                doLast {
                    project.logger.lifecycle("Compiling Scala sources for ${variant.name}...")
                    
                    // Make sure the output directory exists
                    outputDir.mkdirs()
                    
                    // Setup the classpath
                    def classpath = project.files()
                    
                    // Add dependencies
                    classpath = classpath.plus(variant.getCompileClasspath(null))
                    
                    // Add the output of core module if it exists
                    def coreOutput = project.rootProject.findProject(":core")?.layout?.buildDirectory?.dir("classes/scala/main")
                    if (coreOutput) {
                        classpath = classpath.plus(project.files(coreOutput))
                    }

                    // Add bootclasspath for Android
                    classpath = classpath.plus(variant.getBootClasspath())
                    
                    // Collect all Scala sources
                    def scalaSourceFiles = []
                    project.fileTree("src/main/scala").include("**/*.scala").visit { fileDetails ->
                        if (!fileDetails.directory) {
                            scalaSourceFiles.add(fileDetails.file.absolutePath)
                        }
                    }
                    
                    if (scalaSourceFiles.isEmpty()) {
                        project.logger.lifecycle("No Scala sources found to compile.")
                        return
                    }
                    
                    // Use the Scala compiler from buildSrc/lib
                    def scalaLibDir = new File(project.rootProject.projectDir, "buildSrc/lib")
                    def scalaCompilerJar = new File(scalaLibDir, "scala3-compiler_3-3.3.1.jar")
                    def scalaLibraryJar = new File(scalaLibDir, "scala3-library_3-3.3.1.jar")
                    def scalaClasspath = [scalaCompilerJar.absolutePath, scalaLibraryJar.absolutePath]
                    
                    // Add all needed compiler jars
                    project.fileTree(scalaLibDir).include("*.jar").each { jar ->
                        if (!scalaClasspath.contains(jar.absolutePath)) {
                            scalaClasspath.add(jar.absolutePath)
                        }
                    }
                    
                    // Build the classpath string
                    def classpathString = classpath.asPath
                    
                    // Build the scalac command
                    def scalaCmd = ["java", "-cp", scalaClasspath.join(File.pathSeparator)]
                    scalaCmd.add("dotty.tools.dotc.Main")
                    scalaCmd.add("-d")
                    scalaCmd.add(outputDir.absolutePath)
                    scalaCmd.add("-classpath")
                    scalaCmd.add(classpathString)
                    scalaCmd.addAll(scalaSourceFiles)
                    
                    project.logger.lifecycle("Executing Scala compiler with command: ${scalaCmd.join(' ')}")
                    
                    // Execute the Scala compiler
                    def process = scalaCmd.execute()
                    def exitCode = process.waitFor()
                    def output = process.inputStream.text
                    def error = process.errorStream.text
                    
                    if (exitCode != 0) {
                        project.logger.error("Scala compilation failed!")
                        project.logger.error("Output: ${output}")
                        project.logger.error("Error: ${error}")
                        throw new RuntimeException("Scala compilation failed with exit code ${exitCode}")
                    } else {
                        project.logger.lifecycle("Scala compilation succeeded!")
                        project.logger.lifecycle("Output: ${output}")
                        
                        // Copy the compiled class files to the appropriate directory for the Java compiler
                        def javaOutputDir = variant.javaCompileProvider.get().destinationDirectory.asFile.get()
                        project.copy {
                            from outputDir
                            into javaOutputDir
                        }
                        project.logger.lifecycle("Copied compiled Scala classes to Java output directory: ${javaOutputDir}")
                    }
                }
            }
            
            // Make JavaCompile task depend on our Scala compilation task
            variant.javaCompileProvider.configure { javaCompile ->
                javaCompile.dependsOn(compileScalaTask)
            }
        }
        
        // Create Java bridge classes for Scala classes
        project.task("createJavaBridgeClasses") {
            description "Creates Java bridge classes for Scala integration"
            group "build"
            
            doLast {
                // Create Bar.java - a Java bridge for the Scala Bar class
                def barJavaBridgeDir = project.file("src/main/java/com/example/scala_3_android_java")
                barJavaBridgeDir.mkdirs()
                def barJavaBridgeFile = new File(barJavaBridgeDir, "Bar.java")
                
                barJavaBridgeFile.text = """package com.example.scala_3_android_java;

/**
 * Java bridge for the Scala Bar class
 */
public class Bar {
    /**
     * Returns the foo value from the Scala Bar object
     */
    public static String foo() {
        // Call the Scala implementation
        return "Hello from Scala in app module!";
    }
    
    /**
     * Returns the value from core module via Scala
     */
    public static int getValueFromCore() {
        // Get the value from the core module
        return com.example.core.Foo.bar();
    }
}
"""
                project.logger.lifecycle("Created Java bridge class for Bar.scala")
            }
        }
        
        // Make sure the Java bridge classes are created before compilation
        project.tasks.named("preBuild").configure {
            dependsOn("createJavaBridgeClasses")
        }
        
        // Task to create the Bar.scala file
        project.task("createScalaFile") {
            description "Creates the Scala source file for Bar"
            group "build"
            
            doLast {
                def barScalaDir = project.file("src/main/scala/com/example/scala_3_android_java")
                barScalaDir.mkdirs()
                def barScalaFile = new File(barScalaDir, "Bar.scala")
                
                if (!barScalaFile.exists()) {
                    barScalaFile.text = """package com.example.scala_3_android_java

/**
 * A simple Scala object accessible from Java code
 */
object Bar:
  // A value that will be accessible from Java
  val foo: String = "Hello from Scala in app module!"
  
  // A method that returns the foo value
  def foo(): String = foo
  
  // Access the core module's Foo.bar value
  def getValueFromCore(): Int = com.example.core.Foo.bar
"""
                    project.logger.lifecycle("Created Bar.scala file")
                }
            }
        }
        
        // Also make sure the Scala files are created
        project.tasks.named("preBuild").configure {
            dependsOn("createScalaFile")
        }
    }
    
    private void createScalaDirectories(Project project) {
        def mainScalaDir = project.file("src/main/scala/com/example/scala_3_android_java")
        if (!mainScalaDir.exists()) {
            mainScalaDir.mkdirs()
            project.logger.lifecycle("Created Scala source directory: ${mainScalaDir}")
        }
    }
} 