package screenpac.ghosts;

import screenpac.features.NodeScore;
import screenpac.model.Node;
import screenpac.model.GameStateInterface;

public class AimPoint implements NodeScore {
    int x, y;

    public AimPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double score(GameStateInterface gs, Node node) {
        // manhattan distance between node and x,y point
        return Math.abs(node.x - x) + Math.abs(node.y - y);
    }
}