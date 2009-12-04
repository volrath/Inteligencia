package neuralj.networks.feedforward.learning.genetic.selection;

import java.util.Arrays;
import java.util.Vector;
import neuralj.datasets.Pattern;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.learning.bprop.BackPropagation;

/**
 * Simulates a roulette from which population members are selected in respect to
 * their fitness
 */
public class RouletteWheel
{
	// The roulette
	public Vector<RouletteSlice>	roulette_slices	= new Vector<RouletteSlice>();

	// The test set to evaluate the population's members
	public Vector<Pattern>			test_set		= new Vector<Pattern>();

	/**
	 * Adds a member to the roulette wheel
	 * 
	 * @param p_member
	 *            The population member to add to the wheel
	 */
	public void addMember(FeedForwardNeuralNetwork p_member)
	{
		RouletteSlice slice = new RouletteSlice();
		BackPropagation bp = new BackPropagation(p_member);
		slice.member = p_member;
		slice.fitness = 1.0 - bp.network.getPredictionError(this.test_set);
		this.roulette_slices.add(slice);
		Object[] list = this.roulette_slices.toArray();
		Arrays.sort(list);
		this.roulette_slices.clear();
		for (Object sliceobj : list)
			this.roulette_slices.add((RouletteSlice) sliceobj);
	}

	/**
	 * Returns the best fitness in the roulette wheel
	 * 
	 * @return Returns the best fitness in the roulette wheel
	 */
	public double getEliteFitness()
	{
		return this.roulette_slices.get(0).fitness;
	}

	/**
	 * Returns the best member in the roulette wheel
	 * 
	 * @return Returns the best member in the roulette wheel
	 */
	public FeedForwardNeuralNetwork getEliteMember()
	{
		return this.roulette_slices.get(0).member;
	}

	/**
	 * Gets the vector of population members contained in the roulette wheel
	 * 
	 * @return Vector of population member
	 */
	public Vector<FeedForwardNeuralNetwork> getPopulation()
	{
		Vector<FeedForwardNeuralNetwork> vec = new Vector<FeedForwardNeuralNetwork>();
		for (RouletteSlice slice : this.roulette_slices)
			vec.add(slice.member);
		return vec;
	}

	/**
	 * Returns the total fitness of the members present in the wheel
	 * 
	 * @return Returns the total fitness value
	 */
	public double getTotalFitness()
	{
		double sum = 0;
		for (RouletteSlice slice : this.roulette_slices)
			sum += slice.fitness;
		return sum;
	}

	/**
	 * Sets the new population in the roulette wheel (erases the previous one)
	 * 
	 * @param p_population
	 *            Vector with the networks that make the new population
	 */
	public void setPopulation(Vector<FeedForwardNeuralNetwork> p_population)
	{
		this.roulette_slices.clear();
		for (FeedForwardNeuralNetwork net : p_population)
			addMember(net);
	}
}
