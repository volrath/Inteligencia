package samples;

import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm.LearningStrategy;
import neuralj.networks.feedforward.learning.rprop.ResilientBackPropagation;
import neuralj.testers.StringTester;
import neuralj.watchers.FileWatcher;

/**
 * Generalizes a set where the activation is 1 when most inputs are one, and 0
 * otherwise
 */
public class GeneralizeMostPopularRProp
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
		ResilientBackPropagation rp = new ResilientBackPropagation(net);
		// Specify the pattern set that's going to be used for training
		rp.pattern_set = pattern_set;
		// Specify the learning method (Memorize,Generalize)
		rp.learning_strategy = LearningStrategy.Generalization;
		// (OPTIONAL) Specify the desired error (the lower the error, the more accurate the
		// results)
		rp.desired_error = 0.001;
		// (OPTIONAL) Specify the maximum number of epochs the training may take
		rp.maximum_epochs = 1000000000;
		// Attach a FileWatcher to store the training and validation error data
		// in CSV formatted files
		rp.watcher = new FileWatcher(rp, "training_error.log", "validation_error.log");
		// Since we are going to want to wait for the end of the training
		// thread,
		// we have to specify in advance that the training is undergoing,
		// otherwise,
		// the testing would take place before the start of the training
		rp.is_running = true;
		// Start the training
		rp.start();
		// Wait for the training to finish (sleeping for 1 second every pass to
		// save CPU time)
		while (rp.is_running)
			Thread.sleep(1000);
		// Test the network's accuracy and return the output to a string
		String output = new StringTester(rp.network).test(pattern_set);
		// Print the string with the output
		System.out.println(output);
	}
}
