package neuralj.networks.feedforward;

import java.io.Serializable;
import java.util.Vector;
import neuralj.conditioncheckers.ConsoleConditionChecker;
import neuralj.networks.feedforward.activation.ActivationFunctionConstant;
import neuralj.networks.feedforward.activation.IActivationFunction;

/**
 * Represents a layer of neurons
 */
public class NeuronLayer implements Serializable
{
	private static final long		serialVersionUID	= 7854396644414031259L;

	/**
	 * The layer's list of neurons
	 */
	public Vector<Neuron>			neurons;

	private ConsoleConditionChecker	checker				= null;

	/**
	 * Layer constructor
	 * 
	 * @param nr_neurons
	 *            Number of neurons that the layer will contain
	 * @param activation_function
	 *            The activation function that each neuron will contain
	 */
	public NeuronLayer(int nr_neurons, IActivationFunction activation_function)
	{
		this.checker = new ConsoleConditionChecker("NeuronLayer");
		this.checker.init("NeuronLayer");
		this.checker.addCheck(nr_neurons > 0, "The number of neurons specified for the layer is smaller than 0.");
		this.checker.addCheck(activation_function != null, "The activation function is null.");
		if (this.checker.isSecure())
		{
			this.neurons = new Vector<Neuron>();
			for (int x = 0; x < nr_neurons; x++)
			{
				Neuron neuron = new Neuron(activation_function);
				neuron.neuron_type = Neuron.NeuronType.Normal;
				this.neurons.add(neuron);
			}
		}
	}

	/**
	 * Layer constructor
	 * 
	 * @param nr_neurons
	 *            Number of neurons that the layer will contain.
	 * 
	 * @param nr_bias
	 *            The number of bias nodes.
	 */
	public NeuronLayer(int nr_neurons, IActivationFunction activation_function, int nr_bias)
	{
		this(nr_neurons, activation_function);
		for (int x = 0; x < nr_bias; x++)
		{
			Neuron bias_neuron = new Neuron(new ActivationFunctionConstant());
			bias_neuron.neuron_type = Neuron.NeuronType.Bias;
			bias_neuron.input = 0;
			this.neurons.add(bias_neuron);
		}
	}

	/**
	 * Calculates the neurons' outputs by passing their inputs by the activation
	 * function
	 * 
	 * @return Boolean indicating if the operation was successful
	 */
	public boolean calculateOutput()
	{
		this.checker.init("calculateOutput");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			for (Neuron neuron : this.neurons)
				neuron.calculateOutput();
			return true;
		}
		return false;
	}

	/**
	 * Forwards the values to the next layer
	 * 
	 * @return Boolean indicating if the operation succeeded
	 */
	public boolean feedForward()
	{
		this.checker.init("feedForward");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			for (Neuron neuron : this.neurons)
			{
				for (Synapse synapse : neuron.outgoing_synapses)
				{
					Neuron destination_neuron = synapse.destination_neuron;
					double destination_input = destination_neuron.input;
					synapse.value = neuron.output;
					destination_input += synapse.value * synapse.weight;
					destination_neuron.input = destination_input;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns the neuron layer's input
	 * 
	 * @return Returns a vector with all the neuron's inputs, or null in case of
	 *         error
	 */
	public Vector<Double> getInput()
	{
		this.checker.init("getInput");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			Vector<Double> input = new Vector<Double>();
			for (Neuron neuron : this.neurons)
				input.add(new Double(neuron.input));
			return input;
		}
		return null;
	}

	/**
	 * Returns the specified neuron
	 * 
	 * @param i
	 *            Index of the neuron
	 * @return Returns the neuron, returns null in case it doesn't exist
	 */
	public Neuron getNeuron(int i)
	{
		this.checker.init("getNeuron");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		this.checker.addCheck(i >= 0 && i < this.neurons.size(), "The neuron doesn't exist.");
		if (this.checker.isSecure())
			return this.neurons.get(i);
		return null;
	}

	/**
	 * Returns the number of neurons of a certain type
	 * 
	 * @param neuron_type
	 *            The type of neuron (use constants in class Neuron)
	 * @return The number of neurons of the type specified that this neuron
	 *         layer contains, or -1 in case of error
	 */
	public int getNumberNeurons(Neuron.NeuronType neuron_type)
	{
		this.checker.init("getNumberNeurons");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			int acum = 0;
			for (Neuron neuron : this.neurons)
				if (neuron.neuron_type == neuron_type)
					acum++;
			return acum;
		}
		return -1;
	}

	/**
	 * Returns the neuron layer's output
	 * 
	 * @return Returns a vector with all the neuron's outputs, or null in case
	 *         of error
	 */
	public Vector<Double> getOutput()
	{
		this.checker.init("getOutput");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			Vector<Double> output = new Vector<Double>();
			for (Neuron neuron : this.neurons)
				output.add(new Double(neuron.output));
			return output;
		}
		return null;
	}

	/**
	 * Resets the layer's neuron's values
	 * 
	 * @return Boolean indicating if the operation succeeded
	 */
	public boolean resetValues()
	{
		this.checker.init("resetValues");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			for (int x = 0; x < this.neurons.size(); x++)
				getNeuron(x).resetValues();
			return true;
		}
		return false;
	}

	/**
	 * Resets the layer's neuron's weights
	 * 
	 * @return Boolean indicating if the operation succeeded
	 */
	public boolean resetWeights()
	{
		this.checker.init("resetWeights");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		if (this.checker.isSecure())
		{
			for (int x = 0; x < this.neurons.size(); x++)
				getNeuron(x).resetWeights();
			return true;
		}
		return false;
	}

	/**
	 * Sets the activation function for the whole neuron layer
	 * 
	 * @param function
	 *            The activation function to include in the neuron layer's
	 *            neurons
	 * @return Boolean indicating if the operation succeeded
	 */
	public boolean setActivationFunction(IActivationFunction function)
	{
		this.checker.init("setActivationFunction");
		this.checker.addCheck(this.neurons != null, "The neuron list is null.");
		this.checker.addCheck(function != null, "The activation function is null.");
		if (this.checker.isSecure())
		{
			for (Neuron neuron : this.neurons)
			{
				if (neuron.neuron_type == Neuron.NeuronType.Normal)
					neuron.activation_function = function;
			}
			return true;
		}
		return false;
	}
}
