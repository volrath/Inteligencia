package neuralj.testers;

import java.util.Vector;
import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.datasets.PatternSet.PatternType;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

public class StringTester
{
	FeedForwardNeuralNetwork	network	= null;

	public StringTester(FeedForwardNeuralNetwork net)
	{
		this.network = net;
	}

	public String test(PatternSet pattern_set)
	{
		String output_text = "";
		for (Pattern pattern : pattern_set.getShrunkPatterns(PatternType.All))
		{
			Vector<Double> input = pattern.input;
			Vector<Double> output = this.network.getPrediction(input);
			for (Double value : input)
				output_text += pattern_set.input_interval.unshrink(value.doubleValue()) + " ";
			output_text += "= ";
			for (Double value : output)
				output_text += pattern_set.output_interval.unshrink(value.doubleValue()) + " ";
			output_text += "\n";
		}
		return output_text;
	}
}
