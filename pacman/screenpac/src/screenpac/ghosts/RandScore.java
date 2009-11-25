package screenpac.ghosts;

import screenpac.features.NodeScore;
import screenpac.model.Node;
import screenpac.model.GameState;
import screenpac.model.GameStateInterface;

import java.util.Random;

public class RandScore implements NodeScore {
    static Random r = new Random();
    public double score(GameStateInterface gs, Node node) {
        return r.nextDouble();
    }
}
