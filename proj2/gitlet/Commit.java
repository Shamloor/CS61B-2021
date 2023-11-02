package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *
 *  @author Willson Yu
 */
public class Commit implements Serializable {
    private String message;
    private String parent;
    private Map<String, String> fileSnapshot = new HashMap<>();
    private String commitID;
    private Date date;
    private String anotherParent;
    private boolean isSplit = false;

    private SimpleDateFormat d = new SimpleDateFormat("E MMM dd HH" +
            ":mm:ss yyyy Z", Locale.ENGLISH);
    
    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            date = new Date(0);
        } else {
            date = new Date();
        }
        commitID = sha1(this.message, d.format(this.date));
    }

    public String getCommitID() {
        return commitID;
    }

    public String getMessage() {
        return message;
    }

    public void inheritParentMap() {
        if (parent == null) {
            return;
        }
        File parentCommitFile = join(Repository.COMMITS, parent);
        fileSnapshot = readObject(parentCommitFile, Commit.class).fileSnapshot;
    }
    
    public void addOrModifyMapKV(String filename, String snapshot) {
        if (!fileSnapshot.containsKey(filename)){
            fileSnapshot.put(filename, snapshot);
        } else {
            fileSnapshot.replace(filename, snapshot);
        }
    }
    
    public void removeMapKV(String filename) {
        fileSnapshot.remove(filename);
    }
    
    public boolean hasFileComparedToCWD(String filename) {
        File fileInCWD = join(Repository.CWD, filename);
        String sha1OfCWD = getSha1OfFile(fileInCWD);
        if (fileSnapshot.containsKey(filename)) {
            String sha1OfCommit = fileSnapshot.get(filename);
            return sha1OfCommit.equals(sha1OfCWD);
        } else {
            return false;
        }
    }
    
    
    
    public boolean hasFile(String filename) {
        return fileSnapshot.containsKey(filename);
    }
    
    public String getFileSnapshotValue(String filename) {
        return fileSnapshot.get(filename);
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public String getAnotherParent() {
        return this.anotherParent;
    }
    
    public void printLog() {
        System.out.println("===");
        System.out.println("commit " + commitID);
        if (anotherParent != null) {
            System.out.println("Merge: " + parent.substring(0, 7)
                    + " " + anotherParent.substring(0, 7));
        }
        
        System.out.println("Date: " + d.format(date));
        System.out.println(message);
        System.out.println();
    }
    
    
    
    public Map<String, String> getFileSnapshot() {
        return fileSnapshot;
    }
    
    public void copySnapshotToCWD(String filename) {
        File targetFile = join(Repository.CWD, filename);
        String snapshot = fileSnapshot.get(filename);
        File sourceFile = join(Repository.OBJECTS, filename, snapshot);
        writeContents(targetFile, readContents(sourceFile));
    }
    
    public void copyAllSnapshotsInCommitToCWD() {
        for (String filename : fileSnapshot.keySet()) {
            copySnapshotToCWD(filename);
        }
    }
    
    public String getParent() {
        return parent;
    }

    public void setSplitToTrue() {
        isSplit = true;
    }
    
    public boolean getIsSplit() {
        return isSplit;
    }

    public void setAnotherParent(String anotherParent) {
        this.anotherParent = anotherParent;
    }
}
