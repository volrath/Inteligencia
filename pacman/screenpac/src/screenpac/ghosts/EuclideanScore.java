package screenpac.ghosts;

import screenpac.features.NodeScore;
import screenpac.model.Node;
import screenpac.model.GameStateInterface;

public class EuclideanScore implements NodeScore {
    public double score(GameStateInterface gs, Node node) {
        // returns the path distance between this current and the agent
        Node pac = gs.getPacman().current;
        return Math.sqrt(sqr(node.x - pac.x) + sqr(node.y - pac.y));
    }

    private static double sqr(double x) {
        return x * x;
    }

}