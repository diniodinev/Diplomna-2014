package bg.uni.fmi.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Delete;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.file.FileVisitDetails

import static bg.uni.fmi.plugins.FMIJavaPlugin.GET_INFO_TASK_NAME

public class DeleteSrcDirTask extends Delete {

    @InputFiles
    def filesToDelete = []
    def getInfoTask = project.tasks.getByName(GET_INFO_TASK_NAME)

    DeleteSrcDirTask() {
        //We must configure filesToDelete in after evaluate phase because becore that we do not have
        //courseName property
        project.afterEvaluate {
            String outputPath = "${getInfoTask.outputDir}/${getInfoTask.courseName}"
            FileCollection filesTo = project.fileTree(outputPath).visit { FileVisitDetails details ->
                filesToDelete << details.file.path
                println details.file.path
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