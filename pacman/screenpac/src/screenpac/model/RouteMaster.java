
package screenpac.model;

import java.util.Arrays;
import java.util.ArrayList;

public class RouteMaster {
    public static void main(String[] args) {
        // g[i] lists the neighbours of current i
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

    public static int distance(int[][] g, int a, int b) {
        // implement a simple recursive algorithm for finding
        // the shortest distance between two nodes
        int[] d = new int[g.length];
        // assign all distances to MAX
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.MAX_VALUE;
        }
        // start at a, aim to get to b
        // but search the entire maze to be sure
        // not the most efficient way!
        search(g, d, a, 0);
        System.out.println(Arrays.toString(d));
        return d[b];
    }

    public static void search(ArrayList<Node> g, int[] d, int curNode, int curDist) {
        if (curDist < d[curNode]) {
            d[curNode] = curDist;
            for (Node n : g.get(curNode).adj) {
                search(g, d, n.nodeIndex, curDist+1);
            }
        }
    }

    public static void search(int[][] g, int[] d, int curNode, int curDist) {
        if (curDist < d[curNode]) {
            d[curNode] = curDist;
            int[] next = g[curNode];
            for (int i = 0; i < next.length; i++) {
                search(g, d, next[i], curDist + 1);
            }
        }
    }

    static int max = 10000;
    public static void setMax(int[] a) {
        for (int i=0; i<a.length; i++) {
            a[i] = max;
        }
    }
}
