package neuralj.networks.feedforward.learning.genetic.mutation;

import java.util.Random;
import java.util.Vector;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class MutationOrderChanging implements IMutationOperator
{
	public FeedForwardNeuralNetwork mutate(FeedForwardNeuralNetwork net)
	{
		Random random = new Random();
		Vector<Double> chromosome = new Vector<Double>();
		chromosome = net.getWeightVector();
		for (int x = 0; x < chromosome.size(); x++)
		{
			double value = (double) random.nextInt(100) / 100;
			if (value <= mutation_rate)
			{
				int position = random.nextInt(chromosome.size());
				Double temp = chromosome.get(x);
				chromosome.set(x, chromosome.get(position));
				chromosome.set(position, temp);
			}
		}
		net.setWeightVector(chromosome);
		return net;
	}
}
