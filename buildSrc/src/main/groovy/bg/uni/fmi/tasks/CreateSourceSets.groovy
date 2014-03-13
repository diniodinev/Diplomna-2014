package bg.uni.fmi.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Delete;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.file.FileVisitDetails

import static bg.uni.fmi.plugins.FMIJavaPlugin.GET_INFO_TASK_NAME

public class CreateSourceSets extends DefaultTask {

    @InputFiles


    @TaskAction
    def createSourceSets() {
        FileTree allZips = fileTree("src/main/resources")
        allZips.each { sourceName ->
            def fileInfo = getInfoFromProjectName(sourceName.name - '.zip')
            //Create New Source Set
            if (fileInfo.size() == 4) {
                sourceSets.create(fileInfo[0].capitalize()) {
                    //       output="src/build/classes/${fileInfo[1]}/${fileInfo[2]}"
                }
                //Do copy later than delete frim cleanSrc task
                doLast {
                    project.copy {
                        from zipTree(sourceName.getAbsolutePath())
                        into "src/${fileInfo[0].capitalize()}/java/"
                    }
                }
            }
        }
    }

}