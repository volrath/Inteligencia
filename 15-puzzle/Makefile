S_HELPERS = search.cpp heuristics.cpp
O_HELPERS = $(S_HELPERS:.cpp=.o)
HEADER = lib.h

S_MAIN = pdb-generator.cc

CC = g++
CFLAGS = -g -o
NO_DEPRCT = -Wno-deprecated

15puzz:	${S_MAIN} ${O_HELPERS}
	$(CC) $(NO_DEPRCT) $(CFLAGS) $@ ${O_HELPERS} ${S_MAIN}

helpers:$(S_HELPERS)
	$(CC) -c $(CFLAGS) $(NO_DEPRCT) ${S_HELPERS}
clean:
	rm *.o
