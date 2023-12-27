package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gitlet.Utils.*;
import static gitlet.Directory.*;


public class CommitProcess {
    public static Commit createNewCommit(String message) {
        Commit commit = new Commit(message, getCommitID(HEAD));
        commit.inheritParentMap();
        return commit;
    }

    public static String getCommitID(File file) {
        if (!file.exists()) {
            return null;
        }
        // Read from file as String.
        return readContentsAsString(file).substring(0, 40);
    }

    public static void commitPersistence(Commit commit) {
        String commitFilename = commit.getCommitID();
        File commitFile = join(COMMITS, commitFilename);
        writeObject(commitFile, commit);
    }

    public static boolean isCommitted(String filename) {
        Commit commit = getCurrentCommit();
        return commit.hasFile(filename);
    }

    public static Commit getCurrentCommit() {
        File file = join(COMMITS, getCommitID(HEAD));
        return readObject(file, Commit.class);
    }

    public static void createSnapshotOfFile(Commit commit) {
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

    public static void removeSnapshotOfFile(Commit commit) {
        File[] files = REMOVE.listFiles();
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            commit.removeMapKV(file.getName());
            removeFile(REMOVE, file.getName());
        }
    }


    public static boolean hasFileInCommit(Commit commit, String filename) {
        return commit.hasFile(filename);
    }

    public static boolean hasSameSnapshot(Commit commit, String filename) {
        return commit.getFileSnapshotValue
                (filename).equals(getSha1OfFile(join(CWD, filename)));
    }

    public static Commit getSpecifiedCommit(String id) {
        List<String> commits = plainFilenamesIn(COMMITS);
        for (String commitID : commits) {
            if (commitID.substring(0, id.length()).equals(id)) {
                return readObject(join(COMMITS, commitID), Commit.class);
            }
        }
        System.out.println();
        return null;
    }

    public static List<Commit> getCommits() {
        File[] commitFiles = COMMITS.listFiles();
        List<Commit> commits = new ArrayList<>();
        for (File file : commitFiles) {
            commits.add(readObject(file, Commit.class));
        }
        return commits;
    }

    public static Commit getSplitCommit(Commit currentCommit, Commit givenCommit) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        list1.addAll(findSplitCommit(currentCommit));

        list2.addAll(findSplitCommit(givenCommit));

        list1.retainAll(list2);
        return getLatestCommit(list1);
    }

    public static Set<String> findSplitCommit(Commit commit) {
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

    public static Commit getLatestCommit(List<String> commitIDs) {
        Commit latestCommit = getSpecifiedCommit(commitIDs.get(0));
        for (String commitID : commitIDs) {
            Commit commit = getSpecifiedCommit(commitID);
            if (commit.getDate().after(latestCommit.getDate())) {
                latestCommit = commit;
            }
        }
        return latestCommit;
    }

    public static boolean isStagedForAddition(String filename) {
        File fileInCWD = join(CWD, filename);
        File fileInArea = join(ADD, filename);
        if (!fileInArea.exists()) {
            return false;
        }
        String sha1OfCWD = getSha1OfFile(fileInCWD);
        String sha1OfRemove = getSha1OfFile(fileInArea);
        return sha1OfCWD.equals(sha1OfRemove);
    }

    public static boolean isStagedForRemoval(String filename) {
        File fileInArea = join(REMOVE, filename);
        return fileInArea.exists();
    }

    public static boolean isUntrackedFileExistAndWillBeOverwriten(Commit commit) {
        List<String> filenames = plainFilenamesIn(CWD);
        for (String filename : filenames) {
            if (!isStagedForAddition(filename) && !isCommitted(filename)
                    && !isStagedForRemoval(filename) && commit.hasFile(filename)) {
                return true;
            }
        }
        return false;
    }

    public static void clearStagingArea() {
        File[] addFiles = ADD.listFiles();
        File[] removeFiles = REMOVE.listFiles();

        for (File addFile : addFiles) {
            addFile.delete();
        }
        for (File removeFile : removeFiles) {
            removeFile.delete();
        }
    }

    public static boolean isUntrackedFileExist() {
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

    public static void changeHead(Commit commit) {
        if (!HEAD.exists()) {
            writeContents(HEAD, commit.getCommitID() + " master");
        }
        String branch = readContentsAsString(HEAD).substring(40);
        String newContents = commit.getCommitID() + branch;
        writeContents(HEAD, newContents);
    }

    public static void changeHead(Commit commit, String branchName) {
        String newContents = commit.getCommitID() + " " + branchName;
        writeContents(HEAD, newContents);
    }

    public static String getCurrentBranch() {
        return readContentsAsString(HEAD).substring(41);
    }

    public static void deleteRedundantFiles(Commit commit) {
        Set<String> keysOfBranch = commit
                .getFileSnapshot().keySet();
        Set<String> keysOfHead = getSpecifiedCommit(readContentsAsString(HEAD).substring(0, 40))
                .getFileSnapshot().keySet();

        keysOfHead.removeAll(keysOfBranch);

        for (String filename : keysOfHead) {
            join(CWD, filename).delete();
        }
    }
}
