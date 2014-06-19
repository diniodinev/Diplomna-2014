package bg.uni.fmi.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import bg.uni.fmi.tasks.UnzipFilesTask
import bg.uni.fmi.tasks.CheckEssaysTask
import bg.uni.fmi.tasks.CleanEssays

class EssayPlugin implements Plugin<Project> {
    public static final String UNZIP_TASK_NAME = "unzip"
    public static final String CHECK_ESSAY_TASK_NAME = "essaysCheck"
    public static final String CLEAN_ESSAY_TASK_NAME = "essaysClean"

    void apply(Project project) {
        UnzipFilesTask getInfoTask = project.task(UNZIP_TASK_NAME, type: UnzipFilesTask)
        getInfoTask.setDescription('Unzip specified archives.')
        getInfoTask.setGroup('essay')


        CheckEssaysTask checkEssaysTask = project.task(CHECK_ESSAY_TASK_NAME, type: CheckEssaysTask)
        checkEssaysTask.setDescription('Check essays.')
        checkEssaysTask.setGroup('essay')

        CleanEssays cleanEssaysTask = project.task(CLEAN_ESSAY_TASK_NAME, type: CleanEssays)
        cleanEssaysTask.setDescription('Clean essays output.')
        cleanEssaysTask.setGroup('essay')
    }
}

