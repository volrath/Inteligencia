package neuralj.networks.feedforward.activation;

import java.io.Serializable;

/**
 * The linear activation function always returns the inputed value
 */
public class ActivationFunctionLinear implements IActivationFunction,
		Serializable {

	private static final long serialVersionUID = 7348291853232802355L;

	/**
	 * Returns the activation function's output
	 * @param value The input value for the activation function
	 * @return Returns the same value that was passed as a parameter
	 */
	public double calculate(double value) {
		return value;
	}

	/**
	 * Returns the activation function's derivative's output
	 * @param value This value is unused since the output of the derivative of a linear function is always the same
	 * @return Returns 1.0
	 */
	public double calculateDerivate(double value) {
		value++; // FIXME Replace with appropriate SuppressWarning
		return 1;
	}
}