package neuralj.networks.feedforward.activation;

import java.io.Serializable;

/**
 * The constant activation function always returns the same value
 */
public class ActivationFunctionConstant implements IActivationFunction,
		Serializable {

	private static final long serialVersionUID = 8734457924490529051L;

	/**
	 * Returns the activation function's result
	 * 
	 * @param value
	 *            This value is unused since the constant function always
	 *            returns the same value
	 * @return Returns 1.0
	 */
	public double calculate(double value) {
		value++; // FIXME Replace with appropriate SuppressWarning
		return 1;
	}

	/**
	 * Returns the activation function's derivative's result
	 * 
	 * @param value
	 *            This value is unused since the constant function always
	 *            returns the same value
	 * @return Returns 0.0
	 */
	public double calculateDerivate(double value) {
		value++; // FIXME Replace with appropriate SuppressWarning
		return 0;
	}
}
