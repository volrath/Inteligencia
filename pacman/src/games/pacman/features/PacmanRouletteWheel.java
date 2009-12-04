package games.pacman.features;

import neuralj.networks.feedforward.learning.genetic.selection.RouletteWheel;
import neuralj.networks.feedforward.learning.genetic.selection.RouletteSlice;
import neuralj.networks.feedforward.learning.bprop.BackPropagation;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

import java.util.Arrays;

import games.pacman.core.FullGame;
import games.pacman.controllers.PacController;
import games.pacman.controllers.RandomController;
import games.pacman.controllers.SimpleAvoidance;
import games.pacman.controllers.NeuroticPacmanController;
import utilities.ElapsedTimer;

/**
 * Created by IntelliJ IDEA.
 * User: kristoffer
 * Date: Dec 3, 2009
 * Time: 7:20:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacmanRouletteWheel extends RouletteWheel {
    FullGame game = new FullGame();
    PacController pc;    

    public void addMember(FeedForwardNeuralNetwork p_member)
	{
		RouletteSlice slice = new RouletteSlice();
        this.pc = new NeuroticPacmanController(game, p_member);
		this.game.setController(this.pc);
		slice.member = p_member;
		slice.fitness = calcFitness(10);
		this.roulette_slices.add(slice);
		Object[] list = this.roulette_slices.toArray();
		Arrays.sort(list);
		this.roulette_slices.clear();
		for (Object sliceobj : list)
			this.roulette_slices.add((RouletteSlice) sliceobj);
	}

    public double calcFitness(int numIter){
        double score = 0;
        ElapsedTimer t = new ElapsedTimer();
        int lives = 3;
        int maxIts = 10000;        
        for(int i = 0; i < numIter; i++) {
            score += this.game.runModel( lives, maxIts );
        }
        return (score/numIter);
    }
}
