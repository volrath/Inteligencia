package neuralj.networks.feedforward.learning.genetic.crossover;

import java.util.Random;
import java.util.Vector;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class CrossoverUniform implements ICrossoverOperator
{
	public FeedForwardNeuralNetwork cross(FeedForwardNeuralNetwork net1, FeedForwardNeuralNetwork net2)
	{
		Vector<Double> new_chromosome = new Vector<Double>();
		Vector<Double> chromosome1 = net1.getWeightVector();
		Vector<Double> chromosome2 = net2.getWeightVector();
		// Create the new chromosome
		Random gen = new Random();
		for (int x = 0; x < chromosome1.size(); x++)
		{
			if (gen.nextBoolean())
				new_chromosome.add(chromosome1.get(x));
			else
				new_chromosome.add(chromosome2.get(x));
		}
		// Create the new network
		FeedForwardNeuralNetwork new_net = new FeedForwardNeuralNetwork(net1);
		new_net.setWeightVector(new_chromosome);
		return new_net;
	}
}
