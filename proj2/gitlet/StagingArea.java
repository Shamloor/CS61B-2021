package gitlet;

import java.io.File;
import static gitlet.Utils.*;

public class StagingArea {
    public static final File addArea = Repository.ADDAREA;
    public static final File rmArea = Repository.REMOVEAREA;
    
    
    public boolean addToAddArea(File file) {
        if (file.exists()) {
            copyFile(file, addArea);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addToAddArea(String filename) {
        File file = join(Repository.CWD, filename);
        return addToAddArea(file);
    }

    public boolean removeFromAddArea(File file) {
        if (file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeFromAddArea(String filename) {
        File fileInAddArea = join(addArea, filename);
        return removeFromAddArea(fileInAddArea);
    }
    
    public boolean addToRemoveArea(File file) {
        if (file.exists()) {
            copyFile(file, rmArea);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addToRemoveArea(String filename) {
        File file = join(Repository.CWD, filename);
        return addToRemoveArea(file);
    }
}
