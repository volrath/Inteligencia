package neuralj.networks.feedforward.learning.bprop;

import java.util.Vector;
import neuralj.datasets.Pattern;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.Neuron;
import neuralj.networks.feedforward.NeuronLayer;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;

/**
 * Implements the backpropagation learning algorithm for Feed-Forward Networks
 * 
 * @TODO Add checkers
 */
public class BackPropagation extends FeedForwardNetworkLearningAlgorithm
{
	/**
	 * The algorithm's learning rate
	 */
	private static final double	DEFAULT_LEARNING_RATE	= 1.0;

	/**
	 * The algorithm's momentum rate
	 */
	private static final double	DEFAULT_MOMENTUM_RATE	= 0.1;

	/**
	 * The algorithm's learning rate determines the speed of training
	 */
	public double				learning_rate			= DEFAULT_LEARNING_RATE;

	/**
	 * The algorithm's learning rate determines the influence of the previous
	 * update on the current update
	 */
	public double				momentum_rate			= DEFAULT_MOMENTUM_RATE;

	/**
	 * Simple constructor for the BackPropagation learning algorithm
	 * 
	 * @param p_network
	 *            The neural network the algorithm will use
	 */
	public BackPropagation(FeedForwardNeuralNetwork p_network)
	{
		super(p_network);
	}

	/**
	 * Adjusts the network's weights
	 */
	private void adjustWeights()
	{
		for (int x = 0; x < (this.network.neuron_layers.size() - 1); x++)
		{
			adjustWeights(this.network.neuron_layers.get(x));
		}
	}

	/**
	 * Adjusts the weights of the outgoing synapses this neuron feeds data into
	 */
	private void adjustWeights(Neuron neuron)
	{
		for (int z = 0; z < neuron.outgoing_synapses.size(); z++)
		{
			double weight = neuron.getOutgoingSynapse(z).weight;
			double a_weight_update = neuron.output * this.learning_rate * neuron.getOutgoingSynapse(z).destination_neuron.delta + this.momentum_rate * this.getOldWeightUpdate(neuron.getOutgoingSynapse(z));
			setWeightUpdate(neuron.getOutgoingSynapse(z), a_weight_update);
			neuron.getOutgoingSynapse(z).weight = weight + a_weight_update;
		}
	}

	/**
	 * Adjusts the weights of the outgoing synapses
	 */
	private void adjustWeights(NeuronLayer layer)
	{
		for (int y = 0; y < layer.neurons.size(); y++)
		{
			adjustWeights(layer.getNeuron(y));
		}
	}

	/**
	 * Trains the neural network with a pattern for one epoch
	 * 
	 * @param pattern_set
	 *            List of patterns to train the network with
	 */
	@Override
	public void trainEpoch(Vector<Pattern> p_pattern_set)
	{
		for (Pattern pattern : p_pattern_set)
		{
			this.network.feedForward(pattern.input);
			calculateDeltas(pattern.output);
			adjustWeights();
		}
		this.current_epoch++;
	}
}
