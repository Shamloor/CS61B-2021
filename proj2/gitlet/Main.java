package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Willson Yu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.exitWithError("Please enter a command.");
        }
        
        String firstArg = args[0];
        switch(firstArg) {
            case "init":gitgit
                // TODO: handle the `init` command
                if (args.length != 1) {
                    Utils.exitWithError("Incorrect operands.");
                }
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if (args.length != 2) {
                    Utils.exitWithError("Incorrect operands.");
                }
                Repository.add(args[2]);
                break;
            // TODO: FILL THE REST IN
            default:
                Utils.exitWithError("No command with that name exists.");
        }
    }
    
}
