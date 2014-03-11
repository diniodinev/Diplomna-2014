package bg.uni.fmi.plugins

import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class ArtifactNameExtension {
    final Project project

    public ArtifactNameExtension(final Project project) {
        this.project = project
    }

}