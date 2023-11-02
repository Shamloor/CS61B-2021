package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  @author Willson Yu
 */
public class Repository {
    
    /** .gitlet folder will contain these folders and files.
     *  .gitlet
     *  |---objects
     *  |    |---commits
     *  |    |---(hash of filename)
     *  |        |---snapshot of files
     *  |---area
     *  |   |---add
     *  |   |---remove
     *  |---branches
     *  |---HEAD
     * */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS = join(GITLET_DIR, "objects");
    public static final File AREA  = join(GITLET_DIR, "area");
    public static final File BRANCHES = join(GITLET_DIR, "branches");
    /** The second line will store current branch. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    public static final File COMMITS = join(OBJECTS, "commits");
    public static final File ADD = join(AREA, "add");
    public static final File REMOVE = join(AREA, "remove");
    
    public static void init() {
        // Check the existence of GITLET
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control" +
                    " system already exists in the current directory.");
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

    public static void add(String filename) {
        // Find file in working directory and check its existence.
        if (!hasFileInFolder(CWD, filename)) {
            System.out.println("File does not exist.");
            return;
        }
        
        // If the version of current file is identical to the version in current commit,
        // do not stage it to add area, and remove the file in add area.
        Commit commit = getCurrentCommit();
        if (commit.hasFileComparedToCWD(filename)) {
            removeFile(REMOVE, filename);
            
            // Else copy the file to add area whether this file exists in add area.    
        } else {
            File file = join(CWD, filename);
            copyFile(file, ADD);
        }
    }

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

    public static void log() {
        // Get current Commit object.
        Commit commit = getCurrentCommit();
        
        // Print log.
        while (true) {
            commit.printLog();
            if (commit.getParent() == null) {
                break;
            }
            commit = getSpecifiedCommit(commit.getParent());
        }
    }

    public static void globalLog() {
        // Get commits in COMMITS
        List<Commit> commits = getCommits();
        
        // Loop: call printLog() function.
        for (Commit commit : commits) {
            commit.printLog();
        }
    }

    public static void find(String commitMessage) {
        // Get commits in COMMITS
        List<Commit> commits = getCommits();
        boolean hasMessage = false;
        
        // Loop: get message variable of Commit object and compare strings.
        for (Commit commit : commits) {
            if (commitMessage.equals(commit.getMessage())) {
                System.out.println(commit.getCommitID());
                hasMessage = true;
            }
        }
        
        // If no message exists.
        if (!hasMessage) {
            System.out.println("Found no commit with that message.");
        }
    }

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
            System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
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
        writeContents(file, readContentsAsString(HEAD).substring(0,40));
        
        // Current commit will be the split point.
        getSpecifiedCommit(readContentsAsString(HEAD).substring(0, 40)).setSplitToTrue();
    }

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

    public static void reset(String commmitID) {
        // Check the existence of commit.
        Commit commit = getSpecifiedCommit(commmitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        
        // If untracked file exists.
        if (isUntrackedFileExistAndWillBeOverwriten(commit)) {
            System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
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

    public static void merge(String givenBranch) {
        if (isUntrackedFileExist()) {
            System.out.println("There is an untracked file in the way; delete it, " +
                    "or add and commit it first.");
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
            return;
        }
        
        // Otherwise, continue with the steps below.
        // Get split commit.
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
                
            } else if (hasInCurrent && !hasInGiven &&
                    splitMap.get(filename).equals(currentMap.get(filename))) {
                copyFile(join(CWD, filename), REMOVE);
                removeFile(CWD, filename);
            } else if (!hasInCurrent && hasInGiven) {
                // Nothing happens. 
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
                    // Stay as they are.
                } else if (!cmpCurrentToSplit && !cmpGivenToSplit) {
                    if (!cmpCurrentToGiven) {
                        mergeConflict(filename, currentSnap, givenSnap);
                        copyFile(join(CWD, filename), ADD);
                        hasMergeConflict = true;
                    }
                }
            }
        }
        
        // If splitSet does not contain a file.
        currentSet.removeAll(splitSet);
        givenSet.removeAll(splitSet);

        givenSet.removeAll(currentSet);
        for (String filename : givenSet) {
            givenCommit.copySnapshotToCWD(filename);
            copyFile(join(CWD, filename), ADD); 
        }

        commit("Merged " + givenBranch + " into " + getCurrentBranch() + ".");
        
        if (hasMergeConflict) {
            System.out.println("Encountered a merge conflict.");
        }
        
        Commit commit = getCurrentCommit();
        commit.setAnotherParent(readContentsAsString(join(BRANCHES, givenBranch)));
        commitPersistence(commit);
    }

    


    public static void testC() {
        System.out.println(getCurrentCommit().getParent());
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
    
    private static Commit createNewCommit(String message) {
        Commit commit = new Commit(message, getCommitID(HEAD));
        commit.inheritParentMap();
        return commit;
    }
    
    private static void createSnapshotOfFile(Commit commit) {
        File[] files = ADD.listFiles();
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            String sha1 = getSha1OfFile(file);
            File snapshotFolder = join(OBJECTS, file.getName());
            if (!snapshotFolder.exists()) {
                snapshotFolder.mkdir();
            }
            File snapshot = join(snapshotFolder, sha1);
            writeContents(snapshot, readContents(file));
            commit.addOrModifyMapKV(file.getName(), sha1);
            removeFile(ADD, file.getName());
        }
    }
    
    private static void removeSnapshotOfFile(Commit commit) {
        File[] files = REMOVE.listFiles();
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            commit.removeMapKV(file.getName());
            removeFile(REMOVE, file.getName());
        }
    }
    
    private static Commit getCurrentCommit() {
        File file = join(COMMITS, getCommitID(HEAD));
        return readObject(file, Commit.class);
    }
    
    private static Commit getSpecifiedCommit(String id) {
        List<String> commits = plainFilenamesIn(COMMITS);
        for (String commitID : commits) {
            if (commitID.substring(0, id.length()).equals(id)) {
                return readObject(join(COMMITS, commitID), Commit.class);
            }
        }
        System.out.println();
        return null;
    }

    private static String getCommitID(File file) {
        if (!file.exists()) {
            return null;
        }
        // Read from file as String.
        return readContentsAsString(file).substring(0, 40);
    }

    private static List<Commit> getCommits() {
        File[] commitFiles = COMMITS.listFiles();
        List<Commit> commits = new ArrayList<>();
        for (File file : commitFiles) {
            commits.add(readObject(file, Commit.class));
        }
        return commits;
    }

    private static String getCurrentBranch() {
        return readContentsAsString(HEAD).substring(41);
    }
    

    private static Commit getSplitCommit(Commit currentCommit, Commit givenCommit) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        
        while (true) {
            if (currentCommit.getIsSplit()) {
                list1.add(currentCommit.getCommitID());
            }
            if (currentCommit.getParent() == null) {
                break;
            }
            currentCommit = getSpecifiedCommit(currentCommit.getParent());
        }

        while (true) {
            if (givenCommit.getIsSplit()) {
                list2.add(givenCommit.getCommitID());
            }
            if (givenCommit.getParent() == null) {
                break;
            }
            givenCommit = getSpecifiedCommit(givenCommit.getParent());
        }
        
        list1.retainAll(list2);
        return getSpecifiedCommit(list1.get(0));
    }
    
    
    private static void changeHead(Commit commit) {
        if (!HEAD.exists()) {
            writeContents(HEAD, commit.getCommitID() + " master");
        }
        String branch = readContentsAsString(HEAD).substring(40);
        String newContents = commit.getCommitID() + branch;
        writeContents(HEAD, newContents);
    }

    private static void changeHead(Commit commit, String branchName) {
        String newContents = commit.getCommitID() + " " + branchName;
        writeContents(HEAD, newContents);
    }
    
    private static void commitPersistence(Commit commit) {
        String commitFilename = commit.getCommitID();
        File commitFile = join(COMMITS, commitFilename);
        writeObject(commitFile, commit);
    }
    
    private static boolean hasFileInCommit(Commit commit, String filename) {
        return commit.hasFile(filename);
    }
    
    private static boolean hasSameSnapshot(Commit commit, String filename) {
        return commit.getFileSnapshotValue
                (filename).equals(getSha1OfFile(join(CWD, filename)));
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
    
    private static void printComparedFilesInAddandCWD() {
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
                printComparedFilesInAddandCWD();
            }
        }
        System.out.println();
    }
    
    private static void printUntrackedFiles() {
        List<String> filenames = plainFilenamesIn(CWD);
        for (String filename : filenames) {
            if (!isStagedForAddition(filename) && 
                    !isStagedForRemoval(filename) && !isCommitted(filename)) {
                System.out.println(filename);
            }
        }
    }
    
    private static boolean isUntrackedFileExistAndWillBeOverwriten(Commit commit) {
        List<String> filenames = plainFilenamesIn(CWD);
        for (String filename : filenames) {
            if (!isStagedForAddition(filename) && !isCommitted(filename)
                && !isStagedForRemoval(filename) && commit.hasFile(filename)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isUntrackedFileExist() {
        List<String> filenames = plainFilenamesIn(CWD);
        for (String filename : filenames) {
            if (!isCommitted(filename) &&
                    !isStagedForAddition(filename) 
                    && !isStagedForRemoval(filename)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isStagedForAddition(String filename) {
        File fileInCWD = join(CWD, filename);
        File fileInArea = join(ADD, filename);
        if (!fileInArea.exists()) {
            return false;
        }
        String sha1OfCWD = getSha1OfFile(fileInCWD);
        String sha1OfRemove = getSha1OfFile(fileInArea);
        return sha1OfCWD.equals(sha1OfRemove);
    }

    private static boolean isStagedForRemoval(String filename) {
        File fileInArea = join(REMOVE, filename);
        return fileInArea.exists();
    }
    
    private static boolean isCommitted(String filename) {
        Commit commit = getCurrentCommit();
        return commit.hasFile(filename);
    }

    /** Is commit1 the ancestor of commit2.*/
    private static boolean isAncestor(Commit commit1, Commit commit2) {
        while (commit2.getParent() != null) {
            if (commit2.getCommitID().equals(commit1.getCommitID())) {
                return true;
            }
            commit2 = getSpecifiedCommit(commit2.getParent());
        }
        return false;
    }

    
    
    
    private static void deleteRedundantFiles(Commit commit) {
        Set<String> keysOfBranch = commit
                .getFileSnapshot().keySet();
        Set<String> keysOfHead = getSpecifiedCommit(readContentsAsString(HEAD).substring(0, 40))
                .getFileSnapshot().keySet();
        
        keysOfHead.removeAll(keysOfBranch);
        
        for (String filename : keysOfHead) {
            join(CWD, filename).delete();
        }
    }
    
    private static void clearStagingArea() {
        File[] addFiles = ADD.listFiles();
        File[] removeFiles = REMOVE.listFiles();
        
        for (File addFile : addFiles) {
            addFile.delete();
        }
        for (File removeFile : removeFiles) {
            removeFile.delete();
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
