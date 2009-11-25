package games.pacman.maze;

import utilities.ElapsedTimer;

import java.util.ArrayList;
import java.util.Random;
import java.io.PrintStream;

import games.pacman.ghost.Ghost;
import games.pacman.controllers.PacController;
import games.pacman.core.PacMan;
import games.pacman.view.Drawable;
import games.pacman.view.Mobile;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 13-Oct-2004
 * Time: 15:12:27
 * To change this template use Options | File Templates.
 */
public class OldMaze {
    public ArrayList<Drawable> lines;
    public int w;
    public int h;
    int top, left;
    int[] xx;
    int[] yy;
    public MazeNode[] na; // the node array
    MazeNode[][] ng; // the node grid
    static Random r = new Random();
    public int[][] dist;
    static int[] dx = PacController.dx;
    static int[] dy = PacController.dy;
    MazeNode tmp = new MazeNode(0, 0, 0, 0);
    static int nPower = 4;
    static int nPills = 220;
    static int pillSpace = 4;
    int maxDist;
    ArrayList junctions;
    public ArrayList<MazeNode> pills;
    public ArrayList<MazeNode> power;

    public OldMaze(ArrayList lines, int w, int h, int[] xx, int[] yy) {
        this.lines = (ArrayList) lines.clone();
        this.w = w;
        this.h = h;
        this.xx = xx;
        this.yy = yy;
        junctions = new ArrayList();
        pills = new ArrayList<MazeNode>();
        power = new ArrayList<MazeNode>();
        makeGraph();
        // lines.
    }

    public void reset() {
        // put all the pills back!!!
        for (int i = 0; i < pills.size(); i++) {
            MazeNode node = pills.get(i);
            node.pill = MazeNode.PILL;
        }
        for (int i = 0; i < power.size(); i++) {
            MazeNode node = power.get(i);
            node.pill = MazeNode.POWER_PILL;
        }
    }

    public void print(PrintStream ps) {
        for (int i = 0; i < na.length; i++) {
            System.out.println(i + "\t " + na[i]);
        }
    }

    public MazeNode getClosestNode(int ox, int oy) {
        // try the current node, then look around
        // return null if too far away!!!
        int x = ox / 2 + left;
        int y = oy / 2 + top;
        // System.out.println(x + " : " + y);
//        tmp.x = x;
//        tmp.y = y;
//        return tmp;
        MazeNode node = null;
        try {
            node = ng[x][y];
            if (node != null) {
                return node;
            }
        } catch (Exception e) {
        }
        int maxDist = 3;
        for (int dist = 1; dist < maxDist; dist++) {
            for (int i = 0; i < 4; i++) {
                try {
                    node = ng[x + dist * dx[i]][y + dist * dy[i]];
                    if (node != null) {
                        return node;
                    }
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    public void place(Mobile agent) {
        // pick a node at random
        MazeNode node = na[r.nextInt(na.length)];
        agent.setPosition(node);
    }

    public void place(PacMan pacman) {
        pacman.reset();
        pacman.setPosition(ng[57][97]);
    }

    public void place(Ghost ghost) {
        MazeNode position = ng[57][49];
        // System.out.println("Placing ghost: " + position);
        ghost.reset();
        ghost.setPosition(position);
    }

    public int direction(MazeNode a, MazeNode b) {
        // now find the matching dx and dy
        // return -1 if unable to
        // don;t forget to allow for wrap-around on the x-direction
        for (int i = 0; i < PacController.dx.length; i++) {
            if (((a.x + dx[i] + w) % w == b.x) && (a.y + dy[i] == b.y)) {
                return i;
            }
        }
        return -1;
    }

    private void makeGraph() {
        // create a temporary x/y array, then process all the lines...
        // ng is node grid
        // also work out the top and left params
        top = Integer.MAX_VALUE;
        left = Integer.MAX_VALUE;
        ng = new MazeNode[w][h];
        // for each line, add a new Node to the graph
        int nNodes = 0;
        ArrayList<MazeNode> nodes = new ArrayList<MazeNode>();
        System.out.println("Width: " + w);
        setLeftTop();
        // System.out.println("Lines " + lines);
        for (int i = 0; i < lines.size(); i++) {
            // System.out.println(i + "\t " + lines.get(i));
            Line line = (Line) lines.get(i);
            int x = line.x1;
            int y = line.y1;
            int dx = sgn(line.x2 - line.x1);
            int dy = sgn(line.y2 - line.y1);
            do {
                if (ng[(x + w) % w][y] == null) {
                    // if there is no node there yet then make one
                    MazeNode node = new MazeNode(x + w * y, x, y, nNodes++);
                    ng[(x + w) % w][y] = node;
                    nodes.add(node);
                    if (line.pill && pill(node)) {
                        node.pill = MazeNode.PILL;
                        if (powerPill(node)) {
                            node.pill = MazeNode.POWER_PILL;
                        }
                    }
                }
                x += dx;
                y += dy;
                // System.out.println(x + " : " + y);
            } while (line.in(x, y));
        }
        // now process the nodes into a nice convenient array
        na = new MazeNode[nNodes];
        System.out.println("Found " + nNodes + " nodes from " + lines.size() + " lines");
        for (int i = 0; i < nodes.size(); i++) {
            MazeNode node = (MazeNode) nodes.get(i);
            na[i] = node;
        }

        System.out.println("Finding neighbours");
        // now find the neighbours of each node
        for (MazeNode aNa : na) {
            aNa.findNext(ng, w);
        }
        System.out.println("Finished Maze Construction");
        // now compute the distance...
        // and we're done
        ElapsedTimer t = new ElapsedTimer();
        dist = new int[nNodes][nNodes];
        for (int i = 0; i < nNodes; i++) {
            PathFinder.setMax(dist[i]);
            PathFinder.search(na, dist[i], i, 0);
        }
        System.out.println("Created distance array; " + t);
        System.out.println("LeftTop: " + left + " : " + top);
        // VecMat.print(dist[0]);
        // makePills();

        for (int i = 0; i < nodes.size(); i++) {
            MazeNode node = na[i];
            // now insert into junctions and pills
            if (node.pill == MazeNode.POWER_PILL) {
                lines.add(node);
                power.add(node);
            }
            if (node.pill == MazeNode.PILL) {
                lines.add(node);
                pills.add(node);
            }
            if (node.next.length > 2) {
                junctions.add(node);
            }
        }
        System.out.println(power.size() + " : " + junctions.size() + " : " + pills.size());
        setJunctionDists();

    }

    private void setLeftTop() {
        for (int i = 0; i < lines.size(); i++) {
            Line line = (Line) lines.get(i);
            int x = line.x1;
            left = Math.min(left, x);
            int y = line.y1;
            top = Math.min(top, y);
        }
    }

    private void setJunctionDists() {
        for (int i = 0; i < na.length; i++) {
            // find closest junction
            double d = h;
            for (int j = 0; j < junctions.size(); j++) {
                MazeNode junction = (MazeNode) junctions.get(j);
                d = Math.min(d, dist[na[i].ix][junction.ix]);
            }
            na[i].junctionDist = d;
            // System.out.println(i + " : " + na[i].junctionDist);
        }
    }

    public boolean levelCleared() {
        return allEaten(pills) && allEaten(power);
    }

    public boolean allEaten(ArrayList pills) {
        for (int i = 0; i < pills.size(); i++) {
            MazeNode node = (MazeNode) pills.get(i);
            if (node.pill != MazeNode.NO_PILL) {
                return false;
            }
        }
        return true;
    }

    private void makePills() {
        for (int i = 0; i < na.length; i++) {
            if (pill(na[i])) {
                na[i].pill = MazeNode.PILL;
                // lines.add(na[i]);
                // uprate it to a power pill if necessary
                if (powerPill(na[i])) {
                    na[i].pill = MazeNode.POWER_PILL;
                }

            }
        }
    }

    private boolean pill(MazeNode node) {
        return ((node.x - 7) % pillSpace) == 0 &&
                ((node.y - top) % pillSpace) == 0;

    }

    public boolean powerPill(MazeNode node) {
        if (node.x == 7) {
            return node.y == 13 || node.y == 113;
        }
        if (node.x == 107) {
            return node.y == 13 || node.y == 113;
        }
        return false;
    }

//    private void randPills() {
//        // now randomly convert some nodes into pills and power pills
//        CardDeck cd = new CardDeck(na.length);
//        cd.shuffle();
//        for (int i = 0; i < nPower; i++) {
//            int ix = cd.getCard();
//            na[ix].pill = Node.POWER_PILL;
//            // lines.add(na[ix]);
//        }
//        for (int i = 0; i < nPills; i++) {
//            int ix = cd.getCard();
//            na[ix].pill = Node.PILL;
//            lines.add(na[ix]);
//        }
//    }
//
    public void rescale(double fac) {
        for (int i = 0; i < lines.size(); i++) {
            Drawable dr = (Drawable) lines.get(i);
            dr.rescale(fac);
        }
        w = (int) (w * fac);
        h = (int) (h * fac);
    }

    private static int sgn(int x) {
        if (x < 0) return -1;
        if (x > 0) return 1;
        return 0;
    }
}
