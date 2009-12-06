package games.pacman.features;

import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.Neuron;
import neuralj.networks.feedforward.SynapseLayer;
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
    /*
        Simon M. Lucas mutation
     */
    public FeedForwardNeuralNetwork mutate(FeedForwardNeuralNetwork net)
	{
        Vector<Double> newWeights = new Vector<Double>();
		Random gen = new Random();
        int mc = gen.nextInt(100), slSize;

        if (mc <= 10) { // all the weights in the network
            for(SynapseLayer sl: net.synapse_layers) {
                slSize = sl.getWeightVector().size() / 20;
                for (Double weight: sl.getWeightVector())
                    newWeights.add(weight + gen.nextGaussian()*Math.sqrt(slSize));
            }
            net.setWeightVector(newWeights);
        }
        else if (mc <= 37) { // all the weights in a randomly selected layer
            SynapseLayer sl = net.synapse_layers.get(gen.nextInt(net.synapse_layers.size()));
            for (Double weight: sl.getWeightVector())
                newWeights.add(weight + gen.nextGaussian() * Math.sqrt(sl.getWeightVector().size()/20));
            sl.setWeightVector(newWeights);
        }
        else if (mc <= 64) { // all the weights going into a randomly selecte layer, i can't tell the difference between this and the last one
            SynapseLayer sl = net.synapse_layers.get(gen.nextInt(net.synapse_layers.size()));
            for (Double weight: sl.getWeightVector())
                newWeights.add(weight + gen.nextGaussian() * Math.sqrt(sl.getWeightVector().size()/20));
            sl.setWeightVector(newWeights);
        }
        else {
            newWeights = net.getWeightVector();
            int rInd = gen.nextInt(newWeights.size());
            newWeights.set(rInd, newWeights.get(rInd) + Math.sqrt(gen.nextGaussian()*14));
            net.setWeightVector(newWeights);
        }
		return net;
	}
}
