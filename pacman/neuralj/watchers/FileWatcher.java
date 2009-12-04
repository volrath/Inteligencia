package neuralj.watchers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;

public class FileWatcher implements IWatcher, Serializable
{
	private static final long	serialVersionUID	= 7914245778669834094L;

	private String								training_log_path		= "";

	private String								validation_log_path		= "";

	private PrintWriter							writer_training_log		= null;

	private PrintWriter							writer_validation_log	= null;

	private FeedForwardNetworkLearningAlgorithm	algorithm				= null;

	public FileWatcher(FeedForwardNetworkLearningAlgorithm alg, String training_log, String validation_log)
	{
		this.algorithm = alg;
		this.training_log_path = training_log;
		this.validation_log_path = validation_log;
	}

	public void monitor()
	{
		this.writer_training_log.println(this.algorithm.current_epoch + "," + this.algorithm.minimum_training_error);
		this.writer_validation_log.println(this.algorithm.current_epoch + "," + this.algorithm.minimum_validation_error);
	}

	public void start()
	{
		try
		{
			this.writer_training_log = new PrintWriter(new FileOutputStream(this.training_log_path));
			this.writer_validation_log = new PrintWriter(new FileOutputStream(this.validation_log_path));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void stop()
	{
		this.writer_training_log.close();
		this.writer_validation_log.close();
	}
}
