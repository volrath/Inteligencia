package neuralj.networks.feedforward.learning.genetic.mutation;

import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public interface IMutationOperator
{
	// FIXME Figure out how to change the mutation rate
	public double	mutation_rate	= 0.01;

	public FeedForwardNeuralNetwork mutate(FeedForwardNeuralNetwork net);
}
