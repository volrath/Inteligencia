#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/stat.h>
#include <string.h>
#include <math.h>
#include "ia1.h"

#define NUMEVAL 500000000

double evaluar(double * x){
  int r;
  double z;
  if ( (r = ia1("0538675", x, &z)) ){
    fprintf(stderr, "hubo un error: %d\n", r);
    exit(EXIT_FAILURE);
  }
  return z;
};
