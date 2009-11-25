package games.pacman.core;

import utilities.ElapsedTimer;
import games.pacman.controllers.ConstantController;
import games.pacman.controllers.SimpleAvoidance;
import games.pacman.controllers.RandomController;
import games.pacman.controllers.PacController;
import games.pacman.ghost.Ghost;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeOne;
import games.pacman.core.PacMan;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 13-Oct-2004
 * Time: 15:33:22
 * To change this template use Options | File Templates.
 */

public class Evaluator {

    OldMaze maze;
    PacMan pacman;
    int nGhosts = 4;
    Ghost[] ghosts;
    int nSteps = 10000;

    public static void main(String[] args) {

        Evaluator game = new Evaluator();
        ElapsedTimer t = new ElapsedTimer();
        PacController pc = new RandomController();
        PacController avoider = new SimpleAvoidance(game.maze, game.ghosts, game.pacman);
        double fitness = game.fitness(avoider);
        System.out.println(t);
        System.out.println("Fitness: " + fitness);

        // System.exit(0);
        PacController cc = new ConstantController();
        t.reset();
        game.playOff(100, avoider, cc);
        System.out.println(t);
    }

    public int playOff(int n, PacController c1, PacController c2) {
        int c1Wins = 0;
        for (int i=0; i<n; i++) {
            if (fitness(c1) > fitness(c2)) {
                c1Wins++;
            }
        }
        System.out.println("C1 wins " + c1Wins + " out of " + n);
        return c1Wins;
    }

    public Evaluator() {
        maze = MazeOne.getMaze();
        // new CloseableFrame(dc, "Simple Pac-Man", true).center();
        // JFrame frame = new JFrame();
        pacman = new PacMan();
        ghosts = new Ghost[nGhosts];
        maze.lines.add(pacman);
        for (int i=0; i<ghosts.length; i++) {
            ghosts[i] = new Ghost(maze, pacman, ghosts);
            maze.lines.add(ghosts[i]);
            maze.place(ghosts[i]);
        }
        maze.place(pacman);
    }

    public double fitness(PacController controller) {
        randomise();
        double score = runModel(controller, nSteps);
        return score;
    }

    public int runModel(PacController controller, int its) {
        int lives = 3;
        do {
            its--;
            modelCycle(controller);
            if (eaten()) {
                lives--;
                randomise();
            }
        } while( lives > 0 && its > 0);
        // unfinished!!!!
        return 0;
    }

    public void randomise() {
        for (Ghost ghost : ghosts) {
            maze.place(ghost);
        }
        maze.place(pacman);
    }

    public boolean eaten() {
        for (Ghost ghost : ghosts) {
            if (pacman.overlap(ghost)) {
                return true;
            }
        }
        return false;
    }

    public void modelCycle(PacController controller) {
        // one cycle of the game loop
        int dir = controller.getDirection();
        // System.out.println("Direction: " + dir);
        // now work out the movement
        // pacman.move( PacController.dx[dir], PacController.dy[dir] );
        if (dir != PacController.CENTRE) {
            pacman.setDesire(dir);
        }
        pacman.move();
        for (Ghost ghost : ghosts) {
            ghost.move();
        }
        // dc.paintComponent( dc.getGraphics() );
        // dc.repaint();
    }
}
