package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;
import static gitlet.Directory.*;


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
 
    public static void init() {
        InitCommand.init();
    }

    public static void add(String filename) {
        AddCommand.add(filename);
    }

    public static void commit(String message) {
        CommitCommand.commit(message);
    }

    public static void rm(String filename) {
        RmCommand.rm(filename);
    }

    public static void log() {
        LogCommand.log();
    }

    public static void globalLog() {
        LogCommand.globalLog();
    }

    public static void find(String commitMessage) {
        FindCommand.find(commitMessage);
    }

    public static void status() {
        StatusCommand.status();
    }

    public static void checkout1(String filename) {
        CheckoutCommand.checkout1(filename);
    }

    public static void checkout2(String commitID, String filename) {
        CheckoutCommand.checkout2(commitID, filename);
    }

    public static void checkout3(String branchName) {
        CheckoutCommand.checkout3(branchName);
    }

    public static void branch(String branchName) {
        BranchCommand.branch(branchName);
    }

    public static void rmBranch(String branchName) {
        RmBranchCommand.rmBranch(branchName);
    }

    public static void reset(String commmitID) {
        ResetCommand.reset(commmitID);
    }

    public static void merge(String givenBranch) {
        MergeCommand.merge(givenBranch);
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
        
        list1.addAll(findSplitCommit(currentCommit));

        list2.addAll(findSplitCommit(givenCommit));
        
        list1.retainAll(list2);
        return getLatestCommit(list1);
    }
    
    private static Set<String> findSplitCommit(Commit commit) {
        Set<String> splitSet = new HashSet<>();
        
        if (commit.getIsSplit()) {
            splitSet.add(commit.getCommitID());
        }

        if (commit.getParent() == null) {
            return splitSet;
        }
        
        if (commit.getAnotherParent() != null) {
            splitSet.addAll(findSplitCommit
                    (getSpecifiedCommit(commit.getAnotherParent())));
        }
        
        splitSet.addAll(findSplitCommit(getSpecifiedCommit(commit.getParent())));
        return splitSet;
    }

    private static Commit getLatestCommit(List<String> commitIDs) {
        Commit latestCommit = getSpecifiedCommit(commitIDs.get(0));
        for (String commitID : commitIDs) {
            Commit commit = getSpecifiedCommit(commitID);
            if (commit.getDate().after(latestCommit.getDate())) {
                latestCommit = commit;
            }
        }
        return latestCommit;
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
            if (!isStagedForAddition(filename) 
                    && !isStagedForRemoval(filename) && !isCommitted(filename)) {
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

    public static boolean isCommitted(String filename) {
        Commit commit = getCurrentCommit();
        return commit.hasFile(filename);
    }
    
    private static boolean isUntrackedFileExist() {
        List<String> filenames = plainFilenamesIn(CWD);
        for (String filename : filenames) {
            if (!isCommitted(filename) 
                    && !isStagedForAddition(filename) 
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

        // If splitSet does not contain a file.
        currentSet.removeAll(splitSet);
        givenSet.removeAll(splitSet);

        Set<String> tmpC = currentSet;
        Set<String> tmpG = givenSet;

        tmpC.retainAll(tmpG);
        for (String filename : tmpC) {
            String givenSnapshot = givenCommit.getFileSnapshotValue(filename);
            String currentSnapshot = currentCommit.getFileSnapshotValue(filename);
            if (!givenSnapshot.equals(currentSnapshot)) {
                mergeConflict(filename, currentSnapshot, givenSnapshot);
                hasMergeConflict = true;
            }
        }

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
}
