package gitlet;

import java.util.List;
import static gitlet.Process.*;

public class FindCommand {
    public static void find(String commitMessage) {
        // Get commits in COMMITS
        List<Commit> commits = getCommits();
        boolean hasMessage = false;

        // Loop: get message variable of Commit object and compare strings.
        for (Commit commit : commits) {
            if (commitMessage.equals(commit.getMessage())) {
                System.out.println(commit.getCommitID());
                hasMessage = true;
            }
        }

        // If no message exists.
        if (!hasMessage) {
            System.out.println("Found no commit with that message.");
        }
    }
}
