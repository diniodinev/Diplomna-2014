package bg.uni.fmi.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import bg.uni.fmi.tasks.CheckHTMLProblemTask
import bg.uni.fmi.tasks.UnzipFilesTask
import bg.uni.fmi.tasks.CheckEssaysTask

class EssayPlugin implements Plugin<Project> {
    public static final String UNZIP_TASK_NAME = "unzip"
    public static final String CHECK_ESSAY_TASK_NAME = "essaysCheck"


    void apply(Project project) {
        UnzipFilesTask getInfoTask = project.task(UNZIP_TASK_NAME, type: UnzipFilesTask)
        getInfoTask.setDescription('Get information for archive files which will be used for analysis.')
        getInfoTask.setGroup('analysis')


        CheckEssaysTask checkEssaysTask = project.task(CHECK_ESSAY_TASK_NAME, type: CheckEssaysTask)
        getInfoTask.setDescription('Check essays.')
        getInfoTask.setGroup('analysis')
    }



}
