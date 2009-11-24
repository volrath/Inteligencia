#!/usr/bin/env python

from pylab import *
from operator import itemgetter
import matplotlib.pyplot as plt

def main():
    """
    """
    f = open('data/adult.data', 'r')
    data = [line.split() for line in f]
    f.close()

    plt.figure(1)

    xlabel(r"ages", fontsize=12)
    ylabel(r"evaluation", fontsize=12)
    grid(True)
    rich_people = [0] * 100
    for age, rich in map(itemgetter(0,14), data):
        rich_people[int(age)] += 1 if rich == '>50K' else 0
    plt.plot(rich_people)
    plt.savefig('media/concentrations_age.png')
    #plt.show()
    plt.clf()

    xlabel(r"fnlwgts", fontsize=12)
    ylabel(r"evaluation", fontsize=12)
    grid(True)
    rich_people = [0] * 1484706
    for fnlwgts, rich in map(itemgetter(2,14), data):
        rich_people[int(fnlwgts)] += 1 if rich == '>50K' else 0
    plt.plot(rich_people)
    plt.savefig('media/concentrations_fnlwgts.png')
    #plt.show()
    plt.clf()

    xlabel(r"education", fontsize=12)
    ylabel(r"evaluation", fontsize=12)
    grid(True)
    rich_people = [0] * 17
    for educ, rich in map(itemgetter(4,14), data):
        rich_people[int(educ)] += 1 if rich == '>50K' else 0
    plt.plot(rich_people)
    plt.savefig('media/concentrations_educ.png')
    #plt.show()
    plt.clf()

    xlabel(r"gain", fontsize=12)
    ylabel(r"evaluation", fontsize=12)
    grid(True)
    rich_people = [0] * 100000
    for gain, rich in map(itemgetter(10,14), data):
        rich_people[int(gain)] += 1 if rich == '>50K' else 0
    plt.plot(rich_people)
    plt.savefig('media/concentrations_gain.png')
    #plt.show()
    plt.clf()

    xlabel(r"loss", fontsize=12)
    ylabel(r"evaluation", fontsize=12)
    grid(True)
    rich_people = [0] * 4357
    for loss, rich in map(itemgetter(11,14), data):
        rich_people[int(loss)] += 1 if rich == '>50K' else 0
    plt.plot(rich_people)
    plt.savefig('media/concentrations_loss.png')
    #plt.show()
    plt.clf()

    xlabel(r"hpw", fontsize=12)
    ylabel(r"evaluation", fontsize=12)
    grid(True)
    rich_people = [0] * 100
    for hpw, rich in map(itemgetter(12,14), data):
        rich_people[int(hpw)] += 1 if rich == '>50K' else 0
    plt.plot(rich_people)
    plt.savefig('media/concentrations_hpw.png')
    #plt.show()
    plt.clf()

if __name__ == '__main__': main()
