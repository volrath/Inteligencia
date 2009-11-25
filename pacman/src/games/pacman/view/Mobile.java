package games.pacman.view;

import games.pacman.view.Drawable;
import games.pacman.maze.MazeNode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 14-Oct-2004
 * Time: 15:33:32
 * To change this template use Options | File Templates.
 */
public class Mobile implements Drawable {
    public MazeNode current;
    public int rad = 3;
    public Color color;
    static int overlapRad = 1;

    public boolean overlap(Mobile m) {
        return Math.abs(current.x - m.current.x) <= overlapRad &&
                Math.abs(current.y - m.current.y) <= overlapRad;
    }

    public void rescale(double fac) {
        // x = (int) (x * fac);
        // y = (int) (y * fac);
        rad = (int) (rad * fac);
    }

    public void setPosition(MazeNode current) {
        this.current = current;
    }

    public void draw(Graphics g, int w, int h) {
        draw(g, w, h, scale);
    }

    public void draw(Graphics g, int w, int h, int fac) {
        if (current != null) {
            g.setColor(color);
            g.fillRect(fac * (current.x - rad), fac * (current.y - rad), fac * 2 * rad, fac * 2 * rad);
        }
    }
}
