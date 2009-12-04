package neuralj.networks.feedforward.learning.genetic.selection;

import java.util.Random;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class SelectionRank implements ISelectionOperator
{
	public FeedForwardNeuralNetwork select(RouletteWheel population)
	{
		Random gen = new Random();
		int nr_ranks = population.roulette_slices.size();
		int total_ranks = 0;
		for (int x = 0; x <= nr_ranks; x++)
			total_ranks += x;
		int ball = gen.nextInt(total_ranks);
		int acum = 0;
		for (int x = 0; x < population.roulette_slices.size(); x++)
		{
			acum += nr_ranks;
			if (acum >= ball)
				return population.roulette_slices.get(x).member;
			nr_ranks--;
		}
		return population.roulette_slices.get(population.roulette_slices.size() - 1).member;
	}
}
