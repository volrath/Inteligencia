package games.pacman.features;

import neuralj.networks.feedforward.*;
import neuralj.networks.feedforward.activation.ActivationFunctionSigmoid;
import neuralj.networks.feedforward.activation.ActivationFunctionLinear;
import neuralj.networks.feedforward.activation.IActivationFunction;
import neuralj.Mathematics;

import java.util.Random;
import java.util.Vector;

import utilities.ElapsedTimer;
import games.pacman.core.FullGame;
import games.pacman.controllers.NeuroticPacmanController;

/**
 * Created by IntelliJ IDEA.
 * User: kristoffer
 * Date: Dec 4, 2009
 * Time: 7:26:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacmanFeedForwardNeuralNetwork extends FeedForwardNeuralNetwork {

    public PacmanFeedForwardNeuralNetwork(FeedForwardNeuralNetwork net){
        super(net.getNumberNeuronsInput(Neuron.NeuronType.Normal),net.getNumberNeuronsHidden(Neuron.NeuronType.Normal),net.getNumberNeuronsOutput(Neuron.NeuronType.Normal),new ActivationFunctionHTangent());
        this.setWeightVector((Vector<Double>)net.getWeightVector().clone());
    }

    public PacmanFeedForwardNeuralNetwork(int input_size, int[] hidden_size, int output_size)
	{
		super(input_size, hidden_size, output_size, new ActivationFunctionHTangent());        
	}    

	/**
	 * Connects neurons from one layer to the next
	 *
	 * @param source
	 *            Source layer
	 * @param destination
	 *            Destination layer
	 * @return Boolean indicating if the operation was successful
	 */
	private boolean connectLayers(int source, int destination)
	{
		int iter2;
		SynapseLayer synapse_layer = new SynapseLayer();
		for (Neuron neuron : this.neuron_layers.get(source).neurons)
		{
			// int last_neuron_layer = this.neuron_layers.size() - 1;
			for (iter2 = 0; iter2 < this.neuron_layers.get(destination).neurons.size(); iter2++)
			{
				if (this.neuron_layers.get(destination).neurons.get(iter2).neuron_type == Neuron.NeuronType.Normal)
				{
					Neuron source_neuron = neuron;
					Neuron destination_neuron = this.neuron_layers.get(destination).getNeuron(iter2);
					Synapse synapse = connectNeurons(source_neuron, destination_neuron);
					synapse_layer.synapses.add(synapse);
				}
			}
		}
		this.synapse_layers.add(synapse_layer);
		return true;
	}
    

    public Synapse connectNeurons(Neuron source, Neuron destination)
	{
        Random rnd = new Random(); 
		double weight = rnd.nextGaussian()*Math.sqrt(getNumberNeuronsInput(Neuron.NeuronType.Normal));
		Synapse synapse = new Synapse(source, destination, weight);
		source.outgoing_synapses.add(synapse);
		destination.incoming_synapses.add(synapse);
		return synapse;		
	}

    public double calcFitness(int numIter){
        double score = 0;
        FullGame game = new FullGame();
        NeuroticPacmanController pc = new NeuroticPacmanController(game, this);
		game.setController(pc);        
        ElapsedTimer t = new ElapsedTimer();
        int lives = 3;
        int maxIts = 10000;
        for(int i = 0; i < numIter; i++) {
            score += game.runModel( lives, maxIts );
        }
        return (score/numIter);
    }

    public static double calcFitness(int numIter, FeedForwardNeuralNetwork net){
        double score = 0;
        FullGame game = new FullGame();
        NeuroticPacmanController pc = new NeuroticPacmanController(game, net);
		game.setController(pc);
        ElapsedTimer t = new ElapsedTimer();
        int lives = 3;
        int maxIts = 10000;
        for(int i = 0; i < numIter; i++) {
            score += game.runModel( lives, maxIts );
        }
        return (score/numIter);
    }

}
