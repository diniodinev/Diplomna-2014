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
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

public class UploadDropBoxTask extends DefaultTask {

    String DATE_PATTERN_FOR_DIRECTORIES = "yyyy-MM-dd-kk-mm-ss"
    //Path to the file where is the token key
    @Input
    String authFile

    //what the name of file would be on dropbox server
    @Input
    String dropboxName

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
        String pathError = DbxPath.findError(generateDropboxPath());
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
        try {
            InputStream input = new FileInputStream(localPath)
            try {
                metadata = dbxClient.uploadFile(generateDropboxPath(), DbxWriteMode.add(), -1, input);
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

    /**
     * Generate path for the directory in DropBox. If the name of course is specified then the directory will be
     * courseName/date/dropboxName
     * Were date will be in format yyyy-MM-dd-kk-mm-ss
     * @return generated path for file in DropBox
     */
    String generateDropboxPath() {
        StringBuilder dirName = new StringBuilder();

        dirName.append('/')
        String course = project.tasks.getByName("getInfo").courseName
        if (course!=null) {
            dirName.append("${course}/")
        }

        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_PATTERN_FOR_DIRECTORIES);
        dirName.append(fmt.print(dt));
        dirName.append('/')
        dirName.append(dropboxName)
        dirName.toString()
    }

}