package org.frc4145.versiondata;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class VersionPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getTasks().create("versionData", VersionData.class, (task) -> {
            task.setFilePath("src/main/deploy/version.dat");
        });

        project.getTasks().create("debug_versiondata", VersionData.class, (task) -> {
            task.setFilePath("src/main/deploy/version.dat");
            task.setDebug(true);
        });
    }
}
