package games.pacman.features;

import games.pacman.ghost.Ghost;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;
import games.pacman.core.PacMan;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 31-Oct-2004
 * Time: 11:19:10
 * To change this template use File | Settings | File Templates.
 *
 * Implemenetations of this will control the ghost behaviour.
 *
 * For example, scoring regimes can account for proximity to a number
 * of objects, namely the PacMan and the other Ghosts, and can do
 * so both in terms of maze distance and in various geometric
 * distance measures.
 *
 */
public interface NodeEvaluator {

    public double score(MazeNode node, Ghost me, PacMan pacman, Ghost[] ghosts, OldMaze maze);

}
