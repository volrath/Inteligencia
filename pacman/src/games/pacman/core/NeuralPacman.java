package games.pacman.core;

import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.learning.genetic.GeneticAlgorithm;
import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.learning.genetic.selection.SelectionRouletteWheel;
import neuralj.networks.feedforward.learning.genetic.crossover.CrossoverDoublePoint;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;
import neuralj.datasets.PatternSet;
import neuralj.datasets.Pattern;
import neuralj.watchers.ConsoleWatcher;
import games.pacman.features.PacmanSelectionRouletteWheel;
import games.pacman.controllers.SmartController;
import games.pacman.controllers.RandomController;
import games.pacman.controllers.NeuroticPacmanController;

/**
 * Created by IntelliJ IDEA.
 * User: kristoffer
 * Date: Dec 3, 2009
 * Time: 8:32:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuralPacman {
    public static void main(String[] args) throws InterruptedException{

        PatternSet pattern_set = new PatternSet();
        Pattern.split_token = ",";
        pattern_set.loadPatterns("uselesspacman.patterns", 13);
        pattern_set.generateSets();
        FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(13, new int[] { 10 }, 1);
        GeneticAlgorithm ga = new GeneticAlgorithm(net);
        ga.pattern_set = pattern_set;
        ga.learning_strategy = FeedForwardNetworkLearningAlgorithm.LearningStrategy.Generalization;
        ga.desired_error = 0;
		ga.maximum_epochs = 1000;
        ga.crossover_operator = new CrossoverDoublePoint();
        ga.selection_operator = new PacmanSelectionRouletteWheel();
		ga.mutation_operator = new MutationRandom();
		ga.watcher = new ConsoleWatcher(ga);
		ga.is_running = true;
		ga.start();
        while (ga.is_running)
			Thread.sleep(1000);
        GameFrame game = new GameFrame();
        FullGame fg = new FullGame();
        game.controller = new NeuroticPacmanController(fg,net);
        game.run();
    }
}
