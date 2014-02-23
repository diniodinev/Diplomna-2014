package bg.uni.fmi.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree

public class DeleteSrcDirTask extends DefaultTask {
    @InputFiles @Optional
    FileCollection filesToDelete = project.fileTree(/src/).matching {
        exclude "main", "test"
    }

    @TaskAction
    def cleanSrc() {
        filesToDelete.each {
            project.delete(it)
        }
    }
}