package games.pacman.ghost;

import games.pacman.ghost.Ghost;
import games.pacman.features.NodeEvaluator;
import games.pacman.maze.MazeNode;
import games.pacman.core.PacMan;
import games.pacman.maze.OldMaze;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 31-Oct-2004
 * Time: 12:14:22
 * To change this template use File | Settings | File Templates.
 */
public class PathScore implements NodeEvaluator {
    public double score(MazeNode node, Ghost me, PacMan pacman, Ghost[] ghosts, OldMaze maze) {
        return maze.dist[node.ix][pacman.current.ix];
    }
}
