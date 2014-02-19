package bg.uni.fmi.plugins;

import org.gradle.api.*
import org.gradle.api.plugins.*

class FMIJavaPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('hello') << {
            println "Hello from the FMIPlugin"
        }
    }
}