package neuralj.testers;

import java.util.Vector;
import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.datasets.PatternSet.PatternType;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class ConsoleTester
{
	FeedForwardNeuralNetwork	network	= null;

	public ConsoleTester(FeedForwardNeuralNetwork net)
	{
		this.network = net;
	}

	public void test(PatternSet pattern_set)
	{
		for (Pattern pattern : pattern_set.getShrunkPatterns(PatternType.All))
		{
			Vector<Double> input = pattern.input;
			Vector<Double> output = this.network.getPrediction(input);
			for (Double value : input)
				System.out.print(pattern_set.input_interval.unshrink(value.doubleValue()) + " ");
			System.out.print("= ");
			for (Double value : output)
				System.out.print(pattern_set.output_interval.unshrink(value.doubleValue()) + " ");
			System.out.println();
		}
	}
}
