package bg.uni.fmi.tasks

import org.apache.commons.io.FilenameUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import net.glxn.qrgen.image.ImageType

public class GenerateQRFromFilesTask extends GenerateQRTask {
    @Input
    @Optional
    String inputEncoding = "UTF-8"

    @InputFiles
    FileCollection source


    @Override
    def generatePicture() {
        source.each { currentFile ->
            createPicture(currentFile.getText(inputEncoding), new File(getOutputQRPath(), "${FilenameUtils.removeExtension(currentFile.getName()) + '.' + getImageType()}"))
        }
    }
}