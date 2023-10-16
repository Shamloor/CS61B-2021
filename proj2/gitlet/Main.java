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
        
//        if (args[0].equals("init")) {
//            Repository.init();
//        }
//        if (args[0].equals("add")) {
//            Repository.add(args[1]);
//        }
//        if (args[0].equals("commit")) {
//            Repository.commit(args[1]);
//        }
//        if (args[0].equals("rm")) {
//            Repository.rm(args[1]);
//        }
//        if (args[0].equals("log")) {
//            Repository.log();
//        }
//        if (args[0].equals("global-log")) {
//            Repository.global_log();
//        }
//        if (args[0].equals("find")) {
//            
//        }
//        if (args[0].equals("checkout")) {
//            if (args[1].equals("--")) {
//                
//            } else if (args[2].equals("--")) {
//                
//            } else {
//                
//            }  
//        }

//        Repository.init();
//        Repository.add("testFile");
//        Repository.commit("first commit.");
//        Repository.rm("testFile");
//        Repository.commit("second commit");
        Repository.log();
//        Repository.global_log();
        //Repository.checkout("testFile");
    }
}
