package screenpac.adapter;

import games.pacman.ghost.GhostController;

public class GhostProxy implements GhostController {
    int dir;
    public int preferredMove() {
        System.out.println("getPreferredMove: " + dir);
        return dir;
    }

    public void setPreferredMove(int dir) {
        System.out.println("Set preferred move: " + dir);
        this.dir = dir;
    }
}
