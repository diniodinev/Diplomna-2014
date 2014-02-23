package bg.uni.fmi.plugins;

import org.gradle.api.*
import org.gradle.api.plugins.*
import bg.uni.fmi.tasks.DeleteSrcDirTask

class FMIJavaPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.getPlugins().apply(JavaPlugin.class);
        project.extensions.create("FN", ArtifactNameExtention)
        project.task('hello') << {
            println "Hello from the FMIPlugin"
        }

        project.task('deleteSrc',type: DeleteSrcDirTask)
    }
}