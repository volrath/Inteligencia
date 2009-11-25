package games.pacman.features;

import games.pacman.maze.MazeNode;
import games.pacman.core.GameData;

public interface FeatureExtractor {
    public double[] setVec(MazeNode x, GameData vc);
    public int size();
}
