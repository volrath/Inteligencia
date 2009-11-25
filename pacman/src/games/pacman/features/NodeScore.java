package games.pacman.features;

import games.pacman.maze.MazeNode;

/**
 * User: Simon
 * Date: 11-Dec-2007
 * Time: 10:23:13
 */
public interface NodeScore {
    public double score(MazeNode node);
}
