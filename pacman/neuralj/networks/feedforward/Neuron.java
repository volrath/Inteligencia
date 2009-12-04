package neuralj.networks.feedforward;

import java.io.Serializable;
import java.util.Vector;
import neuralj.Mathematics;
import neuralj.conditioncheckers.ConsoleConditionChecker;
import neuralj.networks.feedforward.activation.IActivationFunction;

/**
 * Models a neuron
 */
public class Neuron implements Serializable
{
	private static final long		serialVersionUID	= 6333836046579084070L;

	/**
	 * The default type of neuron
	 */
	public static final NeuronType	DEFAULT_NEURON_TYPE	= NeuronType.Normal;

	/**
	 * Represents the type of neuron
	 */
	public enum NeuronType
	{
		Normal, Bias
	}

	/**
	 * The neuron's activation function
	 */
	public IActivationFunction		activation_function;

	/**
	 * The neuron's incoming synapses
	 */
	public Vector<Synapse>			incoming_synapses	= new Vector<Synapse>();

	/**
	 * The neuron's outgoing synapses
	 */
	public Vector<Synapse>			outgoing_synapses	= new Vector<Synapse>();

	/**
	 * The neuron's input
	 */
	public double					input				= 0;

	/**
	 * The neuron's output
	 */
	public double					output				= 0;

	/**
	 * The neuron's delta
	 */
	public double					delta				= 0;

	/**
	 * The type of neuron (NORMAL,BIAS)
	 */
	public NeuronType				neuron_type			= DEFAULT_NEURON_TYPE;

	private ConsoleConditionChecker	checker				= null;

	/**
	 * Neuron constructor
	 * 
	 * @param activation_function
	 *            The activation function.
	 */
	public Neuron(IActivationFunction function)
	{
		this.checker = new ConsoleConditionChecker("Neuron");
		this.checker.init("Neuron");
		this.checker.addCheck(function != null, "The specified activation function is null.");
		if (this.checker.isSecure())
			this.activation_function = function;
	}

	/**
	 * Input the value into the derivative of the activation function and return
	 * the result
	 * 
	 * @param value
	 *            Value to enter into the derivative of the activation function
	 * @return Returns the output of the activation function's derivative after
	 *         inputing the given value
	 */
	public double calculateDerivative(double value)
	{
		return this.activation_function.calculateDerivate(value);
	}

	/**
	 * Calculates the neuron's output by activating the current input
	 */
	public void calculateOutput()
	{
		this.output = this.activation_function.calculate(this.input);
	}

	/**
	 * Gets a specific synapse attached to the neuron
	 * 
	 * @param i
	 *            Index of the synapse
	 * @return Returns the synapse, null if it doesn't exist
	 */
	public Synapse getOutgoingSynapse(int i)
	{
		this.checker.init("getOutgoingSynapse");
		this.checker.addCheck(i < this.outgoing_synapses.size() && i >= 0, "The outgoing synapse doesn't exist.");
		if (this.checker.isSecure())
			return this.outgoing_synapses.get(i);
		return null;
	}

	/**
	 * Resets the neuron's values
	 */
	public void resetValues()
	{
		this.input = 0;
		this.output = 0;
		this.delta = 0;
	}

	/**
	 * Resets the neuron's weights
	 */
	public void resetWeights()
	{
		for (int x = 0; x < this.outgoing_synapses.size(); x++)
			getOutgoingSynapse(x).weight = Mathematics.rand();
	}
}
