package neuralj.networks.feedforward.learning.genetic.selection;

import java.util.Random;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class SelectionRouletteWheel implements ISelectionOperator
{
	/**
	 * Gets a random member from the roulette wheel (with respect to their slice
	 * sizes)
	 */
	public FeedForwardNeuralNetwork select(RouletteWheel population)
	{
		Random gen = new Random();
		double sum = population.getTotalFitness();
		double ball = gen.nextDouble() * sum;
		double acum = 0;
		for (int x = 0; x < population.roulette_slices.size(); x++)
		{
			acum += population.roulette_slices.get(x).fitness;
			if (acum >= ball)
				return population.roulette_slices.get(x).member;
		}
		return population.roulette_slices.get(population.roulette_slices.size() - 1).member;
	}
}
