package screenpac.model;

import games.pacman.maze.MazeOne;
import utilities.JEasyFrame;
import utilities.ElapsedTimer;
import utilities.StatSummary;
import screenpac.extract.Constants;
import screenpac.controllers.*;
import screenpac.ghosts.GhostTeamController;
import screenpac.ghosts.RandTeam;
import screenpac.ghosts.PincerTeam;
import screenpac.ghosts.LegacyTeam;

public class Game implements Constants {
    // this class brings together the agent
    // controllers together with the model
    // and may also be responsible for taking
    // actions that depend on the game state
    static int delay = 20;
    static boolean visual = true;

    public static void main(String[] args) throws Exception {
        AgentInterface agent = null; // new SimplePillEater();
        agent = new SimplePillEater();
        GhostTeamController ghostTeam;
        //ghostTeam = new RandTeam();
        ghostTeam = new LegacyTeam();
        // ghostTeam = new PincerTeam();

        if (visual) runVisual(agent, ghostTeam);
        else runDark(agent, ghostTeam);
    }

    public static void runDark (AgentInterface agentController, GhostTeamController ghostTeam) throws Exception {
        Maze maze = new Maze();
        maze.processOldMaze(MazeOne.getMaze());
        GameState gs = new GameState(maze);
        gs.reset();
        Game game = new Game(gs, null, agentController, ghostTeam);
        ElapsedTimer t = new ElapsedTimer();
        int nRuns = 100;
        StatSummary ss = new StatSummary();
        for (int i=0; i<nRuns; i++) {
            game.gs.reset();
            game.run();
            ss.add(game.gs.score);
            System.out.println("Final score: " + game.gs.score + ", ticks = " + game.gs.gameTick);
        }
        System.out.println(t);
        System.out.println(ss);
    }

    public static void runVisual(AgentInterface agentController, GhostTeamController ghostTeam) throws Exception {
        Maze maze = new Maze();
        maze.processOldMaze(MazeOne.getMaze());
        GameState gs = new GameState(maze);
        gs.reset();
        GameStateView gsv = new GameStateView(gs);
        JEasyFrame fr = new JEasyFrame(gsv, "Pac-Man vs Ghosts", true);
        KeyController kc = new KeyController();
        fr.addKeyListener(kc);
        // set the key controller if the agent is null
        if (agentController == null) agentController = kc;
        Game game = new Game(gs, gsv, agentController, ghostTeam);
        game.frame = fr;
        game.run();
        // use line below to run for a max number of cycles
        // game.run(100);
        System.out.println("Final score: " + game.gs.score);
    }

    public void run() throws Exception {
        System.out.println("nLives left: " + gs.nLivesRemaining);
        while(!gs.terminal()) {
            cycle();
            System.out.println(gs.pills.cardinality() + " : " + gs.powers.cardinality());
        }
        System.out.println("nLives left: " + gs.nLivesRemaining);
    }

    public void run(int n) throws Exception {
        int i=0;
        while(i++ < n && !gs.terminal()) {
            cycle();
        }
    }

    public void cycle() throws Exception {
        // update the game state
        gs.next(agentController.action(gs), ghostTeam.getActions(gs));
        if (gsv != null) {
            gsv.repaint();
            if (frame != null) frame.setTitle("Score: " + gs.score);
            Thread.sleep(delay);
        }
    }

    public Game(GameState gs, GameStateView gsv, AgentInterface agentController, GhostTeamController ghostTeam) {
        this.gs = gs;
        this.gsv = gsv;
        this.agentController = agentController;
        this.ghostTeam = ghostTeam;
    }

    GameState gs;
    GameStateView gsv;
    AgentInterface agentController;
    GhostTeamController ghostTeam;
    JEasyFrame frame;

}
