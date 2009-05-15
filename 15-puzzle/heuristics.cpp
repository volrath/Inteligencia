#include "lib.h"
#include <cmath>

unsigned misplaced_tiles(state15_t state) { int mt = ZERO; for (int i = ZERO; i < NUM_TILES; i++) { if (state.cont(i) != i) mt++; }; return(mt); }

unsigned manhattan(state15_t state) { int mh = ZERO; for (int i = ZERO; i < NUM_TILES; i++) { mh = mh + (abs(state.cont(i)%4 - i%4) + abs(state.cont(i)/4 - i/4)); } }
