package bg.uni.fmi.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import bg.uni.fmi.tasks.PrintContent;

public class HelloPlugin implements Plugin<Project> {
    public static final String HELLO_TASK_NAME = "helloTask";

    @Override
    public void apply(Project project) {
        createTasks(project);
    }

    void createTasks(Project project) {
        PrintContent helloTask = project.getTasks().create(HELLO_TASK_NAME, PrintContent.class);
        helloTask.setDescription("Just say hello.");
        helloTask.setGroup("Hello");
    }
}