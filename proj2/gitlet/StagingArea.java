package gitlet;

import java.io.File;
import static gitlet.Utils.*;

public class StagingArea {
    public static final File addArea = Repository.ADDAREA;
    public static final File rmArea = Repository.REMOVEAREA;
    
    
    public void addToAddArea(File file) {
        copyFile(file, addArea);
    }
    
    public void removeFromAddArea(String filename) {
        File fileInAddArea = join(addArea, filename);
        if (fileInAddArea.exists()) {
            fileInAddArea.delete();
        }
    }
    
    
}
