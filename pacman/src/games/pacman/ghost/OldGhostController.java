package games.pacman.ghost;

import games.pacman.maze.MazeNode;

/**
 * Created by IntelliJ IDEA.
 * User: simon
 * Date: 11-Dec-2008
 * Time: 23:49:49
 * To change this template use File | Settings | File Templates.
 */
public interface OldGhostController {
    public MazeNode getBest(MazeNode[] next, MazeNode previous);
}
