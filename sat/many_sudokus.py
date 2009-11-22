#!/usr/bin/python/
import sys
from commands import getoutput

sudoku_hard = open(sys.argv[1], 'r')
statistics  = open('sudoku-hard-statistics', 'w')


for instance in sudoku_hard:
    statistics.write('-' * 91 + '\n')
    statistics.write('instance: %s' % instance)
    statistics.write(getoutput('./sudoku %s' % instance[:-1]))
    statistics.write('\n\n\n')
