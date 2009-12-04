package neuralj.networks.feedforward.learning.genetic.selection;

import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

/**
 * Simulates a slice of the roulette wheel
 */
public class RouletteSlice implements Comparable
{
	// The fitness level of the member (so that the sizes can be
	// recomputed at any time)
	public double					fitness	= 0;

	// The population member
	public FeedForwardNeuralNetwork	member;

	public int compareTo(Object arg0)
	{
		if (((RouletteSlice) arg0).fitness < this.fitness)
			return -1;
		else if (this.fitness == ((RouletteSlice) arg0).fitness)
			return 0;
		else
			return 1;
	}
}
