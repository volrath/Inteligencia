package games.pacman.test;

import utilities.ElapsedTimer;
import utilities.StatSummary;
import games.pacman.controllers.SimpleAvoidance;
import games.pacman.controllers.PacController;
import games.pacman.core.FullGame;

public class SimpleEvaluator {

    public static void main(String[] args) {
        SimpleEvaluator ce = new SimpleEvaluator();
        PacController avoider =
                new SimpleAvoidance(ce.game.maze, ce.game.ghosts, ce.game.pacman);
        ce.eval(avoider);
    }

    public int nTrials = 100;
    public FullGame game;

    public SimpleEvaluator() {
        game = new FullGame();
    }

    public SimpleEvaluator(FullGame game) {
        this.game = game;
    }

    public StatSummary eval(PacController pc) {
        StatSummary ss = new StatSummary();
        game.setController(pc);
        ElapsedTimer t = new ElapsedTimer();
        int lives = 3;
        int maxIts = 10000;
        for (int i = 0; i < nTrials; i++) {
            int score = game.runModel(lives, maxIts);
            ss.add(score);
        }
        System.out.println(t);
        System.out.println(ss);
        return ss;
    }

}