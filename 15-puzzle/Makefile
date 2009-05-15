S_HELPERS = search.cpp heuristics.cpp
O_HELPERS = $(S_HELPERS:.cpp=.o)
HEADER = lib.h

CC = g++
CFLAGS = -g -o
NO_DEPRCT = -Wno-deprecated

15puzz:	15puzzle.cpp ${O_HELPERS}
	$(CC) $(NO_DEPRCT) $(CFLAGS) 15puzzle ${O_HELPERS} 15puzzle.cpp

helpers:$(S_HELPERS)
	$(CC) -c $(CFLAGS) $(NO_DEPRCT) ${S_HELPERS}
clean:
	rm 15puzzle *.o
