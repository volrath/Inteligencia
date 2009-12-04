package neuralj.networks.feedforward.learning.genetic.crossover;

import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public interface ICrossoverOperator
{
	/**
	 * Crosses two chromosomes into a new one
	 * 
	 * @param net1
	 *            The neural network of the father
	 * @param net2
	 *            The neural network of the mother
	 * @return Returns a mixed chromosome
	 */
	public FeedForwardNeuralNetwork cross(FeedForwardNeuralNetwork net1, FeedForwardNeuralNetwork net2);
}
