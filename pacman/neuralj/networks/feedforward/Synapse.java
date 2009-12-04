package neuralj.networks.feedforward;

import java.io.Serializable;
import neuralj.conditioncheckers.ConsoleConditionChecker;

/**
 * Represents a synapse (the connection between two neurons)
 */
public class Synapse implements Serializable
{
	private static final long		serialVersionUID	= 5302869469322225447L;

	/**
	 * The neuron that's on the end of the synapse
	 */
	public Neuron					destination_neuron;

	/**
	 * The neuron that's on the start of the synapse
	 */
	public Neuron					source_neuron;

	/**
	 * The value that is being transmitted throught this synapse: the output
	 * from the source neuron.
	 */
	public double					value;

	/**
	 * The synapse's weight
	 */
	public double					weight;

	private ConsoleConditionChecker	checker				= null;

	/**
	 * Synapse constructor
	 * 
	 * @param p_source_neuron
	 *            Source neuron
	 * @param p_destination_neuron
	 *            Destination neuron
	 * @param p_weight
	 *            Synapse weight
	 */
	public Synapse(Neuron p_source_neuron, Neuron p_destination_neuron, double p_weight)
	{
		this.checker = new ConsoleConditionChecker("Synapse");
		this.checker.init("Synapse");
		this.checker.addCheck(p_source_neuron != null, "The specified source neuron is null.");
		this.checker.addCheck(p_destination_neuron != null, "The specified destination neuron is null.");
		if (this.checker.isSecure())
		{
			this.source_neuron = p_source_neuron;
			this.destination_neuron = p_destination_neuron;
			this.weight = p_weight;
		}
	}
}
