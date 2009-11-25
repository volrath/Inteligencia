package games.pacman.controllers;

import games.pacman.ghost.Ghost;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;
import games.pacman.features.DirectionalScore;
import games.pacman.features.NodeScore;
import games.pacman.core.GameFrame;
import games.pacman.core.PacMan;

import java.util.Iterator;
import java.util.LinkedList;

public class RouteTest implements PacController, NodeScore {

    // what this does is to add certain nodes to the

    public static void main(String[] args) {
        GameFrame gf = new GameFrame();
        RouteTest rt = new RouteTest(gf.game.maze, gf.game.pacman);
        DirectionalScore ds = new DirectionalScore(gf.game.ghosts);
        // gf.dc.enableActivations(gf.game.maze.na,  rt);
        gf.dc.enableActivations(gf.game.maze.na,  ds);
        gf.game.setController(rt);
        gf.start();
    }

    LinkedList<MazeNode> toVisit;
    Iterator<MazeNode> li;
    MazeNode next;

    OldMaze maze;
    PacMan pac;

    public RouteTest(OldMaze maze, PacMan pac) {
        this.maze = maze;
        this.pac = pac;
        toVisit = new LinkedList<MazeNode>();
        for (MazeNode n : maze.na) {
            if (maze.powerPill(n)) {
                toVisit.add(n);
                System.out.println("Added: " + n);
            }
        }

        li = toVisit.descendingIterator();
        next = li.next();
    }

    public double score(MazeNode node, Ghost me, PacMan pacman, Ghost[] ghosts, OldMaze maze) {
        if (node.equals(next)) {
            System.out.println("Got it!");
            if (!li.hasNext()) {
                li = toVisit.iterator();
            }
            next = li.next();
        }
        // now simply find the closest one to this
        return 0;
    }

    public int getDirection() {
        // get all the successors of the current node
        MazeNode cur = pac.current;
        // System.out.println("Aiming for: " + next);
        if (cur.equals(next)) {
            System.out.println("Got it!");
            if (!li.hasNext()) {
                li = toVisit.iterator();
            }
            next = li.next();
        }
        // now simply find the closest one to this
        try {
            MazeNode[] poss = cur.succ();
            MazeNode best = getBest(poss);
            return maze.direction(cur, best);
        } catch (Exception e) {
            // System.out.println(e);
            return CENTRE;
        }
    }

    private MazeNode getBest(MazeNode[] poss) {
        // a free choice of nodes
        MazeNode bestNode = null;
        double bestScore = Integer.MAX_VALUE;
        // the best node is the one with the maximum score
        // i.e. the one furthest from the nearest ghost
        for (MazeNode pos : poss) {
            double score = score(pos);
            if (score <= bestScore) {
                bestScore = score;
                bestNode = pos;
            }
        }
        return bestNode;
    }

    public double score(MazeNode n) {
        return maze.dist[n.ix][next.ix];
    }
}
