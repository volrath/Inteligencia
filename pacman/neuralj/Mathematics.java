package neuralj;

import java.util.Random;

/**
 * Implements the reusable mathematical functions that the neural networks need
 */
public class Mathematics
{
	/**
	 * Applies the exponential function to the input, returning a value in [0,
	 * inf]
	 * 
	 * @param num
	 *            The function's input
	 * @return Returns the function's output
	 */
	public static double exponential(double num)
	{
		return Math.exp(num);
	}

	/**
	 * Performs the identity function =P
	 * 
	 * @param num
	 *            The function's input
	 * @return Returns the function's output
	 */
	public static double identity(double num)
	{
		return num;
	}

	/**
	 * Applies the ramp function to the input, returning a value in [-1,1]
	 * 
	 * @param num
	 *            The function's input
	 * @return Returns the function's output
	 */
	public static double ramp(double num)
	{
		if (num < -1.0)
			return -1.0;
		else if (num > -1.0 && num < 1.0)
			return num;
		else
			return 1.0;
	}

	/**
	 * Returns a random value between -1 and 1
	 * 
	 * @return Returns a random value between -1 and 1
	 */
	public static double rand()
	{
		Random random = new Random();
		return random.nextDouble() * 2 - 1.0;
	}

	/**
	 * Applies the sigmoid function to a value, returning a value in [0,1]
	 * 
	 * @param num
	 *            The function's input
	 * @return Returns the function's output
	 */
	public static double sigmoid(double num)
	{
		double sigma;
		sigma = (1 / (1 + Math.exp(-(num))));
		return sigma;
	}

	/**
	 * Applies the step function to the input, returning a value in [0,1]
	 * 
	 * @param num
	 *            The function's input
	 * @return Returns the function's output
	 */
	public static double step(double num)
	{
		if (num < 0.0)
			return 0.0;
		return 1.0;
	}
}
