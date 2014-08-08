package bg.uni.fmi.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.GradleException
import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType


public abstract class GenerateQRTask extends DefaultTask {

    @Input
    String outputEncoding = "UTF-8"

    @OutputDirectory
    def outputQRPath = new File('build/essays/QR/')

    @Optional
    String imageType = "PNG"

    @Input
    Integer pictureHeight = 100

    @Input
    Integer pictureWidth = 100

    @TaskAction
    def generate() {
          generatePicture()
    }

    abstract def generatePicture();

    File getOutputQRPath() {
        project.file(outputQRPath)
    }

    /**
     * Create QR picture
     * @param text which will be transformed in QR content
     * @param outputFile The file where the QR will be recorded
     */
    def createPicture(def text, File outputFile){
        ByteArrayOutputStream stream = QRCode.from(text).withSize(pictureHeight, pictureWidth).to(getImageType()).withCharset(outputEncoding).stream()
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile)
        stream.writeTo(fileOutputStream)
    }

    /**
     * Get method for property imageType
     * @return PNG, JPG or GIF. If imageType is not specified the default is PNG. IF other type is specified
     * GradleException will be thrown
     */
    def getImageType() {
        switch (imageType.toUpperCase()) {
            case "PNG": ImageType.PNG
                break
            case "JPG": ImageType.JPG
                break
            case "GIF": ImageType.GIF
                break
            default:
                throw new GradleException('As imageType you can specify only - PNG,JPG or GIF')
        }
    }
}
