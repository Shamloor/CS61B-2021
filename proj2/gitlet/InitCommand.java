package gitlet;

import static gitlet.Process.*;
import static gitlet.Directory.*;


public class InitCommand {
    public static void init() {
        // Check the existence of GITLET
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control"
                    + " system already exists in the current directory.");
            return;
        }

        // Create folders.
        initailizeFolders();

        // create Commit object initial commit and print out message.
        Commit commit = createNewCommit("initial commit");

        // Persistence of Initial commit.
        commitPersistence(commit);

        // HEAD pointer points to initial commit.
        changeHead(commit);
    }
    
    private static void initailizeFolders() {
        GITLET_DIR.mkdir();
        OBJECTS.mkdir();
        AREA.mkdir();
        BRANCHES.mkdir();
        ADD.mkdir();
        REMOVE.mkdir();
        COMMITS.mkdir();
    }
}
