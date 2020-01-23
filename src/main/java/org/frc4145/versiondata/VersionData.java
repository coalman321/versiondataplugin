package org.frc4145.versiondata;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VersionData extends DefaultTask {

    private String filePath = "src/main/deploy/version.dat";

    private final String newFile = "VERSION_ID=0;\n" +
                                   "BUILD_AUTHOR=none;\n" +
                                   "BUILD_DATE=1970/01/01-0:0:0;\n";

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    @TaskAction
    void doVersionData(){
        File versionFile = new File(System.getProperty("user.dir") + filePath);
        try{
            //pull data from versiondata file
            String versionID = findData("VERSION_ID", versionFile),
                    buildAuthor = findData("BUILD_AUTHOR", versionFile),
                    buildDate = findData("BUILD_DATE", versionFile);

            //create a writer
            PrintWriter writer =  new PrintWriter(versionFile.getAbsolutePath());

            //check to see if any of the data is missing.
            //if it is, overwrite the file
            if(versionID == null || buildAuthor == null || buildDate == null){
                writer.print(newFile);
                writer.flush();
                throw new GradleException("The " + versionFile.getName() + " file did not exist or was malformed." +
                        " The file has been re-created. Check to make sure it is correct and deploy again.");
            }

            //create the new file
            String newData = "VERSION_ID = " + (Integer.parseInt(versionID) + 1) + ";\n";
            newData += "BUILD_AUTHOR = " + System.getProperty("user.name") + ";\n";
            newData += "BUILD_DATE = " + new SimpleDateFormat(
                    "yyyy/MM/dd-HH:mm:ss").format(new Date()) + ";\n";

            //write the file
            writer.print(newFile);
            writer.flush();




        } catch (IOException e){
            throw new GradleException("The " + versionFile.getName() + " file could not be opened. check " +
                    "it to see if it has the correct permissions.");
        }
    }

    private String findData(String name, File file) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        name += " =";
        int loc = content.indexOf(name);
        if (loc < 0) return null;

        String fromKeyToEnd = content.substring(loc + name.length());
        int endlineSemicolon = fromKeyToEnd.indexOf(";\n");
        if (endlineSemicolon < 0) return null;

        return content.substring(loc + name.length(), endlineSemicolon);
    }


}
