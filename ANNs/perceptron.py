#!/usr/bin/env python

class Perceptron(object):
    def __init__(self, inputs):
        self.inputs = inputs + 1
        self.weights = [0] * self.inputs # The first one is the bias

    def __repr__(self):
        return '<Weights: %s>' % self.weights

    def _alter_weight(self, inputs, target_result, learning_rate,
                      acc_error=None):
        """
        """
        delta = acc_error if acc_error else \
                target_result - self.evaluate(inputs)
        self.weights = [weight + learning_rate * delta * inp
                        for weight, inp in zip(self.weights, [1] + inputs)]

    def _get_sum_error(self, training_set):
        """
        Gets the current error for a given `training_set`
        """
        return sum([target_result - self.evaluate(inputs)
                    for inputs, target_result in training_set])

    def train(self, training_set, learning_rate, pre_error=False):
        """
        Execute the training with all the examples in the training set
        """
        error = 0
        pe = self._get_sum_error(training_set) if pre_error else None
        for inputs, target_result in training_set:
            error += (target_result - self.evaluate(inputs))**2
            self._alter_weight(inputs, target_result, learning_rate, pe)
        return error / 2.

    def evaluate(self, inputs):
        """
        Evaluates the output of the perceptron for the given input
        """
        if len(inputs) != (self.inputs - 1):
            raise ValueError("evaluate needs exactly %s inputs" % \
                             (self.inputs - 1))
        inp = [1] + map(int, inputs)
        #return 1 if sum([w * x for w in self.weights for x in inp]) > 0 else -1
        return sum([w * x for w, x in zip(self.weights, inp)])

class BooleanPerceptron(Perceptron):
    def _alter_weight(self, inputs, target_result, learning_rate):
        delta = target_result - self.evaluate(inputs)
        self.weights = [weight + learning_rate * delta * inp
                        for weight, inp in zip(self.weights, [1] + inputs)]

    def evaluate(self, inputs):
        return int(super(BooleanPerceptron, self).evaluate(inputs) > 0)

def training(perceptron, training_set, learning_rate=0.01, reduce_rate=False,
             standard_gradient_descent=False, max_iterations=31000):
    """
    Trains the perceptron with the given training set over and over until
    the error converge to 0
    """
    it = 0; log = []
    while it < max_iterations:
        log.append(perceptron.train(training_set, learning_rate,
                                    pre_error=standard_gradient_descent))
        print 'Iteration %s - Error: %s' % (it, log[it])
        if not log[it]:
            break
        it += 1
        learning_rate = learning_rate / it if reduce_rate else learning_rate
    print perceptron.weights
    return log

def plot(log):
    """
    A matplotlib way to plot the training log
    """
    import matplotlib.pyplot as plt
    plt.plot(log)
    plt.show()

and_training_set = [
    ([1,1], 1),
    ([1,0], 0),
    ([0,1], 0),
    ([0,0], 0),
]
or_training_set = [
    ([1,1], 1),
    ([1,0], 1),
    ([0,1], 1),
    ([0,0], 0),
]
xor_training_set = [
    ([1,1], 0),
    ([1,0], 1),
    ([0,1], 1),
    ([0,0], 0),
]
if __name__ == '__main__':
    plot(training(Perceptron(2), and_training_set, reduce_rate=False,
                  standard_gradient_descent=False, max_iterations=5000))
