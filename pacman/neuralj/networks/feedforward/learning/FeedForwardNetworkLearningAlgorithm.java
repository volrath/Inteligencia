package neuralj.networks.feedforward.learning;

import java.util.HashMap;
import java.util.Vector;
import neuralj.conditioncheckers.ConsoleConditionChecker;
import neuralj.conditioncheckers.IConditionChecker;
import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.datasets.PatternSet.PatternType;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.Neuron;
import neuralj.networks.feedforward.NeuronLayer;
import neuralj.networks.feedforward.Synapse;
import neuralj.networks.feedforward.SynapseLayer;
import neuralj.watchers.BlindWatcher;
import neuralj.watchers.IWatcher;

/**
 * Implements the basic services for Feed Forward Neural Network learning
 * algorithms
 */
public abstract class FeedForwardNetworkLearningAlgorithm extends Thread
{
	// The default maximum number of epochs of computation allowed
	protected static final int			DEFAULT_MAXIMUM_EPOCHS				= 10000;

	// The default minimum required error
	protected static final double		DEFAULT_MINIMUM_ERROR				= 0.001;

	// The maximum of epochs the algorithm can do
	public int							maximum_epochs						= DEFAULT_MAXIMUM_EPOCHS;

	// The minimum error the algorithm must reach to end the training
	public double						desired_error						= DEFAULT_MINIMUM_ERROR;

	// The network that's going to be trained with the learning algorithm
	public FeedForwardNeuralNetwork		network;

	protected HashMap<Synapse, Double>	delta_weight;

	protected HashMap<Synapse, Double>	old_error_partial_derivative;

	protected HashMap<Synapse, Double>	error_partial_derivative;

	// The previous weight update performed in each synapse
	private HashMap<Synapse, Double>	old_weight_update;

	// The weight update to perform in each synapse
	protected HashMap<Synapse, Double>	weight_update;

	// Indicates if the algorithm is running, also allows external stopping of
	// the same
	public boolean						is_running							= false;

	// Indicates if the algorithm should wait to continue
	public boolean						is_paused							= false;

	// Indicates if the algorithm should pause after each epoch
	public boolean						auto_pause							= false;

	// The current epoch the training process is going through
	public int							current_epoch						= 0;

	// The current error computed in the validation phase
	public double						current_validation_error			= Double.MAX_VALUE;

	// The current error computed in the validation phase
	public double						current_training_error				= Double.MAX_VALUE;

	// The epoch where the minimum error was found in the training phases during
	// the learning process
	public int							minimum_training_error_epoch		= 0;

	// The minimum error found in the training phases during the learning
	// process
	public double						minimum_training_error				= Double.MAX_VALUE;

	// The weights the network had when the minimum training error was found
	public Vector<Double>				minimum_training_error_weights		= null;

	// The epoch where the minimum error was found in the validation phases
	// during the learning process
	public int							minimum_validation_error_epoch		= 0;

	// The minimum error found in the validation phases during the learning
	// process
	public double						minimum_validation_error			= Double.MAX_VALUE;

	// The weights the network had when the minimum validation error was found
	public Vector<Double>				minimum_validation_error_weights	= null;

	// The pattern set that's going to be used for learning
	public PatternSet					pattern_set							= null;

	// The object that watches the learning process
	public IWatcher						watcher								= new BlindWatcher();

	// The types of learning strategy employable
	public enum LearningStrategy
	{
		Memorize, Generalization
	}

	// The types of learning strategy employed
	public LearningStrategy		learning_strategy	= LearningStrategy.Memorize;

	private IConditionChecker	checker				= null;

	/**
	 * Constructor for the learning algorithm
	 * 
	 * @param p_network
	 *            The neural network the learning algorithm will act upon
	 */
	public FeedForwardNetworkLearningAlgorithm(FeedForwardNeuralNetwork p_network)
	{
		this.checker = new ConsoleConditionChecker("FeedForwardNetworkLearningAlgorithm");
		this.network = p_network;
		reset();
	}

	/**
	 * Resets all the data used by the learning algorithm
	 */
	private void reset()
	{
		this.current_epoch = 0;
		this.current_validation_error = Double.MAX_VALUE;
		this.current_training_error = Double.MAX_VALUE;
		this.minimum_training_error_epoch = 0;
		this.minimum_training_error = Double.MAX_VALUE;
		this.minimum_training_error_weights = null;
		this.minimum_validation_error_epoch = 0;
		this.minimum_validation_error = Double.MAX_VALUE;
		this.minimum_validation_error_weights = null;
		this.old_error_partial_derivative = new HashMap<Synapse, Double>();
		this.error_partial_derivative = new HashMap<Synapse, Double>();
		this.delta_weight = new HashMap<Synapse, Double>();
		this.old_weight_update = new HashMap<Synapse, Double>();
		this.weight_update = new HashMap<Synapse, Double>();
		for (NeuronLayer layer : this.network.neuron_layers)
			for (Neuron neuron : layer.neurons)
				for (Synapse synapse : neuron.outgoing_synapses)
				{
					this.weight_update.put(synapse, new Double(0));
					this.old_error_partial_derivative.put(synapse, new Double(0));
					this.error_partial_derivative.put(synapse, new Double(0));
					this.delta_weight.put(synapse, new Double(0));
				}
		resetPartialDerivatives();
	}

	/**
	 * Calculates the deltas for all the neurons
	 * 
	 * @param output
	 *            The desired network output
	 * @return Boolean indicating if the operation was successful
	 */
	protected boolean calculateDeltas(Vector<Double> output)
	{
		this.checker.init("calculateDeltas");
		this.checker.addCheck(output != null, "The provided list of output values is null.");
		this.checker.addCheck(output.size() > 0, "The provided list of output values is empty.");
		if (this.checker.isSecure())
		{
			int number_of_layers = this.network.neuron_layers.size();
			calculateOutputLayerDeltas(output);
			// Calculates the deltas in the other layers
			for (int x = number_of_layers - 2; x > 0; x--)
			{
				NeuronLayer hidden_layer = this.network.neuron_layers.get(x);
				calculateHiddenLayerDeltas(hidden_layer);
			}
			return true;
		}
		return false;
	}

	/**
	 * @param synapse
	 * @return the new derivative value. This function assumes that the neruon
	 *         delta values (the neuron sensitivities) have already been
	 *         updated; that is, that calculateDeltas(output) has already been
	 *         called this epoch.
	 */
	protected double calculateErrorPartialDerivative(Synapse synapse)
	{
		double a_delta = synapse.destination_neuron.delta;
		double synapseValue = synapse.source_neuron.output;
		double derivative = -a_delta * synapseValue;
		return derivative;
	}

	/**
	 * Calculates the layer's derived error via the next layer's neuron's error *
	 * 
	 * @return Boolean indicating if the operation was successful
	 */
	private boolean calculateHiddenLayerDeltas(NeuronLayer layer)
	{
		this.checker.init("calculateHiddenLayerDeltas");
		this.checker.addCheck(layer != null, "The provided layer is null.");
		this.checker.addCheck(layer.neurons != null, "The list of neurons contained in the layer is null.");
		this.checker.addCheck(layer.neurons.size() > 0, "The list of neurons contained in the layer is empty.");
		if (this.checker.isSecure())
		{
			for (Neuron neuron : layer.neurons)
				neuron.delta = calculateHiddenNeuronDelta(neuron);
			return true;
		}
		return false;
	}

	/**
	 * Calculates the neuron's error via the error of the next layer's neurons
	 */
	private double calculateHiddenNeuronDelta(Neuron neuron)
	{
		double sum = 0;
		for (int z = 0; z < neuron.outgoing_synapses.size(); z++)
		{
			Synapse synapse = neuron.getOutgoingSynapse(z);
			double downstream_delta = synapse.destination_neuron.delta;
			sum += synapse.weight * downstream_delta;
		}
		double derivative = neuron.calculateDerivative(neuron.input);
		return derivative * sum;
	}

	/**
	 * Calculate an output layer's error
	 * 
	 * @param output
	 *            Desired output
	 * @return Boolean indicating if the operation was successful
	 */
	private boolean calculateOutputLayerDeltas(Vector<Double> output)
	{
		this.checker.init("calculateOutputLayerDeltas");
		this.checker.addCheck(output != null, "The provided list of output values is null.");
		this.checker.addCheck(output.size() > 0, "The provided list of output values is empty.");
		if (this.checker.isSecure())
		{
			NeuronLayer output_layer = this.network.neuron_layers.get(this.network.neuron_layers.size() - 1);
			for (int y = 0; y < output_layer.neurons.size(); y++)
			{
				Neuron neuron = output_layer.neurons.get(y);
				neuron.delta = calculateOutputNeuronDelta(output.get(y).doubleValue(), neuron);
			}
			return true;
		}
		return false;
	}

	/**
	 * Calculates the neuron's error and stores it in the delta variable
	 * 
	 * @param target
	 *            Desired output
	 */
	private double calculateOutputNeuronDelta(double target, Neuron neuron)
	{
		double derivative = neuron.calculateDerivative(neuron.output);
		double error = target - neuron.output;
		return derivative * error;
	}

	/**
	 * Trains the network to generalize by the "Early Stopping Method of
	 * Training"
	 * 
	 * @param training_patterns
	 *            Training set
	 * @param validation_patterns
	 *            Validation set
	 * @return Boolean indicating if the operation was successfull
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	protected boolean generalize(Vector<Pattern> training_patterns, Vector<Pattern> validation_patterns)
	{
		// Initialize the checks
		this.checker.init("generalize");
		this.checker.addCheck(training_patterns != null, "The training patterns provided were in fact a null pointer.");
		this.checker.addCheck(validation_patterns != null, "The validation patterns provided were in fact a null pointer.");
		this.checker.addCheck(training_patterns.size() >= 0, "The training patterns list is empty.");
		this.checker.addCheck(validation_patterns.size() >= 0, "The validation patterns list is empty.");
		// Run if checks succeeded
		if (this.checker.isSecure())
		{
			reset();
			this.is_running = true;
			this.watcher.start();
			// Train while minimum error is not met and the maximum number of
			// steps is not exceeded
			while (this.current_epoch < this.maximum_epochs && this.current_validation_error >= this.desired_error && this.is_running)
			{
				while (this.is_paused)
					;
				trainEpoch(training_patterns);
				this.current_training_error = this.network.getPredictionError(training_patterns);
				this.current_validation_error = this.network.getPredictionError(validation_patterns);
				if (this.current_validation_error < this.minimum_validation_error)
				{
					this.minimum_validation_error_weights = this.network.getWeightVector();
					this.minimum_validation_error = this.current_validation_error;
					this.minimum_validation_error_epoch = this.current_epoch;
				}
				if (this.current_training_error < this.minimum_training_error)
				{
					this.minimum_training_error_weights = this.network.getWeightVector();
					this.minimum_training_error = this.current_training_error;
					this.minimum_training_error_epoch = this.current_epoch;
				}
				if (this.auto_pause)
					this.is_paused = true;
				this.watcher.monitor();
			}
			this.network.setWeightVector(this.minimum_validation_error_weights);
			this.watcher.stop();
			this.is_running = false;
			return true;
		}
		return false;
	}

	/**
	 * Returns the old weight update for the given synapse
	 * 
	 * @param synapse
	 *            The synapse associated with the old weight update you want to
	 *            retrieve
	 * @return Returns the value of the previous update, returns 0 if it doesn't
	 *         exist
	 */
	protected double getOldWeightUpdate(Synapse synapse)
	{
		if (this.old_weight_update.get(synapse) == null)
			return 0.0;
		return this.old_weight_update.get(synapse).doubleValue();
	}

	/**
	 * Teaches the training set to the network by memorization
	 * 
	 * @param patterns
	 *            Training set
	 * @return Boolean indicating if the operation was successfull
	 */
	@SuppressWarnings("unchecked")
	protected boolean memorize(Vector<Pattern> patterns)
	{
		// Initialize the checks
		this.checker.init("memorize");
		this.checker.addCheck(patterns != null, "The patterns provided were in fact a null pointer.");
		this.checker.addCheck(patterns.size() >= 0, "The patterns list is empty.");
		// Run if checks succeeded
		if (this.checker.isSecure())
		{
			this.is_running = true;
			reset();
			this.watcher.start();
			// Train while the error is superior to the minimum and maximum
			// number
			// is not exceeded
			do
			{
				// If the pause signal is active, wait..
				while (this.is_paused)
					;
				trainEpoch(patterns);
				this.current_training_error = this.network.getPredictionError(patterns);
				if (this.current_training_error < this.minimum_training_error)
				{
					this.minimum_training_error_weights = this.network.getWeightVector();
					this.minimum_training_error = this.current_training_error;
					this.minimum_training_error_epoch = this.current_epoch;
				}
				if (this.auto_pause)
					this.is_paused = true;
				this.watcher.monitor();
			}
			while (this.current_training_error > this.desired_error && this.current_epoch < this.maximum_epochs && this.is_running);
			this.network.setWeightVector(this.minimum_training_error_weights);
			this.watcher.stop();
			this.is_running = false;
			return true;
		}
		return false;
	}

	/**
	 * Resets the partial derivative field of all synapses.
	 * 
	 * @return Boolean indicating if the operation was successfull
	 */
	protected boolean resetPartialDerivatives()
	{
		this.checker.init("memorize");
		this.checker.addCheck(this.network.synapse_layers != null, "The network has no synapse layer pointer.");
		this.checker.addCheck(this.network.synapse_layers.size() > 0, "The network has no synapse layers.");
		if (this.checker.isSecure())
		{
			for (SynapseLayer layer : this.network.synapse_layers)
			{
				for (Synapse synapse : layer.synapses)
				{
					this.error_partial_derivative.put(synapse, new Double(0.0));
					this.old_error_partial_derivative.put(synapse, new Double(0.0));
					this.delta_weight.put(synapse, new Double(0.1));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Updates the weight update for a given synapse, if one was already stored
	 * then it is moved to the old weight update, and the new one takes it's
	 * place
	 * 
	 * @param synapse
	 *            The synapse associated with the given weight update
	 * @param update
	 *            The value of the update
	 */
	protected void setWeightUpdate(Synapse synapse, double update)
	{
		Double stored_update = this.weight_update.get(synapse);
		if (stored_update != null)
			this.old_weight_update.put(synapse, stored_update);
		this.weight_update.put(synapse, new Double(update));
	}

	/**
	 * Trains the neural network with a pattern for one epoch
	 * 
	 * @param patterns
	 *            The list of patterns the network is going to be trained with
	 *            for one epoch
	 */
	protected abstract void trainEpoch(Vector<Pattern> patterns);

	@Override
	public void run()
	{
		this.is_running = true;
		if (this.pattern_set != null)
		{
			if (this.learning_strategy == LearningStrategy.Memorize)
				memorize(this.pattern_set.getShrunkPatterns(PatternType.All));
			else if (this.learning_strategy == LearningStrategy.Generalization)
				generalize(this.pattern_set.getShrunkPatterns(PatternType.Training), this.pattern_set.getShrunkPatterns(PatternType.Validation));
		}
		this.is_running = false;
	}
}
