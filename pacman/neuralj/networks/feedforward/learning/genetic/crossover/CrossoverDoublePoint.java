package neuralj.networks.feedforward.learning.genetic.crossover;

import java.util.Random;
import java.util.Vector;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class CrossoverDoublePoint implements ICrossoverOperator
{
	public FeedForwardNeuralNetwork cross(FeedForwardNeuralNetwork net1, FeedForwardNeuralNetwork net2)
	{
		Vector<Double> new_chromosome = new Vector<Double>();
		Vector<Double> chromosome1 = net1.getWeightVector();
		Vector<Double> chromosome2 = net2.getWeightVector();
		// Create the new chromosome
		Random gen = new Random();
		int split_point1 = gen.nextInt(chromosome1.size());
		int split_point2 = gen.nextInt(chromosome1.size() - split_point1) + split_point1;
		for (int x = 0; x < split_point1; x++)
			new_chromosome.add(chromosome1.get(x));
		for (int x = split_point1; x < split_point2; x++)
			new_chromosome.add(chromosome2.get(x));
		for (int x = split_point2; x < chromosome2.size(); x++)
			new_chromosome.add(chromosome1.get(x));
		// Create the new network
		FeedForwardNeuralNetwork new_net = new FeedForwardNeuralNetwork(net1);
		new_net.setWeightVector(new_chromosome);
		return new_net;
	}
}
