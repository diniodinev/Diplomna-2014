package bg.uni.fmi.plugins;

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.plugins.*

class WWWPlugin implements Plugin<Project> {
    public static final String DELETE_SRC_TASK = "deleteSrc"

    void apply(Project project) {}
}