package gitlet;

import java.io.File;
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
    /** Tracked files. */
    public static final File TRACKED = join(GITLET_DIR, "tracked_files");
    /** Object folder for commits and blobs. */
    public static final File OBJECTS = join(GITLET_DIR, "objects");
    /** Containing commit object file. */
    public static final File COMMIT = join(OBJECTS, "commits");
    /** Contains add area and remove area. */
    public static final File AREA = join(GITLET_DIR, "area");
    /** Add area in staging area. */
    public static final File ADDAREA = join(AREA, "AddArea");
    /** Remove area in staging area. */
    public static final File REMOVEAREA = join(AREA, "RemoveArea");
    
    /** Creates a new Gitlet version-control system in the current directory. This system
     *  will automatically start with one commit: a commit that contains no files and has
     *  the commit message initial commit (just like that, with no punctuation). It will
     *  have a single branch: master, which initially points to this initial commit, and master
     *  will be the current branch. The timestamp for this initial commit will be 00:00:00
     *  UTC, Thursday, 1 January 1970 in whatever format you choose for dates (this is called
     *  represented internally by the time 0.) Since the initial commit in
     *  all repositories created by Gitlet will have exactly the same content, it follows that
     *  all repositories will automatically share this commit (they will all have the same UID)
     *  and all commits in all repositories will trace back to it.*/
    
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
        
        // Initial commit, and persistence.
        Commit initial = new Commit("initial commit", null);
        String sha1Code = initial.getCommitID();
        File commitFile = join(COMMIT, sha1Code);
        writeObject(commitFile, initial);
        
        // Create a branch named master and write into a file.
        File branch = join(GITLET_DIR, "master");
        writeContents(branch, initial.getCommitID());
        
        // Write into HEAD pointer.
        writeContents(HEAD, initial.getCommitID());
        
        // Create an empty file.
        writeContents(TRACKED, "");
    }
    
    /** Adds a copy of the file as it currently exists to the staging area (see the description
     *  of the commit command). For this reason, adding a file is also called staging the file
     *  for addition. Staging an already-staged file overwrites the previous entry in the staging
     *  area with the new contents. The staging area should be somewhere in .gitlet. If the current
     *  working version of the file is identical to the version in the current commit, do not stage
     *  it to be added, and remove it from the staging area if it is already there (as can happen
     *  when a file is changed, added, and then changed back to its original version). The file
     *  will no longer be staged for removal (see gitlet rm), if it was at the time of the
     *  command.*/
    
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
        
        String sha1ofFile = sha1(fileInWorkdir.toString());
        
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
    
    /** Saves a snapshot of tracked files in the current commit and staging area, so they
     *  can be restored at a later time, creating a new commit. The commit is said to
     *  be tracking the saved files. By default, each commit’s snapshot of files will be exactly
     *  the same as its parent commit’s snapshot of files; it will keep versions of files
     *  exactly as they are, and not update them. A commit will only update the contents
     *  of files it is tracking that have been staged for addition at the time of commit,
     *  in which case the commit will now include the version of the file that was staged
     *  instead of the version it got from its parent. A commit will save and start tracking
     *  any files that were staged for addition but weren’t tracked by its parent. Finally,
     *  files tracked in the current commit may be untracked in the new commit as a result
     *  being staged for removal by the rm command (below).

     The bottom line: By default a commit has the same file contents as its parent. Files staged
     for addition and removal are the updates to the commit. Of course, the date (and likely the
     mesage) will also different from the parent.

     Some additional points about commit:

     The staging area is cleared after a commit.

     The commit command never adds, changes, or removes files in the working directory (other than
     those in the .gitlet directory). The rm command will remove such files, as well as staging
     them for removal, so that they will be untracked after a commit.

     Any changes made to files after staging for addition or removal are ignored by the commit
     command, which only modifies the contents of the .gitlet directory. For example, if you
     remove a tracked file using the Unix rm command (rather than Gitlet’s command of the same
     name), it has no effect on the next commit, which will still contain the (now deleted)
     version of the file.

     After the commit command, the new commit is added as a new node in the commit tree.

     The commit just made becomes the current, and the head pointer now points to it.
     The previous head commit is this commit’s parent commit.

     Each commit should contain the date and time it was made.

     Each commit has a log message associated with it that describes the changes to the files
     in the commit. This is specified by the user. The entire message should take up only one
     entry in the array args that is passed to main. To include multiword messages, you’ll have
     to surround them in quotes.

     Each commit is identified by its SHA-1 id, which must include the file (blob) references
     of its files, parent reference, log message, and commit time.*/
    
    public static void commit(String commitMessage) {
        // Initialization.
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        
        // Need a non-blank message.
        if (commitMessage == null) {
            System.out.println("Please enter a commit message.");
            return;
        }
        
        // Whether the folder in add area is empty.
        File[] files = ADDAREA.listFiles();
        if (files.length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        
        // Create Commit object to store information, 
        String headInfo = readContentsAsString(HEAD);
        Commit commit = new Commit(commitMessage, headInfo);
        
        for (File file : files) {
            // Create blob of file.
            String blob = Blobs.createEachBlob(file);
            // Add map.
            commit.addMapKey(file.getName(), blob);
            // Persistence in tracked file.
            String contents = readContentsAsString(TRACKED);
            String newContents = contents + file.getName() + '\n';
            writeContents(TRACKED, newContents);
            // Delete file in add area.
            file.delete();
        }
        
        // Add tracked file blob.
        String trackedBlob = Blobs.createEachBlob(TRACKED);
        commit.addMapKey(TRACKED.getName(), trackedBlob);
        
        // Persistence of Commit object.
        File commitFile = join(COMMIT, commit.getCommitID());
        writeObject(commitFile, commit);

        // Change the contents of HEAD file.
        writeContents(HEAD, commit.getCommitID());
    }
    
    /** Unstage the file if it is currently staged for addition. If the file is tracked
     *  in the current commit, stage it for removal and remove the file from the working
     *  directory if the user has not already done so (do not remove it unless it is
     *  tracked in the current commit).*/
    
    public static void rm(String filename) {
        // Unstage the file if it is currently staged for addition.
        File fileInAddArea = join(ADDAREA, filename);
        if (fileInAddArea.exists()) {
            // copyFile(fileInAddArea, REMOVEAREA);
            fileInAddArea.delete();
        }
        
        String commitCode = readContentsAsString(HEAD);
        File currentCommit = join(COMMIT, commitCode);
        Commit commit = readObject(currentCommit, Commit.class);
        
        // If tracked file exists.
        if (commit.doesFileExists(filename)) {
            // Get blob of file.
            String blob = commit.getBlobOfFile(filename);
            // Copy to targetfile in remove area.
            File sourceFile = join(OBJECTS, filename, blob);
            File targetFile = join(REMOVEAREA, commitCode, filename);
            writeContents(targetFile, readContents(sourceFile));
            
            // Finally delete the file in working directory.
            File fileInWD = join(CWD, filename);
            fileInWD.delete();
        }
    }
    
    /** Starting at the current head commit, display information about each commit
     *  backwards along the commit tree until the initial commit, following the
     *  first parent commit links, ignoring any second parents found in merge commits.
     *  (In regular Git, this is what you get with git log --first-parent). This set
     *  of commit nodes is called the commit’s history. For every node in this history,
     *  the information it should display is the commit id, the time the commit was made,
     *  and the commit message. Here is an example of the exact format it should follow:*/
    public static void log() {
        String commitCode;
        for (commitCode = readContentsAsString(HEAD); commitCode != null;) {
            File currentCommit = join(COMMIT, commitCode);
            Commit commit = readObject(currentCommit, Commit.class);
            commit.printLog();
            commitCode = commit.getParentCommitID();
        }
    }
    
    public static void global_log() {
        List<String> log = plainFilenamesIn(COMMIT);
        for (String s : log) {
            System.out.println("commit " + s);
        }
    }

    public static void find(String commitMessage) {
        File[] commitsInFolder = COMMIT.listFiles();
        for (File file : commitsInFolder) {
            Commit checkcommit = readObject(file, Commit.class);
            if (checkcommit.getMessage().equals(commitMessage)) {
                System.out.println(checkcommit.getCommitID());
            }
        }
    }
    
    
}
