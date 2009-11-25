package games.pacman.controllers;

import games.pacman.controllers.PacController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 13-Oct-2004
 * Time: 15:39:43
 * To change this template use Options | File Templates.
 */
public class KeyController extends KeyAdapter implements PacController {
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
        return CENTRE;
    }

    public void keyPressed(KeyEvent e) {
        // System.out.println(e);
        key = e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
        key = noKey;
    }
}
