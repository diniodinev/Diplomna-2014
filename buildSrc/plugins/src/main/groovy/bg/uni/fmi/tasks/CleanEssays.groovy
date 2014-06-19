package bg.uni.fmi.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.TaskAction

import static bg.uni.fmi.plugins.EssayPlugin.UNZIP_TASK_NAME

public class CleanEssays extends Delete {

    def filesToDelete = []
    def unzipTask = project.tasks.getByName(UNZIP_TASK_NAME)

    CleanEssays() {
        project.afterEvaluate {
            String outputPath = "${unzipTask.outputDir}"
            FileCollection filesTo = project.fileTree(outputPath).visit { FileVisitDetails details ->
                filesToDelete << details.file.path
            }
            filesToDelete << outputPath
            filesToDelete << "build"
        }
    }

    @TaskAction
    def cleanSrc() {
        setDelete(filesToDelete)
        clean()
    }
}
