package games.pacman.core;

import utilities.JEasyFrame;
import games.pacman.controllers.KeyController;
import games.pacman.controllers.PacController;
import games.pacman.ghost.Ghost;
import games.pacman.view.DisplayComponent;
import games.pacman.core.FullGame;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 13-Oct-2004
 * Time: 15:33:22
 * To change this template use Options | File Templates.
 */

public class GameFrame extends Thread {
    static int width = 223;
    static int height = 266;
    public DisplayComponent dc;
    PacController controller;
    JEasyFrame frame;
    public FullGame game;
    public int delay = 50;

    public static void main(String[] args) {

        // FullGame game = new FullGame();
        GameFrame game = new GameFrame();
        // game.game.setController(new SimpleAvoidance(game.game));
        game.start();
    }

    public GameFrame() {
        this(new KeyController());
    }

    public GameFrame(PacController controller) {
        this.controller = controller;
        Ghost.nColor = 0;
        game = new FullGame();
        dc = new DisplayComponent(width, height);
        dc.updateObjects(game.maze.lines);
        frame = new JEasyFrame(dc, "Pac-Man", true);
        frame.center();
        game.setController(controller);
        game.randomise();

        if (controller instanceof KeyController) {
            dc.addKeyListener((KeyController) controller);
            dc.requestFocus();
        }
    }

    public void run() {
        int lives = 3;
        System.out.println("Started!");
        while (true) {
            try {
                game.modelCycle();
                dc.repaint();
                frame.setTitle("Score: " + game.score);
                if (game.eaten()) {
                    lives--;
                    game.initialPositions();
                    System.out.println("Lives left: " + lives);
                    if (lives == 0) {
                        System.out.println("Game over: " + game.score);
                        System.out.println("nSteps: " + game.nSteps);
                        break;
                    }
                }
                sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
                // System.out.println(e);
            }
        }
    }

    public void updateView(FullGame fullGame) {
        dc.repaint();
        frame.setTitle("Score: " + game.score);
    }
}
