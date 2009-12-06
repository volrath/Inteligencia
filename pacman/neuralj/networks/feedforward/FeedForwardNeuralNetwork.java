package neuralj.networks.feedforward;                                     

import java.io.Serializable;
import java.util.Vector;
import neuralj.Mathematics;
import neuralj.conditioncheckers.ConsoleConditionChecker;
import neuralj.conditioncheckers.IConditionChecker;
import neuralj.datasets.Pattern;
import neuralj.networks.feedforward.Neuron.NeuronType;
import neuralj.networks.feedforward.activation.ActivationFunctionLinear;
import neuralj.networks.feedforward.activation.ActivationFunctionSigmoid;
import neuralj.networks.feedforward.activation.IActivationFunction;

/**
 * Represents a FeedForward Neural Network
 */
public class FeedForwardNeuralNetwork implements Serializable
{
	private static final long	serialVersionUID			= -7802592286437397341L;

	/**
	 * The default number of bias neurons added to each neuron
	 */
	public static final int		DEFAULT_NUMBER_BIAS_NEURONS	= 1;

	/**
	 * The list of neuron layers the network's made of
	 */
	public Vector<NeuronLayer>	neuron_layers;

	/**
	 * The list of synapse layers that unite the neuron layers
	 */
	public Vector<SynapseLayer>	synapse_layers;

	private IConditionChecker	checker						= null;

	/**
	 * Neural Network constructor, builds it from another network (copies it's
	 * structure)
	 * 
	 * @param net
	 *            The feed forward neural network to be copied
	 */
	public FeedForwardNeuralNetwork(FeedForwardNeuralNetwork net)
	{
		this(net.getNumberNeuronsInput(Neuron.NeuronType.Normal), net.getNumberNeuronsHidden(Neuron.NeuronType.Normal), net.getNumberNeuronsOutput(Neuron.NeuronType.Normal));
	}

	/**
	 * Neural Network constructor
	 * 
	 * @param input_size
	 *            Number of neurons in the input layer
	 * @param hidden_size
	 *            Number of neurons in the hidden layers
	 * @param output_size
	 *            Number of neurons in the output layer
	 */
	public FeedForwardNeuralNetwork(int input_size, int[] hidden_size, int output_size)
	{
		this(input_size, hidden_size, output_size, new ActivationFunctionSigmoid());
	}

	/**
	 * Neural Network constructor
	 * 
	 * @param input_size
	 *            Number of neurons in the input layer
	 * @param hidden_size
	 *            Number of neurons in the hidden layers
	 * @param output_size
	 *            Number of neurons in the output layer
	 * @param activation_function
	 *            The activation function for the network's neurons
	 */
	public FeedForwardNeuralNetwork(int input_size, int[] hidden_size, int output_size, IActivationFunction activation_function)
	{
		this.checker = new ConsoleConditionChecker("FeedForwardNeuralNetwork");
		this.checker.init("FeedForwardNeuralNetwork");
		this.checker.addCheck(input_size > 0, "The specified input size for the network is smaller than 1.");
		this.checker.addCheck(output_size > 0, "The specified output size for the network is smaller than 1.");
		if (this.checker.isSecure())
		{
			// Create the layers
			int bias_nodes_per_layer = FeedForwardNeuralNetwork.DEFAULT_NUMBER_BIAS_NEURONS;
			this.neuron_layers = new Vector<NeuronLayer>();
			this.neuron_layers.add(new NeuronLayer(input_size, new ActivationFunctionLinear(), bias_nodes_per_layer));
			for (int x = 0; x < hidden_size.length; x++)
				this.neuron_layers.add(new NeuronLayer(hidden_size[x], activation_function, bias_nodes_per_layer));
			this.neuron_layers.add(new NeuronLayer(output_size, activation_function));
			// Connect the layers
			this.synapse_layers = new Vector<SynapseLayer>();
			for (int x = 0; x < this.neuron_layers.size() - 1; x++)
				connectLayers(x, x + 1);
		}
	}

	/**
	 * Connects neurons from one layer to the next
	 * 
	 * @param source
	 *            Source layer
	 * @param destination
	 *            Destination layer
	 * @return Boolean indicating if the operation was successful
	 */
	private boolean connectLayers(int source, int destination)
	{
		this.checker.init("connectLayers");
		this.checker.addCheck(source < this.neuron_layers.size(), "The source neuron layer doesn't exist.");
		this.checker.addCheck(destination < this.neuron_layers.size(), "The destination neuron layer doesn't exist.");
		this.checker.addCheck(source < destination, "The source must be before the destination.");
		if (this.checker.isSecure())
		{
			int iter2;
			SynapseLayer synapse_layer = new SynapseLayer();
			for (Neuron neuron : this.neuron_layers.get(source).neurons)
			{
				// int last_neuron_layer = this.neuron_layers.size() - 1;
				for (iter2 = 0; iter2 < this.neuron_layers.get(destination).neurons.size(); iter2++)
				{
					if (this.neuron_layers.get(destination).neurons.get(iter2).neuron_type == Neuron.NeuronType.Normal)
					{
						Neuron source_neuron = neuron;
						Neuron destination_neuron = this.neuron_layers.get(destination).getNeuron(iter2);
						Synapse synapse = connectNeurons(source_neuron, destination_neuron);
						synapse_layer.synapses.add(synapse);
					}
				}
			}
			this.synapse_layers.add(synapse_layer);
			return true;
		}
		return false;
	}

	/**
	 * Connect two neurons with a synapse.
	 * 
	 * @param source
	 *            the source neuron
	 * @param destination
	 *            the destination neuron
	 * @return The newly created synapse, or null in case of error
	 */
	public Synapse connectNeurons(Neuron source, Neuron destination)
	{
		this.checker.init("connectNeurons");
		this.checker.addCheck(source != null, "The given source neuron is null.");
		this.checker.addCheck(destination != null, "The given destination neuron is null.");
		if (this.checker.isSecure())
		{
			double weight = Mathematics.rand();
			Synapse synapse = new Synapse(source, destination, weight);
			source.outgoing_synapses.add(synapse);
			destination.incoming_synapses.add(synapse);
			return synapse;
		}
		return null;
	}

	/**
	 * Returns the current error for the a vector of patterns
	 * 
	 * @param patterns
	 *            The list of patterns that will be tested
	 * @return The mean squared error for the list of patterns, or -1.0 in case
	 *         of error
	 */
	public double measurePatternListError(Vector<Pattern> patterns)
	{
		this.checker.init("measurePatternListError");
		this.checker.addCheck(patterns != null, "The given list of patterns neuron is null.");
		this.checker.addCheck(patterns.size() > 0, "The given list of patterns is empty.");
		if (this.checker.isSecure())
		{
			double error = 0;
			int count = 0;
			for (Pattern pattern : patterns)
			{
				feedForward(pattern.input);
				error += meanSquaredError(pattern.output);
				count++;
			}
			error /= count;
			return error;
		}
		return -1.0;
	}

	/**
	 * Returns the mean squared error between the network's output and the
	 * desired one
	 * 
	 * @param output
	 *            Desired ouput
	 * @return Returns the mean squared error, or -1.0 in case of error
	 */
	public double meanSquaredError(Vector<Double> output)
	{
		this.checker.init("meanSquaredError");
		this.checker.addCheck(output != null, "The given list of outputs is null.");
		this.checker.addCheck(output.size() > 0, "The given list of outputs is empty.");
		if (this.checker.isSecure())
		{
			double sum = 0;
			int lastLayerIndex = this.neuron_layers.size() - 1;
			NeuronLayer lastLayer = this.neuron_layers.get(lastLayerIndex);
			for (int x = 0; x < output.size(); x++)
			{
				double network_output = lastLayer.getNeuron(x).output;
				double desired_output = output.get(x).doubleValue();
				sum += Math.pow(desired_output - network_output, 2);
			}
			return sum / 2;
		}
		return -1.0;
	}

	/**
	 * Calculates the network's output by feeding the input all the way to the
	 * output layer
	 * 
	 * @param input
	 *            The network's input
	 * @return Returns the network's output, or null in case of error
	 */
	public Vector<Double> feedForward(Vector<Double> input)
	{
		this.checker.init("feedForward");
		this.checker.addCheck(input != null, "The given list of inputs is null.");
		this.checker.addCheck(input.size() > 0, "The given list of inputs is empty.");
		if (this.checker.isSecure())
		{
			resetValues();
			feedForwardInputLayer(input);
			this.neuron_layers.get(0).feedForward();
			for (int x = 1; x < (this.neuron_layers.size() - 1); x++)
			{
				this.neuron_layers.get(x).calculateOutput();
				this.neuron_layers.get(x).feedForward();
			}
			int output_layer_index = this.neuron_layers.size() - 1;
			this.neuron_layers.get(output_layer_index).calculateOutput();
			return this.neuron_layers.get(output_layer_index).getOutput();
		}
		return null;
	}

	/**
	 * Feed forwards the input to the output of the input layer
	 * 
	 * @param input
	 *            The vector of inputs to the network.
	 * @return Boolean indicating if the operation has succeeded
	 */
	@SuppressWarnings("unchecked")
	protected boolean feedForwardInputLayer(Vector<Double> input)
	{
		this.checker.init("feedForwardInputLayer");
		this.checker.addCheck(input != null, "The given list of inputs is null.");
		this.checker.addCheck(input.size() > 0, "The given list of inputs is empty.");
		if (this.checker.isSecure())
		{
			NeuronLayer input_layer = this.neuron_layers.get(0);
			int number_of_bias_nodes = input_layer.getNumberNeurons(Neuron.NeuronType.Bias);
			Vector<Double> padded_input = (Vector<Double>) input.clone();
			for (int i = 0; i < number_of_bias_nodes; i++)
			{
				padded_input.add(new Double(1));
			}
			for (int x = 0; x < padded_input.size(); x++)
			{
				input_layer.getNeuron(x).input = padded_input.get(x).doubleValue();
				input_layer.getNeuron(x).calculateOutput();
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of neurons in the hidden layers
	 * 
	 * @param Type
	 *            of neuron (use constants in class Neuron)
	 * @return Number of neurons in the hidden layers
	 */
	public int[] getNumberNeuronsHidden(NeuronType neuron_type)
	{
		int[] hidden = new int[this.neuron_layers.size() - 2];
		for (int x = 1; x < this.neuron_layers.size() - 1; x++)
			hidden[x - 1] = this.neuron_layers.get(x).getNumberNeurons(neuron_type);
		return hidden;
	}

	/**
	 * Returns the number of neurons in the input layer
	 * 
	 * @param Type
	 *            of neuron (use constants in class Neuron)
	 * @return Number of neurons in the input layer
	 */
	public int getNumberNeuronsInput(NeuronType neuron_type)
	{
		return this.neuron_layers.get(0).getNumberNeurons(neuron_type);
	}

	/**
	 * Returns the number of neurons in the output layer
	 * 
	 * @param Type
	 *            of neuron (use constants in class Neuron)
	 * @return Number of neurons in the output layer
	 */
	public int getNumberNeuronsOutput(NeuronType neuron_type)
	{
		return this.neuron_layers.get(this.neuron_layers.size() - 1).getNumberNeurons(neuron_type);
	}

	/**
	 * Returns the network's output
	 * 
	 * @return Returns an array of doubles with the network's output
	 */
	public Vector<Double> getOutput()
	{
		Vector<Double> output = new Vector<Double>();
		for (int x = 0; x < this.neuron_layers.get(this.neuron_layers.size() - 1).neurons.size(); x++)
			output.add(new Double(this.neuron_layers.get(this.neuron_layers.size() - 1).getNeuron(x).output));
		return output;
	}

	/**
	 * Gets a vector of weights contained in the neural network
	 * 
	 * @return Vector of weight values
	 */
	public Vector<Double> getWeightVector()
	{
		Vector<Double> list = new Vector<Double>();
		for (SynapseLayer synlayer : this.synapse_layers)
			for (Double val : synlayer.getWeightVector())
				list.add(val);
		return list;
	}

	/**
	 * Resets the neural network's values
	 */
	public void resetValues()
	{
		for (int x = 0; x < this.neuron_layers.size(); x++)
			this.neuron_layers.get(x).resetValues();
	}

	/**
	 * Resets the neural network's weights
	 */
	public void resetWeights()
	{
		for (int x = 0; x < this.neuron_layers.size(); x++)
			this.neuron_layers.get(x).resetWeights();
	}

	/**
	 * Sets the activation function for the whole network
	 * 
	 * @param function
	 *            The activation function to include in the network's neurons
	 * @return Boolean indicating if the operation succeeded
	 */
	public boolean setActivationFunction(IActivationFunction function)
	{
		this.checker.init("setActivationFunction");
		this.checker.addCheck(function != null, "The given activation function is null.");
		if (this.checker.isSecure())
		{
			for (int x = 1; x < this.neuron_layers.size(); x++)
				this.neuron_layers.get(x).setActivationFunction(function);
			return true;
		}
		return false;
	}

	/**
	 * Sets the values of the given list as weights in the synapse layer
	 * 
	 * @param list
	 *            The list of weights
	 * @return Boolean indicating if the operation succeeded
	 */
	public boolean setWeightVector(Vector<Double> list)
	{
		this.checker.init("setWeightVector");
		this.checker.addCheck(list != null, "The given list of weights is null.");
		this.checker.addCheck(list.size() > 0, "The given list of weights is empty.");
		if (this.checker.isSecure())
		{
			if (list != null)
			{
				int offset = 0;
				for (SynapseLayer synlayer : this.synapse_layers)
					for (Synapse synapse : synlayer.synapses)
						synapse.weight = list.get(offset++).doubleValue();
			}
			else
				System.err.println("ERROR: Tried to set the network's weights by providing a null pointer instead of the weight list.");
			return true;
		}
		return false;
	}

	/**
	 * Returns the mean squared error for a input-output pair
	 * 
	 * @param input
	 *            The input
	 * @param output
	 *            The desired output
	 * @return The mean squared error for the input-output pair, or -1.0 in case
	 *         of error
	 */
	public double getPredictionError(Vector<Double> input, Vector<Double> output)
	{
		this.checker.init("getPredictionError");
		this.checker.addCheck(input != null, "The given list of inputs is null.");
		this.checker.addCheck(input.size() > 0, "The given list of inputs is empty.");
		this.checker.addCheck(output != null, "The given list of outputs is null.");
		this.checker.addCheck(output.size() > 0, "The given list of outputs is empty.");
		if (this.checker.isSecure())
		{
			feedForward(input);
			return meanSquaredError(output);
		}
		return -1.0;
	}

	/**
	 * Returns the predicted pattern for an input
	 * 
	 * @param input
	 *            The input
	 * @return The predicted pattern for the given input, or null in case of
	 *         error
	 */
	public Vector<Double> getPrediction(Vector<Double> input)
	{
		this.checker.init("getPrediction");
		this.checker.addCheck(input != null, "The given list of inputs is null.");
		this.checker.addCheck(input.size() > 0, "The given list of inputs is empty.");
		if (this.checker.isSecure())
		{
			feedForward(input);
			return getOutput();
		}
		return null;
	}

	/**
	 * Returns the mean squared error for a set of input-output pairs
	 * 
	 * @param patterns
	 *            A vector of input-output pairs
	 * @return The mean squared error for the set of input-output pairs, or -1.0
	 *         in case of error
	 */
	public double getPredictionError(Vector<Pattern> patterns)
	{
		this.checker.init("getPredictionError");
		this.checker.addCheck(patterns != null, "The given list of patterns is null.");
		this.checker.addCheck(patterns.size() > 0, "The given list of patterns is empty.");
		if (this.checker.isSecure())
		{
			double error = 0;
			for (Pattern pattern : patterns)
				error += getPredictionError(pattern.input, pattern.output);
			return error / patterns.size();
		}
		return -1.0;
	}
}
