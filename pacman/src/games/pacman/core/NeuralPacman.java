package games.pacman.core;

import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.SynapseLayer;
import neuralj.networks.feedforward.learning.genetic.GeneticAlgorithm;
import games.pacman.features.PacmanGeneticAlgorithm;
import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.learning.genetic.selection.SelectionRouletteWheel;
import neuralj.networks.feedforward.learning.genetic.crossover.CrossoverDoublePoint;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;
import neuralj.datasets.PatternSet;
import neuralj.datasets.Pattern;
import neuralj.watchers.ConsoleWatcher;
import neuralj.Serializer;
import games.pacman.features.PacmanSelectionRouletteWheel;
import games.pacman.features.PacmanMutationRandom;
import games.pacman.features.PacmanFeedForwardNeuralNetwork;
import games.pacman.controllers.SmartController;
import games.pacman.controllers.RandomController;
import games.pacman.controllers.NeuroticPacmanController;
import games.pacman.controllers.PacController;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeNode;

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
        PacmanFeedForwardNeuralNetwork net = new PacmanFeedForwardNeuralNetwork(13, new int[] { 20 }, 1);
        PacmanGeneticAlgorithm ga = new PacmanGeneticAlgorithm(net);
        ga.pattern_set = pattern_set;
        ga.learning_strategy = PacmanGeneticAlgorithm.LearningStrategy.Optimization;
        ga.desired_error = -10;
		ga.maximum_epochs = 25;
        ga.crossover_operator = new CrossoverDoublePoint();
        ga.selection_operator = new PacmanSelectionRouletteWheel();
		ga.mutation_operator = new PacmanMutationRandom();
		ga.watcher = new ConsoleWatcher(ga);
		ga.is_running = true;
		ga.start();
        while (ga.is_running)
			Thread.sleep(1000);
        Serializer.saveObject(net, "network.net");
        int i = 0;
        for(SynapseLayer sl: net.synapse_layers){
            System.out.println("Capa de Sinapsis "+i+": ");
            for(int j = 0; j < sl.getWeightVector().size(); j++){
                System.out.println("j: "+j+ " Peso: "+sl.getWeightVector().get(j));
            }
            i++;
        }
        // Restore the network
        net = (PacmanFeedForwardNeuralNetwork) Serializer.loadObject("network.net");
        System.out.println("Best Fitness: "+net.calcFitness(50));
        // Test the results again
        NeuroticPacmanController pc = new NeuroticPacmanController(net);
        pc.verbose = 1;
        GameFrame game = new GameFrame(pc);
        pc.setGame(game.game);
        game.run();
    }
}
