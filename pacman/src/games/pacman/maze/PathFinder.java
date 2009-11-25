package games.pacman.maze;

// import vecmat.VecMat;
import games.pacman.maze.MazeNode;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 12-Oct-2004
 * Time: 11:09:58
 * To change this template use Options | File Templates.
 */
public class PathFinder {

    // finds paths through the graph

    public static void main(String[] args) {
        // g[i] lists the neighbours of node i
        int[][] g = {
            {1, 3},
            {0, 2},
            {1, 5},
            {0, 6},
            {7},
            {2, 8},
            {3, 7},
            {6, 4, 8},
            {7, 5},
        };

        int d = distance(g, 0, 7);
        System.out.println("Distance is: " + d);

    }

    public static MazeNode[] copy(MazeNode[] a) {
        MazeNode[] cp = new MazeNode[a.length];
        for (int i=0; i<cp.length; i++) {
            cp[i] = a[i];
        }
        return cp;
    }

    public static int distance(MazeNode[] g, int a, int b) {
        // implement a simple recursive algorithm for this
        int[] d = new int[g.length];
        // assign all distances to MAX
        for (int i=0; i<d.length; i++) {
            d[i] = Integer.MAX_VALUE;
        }
        // start at a, aim to get to b
        // but search the entire maze to be sure
        // not the most efficient way!

        search(g, d, a, 0);
        System.out.println(Arrays.toString(d));
        return d[b];
    }

    public static void setMax(int[] d) {
        for (int i=0; i<d.length; i++) {
            d[i] = Integer.MAX_VALUE;
        }
    }

    public static void search(MazeNode[] g, int[] d, int curNode, int curDist) {
        if (curDist < d[curNode]) {
            d[curNode] = curDist;
            MazeNode[] next = g[curNode].succ();
            for (int i=0; i<next.length; i++) {
                search(g, d, next[i].ix, curDist+1);
            }
        }
    }

    public static int distance(int[][] g, int a, int b) {
        // implement a simple recursive algorithm for finding
        // the shortest distance between two nodes
        int[] d = new int[g.length];
        // assign all distances to MAX
        for (int i=0; i<d.length; i++) {
            d[i] = Integer.MAX_VALUE;
        }
        // start at a, aim to get to b
        // but search the entire maze to be sure
        // not the most efficient way!

        search(g, d, a, 0);
        System.out.println(Arrays.toString(d));
        return d[b];
    }

    public static void search(int[][] g, int[] d, int curNode, int curDist) {
        if (curDist < d[curNode]) {
            d[curNode] = curDist;
            int[] next = g[curNode];
            for (int i=0; i<next.length; i++) {
                search(g, d, next[i], curDist+1);
            }
        }
    }
}
