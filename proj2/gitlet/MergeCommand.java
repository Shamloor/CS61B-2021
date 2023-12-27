package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.CheckoutCommand.checkout3;
import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.Process.*;

public class MergeCommand {
    public static void merge(String givenBranch) {
        if (isUntrackedFileExist()) {
            System.out.println("There is an untracked file in the way; delete it, "
                    + "or add and commit it first.");
            return;
        }
        if (ADD.listFiles().length != 0 || REMOVE.listFiles().length != 0) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!join(BRANCHES, givenBranch).exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (readContentsAsString(HEAD).substring(41).equals(givenBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Commit currentCommit = getCurrentCommit();
        Commit givenCommit = getSpecifiedCommit(getCommitID(join(BRANCHES, givenBranch)));
        // If given branch is the ancestor of current branch.
        if (isAncestor(givenCommit, currentCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        // If current branch is the ancestor of given branch.
        if (isAncestor(currentCommit, givenCommit)) {
            checkout3(givenBranch);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        mergeDetail(givenBranch, currentCommit, givenCommit);
    }

    private static boolean isAncestor(Commit commit1, Commit commit2) {
        while (commit2.getParent() != null) {
            if (commit2.getCommitID().equals(commit1.getCommitID())) {
                return true;
            }
            commit2 = getSpecifiedCommit(commit2.getParent());
        }
        return false;
    }

    private static void mergeDetail(String givenBranch, Commit currentCommit, Commit givenCommit) {
        Commit splitCommit = getSplitCommit(currentCommit, givenCommit);
        Map<String, String> splitMap = splitCommit.getFileSnapshot();
        Map<String, String> currentMap = currentCommit.getFileSnapshot();
        Map<String, String> givenMap = givenCommit.getFileSnapshot();
        Set<String> splitSet = splitMap.keySet();
        Set<String> currentSet = currentMap.keySet();
        Set<String> givenSet = givenMap.keySet();

        boolean hasMergeConflict = false;
        for (String filename : splitSet) {
            boolean hasInCurrent = currentSet.contains(filename);
            boolean hasInGiven = givenSet.contains(filename);

            if (!hasInCurrent && !hasInGiven) {
                continue;
            } else if (hasInCurrent && !hasInGiven
                    && splitMap.get(filename).equals(currentMap.get(filename))) {
                copyFile(join(CWD, filename), REMOVE);
                removeFile(CWD, filename);
            } else if (!hasInCurrent && hasInGiven) {
                continue;
            } else {
                String splitSnap = splitMap.get(filename);
                String currentSnap = currentMap.get(filename);
                String givenSnap = givenMap.get(filename);

                boolean cmpCurrentToSplit = splitSnap.equals(currentSnap);
                boolean cmpGivenToSplit = splitSnap.equals(givenSnap);
                boolean cmpCurrentToGiven = currentSnap.equals(givenSnap);

                if (cmpCurrentToSplit && !cmpGivenToSplit) {
                    givenCommit.copySnapshotToCWD(filename);
                    copyFile(join(CWD, filename), ADD);
                } else if (!cmpCurrentToSplit && cmpGivenToSplit) {
                    continue;
                } else if (!cmpCurrentToSplit && !cmpGivenToSplit) {
                    if (!cmpCurrentToGiven) {
                        mergeConflict(filename, currentSnap, givenSnap);
                        copyFile(join(CWD, filename), ADD);
                        hasMergeConflict = true;
                    }
                }
            }
        }
    }

    private static void mergeConflict(String filename, String currentSnap, String givenSnap) {
        if (givenSnap == null) {
            givenSnap = "null";
        }
        File currentFile = join(OBJECTS, filename, currentSnap);
        File givenFile = join(OBJECTS, filename, givenSnap);
        byte[] head = "<<<<<<< HEAD\n".getBytes();
        byte[] split = "=======\n".getBytes();
        byte[] end = ">>>>>>>\n".getBytes();

        File target = join(CWD, filename);
        if (givenFile.exists()) {
            writeContents(target, head, readContents(currentFile), split,
                    readContents(givenFile), end);
        } else {
            writeContents(target, head, readContents(currentFile), split, end);
        }
    }

    
}
