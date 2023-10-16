package gitlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** HEAD pointer. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** Object folder for commits and blobs. */
    public static final File OBJECTS = join(GITLET_DIR, "objects");
    /** Containing commit object file. */
    public static final File COMMIT = join(OBJECTS, "commits");
    /** Contains add area and remove area. */
    public static final File AREA = join(GITLET_DIR, "area");
    /** Add area in staging area. */
    public static final File ADDAREA = join(AREA, "addarea");
    /** Remove area in staging area. */
    public static final File REMOVEAREA = join(AREA, "rmarea");
    /** Branches. */
    public static final File BRANCH = join(GITLET_DIR, "branch");
    
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        
        // Create folders.
        // Cannot directly create a file in multiple folders, you can just create them step by step. 
        GITLET_DIR.mkdir();
        OBJECTS.mkdir();
        COMMIT.mkdir();
        AREA.mkdir();
        ADDAREA.mkdir();
        REMOVEAREA.mkdir();
        BRANCH.mkdir();
        
        // Initial commit, and persistence.
        Commit initial = new Commit("initial commit", null);
        String sha1Code = initial.getCommitID();
        File commitFile = join(COMMIT, sha1Code);
        writeObject(commitFile, initial);
        
        // Create a branch named master and write into a file.
        File branch = join(BRANCH, "master");
        writeContents(branch, initial.getCommitID());
        
        // Write into HEAD pointer.
        writeContents(HEAD, initial.getCommitID());
        
        // Create an empty file.
        // writeContents(TRACKED, "");
    }
    
    public static void add(String filename) {
        // Initialization.
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        
        // Current work directory.
        File fileInWorkdir = join(CWD, filename);
        if (!fileInWorkdir.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        
        String sha1ofFile = sha1(fileInWorkdir.getName(), readContentsAsString(fileInWorkdir));
        
        // File folder will store all blobs of one file.
        File fileFolderInBlobs = join(OBJECTS, filename);
        File fileInBlobs = join(fileFolderInBlobs, sha1ofFile);
        
        StagingArea stagingArea = new StagingArea();
        if (!fileFolderInBlobs.exists() || !fileInBlobs.exists()) {
            // If the blob of the file does not exist, add to staging area. 
            stagingArea.addToAddArea(fileInWorkdir);
        } else {
            // Remove from add area if it exists.
            stagingArea.removeFromAddArea(filename);
        }
    }
    
    public static void commit(String commitMessage) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        
        if (commitMessage == null) {
            System.out.println("Please enter a commit message.");
            return;
        }
        
        // Whether the folder in add area is empty.
        File[] addFiles = ADDAREA.listFiles();
        File[] rmFiles = REMOVEAREA.listFiles();
        if (addFiles.length == 0 && rmFiles.length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        
        Commit commit = currentCommit();
        commit.changeMessage(commitMessage);
        
        for (File file : addFiles) {
            String blob = Blobs.createEachBlob(file);
            commit.addMapKey(file.getName(), blob);
            file.delete();
        }
        for (File file : rmFiles) {
            commit.removeMapKey(file.getName());
        }
        
        // Persistence of Commit object.
        File commitFile = join(COMMIT, commit.getCommitID());
        writeObject(commitFile, commit);

        // Change the contents of HEAD file.
        writeContents(HEAD, commit.getCommitID());
        
        // Change the branch contents of branch file.
        File branch = join(BRANCH, "master");
        writeContents(branch, commit.getCommitID());
    }
    
    public static void rm(String filename) {
        File fileInCWD = join(CWD, filename);
        
        if (!fileInCWD.exists()) {
            System.out.println("The file does not exist.");
        }
        
        StagingArea stagingArea = new StagingArea();
        boolean rmStatus = stagingArea.removeFromAddArea(filename);
        
        if (isTracked(filename)) {
            if (isModified(filename)) {
                System.out.println("The file is modified, cannot be deleted.");
            } else {
                stagingArea.addToRemoveArea(filename);
                fileInCWD.delete();
            }
        } else if (!rmStatus) {
            System.out.println("No reason to remove the file.");
        }
    }
    
    public static void log() {
        Commit commit = currentCommit();
        while (commit.getParent() != null) {
            commit.printLog();
            commit = specifiedCommit(commit.getCommitID());
        }
    }
    
    public static void global_log() {
        List<String> log = plainFilenamesIn(COMMIT);
        for (String s : log) {
            System.out.println("commit " + s);
        }
    }

    public static void find(String commitMessage) {
        
    }
    
    public static void checkout(String filename) {
        Commit currentCommit = currentCommit();
        if (currentCommit.hasFile(filename)) {
            currentCommit.copyFileToCWD(filename);
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }
    
//    public static void checkout()

    private static boolean isBranch(String arg) {
        List<String> filenames = plainFilenamesIn(BRANCH);
        if (filenames.contains(arg)) {
            return true;
        }
        return false;
    }

    private static boolean isCommit(String arg) {
        List<String> filenames = plainFilenamesIn(COMMIT);
        for (String filename : filenames) {
           if (filename.substring(0, arg.length()).equals(arg)) {
               return true;
           }
        }
        return false;
    }
    
    private static boolean isModified(String filename) {
        File currentFile = join(CWD, filename);
        if (!currentFile.exists()) {
            System.out.println("File in working directory does not exist.");
            return false;
        }
        String sha1ofCurrentFile = sha1(currentFile.getName(), readContentsAsString(currentFile));
        
        Commit commit = currentCommit();
        String sha1ofCommitFile = commit.getBlobOfFile(filename);
        
        return (!sha1ofCurrentFile.equals(sha1ofCommitFile));
    }

    private static boolean isTracked(String filename) {
        Commit commit = currentCommit();
        return commit.hasFile(filename);
    }
    
    private static Commit currentCommit() {
        String commitCode = readContentsAsString(Repository.HEAD);
        File currentCommit = join(Repository.COMMIT, commitCode);
        Commit commit = readObject(currentCommit, Commit.class);
        return commit;
    }
    
    private static Commit specifiedCommit(String commitCode) {
        File specifiedCommit = join(Repository.COMMIT, commitCode);
        Commit commit = readObject(specifiedCommit, Commit.class);
        return commit;
    }

/** How to deal with master? */    
}
