package gitlet;
import static gitlet.Utils.*;
import static gitlet.Utils.readContents;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCommand {
    
    @Test
    public void testInit() {
        Repository.init();
    }
    
    @Test
    public void testAdd() {
        Repository.add("test");
    }
    
    @Test
    public void testCommit() {
        Repository.commit("third commit");
    }
    @Test
    public void testLog() {
        Repository.log();
    }
    
    @Test
    public void testGlobalLog() {
        Repository.globalLog();
    }
    
    @Test
    public void testCheckout1() {
        Repository.checkout1("test");
    }
    
    @Test
    public void testCheckout2() {
        Repository.checkout2("104", "test");
    }
    
    @Test
    public void testTestC() {
        File a = join(Repository.CWD, "a");
        System.out.println(readContents(a));
    }
    
    @Test
    public void testStatus() {
        Repository.status();
    }
    
    @Test
    public void integrationTest() {
        Repository.init();
        Repository.add("g.txt");
        Repository.add("f.txt");
        Repository.commit("Two files");
        Repository.rm("f.txt");
        Repository.status();
    }
    
    @Test
    public void integrationTest2() {
        Repository.init();
        
        Repository.branch("B1");
        Repository.branch("B2");
        
        Repository.checkout3("B1");
        Repository.add("test");
        Repository.commit("B1 second node");
        
        Repository.checkout3("B2");
        Repository.add("test1");
        Repository.commit("B2 second node");
        
        Repository.branch("C1");
        Repository.add("test2");
        Repository.commit("B2 third commit C1 first commit");
        
        Repository.checkout3("B1");
        
        //Repository.merge("C1");
        Repository.globalLog();
        
    }
    
    @Test
    public void mergeConflict() {
        File currentFile = join(Repository.CWD, "test1");
        File givenFile = join(Repository.CWD, "test2");
        byte[] head = "<<<<<<< HEAD\n".getBytes();
        byte[] split = "=======\n".getBytes();
        byte[] end = ">>>>>>>".getBytes();

        File target = join(Repository.CWD, "test");
        writeContents(target, head, readContents(currentFile), split,
                readContents(givenFile), end);
    }
}
