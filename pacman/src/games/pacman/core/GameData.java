package games.pacman.core;

import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;
import games.pacman.ghost.Ghost;

import java.util.ArrayList;

public class GameData {
    public OldMaze maze;
    public Ghost[] ghosts;
    public PacMan pacMan;

    public GameData(OldMaze maze, Ghost[] ghosts, PacMan pacMan) {
        this.maze = maze;
        this.ghosts = ghosts;
        this.pacMan = pacMan;
    }

    public double pillDist(MazeNode node, ArrayList pills) {
        double d = maze.h * 2;
        for (Object pill1 : pills) {
            // find closest junction
            MazeNode pill = (MazeNode) pill1;
            if (pill.pill != MazeNode.NO_PILL) {
                d = Math.min(d, maze.dist[node.ix][pill.ix]);
            }
        }
        return d;
    }
}
