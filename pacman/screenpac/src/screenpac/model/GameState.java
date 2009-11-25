package screenpac.model;

import screenpac.extract.Constants;
import screenpac.features.Utilities;

import java.util.BitSet;

public class GameState implements GameStateInterface, Constants {
    public Agent getPacman() {
        return pacMan;
    }

    public Maze getMaze() {
        return maze;
    }

    public BitSet getPills() {
        return pills;
    }

    public BitSet getPowers() {
        return powers;
    }

    public GhostState[] getGhosts() {
        return ghosts;
    }

    public int getScore() {
        return score;
    }

    public int getGameTick() {
        return gameTick;
    }

    public int getEdibleGhostScore() {
        return edibleGhostScore;
    }

    public Maze maze;
    public BitSet powers;
    public BitSet pills;
    public Agent pacMan;// what is the difference between a ghost and a Pac-Man?
    public GhostState[] ghosts;
    public int score = 0;
    public int edibleGhostScore;
    int gameTick;
    public int nLivesRemaining;

    public GameStateInterface copy() {
        // return a deep copy of the current game state
        // just copy a reference for the ones that don't change
        GameState gs = new GameState();
        gs.maze = maze;
        gs.powers = (BitSet) powers.clone();
        gs.pills = (BitSet) pills.clone();
        gs.pacMan = pacMan.copy();
        for (int i = 0; i < ghosts.length; i++) {
            gs.ghosts[i] = ghosts[i].copy();
        }
        gs.score = score;
        gs.edibleGhostScore = edibleGhostScore;
        gs.gameTick = gameTick;
        gs.nLivesRemaining = nLivesRemaining;
        return gs;
    }


    public GameState() {
        // not sure there's any point in this without the maze...
        ghosts = new GhostState[nGhosts];
        for (int i = 0; i < nGhosts; i++) {
            ghosts[i] = new GhostState(initGhostDelay[i]);
        }
    }

    public GameState(Maze maze) {
        this.maze = maze;
        ghosts = new GhostState[nGhosts];
        pills = new BitSet(maze.getPills().size());
        powers = new BitSet(maze.getPowers().size());
        reset();
    }

    public void reset() {
        resetAgents();
        resetPills();
        resetScores();
    }

    public void resetAgents() {
        pacMan = new Agent(maze.getMap().get(0));
        pacMan.current = maze.pacStart();
        for (int i = 0; i < nGhosts; i++) {
            ghosts[i] = new GhostState(initGhostDelay[i]);
            ghosts[i].current = maze.ghostStart();
        }
    }

    public void resetPills() {
        pills.set(0, maze.getPills().size());
        powers.set(0, maze.getPowers().size());
    }

    public void resetScores() {
        gameTick = 0;
        score = 0;
        nLivesRemaining = nLives;
    }

    public void nextLevel() {
        resetPills();
        resetAgents();
        // also reduce edible time
    }

    public void next(int pacDir, int[] ghostDirs) {
        // calculate the next game state for agent and ghosts
        pacMan.next(pacDir, maze);
        tryEatPill();
        tryEatPower();
        eatGhosts();
        if (ghostReversal()) {
            reverseGhosts();
        } else {
            moveGhosts(ghostDirs);
        }
        if (pillsCleared()) {
            nextLevel();
            System.out.println("Pills cleared!");
        } else {
            if (agentDeath()) {
                nLivesRemaining--;
                resetAgents();
            }
        }
        gameTick++;
    }

    public boolean ghostReversal() {
        return rand.nextDouble() < 0.005;
    }

    public void reverseGhosts() {
        for (GhostState g : ghosts) g.reverse();
    }

    public void moveGhosts(int[] ghostDirs) {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].next(ghostDirs[i], this);
        }
    }

    public void tryEatPill() {
        int ix = pacMan.current.pillIndex;
        if (ix >= 0) {
            if (pills.get(ix)) {
                pills.clear(ix);
                score += pillScore;
            }
        }
    }

    public void tryEatPower() {
        int ix = pacMan.current.powerIndex;
        if (ix >= 0) {
            if (powers.get(ix)) {
                powers.clear(ix);
                // also trigger edibility
                reverseGhosts();
                setEdible();
                score += powerScore;
            }
        }
    }

    public void setEdible() {
        // each ghost is initialised to it's edible state
        // and keeps track of it's own time
        edibleGhostScore = edibleGhostStartingScore;
        for (GhostState g : ghosts) g.edibleTime = 100;
    }

    // now define a series of tests on the game state

    public boolean pillsCleared() {
        
        return pills.isEmpty() && powers.isEmpty();
    }

    public boolean agentDeath() {
        for (GhostState g : ghosts) {
            if (!g.edible() && !g.returning() && overlap(g, pacMan)) return true;
        }
        return false;
    }

    public void eatGhosts() {
        // eat any edible ghosts
        // this involves incrementing the score, doubling the ghost score
        // for the next ghost to be consumed, and setting the ghost
        // to the "returning" state
        for (GhostState g : ghosts) {
            if (g.edible() && overlap(g, pacMan)) {
                score += edibleGhostScore;
                edibleGhostScore *= 2;
                g.returnNode = maze.ghostStart();
                g.setPredatory();
            }
        }
    }

    public boolean terminal() {
        return nLivesRemaining <= 0;
    }

    public static boolean overlap(GhostState g, Agent agent) {
        return Utilities.manhattan(g.current, agent.current)
                <= agentOverlapDistance;
    }
}
