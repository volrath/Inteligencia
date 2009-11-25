package screenpac.features;

import screenpac.model.Node;
import screenpac.model.GameState;
import screenpac.model.GameStateInterface;

import java.awt.*;

public class NearestPillDistance implements NodeScore {
    static int max = Integer.MAX_VALUE;

    // this can be useful: make it public
    public Node closest = null;

    public double score(GameStateInterface gs, Node node) {
        // most obvious way: iterate over the set of pills
        // int closest
        int minDist = max;
        for (Node n : gs.getMaze().getPills()) {
            // check the state of this pill
            // by querying the BitSet of pill states
            if (gs.getPills().get(n.pillIndex)) {
                // this pill is on, but is it closer?
                if (gs.getMaze().dist(node, n) < minDist) {
                    minDist = gs.getMaze().dist(node, n);
                    closest = n;
                }
            }
        }
        if (closest != null) {
            closest.col = Color.green;
        }
        return minDist;
    }
}
