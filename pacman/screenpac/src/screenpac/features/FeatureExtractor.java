package screenpac.features;

import screenpac.model.GameState;

public interface FeatureExtractor {
    // all to be in range -1 to +1 ???
    public double[] getFeatures(GameState gs);
    public int nFeatures();
}
