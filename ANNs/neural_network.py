#!/usr/bin/env python

from math import exp
from random import randrange, uniform
import matplotlib.pyplot as plt

from perceptron import Perceptron, and_training_set, xor_training_set, \
     or_training_set

class SigmoidNeuron(Perceptron):
    """
    A sigmoid perceptron =S
    """
    def __init__(self, inputs):
        """
        Initializes all the weights in small random numbers
        between -.05 and .05
        """
        super(SigmoidNeuron, self).__init__(inputs)
        self.weights = [weight + randrange(-5,5) / 100.
                        for weight in self.weights]
        self.last_evaluation = None

    def evaluate(self, inputs):
        """
        Evaluates the Perceptron output according to the sigmoid function
        """
        self.inputs = inputs
        self.last_evaluation = 1. / (1 + exp(-sum([w * x for w, x in zip(self.weights, inputs + [1])])))
        return self.last_evaluation


class NeuralNetwork(object):
    def __init__(self, inputs, hidden, outputs):
        """
        3 attributes:
          + inputs_number: just a number of inputs
          + hidden: a list of hidden SigmoidNeuron's
          + outputs: a list of output SigmoidNeuron's
        """
        self.inputs_number = inputs
        self.hidden = [SigmoidNeuron(inputs) for i in range(hidden)]
        self.outputs = [SigmoidNeuron(hidden) for i in range(outputs)]

    def get_error(self, training_set):
        """
        The addition of all the errors for all the outputs in all the examples.
        assumes at least one previous evaluation.
        """
        error = 0
        for inputs, target_results in training_set:
            self.evaluate(inputs);
            for target_result,op in zip(target_results,self.outputs):                
                error += ((target_result - op.last_evaluation)**2)
                
        return error / 2.

    def evaluate(self, inputs):
        """
        Propagates forward the inputs through the network and return the
        result in a list
        """
        return [op.evaluate([hp.evaluate(inputs) for hp in self.hidden])
                for op in self.outputs]

    def backpropagation_train(self, training_set, learning_rate=0.05):
        """
        """
        error = 0
        for inputs, target_results in training_set:
            output_results = self.evaluate(inputs)
            dos = []
            for i, op in enumerate(self.outputs):
                dos.append(op.last_evaluation * (1 - op.last_evaluation) * \
                           (target_results[i] - op.last_evaluation))
                op.weights = [weight + (learning_rate * inp * dos[i])
                              for weight, inp in zip(op.weights, op.inputs + [1])]

            for i, hp in enumerate(self.hidden):
                dh = hp.last_evaluation * (1 - hp.last_evaluation) * \
                     sum([op.weights[i] * dos[j]
                          for j, op in enumerate(self.outputs)])
                hp.weights = [weight + (learning_rate * inp * dh)
                              for weight, inp in zip(hp.weights, hp.inputs + [1])]
            # calculates error
            error += sum([(target_result - op.last_evaluation)**2
                          for target_result, op in zip(target_results, self.outputs)])
        return error / 2.


def training(neural_network, training_set, learning_rate=.1,
             max_iterations=1000, reduce_rate=False):
    """
    """
    it = 0; log = []
    try:
        while it < max_iterations or (not max_iterations):
            log.append(neural_network.backpropagation_train(training_set, learning_rate))
            if (it + 1) % 500 == 0:
                print '        Iteration %s' % (it + 1)
            if not log[it]:
                break
            it += 1
            learning_rate = learning_rate / it if reduce_rate else learning_rate
    except KeyboardInterrupt:
        pass
    return log

def test(neural_network, test_set):
    """
    Returns what the neural network think is in the rectangle and what it thinks
    is on the circle.
    ([dots on the rectangle], [dots on the circle])
    """
    total = 0; errors = 0; zeros = 0
    rect_results = ([],[]); circle_results = ([],[])
    for inputs, target_results in test_set:
        results = neural_network.evaluate(inputs)
        if results[0] >= .5:
            tresult = 1
            rect_results[0].append(inputs[0])
            rect_results[1].append(inputs[1])
        else:
            tresult = 0
            circle_results[0].append(inputs[0])
            circle_results[1].append(inputs[1])
        if target_results[0] != tresult:
            errors +=1
        if tresult == 0:
            zeros += 1
        total += 1
    print
    #print 'TOTAL: %s ERRORS: %s | FAILURE: %s %% | ZEROS: %s' % (total, errors, errors * 100. / total, zeros)
    return (rect_results, circle_results), errors

def get_random_set(set_size):
    points = [(uniform(0,20),uniform(0,10)) for i in range(0,set_size)]
    random_set = []
    for (x,y) in points:
        if(((x - 15)**2+(y - 6)**2) <= 9):
            random_set.append(([float(x),float(y)],[0]))
        else:
            random_set.append(([float(x),float(y)],[1]))
    return random_set

def load_training_set(file_name):
    """
    Loads the training set in memory, returns
    [([inputs], [results]), ... , ()]
    """
    print 'loading training file \'%s\'' % file_name
    f = open(file_name)
    training_set = []
    for line in f:
        p1, p2, area = line.split()
        training_set.append(([float(p1), float(p2)], [int(area == 'A')]))
    f.close()
    return training_set

def plot(error_log, test_log, save=None):
    """
    Makes the plot of the error and the result of the learned points
    """
    plt.figure(1)
    plt.subplot(2,1,1)
    plt.plot(error_log)

    plt.subplot(2,1,2)
    plt.axis('equal')
    rect_results, circle_results = test_log
    plt.plot(rect_results[0], rect_results[1], 'b+')
    plt.plot(circle_results[0], circle_results[1], 'ro')

    if save:
        plt.savefig(save)
        plt.clf()
    else:
        plt.show()

if __name__ =='__main__':
    nn = NeuralNetwork(2,10,1)
    training_set = load_training_set('bp_training/500.txt') # [(inputs, [target_result]) for inputs, target_result in xor_training_set], #
    error_log = training(nn, training_set, max_iterations=2500)
    test_log, total_errors = test(nn, get_random_set(10000))
    plot(error_log, test_log)
