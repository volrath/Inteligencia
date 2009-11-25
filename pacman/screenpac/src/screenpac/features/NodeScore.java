package screenpac.features;

import screenpac.model.Node;
import screenpac.model.GameStateSetter;
import screenpac.model.GameState;
import screenpac.model.GameStateInterface;

public interface NodeScore {
    public double score(GameStateInterface gs, Node node);
}
