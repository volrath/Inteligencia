#!/usr/bin/env python

from sys import maxint
from operator import itemgetter
import numpy.numarray as na
import matplotlib.pyplot as plt

from neural_network import NeuralNetwork, load_training_set, training, test, \
     get_random_set, plot

training_sets = [
    ('bp_training/500.txt',   '500'), 
    ('bp_training/1000.txt',  '1000'), 
    ('bp_training/2000.txt',  '2000'), 
#    ('bp_training/500e.txt',  '500e'), 
#    ('bp_training/1000e.txt', '1000e'), 
#    ('bp_training/2000e.txt', '2000e'), 
]

def bar_(points, file_name):
    plt.figure(1)
    plt.xlabel('Numero de neuronas')
    plt.ylabel('Errores en barrido de 10000 puntos')
    plt.grid(True)
    labels = map(str, range(2,11))
    xlocations = na.array(range(len(points))) + .5
    width = .5
    plt.bar(xlocations, points, width=width)
    plt.xticks(xlocations + width/2, labels)
    plt.savefig(file_name)
    plt.clf()

log_file = open('backpropagation.log', 'w')

for training_set, ts_name in training_sets:
    training_set = load_training_set(training_set)
    print '.. begining %s' % ts_name
    log_file.write('.. begining %s\n' % ts_name)
    lowest_error = maxint
    total_error_log = []
    for hs in range(2, 11):
        print '      %s hidden neurons' % hs
        nn = NeuralNetwork(2, hs, 1)
        error_log = training(nn, training_set, max_iterations=2000)
        test_log, total_error = test(nn, get_random_set(10000))
        log_file.write('%s neurons: %s errors \nhidden:  %s\noutputs: %s\n\n' % (hs, total_error, [hn.weights for hn in nn.hidden], [on.weights for on in nn.outputs]))
        total_error_log.append((hs, total_error, error_log, test_log))
    best = min(total_error_log, key=itemgetter(1))
    print '   lowest error on %s hidden neurons network: %s failures' % (best[0], best[1])
    plot(best[2], best[3], save='media/%s-%s-best-performance.png' % (ts_name, best[0]))
    bar_plot(map(itemgetter(1), total_error_log), 'media/error-derivation-%s' % ts_name)

log_file.close()
