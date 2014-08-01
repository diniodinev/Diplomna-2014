package bg.uni.fmi.tasks

import org.apache.commons.io.FilenameUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType
import net.glxn.qrgen.vcard.VCard

public class GenerateQRFromFilesTask extends GenerateQRTask {
    @Input
    @Optional
    String inputEncoding = "UTF-8"

    @InputFiles
    FileCollection source


    @Override
    def generatePicture() {
        source.each {currentFile ->
            createPicture(currentFile.getText(inputEncoding),new File(getOutputQRPath(),"${FilenameUtils.removeExtension(currentFile.getName())+'.'+getImageType()}"))
        }
    }

    @Override
    def generateVcard() {
        VCard johnSpecial = new VCard("Jöhn Dɵe")
                .setAddress("Диньо ëåäöƞ Sträät 1, 1234 Döestüwn");
        File vCard = QRCode.from(johnSpecial).withCharset("UTF-8").file();
        project.copy {
            from vCard
            into "C:\\Users\\Cannibal\\Desktop\\"
        }
    }

    def getImageType() {
        switch(pictureType.toUpperCase()){
            case "PNG": ImageType.PNG
                break
            case "JPG": ImageType.JPG
                break
            case "GIF":ImageType.GIF
                break
            default:
                throw GradleExeption("As pictureType you can specify only: PNG,JPG or GIF")
        }
    }
}