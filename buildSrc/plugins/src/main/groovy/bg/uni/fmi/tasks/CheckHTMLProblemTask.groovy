package bg.uni.fmi.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import com.rockymadden.stringmetric.similarity.JaroMetric
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import org.cyberneko.html.parsers.SAXParser
import org.gradle.api.GradleException

import groovy.xml.XmlUtil


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

//                //println JaroMetric.compare(it.problemSolution,text)
//               println LevenshteinMetric.compare("a","aaaaaaaaaaaaaaa")

            //println text.contains(it.problemSolution)

//                Elements answerers = document.select("#answers .user-details a");
//                for (Element answerer : answerers) {
//                    System.out.println("Answerer: " + answerer.text());
//                }
        }
//            println "WWWWWW"
//            println it.problemSolution
//            println it.url
//            println url
//            println htmlBody

    }

}
