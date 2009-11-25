package screenpac.test;

import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 30-Mar-2009
 * Time: 10:00:41
 * To change this template use File | Settings | File Templates.
 */

public class TestRobot {
    public static void main(String[] args) throws Exception {
        Robot r = new Robot();
        StatSummary ss = new StatSummary();
        for (int i=0; i<100; i++) {
            ElapsedTimer t = new ElapsedTimer();
            BufferedImage bc = r.createScreenCapture(new Rectangle(0, 0, 260, 260));
            long et = t.elapsed();
            ss.add(et);
        }
        System.out.println(ss);
    }
}
