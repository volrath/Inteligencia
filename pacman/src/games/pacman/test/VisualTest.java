package games.pacman.test;

import games.pacman.core.GameFrame;
import games.pacman.controllers.PacController;
import games.pacman.controllers.SimpleAvoidance;
import utilities.ElapsedTimer;

/**
 * This test checks that we can construct a random MLP and then
 * evaluate it.
 */
public class VisualTest {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        SimpleEvaluator se = new SimpleEvaluator(frame.game);
        se.nTrials = 1;
        se.game.frame = frame;
        ElapsedTimer t = new ElapsedTimer();
        PacController cont = new SimpleAvoidance(frame.game);
        se.eval(cont);
        System.out.println(t);
    }
}