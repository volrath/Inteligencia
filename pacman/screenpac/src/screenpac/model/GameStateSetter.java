package screenpac.model;

public class GameStateSetter extends GameState {

    public GameStateSetter() {
        super();
    }

    public GameStateSetter(Maze maze) {
        super(maze);
    }

    // a game state is terminal (for this level)
    // if all the pills and power-pills have been eaten
    // (Note: maybe it should be called Level state.)

    // Agent[] ghosts;

    // let's do the following stuff

    // is this the right place for this function?

    public void setAgent(int x, int y) {
        Node n = maze.getNode(x, y);
        if (n == null) {
            System.out.println("No current at: " + x + "\t " +  y);
        } else {
            pacMan.current = n;
            // System.out.println("Set pacMan to " + x + "\t " + y);
        }
    }

    public void setGhost(int x, int y, int ix) {
        Node node = maze.getNode(x, y);
        if (node == null) {
            System.out.println("No current for ghost at: " + x + "\t " +  y);
        } else {
            ghosts[ix % nGhosts].updateState(node);
            // System.out.println("Set pacMan to " + x + "\t " + y);
        }
    }

    public void setGhost(int x, int y, int ix, int edible) {
        Node node = maze.getNode(x, y);
        if (node == null) {
            System.out.println("No current for ghost at: " + x + "\t " +  y);
        } else {
            ghosts[ix % nGhosts].updateState(node);
            ghosts[ix % nGhosts].edibleTime = edible;
            // System.out.println("Set pacMan to " + x + "\t " + y);
        }
    }

    public void setGhost(int cx, int cy, int px, int py, int ix, int edible) {
        Node cur = maze.getNode(cx, cy);
        Node prev = maze.getNode(px, py);
        if (prev == null) prev = cur;
        if (cur == null) {
            System.out.println("No current for ghost at: " + cx + "\t " +  cy);
        } else {
            ghosts[ix % nGhosts].updateState(cur, prev);
            ghosts[ix % nGhosts].edibleTime = edible;
            // System.out.println("Set pacMan to " + x + "\t " + y);
        }
    }

    public void setPill(int x, int y) {
        // need a way to look up the map
        // the chamfer distance should really be part of this
        // map structure
        Node node = maze.getNode(x, y);
        if (node != null && node.pillIndex >= 0) {
            pills.set(node.pillIndex);
        } else {
            // System.out.println("Failed pill: " + current + "\t " + current.pillIndex);
            nFails++;
        }
    }

    public void setPower(int x, int y) {
        // need a way to look up the map
        // the chamfer distance should really be part of this
        // map structure
        Node node = maze.getNode(x, y);
        if (node != null && node.powerIndex >= 0) {
            powers.set(node.powerIndex);
        } else {
            // System.out.println("Failed pill: " + current + "\t " + current.pillIndex);
            nFails++;
        }
    }

    int nFails = 0;
    public void reset() {
        // clear them all (ready re-extract from the screencap the next time)
        pills.clear();
        powers.clear();
        nFails = 0;
    }
}
