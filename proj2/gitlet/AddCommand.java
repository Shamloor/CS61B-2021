package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.CommitProcess.*;

public class AddCommand {
    public static void add(String filename) {
        // Find file in working directory and check its existence.
        if (!hasFileInFolder(CWD, filename)) {
            System.out.println("File does not exist.");
            return;
        }

        // If the version of current file is identical to the version in current commit,
        // do not stage it to add area, and remove the file in add area.
        Commit commit = getCurrentCommit();
        if (commit.hasFileComparedToCWD(filename)) {
            removeFile(REMOVE, filename);

            // Else copy the file to add area whether this file exists in add area.    
        } else {
            File file = join(CWD, filename);
            copyFile(file, ADD);
        }
    }
}
