package neuralj.watchers;

import java.io.Serializable;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;

public class ConsoleWatcher implements IWatcher, Serializable
{
	private static final long	serialVersionUID	= -7646879559055642213L;
	
	private FeedForwardNetworkLearningAlgorithm	algorithm	= null;

	public ConsoleWatcher(FeedForwardNetworkLearningAlgorithm alg)
	{
		this.algorithm = alg;
	}

	public void monitor()
	{
		System.out.println("Epoch: " + this.algorithm.current_epoch + " Training MSE: " + this.algorithm.minimum_training_error + " Validation MSE: " + this.algorithm.minimum_validation_error);
	}

	public void start()
	{
		// Nothing
	}

	public void stop()
	{
		this.algorithm = null;
	}
}
