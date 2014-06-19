package bg.uni.fmi.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.apache.commons.io.FilenameUtils

public class UnzipFilesTask extends DefaultTask {
    static final String ARCHIVE_TYPE = 'zip'

    @Input
    FileCollection sourceFiles

    @OutputDirectory
    @Optional
    File outputDir = project.file("src")

    @Input
    @Optional
    String courseName


    def UnzipFilesTask() {
        project.afterEvaluate {
            if (courseName == null) {
                courseName = project.ext.conf
            }
            if (outputDir.equals('src')) {
                outputDir = "${outputDir}/${courseName}/"
            }
            sourceFiles.each {
                if (it.isDirectory()) {
                    FileTree allZips = project.fileTree(it)
                    allZips.each { sourceName ->
                        checkTypeExtention(sourceName, FilenameUtils.getExtension(sourceName.getName()))
                    }
                } else {
                    checkTypeExtention(it, FilenameUtils.getExtension(it.getName()))
                }
            }
        }
    }

    def checkTypeExtention(File file, String extension) {
        if (!extension.equals(ARCHIVE_TYPE)) {
            throw new GradleException("The file ${file.getAbsolutePath()} is with incompatible extension type \"${extension}\". The appropriate " +
                    "extension is \"${ARCHIVE_TYPE}\".")
        }
    }

    @TaskAction
    def getInfoFromProjectFiles() {
        sourceFiles.each {
            if (it.isDirectory()) {
                FileTree allZips = project.fileTree(it)
                allZips.each { sourceName ->
                    processingFiles(sourceName)
                }
            } else {
                processingFiles(it)
            }
        }

    }

    def processingFiles(File file) {
        extractFiles(file, ARCHIVE_TYPE)
    }

    def extractFiles(File file, String extension) {
        project.copy {
            from project.zipTree(file.getAbsolutePath())
            into "${outputDir.absolutePath}/${file.getName()}" - ".${extension}"
        }
    }

}