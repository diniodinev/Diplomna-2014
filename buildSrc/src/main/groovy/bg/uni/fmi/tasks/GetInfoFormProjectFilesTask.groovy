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

    @Input
    FileCollection sourceFiles

    @Input
    @Optional
    String outputDir = "src"

    @Input
    @Optional
    String courseName


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
        checkTypeExtention(file)
        extractFiles(file)
    }

    def checkTypeExtention(File file) {
        String extension = FilenameUtils.getExtension(file.getName())
        if (!extension.equals(ARCHIVE_TYPE)) {
            throw new GradleException("The file ${file.getAbsolutePath()} is with incompatible extension type \"${extension}\". The appropriate " +
                    "extension is \"${ARCHIVE_TYPE}\". ")
        }
    }

    def extractFiles(File file) {
        if (courseName == null) {
            courseName = project.ext.conf
        }
        project.copy {
            from project.zipTree(file.getAbsolutePath())
            into "${outputDir}/${courseName}/${file.getName()}/java/"
        }
    }
//def getInfoFromProjectFiles(ArtifactNameExtention extention) {
//        def nameOfFile = /NetJava2012_M24133_FinalProject/
//        def matcher = nameOfFile =~ extention.filePattern
//        if (matcher.matches()) {
//            matcher.findResult { fullName, courseName, faqultyNumber, projectName ->
//                [nameOfFile, courseName, faqultyNumber, projectName]
//            }
//        } else {
//            [nameOfFile]
//        }
//    }
}