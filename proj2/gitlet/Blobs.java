package gitlet;

import java.io.File;

import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class Blobs {
    
    /** The filename of one blob which was made by SHA1 function. */
    private String blobID;
    /** information about modification. */
    private String modifyInfo;
    private static final File BLOBS = Repository.OBJECTS; 
    
    
    public static String createEachBlob(File file) {
        // Create directory and file.
        File folder = join(BLOBS, file.getName());
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        String sha1ofFile = Utils.sha1(file.getName(), readContentsAsString(file));
        File sha1File = join(folder, sha1ofFile);
        
        // Write contents.
        String contents = Utils.readContentsAsString(file);
        Utils.writeContents(sha1File, contents);
        
        // Return blob name.
        return sha1ofFile;
    }
    
    public static void removeTrackedFiles(String fileBlob) {
        
    }
}
