package org.frc4145.versiondata;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class VersionPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getTasks().create("versionData", VersionData.class, (task) -> {});
    }
}
