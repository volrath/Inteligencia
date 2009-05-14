S_HELPERS = search.cpp heuristics.cpp
O_HELPERS = $(S_HELPERS:.cpp=.o)
HEADER = lib.h

CC = g++
CFLAGS = -g -o
NO_DEPRCT = -Wno-deprecated

8puzz:	8puzzle.cpp ${O_HELPERS}
	$(CC) $(NO_DEPRCT) $(CFLAGS) 8puzzle ${O_HELPERS} 8puzzle.cpp

helpers:
	gcc -c $(NO_DEPRCT) ${S_HELPERS}
clean:
	rm 8puzzle *.o
