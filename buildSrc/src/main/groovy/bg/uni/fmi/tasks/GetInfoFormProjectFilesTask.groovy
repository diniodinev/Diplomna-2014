package bg.uni.fmi.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.apache.commons.io.FilenameUtils
import org.gradle.api.GradleException;

public class GetInfoFromProjectFilesTask extends DefaultTask {
    static final String ARCHIVE_TYPE = 'zip'
    private FileCollection sourceFilesOnly

    @Input
    FileCollection sourceFiles

    @Input
    @Optional
    String outputDir = "src"

    @Input
    @Optional
    String courseName

    def GetInfoFromProjectFilesTask() {
        project.afterEvaluate {
            if (outputDir.equals('src')) {
                outputDir = "${outputDir}/${courseName}/"
            }
            sourceFiles.each {
                if (it.isDirectory()) {
                    FileTree allZips = project.fileTree(it)
                    allZips.each { sourceName ->
                        checkTypeExtention(sourceName, FilenameUtils.getExtension(sourceName.getName()))
                        createSourceSets(sourceName, ARCHIVE_TYPE)
                    }
                } else {
                    checkTypeExtention(sourceName, FilenameUtils.getExtension(sourceName.getName()))
                    createSourceSets(sourceName, ARCHIVE_TYPE)
                }
            }
        }
    }

    @TaskAction
    def getInfoFromProjectFiles() {

//        //TODO Check if there is applyed ArtifactNameExtention on the project. TO be sure that FN extention exists
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

    def checkTypeExtention(File file, String extension) {
        if (!extension.equals(ARCHIVE_TYPE)) {
            throw new GradleException("The file ${file.getAbsolutePath()} is with incompatible extension type \"${extension}\". The appropriate " +
                    "extension is \"${ARCHIVE_TYPE}\". ")
        }
    }

    def extractFiles(File file, String extension) {
        if (courseName == null) {
            courseName = project.ext.conf
        }
        project.copy {
            from project.zipTree(file.getAbsolutePath())
            into "${outputDir}/${file.getName()}" - ".${extension}"
        }

    }

    def createSourceSets(File file, String extension) {
        project.sourceSets.create("${file.getName()}" - ".${extension}".capitalize()) {
            java{
                srcDirs = project.files("${outputDir}${file.getName()}" - ".${extension}")
            }
        }
    }
}