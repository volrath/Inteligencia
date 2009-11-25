package games.pacman.maze;

import games.pacman.controllers.PacController;
import games.pacman.view.Drawable;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 30-Sep-2004
 * Time: 09:42:18
 * To change this template use Options | File Templates.
 */
public class MazeNode implements Drawable {

    public static int NO_PILL = 0;
    public static int PILL = 10;
    public static int POWER_PILL = 50;

    int p; // the pixel position = x + y * w
    public int x;
    public int y; // also keep these for convenience
    public int ix; // index of this node in the array of all graph nodes
    public MazeNode[] next;
    MazeNode[] dirs;
    int n; // number of neighbours
    static int[] dx = PacController.dx;
    static int[] dy = PacController.dy;
    public int pill;
    static Color color = Color.white;
    public double junctionDist;

    public int getPill() {
        int ret = pill;
        // if it was not already eaten, it is now!!!
        pill = NO_PILL;
        return ret;
    }

    public MazeNode(int p, int x, int y, int ix) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.ix = ix;
    }

    public String toString() {
        return x + " : " + y + " : " + n;
    }

    public MazeNode[] succ() {
        return next;
    }

    public boolean canMove(int dir) {
        return dirs[dir] != null;
    }

    public MazeNode next(int dir) {
        if (dir >= dirs.length) {
            System.out.println("Ghost neutral not allowed");
            return null;
        }

        return dirs[dir];
    }

    public MazeNode getOtherNext(int choice, MazeNode tabu) {
        int ix = 0;
        // choose among the allowed choices
        for (int i=0; i<n; i++) {
            if (next[i] != null && next[i] != tabu) {
                // this is a possible choice
                if (choice == ix) {
                    return next[i];
                }
                ix++;
            }
        }
        System.out.println(this + " with tabu: " + tabu);
        return null;
    }

    public void findNext(MazeNode[][] ng, int w) {
        // w is the width of the array
        // look in each direction for the successor nodes
        // find out how many first, then create the successor array

        n = nNext(ng, w);
        // System.out.println(this);
        next = new MazeNode[n];
        // the change to 5 now includes the neutral node
        // which points pointlessly to itself!
        // this is to maintain compatibility with another version
        dirs = new MazeNode[4];
        int ix = 0;
        // System.out.println("Finding for: " + this);
        for (int i=0; i<dirs.length; i++) {
            MazeNode node = g(ng, x + dx[i], y+dy[i], w);
            dirs[i] = node;
            // System.out.println(i + " :  " + node);
            if (node != null) {
                next[ix++] = node;
            }
        }
    }

    public int nNext(MazeNode[][] ng, int w) {
        int n = f(ng, x+1, y, w) + f(ng, x-1, y, w) + f(ng, x, y+1, w) + f(ng, x, y-1, w);
        return n;
    }

    private MazeNode g(MazeNode[][] ng, int x, int y, int w) {
        try {
            // the % w takes care of wraparound -
            // this is important for the tunnels
            // on some mazes it would also be necessary to implement this
            // for the y-axis too
            return ng[(x+w)%w][y];
        } catch(Exception e) {}
        return null;
    }

    private int f(MazeNode[][] ng, int x, int y, int w) {
        try {
            if (ng[(x+w)%w][y] != null) {
                // System.out.println("1");
                return 1;
            }
        } catch(Exception e) {}
        // System.out.println("0");
        return 0;
    }


    public void draw(Graphics g, int w, int h) {
        draw(g, w, h, scale);
    }

    public void draw(Graphics g, int w, int h, int fac) {
        if (pill != NO_PILL) {
            int rad = 1 + pill / 25;
            g.setColor(color);
            g.fillOval(fac * (x - rad), fac * (y - rad), fac * 2 * rad, fac * 2 * rad);
        }
    }

    public void rescale(double x) {}
}
