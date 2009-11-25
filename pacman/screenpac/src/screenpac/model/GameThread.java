package screenpac.model;

import games.pacman.maze.MazeOne;
import utilities.JEasyFrame;
import utilities.ElapsedTimer;
import utilities.StatSummary;
import screenpac.extract.Constants;
import screenpac.controllers.AgentInterface;
import screenpac.controllers.SimplePillEater;
import screenpac.controllers.RandomNonReverseAgent;
import screenpac.ghosts.GhostTeamController;
import screenpac.ghosts.RandTeam;
import screenpac.ghosts.PincerTeam;
import screenpac.ghosts.LegacyTeam;

public class GameThread implements Constants {
    // this class brings together the agent
    // controllers together with the model
    // and may also be responsible for taking
    // actions that depend on the game state
    static int delay = 40;
    static boolean visual = true;

    public static void main(String[] args) throws Exception {
        AgentInterface agent = new SimplePillEater();
        agent = new RandomNonReverseAgent();
        GhostTeamController ghostTeam;
        ghostTeam = new RandTeam();
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
        GameThread game = new GameThread(gs, null, agentController, ghostTeam);
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
        GameThread game = new GameThread(gs, gsv, agentController, ghostTeam);
        game.frame = fr;
        game.run();
        // use line below to run for a max number of cycles
        // game.run(100);
        System.out.println("Final score: " + game.gs.score);
    }

    public void run() throws Exception {
        setProxies();
        while(!gs.terminal()) {
            cycle();
        }
        killProxies();
    }

    public void run(int n) throws Exception {
        setProxies();
        int i=0;
        while(i++ < n && !gs.terminal()) {
            cycle();
        }
        killProxies();
    }

    private void setProxies() {
        agentThread = new AgentThread(agentController);
        ghostTeamThread = new GhostTeamThread(ghostTeam);
    }

    private void killProxies() {
        agentThread.die();
        ghostTeamThread.die();
    }

    public void cycle() throws Exception {
        // update the game state
        // call out for the next directions
        agentThread.action(gs, this);
        ghostTeamThread.action(gs, this);
        Thread.sleep(delay);
        // after the delay the callbacks should have come through
        gs.next(agentDir, ghostTeamDirs);
        if (gsv != null) {
            gsv.repaint();
            if (frame != null) frame.setTitle("Score: " + gs.score);
        }
    }

    public GameThread(GameState gs, GameStateView gsv, AgentInterface agentController, GhostTeamController ghostTeam) {
        this.gs = gs;
        this.gsv = gsv;
        this.agentController = agentController;
        this.ghostTeam = ghostTeam;
        ghostTeamDirs = new int[dx.length];
    }

    public void setAgentDir(int dir) {
        agentDir = dir;
    }

    public void setGhostTeamDirs(int[] dirs) {
        for (int i=0; i<dirs.length; i++) {
            ghostTeamDirs[i] = dirs[i];
        }
    }

    int agentDir;
    int[] ghostTeamDirs;

    AgentThread agentThread;
    GhostTeamThread ghostTeamThread;
    GameState gs;
    GameStateView gsv;
    AgentInterface agentController;
    GhostTeamController ghostTeam;
    JEasyFrame frame;

}