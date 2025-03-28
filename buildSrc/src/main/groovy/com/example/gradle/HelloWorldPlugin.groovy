package com.example.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

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
        
        // Only apply to the app module
        if (project.name == 'app') {
            project.afterEvaluate {
                // Find the core module and its Scala compilation task
                def coreProject = project.rootProject.findProject(':core')
                if (coreProject != null) {
                    // Get the compile task from the core module
                    def compileScalaTask = coreProject.tasks.findByName('compileScala')
                    
                    if (compileScalaTask != null) {
                        // Make the preBuild task depend on compileScala
                        project.tasks.named('preBuild').configure {
                            dependsOn(compileScalaTask)
                            doFirst {
                                println "Running Scala compilation as part of the app build process"
                            }
                        }
                    }
                }
            }
        }
    }
} 