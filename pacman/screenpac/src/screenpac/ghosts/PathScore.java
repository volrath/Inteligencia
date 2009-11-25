package screenpac.ghosts;

import screenpac.features.NodeScore;
import screenpac.model.Node;
import screenpac.model.GameStateInterface;

public class PathScore implements NodeScore {
    public double score(GameStateInterface gs, Node node) {
        // returns the path distance between this current and the agent
        return gs.getMaze().dist(gs.getPacman().current, node);
    }
}
