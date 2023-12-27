package gitlet;

import static gitlet.BranchCommand.branch;
import static gitlet.CommitProcess.*;
import static gitlet.Directory.*;
import static gitlet.Utils.join;

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

        // Branch pointer master points to initial commit.
        branch("master");
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
