package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;
import static gitlet.Directory.*;
import static gitlet.Process.*;


public class StatusCommand {
    public static void status() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        System.out.println("=== Branches ===");
        // Print branches.
        printBranches();

        System.out.println("=== Staged Files ===");
        // Print files in add area.
        printFiles(ADD);

        System.out.println("=== Removed Files ===");
        // Print files in remove area.
        printFiles(REMOVE);

        System.out.println("=== Modifications Not Staged For Commit ===");
        // Print files modified but not be staged for commit.
        printComparedFilesInCommitAndCWD();

        System.out.println("=== Untracked Files ===");
        // Print untracked files.
        printUntrackedFiles();
    }

    private static void printBranches() {
        List<String> branchNames = plainFilenamesIn(BRANCHES);
        for (String branchName : branchNames) {
            if (getCurrentBranch().equals(branchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();
    }

    private static void printFiles(File folder) {
        List<String> filenames = plainFilenamesIn(folder);
        for (String filename : filenames) {
            System.out.println(filename);
        }
        System.out.println();
    }

    private static void printComparedFilesInAddAndCWD() {
        File[] filesInAdd = ADD.listFiles();
        List<String> filenames = plainFilenamesIn(CWD);
        for (File file : filesInAdd) {
            String sha1OfAdd = getSha1OfFile(file);
            String sha1OfCWD = getSha1OfFile(join(CWD, file.getName()));
            if (!filenames.contains(file.getName())) {
                System.out.println(file.getName() + " (deleted)");
            } else if (!sha1OfAdd.equals(sha1OfCWD)) {
                System.out.println(file.getName() + " (modified)");
            }
        }
    }

    private static void printComparedFilesInCommitAndCWD() {
        Map<String, String> fileSnapshot = getCurrentCommit().getFileSnapshot();
        for (String filename : fileSnapshot.keySet()) {
            File fileInCWD = join(CWD, filename);
            File fileInRemove = join(REMOVE, filename);
            String snapshot = getSha1OfFile(fileInCWD);
            if (!fileInCWD.exists() && !isStagedForRemoval(filename)) {
                System.out.println(filename + " (deleted)");
            } else if (fileInCWD.exists() && !fileSnapshot.get(filename).equals(snapshot)) {
                System.out.println(filename + " (modified)");
            } else {
                printComparedFilesInAddAndCWD();
            }
        }
        System.out.println();
    }

    private static void printUntrackedFiles() {
        List<String> filenames = plainFilenamesIn(CWD);
        for (String filename : filenames) {
            if (!isStagedForAddition(filename)
                    && !isStagedForRemoval(filename) && !isCommitted(filename)) {
                System.out.println(filename);
            }
        }
    }
}
