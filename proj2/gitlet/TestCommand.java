package gitlet;
import static org.junit.Assert.*;
import org.junit.Test;

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
        Repository.testC();
    }
}
