package screenpac.adapter;

import games.pacman.core.FullGame;
import games.pacman.maze.MazeNode;
import games.pacman.ghost.Ghost;
import screenpac.model.Maze;
import screenpac.model.GameStateSetter;
import screenpac.model.GameStateView;
import screenpac.ghosts.GhostTeamController;

public class LegacyGameWrapper {
    // this class test the game wrapper in a static way
    public static void main(String[] args) {
        FullGame fg = new FullGame();
        Maze maze = new Maze();
        maze.processOldMaze(fg.maze);
        // MapView.test(map);
        GameStateSetter gs = new GameStateSetter(maze);
        LegacyGameWrapper lgw = new LegacyGameWrapper(fg, gs);
        lgw.updateState();
        GameStateView gsv = GameStateView.test(gs);
    }

    FullGame fg;
    GameStateSetter gs;
    public GhostTeamController ghosts;

    public LegacyGameWrapper(FullGame fg, GameStateSetter gs) {
        this.fg = fg;
        this.gs = gs;
    }

    public void updateState() {
        // update the pill states
        gs.reset();
        for (MazeNode mn : fg.maze.pills) {
            if (mn.pill == MazeNode.PILL) {
                gs.setPill(mn.x, mn.y);
            }
        }
        // power pills
        for (MazeNode mn : fg.maze.power) {
            if (mn.pill == MazeNode.POWER_PILL) {
                gs.setPower(mn.x, mn.y);
            }
        }
        // the agent
        gs.setAgent(fg.pacman.current.x, fg.pacman.current.y);

        // the ghosts
        for (int i=0; i<fg.ghosts.length; i++) {
            Ghost g = fg.ghosts[i];
            // need to account for edible or not
            gs.setGhost(g.current.x, g.current.y, i, g.edible);
        }
        // if the ghostTeam is not null, force their actions
        if (ghosts != null) {
            ghosts.getActions(gs);
        }
    }
}
