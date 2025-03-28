package com.example.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.scala.ScalaCompile
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.LogLevel

class HelloWorldPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Register a 'hello' task
        project.task('hello') {
            description = 'A simple hello world Gradle plugin task'
            group = 'Sample'
            
            doLast {
                println "Hello from Gradle plugin! Running on ${project.name} module."
                println "Project path: ${project.path}"
                println "Project directory: ${project.projectDir}"
            }
        }
        
        // Add a task to show basic project info
        project.task('projectInfo') {
            description = 'Shows basic information about the project'
            group = 'Sample'
            
            doLast {
                println "Project name: ${project.name}"
                println "Project version: ${project.version}"
                println "Project directory: ${project.projectDir}"
                println "Project buildDir: ${project.buildDir}"
                
                println "\nProject dependencies:"
                try {
                    project.configurations.implementation.dependencies.each { dependency ->
                        println "- ${dependency}"
                    }
                } catch (Exception e) {
                    println "- Could not list dependencies: ${e.message}"
                }
            }
        }
        
        // Only apply Scala compilation to the app module
        if (project.name == 'app') {
            project.logger.lifecycle("Applying Scala compilation to app module")
            
            // Create Scala source directories
            def scalaSourceDir = project.file("${project.projectDir}/src/main/scala")
            if (!scalaSourceDir.exists()) {
                scalaSourceDir.mkdirs()
                project.logger.lifecycle("Created Scala source directory: ${scalaSourceDir}")
            }
            
            // Register a task to compile Scala in the app module
            project.task('compileAppScala', type: ScalaCompile) {
                description = 'Compile Scala source code in the app module'
                group = 'Build'
                
                // Configure source and include pattern
                source = project.fileTree(dir: scalaSourceDir, include: '**/*.scala')
                
                // Set classpath using Android configurations
                project.afterEvaluate {
                    // We need to wait for the Android plugin to configure the project
                    classpath = project.files(
                        project.android.bootClasspath,
                        project.configurations.implementation
                    )
                    
                    // Set the destination directory for compiled classes
                    destinationDirectory = project.file("${project.buildDir}/intermediates/scala/debug")
                }
                
                // Scala compiler options
                scalaCompileOptions.with {
                    encoding = 'UTF-8'
                    additionalParameters = ['-feature']
                    fork = true
                }
                
                doFirst {
                    project.logger.lifecycle("==================================")
                    project.logger.lifecycle("| Compiling Scala code in app module |")
                    project.logger.lifecycle("==================================")
                    project.logger.lifecycle("Source directory: ${scalaSourceDir}")
                    
                    // Make sure output directory exists
                    destinationDirectory.get().asFile.mkdirs()
                }
                
                doLast {
                    project.logger.lifecycle("==================================")
                    project.logger.lifecycle("| App module Scala compilation completed! |")
                    project.logger.lifecycle("==================================")
                }
            }
            
            // Ensure our task is visible in the standard build logs
            project.gradle.addListener(new org.gradle.api.execution.TaskExecutionListener() {
                void beforeExecute(org.gradle.api.tasks.TaskState state) {
                    if (state.name == 'compileAppScala') {
                        project.logger.lifecycle("Starting compileAppScala task...")
                    }
                }
                
                void afterExecute(org.gradle.api.tasks.TaskState state, def result) {
                    if (state.name == 'compileAppScala') {
                        project.logger.lifecycle("Finished compileAppScala task.")
                    }
                }
            })
            
            // Make sure the compileAppScala task runs during the build process
            project.afterEvaluate {
                // Hook into Android build tasks
                def preBuildTask = project.tasks.findByName('preBuild')
                if (preBuildTask != null) {
                    preBuildTask.dependsOn 'compileAppScala'
                    project.logger.lifecycle("Added compileAppScala task as a dependency to preBuild")
                }
                
                // Hook into Java compilation to ensure Scala classes are available
                def compileDebugJavaWithJavac = project.tasks.findByName('compileDebugJavaWithJavac')
                if (compileDebugJavaWithJavac != null) {
                    compileDebugJavaWithJavac.dependsOn 'compileAppScala'
                    
                    // Add the Scala output to the Java classpath
                    def scalaOutputDir = project.file("${project.buildDir}/intermediates/scala/debug")
                    compileDebugJavaWithJavac.doFirst {
                        if (compileDebugJavaWithJavac.classpath != null) {
                            compileDebugJavaWithJavac.classpath = compileDebugJavaWithJavac.classpath + project.files(scalaOutputDir)
                            project.logger.lifecycle("Added Scala output directory to Java classpath")
                        }
                    }
                }
                
                // Make the task show up in all gradle builds
                project.gradle.taskGraph.whenReady { graph ->
                    if (graph.hasTask(':app:assembleDebug') || graph.hasTask(':app:build')) {
                        project.logger.lifecycle("Build includes app module - Scala compilation is enabled")
                    }
                }
            }
        }
    }
} 