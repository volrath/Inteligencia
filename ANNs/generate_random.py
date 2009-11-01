#!/usr/bin/env python
from random import uniform
if __name__ == '__main__':
    points = [(uniform(0,20),uniform(0,10)) for i in range(0,2000)]
    for (x,y) in points:
        if(((x - 15)**2+(y - 6)**2) <= 9):
            print '%.6f %.6f B'%(x,y)
        else:
            print '%.6f %.6f A'%(x,y)
            
        
