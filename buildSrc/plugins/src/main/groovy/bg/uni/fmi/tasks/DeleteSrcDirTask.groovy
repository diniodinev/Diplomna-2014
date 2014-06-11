package bg.uni.fmi.tasks;

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Delete;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileVisitDetails

import static bg.uni.fmi.plugins.FMIJavaPlugin.GET_INFO_TASK_NAME

public class DeleteSrcDirTask extends Delete {

    private def filesToDelete = []
    private def getInfoTask = project.tasks.getByName(GET_INFO_TASK_NAME)

    DeleteSrcDirTask() {
        //We must configure filesToDelete in after evaluate phase because before that we do not have
        //courseName property
        project.afterEvaluate {
            String outputPath = "${getInfoTask.outputDir}"
            FileCollection filesTo = project.fileTree(outputPath).visit { FileVisitDetails details ->
                filesToDelete << details.file.path
            }
            filesToDelete << outputPath
        }
    }

    @TaskAction
    def cleanSrc() {
        setDelete(filesToDelete)
        clean()
    }
}