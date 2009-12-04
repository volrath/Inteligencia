package samples;

import neuralj.Serializer;
import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm.LearningStrategy;
import neuralj.networks.feedforward.learning.genetic.GeneticAlgorithm;
import neuralj.networks.feedforward.learning.genetic.crossover.CrossoverDoublePoint;
import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.learning.genetic.selection.SelectionRouletteWheel;
import neuralj.testers.ConsoleTester;
import neuralj.testers.StringTester;
import neuralj.watchers.ConsoleWatcher;

/**
 * Generalizes a set where the activation is 1 when most inputs are one, and 0
 * otherwise
 */
public class GeneralizeMostPopularGenetic
{
	public static void main(String[] args) throws InterruptedException
	{
		// Create the pattern set
		PatternSet pattern_set = new PatternSet();
		// Define the token to split the pattern items by
		Pattern.split_token = ",";
		// Load the pattern set
		pattern_set.loadPatterns("mostpopular.patterns", 5);
		// Shuffles and distributes the patterns into training, validation and
		// test sets
		pattern_set.generateSets();
		// Initialize the network by giving it's input neurons, hidden neurons
		// and output neurons
		FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(5, new int[] { 3 }, 1);
		// Initialize the BackPropagation algorithm
		GeneticAlgorithm ga = new GeneticAlgorithm(net);
		// Specify the pattern set that's going to be used for training
		ga.pattern_set = pattern_set;
		// Specify the learning method (Memorize,Generalize)
		ga.learning_strategy = LearningStrategy.Generalization;
		// (OPTIONAL) Specify the desired error (the lower the error, the more
		// accurate the
		// results)
		ga.desired_error = 0.001;
		// (OPTIONAL) Specify the maximum number of epochs the training may take
		ga.maximum_epochs = 1000000000;
		// (OPTIONAL) Specify the crossover operator (the way the genes are
		// mixed)
		ga.crossover_operator = new CrossoverDoublePoint();
		// (OPTIONAL) Specify the selection operator
		// (the way to select the chromosomes that pass to another generation)
		ga.selection_operator = new SelectionRouletteWheel();
		// (OPTIONAL) Specify the mutation operator (how genes are affected by
		// randomness)
		ga.mutation_operator = new MutationRandom();
		// Attach a FileWatcher to store the training and validation error data
		// in CSV formatted files
		ga.watcher = new ConsoleWatcher(ga);
		// Since we are going to want to wait for the end of the training
		// thread,
		// we have to specify in advance that the training is undergoing,
		// otherwise,
		// the testing would take place before the start of the training
		ga.is_running = true;
		// Start the training
		ga.start();
		// Wait for the training to finish (sleeping for 1 second every pass to
		// save CPU time)
		while (ga.is_running)
			Thread.sleep(1000);
		// Test the network's accuracy and return the output to a string
		String output = new StringTester(net).test(pattern_set);
		// Print the string with the output
		System.out.println(output);
		// Save the network to a file
		Serializer.saveObject(net, "network.net");
		// Restore the network
		net = (FeedForwardNeuralNetwork) Serializer.loadObject("network.net");
		// Test the results again
		System.out.println("--- Results with the stored network ---");
		new ConsoleTester(net).test(pattern_set);
	}
}
