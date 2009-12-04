package neuralj.networks.feedforward.learning.genetic.selection;

import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public interface ISelectionOperator
{
	public FeedForwardNeuralNetwork select(RouletteWheel population);
}
