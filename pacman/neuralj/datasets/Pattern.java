package neuralj.datasets;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import neuralj.conditioncheckers.ConsoleConditionChecker;
import neuralj.conditioncheckers.IConditionChecker;

/**
 * Represents a pattern (an association of value sequences)
 */
public class Pattern implements Serializable
{
	private static final long	serialVersionUID	= 8732033859984432273L;

	/**
	 * The token with which the values in a training file are split by
	 */
	public static String		split_token			= ";";

	/**
	 * The pattern's input
	 */
	public Vector<Double>		input				= null;

	/**
	 * The pattern's output
	 */
	public Vector<Double>		output				= null;

	private IConditionChecker	checker				= null;

	/**
	 * Pattern constructor for unsupervised patterns
	 * 
	 * @param first
	 *            Input vector
	 */
	public Pattern(double[] first)
	{
		this.checker = new ConsoleConditionChecker("Pattern");
		this.checker.init("Pattern");
		this.checker.addCheck(first.length > 0, "The given input values to initialize the patterns with were an empty list.");
		if (this.checker.isSecure())
		{
			this.input = new Vector<Double>();
			this.output = new Vector<Double>();
			for (int x = 0; x < first.length; x++)
				this.input.add(new Double(first[x]));
		}
	}

	/**
	 * Pattern constructor for supervised patterns
	 * 
	 * @param first
	 *            Input vector
	 * @param second
	 *            Output vector
	 */
	public Pattern(double[] first, double[] second)
	{
		this.checker = new ConsoleConditionChecker("Pattern");
		this.checker.init("Pattern");
		this.checker.addCheck(first.length > 0, "The given input values to initialize the patterns with were an empty list.");
		this.checker.addCheck(second.length > 0, "The given output values to initialize the patterns with were an empty list.");
		if (this.checker.isSecure())
		{
			this.input = new Vector<Double>();
			this.output = new Vector<Double>();
			for (int x = 0; x < first.length; x++)
				this.input.add(new Double(first[x]));
			for (int x = 0; x < second.length; x++)
				this.output.add(new Double(second[x]));
		}
	}

	/**
	 * Pattern constructor for supervised patterns
	 * 
	 * @param first
	 *            List of input values separated by the split token
	 * @param second
	 *            List of output values separated by the split token
	 */
	public Pattern(String first, String second)
	{
		this.checker = new ConsoleConditionChecker("Pattern");
		this.checker.init("Pattern");
		this.checker.addCheck(!first.equals(""), "The given input values to initialize the patterns with were an empty list.");
		this.checker.addCheck(!second.equals(""), "The given output values to initialize the patterns with were an empty list.");
		if (this.checker.isSecure())
		{
			this.input = parsePatternString(first);
			this.output = parsePatternString(second);
		}
	}

	/**
	 * Pattern constructor for supervised patterns
	 * 
	 * @param first
	 *            Input vector
	 * @param second
	 *            Output vector
	 */
	public Pattern(Vector<Double> p_input, Vector<Double> p_output)
	{
		this.checker = new ConsoleConditionChecker("Pattern");
		this.checker.init("Pattern");
		this.checker.addCheck(p_input.size() > 0, "The given input values to initialize the patterns with were an empty list.");
		this.checker.addCheck(p_output.size() > 0, "The given output values to initialize the patterns with were an empty list.");
		if (this.checker.isSecure())
		{
			this.input = p_input;
			this.output = p_output;
		}
	}

	/**
	 * Parses a pattern from a string and returns the corresponding vector
	 * 
	 * @param pattern_string
	 *            The list of values separated by the split token
	 * @return A vector with the pattern values, null if error
	 */
	private Vector<Double> parsePatternString(String pattern_string)
	{
		this.checker.init("parsePatternString");
		this.checker.addCheck(pattern_string.length() > 0, "The given pattern string to pass for pattern values was an empty list.");
		if (this.checker.isSecure())
		{
			Vector<Double> list = new Vector<Double>();
			StringTokenizer tokenizer = new StringTokenizer(pattern_string, Pattern.split_token);
			while (tokenizer.hasMoreTokens())
				list.add(new Double(Double.parseDouble(tokenizer.nextToken())));
			return list;
		}
		return null;
	}
}
