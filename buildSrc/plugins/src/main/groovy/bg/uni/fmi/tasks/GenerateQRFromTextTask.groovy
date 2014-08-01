package bg.uni.fmi.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class GenerateQRFromTextTask extends GenerateQRTask {
    /**
     * [text in QR,QR fileName]
     */
    @Input
    Map<String, String> QRInfo

    @TaskAction
    def generate() {
        super.generate()

    }

    @Override
    def generatePicture() {
        QRInfo.each { text, fileName ->
            createPicture(text, new File(getOutputQRPath(), "${fileName + '.' + getImageType()}"))
        }
    }
}
