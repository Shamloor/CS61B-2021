package gitlet;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.Process.*;

public class CommitCommand {
    public static void commit(String message) {
        // Check the area folder, if both of add and remove are empty.
        if (ADD.listFiles().length == 0 && REMOVE.listFiles().length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }

        // Create a new Commit object.
        Commit commit = createNewCommit(message);

        // Create snapshot of files and clear both areas.
        // Modify fileSnapshot variable from add area and remove area.
        // Clear both areas.
        createSnapshotOfFile(commit);
        removeSnapshotOfFile(commit);

        // Persistence of new commit.
        commitPersistence(commit);

        // HEAD points to new commit.
        changeHead(commit);

        // Current branch points to new commit.
        writeContents(join(BRANCHES, getCurrentBranch()), commit.getCommitID());
    }
}
