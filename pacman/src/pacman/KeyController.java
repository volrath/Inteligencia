package pacman;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * User: Simon
 * Date: 13-Oct-2007
 * Time: 22:35:00
 */
public class KeyController extends KeyAdapter implements Constants, PacAgent {
    // extend this in a meaningful way next time...

    static int noKey = -1;
    int key = noKey;

    public int getDirection() {

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

    public int move(GameState gs) {
        return getDirection();
    }
}
