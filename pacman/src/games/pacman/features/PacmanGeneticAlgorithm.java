package games.pacman.features;

import java.util.Random;
import java.util.Vector;
import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;
import neuralj.networks.feedforward.activation.ActivationFunctionSigmoid;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;
import neuralj.networks.feedforward.learning.genetic.crossover.CrossoverSinglePoint;
import neuralj.networks.feedforward.learning.genetic.crossover.ICrossoverOperator;
import neuralj.networks.feedforward.learning.genetic.mutation.IMutationOperator;
import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.learning.genetic.selection.ISelectionOperator;
import neuralj.networks.feedforward.learning.genetic.selection.RouletteWheel;
import neuralj.networks.feedforward.learning.genetic.selection.SelectionRouletteWheel;
import games.pacman.features.PacmanSelectionRouletteWheel;
import games.pacman.features.PacmanRouletteWheel;

/**
 * Implements a Genetic Algorithm to find the correct neural network
 * configuration
 *
 * @FIXME Currently enforces use of sigmoid function due unknown weight
 *        amplitudes
 * @FIXME Mutation operator?
 * @FIXME Training epoch no return???
 * @TODO Add checkers
 */
public class PacmanGeneticAlgorithm extends FeedForwardNetworkLearningAlgorithm
{
	// The default crossover rate for the genetic algorithm
	public static final double	DEFAULT_CROSSOVER_RATE		= 0;

	// The default mutation rate for the genetic algorithm
	public static final double	DEFAULT_MUTATION_RATE		= 0.5;

	// The default population size for the genetic algorithm
	public static final int		DEFAULT_POPULATION_SIZE		= 20;

	// The default size weights can take up or down (example: 5.0 means the
	// weight range is [-5.0,5.0]
	public static final double	DEFAULT_WEIGHT_AMPLITUDE	= 100.0;

	// Genetic algorithm crossover operator
	public ICrossoverOperator	crossover_operator			= new CrossoverSinglePoint();

	// The genetic algorithm's crossover rate
	public double				crossover_rate				= DEFAULT_CROSSOVER_RATE;

	// Genetic algorithm mutation operator
	public IMutationOperator	mutation_operator			= new PacmanMutationRandom(); 

    public double               mutation_rate               = DEFAULT_MUTATION_RATE;

	// The genetic algorithm's mutation rate
	// private double mutation_rate;
	// The genetic algorithm's population
	public RouletteWheel		population					= new PacmanRouletteWheel();

	// The genetic algorithm's population size
	public int					population_size				= DEFAULT_POPULATION_SIZE;

	// Genetic algorithm selection operator
	public ISelectionOperator	selection_operator			= new PacmanSelectionRouletteWheel();

	/**
	 * Simple constructor for the Genetic Algorithm
	 *
	 * @param p_network
	 *            The neural network the algorithm will use
	 */
	@SuppressWarnings("unchecked")
	public PacmanGeneticAlgorithm(PacmanFeedForwardNeuralNetwork p_network)
	{
		super(p_network);
        System.out.println("Network: "+this.network);
		this.network.setActivationFunction(new ActivationFunctionSigmoid());
		this.population.addMember(this.network);
        for(int i = 0; i < this.population_size; i++){
            FeedForwardNeuralNetwork new_net = new FeedForwardNeuralNetwork(13, new int[] { 20 }, 1);
            this.population.addMember(new_net);
        }
	}

	/**
	 * Generates a new population
	 */
	@SuppressWarnings("unchecked")
	public void generatePopulation()
	{
        System.out.println("Current generation: "+this.current_epoch);
		Random rnd = new Random();
		FeedForwardNeuralNetwork member1;
		FeedForwardNeuralNetwork member2;
		Vector<FeedForwardNeuralNetwork> new_population = new Vector<FeedForwardNeuralNetwork>();
		// Fill population to it's maximum
		while (new_population.size() < this.population_size)
		{
			double value = (double) rnd.nextInt(100) / 100;
			member1 = this.selection_operator.select(this.population);
			member2 = this.selection_operator.select(this.population);
			// Crossover or copy
			/*if (value < this.crossover_rate)
			{
		        new_population.add(this.mutation_operator.mutate(this.crossover_operator.cross(member1, member2)));
			    new_population.add(this.mutation_operator.mutate(this.crossover_operator.cross(member1, member2)));
			}
			else
			{      */
            
				double value2 = Math.random();//double) rnd.nextInt(100)/100;
			    if(value2 <= this.mutation_rate){
                    new_population.add(this.mutation_operator.mutate(member1));
				    new_population.add(this.mutation_operator.mutate(member2));
			    }else{
				    new_population.add(member1);
				    new_population.add(member2);
				}
			//}
		}                                              
		this.population.setPopulation(new_population);
	}

	@Override
	protected void trainEpoch(Vector<Pattern> patterns)
	{
		this.population.test_set = patterns;
		generatePopulation();
		this.network = this.population.getEliteMember();
		this.current_epoch++;
		// return 1.0 - this.population.getEliteFitness();
	}
    
}
