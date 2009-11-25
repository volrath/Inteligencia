package screenpac.extract;

import utilities.JEasyFrame;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.awt.*;
import java.awt.peer.RobotPeer;
import java.util.ArrayList;
import java.util.HashSet;

import screenpac.control.PacMover;
import screenpac.control.DirController;
import screenpac.control.LeftRight;
import sun.awt.ComponentFactory;


public class CaptureTest implements Constants {
    // delay between each screen capture
    static int delay = 1000;
    static boolean display = true;
    int left, top, w, h;
    RobotPeer peer;

    public static void main(String[] args) throws Exception {
        CaptureTest ct = new CaptureTest(400, 70, 260, 260);
        // doing a tiny window actually made no difference to the speed
        // CaptureTest ct = new CaptureTest(400, 70, 26, 26);
        StatSummary ss = new StatSummary();
        PacMover pm = new PacMover();
        DirController controller;
        controller = new LeftRight();
        // int[] dir = {left, right};
        int n = 0;

        while (true) {
            try {
                Thread.sleep(delay);
                ElapsedTimer t = new ElapsedTimer();
                // System.out.println(t);
                ct.analyse();
                // System.out.println("Delay = " + delay);
                int dir = controller.getDirection(ct.ex.gs);
                pm.move(dir);
                // System.out.println(t);
                // pm.randMove();
                long el = t.elapsed();
                // System.out.println(el);
                ss.add(el);
                // System.out.println(ss.mean());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // System.out.println(ss);
    }

    public CaptureTest(int left, int top, int w, int h) throws Exception {
        this.left = left;
        this.top = top;
        this.w = w;
        this.h = h;
        robot = new Robot();
        pixels = new int[w * h];
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
        if (toolkit instanceof ComponentFactory) {
            peer = ((ComponentFactory)toolkit).createRobot(robot, screen);
        }
        // peer.
        ex = new Extractor(w, h);
        sd = new SimpleDisplay(w, h);
        new JEasyFrame(sd, "Extracted", true);
    }

    public void analyse() {
        int[] pix = getPixels();
        ex.consume(pix);
        // System.out.println("N Objects: " + al.size());
//        System.out.println(ex.sx.min() + "\t " + ex.sx.max());
//        System.out.println(ex.sy.min() + "\t " + ex.sy.max());
//        System.out.println(ex.sx.max() - ex.sx.min());
//        System.out.println(ex.sy.max() - ex.sy.min());
//        System.out.println(ex.sx.mean() + "\t " + ex.sy.mean());
//        System.out.println("Ex Top and Left: " + ex.left + "\t " + ex.top);
        sd.updateObjects(ex.gs);
    }

    int[] pixels;
    Robot robot;
    Extractor ex;
    SimpleDisplay sd;

    static HashSet<Integer> colors = new HashSet<Integer>();

    static {
        colors.add(blinky);
        colors.add(pinky);
        colors.add(inky);
        colors.add(sue);
        colors.add(pacMan);
        colors.add(edible);
        colors.add(pill);
    }

    public CaptureTest() throws Exception {
        this(0, 0, 500, 500);
    }

    // int[] pix = new int[260 * 260];
    public int[] getPixels() {
//        BufferedImage im = robot.createScreenCapture(
//                new Rectangle(left, top, w, h));
//        im.getRGB(0, 0, w, h, pixels, 0, w);
        return peer.getRGBPixels(new Rectangle(left, top, w, h));
//        return pix; // new int[w * h];
//        return pixels;
    }
}
