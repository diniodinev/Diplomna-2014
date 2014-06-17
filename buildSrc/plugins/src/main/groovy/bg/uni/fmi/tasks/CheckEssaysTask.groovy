package bg.uni.fmi.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Delete;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.util.ConfigureUtil
import org.jsoup.nodes.Document
import org.gradle.api.DefaultTask

import static bg.uni.fmi.plugins.EssayPlugin.UNZIP_TASK_NAME

public class CheckEssaysTask extends DefaultTask {
    @Input
    String inputEncoding

    @Input
    String outputEncoding

    @Input
    Integer wordLength

    @Input
    Integer sentanceLength

    @Input
    Integer wordsPerPage

    @Input
    Integer symbolsPerPage

    @Input
    FileCollection sources

    @TaskAction
    void checkEssays() {
        copyFiles()



        project.fileTree("${project.buildDir}/essays/").each { currectFile ->
            Document document = org.jsoup.Jsoup.parse(currectFile.getText(getInputEncoding()))
            document.outputSettings().charset(getOutputEncoding())
            project.file(currectFile).setText(document.body().text(), outputEncoding)

            Integer sentances = 0
            Integer allWords = 0

            currectFile.getText(outputEncoding).split("\\.").each {
                Integer words = 0
                it.split("\\s+").each {
                    if(it.length()>=wordLength){
                        words++
                        allWords++
                    }
                }
                if(words>=sentanceLength){
                    sentances++
                }
            }
            print "File ${currectFile.absolutePath} has " + currectFile.getText(outputEncoding).length() + " symbols " + "sentence= ${sentances}"
            print " and ${allWords} words. Pages based on symbols count ${currectFile.getText(outputEncoding).length()/symbolsPerPage} pages"
            println " , pages based on words count " +allWords/wordsPerPage

        }
    }

    def copyFiles() {
        new File("${project.buildDir}").mkdir()
        project.copy {
            from sources
            into "${project.buildDir}/essays/"
            rename { String fileName ->
                fileName.replace('html', 'txt')
            }
            includeEmptyDirs = false
        }
    }

}