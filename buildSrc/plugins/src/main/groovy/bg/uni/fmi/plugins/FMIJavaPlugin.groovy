package bg.uni.fmi.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import bg.uni.fmi.tasks.DeleteSrcDirTask
import bg.uni.fmi.tasks.GetInfoFromProjectFilesTask
import bg.uni.fmi.tasks.UploadDropBoxTask
import bg.uni.fmi.tasks.SendEmailsTask
import org.gradle.api.plugins.JavaPlugin


class FMIJavaPlugin implements Plugin<Project> {
    public static final String DELETE_SRC_TASK = "deleteSrc"
    public static final String GET_INFO_TASK_NAME = "getInfo"
    public static final String UPLOAD_DROPBOX_FILES = "uploadDropbox"
    public static final String SEND_EAMIL = "sendEmail"
    public static final String FMI_PLUGIN_EXTENSION_NAME = "FN"

    void apply(Project project) {
        project.getPlugins().apply(JavaPlugin.class);
        ArtifactNameExtension extension = project.extensions.create(FMI_PLUGIN_EXTENSION_NAME, ArtifactNameExtension, project)

        GetInfoFromProjectFilesTask getInfoTask = project.task(GET_INFO_TASK_NAME, type: GetInfoFromProjectFilesTask)
        getInfoTask.setDescription('Get information for archive files which will be used for analysis.')
        getInfoTask.setGroup('analysis')

        DeleteSrcDirTask deleteSrcDirTask = project.task(DELETE_SRC_TASK, type: DeleteSrcDirTask)
        deleteSrcDirTask.setDescription('Delete output dir for the project.')
        deleteSrcDirTask.setGroup('analysis')

        UploadDropBoxTask uploadDropBoxTask = project.task(UPLOAD_DROPBOX_FILES, type: UploadDropBoxTask)
        uploadDropBoxTask.setDescription('Upload files to DropBox account.')
        uploadDropBoxTask.setGroup('upload')

        SendEmailsTask sendEmailsTask = project.task(SEND_EAMIL, type: SendEmailsTask)
        uploadDropBoxTask.setDescription('Send email.')
        uploadDropBoxTask.setGroup('upload')
    }
}
