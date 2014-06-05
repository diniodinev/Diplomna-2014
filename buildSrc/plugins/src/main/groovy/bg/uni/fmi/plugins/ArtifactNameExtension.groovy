package bg.uni.fmi.plugins

import org.gradle.api.Project

class ArtifactNameExtension {
    final Project project

    public ArtifactNameExtension(final Project project) {
        this.project = project
    }

}