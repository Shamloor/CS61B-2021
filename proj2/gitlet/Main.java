package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     *  java gitlet.Main add hello.txt
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
        }
        
        String firstArg = args[0];
        
        if (firstArg.equals("init")) {
            Repository.init();
        }
        if (firstArg.equals("add")) {
            Repository.add(args[1]);
        }
        if (firstArg.equals("commit")) {
            Repository.commit(args[1]);
        }
        if (firstArg.equals("rm")) {
            Repository.rm(args[1]);
        }
        if (firstArg.equals("log")) {
            Repository.log();
        }
        if (firstArg.equals("global-log")) {
            Repository.global_log();
        }
        if (firstArg.equals("find")) {
            Repository.find(args[1]);
        }
        
    }
}
