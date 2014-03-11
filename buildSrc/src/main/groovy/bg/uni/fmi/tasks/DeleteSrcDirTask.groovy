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

import static bg.uni.fmi.plugins.FMIJavaPlugin.GET_INFO_TASK_NAME

public class DeleteSrcDirTask extends Delete {
    @InputFiles
    @Optional
    FileCollection filesToDelete = project.fileTree(project.tasks.getByName(GET_INFO_TASK_NAME).outputDir).matching {
        exclude "main", "test"
    }

    @TaskAction
    def cleanSrc() {
        filesToDelete.each{
            println it.absolutePath
        }
        setDelete(filesToDelete)
        clean()
    }
}