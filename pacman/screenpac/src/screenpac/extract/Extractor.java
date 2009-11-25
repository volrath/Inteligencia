package screenpac.extract;


import java.util.*;
import java.awt.*;

import utilities.StatSummary;

public class Extractor implements Constants {

    // this will hunt through a pixel array
    // adding all the connected components to
    // itself

    // the background - components of this will not be created

    // this needs to be modified in order to keep track of existing agents,
    // rather than making a new agent every time that consume() is called


    public void consume(int[] pix) {
        gs.reset();
        ArrayList<ENode> nodes = new ArrayList<ENode>();
        addNodes(pix, nodes);
        gs.setNodes(nodes);
        StatSummary ss = new StatSummary();
        int nPills = 0;
        int nPower = 0;
        int nGhosts = 0;
        int nAgent = 0;
        for (int p = 0; p < pix.length; p++) {
            if ((pix[p] & 0xFFFFFF) != 0) { // && colors.contains(pix[p])) {
                ConnectedSet cs = consume(pix, p, pix[p]);
                // objects.add(cs);
                gs.update(cs);

                if (cs.pill()) {
                    ss.add(nodeArray[cs.x()][cs.y()] == null ? 0 : 1);
                    // if (nodeArray[cs.x()][cs.y()] != null) nPills++;
                    nPills++;
                }

                if (cs.powerPill()) {
                    nPower++;
                }
                if (cs.ghostLike() && nodeArray[cs.x()][cs.y()] != null) {
                    nGhosts++;
                }
                if (cs.isPacMan()) { //  && nodeArray[cs.x()][cs.y()] != null) {
                    nAgent++;
                }
                // System.out.println(cs.height);
                // gs.update(cs, pix);
            }
        }
        // System.out.println("Hit ratio: " + ss.mean());
//        System.out.println("nPills: " + nPills);
//        System.out.println("nPower: " + nPower);
//        System.out.println("nGhosts: " + nGhosts);
//        System.out.println("nAgent: " + nAgent);
//        objects.add(gs);
//        return objects;
    }



    int w, h;
    public int left, top;
    IntStack stack;
    HashSet uniques;
    // Agent agent;
    // SimpleGameState gs;

    // is is integral array
    StatSummary sx, sy;

    int[][] ia;
    public GameObjects gs;
    int[] xHist;
    int[] yHist;
    ENode[][] nodeArray;

    public Extractor(int w, int h) {
        this.w = w;
        this.h = h;
        int size = 4 * w * h;
        stack = new IntStack(size);
        // uniques = new HashSet();
        // agent = new Agent();
        gs = new GameObjects();
        ia = new int[w][h];
        xHist = new int[w];
        yHist = new int[h];
        sx = new StatSummary();
        sy = new StatSummary();
    }

    // static int boxSize = 10;

    private void addNodes(int[] pix, ArrayList<ENode> objects) {

        // count(pix);
        // check the total
        int tt = 0;
        for (int i = 0; i < pix.length; i++) {
            tt += val(pix[i]);
        }
        // System.out.println("Total bg pixels: " + tt);

        // set up integral array
        integral(pix, ia);
        ArrayList<ENode> nodes = new ArrayList<ENode>();
        int r = 16;
        int maxTot = 0;
        nodeArray = new ENode[w/DIV][h/DIV];

        for (int left = 0; left < w - r; left++) {
            for (int top = 0; top < h - r; top++) {
                int right = left + r;
                int bot = top + r;
                int a1 = ia[left][top];
                int a2 = ia[right][top];
                int a3 = ia[left][bot];
                int a4 = ia[right][bot];
                int tot = a1 + a4 - (a2 + a3);
                if (tot >= r * r) {
                    int x = (left + (r/2)) / DIV;
                    int y = (top + (r/2)) / DIV;
                    if (nodeArray[x][y] == null) {
                        ENode node = new ENode(x, y, Color.blue);
                        nodeArray[x][y] = node;
                        nodes.add(node);
                    }
                }
                maxTot = Math.max(tot, maxTot);
            }
        }
        // System.out.println("maxTot: " + maxTot);
        // now to histogram analysis to remove noise
        // only keep nodes that share many x values or y values
        // with other nodes
        setVec(xHist, 0);
        setVec(yHist, 0);
        for (ENode n : nodes) {
            xHist[n.x]++;
            yHist[n.y]++;
        }
        // now only keep the ones that exceed the threshold
        // this is a bit of a hack that might not work on all levels
        int thresh = 30 / DIV;
        nodeArray = new ENode[w/DIV][h/DIV];
        int nObj = 0;
        sx.reset();
        sy.reset();
        for (ENode n : nodes) {
            if (nodeArray[n.x][n.y] == null && (xHist[n.x] > thresh || yHist[n.y] > thresh)) {
                objects.add(n);
                nodeArray[n.x][n.y] = n;
                nObj++;
                sx.add(n.x);
                sy.add(n.y);
            }
        }
//        System.out.println("nObj: " + nObj);
        // now check them for neighbours
        left = MapMatcher.sigLeft(nodeArray);
        top = MapMatcher.sigTop(nodeArray);
        for (ENode n : nodes) {
            addNeighbour(n, 0, -1);
            addNeighbour(n, 0, 1);
            addNeighbour(n, -1, 0);
            addNeighbour(n, 1, 0);
        }

        int nJunctions = 0;
        for (ENode n : nodes) {
            if (n.isJunction()) nJunctions++;
        }
        // System.out.println("nJunctions: " + nJunctions);
//        StatSummary ss = new StatSummary();
//        for (ENode n : nodes) {
//            ss.add(n.adj.size());
//        }
//        System.out.println("Mean neighbours: " + ss.mean());
//        System.out.println("Min neighbours: " + ss.min());
//        System.out.println("Max neighbours: " + ss.max());
    }

    private void addNeighbour(ENode n, int i, int j) {
        try {
            if (nodeArray[n.x + i][n.y + j] != null) {
                n.adj.add(nodeArray[n.x + i][n.y + j]);
            }

        } catch (Exception e) {

        }
    }

    private void setVec(int[] v, int x) {
        for (int i = 0; i < v.length; i++) {
            v[i] = x;
        }
    }

    public int[][] integral(int[] pix, int[][] a) {
        a[0][0] = val(pix[0]);
        for (int i = 1; i < a.length; i++) {
            a[i][0] = a[i - 1][0] + val(pix[i]);
        }
        for (int i = 1; i < a[0].length; i++) {
            a[0][i] = a[0][i - 1] + val(pix[i * w]);
        }
        for (int i = 1; i < a.length; i++) {
            for (int j = 1; j < a[i].length; j++) {
                a[i][j] = val(pix[i + j * w])
                        - a[i - 1][j - 1] + (a[i][j - 1] + a[i - 1][j]);
            }
        }
        return a;
    }

    private static int val(int p) {
        return (p == BG) || (p == pill) || (p == pacMan)
                ? 1 : 0;
    }

    private void count(int[] pix) {
        TreeMap<Integer, Integer> count = new TreeMap<Integer, Integer>();
        for (int i = 0; i < pix.length; i++) {
            Integer c = count.get(pix[i]);
            if (c == null) c = 0;
            count.put(pix[i], c + 1);
        }
        for (Integer key : count.keySet()) {
            if (count.get(key) > 100)
                System.out.println(key + "\t " + count.get(key));
        }
    }

    public ConnectedSet consume(int[] pix, int p, int fg) {
        ConnectedSet cs = new ConnectedSet(p % w, p / w, fg);
        // push the current pixel on the stack
        stack.reset();
        // int p = x + y * w;
        stack.push(p);
        // int count = 0;
        // System.out.println( stack );
        while (!stack.isEmpty()) {
            // count++;
            p = stack.pop();
            if (pix[p] == fg) {
                // System.out.println(cx + " : " + cy + " : " + pix[p] );
                // System.in.read();
                cs.add(p % w, p / w, p, pix[p]);
                pix[p] = 0;
                int cx = p % w;
                int cy = p / w;
                if (cx > 0) {
                    stack.push(p - 1);
                }
                if (cy > 0) {
                    stack.push(p - w);
                }
                if (cx < (w - 1)) {
                    stack.push(p + 1);
                }
                if (cy < (h - 1)) {
                    stack.push(p + w);
                }
            }
        }
        return cs;
    }
}
