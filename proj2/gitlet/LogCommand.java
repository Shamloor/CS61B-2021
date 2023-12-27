package gitlet;

import java.util.List;

import static gitlet.Process.*;

public class LogCommand {
    public static void log() {
        // Get current Commit object.
        Commit commit = getCurrentCommit();

        // Print log.
        while (true) {
            commit.printLog();
            if (commit.getParent() == null) {
                break;
            }
            commit = getSpecifiedCommit(commit.getParent());
        }
    }

    public static void globalLog() {
        // Get commits in COMMITS
        List<Commit> commits = getCommits();

        // Loop: call printLog() function.
        for (Commit commit : commits) {
            commit.printLog();
        }
    }
}
