package screenpac.control;

import screenpac.extract.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PacMover implements Constants {
    public Robot robot;
    boolean keyPressed;
    int curKey;
    int curDir;
    // not used when waitForIdle isset to false
    static int autoDelay = 2;
    static Random r = new Random();

    // these are related to the definitions in PacController
    static int[] keys = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT,
            KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, -1};

    public void move(int direction) {
        // robot.keyRelease( curKey );

        // release the current key if it is pressed
        if (keyPressed) {
            robot.keyRelease(curKey);
            keyPressed = false;
        }

        // now work out the action
        if (direction >= 0 && direction < keys.length-1) {

            curKey = keys[direction];
            robot.keyPress(curKey);
            // robot.keyRelease(curKey);
            robot.waitForIdle();
            keyPressed = true;
        }
        curDir = direction;
    }

    public void randMove() {
        move(r.nextInt(keys.length));
    }

    public PacMover() {
        keyPressed = false;
        try {
            robot = new Robot();
            // prevent the robot from being flooded with too many events
            robot.setAutoWaitForIdle(false);
            robot.setAutoDelay(autoDelay);
            System.out.println(robot.getAutoDelay());
            System.out.println(robot.isAutoWaitForIdle());
            curKey = -1;
            // robot.waitForIdle();
        } catch (Exception e) {
        }
    }

    public void finalize() {
        System.out.println("Finalized");
        if (keyPressed) {
            robot.keyRelease(curKey);
        }
    }
}
