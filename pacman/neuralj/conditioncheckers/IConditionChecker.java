package neuralj.conditioncheckers;

/**
 * Interface for code condition checkers
 */
public interface IConditionChecker
{
	/**
	 * Indicates if it's secure to continue (it's secure when all checks pass)
	 */
	boolean isSecure();

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
	void addCheck(boolean condition, String message);

	/**
	 * Initializes the condition checker
	 * 
	 * @param p_function_name
	 *            The name of the function that's going to be monitored
	 */
	void init(String p_function_name);
}
