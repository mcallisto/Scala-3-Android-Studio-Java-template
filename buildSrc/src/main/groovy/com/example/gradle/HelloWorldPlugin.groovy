package com.example.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

class HelloWorldPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Register a 'hello' task
        project.task('hello') {
            description = 'A simple hello world Gradle plugin task'
            group = 'Sample'
            
            doFirst {
                project.logger.lifecycle("Executing hello task...")
            }
            
            doLast {
                project.logger.lifecycle("Hello from Gradle plugin! Running on ${project.name} module.")
                project.logger.lifecycle("Project path: ${project.path}")
                project.logger.lifecycle("Project directory: ${project.projectDir}")
            }
        }
        
        // Add a task to show basic project info
        project.task('projectInfo') {
            description = 'Shows basic information about the project'
            group = 'Sample'
            
            doFirst {
                project.logger.lifecycle("Executing projectInfo task...")
            }
            
            doLast {
                project.logger.lifecycle("Project name: ${project.name}")
                project.logger.lifecycle("Project version: ${project.version}")
                project.logger.lifecycle("Project directory: ${project.projectDir}")
                project.logger.lifecycle("Project buildDir: ${project.buildDir}")
                
                project.logger.lifecycle("\nProject dependencies:")
                try {
                    project.configurations.implementation.dependencies.each { dependency ->
                        project.logger.lifecycle("- ${dependency}")
                    }
                } catch (Exception e) {
                    project.logger.lifecycle("- Could not list dependencies: ${e.message}")
                }
            }
        }
        
        // Make sure the hello task runs during the build process
        if (project.name == 'app') {
            project.afterEvaluate {
                // Hook into Android build tasks - make our tasks run automatically
                def preBuildTask = project.tasks.findByName('preBuild')
                if (preBuildTask != null) {
                    preBuildTask.dependsOn 'hello'
                    project.logger.lifecycle("Added hello task as a dependency to preBuild")
                }
            }
        }
    }
} 