package neuralj.networks.feedforward.learning.rprop;

import java.util.Vector;
import neuralj.datasets.Pattern;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.Synapse;
import neuralj.networks.feedforward.SynapseLayer;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;

/**
 * Implements the Resilient BackPropagation learning algorithm
 */
public class ResilientBackPropagation extends FeedForwardNetworkLearningAlgorithm
{
	private static final double	DEFAULT_DELTA_MAX		= 50;

	private static final double	DEFAULT_DELTA_MIN		= Math.pow(10, -6);

	private static final double	DEFAULT_DELTA_NOUGHT	= 0.1;

	private static final double	DEFAULT_ETA_MINUS		= 0.5;

	private static final double	DEFAULT_ETA_PLUS		= 1.2;

	private double				delta_max				= DEFAULT_DELTA_MAX;

	private double				delta_min				= DEFAULT_DELTA_MIN;

	private double				delta_nought			= DEFAULT_DELTA_NOUGHT;

	private final double		eta_minus				= DEFAULT_ETA_MINUS;

	private final double		eta_plus				= DEFAULT_ETA_PLUS;

	/**
	 * Complete constructor for the Resilient BackPropagation learning algorithm
	 * 
	 * @param p_network
	 *            The neural network the algorithm will use
	 */
	public ResilientBackPropagation(FeedForwardNeuralNetwork p_network)
	{
		super(p_network);
		initializeSynapseDeltas();
	}

	/**
	 * Initializes the outgoingSynapses' delta parameter
	 */
	private void initializeSynapseDeltas()
	{
		for (SynapseLayer layer : this.network.synapse_layers)
		{
			for (Synapse synapse : layer.synapses)
			{
				setWeightUpdate(synapse, this.delta_nought);
			}
		}
	}

	/**
	 * Indicates if the partial derivative changed sign in relation to the
	 * previous epoch
	 * 
	 * @param synapse
	 *            The synapse that's being tested
	 * @return Returns boolean indicating whether the partial derivative changed
	 *         sign in relation to the previous epoch
	 */
	private boolean partialDerivativeChangesSign(Synapse synapse)
	{
		double old = 0.0;
		if (this.old_error_partial_derivative.get(synapse) != null)
			old = this.old_error_partial_derivative.get(synapse).doubleValue();
		double current = this.error_partial_derivative.get(synapse).doubleValue();
		boolean changes = current * old < 0.0;
		return changes;
	}

	/**
	 * Indicates if the partial derivative maintained sign in relation to the
	 * previous epoch
	 * 
	 * @param synapse
	 *            The synapse that's being tested
	 * @return Returns boolean indicating whether the partial derivative
	 *         maintained sign in relation to the previous epoch
	 */
	private boolean partialDerivativeMaintainsSign(Synapse synapse)
	{
		double old = 0.0;
		if (this.old_error_partial_derivative.get(synapse) != null)
			old = this.old_error_partial_derivative.get(synapse).doubleValue();
		double current = this.error_partial_derivative.get(synapse).doubleValue();
		boolean maintains = current * old > 0.0;
		return maintains;
	}

	/**
	 * Trains the neural network with a pattern for one epoch
	 * 
	 * @param patterns
	 *            List of patterns
	 * 
	 * @return the mean squared error of the network on the training set after
	 *         the weight updates of this epoch have been performed
	 */
	@Override
	public void trainEpoch(Vector<Pattern> patterns)
	{
		for (SynapseLayer layer : this.network.synapse_layers)
		{
			for (Synapse synapse : layer.synapses)
			{
				this.delta_weight.put(synapse, new Double(ResilientBackPropagation.DEFAULT_DELTA_NOUGHT));
			}
		}
		if (this.current_epoch >= 0)
		{
			for (Pattern pattern : patterns)
			{
				this.network.feedForward(pattern.input);
				calculateDeltas(pattern.output);
				for (SynapseLayer layer : this.network.synapse_layers)
				{
					for (Synapse synapse : layer.synapses)
					{
						updatePartialDerivative(synapse);
					}
				}
			}
			updateWeights();
		}
		this.current_epoch++;
	}

	/**
	 * Updates the partial derivative for the specified synapse
	 * 
	 * @param synapse
	 *            The synapse where the partial derivative is going to be
	 *            updated
	 */
	private void updatePartialDerivative(Synapse synapse)
	{
		double derivative = this.error_partial_derivative.get(synapse).doubleValue();
		double derivativeUpdate = calculateErrorPartialDerivative(synapse);
		this.error_partial_derivative.put(synapse, new Double(derivative + derivativeUpdate));
	}

	/**
	 * Updates the outgoing synapses' weights to improve the network's output
	 */
	private void updateWeights()
	{
		// straight from the pseudocode in the rprop paper
		for (SynapseLayer layer : this.network.synapse_layers)
		{
			for (Synapse synapse : layer.synapses)
			{
				double u_delta_weight = this.delta_weight.get(synapse).doubleValue();
				double u_error_partial_derivative = this.error_partial_derivative.get(synapse).doubleValue();
				double u_old_error_partial_derivative = this.old_error_partial_derivative.get(synapse).doubleValue();
				double u_weight_update = this.weight_update.get(synapse).doubleValue();
				if (partialDerivativeMaintainsSign(synapse))
				{
					this.delta_weight.put(synapse, new Double(Math.min(u_delta_weight * this.eta_plus, this.delta_max)));
					this.weight_update.put(synapse, new Double(-Math.signum(u_error_partial_derivative) * this.delta_weight.get(synapse).doubleValue()));
					synapse.weight = synapse.weight + this.weight_update.get(synapse).doubleValue();
					this.old_error_partial_derivative.put(synapse, new Double(u_error_partial_derivative));
				}
				else if (partialDerivativeChangesSign(synapse))
				{
					this.delta_weight.put(synapse, new Double(Math.max(u_delta_weight * this.eta_minus, this.delta_min)));
					this.old_error_partial_derivative.put(synapse, new Double(0.0));
				}
				else
				{
					this.weight_update.put(synapse, new Double(-Math.signum(u_error_partial_derivative) * u_delta_weight));
					synapse.weight = synapse.weight + this.weight_update.get(synapse).doubleValue();
					this.old_error_partial_derivative.put(synapse, new Double(u_error_partial_derivative));
				}
			}
		}
	}
}
