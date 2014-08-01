package bg.uni.fmi.tasks;

import org.gradle.api.Task;
import org.gradle.api.tasks.Delete;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static bg.uni.fmi.plugins.EssayPlugin.UNZIP_TASK_NAME;

public class CleanEssays extends Delete {

    List<File> filesToDelete = new LinkedList<File>();
    Task unzipTask = getProject().getTasks().getByName(UNZIP_TASK_NAME);

    public CleanEssays() {
        getProject().afterEvaluate(new Action<Project>() {
            public void execute(Project project) {
              String outputPath = unzipTask.property("outputDir").toString();
                ConfigurableFileTree fileTree =  project.fileTree(outputPath);

                for(File currentFile:fileTree) {
                    filesToDelete.add(currentFile);
                }
                filesToDelete.add(project.file(outputPath));
                filesToDelete.add(project.file(project.getBuildDir()));
            }
        });
    }

    @TaskAction
    public void cleanSrc() {
        setDelete(filesToDelete);
        clean();
    }
}

