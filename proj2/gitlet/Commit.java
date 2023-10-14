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
public class Commit implements Serializable {

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private String parent;
    private String commitID;
    private Map<String, String> fileToBlob = new HashMap<>();
    private String mergedID;
    
    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            this.timestamp = new Date(0).toString();
        } else {
            this.timestamp = new Date().toString();
            this.fileToBlob = getModifiedMap();
        }
        this.commitID = Utils.sha1(this);
        

        System.out.println(message);
    }
    
    public void addMapKey(String filename, String blob) {
        fileToBlob.put(filename, blob);
    }
    
    public String getCommitID() {
        return commitID;
    }
    
    
    private Map getModifiedMap() {
        Map map = getParentMap();
        // If folder exists, meaning some filename need to be deleted.
        File fileFolderForDelete = join(Repository.REMOVEAREA, parent);
        if (fileFolderForDelete.exists()) {
            File[] fileForDelete = fileFolderForDelete.listFiles();
            for (File file : fileForDelete) {
                map.remove(file.getName());
            }
        }
        return map;
    }

    private Map getParentMap() {
        File parent = join(Repository.OBJECTS, this.parent);
        Map parentMap = readObject(parent, Commit.class).fileToBlob;
        return parentMap;
    }
    
    public boolean doesFileExists(String filename) {
        return fileToBlob.containsKey(filename);
    }
    
    public String getBlobOfFile(String filename) {
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

    public String getMessage() {
        return message;
    }
}
