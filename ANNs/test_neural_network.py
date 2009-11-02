#!/usr/bin/env python

from sys import maxint
import matplotlib.pyplot as plt

from neural_network import NeuralNetwork, load_training_set, training, test, \
     get_random_set, plot

training_sets = [
#    ('bp_training/500.txt',   '500'), 
    ('bp_training/1000.txt',  '1000'), 
    ('bp_training/2000.txt',  '2000'), 
    ('bp_training/500e.txt',  '500e'), 
    ('bp_training/1000e.txt', '1000e'), 
    ('bp_training/2000e.txt', '2000e'), 
]

for training_set, ts_name in training_sets:
    training_set = load_training_set(training_set)
    print '.. begining %s' % ts_name
    lowest_error = maxint
    for hs in range(2, 11):
        print '      %s hidden neurons' % hs
        nn = NeuralNetwork(2, hs, 1)
        error_log = training(nn, training_set, max_iterations=2000)
        test_log, total_error = test(nn, get_random_set(10000))
        if total_error < lowest_error:
            best_nn = nn
            best_hs = hs
            best_error_log = error_log
            best_test_log = test_log
            lowest_error = total_error
    print '   lowest error on %s hidden neurons network: %s failures' % (hs, lowest_error)
    plot(best_error_log, best_test_log, save='media/%s-%s-best-performance.png' % (ts_name, hs))
