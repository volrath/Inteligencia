package neuralj.conditioncheckers;

import java.io.Serializable;
import java.util.Vector;
import neuralj.GlobalConfiguration;

/**
 * Condition checker class that writes an error to the console when one occurs
 */
public class ConsoleConditionChecker implements IConditionChecker, Serializable
{
	private static final long	serialVersionUID	= -1114272575130123356L;

	private class BoolStringPair
	{
		public boolean	condition	= false;

		public String	message		= "";

		public BoolStringPair(boolean cond, String msg)
		{
			this.condition = cond;
			this.message = msg;
		}
	}

	private Vector<BoolStringPair>	conditions		= new Vector<BoolStringPair>();

	private String					class_name		= "";

	private String					function_name	= "";

	/**
	 * Constructs the condition checker
	 * 
	 * @param p_class_name
	 *            The name of the class the checker will monitor
	 */
	public ConsoleConditionChecker(String p_class_name)
	{
		this.class_name = p_class_name;
	}

	/**
	 * Initializes the condition checker
	 * 
	 * @param p_function_name
	 *            The name of the function that's going to be monitored
	 */
	public void init(String p_function_name)
	{
		this.function_name = p_function_name;
	}

	/**
	 * Adds a check to the check list along with the error message to print when
	 * check fails
	 * 
	 * @param condition
	 *            The value of the boolean condition
	 * @param message
	 *            The associated error message to print when the condition is
	 *            false
	 */
	public void addCheck(boolean condition, String message)
	{
		if (GlobalConfiguration.SafeCheck)
			this.conditions.add(new BoolStringPair(condition, message));
	}

	/**
	 * Indicates if it's secure to continue (it's secure when all checks pass)
	 */
	public boolean isSecure()
	{
		if (GlobalConfiguration.SafeCheck)
		{
			boolean secure = true;
			for (BoolStringPair pair : this.conditions)
				if (pair.condition == false)
				{
					secure = false;
					System.err.println("ERROR: " + this.class_name + " - " + this.function_name + " - " + pair.message);
				}
			return secure;
		}
		return true;
	}
}
