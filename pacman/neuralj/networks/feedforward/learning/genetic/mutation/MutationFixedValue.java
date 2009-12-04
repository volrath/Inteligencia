package neuralj.networks.feedforward.learning.genetic.mutation;

import java.util.Random;
import java.util.Vector;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class MutationFixedValue implements IMutationOperator
{
	private final static double	DEFAULT_FIXED_VALUE	= 1.0;

	// The fixed value to add in each mutation
	private double				fixed_value;

	public MutationFixedValue()
	{
		this.fixed_value = DEFAULT_FIXED_VALUE;
	}

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
				chromosome.set(x, new Double(chromosome.get(x).doubleValue() + this.fixed_value));
			}
		}
		net.setWeightVector(chromosome);
		return net;
	}

	/**
	 * Sets the fixed value to use in the mutation operator
	 * 
	 * @param p_fixed_value
	 *            The double to be added to a gene
	 */
	public void setFixedValue(double p_fixed_value)
	{
		this.fixed_value = p_fixed_value;
	}

	/**
	 * Gets the current fixed value used by the mutation operator
	 * 
	 * @return Returns the current fixed value used by the mutation operator
	 */
	public double getFixedValue()
	{
		return this.fixed_value;
	}
}
