package neuralj.networks.feedforward.activation;

import java.io.Serializable;

import neuralj.Mathematics;

/**
 * Implements the sigmoid activation function
 */
public class ActivationFunctionSigmoid implements IActivationFunction,
		Serializable {
	private static final long serialVersionUID = -4116870092849352481L;

	/**
	 * Returns the sigmoid's output for the given value
	 * 
	 * @param value
	 *            The input for the sigmoid function
	 * @return Returns the sigmoid's output
	 */
	public double calculate(double value) {
		return Mathematics.sigmoid(value);
	}

	/**
	 * Returns the sigmoid's derivative's output for the given value
	 * 
	 * @param value
	 *            The input for the derivated sigmoid function
	 * @return Returns the derivated sigmoid's output
	 */
	public double calculateDerivate(double value) {
		return calculate(value) * (1 - calculate(value));
	}
}