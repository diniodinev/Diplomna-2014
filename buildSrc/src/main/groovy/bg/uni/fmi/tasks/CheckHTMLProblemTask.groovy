package bg.uni.fmi.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import com.rockymadden.stringmetric.similarity.JaroMetric
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import org.cyberneko.html.parsers.SAXParser

import groovy.xml.XmlUtil


class CheckHTMLProblemTask extends DefaultTask {
    @Input @Optional
    String url

    @TaskAction
    def checkProblems(){
        project.extensions.problems.each{
            boolean isSolutionFound
            def page = new XmlSlurper(new SAXParser()).parse(url)
            //println page.BODY[0].P[0].text()
            String htmlBody = XmlUtil.serialize(page.BODY)

            //If the problem has it's own URL, it takes advantage over the url defined for the extention
            if(it.url==null) {
                isSolutionFound = (htmlBody.indexOf(it.problemSolution)==-1)?false:true
              println isSolutionFound

//                println it.problemSolution


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

}