package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.Process.*;

public class BranchCommand {
    public static void branch(String branchName) {
        // Check the existence of branch named arg.
        if (hasFileInFolder(BRANCHES, branchName)) {
            System.out.println("A branch with that name already exists.");
        }

        // Turn isSplit to True.
        Commit commit = getCurrentCommit();
        commit.setSplitToTrue();
        commitPersistence(commit);

        // Create new branch file.
        File file = join(BRANCHES, branchName);
        writeContents(file, readContentsAsString(HEAD).substring(0, 40));

        // Current commit will be the split point.
        getSpecifiedCommit(readContentsAsString(HEAD).substring(0, 40)).setSplitToTrue();
    }
}
