package screenpac.model;

import screenpac.extract.Constants;
import screenpac.features.Utilities;

import java.util.ArrayList;

public class GhostState implements Constants {

    public int edibleTime;
    public Node current, previous;
    int curDir;
    int delay;
    int delayCounter;
    // this can be set to a node that the ghost eyes must
    // return to in order to regenerate the ghost
    Node returnNode;

    public GhostState() {}

    public GhostState(int delay) {
        this.delay = delay;
        delayCounter = delay;
        edibleTime = -1;
        // possibles = new ArrayList<Node>();
    }

    public GhostState(Node current) {
        this(-1, current);
    }

    public GhostState(int edibleTime, Node current) {
        this.edibleTime = edibleTime;
        this.current = current;
        // possibles = new ArrayList<Node>();
    }

    public GhostState(GhostState gs) {
        this.edibleTime = gs.edibleTime;
        this.delay = gs.delay;
        this.delayCounter = gs.delayCounter;
        this.current = gs.current;
        this.previous = gs.previous;
        // possibles = new ArrayList<Node>();
    }

    public void updateState(Node next) {
        previous = current;
        current = next;
        if (edibleTime >= 0) {
            edibleTime--;
        }
    }

    public void updateState(Node node, Node previous) {
        this.current = node;
        this.previous = previous;
        if (edibleTime >= 0) {
            edibleTime--;
        }
    }

    public void reverse() {
        if (previous != null) {
            Node tmp = current;
            current = previous;
            previous = tmp;
        }
    }

    public boolean returning() {
        return returnNode != null;
    }

    public void makeReturnMove(Maze maze) {
        // check that we're still in a returning state
        if (returning()) {
            // then move toward the return current
            Node next = Utilities.getClosest(current.adj, returnNode, maze);
            updateState(next);
            if (next.equals(returnNode)) returnNode = null;
        }
    }

    public ArrayList<Node> getPossibles() {
        // possibles.clear();
        ArrayList<Node> possibles = new ArrayList<Node>();
        for (Node n : current.adj) {
            if (!n.equals(previous)) {
                possibles.add(n);
            }
        }
        return possibles;
    }

    public void next(int dir, GameState gs) {
        Maze maze = gs.maze;
        // if returning then make two moves until the return current
        // has been reached
        if (returning()) {
            makeReturnMove(maze);
            makeReturnMove(maze);
            return;
        }

        // if delayed at the start of the game (effectively in the cage)
        // then decrement the delay counter and do nothing else
        if (delayCounter > 0) {
            delayCounter--;
            return;
        }

        // edible ghosts only move every other time step
        if (gs.gameTick % 2 == 0 && edible()) return;

        // if it's a valid direction that does NOT lead back to the
        // previous current, then return the selected current
        // otherwise, try and continue with the current direction
        // or choose a random legal one
        // otherwise we're stuck
        Node next = maze.getNode(current.x + dx[dir], current.y + dy[dir]);
        // if the selection is not ok then keep going or choose a random dir
        if (next == null || next.equals(previous) || next.equals(current)) {
            ArrayList<Node> possibles = getPossibles();
            if (possibles.size() == 1) {
                // if there's only one option then save the call to rand.
                next = possibles.get(0);
            }
            else {
                next = possibles.get(rand.nextInt(possibles.size()));
            }
        }
        updateState(next);
    }

    public GhostState copy() {
        return new GhostState(this);
    }

    public void nextState() {
        if (edibleTime >= 0) {
            edibleTime--;
        }
    }

    public boolean edible() {
        return edibleTime >= 0 && !returning();
    }

    public void setPredatory() {
        edibleTime = -1;
    }
}
