package neuralj.networks.feedforward;

import java.io.Serializable;
import java.util.Vector;
import neuralj.conditioncheckers.ConsoleConditionChecker;

/**
 * Represents a block of outgoingSynapses
 */
public class SynapseLayer implements Serializable
{
	private static final long		serialVersionUID	= -3045094848339058452L;

	// List of outgoingSynapses
	public Vector<Synapse>			synapses			= null;

	private ConsoleConditionChecker	checker				= null;

	/**
	 * Class constructor
	 */
	public SynapseLayer()
	{
		this.checker = new ConsoleConditionChecker("NeuronLayer");
		this.checker.init("NeuronLayer");
		this.synapses = new Vector<Synapse>();
	}

	/**
	 * Gets a vector of weights contained in the synapse layer
	 * 
	 * @return Vector of weight values, or null in case of error
	 */
	public Vector<Double> getWeightVector()
	{
		this.checker.init("getWeightVector");
		this.checker.addCheck(this.synapses != null, "The synapse list is empty.");
		if (this.checker.isSecure())
		{
			Vector<Double> list = new Vector<Double>();
			for (Synapse syn : this.synapses)
				list.add(new Double(syn.weight));
			return list;
		}
		return null;
	}

	/**
	 * Sets the values of the given list as weights in the synapse layer
	 * 
	 * @param list
	 *            The list of weights
	 * @return Boolean indicating if the operation has succeeded
	 */
	public boolean setWeightVector(Vector<Double> list)
	{
		this.checker.init("setWeightVector");
		this.checker.addCheck(this.synapses != null, "The synapse list is empty.");
		this.checker.addCheck(list != null, "The given weight list is empty.");
		if (this.checker.isSecure())
		{
			if (list.size() == this.synapses.size())
				for (int x = 0; x < list.size(); x++)
					this.synapses.get(x).weight = list.get(x).doubleValue();
			return true;
		}
		return false;
	}
}
