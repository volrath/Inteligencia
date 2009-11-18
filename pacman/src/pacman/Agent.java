package pacman;

import static pacman.MsPacInterface.width;

import java.awt.*;

import games.math.Vector2d;

/**
 * User: Simon
 * Date: 05-Mar-2007
 * Time: 00:21:34
 * Agent class
 */

public class Agent implements Drawable, PacAgent, Constants {
    int x, y;
    int w, h;
    Color color;

    static int[] dirs = {-width, 1, width, -1};
    static Vector2d[] vDirs = {
            new Vector2d(0, -1),
            new Vector2d(1, 0),
            new Vector2d(0, 1),
            new Vector2d(-1, 0),
    };
    static int pill = MsPacInterface.pill & 0xFFFFFF;
    static int pacMan = MsPacInterface.pacMan & 0xFFFFFF;
    static int lips = MsPacInterface.blinky & 0xFFFFFF;

    // d is the distance in each direction to the nearest wall
    int[] d;
    int move;
    int currentDirection;

    Vector2d cur, prev, tmp;

    public Agent() {
        d = new int[]{20, 20, 20, 20};
        prev = new Vector2d();
        cur = new Vector2d();
        tmp = new Vector2d();
        System.out.println("new agent");
    }

    public Agent(ConnectedSet cs, int[] pix) {
        this();
        update(cs, pix);
    }

    public void update(ConnectedSet cs, int[] pix) {
        cs.validate();
        w = cs.width;
        h = cs.height;
        prev.set(x, y);
        x = cs.xMin + w / 2;
        y = cs.yMin + h / 2;
        cur.set(x, y);
        setDir(prev, cur);
        // now check lines
        // System.out.println(cur + " : " + x + " : " + y);
        for (int i = 0; i < dirs.length; i++) {
            d[i] = search(x + y * width, pix, dirs[i]);
            // System.out.println(i + "\t " + d[i]);
        }
        this.color = cs.c;
    }

    public void setDir(Vector2d prev, Vector2d cur) {
        tmp.set(cur);
        tmp.subtract(prev);
        // System.out.println(tmp);
        if (tmp.equals(NEUTRAL)) currentDirection = NEUTRAL;
        if (tmp.scalarProduct(vUp) > 0) currentDirection = UP;
        if (tmp.scalarProduct(vRight) > 0) currentDirection = RIGHT;
        if (tmp.scalarProduct(vDown) > 0) currentDirection = DOWN;
        if (tmp.scalarProduct(vLeft) > 0) currentDirection = LEFT;
    }

    public int move(GameState gs) {
        // let's say we move towards the
        // simple controller that tries to move towards the nearest power pill
        // set up a rogue value for the move, and a large value for the closest pill
        move = -1;
        double best = 100000;
        for (int i = 0; i < dirs.length; i++) {
            // why test if d[i]
            if (d[i] > 12) {
                // set tmp vector to current postition of agent
                tmp.set(cur);
                // now add in the current direction - this sets the position
                // to where the agent would be after taking the proposed action for one time step
                tmp.add(vDirs[i]);
                //System.out.println(i + "\t " + eval(tmp, gs));
                // now call eval, and update
                double curScore = eval(tmp, gs);
                if (curScore < best) {
                    move = i;
                    best = curScore;
                }
            }
        }
        if (move < 0) {
            System.out.println("Move error: " + move);
            // move = 3;
        }
        move += 1;
        //System.out.println(move + " \t " + currentDirection);
        if (move == currentDirection) {
            // System.out.println("Already moving that way");
            return NEUTRAL;
        } else {
            return move;
        }
    }

    public double eval(Vector2d pos, GameState gs) {
        if (gs.closestPill != null) {
            return pos.dist(gs.closestPill);
        } else {
            return 0;
        }
    }

    private int search(int p, int[] pix, int delta) {
        int len = 0;
        int pp = pix[p] & 0xFFFFFF;
        try {
            while (pp == 0 || pp == pacMan || pp == pill || pp == lips) {
                len++;
                if (len > width) return width;
                p += delta;
                pp = pix[p] & 0xFFFFFF;
            }
        } catch (Exception e) {
        }
        // System.out.println(pp);
        return len;
    }


    public void draw(Graphics gg, int ww, int hh) {
        Graphics2D g = (Graphics2D) gg;
        g.setColor(color);
        g.fillRect(x - w / 2, y - h / 2, w, h);
        // now the four lines
        g.setColor(Color.green);
        g.drawLine(x, y, x, y - d[0]);
        g.drawLine(x, y, x + d[1], y);
        g.drawLine(x, y, x, y + d[2]);
        g.drawLine(x, y, x - d[3], y);
        g.setColor(Color.red);
        if (move > 0) {
            // g.setStroke();
            tmp.set(vDirs[move - 1]);
            tmp.mul(20);
            g.drawLine(x, y, x + (int) tmp.x, y + (int) tmp.y);
        }
        // g.setColor(Color.magenta);
        // g.fillOvl
    }

}
