package bg.uni.fmi.tasks

import groovy.xml.StreamingMarkupBuilder
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

public class CheckEssaysTask extends SourceTask {
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

    def reportPath
    @Input
    @Optional
    String getReportPath() {
        if (reportPath == null) {

            return "${project.buildDir}/reports/essays/"
        }
        return project.file(reportPath).getPath()
    }

    private File report = new File("${project.buildDir}/reports/essays/", 'reportEssay.xml')
    private File outPutHTML = new File(getReportPath() , "reportEssay.html")

    @TaskAction
    void checkEssays() {
        createReport()
        copyFiles()
        checkFiles()
        transform()
    }

    def createReport() {
        new File("${project.buildDir}/reports/essays/").mkdirs()
        report.createNewFile()
        report.setText("""<?xml version="1.0" encoding="UTF-8"?>
<report>
</report>""")
    }

    def copyFiles() {
        project.copy {
            from getSource()
            into "${project.buildDir}/essays/works"
            include '**/*.html'
            rename { String fileName ->
                fileName.replace('html', 'txt')
            }
            includeEmptyDirs = false
        }
    }

    def checkFiles() {
        project.fileTree("${project.buildDir}/essays/works").each { currectFile ->
            Document document = org.jsoup.Jsoup.parse(currectFile.getText(getInputEncoding()), "", Parser.xmlParser())
            document.outputSettings().charset(getOutputEncoding())
            project.file(currectFile).setText(document.body().text(), outputEncoding)

            Integer sentances = 0
            Integer allWords = 0

            currectFile.getText(outputEncoding).split("\\.").each {
                Integer words = 0
                it.split("\\s+").each {
                    if (it.length() >= wordLength) {
                        words++
                        allWords++
                    }
                }
                if (words >= sentanceLength) {
                    sentances++
                }
            }
            addFile(currectFile.absolutePath, currectFile.getText(outputEncoding).length(), sentances, allWords)
        }
    }

    def addFile(String fileElementName, Integer symbols, Integer sentances, Integer allWords) {
        def root = new XmlSlurper().parse(report)
        root.appendNode {
            file(name: fileElementName) {
                info(symbols: symbols, sentances: sentances, allWords: allWords, pagesBySymbols: (symbols / symbolsPerPage), pagesByWords: (allWords / wordsPerPage))
            }
        }

        def outputBuilder = new StreamingMarkupBuilder()
        String result = outputBuilder.bind { mkp.yield root }
        report.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + result)
    }

    def transform() {
        outPutHTML.createNewFile()
        def factory = TransformerFactory.newInstance()
        def transformer = factory.newTransformer(new StreamSource(new StringReader(project.file("src/main/xslt/essayReport.xslt").getText())))
        transformer.transform(new StreamSource(new StringReader(report.getText())), new StreamResult(new FileOutputStream(outPutHTML)))
        logger.quiet "The essays report is generated in ${outPutHTML.absolutePath}"
    }
}

