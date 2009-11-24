#!/usr/bin/env python

from os import remove, error, path
from commands import getoutput
import matplotlib.pyplot as plt

POP_SIZE = 200
MUTATE_CHANCEs = [.02, .06, .1]
NEW_CHILDREN_PERCs = [.4, .6, .9]

def plot_one(output, mc, ncp):
    plt.figure(1)
    plt.xlabel('Numero de poblaciones')
    plt.ylabel('Fitness')
    plt.grid(True)
    plt.plot([fitness for fitness in output[:-1]], label='mc: %s ncp: %s' % (mc, ncp))
    plt.legend()
    plt.savefig('media/%s-%s-%s.png' % (POP_SIZE, mc, ncp))
    plt.clf()

def plot_pop(outputs):
    print
    print 'Plotting \'em all..'
    plt.figure(1)
    plt.xlabel('Numero de poblaciones')
    plt.ylabel('Fitness')
    plt.grid(True)
    for label, output in outputs.iteritems():
        plt.plot([fitness for fitness in output[:-1]], label=label)
    plt.legend()
    plt.savefig('media/all.png')
    plt.clf()

def main():
    i = 0
    outputs = {}
    for mc, ncp in [(x,y) for x in MUTATE_CHANCEs for y in NEW_CHILDREN_PERCs]:
        i += 1
        print '[%s] plotting ./main %s %s %s' % (i, POP_SIZE, mc, ncp)
        results = []
        for j in range(10):
            print '      ', j
            results.append(getoutput('./main %s %s %s' % (POP_SIZE, mc, ncp)).split())
        result = average(results)
        outputs.update({'mc: %s ncp: %s' % (mc, ncp): result})
        plot_one(result, mc, ncp)
    plot_pop(outputs)
    save_bf(outputs)

def average(results):
    result = []
    for i in range(len(results[0])):
        result.append(sum([float(r[i]) for r in results]) / float(len(results[0]) - 1.))
    return result

def save_bf(outputs):
    print
    print 'Writting \'em all..'
    f = open('best_fitness.log', 'w')
    for label, output in outputs.iteritems():
        f.write('%s -> %s\n' % (label, output[-1]))
    f.close()

if __name__ == '__main__':
    main()
