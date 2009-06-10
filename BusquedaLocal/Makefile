CC = gcc
CFLAGS = -w -lm -L. -lfia1

all: algoritmomodif annealing hillclimbing genetic

algoritmomodif:	
	$(CC) $(CFLAGS) algoritmomodif.c -o  algoritmomodif

annealing:	
	$(CC) $(CFLAGS) annealing.c -o annealing

hillclimbing:	
	$(CC) $(CFLAGS) hillclimbing.c -o hillclimbing

genetic:	
	$(CC) $(CFLAGS) genetic.c -o genetic

clean:
	rm algoritmomodif annealing hillclimbing genetic
