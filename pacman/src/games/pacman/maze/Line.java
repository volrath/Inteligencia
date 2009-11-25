package games.pacman.maze;

import games.pacman.view.Drawable;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 12-Oct-2004
 * Time: 22:33:22
 * To change this template use File | Settings | File Templates.
 */
public class Line implements Drawable {
    int x1, y1, x2, y2;
    boolean pill;

    public Line(int x1, int y1, int x2, int y2) {
        this(x1, y1, x2, y2, true);
    }

    public Line(int x1, int y1, int x2, int y2, boolean pill) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.pill = pill;
    }

    public void rescale(double fac) {
        x1 = (int) (fac * x1);
        y1 = (int) (fac * y1);
        x2 = (int) (fac * x2);
        y2 = (int) (fac * y2);
    }

    public void draw(Graphics g, int w, int h) {
        draw(g, w, h, scale);
    }

    public void draw(Graphics g, int w, int h, int fac) {
        //To change body of implemented methods use File | Settings | File Templates.
        if (g != null) {
            g.setColor(Color.blue);
            g.drawLine(fac * x1, fac * y1, fac * x2, fac * y2);
        }
    }

    public boolean in(int px, int py) {
        return between(x1, px, x2) && between(y1, py, y2);
    }

    private static boolean between(int a, int b, int c) {
        // return true of b is between a and c
        return (a-b) * (b-c) >= 0;
    }
}
