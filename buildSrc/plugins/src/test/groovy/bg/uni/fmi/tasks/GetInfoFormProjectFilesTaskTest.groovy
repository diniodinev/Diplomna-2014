package bg.uni.fmi.tasks

import org.gradle.api.GradleException;
import spock.lang.Specification
import spock.lang.Shared
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import static bg.uni.fmi.tasks.GetInfoFromProjectFilesTask.ARCHIVE_TYPE
import org.junit.Rule
import org.junit.rules.TemporaryFolder


public class GetInfoFromProjectFilesTaskTest extends Specification {
    //Create shared object for all future methods
    @Shared
    Project project
    @Shared
    GetInfoFromProjectFilesTask getInfoTask
    @Rule
    public TemporaryFolder folder = new TemporaryFolder()


    def setupSpec() {
        project = ProjectBuilder.builder().build()
        getInfoTask = project.tasks.create("getInfoTestTask", GetInfoFromProjectFilesTask)
    }

    def "check default values"() {
        expect:
        getInfoTask instanceof GetInfoFromProjectFilesTask
        getInfoTask.outputDir == "src"
        getInfoTask.courseName == null
        getInfoTask.sourceFiles == null

    }

    def "check checkTypeExtention()"() {
        when:
        //assert new File('').absolutePath+"/src/test/resources/bg/uni/fmi/tasks/UDP2012_M66666_FinalProject.zip"=="asd"
        //E:\DinioDinev\Magistratura\3 semestur\Diplomna rabota\Project\buildSrc\src\test\resources
        //getInfoTask.checkTypeExtention(project.file(new File('').absolutePath+"/src/test/resources/bg/uni/fmi/tasks/UDP2012_M66666_FinalProject.zip"),'zip')
        getInfoTask.checkTypeExtention(new File(''), 'zip')

        then:
        notThrown(Exception)

        when:
        getInfoTask.checkTypeExtention(new File(''), 'rar')
        then:
        GradleException e = thrown()
        assert e.message == "The file ${new File('').getAbsolutePath()} is with incompatible extension type \"rar\". The appropriate extension is \"${ARCHIVE_TYPE}\"."
    }

    def "check extractFiles()"() {
        given:
        File tempFolder = folder.newFolder("temp")
        getInfoTask.outputDir = tempFolder.absolutePath
        project.ext.conf = "default"

        when:
        getInfoTask.extractFiles(project.file(new File('').absolutePath + "/src/test/resources/bg/uni/fmi/tasks/UDP2012_M66666_FinalProject.zip"), 'zip')

        then:
        assert project.fileTree(tempFolder).find {
            it.name == 'Main.java'
        }.name == "Main.java"
    }

    def "check createSourceSets()"() {
        when:
        project.apply(plugin: 'fmi-java')
        getInfoTask.createSourceSets(project.file(new File('').absolutePath + "/plugins/src/test/resources/bg/uni/fmi/tasks/UDP2012_M66666_FinalProject.zip"), 'zip')
        Set s1 = ["${getInfoTask.outputDir}"+"UDP2012_M66666_FinalProject"]
        then:
        assert project.sourceSets.find{
            it.name=="UDP2012_M66666_FinalProject"
        }.name=="UDP2012_M66666_FinalProject"

        when:
        Set srcDirs = project.sourceSets.find{
            it.name=="UDP2012_M66666_FinalProject"
        }.java.srcDirs
        then:
        //Works only for one file in srcDirs Set
        assert srcDirs.join("")=="${getInfoTask.outputDir}"+"UDP2012_M66666_FinalProject"
    }
}
