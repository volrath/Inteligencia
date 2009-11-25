package screenpac.model;

import screenpac.extract.Constants;

import java.util.ArrayList;

import games.pacman.maze.*;
import utilities.ElapsedTimer;

public class Maze implements MazeInterface, Constants {

    // need to map from an x,y position
    // to an Node object in a maze
    // to a pill index
    // or a power-pill index
    // can therefore add these to the Node object

    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.processOldMaze(MazeOne.getMaze());
        MapView.test(maze);
    }


    public ArrayList<Node> getPowers() {
        return powers;
    }

    public  ArrayList<Node> getPills() {
        return pills;
    }

    public ArrayList<Node> getMap() {
        return map;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public Node[][] getNode2DArray() {
        return na;
    }

    private ArrayList<Node> powers;
    private ArrayList<Node> pills;

    private ArrayList<Node> map;
    private int w, h;
    private Node[][] na;
    private int[][] dist;

    public Maze() {
        map = new ArrayList<Node>();
        pills = new ArrayList<Node>();
        powers = new ArrayList<Node>();
    }

    public int dist(Node a, Node b) {
        return dist[a.nodeIndex][b.nodeIndex];
    }

    public Node ghostStart() {
        return getNode(57,49);
    }

    public Node pacStart() {
        return getNode(57, 97);
    }

    public void processOldMaze(OldMaze maze) {
        // this part needs to read in the data from
        // some other system - but we already have this, of course!
        // now just need this in the correct format
        w = maze.w;
        h = maze.h;
        int nNodes = 0;
        int mag = 1;
        int powerIndex = 0;
        int pillIndex = 0;
        na = new Node[maze.w * mag][maze.h * mag];
        maze.reset();
        for (MazeNode n : maze.na) {
            // System.out.println(nNodes + "\t " + n.pill + "\t " + n.getPill());
            int x = n.x * mag;
            int y = n.y * mag;
            Node node = new Node(x, y);
            node.nodeIndex = nNodes;
            na[x][y] = node;
            map.add(node);
            nNodes++;
            if (n.pill == MazeNode.PILL) {
                pills.add(node);
                node.pillIndex = pillIndex++;
            } else if (n.pill == MazeNode.POWER_PILL) {
                powers.add(node);
                node.powerIndex = powerIndex++;
            }
            // then need to add to other pills etc
        }
        System.out.println("pillIndex: " + pillIndex);
        System.out.println("powerIndex: " + powerIndex);
        addAdjacencies();
        System.out.println("nNodes: " + nNodes + "\t " + map.size());
//        for (Node n : map) {
//            System.out.println(n);
//        }
        // now set up the distances
        ElapsedTimer t = new ElapsedTimer();
        System.out.println("Setting distances");
        dist = maze.dist;
        // just for interest this histogram shows the distribution
        // of number of neighbours
        int[] h = new int[5];
        for (Node n : map) {
            h[n.adj.size()]++;
        }
        for (int i = 0; i < h.length; i++) {
            System.out.println(i + "\t " + h[i]);
        }
    }

    private void addAdjacencies() {
        // for each current in the map, simply check out its neighbours
        // using the na array
        for (Node n : map) {
            // then for each proper direction
            for (int i = 0; i < dx.length; i++) {
                if (dx[i] != 0 || dy[i] != 0) {
                    Node adj = getNode(n.x + dx[i], n.y + dy[i]);
                    if (adj != null) n.adj.add(adj);
                }
            }
        }
    }

    public Node getNode(int x, int y) {
        return na[(x + w) % w][(y + h) % h];
    }

    public Node getNode(int index) {
        return map.get(index);
    }

}
