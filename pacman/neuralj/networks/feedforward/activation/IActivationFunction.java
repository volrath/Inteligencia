package neuralj.networks.feedforward.activation;

public interface IActivationFunction {

	public double calculate(double value);

	public double calculateDerivate(double value);
	
}