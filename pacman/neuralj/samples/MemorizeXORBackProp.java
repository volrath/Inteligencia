package samples;

import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm.LearningStrategy;
import neuralj.networks.feedforward.learning.bprop.BackPropagation;
import neuralj.testers.ConsoleTester;
import neuralj.watchers.ConsoleWatcher;

/**
 * This sample teaches the XOR table to the neural network with BackPropagation
 */
public class MemorizeXORBackProp
{
	public static void main(String[] args) throws InterruptedException
	{
		// Create the pattern set
		PatternSet pattern_set = new PatternSet();
		// Define the token to split the pattern items by
		Pattern.split_token = ";";
		// Add the XOR table items
		pattern_set.addPattern(new Pattern("0;0", "0"));
		pattern_set.addPattern(new Pattern("1;0", "1"));
		pattern_set.addPattern(new Pattern("0;1", "1"));
		pattern_set.addPattern(new Pattern("1;1", "0"));
		// Initialize the network by giving it's input neurons, hidden neurons
		// and output neurons
		FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(2, new int[] { 3 }, 1);
		// Initialize the BackPropagation algorithm
		BackPropagation bp = new BackPropagation(net);
		// Specify the pattern set that's going to be used for training
		bp.pattern_set = pattern_set;
		// Specify the learning method (Memorize,Generalize)
		bp.learning_strategy = LearningStrategy.Memorize;
		// Specify the desired error (the lower the error, the more accurate the
		// results)
		bp.desired_error = 0.001;
		// Specify the maximum number of epochs the training may take
		bp.maximum_epochs = 1000000000;
		// Attach a ConsoleWatcher to be able to monitor the training in the
		// console
		bp.watcher = new ConsoleWatcher(bp);
		// Since we are going to want to wait for the end of the training
		// thread,
		// we have to specify in advance that the training is undergoing,
		// otherwise,
		// the testing would take place before the start of the training
		bp.is_running = true;
		// Start the training
		bp.start();
		// Wait for the training to finish (sleeping for 1 second every pass to
		// save CPU time)
		while (bp.is_running)
			Thread.sleep(1000);
		// Test the network's accuracy
		new ConsoleTester(bp.network).test(pattern_set);
	}
}
