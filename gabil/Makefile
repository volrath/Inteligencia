S_HELPERS = genetics.cpp ga_variants.cpp
O_HELPERS = $(S_HELPERS:.cpp=.o)
HEADER = gabil.h

S_MAIN = main.cpp

CC = g++
CFLAGS = -g -pg -O3
LDFLAGS = 

main:		${S_MAIN} ${O_HELPERS}
		${CC} ${CFLAGS} ${O_HELPERS} ${S_MAIN} -o $@ ${LDFLAGS}

genetics.o:	${S_HELPERS} ${HEADER} encode
		${CC} ${CFLAGS} -c ${S_HELPERS} ${LDFLAGS}

encode:
		python data_parsing.py
		python data_parsing2.py
clean:
		rm *.o data/adult.bin main
