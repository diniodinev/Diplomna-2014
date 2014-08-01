package bg.uni.fmi.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.GradleException
import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType
import net.glxn.qrgen.vcard.VCard

public abstract class GenerateQRTask extends DefaultTask {

    @Input
    String outputEncoding = "UTF-8"

    @OutputDirectory
    def outputQRPath = new File('build/essays/QR')

    @Optional
    String pictureType = "PNG"

    @TaskAction
    def generate() {
          generatePicture()
       // generateVcard()
    }

    def generatePicture() {
    }

    def generateVcard() {
        VCard johnSpecial = new VCard("Jöhn Dɵe")
                .setAddress("ëåäöƞ Sträät 1, 1234 Döestüwn")
        File vCard = QRCode.from(johnSpecial).withCharset("UTF-8").file()
        project.copy {
            from vCard
            into "C:\\"
        }
    }

    def getOutputQRPath() {
        project.file(outputQRPath)
    }

    /**
     * Create QR picture
     * @param text which will be transformed in QR content
     * @param outputFile The file where the QR will be recorded
     */
    def createPicture(String text, File outputFile){
        ByteArrayOutputStream stream = QRCode.from(text).to(getImageType()).withCharset(outputEncoding).stream()
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile)
        stream.writeTo(fileOutputStream)
    }

    /**
     * Get method for property pictureType
     * @return PNG, JPG or GIF. If pictureType is not specified the default is PNG. IF other type is specified
     * GradleException will be thrown
     */
    def getImageType() {
        switch (pictureType.toUpperCase()) {
            case "PNG": ImageType.PNG
                break
            case "JPG": ImageType.JPG
                break
            case "GIF": ImageType.GIF
                break
            default:
                throw new GradleException('As pictureType you can specify only - PNG,JPG or GIF')
        }
    }
}
