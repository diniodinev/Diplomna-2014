package bg.uni.fmi.tasks

import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.cyberneko.html.parsers.SAXParser

class CheckHTMLProblemTask extends DefaultTask {
    @Input
    @Optional
    String url

    @TaskAction
    def checkProblems() {
        project.extensions.problems.each {
            boolean isSolutionFound
            def page
            try {
                if (it.url != null) {
                    page = new XmlSlurper(new SAXParser()).parse(it.url)
                } else {
                    page = new XmlSlurper(new SAXParser()).parse(url)
                }
            } catch (NullPointerException ex) {
                throw new GradleException("The 'url' property of the ${it.name} problem has to be specified. Or" +
                        "'checkProblems' property url of checkProblems task.")
            }
            String htmlBody = XmlUtil.serialize(page.BODY)

            isSolutionFound = (htmlBody.indexOf(it.problemSolution) == -1) ? false : true

            println isSolutionFound
        }
    }
}

