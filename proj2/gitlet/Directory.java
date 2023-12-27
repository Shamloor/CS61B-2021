package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Directory {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS = join(GITLET_DIR, "objects");
    public static final File AREA  = join(GITLET_DIR, "area");
    public static final File BRANCHES = join(GITLET_DIR, "branches");
    /** The second line will store current branch. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    public static final File COMMITS = join(OBJECTS, "commits");
    public static final File ADD = join(AREA, "add");
    public static final File REMOVE = join(AREA, "remove");
}
