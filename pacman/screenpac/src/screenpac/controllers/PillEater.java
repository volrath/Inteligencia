package screenpac.controllers;

import screenpac.model.Node;
import screenpac.model.GameStateInterface;
import screenpac.features.NearestPillDistance;
import screenpac.features.Utilities;

public class PillEater implements AgentInterface {
    // this is a dumb version that does not take
    // into account positional innaccuracy in the
    // pacman locator, or time lags in the screen
    // parsing.  as a consequence, it frequently gets stuck
    NearestPillDistance npd = new NearestPillDistance();
    public int action(GameStateInterface gs) {
        // choose the adjacent current with the
        // nearest pill
        Node target = Utilities.getMin(gs.getPacman().current.adj, npd, gs);
        if (target == null) {
            // something's gone wrong
            System.out.println("Error: null target in PillEater.action");
            return 0;
        } else {
            // find the direction to this current
            return Utilities.getDirection(gs.getPacman().current, target);
        }
    }
}
