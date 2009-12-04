package neuralj;

import java.io.Serializable;
import java.util.Random;

/**
 * Stores an interval and allows to make operations on it
 */
public class IntervalScaler implements Serializable
{
	private static final long	serialVersionUID	= 2883709540907020016L;

	/**
	 * The maximum value in the specified interval
	 */
	public double				maximum;

	/**
	 * The minimum value in the specified interval
	 */
	public double				minimum;

	/**
	 * Interval constructor
	 * 
	 * @param p_minimum
	 *            Minimum of the interval
	 * @param p_maximum
	 *            Maximum of the interval
	 */
	public IntervalScaler(double p_minimum, double p_maximum)
	{
		this.minimum = p_minimum;
		this.maximum = p_maximum;
	}

	/**
	 * Returns a value between minimum and maximum
	 * 
	 * @return A value between minimum and maximum
	 */
	public int random()
	{
		Random generator = new Random();
		// get the range, casting to long to avoid overflow problems
		long range = (long) this.maximum - (long) this.minimum + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * generator.nextDouble());
		return (int) (fraction + this.minimum);
	}

	/**
	 * Shrinks the value to one between 0 and 1
	 * 
	 * @param value
	 *            The value to be shrinked
	 * @return A value between 0 and 1
	 */
	public double shrink(double value)
	{
		return value / this.maximum;
	}

	/**
	 * Unshrinks a value from one between 0 and 1 to one between minimum and
	 * maximum
	 * 
	 * @param value
	 *            The value to be expanded
	 * @return A value between minimum and maximum
	 */
	public double unshrink(double value)
	{
		return value * this.maximum;
	}
}
