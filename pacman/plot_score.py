#!/usr/bin/env python

import re
import sys
from os import path
import matplotlib.pyplot as plt

LOGS_PATH = 'logs'

def plot_em(log):
    """
    """
    log_path = path.join(LOGS_PATH, log)
    if not path.isfile(log_path):
        print 'No se encontro el archivo', log_path
        sys.exit(0)

    f = open(log_path, 'r')
    scores, _, result = f
    m = re.match(r"Maximum: (\d+\.\d+) at (\d+) epochs...", result)
    f.close()

    plt.figure(1)
    plt.xlabel('Generaciones')
    plt.ylabel('Score obtenido')
    plt.grid(True)
    plt.plot(map(float, scores.split()), 'g', label='%s @ %s' % m.groups())
    plt.plot([int(m.group(2))], [float(m.group(1))], 'ro')
    plt.plot()
    plt.legend()
    plt.savefig(log_path.split('.')[0] + '.png')
    plt.clf

if __name__ == '__main__':
    plot_em(sys.argv[1])
