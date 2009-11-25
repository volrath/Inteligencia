package screenpac.ghosts;

import screenpac.features.NodeScore;
import screenpac.model.Node;
import screenpac.model.GameStateInterface;

public class ManhattanScore implements NodeScore {

    public double score(GameStateInterface gs, Node node) {
        // returns the path distance between this current and the agent
        Node pac = gs.getPacman().current;
        return Math.abs(node.x - pac.x) + Math.abs(node.y - pac.y);
    }
}
