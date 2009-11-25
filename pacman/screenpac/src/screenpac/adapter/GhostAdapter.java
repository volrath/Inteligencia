package screenpac.adapter;

import games.pacman.ghost.GhostController;
import screenpac.model.GameStateSetter;
import screenpac.controllers.AgentInterface;

public class GhostAdapter implements GhostController {

    GameStateSetter gs;
    AgentInterface cont;

    // a GhostAdapter does the following
    public GhostAdapter(GameStateSetter gs, AgentInterface cont) {
        this.gs = gs;
        this.cont = cont;
    }

    public int preferredMove() {
        int dir = cont.action(gs);
        // System.out.println("Selected: " + dir);
        return dir;
    }
}