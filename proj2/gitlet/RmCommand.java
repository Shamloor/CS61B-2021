package gitlet;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.CommitProcess.*;

public class RmCommand {
    public static void rm(String filename) {
        Commit commit = getCurrentCommit();
        // If one file is currently in add area, just remove it.
        if (hasFileInFolder(ADD, filename)) {
            removeFile(ADD, filename);

            // Else if current commit contains this file and has the same snapshot,
            // stage it for removal and remove the file from working directory.
        } else if (hasFileInCommit(commit, filename)) {
            if (!join(CWD, filename).exists()) {
                commit.copySnapshotToCWD(filename);
            }
            if (hasSameSnapshot(commit, filename)) {
                copyFile(join(CWD, filename), REMOVE);
                removeFile(CWD, filename);
            }

            // Else print out error message
        } else {
            System.out.println("No reason to remove the file.");
        }
    }
}
