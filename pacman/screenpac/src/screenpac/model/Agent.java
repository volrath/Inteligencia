package screenpac.model;

import screenpac.extract.Constants;

public class Agent implements Constants {
    // the only state of a PacMan agent is it's current current in the maze
    public Node current;
    int curDir;

    public Agent(Node current) {
        this.current = current;
        // pacman traditionally starts moving left when placed
        // in the maze
        curDir = LEFT;
    }

    public Agent copy() {
        // note: the map nodes never change and don't need to be copied
        Agent p = new Agent(current);
        p.curDir = curDir;
        return p;
    }

    public Node next(int dir, Maze maze) {
        // if it's a valid direction then return the current there
        // otherwise, try and continue with the current direction
        // otherwise we're stuck
        if (dir == NEUTRAL) dir = curDir;
        Node next = maze.getNode(current.x + dx[dir], current.y + dy[dir]);
        if (next != null) {
            current = next;
            curDir = dir;
        } else {
            next = maze.getNode(current.x + dx[curDir], current.y + dy[curDir]);
            if (next != null) current = next;
        }
        return current;
    }
}
