S_HELPERS = heuristics.cpp search.cpp
O_HELPERS = $(S_HELPERS:.cpp=.o)
HEADER = lib.h

S_MAIN = 15puzzle.cpp
S_PDB  = pdb-generator.cc

CC = g++
CFLAGS = -g -o
NO_DEPRCT = -Wno-deprecated

all:		15puzzle pdb-generator

15puzzle:	${S_MAIN} ${O_HELPERS}
		$(CC) $(NO_DEPRCT) $(CFLAGS) $@ ${O_HELPERS} ${S_MAIN}

pdb-generator:	${S_PDB} ${O_HELPERS}
		$(CC) $(NO_DEPRCT) $(CFLAGS) $@ ${O_HELPERS} ${S_PDB}

helpers:	$(S_HELPERS)
		$(CC) -c $(CFLAGS) $(NO_DEPRCT) ${S_HELPERS}

clean:
		rm *.o
