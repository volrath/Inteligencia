package screenpac.model;

import java.util.BitSet;

public interface GameStateInterface {
    GameStateInterface copy();
    void next(int pacDir, int[] ghostDirs);
    Agent getPacman();
    MazeInterface getMaze();
    BitSet getPills();
    BitSet getPowers();
    GhostState[] getGhosts();
    int getScore();
    int getGameTick();
    int getEdibleGhostScore();
}
