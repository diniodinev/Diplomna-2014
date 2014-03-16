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
import com.dropbox.core.DbxAuthInfo
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.util.IOUtil
import com.dropbox.core.DbxPath
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.DbxClient
import com.dropbox.core.DbxWriteMode
import com.dropbox.core.DbxException
import com.dropbox.core.DbxEntry
import java.io.InputStream
import java.util.Locale

import static bg.uni.fmi.plugins.FMIJavaPlugin.GET_INFO_TASK_NAME

public class UploadDropBoxTask extends DefaultTask {

    //Path to the file where is the token key
    @Input
    String authFile

    //where to upload the file
    @Input
    String dropboxPath

    //local path
    @Input
    String localPath

    @TaskAction
    def uploadDropBoxTask() {
        DbxAuthInfo autoInfo = readAuthInfoFille()
        checkDropboxUploadPath()
        uploadFile(autoInfo)
    }

    DbxAuthInfo readAuthInfoFille() {
        // Read auth info file.
        DbxAuthInfo authInfo
        try {
            authInfo = DbxAuthInfo.Reader.readFromFile(authFile)
        }
        catch (JsonReader.FileLoadException ex) {
            throw new org.gradle.api.GradleException("Error loading <auth-file>:" + ex.getMessage())
        }
        authInfo
    }

    def checkDropboxUploadPath() {
        String pathError = DbxPath.findError(dropboxPath);
        if (pathError != null) {
            throw new org.gradle.api.GradleException("Invalid <dropbox-path>: " + pathError);
        }
    }

    def uploadFile(DbxAuthInfo authInfo) {
        String userLocale = Locale.getDefault().toString();
        DbxRequestConfig requestConfig = new DbxRequestConfig("project-upload", userLocale);
        DbxClient dbxClient = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);

        // Make the API call to upload the file.
        DbxEntry.File metadata;
        String errorMessage="dadsad"
        try {
            InputStream input = new FileInputStream(localPath)
            try {
                metadata = dbxClient.uploadFile(dropboxPath, DbxWriteMode.add(), -1, input);
            } catch (DbxException ex) {
                throw new org.gradle.api.GradleException("Error uploading to Dropbox: " + ex.getMessage())
            } finally {
                IOUtil.closeInput(input);
            }
        }
        catch (IOException ex) {
            throw new org.gradle.api.GradleException("Error reading from file \"" + localPath + "\": " + ex.getMessage());
        }
    }

}