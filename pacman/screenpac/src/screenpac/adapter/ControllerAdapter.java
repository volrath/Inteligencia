package screenpac.adapter;

import games.pacman.controllers.PacController;
import screenpac.model.GameStateSetter;
import screenpac.controllers.AgentInterface;

public class ControllerAdapter implements PacController {

    GameStateSetter gs;
    AgentInterface cont;

    public ControllerAdapter(GameStateSetter gs, AgentInterface cont) {
        this.gs = gs;
        this.cont = cont;
    }

    public int getDirection() {
        int dir = cont.action(gs);
        // System.out.println("Selected: " + dir);
        return dir;
    }
}
