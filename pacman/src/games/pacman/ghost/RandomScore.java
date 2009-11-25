
package games.pacman.ghost;

import games.pacman.ghost.Ghost;
import games.pacman.features.NodeEvaluator;
import games.pacman.maze.MazeNode;
import games.pacman.core.PacMan;
import games.pacman.maze.OldMaze;

import java.util.Random;

public class RandomScore implements NodeEvaluator {
    static Random r = new Random();
    public double score(MazeNode node, Ghost me, PacMan pacman, Ghost[] ghosts, OldMaze maze) {
        return r.nextDouble();
    }
}
