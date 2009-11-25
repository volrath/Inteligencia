package screenpac.controllers;

import screenpac.model.Node;
import screenpac.model.Maze;
import screenpac.model.MazeInterface;

import java.util.ArrayList;

import games.pacman.maze.MazeOne;

public class PathPlanner {
    // plans a route from one current to another
    // by
    // the route is represented as a list of nodes
    public static void main(String[] args) {
        // include a simple test method
        // create a map with a maze
        // choose a route between some random elements
        Maze maze = new Maze();
        maze.processOldMaze(MazeOne.getMaze());
        PathPlanner pp = new PathPlanner(maze);
        Node from = maze.getMap().get(0);
        Node to = maze.getMap().get(100);
        System.out.println("Going from " + from + " to " + to);
        ArrayList<Node> path = pp.getPath(from, to);
        System.out.println("Route map: ");
        for (Node n : path) {
            System.out.println(n);
        }
        System.out.println("End of route");
    }

    MazeInterface maze;

    public PathPlanner(MazeInterface maze) {
        this.maze = maze;
    }

    public ArrayList<Node> getPath(Node from, Node to) {
        return getPath(from, to, new ArrayList<Node>());
    }

    private ArrayList<Node> getPath(Node from, Node to, ArrayList<Node> path) {
        // this code is buggy: can cause a stack overflow error -
        // due to cycle
        if (from.equals(to)) return path;
        else {
            // find out which of this current's neighbours are closest to 'to'
            Node next = getClosest(from.adj, to, maze);
            path.add(next);
            return getPath(next, to, path);
        }
    }

    private Node getClosest(ArrayList<Node> nodes, Node to, MazeInterface maze) {
        double best = Double.MAX_VALUE;
        // selected current
        Node sel = null;
        for (Node n : nodes) {
            if (maze.dist(n, to) < best) {
                best = maze.dist(n, to);
                sel = n;
            }
        }
        return sel;
    }
}

