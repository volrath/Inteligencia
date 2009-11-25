package screenpac.features;

import screenpac.model.*;
import screenpac.extract.Constants;

import java.util.ArrayList;

public class Utilities implements Constants {
    public static Node getMin(ArrayList<Node> nodes, NodeScore f, GameStateInterface gs) {
        double best = Double.MAX_VALUE;
        // selected current
        Node sel = null;
        for (Node n : nodes) {
            if (f.score(gs, n) < best) {
                best = f.score(gs, n);
                sel = n;
            }
        }
        return sel;
    }

    public static int getMinDir(ArrayList<Node> nodes, Node cur, NodeScore f, GameStateInterface gs) {
        Node target = getMin(nodes, f, gs);
        // System.out.println("Targetting: " + target);
        return getWrappedDirection(cur, target, gs.getMaze());
    }

    public static int getClosestDir(ArrayList<Node> nodes, Node cur, NodeScore f, GameState gs) {
        Node target = getMin(nodes, f, gs);
        // System.out.println("Targetting: " + target);
        return getWrappedDirection(cur, target, gs.getMaze());
    }

    public static Node getClosest(ArrayList<Node> nodes, Node target, MazeInterface maze) {
        // if the target current is null then print a warning
        if (target == null) {
            System.out.println("Warning: null target in Utilities.getClosest()");
            return nodes.get(rand.nextInt(nodes.size()));
        }
        double best = Double.MAX_VALUE;
        // selected current
        Node sel = null;
        for (Node n : nodes) {
            int d = maze.dist(n, target);
            if (d < best) {
                best = d;
                sel = n;
            }
        }
        return sel;
    }

    public static int getDirection(Node from, Node to) {
        // may not need the sign function here
        int xDiff = sgn(to.x - from.x);
        int yDiff = sgn(to.y - from.y);
        for (int i = 0; i < dx.length; i++) {
            if (dx[i] == xDiff && dy[i] == yDiff) {
                // System.out.println("Direction: " + from + "\t " + to);
//                System.out.println("diffs: " + xDiff + "\t " + yDiff);
//                System.out.println("returning: " + i );
//                System.out.println("");
                return i;
            }
        }
        System.out.println("Error in Util.getDirection");
        throw new RuntimeException("Error in getDirection " + from + " : " + to);
        // return 0;
    }


    public static int getWrappedDirection(Node a, Node b, MazeInterface maze) {
        int w = maze.getWidth();
        int h = maze.getHeight();
        for (int i = 0; i < dx.length; i++) {
            if (
                    ((a.x + dx[i] + w) % w == b.x) &&
                    ((a.y + dy[i] +h) % h == b.y)
                    )
            {
                return i;
            }
        }
        // something's wrong
        System.out.println("Non-adjacent nodes in getWrappedDirection");
        return NEUTRAL;
    }

    public static int sgn(int x) {
        if (x < 0) return -1;
        if (x > 0) return 1;
        return 0;
    }

    public static int manhattan(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
