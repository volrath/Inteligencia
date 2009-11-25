package screenpac.adapter;

import games.pacman.core.FullGame;
import games.pacman.view.DisplayComponent;
import screenpac.model.Maze;
import screenpac.model.GameStateSetter;
import screenpac.model.GameStateView;
import screenpac.controllers.SimplePillEater;
import screenpac.controllers.AgentInterface;
import screenpac.ghosts.GhostTeamController;
import screenpac.ghosts.LegacyTeam;
import utilities.JEasyFrame;

public class WrapperTest {
    public static void main(String[] args) throws InterruptedException {
        FullGame fg = new FullGame();
        Maze maze = new Maze();
        maze.processOldMaze(fg.maze);
        // MapView.test(map);
        GameStateSetter gs = new GameStateSetter(maze);
        LegacyGameWrapper lgw = new LegacyGameWrapper(fg, gs);
        lgw.updateState();
        GameStateView gsv = GameStateView.test(gs);
        // replace this SimplePillEater with your own better screenpac.game!
        AgentInterface agent = new SimplePillEater();
        ControllerAdapter controller = new ControllerAdapter(gs, agent);
        fg.setController(controller);
        GhostTeamController ghosts = new LegacyTeam();
        // ghosts.setProxies(fg.ghosts);
        lgw.ghosts = ghosts;
        DisplayComponent dc = new DisplayComponent(fg.width, fg.height);
        dc.updateObjects(fg.maze.lines);
        JEasyFrame frame = new JEasyFrame(dc, "Pac-Man", true);
        frame.center();

        int i = 0;
        int delay = 50;
        // now go into a loop until the game terminates
        while(!fg.eaten()) {
            System.out.println("Cycle: " + i++);
            lgw.updateState();
            fg.modelCycle();
            dc.repaint();
            gsv.repaint();
            Thread.sleep(delay);
            System.out.println("");
        }
        System.out.println("End of game");
    }
}
