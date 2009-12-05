package games.pacman.features;

import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.Neuron;
import neuralj.Mathematics;

import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kristoffer
 * Date: Dec 4, 2009
 * Time: 5:20:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacmanMutationRandom extends MutationRandom {
    public FeedForwardNeuralNetwork mutate(FeedForwardNeuralNetwork net)
	{
		Vector<Double> chromosome = net.getWeightVector();
		Random gen = new Random();
		for (int x = 0; x < chromosome.size(); x++)
		{
			double value = Math.random();
			if (value <= mutation_rate){
			    double valuep = Math.random();
			    if(valuep <= 0.5){
				    chromosome.set(x,new Double(chromosome.get(x)+gen.nextGaussian()*net.getNumberNeuronsInput(Neuron.NeuronType.Normal)));
				}else{
				    chromosome.set(x,new Double(chromosome.get(x)- Mathematics.rand() * 100));
				}
            }
		}
		net.setWeightVector(chromosome);
		return net;
	}
}
