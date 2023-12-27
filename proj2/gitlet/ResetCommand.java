package gitlet;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.CommitProcess.*;


public class ResetCommand {
    public static void reset(String commmitID) {
        // Check the existence of commit.
        Commit commit = getSpecifiedCommit(commmitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        // If untracked file exists.
        if (isUntrackedFileExistAndWillBeOverwriten(commit)) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            return;
        }

        // Copy all tracked files from commit to CWD.
        commit.copyAllSnapshotsInCommitToCWD();

        // Delete redundant files.
        deleteRedundantFiles(commit);

        // Clear staging area.
        clearStagingArea();

        // Change head.
        changeHead(commit);

        // Change current branch.
        writeContents(join(BRANCHES, getCurrentBranch()), commit.getCommitID());
    }
}
