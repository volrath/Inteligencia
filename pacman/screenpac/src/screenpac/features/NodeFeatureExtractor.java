package screenpac.features;

import screenpac.model.Node;
import screenpac.model.GameState;

public interface NodeFeatureExtractor {
    // all to be in range -1 to +1 ???
    // this one extracts the features for a specified node
    // and is well suited to state value functions
    public double[] getFeatures(GameState gs, Node node);
    public int nFeatures();
}
