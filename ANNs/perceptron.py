#!/usr/bin/env python
from random import randrange

class Perceptron(object):
    def __init__(self, inputs):
        self.inputs = inputs + 1
        #self.weights = [randrange(-5,5)/100. for i in range(0,self.inputs)] # The first one is the bias
        self.weights = [0] * self.inputs

    def __repr__(self):
        return '<Weights: %s>' % self.weights

    def _alter_weight(self, inputs, target_result, learning_rate,
                      acc_error=None):
        """
        """
        delta = acc_error if acc_error else \
                target_result - self.evaluate(inputs)
        self.weights = [weight + learning_rate * delta * inp
                        for weight, inp in zip(self.weights, inputs + [1])]

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
        #pe = self._get_sum_error(training_set) if pre_error else None
        for inputs, target_result in training_set:
            error += (target_result - self.evaluate(inputs))**2
        if pre_error:
            evals = []
            for inputs, target_result in training_set:
                evals.append(self.evaluate(inputs))
            for i, weight in enumerate(self.weights):
                delta = 0
                for j,(inputs, target_result) in enumerate(training_set):
                    inp = inputs + [1]
                    delta += (target_result - evals[j])*inp[i]
                self.weights[i] += learning_rate*delta
        else:
            for inputs, target_result in training_set:
                self._alter_weight(inputs, target_result, learning_rate, None)
        return error / 2.

    def evaluate(self, inputs):
        """
        Evaluates the output of the perceptron for the given input
        """
        if len(inputs) != (self.inputs - 1):
            raise ValueError("evaluate needs exactly %s inputs" % \
                             (self.inputs - 1))
        inp = map(int, inputs) + [1]
        #return 1 if sum([w * x for w,x in zip(self.weights,inp)]) > 0 else 0
        return sum([w * x for w, x in zip(self.weights, inp)])

class BooleanPerceptron(Perceptron):
    def _alter_weight(self, inputs, target_result, learning_rate):
        delta = target_result - self.evaluate(inputs)
        self.weights = [weight + learning_rate * delta * inp
                        for weight, inp in zip(self.weights, inputs+[1])]

    def evaluate(self, inputs):
        return int(super(BooleanPerceptron, self).evaluate(inputs) > 0)

def training(perceptron, training_set, learning_rate=0.1, reduce_rate=False,
             standard_gradient_descent=False, max_iterations=31000):
    """
    Trains the perceptron with the given training set over and over until
    the error converge to 0
    """
    it = 0; log = []
    try:
        while it < max_iterations:
            log.append(perceptron.train(training_set, learning_rate,
                                        pre_error=standard_gradient_descent))
            print 'Iteration %s - Error: %s' % (it, log[it])
            if not log[it]:
                break
            it += 1
            learning_rate = learning_rate / it if reduce_rate else learning_rate
            print perceptron.weights
    except KeyboardInterrupt:
        pass
    test(perceptron, training_set)
    return log

def test(perceptron, training_set):
    total = 0; errors = 0; zeros = 0
    print
    for inputs, target_results in training_set:
        results = perceptron.evaluate(inputs)
        tresult = 1 if results >= .5 else 0
        print 'Inputs: %s -> Result: %s [Wanted %s]' % (inputs, tresult, target_results)
        if target_results != tresult:
            errors +=1
        if tresult == 0:
            zeros += 1
        total += 1
    print
    print 'TOTAL: %s ERRORS: %s | FAILURE: %s %% | ZEROS: %s' % (total, errors, errors * 100. / total, zeros)

def plot(log):
    """
    A matplotlib way to plot the training log
    """
    import matplotlib.pyplot as plt
    plt.plot(log)
    plt.savefig("new.png")
    #plt.show()

and_training_set = [
    ([0,0], 0),
    ([0,1], 0),
    ([1,0], 0),    
    ([1,1], 1),
]
or_training_set = [
    ([0,0], 0),
    ([0,1], 1),
    ([1,0], 1),    
    ([1,1], 1),
]
xor_training_set = [
    ([0,0], 0),
    ([0,1], 1),
    ([1,0], 1),
    ([1,1], 0),
]
if __name__ == '__main__':    
    plot(training(Perceptron(2), and_training_set, learning_rate=0.3, reduce_rate=False,
                  standard_gradient_descent=False, max_iterations=1000))
