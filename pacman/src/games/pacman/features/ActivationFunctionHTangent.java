package games.pacman.features;

import java.io.Serializable;

import neuralj.Mathematics;
import neuralj.networks.feedforward.activation.IActivationFunction;

/**
 * Implements the sigmoid activation function
 */
public class ActivationFunctionHTangent implements IActivationFunction,
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
		return Math.tanh(value);
	}

	/**
	 * Returns the sigmoid's derivative's output for the given value
	 *
	 * @param value
	 *            The input for the derivated sigmoid function
	 * @return Returns the derivated sigmoid's output
	 */
	public double calculateDerivate(double value) {
		return (1 - Math.pow(Math.tan(value),2));
	}
}