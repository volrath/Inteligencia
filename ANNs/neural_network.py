from math import exp
from random import random

from perceptron import Perceptron, plot

class SigmoidPerceptron(Perceptron):
    """
    A sigmoid perceptron =S
    """
    def __init__(self, inputs):
        """
        Initializes all the weights in small random numbers
        between -.05 and .05
        """
        super(SigmoidPerceptron, self).__init__()
        self.weights = [weight + randrange(-5,5) / 100.
                        for weight in self.weights]
        self.last_evaluation = None

    def evaluate(self, inputs):
        """
        Evaluates the Perceptron output according to the sigmoid function
        """
        self.inputs = inputs
        self.last_evaluation = 1. / (1 + exp(super(SigmoidPerceptron, self).evaluate(inputs)))
        return self.last_evaluation


class NeuralNetwork(object):
    def __init__(self, inputs, hidden, outputs):
        """
        3 attributes:
          + inputs_number: just a number of inputs
          + hidden: a list of hidden SigmoidPerceptron's
          + outputs: a list of output SigmoidPerceptron's
        """
        self.inputs_number = inputs
        self.hidden = [SigmoidPerceptron(inputs) for i in range(hidden)]
        self.outputs = [SigmoidPerceptron(hidden) for i in range(outputs)]

    # Check this!
    def _get_error(self, training_set):
        """
        The addition of all the errors for all the outputs in all the examples.
        """
        error = 0
        for inputs, target_results in training_set:
            output_results = self.evaluate(inputs)
            error += sum([(target_result - output_result)
                          for target_result, output_result
                          in zip(target_results, output_results)])
        return error

    def evaluate(self, inputs):
        """
        Propagates forward the inputs through the network and return the
        result in a list
        """
        results = []
        for op in self.outputs:
            results.append(op.evaluate([hp.evaluate(inputs) for hp in self.hidden]))
        return results

    def backpropagation_train(self, training_set, learning_rate=0.05):
        """
        """
        for inputs, target_results in training_set:
            output_results = self.evaluate(inputs)
            delta_outputs = [op.last_evaluation * (1 - op.last_evaluation) * \
                             (target_results[i] - op.last_evaluation)
                             for i, op in enumerate(self.outputs)]
            for i, op in enumerate(self.outputs):
                op.weights = [weight + learning_rate * inp * delta_outputs[i]
                              for weight, inp in zip(op.weights, op.inputs)]

            # Fuck, too much pasta-code
            delta_hidden = [hp.last_evaluation * (1 - hp.last_evaluation) * \
                            sum([0])
                            for i, hp in enumerate(self.hidden)]

def training(neural_network, training_set, learning_rate=.01,
             max_iterations=10000):
    """
    """
    it = 0; log = []
    while it < max_iterations:
        log.append(neural_network.backpropagation_train(training_set,
                                                        learning_rate))
        print 'Iteration %s - Error: %s' % (it, log[it])
        if not log[it]:
            break
        it += 1
    return log

def load_training_set(file_name):
    """
    Loads the training set in memory, returns
    [([inputs], [results]), ... , ()]
    """
    f = open(file_name)

if __name__ =='__main__':
    plot(training(NeuralNetwork(2,5,1), load_training_set('bp_training/500.txt')))
