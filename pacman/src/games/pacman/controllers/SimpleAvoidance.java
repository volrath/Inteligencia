package games.pacman.controllers;

import games.pacman.core.FullGame;
import games.pacman.core.GameModel;
import games.pacman.core.PacMan;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;
import games.pacman.ghost.Ghost;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 19-Oct-2004
 * Time: 11:59:55
 * To change this template use Options | File Templates.
 */
public class SimpleAvoidance implements PacController {

    // easy next - simply evaluate the current position
    // compared to the other positions, and then take it
    // from there...

    // public SimpleAvoidance

    OldMaze maze;
    Ghost[] ghosts;
    PacMan pacman;

    public SimpleAvoidance(GameModel model) {
        this(model.maze, model.ghosts, model.pacman);
    }

    public SimpleAvoidance(FullGame model) {
        this(model.maze, model.ghosts, model.pacman);
    }

    public SimpleAvoidance(OldMaze maze, Ghost[] ghosts, PacMan pacman) {
        this.maze = maze;
        this.ghosts = ghosts;
        this.pacman = pacman;
    }

    public int getDirection() {
        // System.out.println("Called getDirection");
        // get all the successors of the current node
        try {
            MazeNode[] poss = pacman.current.succ();
            // Node[] poss = pacman.current.getOtherNext()
            MazeNode best = getBest(poss, pacman.previous);
            // Node best = getBest(poss, null);
            // System.out.println("Current: " + pacman.current);
            // System.out.println("Best: " + best);
            // System.out.println(maze.direction(pacman.current,best));
            return maze.direction(pacman.current, best);
        } catch (Exception e) {
            // System.out.println(e);
            return CENTRE;
        }
    }

    private MazeNode getBest(MazeNode[] poss) {
        // a free choice of nodes
        MazeNode bestNode = null;
        int bestScore = 0;
        // the best node is the one with the maximum score
        // i.e. the one furthest from the nearest ghost
        for (int i = 0; i < poss.length; i++) {
            int score = score(poss[i]);
            // System.out.println(i + "\t " + poss[i] + "\t dist: " + score);
            if (score >= bestScore) {
                bestScore = score;
                bestNode = poss[i];
            }
        }
        return bestNode;
    }

    private MazeNode getBest(MazeNode[] poss, MazeNode previous) {
        MazeNode bestNode = null;
        int bestScore = 0;
        // the best node is the one with the maximum score
        // i.e. the one furthest from the nearest ghost
        // set previous to null to allow it to turn back on itself
        previous = null;
        for (int i = 0; i < poss.length; i++) {
            if (poss[i] != previous) {
                int score = score(poss[i]);
                // System.out.println(i + "\t " + poss[i] + "\t dist: " + score);
                if (score >= bestScore) {
                    bestScore = score;
                    bestNode = poss[i];
                }
            }
        }
        return bestNode;
    }

    // for now return score as the shortest path distance to
    // the nearest ghost

    // an improved version would be as follows: it would
    // consider the current direction of each ghost
    // 

    public int score(MazeNode node) {
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < ghosts.length; i++) {
            // need the distance between this node and the ghost
            MazeNode gn = ghosts[i].current;
            if (gn != null) {
                int d = maze.dist[node.ix][ghosts[i].current.ix];
                // System.out.println(i + "\t " + d);
                if (d < minDist) {
                    minDist = d;
                }
            }
        }
        // System.out.println(node + " \t " + minDist);
        return minDist;
    }
}
