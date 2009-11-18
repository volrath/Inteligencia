package pacman;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PacMover implements Constants {
    Robot robot;
    boolean keyPressed;
    int curKey;
    int curDir;
    // not used when waitForIdle isset to false
    static int autoDelay = 20;
    static Random r = new Random();

    // these are related to the definitions in PacController
    static int[] keys = {-1, KeyEvent.VK_UP, KeyEvent.VK_RIGHT,
                         KeyEvent.VK_DOWN, KeyEvent.VK_LEFT};

    public void move(int direction) {
        // release the current key if it is pressed
        if (keyPressed) {
//            robot.keyRelease( curKey );
//            keyPressed = false;
        }

        // now work out the action
        if (direction > 0 && direction < keys.length) {

            curKey = keys[direction];
            robot.keyPress(curKey);
            robot.keyRelease(curKey);
            robot.waitForIdle();
//            keyPressed = true;
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
            //System.out.println(robot.getAutoDelay());
            //System.out.println(robot.isAutoWaitForIdle());
            curKey = -1;
            // robot.waitForIdle();
        } catch(Exception e) {}
    }
}
