package gui;

import javax.swing.*;
import java.awt.*;
/**
 * @author EdwinD
 * @create 2020.08.19 上午 08:31
 * @desc Swing
 **/
public class JFrameDemo1 {
    public static void main(String[] args) {
        new JFrameDemo1().init();
    }
    //    init()初始化。
    public void init(){
        JFrame jFrame = new JFrame("这是我们的JFrame窗口。");
        jFrame.setVisible(true);
        jFrame.setBackground(Color.BLUE);
        jFrame.setBounds(377, 377, 277, 277);

//        设置文字。 JLabel
        JLabel jLabel = new JLabel("Welcome to EdwinD's Way.");
        jFrame.add(jLabel);

//        关闭事件
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
