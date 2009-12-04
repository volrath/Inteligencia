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
 * Updated May 2009 to allow a separate plugged-in ghost controller
 */

public class Ghost extends Mobile {

    static int edibleTime = 100;
    static Color edibleColor = Color.blue;
    static int rad = 3;
    static Random r = new Random();
    static double defaultNoiseLevel = 0.1;
    double noiseLevel = defaultNoiseLevel;
    public static double repulsionFactor = 0.1;
    GhostController controller;

    // int ix;
    int w;
    public MazeNode previous;
    int curDir;
    int desired;
    public int edible;
    // GameModel model;
    OldMaze maze;
    PacMan pacman;
    Ghost[] ghosts;
    public NodeEvaluator eval;


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

    public Ghost(OldMaze maze, PacMan pacman, Ghost[] ghosts) {
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

    public void setController(GhostController controller) {
        this.controller = controller;
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

            MazeNode next = null;
            //System.out.println("Moving ghost: " + eval);
            if (controller != null) {
                int dir = controller.preferredMove();
                // check it's legal
                next = current.next(dir);
                if (next == null || next == previous) {
                    throw new RuntimeException("Disallowed: " + next + " : " + dir + (next == previous));
                }
                if (next == previous) {
                    System.out.println("Attempted a double-back");
                    next = null;
                }
            }
            // if this failed then use the old ghost algorithm
            if (next == null) {
               next = getBest(current.next, previous);
            }
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

    Integer preferredDirection;
    public void setPreferredDirection(int dir) {
        preferredDirection = (dir == PacController.CENTRE) ? null : dir;
    }

    private MazeNode getBest(MazeNode[] poss, MazeNode previous) {
        MazeNode bestNode = null;
        double bestScore = 10000000;
        // the best node is the one with the mimimum score
        for (int i = 0; i < poss.length; i++) {
            if (poss[i] != previous) {
                double score = score(poss[i]);
                // System.out.println(i + "\t " + poss[i] + "\t dist: " + score);
                if (score <= bestScore) {
                    bestScore = score;
                    bestNode = poss[i];
                }
            }
        }
        return bestNode;
    }


    // for now return score as the shortest path distance to
    // the nearest ghost
    private int getscore(MazeNode node) {
        return maze.dist[node.ix][pacman.current.ix];
    }

    public double score(MazeNode node) {
        double score = r.nextDouble() * noiseLevel + eval.score(node, this, pacman, ghosts, maze);
        // System.out.println("Score: " + score);
        if (repulsionFactor != 0) {
            score -= repulsionFactor * closestGhostDist(ghosts, node);
        }
        return score;
    }

    private int closestGhostDist(Ghost[] ghosts, MazeNode poss) {
        // find the closest ghost to the current one
        int min = 10000;
        for (Ghost g : ghosts) {
            if (g != this) {
                min = Math.min(min, maze.dist[poss.ix][g.current.ix]);
            }
        }
        return min;
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
