package neuralj.datasets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;
import neuralj.IntervalScaler;
import neuralj.conditioncheckers.ConsoleConditionChecker;
import neuralj.conditioncheckers.IConditionChecker;

/**
 * Represents a set of patterns (for example, for a xor patternset, this set
 * would contain all the entries of the xor table)
 */
public class PatternSet implements Serializable
{
	private static final long	serialVersionUID					= -5553108765660688521L;

	/**
	 * The default percentage of patterns that belongs to the training set
	 */
	private static final double	DEFAULT_PERCENTAGE_TRAINING_SET		= 0.60;

	/**
	 * The default percentage of patterns that belongs to the validation set
	 */
	private static final double	DEFAULT_PERCENTAGE_VALIDATION_SET	= 0.30;

	/**
	 * The default percentage of patterns that belongs to the test set
	 */
	private static final double	DEFAULT_PERCENTAGE_TEST_SET			= 0.10;

	/**
	 * Scales the pattern set's input
	 */
	public IntervalScaler		input_interval						= new IntervalScaler(0, 1);

	/**
	 * Scales the pattern set's output
	 */
	public IntervalScaler		output_interval						= new IntervalScaler(0, 1);

	/**
	 * List of patterns that make the set
	 */
	public Vector<Pattern>		patterns							= new Vector<Pattern>();

	/**
	 * List of patterns to test at the end of training
	 */
	public Vector<Pattern>		test_set							= new Vector<Pattern>();

	/**
	 * List of patterns to be used in training
	 */
	public Vector<Pattern>		training_set						= new Vector<Pattern>();

	/**
	 * List of patterns to validate during training
	 */
	public Vector<Pattern>		validation_set						= new Vector<Pattern>();

	/**
	 * The percentage of patterns that belongs to the training set
	 */
	private double				percentage_training_set				= DEFAULT_PERCENTAGE_TRAINING_SET;

	/**
	 * The percentage of patterns that belongs to the validation set
	 */
	private double				percentage_validation_set			= DEFAULT_PERCENTAGE_VALIDATION_SET;

	/**
	 * The percentage of patterns that belongs to the test set
	 */
	private double				percentage_test_set					= DEFAULT_PERCENTAGE_TEST_SET;

	private IConditionChecker	checker								= null;

	/**
	 * Represents the kinds of pattern lists available
	 */
	public enum PatternType
	{
		All, // All patterns
		Training, // Patterns used in the training phase of learning
		Validation, // Patterns used in the validation phase of learning
		Test
		// Patterns used in the test phase of learning
	}

	public PatternSet()
	{
		this.checker = new ConsoleConditionChecker("PatternSet");
	}

	/**
	 * Adds a pattern to the pattern set
	 * 
	 * @param pattern
	 *            The pattern to be added
	 * @return Boolean value indicating if the operation was successful
	 */
	public boolean addPattern(Pattern pattern)
	{
		this.checker.init("addPattern");
		this.checker.addCheck(pattern.input != null, "The given input values in the given pattern were in fact a null list.");
		this.checker.addCheck(pattern.output != null, "The given output values in the given pattern were in fact a null list.");
		this.checker.addCheck(pattern.input.size() > 0, "The given input values to initialize the patterns with were an empty list.");
		this.checker.addCheck(pattern.output.size() > 0, "The given output values to initialize the patterns with were an empty list.");
		if (this.checker.isSecure())
		{
			this.patterns.add(pattern);
			setPatternSetRanges();
			return true;
		}
		return false;
	}

	/**
	 * Generates the training, validation and test sets from the list of
	 * patterns by splitting them randomly by a given ratio
	 * 
	 * @return Boolean indicating if the operation was successful
	 */
	@SuppressWarnings("unchecked")
	public boolean generateSets()
	{
		this.checker.init("generateSets");
		this.checker.addCheck(this.patterns != null, "The pattern list is a null pointer.");
		this.checker.addCheck(this.patterns.size() > 0, "The pattern list is empty.");
		if (this.checker.isSecure())
		{
			int pattern_set_size = this.patterns.size();
			int nr_training = (int) (this.percentage_training_set * pattern_set_size);
			int nr_validation = (int) (this.percentage_validation_set * pattern_set_size);
			int nr_test = (int) (this.percentage_test_set * pattern_set_size);
			int position = 0;
			shufflePatterns();
			while (nr_training != 0)
			{
				this.training_set.add(this.patterns.get(position++));
				nr_training--;
			}
			while (nr_validation != 0)
			{
				this.validation_set.add(this.patterns.get(position++));
				nr_validation--;
			}
			while (nr_test != 0)
			{
				this.test_set.add(this.patterns.get(position++));
				nr_test--;
			}
			return true;
		}
		return false;
	}

	/**
	 * Shuffles the stored patterns into another random order
	 * 
	 * @return Boolean indicating if the operation was successful
	 */
	public boolean shufflePatterns()
	{
		this.checker.init("shufflePatterns");
		this.checker.addCheck(this.patterns != null, "The pattern list is a null pointer.");
		this.checker.addCheck(this.patterns.size() > 0, "The pattern list is empty.");
		if (this.checker.isSecure())
		{
			Random r = new Random();
			int j = 0;
			for (int i = 0; i < this.patterns.size(); i++)
			{
				j = r.nextInt(i + 1);
				Pattern temp = this.patterns.get(i);
				this.patterns.set(i, this.patterns.get(j));
				this.patterns.set(j, temp);
			}
			return true;
		}
		return false;
	}

	/**
	 * Loads a pattern set from a file
	 * 
	 * @param path
	 *            Path to the pattern file
	 * @param input_size
	 *            Number of inputs per line
	 * @return Number of parsed patterns, -1 in case of error
	 */
	public int loadPatterns(String path, int input_size)
	{
		this.checker.init("loadPatterns");
		this.checker.addCheck(!path.equals(""), "The given path is empty.");
		this.checker.addCheck(input_size > 0, "The specified number of input values per line is smaller than 0.");
		if (this.checker.isSecure())
		{
			try
			{
				BufferedReader leitor = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
				String line;
				while ((line = leitor.readLine()) != null)
				{
					int input = 0;
					int x = 0;
					for (x = 0; x < line.length() && input < input_size; x++)
						if (new String("" + line.charAt(x)).equals(Pattern.split_token))
							input++;
					if (input == input_size)
					{
						String input_string = line.substring(0, x - 1);
						String output_string = line.substring(x);
						this.patterns.add(new Pattern(input_string, output_string));
					}
				}
				setPatternSetRanges();
				return this.patterns.size();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				System.err.println("ERROR: File not found in " + path);
				return -1;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.err.println("ERROR: Error reading file in " + path);
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Discovers how to compress the values to an usable range by discovering
	 * the minimum and maximum input and output values in the pattern set
	 * 
	 * @return Boolean indicating if the operation was successful
	 */
	private boolean setPatternSetRanges()
	{
		this.checker.init("setPatternSetRanges");
		this.checker.addCheck(this.patterns != null, "The pattern list is a null pointer.");
		this.checker.addCheck(this.patterns.size() > 0, "The pattern list is empty.");
		if (this.checker.isSecure())
		{
			double minimum_input = Double.MAX_VALUE;
			double maximum_input = Double.MIN_VALUE;
			double minimum_output = Double.MAX_VALUE;
			double maximum_output = Double.MIN_VALUE;
			for (Pattern pattern : this.patterns)
			{
				for (Double value : pattern.input)
				{
					minimum_input = value.doubleValue() < minimum_input ? value.doubleValue() : minimum_input;
					maximum_input = value.doubleValue() > maximum_input ? value.doubleValue() : maximum_input;
				}
				for (Double value : pattern.output)
				{
					minimum_output = value.doubleValue() < minimum_output ? value.doubleValue() : minimum_output;
					maximum_output = value.doubleValue() > maximum_output ? value.doubleValue() : maximum_output;
				}
			}
			this.input_interval = new IntervalScaler(minimum_input, maximum_input);
			this.output_interval = new IntervalScaler(maximum_input, maximum_output);
			return true;
		}
		return false;
	}

	/**
	 * Sets the list of patterns for the training set
	 * 
	 * @param p_patterns
	 *            List of patterns for the training set
	 * @return Boolean indicating if the operation was successful
	 */
	public boolean setPatterns(Vector<Pattern> p_patterns)
	{
		this.checker.init("setPatterns");
		this.checker.addCheck(p_patterns != null, "The given pattern list is a null pointer.");
		this.checker.addCheck(p_patterns.size() > 0, "The given pattern list is empty.");
		if (this.checker.isSecure())
		{
			this.patterns = p_patterns;
			setPatternSetRanges();
			return true;
		}
		return false;
	}

	/**
	 * Sets the test set and adds the new patterns to the full pattern list
	 * 
	 * @param p_test_set
	 *            The list of patterns that make the test set
	 * @return Boolean indicating if the operation was successful
	 */
	public boolean setTestSet(Vector<Pattern> p_test_set)
	{
		this.checker.init("setTestSet");
		this.checker.addCheck(p_test_set != null, "The given pattern list is a null pointer.");
		this.checker.addCheck(p_test_set.size() > 0, "The given pattern list is empty.");
		if (this.checker.isSecure())
		{
			this.test_set = p_test_set;
			for (Pattern pattern : this.test_set)
				if (!this.patterns.contains(pattern))
					this.patterns.add(pattern);
			for (Pattern pattern : this.patterns)
				if (!this.test_set.contains(pattern))
					this.patterns.remove(pattern);
			return true;
		}
		return false;
	}

	/**
	 * Sets the validation set and adds the new patterns to the full pattern
	 * list
	 * 
	 * @param p_validation_set
	 *            The list of patterns that make the validation set
	 * @return Boolean indicating if the operation was successful
	 */
	public void setValidationSet(Vector<Pattern> p_validation_set)
	{
		this.checker.init("setValidationSet");
		this.checker.addCheck(p_validation_set != null, "The given pattern list is a null pointer.");
		this.checker.addCheck(p_validation_set.size() > 0, "The given pattern list is empty.");
		if (this.checker.isSecure())
		{
			this.validation_set = p_validation_set;
			for (Pattern pattern : this.validation_set)
				if (!this.patterns.contains(pattern))
					this.patterns.add(pattern);
			for (Pattern pattern : this.patterns)
				if (!this.validation_set.contains(pattern))
					this.patterns.remove(pattern);
		}
	}

	/**
	 * Sets the training set and adds the new patterns to the full pattern list
	 * 
	 * @param p_training_set
	 *            The list of patterns that make the training set
	 * @return Boolean indicating if the operation was successful
	 */
	public boolean setTrainingSet(Vector<Pattern> p_training_set)
	{
		this.checker.init("setTrainingSet");
		this.checker.addCheck(p_training_set != null, "The given pattern list is a null pointer.");
		this.checker.addCheck(p_training_set.size() > 0, "The given pattern list is empty.");
		if (this.checker.isSecure())
		{
			this.training_set = p_training_set;
			for (Pattern pattern : this.training_set)
				if (!this.patterns.contains(pattern))
					this.patterns.add(pattern);
			for (Pattern pattern : this.patterns)
				if (!this.training_set.contains(pattern))
					this.patterns.remove(pattern);
			return true;
		}
		return false;
	}

	/**
	 * Shrinks the values contained in a pattern to the input and output ranges
	 * calculated for this pattern set
	 * 
	 * @param pattern
	 *            The pattern whose values are going to be shrunk
	 * @return The pattern given with it's values shrunk to be within the input
	 *         and output ranges, or null in case of error
	 */
	private Pattern shrinkPattern(Pattern pattern)
	{
		this.checker.init("shrinkPattern");
		this.checker.addCheck(pattern.input != null, "The given input values in the given pattern were in fact a null list.");
		this.checker.addCheck(pattern.output != null, "The given output values in the given pattern were in fact a null list.");
		this.checker.addCheck(pattern.input.size() > 0, "The given input values to initialize the patterns with were an empty list.");
		this.checker.addCheck(pattern.output.size() > 0, "The given output values to initialize the patterns with were an empty list.");
		if (this.checker.isSecure())
		{
			for (int x = 0; x < pattern.input.size(); x++)
				pattern.input.set(x, new Double(this.input_interval.shrink(pattern.input.get(x).doubleValue())));
			for (int x = 0; x < pattern.output.size(); x++)
				pattern.output.set(x, new Double(this.output_interval.shrink(pattern.output.get(x).doubleValue())));
			return pattern;
		}
		return null;
	}

	/**
	 * Returns a version of the pattern set compressed for the input and output
	 * ranges
	 * 
	 * @param type
	 *            The type of patterns
	 * @return The compressed version of the patterns
	 */
	public Vector<Pattern> getShrunkPatterns(PatternType type)
	{
		Vector<Pattern> shrunk = new Vector<Pattern>();
		Vector<Pattern> list = null;
		if (type == PatternType.All)
			list = this.patterns;
		else if (type == PatternType.Training)
			list = this.training_set;
		else if (type == PatternType.Validation)
			list = this.validation_set;
		else if (type == PatternType.Test)
			list = this.test_set;
		for (Pattern pattern : list)
			shrunk.add(shrinkPattern(pattern));
		return shrunk;
	}

	/**
	 * Sets the percentages for splitting the patterns into different sets (must
	 * amount to 1)
	 * 
	 * @param training_percentage
	 *            Percentage for the training set
	 * @param validation_percentage
	 *            Percentage for the validation set
	 * @param test_percentage
	 *            Percentage for the test set
	 */
	public boolean setSetPercentages(double training_percentage, double validation_percentage, double test_percentage)
	{
		this.checker.init("setSetPercentages");
		this.checker.addCheck(training_percentage + validation_percentage + test_percentage == 100.0, "The given percentages do not amount to a total of 100%");
		if (this.checker.isSecure())
		{
			this.percentage_training_set = training_percentage;
			this.percentage_validation_set = validation_percentage;
			this.percentage_test_set = test_percentage;
			return true;
		}
		return false;
	}

	/**
	 * Sets the minimum and maximum values for the scalers to be the same for
	 * both pattern sets
	 * 
	 * @param pattern_set
	 *            The second pattern set
	 * @return Boolean indicating if the operation was successful
	 */
	public boolean equalizeRanges(PatternSet pattern_set)
	{
		this.checker.init("equalizeRanges");
		this.checker.addCheck(pattern_set != null, "The given pattern set is in fact a null pointer.");
		if (this.checker.isSecure())
		{
			double minimum_input = pattern_set.input_interval.minimum;
			double maximum_input = pattern_set.input_interval.maximum;
			double minimum_output = pattern_set.output_interval.minimum;
			double maximum_output = pattern_set.output_interval.maximum;
			minimum_input = this.input_interval.minimum < minimum_input ? this.input_interval.minimum : minimum_input;
			maximum_input = this.input_interval.maximum > maximum_input ? this.input_interval.maximum : maximum_input;
			minimum_output = this.output_interval.minimum < minimum_output ? this.output_interval.minimum : minimum_output;
			maximum_output = this.output_interval.maximum > maximum_output ? this.output_interval.maximum : maximum_output;
			this.input_interval = new IntervalScaler(minimum_input, maximum_input);
			this.output_interval = new IntervalScaler(maximum_input, maximum_output);
			pattern_set.input_interval = new IntervalScaler(minimum_input, maximum_input);
			pattern_set.output_interval = new IntervalScaler(maximum_input, maximum_output);
			return true;
		}
		return false;
	}
}
