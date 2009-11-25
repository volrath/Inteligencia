package games.pacman.ghost;

import games.pacman.core.PacMan;
import games.pacman.features.NodeEvaluator;
import games.pacman.view.Mobile;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;
import games.pacman.controllers.PacController;

import java.awt.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 30-Sep-2004
 * Time: 09:26:07
 * To change this template use Options | File Templates.
 */

public class GhostShell extends Mobile {

    static int edibleTime = 100;
    static Color edibleColor = Color.blue;
    static int rad = 3;
    static Random r = new Random();
    static double defaultNoiseLevel = 0.1;
    double noiseLevel = defaultNoiseLevel;
    public static double repulsionFactor = 0.1;

    // int ix;
    int w;
    public MazeNode previous;
    int curDir;
    int desired;
    public int edible;
    // GameModel model;
    OldMaze maze;
    PacMan pacman;
    GhostShell[] ghosts;
    public NodeEvaluator eval;
    OldGhostController gc;


    int nDelay;
    public static int[] initDelay = {0, 10, 20, 30};
    int delayResetValue;


    // the ghost behaviour is implemented on the basis
    // of scoring each individual location

    public static void main(String[] args) {
        // Ghost ghost = new Ghost();
    }

//    public Ghost() {
//    }

    public GhostShell(OldMaze maze, PacMan pacman, GhostShell[] ghosts) {
        this.maze = maze;
        this.pacman = pacman;
        this.ghosts = ghosts;
        // bad practice, this static allocation!!
        color = colors[nColor % colors.length];
        delayResetValue = initDelay[nColor % initDelay.length];
        try {
            eval = (NodeEvaluator) behaviour[nColor % behaviour.length].newInstance();
        } catch (Exception e) {
        }
        nColor++;

    }

    public void reset() {
        current = null;
        previous = null;
        edible = 0;
        desired = PacController.CENTRE;
        curDir = PacController.CENTRE;
        this.nDelay = delayResetValue;
    }

    private void reset(int nDelay) {
        reset();
    }

    static Color[] colors = {Color.red,
                             Color.magenta,
                             Color.cyan,
                             Color.orange,
                             Color.pink, Color.green,
                             Color.gray,
                             Color.lightGray,
    };

    static Class[] behaviour = {
        PathScore.class, EuclideanScore.class, ManhattanScore.class,
        // YScore.class,
        RandomScore.class,
    };

    public static int nColor = 0;

    public void move() {
        // try and move in the desired direction
        // ghosts move as follows
        // they carry on until there is
        // a choice
        // when making a choice, they never choose to
        // go back to the previous node

        // edible ghosts move at half speed, so skip this movement
        // 50% of the time if the ghost is edible
        // System.out.println("Edible: " + edible);

        if (nDelay > 0) {
            // this enables variable ghost delay
            nDelay--;
            return;
        }
        if (edible == 0 || (edible > 0 && (edible % 2) == 0)) {

            // int nChoices = current.n - 1;
            // int choice = r.nextInt(nChoices);
            // System.out.println("Choice = " + choice + " / " + nChoices);

            // Node next = current.getOtherNext(choice, previous);
            MazeNode next = gc.getBest(current.next, previous);
            // System.out.println("Next: " + next);
            if (next == null) {
                System.out.println("Stuck at : " + current);
            } else {
                previous = current;
                current = next;
            }
        }
        if (edible > 0) {
            edible--;
        }
    }


    // for now return score as the shortest path distance to
    // the nearest ghost
    private int getscore(MazeNode node) {
        return maze.dist[node.ix][pacman.current.ix];
    }


    public void setEdible() {
        edible = edibleTime;
    }

    public boolean edible() {
        return edible > 0;
    }

    public void reverse() {
        if (previous != null) {
            MazeNode tmp = current;
            current = previous;
            previous = tmp;
        }
    }

    public void setDesire(int dir) {
        desired = dir;
    }

    public void draw(Graphics g, int w, int h, int fac) {
        if (current != null) {
            if (edible()) {
                g.setColor(edibleColor);
            } else {
                g.setColor(color);
            }
            g.fillRect(fac * (current.x - rad), fac * (current.y - rad), fac * 2 * rad, fac * 2 * rad);
        }
    }
}