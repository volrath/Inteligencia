package neuralj.networks.feedforward.learning.genetic.selection;

import java.util.Random;
import java.util.Vector;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class SelectionTournament implements ISelectionOperator
{
	private static final int	DEFAULT_TOURNAMENT_SIZE	= 2;

	private int					tournament_size;

	public SelectionTournament()
	{
		this.tournament_size = DEFAULT_TOURNAMENT_SIZE;
	}

	public FeedForwardNeuralNetwork select(RouletteWheel population)
	{
		Random gen = new Random();
		Vector<FeedForwardNeuralNetwork> vector = new Vector<FeedForwardNeuralNetwork>();
		for (int x = 0; x < this.tournament_size; x++)
		{
			int position = gen.nextInt(population.roulette_slices.size());
			vector.add(population.roulette_slices.get(position).member);
		}
		return vector.get(gen.nextInt(this.tournament_size));
	}
}
