package screenpac.model;


import screenpac.extract.Constants;
import screenpac.extract.GameObjects;
import screenpac.controllers.AgentInterface;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter implements Constants, AgentInterface {
    public int action(GameStateInterface gs) {
        return getDirection(gs);
    }
    // extend this in a meaningful way next time...

    static int noKey = -1;
    int key = noKey;

    public int getDirection(GameStateInterface gs) {

        if (key == KeyEvent.VK_DOWN) {
            return DOWN;
        }
        if (key == KeyEvent.VK_UP) {
            return UP;
        }
        if (key == KeyEvent.VK_RIGHT) {
            return RIGHT;
        }
        if (key == KeyEvent.VK_LEFT) {
            return LEFT;
        }
        return NEUTRAL;
    }

    public void keyPressed(KeyEvent e) {
        // System.out.println(e);
        key = e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
        key = noKey;
    }

    public int getDirection(GameObjects gs) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
