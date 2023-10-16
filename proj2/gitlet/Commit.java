package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
@SuppressWarnings("unchecked")
public class Commit implements Serializable {

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private String parent;
    private String commitID;
    private Map<String, String> fileToBlob = new HashMap<>();
    private String mergedID;
    
    public Commit(String message, String parent) {
        changeMessage(message);
        this.parent = parent;
        if (this.parent == null) {
            this.timestamp = new Date(0).toString();
        } else {
            this.timestamp = new Date().toString();
        }
    }
    
    public void addMapKey(String filename, String blob) {
        fileToBlob.put(filename, blob);
    }
    
    public void removeMapKey(String filename) {
        fileToBlob.remove(filename);
    }
    
    public String getCommitID() {
        commitID = sha1(this.toString());
        return commitID;
    }
    
    public boolean hasFile(String filename) {
        return fileToBlob.containsKey(filename);
    }
    
    public String getBlobOfFile(String filename) {
        if (!fileToBlob.containsKey(filename)) {
            return null;
        }
        return fileToBlob.get(filename);
    }
    
    public void printLog() {
        System.out.println("===");
        System.out.println("commit " + this.commitID);
        if (this.mergedID != null) {
            String firstCommit = this.parent.substring(0, 7);
            String secondCommit = this.mergedID.substring(0, 7);
            System.out.println("Merge: " + firstCommit + " " + secondCommit);
        }
        System.out.println("Date: " + this.timestamp);
        System.out.println(this.message);
        System.out.println();
    }
    
    public String getParentCommitID() {
        return parent;
    }

    public void copyFileToCWD(String filename) {
        String blob = fileToBlob.get(filename);
        File fileBlob = join(Repository.OBJECTS, filename, blob);
        File fileInCWD = join(Repository.CWD, filename);
        writeContents(fileInCWD, readContentsAsString(fileBlob));
    }
    
    public void changeMessage(String message) {
        this.message = message;
        System.out.println(message);
    }

    public String getParent() {
        return parent;
    }
}
