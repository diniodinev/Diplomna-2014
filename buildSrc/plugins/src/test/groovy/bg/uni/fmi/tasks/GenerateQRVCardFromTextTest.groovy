package bg.uni.fmi.tasks

import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Shared
import org.gradle.api.Project
import net.glxn.qrgen.vcard.VCard

public class GenerateQRVCardFromTextTest extends Specification {

    @Shared
    Project project
    @Shared
    GenerateQRVCardFromTextTask vCardTask
    @Rule
    public TemporaryFolder folder = new TemporaryFolder()


    def setupSpec() {
        project = ProjectBuilder.builder().build()
        vCardTask = project.tasks.create("generateQRVcardFromText", GenerateQRVCardFromTextTask)
    }

    def 'check default vCardFileName'() {
        when:
        vCardTask.personName = "Ivan Petrov"
        then:
        vCardTask.vCardFileName == "vCardQR"
        vCardTask.email == null
        vCardTask.address == null
        vCardTask.title == null
        vCardTask.company == null
        vCardTask.phonenumber == null
        vCardTask.website == null
        vCardTask.personName == "Ivan Petrov"
    }

    def 'check vCardFileName'() {
        given:
        vCardTask.email = 'some@gmail.com'
        vCardTask.address = 'ulitza kokiche'
        vCardTask.title = 'Gospodin'
        vCardTask.company = 'FMI'
        vCardTask.phonenumber = '0888-88-88-88'
        vCardTask.website = 'example.com'
        when:
        def defaultName = vCardTask.fillVcard()
        then:
        defaultName instanceof VCard
        defaultName !=null
        defaultName.email == "some@gmail.com"
        defaultName.address == 'ulitza kokiche'
        defaultName.title == 'Gospodin'
        defaultName.company == 'FMI'
        defaultName.phonenumber == '0888-88-88-88'
        defaultName.website == 'example.com'
    }
}