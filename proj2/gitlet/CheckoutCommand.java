package gitlet;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.Process.*;

public class CheckoutCommand {
    public static void checkout1(String filename) {
        Commit commit = getCurrentCommit();
        // Check the existence of file in current commit.
        if (!commit.hasFile(filename)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        // Find the snapshot of file and copy the contents to CWD.
        commit.copySnapshotToCWD(filename);
    }

    public static void checkout2(String commitID, String filename) {
        // Get specified commit.
        Commit commit = getSpecifiedCommit(commitID);

        // If no commit with the given id exists.
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        // Check the existence of file in current commit.
        if (!commit.hasFile(filename)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        // Find the snapshot of file and copy the contents to CWD.
        commit.copySnapshotToCWD(filename);
    }

    public static void checkout3(String branchName) {
        // Check the existence of branch and 
        if (!hasFileInFolder(BRANCHES, branchName)) {
            System.out.println("No such branch exists.");
            return;
        }

        // Whether head and branch is the same.
        if (getCurrentBranch().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        // Get of commitID from branch.
        String branchCommitID = readContentsAsString(join(BRANCHES, branchName));

        // Get specified commit.
        Commit branchCommit = getSpecifiedCommit(branchCommitID);

        // Check the existence of untracked files. 
        if (isUntrackedFileExistAndWillBeOverwriten(branchCommit)) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            return;
        }



        // Copy all files to CWD.
        branchCommit.copyAllSnapshotsInCommitToCWD();

        // Delete all the files in current branch but aren't present in checked-out branch.
        deleteRedundantFiles(branchCommit);

        // Change HEAD file.
        changeHead(branchCommit, branchName);

        // Clear staging area.
        clearStagingArea();
    }
}
