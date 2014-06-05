package bg.uni.fmi.plugins;

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.plugins.*
import bg.uni.fmi.tasks.CheckHTMLProblemTask

class WWWPlugin implements Plugin<Project> {
    static final String PLUGIN_EXTENSION_NAME = 'problems'
    static final String CHECK_PROBLEMS_TASK = 'checkProblems'

    void apply(Project project) {

        WWWPluginExtension extention = project.extensions.add(PLUGIN_EXTENSION_NAME, project.container(WWWPluginExtension))

        CheckHTMLProblemTask checkHTMLProblemTask = project.task(CHECK_PROBLEMS_TASK, type: CheckHTMLProblemTask)
        checkHTMLProblemTask.setDescription('Check given URL HTML for specific problemSolution')
        checkHTMLProblemTask.setGroup('problemsChecking')
    }
}