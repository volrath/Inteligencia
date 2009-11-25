package screenpac.test;

import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.awt.*;
import java.awt.peer.RobotPeer;
import java.awt.image.BufferedImage;

import sun.awt.ComponentFactory;


/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 30-Mar-2009
 * Time: 10:00:41
 * To change this template use File | Settings | File Templates.
 */

public class TestPeerRobot {
    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        RobotPeer peer;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        peer = ((ComponentFactory) toolkit).createRobot(robot, screen);

        StatSummary ss = new StatSummary();
        for (int i = 0; i < 100; i++) {
            ElapsedTimer t = new ElapsedTimer();
            int[] pix = peer.getRGBPixels(new Rectangle(0, 0, 260, 260));
            long et = t.elapsed();
            ss.add(et);
        }
        System.out.println(ss);
    }
}