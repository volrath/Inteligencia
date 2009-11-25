package games.pacman.features;

import games.pacman.ghost.Ghost;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;
import games.pacman.features.NodeEvaluator;
import games.pacman.core.PacMan;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 31-Oct-2004
 * Time: 12:14:22
 * To change this template use File | Settings | File Templates.
 */
public class YScore implements NodeEvaluator {
    public double score(MazeNode node, Ghost me, PacMan pacman, Ghost[] ghosts, OldMaze maze) {
        return Math.abs(node.y - pacman.current.y);
    }
}
