package bg.uni.fmi.tasks

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import bg.uni.fmi.plugins.EssayPlugin
import spock.lang.Specification
import spock.lang.Shared
import net.glxn.qrgen.image.ImageType

public class GenerateQRFromTextTest extends Specification {
    @Shared
    Project project
    @Shared
    GenerateQRFromTextTask generateQRFromTextTask
    @Rule
    TemporaryFolder testFolder = new TemporaryFolder()

    def setupSpec() {

        project = ProjectBuilder.builder().build()
        generateQRFromTextTask = project.tasks.create(EssayPlugin.GENERATE_QR_FROM_TEXT_TASK_NAME, GenerateQRFromTextTask)
    }

    def 'checkDefaultImageType'() {
        expect:
        generateQRFromTextTask.getImageType() == ImageType.PNG
    }

    def 'checkImageTypes'() {
        when:
        generateQRFromTextTask.imageType = 'PNG'
        then:
        generateQRFromTextTask.getImageType() == ImageType.PNG

        when:
        generateQRFromTextTask.imageType = 'JPG'
        then:
        generateQRFromTextTask.getImageType() == ImageType.JPG

        when:
        generateQRFromTextTask.imageType = 'GIF'
        then:
        generateQRFromTextTask.getImageType() == ImageType.GIF

        when:
        generateQRFromTextTask.imageType = 'png'
        then:
        generateQRFromTextTask.getImageType() == ImageType.PNG

        when:
        generateQRFromTextTask.imageType = 'jpg'
        then:
        generateQRFromTextTask.getImageType() == ImageType.JPG

        when:
        generateQRFromTextTask.imageType = 'gif'
        then:
        generateQRFromTextTask.getImageType() == ImageType.GIF

        when:
        generateQRFromTextTask.imageType = 'bmp'
        generateQRFromTextTask.getImageType()
        then:
        GradleException e = thrown()
        assert e.message == 'As imageType you can specify only - PNG,JPG or GIF'


    }

    def 'checkDefaultOutputEncoding'() {
        expect:
        generateQRFromTextTask.outputEncoding == 'UTF-8'
    }

    def 'checkDefaultOutputQRPath'() {
        expect:
        generateQRFromTextTask.outputQRPath == project.file('build/essays/QR')
    }

    def 'change default outputQRPath dir'() {
        when:
        generateQRFromTextTask.outputQRPath = new File(project.rootProject.buildDir, "newPath")
        then:
        generateQRFromTextTask.getOutputQRPath() instanceof File
        generateQRFromTextTask.getOutputQRPath().isDirectory() == false
        generateQRFromTextTask.getOutputQRPath() == new File(project.rootProject.buildDir, "newPath")
    }

    def 'create Picture'() {
        given:
        String text = "Група 2  задачи. Problems"
        String fileName = "Group2_QR_Info"
        generateQRFromTextTask.imageType = "PNG"
        generateQRFromTextTask.pictureHeight = 125
        generateQRFromTextTask.pictureWidth = 125
        ImageType imageType = generateQRFromTextTask.getImageType()
        File qrFile = testFolder.newFile("${fileName}.${generateQRFromTextTask.imageType}")
        File testImage = project.file(new File('').absolutePath + "/src/test/resources/bg/uni/fmi/tasks/Image.PNG")
        when:
        generateQRFromTextTask.createPicture(text, qrFile)
        then:
        qrFile.exists() == true
        qrFile.length() == testImage.length()
        compareFiles(qrFile, testImage)
    }

    def 'check outputPath'() {
        when:
            def outputPath = generateQRFromTextTask.getOutputQRPath()
        then:
            assert  outputPath instanceof File
    }
    /**
     * Compare two files
     * @param first The first file which will be compared
     * @param second The second file which will be compared
     * @return
     */
    boolean compareFiles(File first, File second) {
        boolean flag = true
        FileReader fR1 = new FileReader(first)
        FileReader fR2 = new FileReader(second)

        BufferedReader reader1 = new BufferedReader(fR1)
        BufferedReader reader2 = new BufferedReader(fR2)

        String line1 = null
        String line2 = null

        while (((line1 = reader1.readLine()) != null) && ((line2 = reader2.readLine()) != null)) {
            if (!line1.equals(line2)) {
                flag = false;
                break
            }
        }
        return flag
    }
}