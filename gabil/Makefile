S_HELPERS = genetics.cpp ga_variants.cpp
O_HELPERS = $(S_HELPERS:.cpp=.o)
HEADER = gabil.h

S_MAIN = main.cpp

CC = g++
CFLAGS = -g
LDFLAGS = 

main:		${S_MAIN} ${O_HELPERS}
		${CC} ${CFLAGS} ${O_HELPERS} ${S_MAIN} -o $@ ${LDFLAGS}

genetics.o:	${S_HELPERS} ${HEADER}
		${CC} ${CFLAGS} -c ${S_HELPERS} ${LDFLAGS}

clean:
		rm *.o
