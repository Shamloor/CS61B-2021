package gitlet;


import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.Process.*;

public class RmBranchCommand {
    public static void rmBranch(String branchName) {
        // Check the existence of branch named arg.
        if (!hasFileInFolder(BRANCHES, branchName)) {
            System.out.println("A branch with that name does not exist.");
        }

        // Check weather this branch is current branch.
        if (branchName.equals(getCurrentBranch())) {
            System.out.println("Cannot remove the current branch.");
        }

        // Delete the file named branchName.
        removeFile(BRANCHES, branchName);
    }
}
