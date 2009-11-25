package games.pacman.core;

import games.pacman.controllers.PacController;
import games.pacman.view.Mobile;
import games.pacman.maze.MazeNode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 30-Sep-2004
 * Time: 09:26:07
 * To change this template use Options | File Templates.
 */

public class PacMan extends Mobile {

    int ix;
    int w;
    // Node node;
    int curDir;
    int desired;
    public MazeNode previous;

    public void reset() {
        desired = PacController.LEFT;
        curDir = PacController.LEFT;
        previous = null;
    }

    public PacMan() {
        color = Color.yellow;
    }

    public void move() {
        // try and move in the desired direction
        MazeNode next = current.next(desired);

        if (next != null) {
            previous = current;
            current = next;
            curDir = desired;
        } else {
            next = current.next(curDir);
            if (next != null) {
                previous = current;
                current = next;
            }
        }
    }

    public int tryEat() {
        return current.getPill();
    }

    public void setDesire(int dir) {
        desired = dir;
    }

    public void draw(Graphics g, int w, int h, int fac) {
        if (current != null) {
            g.setColor(color);
            g.fillOval(fac * (current.x - rad), fac * (current.y - rad), fac * 2 * rad, fac * 2 * rad);
        }
    }
}
