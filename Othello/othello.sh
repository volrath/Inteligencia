#!/bin/bash

OT2="0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 2 0 0 0 0 2 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0"

for i in $(seq 1 20); do echo $OT2; OT1=`./othello2 ${OT2}`; echo $OT1; OT2=`./othello1 ${OT1}`; done