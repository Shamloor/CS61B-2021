package gitlet;

import java.io.File;
import static gitlet.Utils.*;

public class StagingArea {
    public static final File addArea = Repository.ADDAREA;
    public static final File rmArea = Repository.REMOVEAREA;
    
    private boolean beRemoved;
    
    public void addToAddArea(File file) {
        // Initialize folder if it's not exist.
        if (!addArea.exists()) {
            addArea.mkdir();
        }
        
        String sha1ofSourceFile = Utils.sha1(file);
        File fileInAddArea = join(addArea, file.getName());
        
        // When file in add area exists and has a different version.
        if (fileInAddArea.exists()) {
            String sha1ofTargetFile = Utils.sha1(fileInAddArea);
            if (sha1ofSourceFile.equals(sha1ofTargetFile)) {
                return;
            }
        }
        
        beRemoved = false;
    }
    
    public void removeFromAddArea(File file) {
        File fileInAddArea = join(addArea, file.getName());
        if (fileInAddArea.exists()) {
            fileInAddArea.delete();
        }
    }
    
    public boolean canBeRemoved(File file) {
        return beRemoved;
    }
    
}
