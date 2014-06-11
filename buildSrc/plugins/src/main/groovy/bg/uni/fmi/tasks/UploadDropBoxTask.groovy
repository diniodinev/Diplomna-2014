package bg.uni.fmi.tasks;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import com.dropbox.core.DbxAuthInfo
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.util.IOUtil
import com.dropbox.core.DbxPath
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.DbxClient
import com.dropbox.core.DbxWriteMode
import com.dropbox.core.DbxException
import com.dropbox.core.DbxEntry
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

public class UploadDropBoxTask extends DefaultTask {

    private final static String DATE_PATTERN_FOR_DIRECTORIES = "yyyy-MM-dd-kk-mm-ss"
    //Path to the file where is the token key
    @Input
    String authFile

    //local path
    @Input
    def localFilesToUpload

    @TaskAction
    def uploadDropBoxTask() {
        DbxAuthInfo autoInfo = readAuthInfoFille()
        uploadFiles(autoInfo)
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

    def checkDropboxUploadPath(def fileNameOnDropbox) {
        String pathError = DbxPath.findError(generateDropboxPath(fileNameOnDropbox));
        if (pathError != null) {
            throw new org.gradle.api.GradleException("Invalid <dropbox-path>: " + pathError);
        }
    }

    def uploadFiles(DbxAuthInfo authInfo) {
        File item = new File(localFilesToUpload)
        if(item.isFile()){
            checkDropboxUploadPath(generateDropboxPath(item.getName()))
            uploadFile(authInfo,localFilesToUpload, generateDropboxPath(item.getName()))
        }else{
            def rootPathToUploadIn = generateDropboxPath("")
            project.fileTree(new File(localFilesToUpload)).each {fileToUpload ->
                def dirToUpload = fileToUpload.getParent()- localFilesToUpload
                uploadFile(authInfo,fileToUpload,rootPathToUploadIn+'/'+dirToUpload.replace("\\","/")+'/'+ fileToUpload.getName())
            }
        }
    }

    def uploadFile(DbxAuthInfo authInfo, def localFile, def pathToUpload) {
        String userLocale = Locale.getDefault().toString();
        DbxRequestConfig requestConfig = new DbxRequestConfig("project-upload", userLocale);
        DbxClient dbxClient = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);

        // Make the API call to upload the file.
        DbxEntry.File metadata;
        try {
            InputStream input = new FileInputStream(localFile)
            try {
                metadata = dbxClient.uploadFile(pathToUpload, DbxWriteMode.add(), -1, input);
            } catch (DbxException ex) {
                throw new org.gradle.api.GradleException("Error uploading to Dropbox: " + ex.getMessage())
            } finally {
                IOUtil.closeInput(input);
            }
        }
        catch (IOException ex) {
            throw new org.gradle.api.GradleException("Error reading from file \"" + localFile + "\": " + ex.getMessage());
        }
    }

    /**
     * Generate path for the directory in DropBox. If the name of course is specified then the directory will be
     * courseName/date/itemToUpload
     * Were date will be in format yyyy-MM-dd-kk-mm-ss
     * @return generated path for file in DropBox
     */
    String generateDropboxPath(String fileNameInDropbox) {
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
        dirName.append(fileNameInDropbox)
        dirName.toString()
    }

}