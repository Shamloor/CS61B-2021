package gitlet;




/** Represents a gitlet repository.
 *  @author Willson Yu
 */

/** .gitlet folder will contain these folders and files.
 *  .gitlet
 *  |---objects
 *  |    |---commits
 *  |    |---(hash of filename)
 *  |        |---snapshot of files
 *  |---area
 *  |   |---add
 *  |   |---remove
 *  |---branches
 *  |---HEAD
 * */
public class Repository {
    private InitCommand initCommand = new InitCommand();
    private AddCommand addCommand = new AddCommand();
    private CommitCommand commitCommand = new CommitCommand();
    private RmCommand rmCommand = new RmCommand();
    private LogCommand logCommand = new LogCommand();
    private FindCommand findCommand = new FindCommand();
    private StatusCommand statusCommand = new StatusCommand();
    private CheckoutCommand checkoutCommand = new CheckoutCommand();
    private BranchCommand branchCommand = new BranchCommand();
    private RmBranchCommand rmBranchCommand = new RmBranchCommand();
    private ResetCommand resetCommand = new ResetCommand();
    private MergeCommand mergeCommand = new MergeCommand();
    
 
    public static void init() {
        InitCommand.init();
        // New branch.
        BranchCommand.branch("master");
    }

    public static void add(String filename) {
        AddCommand.add(filename);
    }

    public static void commit(String message) {
        CommitCommand.commit(message);
    }

    public static void rm(String filename) {
        RmCommand.rm(filename);
    }

    public static void log() {
        LogCommand.log();
    }

    public static void globalLog() {
        LogCommand.globalLog();
    }

    public static void find(String commitMessage) {
        FindCommand.find(commitMessage);
    }

    public static void status() {
        StatusCommand.status();
    }

    public static void checkout1(String filename) {
        CheckoutCommand.checkout1(filename);
    }

    public static void checkout2(String commitID, String filename) {
        CheckoutCommand.checkout2(commitID, filename);
    }

    public static void checkout3(String branchName) {
        CheckoutCommand.checkout3(branchName);
    }

    public static void branch(String branchName) {
        BranchCommand.branch(branchName);
    }

    public static void rmBranch(String branchName) {
        RmBranchCommand.rmBranch(branchName);
    }

    public static void reset(String commmitID) {
        ResetCommand.reset(commmitID);
    }

    public static void merge(String givenBranch) {
        MergeCommand.merge(givenBranch);
    }
    
}
