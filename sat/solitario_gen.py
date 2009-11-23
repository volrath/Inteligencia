#!/usr/bin/env python
# encoding: utf-8
"""
solitario.py

Created by Carlos Castillo on 2009-06-25.
Copyright (c) 2009 . All rights reserved.
"""

import sys
import os
import random

def main(n):
    print n
    
    for i in range(n):
        for j in range(n):
            pq = random.randint(0,8)
            if pq==1:
                print pq,"",
            elif pq==2:
                print pq,"",
            else:
                print "0 ",
        print

if __name__ == '__main__':
	main(int(sys.argv[1]))

