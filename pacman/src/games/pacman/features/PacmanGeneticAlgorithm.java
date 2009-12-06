package games.pacman.features;

import java.util.Random;
import java.util.Vector;
import java.util.HashMap;

import neuralj.datasets.Pattern;
import neuralj.datasets.PatternSet;
import neuralj.networks.feedforward.*;
import neuralj.networks.feedforward.activation.ActivationFunctionSigmoid;
import neuralj.networks.feedforward.learning.FeedForwardNetworkLearningAlgorithm;
import neuralj.networks.feedforward.learning.genetic.crossover.CrossoverSinglePoint;
import neuralj.networks.feedforward.learning.genetic.crossover.ICrossoverOperator;
import neuralj.networks.feedforward.learning.genetic.mutation.IMutationOperator;
import neuralj.networks.feedforward.learning.genetic.mutation.MutationRandom;
import neuralj.networks.feedforward.learning.genetic.selection.ISelectionOperator;
import neuralj.networks.feedforward.learning.genetic.selection.RouletteWheel;
import neuralj.networks.feedforward.learning.genetic.selection.SelectionRouletteWheel;
import neuralj.networks.feedforward.learning.genetic.selection.RouletteSlice;
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
	public PacmanRouletteWheel		population					= new PacmanRouletteWheel();

	// The genetic algorithm's population size
	public int					population_size				= DEFAULT_POPULATION_SIZE;

	// Genetic algorithm selection operator
	public ISelectionOperator	selection_operator			= new PacmanSelectionRouletteWheel();

    // The previous weight update performed in each synapse
    private HashMap<Synapse, Double>	old_weight_update;

    public Double maximum_score;
    private Double current_score;
    private Vector<Double> maximum_score_weights;
    public int maximum_score_epoch;
    // The types of learning strategy employable
	public enum LearningStrategy
	{
		Memorize, Generalization, Optimization
	}
    // The types of learning strategy employed
	public LearningStrategy		learning_strategy	= LearningStrategy.Optimization;

    public Vector<Double> score_timeline = new Vector<Double>();

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
		Random rnd = new Random();
        int index;
		FeedForwardNeuralNetwork member;
        Vector<FeedForwardNeuralNetwork> old_population = this.population.getPopulation();
		Vector<FeedForwardNeuralNetwork> new_population = new Vector<FeedForwardNeuralNetwork>();

        // Fill half of the new population with the best of the old one...
        while (new_population.size() < this.population_size / 2)
            new_population.add(old_population.remove(0));

        // Then add a mutated copy of the best one...
        for (int i = 0; i < this.population_size / 2; i++)
            new_population.add(this.mutation_operator.mutate(new_population.get(i)));


			// Crossover or copy
			/*if (value < this.crossover_rate)
			{
		        new_population.add(this.mutation_operator.mutate(this.crossover_operator.cross(member1, member2)));
			    new_population.add(this.mutation_operator.mutate(this.crossover_operator.cross(member1, member2)));
			}
			else
			{
            
				double value2 = Math.random();//double) rnd.nextInt(100)/100;
			    if(value2 <= this.mutation_rate){
                    new_population.add(this.mutation_operator.mutate(member1));
				    new_population.add(this.mutation_operator.mutate(member2));
			    }else{
				    new_population.add(member1);
				    new_population.add(member2);
				}
			}*/
		this.population.setPopulation(new_population);
	}

    /**
	 * Resets all the data used by the learning algorithm
	 */
	private void reset()
	{
		this.current_epoch = 0;
        this.maximum_score = 0.;
        this.maximum_score_epoch = 0;
        this.maximum_score_weights = null;
        this.current_score = 0.;
		this.current_validation_error = Double.MAX_VALUE;
		this.current_training_error = Double.MAX_VALUE;
		this.minimum_training_error_epoch = 0;
		this.minimum_training_error = Double.MAX_VALUE;
		this.minimum_training_error_weights = null;
		this.minimum_validation_error_epoch = 0;
		this.minimum_validation_error = Double.MAX_VALUE;
		this.minimum_validation_error_weights = null;
		this.old_error_partial_derivative = new HashMap<Synapse, Double>();
		this.error_partial_derivative = new HashMap<Synapse, Double>();
		this.delta_weight = new HashMap<Synapse, Double>();
		this.old_weight_update = new HashMap<Synapse, Double>();
		this.weight_update = new HashMap<Synapse, Double>();
		for (NeuronLayer layer : this.network.neuron_layers)
			for (Neuron neuron : layer.neurons)
				for (Synapse synapse : neuron.outgoing_synapses)
				{
					this.weight_update.put(synapse, new Double(0));
					this.old_error_partial_derivative.put(synapse, new Double(0));
					this.error_partial_derivative.put(synapse, new Double(0));
					this.delta_weight.put(synapse, new Double(0));
				}
		resetPartialDerivatives();
	}

    @SuppressWarnings( { "unchecked", "unchecked" })
    protected boolean optimize() {
        reset();
        this.is_running = true;
        this.watcher.start();
        // Train while minimum error is not met and the maximum number of
        // steps is not exceeded
        while (this.current_epoch < this.maximum_epochs && this.is_running)
        {
            while (this.is_paused)
                ;
            this.current_score = trainEpochOptimization();

            System.out.println("Epoch " + this.current_epoch + " of " + this.maximum_epochs + " - Score: " + this.current_score);
            if (this.current_score > this.maximum_score) {
                this.maximum_score_weights = (Vector<Double>)this.network.getWeightVector().clone();
                this.maximum_score = this.current_score;
                this.maximum_score_epoch = this.current_epoch;
            }
            if (this.auto_pause)
                this.is_paused = true;
            this.watcher.monitor();
            this.score_timeline.add(this.current_score);
        }

        this.network.setWeightVector(this.maximum_score_weights);
        System.out.println("Best member fitness: " + PacmanFeedForwardNeuralNetwork.calcFitness(50,this.network));
        this.watcher.stop();
        this.is_running = false;
        return true;
    }

	protected Double trainEpochOptimization()
	{
		generatePopulation();
		this.network.setWeightVector((Vector<Double>)this.population.getEliteMember().getWeightVector().clone());
		this.current_epoch++;
		return this.population.getEliteFitness();
	}

    @Override
    protected void trainEpoch(Vector<Pattern> pattern)
    {
        generatePopulation();
        this.network = this.population.getEliteMember();
        this.current_epoch++;
        //return this.population.getEliteFitness();
    }

    @Override
    public void run()
    {
        this.is_running = true;
        if (this.pattern_set != null)
        {
            if (this.learning_strategy == LearningStrategy.Memorize)
                memorize(this.pattern_set.getShrunkPatterns(PatternSet.PatternType.All));
            else if (this.learning_strategy == LearningStrategy.Generalization)
                generalize(this.pattern_set.getShrunkPatterns(PatternSet.PatternType.Training), this.pattern_set.getShrunkPatterns(PatternSet.PatternType.Validation));
            else if (this.learning_strategy == LearningStrategy.Optimization)
                optimize();
        }
        this.is_running = false;
    }

    
}
