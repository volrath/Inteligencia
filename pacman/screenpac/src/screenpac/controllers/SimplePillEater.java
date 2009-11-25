package screenpac.controllers;

import screenpac.model.Node;
import screenpac.model.GameStateInterface;
import screenpac.features.NearestPillDistance;
import screenpac.features.Utilities;
import screenpac.extract.Constants;

public class SimplePillEater implements AgentInterface, Constants {
    /*
    This simple pill eater just heads for the nearest pill each time
     */
    NearestPillDistance npd;

    public SimplePillEater() {
        npd = new NearestPillDistance();
    }

    public int action(GameStateInterface gs) {
        // choose the adjacent current with the
        // nearest pill
        // check that copying works!
        gs = gs.copy();
        Node current = gs.getPacman().current;
        npd.score(gs, current);
        Node next = Utilities.getClosest(current.adj, npd.closest, gs.getMaze());
        int dir = Utilities.getWrappedDirection(current, next, gs.getMaze());
        return dir;
    }
}