package neuralj.networks.feedforward.learning.genetic.mutation;

import java.util.Random;
import java.util.Vector;
import neuralj.Mathematics;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class MutationRandom implements IMutationOperator
{
	/**
	 * Mutates the neural network
	 * 
	 * @param net
	 *            The neural network that's going to be mutated
	 * @return The mutated neural network
	 */
	public FeedForwardNeuralNetwork mutate(FeedForwardNeuralNetwork net)
	{
		Vector<Double> chromosome = net.getWeightVector();
		Random gen = new Random();
		for (int x = 0; x < chromosome.size(); x++)
		{
			double value = (double) gen.nextInt(100) / 100;
			if (value <= mutation_rate)
				chromosome.set(x, new Double(Mathematics.rand() * 100));
		}
		net.setWeightVector(chromosome);
		return net;
	}
}
