#!/usr/bin/env python
from random import uniform
if __name__ == '__main__':
    points = []
    pA = 0
    pB = 0
    x = 0.
    y = 0.
    size_set = 500
    while 1:
        if(pA == size_set/2 and pB == size_set/2):
            break
        x = uniform(0,20)
        y = uniform(0,12)
        if(((x - 15)**2+(y - 6)**2) <= 9):
            if(pB < size_set/2):
                pB += 1
                points.append(([float(x),float(y)],'B'))
        else:
            if(pA < size_set/2):
                pA += 1
                points.append(([float(x),float(y)],'A'))
    for (pos,area) in points:
        print '%.6f %.6f %s'%(pos[0],pos[1],area)
