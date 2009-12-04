package games.pacman.core;

import games.pacman.controllers.PacController;
import games.pacman.controllers.RandomController;
import games.pacman.controllers.SimpleAvoidance;
import games.pacman.controllers.TDController;
import games.pacman.ghost.Ghost;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeOne;
import games.pacman.maze.MazeNode;
import games.pacman.core.PacMan;
import games.pacman.core.GameFrame;
import utilities.ElapsedTimer;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.learning.genetic.GeneticAlgorithm;


public class FullGame {
    // the full game - but just the model and screenpac.game
    // begins with a maze and the PacMan

    static int reversalTime = 250;
    public static int width = 223;
    public static int height = 266;
    int delay = 50;
    // static int mag = 2;
    int nSteps;
    int nPills; // number of pills eaten on this level - determines when level is complete
    int totalPills; // total number of pills on level

    // DisplayComponent dc;
    public OldMaze maze;
    PacController controller;
    public PacMan pacman;
    int nGhosts = 4; // 1;
    public Ghost[] ghosts;
    // JEasyFrame frame;
    public GameFrame frame;

    static int startingGhostScore = 200;
    int ghostScore;
    int score; // game score

    public static void main(String[] args) {
        int[] hidden_neurons = {4};
        FeedForwardNeuralNetwork ne = new FeedForwardNeuralNetwork(2,hidden_neurons,1);
        GeneticAlgorithm ga = new GeneticAlgorithm(ne);
        FullGame game = new FullGame();
        PacController pc;
        pc = new RandomController();
        pc = new SimpleAvoidance(game.maze, game.ghosts, game.pacman);
        game.setController(pc);
        ElapsedTimer t = new ElapsedTimer();
        int lives = 3;
        int maxIts = 10000;
        int score = game.runModel( lives, maxIts );
        System.out.println(t);
        System.out.println("Score = " + score);
    }       

    public void setController(PacController controller) {
        this.controller = controller;
    }

    public FullGame() {
        maze = MazeOne.getMaze();
        pacman = new PacMan();
        ghosts = new Ghost[nGhosts];
        maze.lines.add(pacman);
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(maze, pacman, ghosts);
            maze.lines.add(ghosts[i]);
            maze.place(ghosts[i]);
            //System.out.println(ghosts[i].eval);
        }
        maze.place(pacman);
        // maze.print(System.out);
    }

    public void reverseGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].reverse();
        }
    }

    public void edibleGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setEdible();
        }
    }

    public int runModel(int lives, int maxIts) {
        nSteps = 0;
        score = 0;
        resetLevel();
        int i = maxIts;
        do {
            modelCycle();
            i--;
            if (eaten()) {
                lives--;
                // System.out.println("Lives left: " + lives + " ; score : " + score);
                initialPositions();
            }
        } while( lives > 0 && i > 0);
        // System.out.println("Survived for: " + (maxIts - i));
        return score;
    }

    public void resetLevel() {
        nPills = 0;
        initialPositions();
        maze.reset();
        totalPills = maze.pills.size() + maze.power.size();
    }

    public void initialPositions() {
        for (int i = 0; i < ghosts.length; i++) {
            maze.place(ghosts[i]);
        }
        maze.place(pacman);
    }

    public void randomise() {
        for (int i = 0; i < ghosts.length; i++) {
            maze.place(ghosts[i]);
        }
        maze.place(pacman);
    }

    public boolean eaten() {
        for (int i = 0; i < ghosts.length; i++) {
            if (pacman.overlap(ghosts[i])) {
                // System.out.println("Distance: " + maze.dist[pacman.ix][ghosts[i].current.ix]);
                // now see if pacman or the ghost gets eaten!!
                if (ghosts[i].edible()) {
                    // eat the bugger
                    score += ghostScore;
                    train(ghostScore * eatGhostReward);
                    ghostScore *= 2;
                    ghosts[i].edible = 0;
                    maze.place(ghosts[i]);
                } else {
                    train(killReward);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean learning = true;
    public static double killReward = -1;
    public static double pillRewards = 20.0 / 1600;
    public static double eatGhostReward = 1.0 / 1600;

    private void train(double reward) {
        if (learning && controller instanceof TDController) {
            TDController tdc = (TDController) controller;
            tdc.update(reward);
        }
    }

    public synchronized void modelCycle() {
        // one cycle of the game loop
        nSteps++;
        int dir = controller.getDirection();
        if (dir != PacController.CENTRE) {
            pacman.setDesire(dir);
        }
        pacman.move();
        // check to see if a pill has been eaten
        int pill = pacman.tryEat();
        // now take appropriate action...
        if (pill == MazeNode.PILL) {
            score += pill;
            nPills++;
            train(pillRewards);
        }

        if (pill == MazeNode.POWER_PILL) {
            nPills++;
            ghostScore = startingGhostScore;
            score += pill;
            reverseGhosts();
            edibleGhosts();
        }
        // System.out.println("Moving ghosts: " + ghosts.length);
        for (int i = 0; i < ghosts.length; i++) {
            //System.out.println("Processing ghost: " + i);
            if ((nSteps) % reversalTime == 0) {
                ghosts[i].reverse();
            } else {
                ghosts[i].move();
            }
        }
        if (nPills == totalPills) {
            resetLevel();
        }
        if (frame != null) {
            try {
                frame.updateView(this);
                Thread.sleep(delay);
            } catch(Exception e) {}
        }
    }
}
