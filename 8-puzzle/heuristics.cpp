#include "lib.h"
#include <cmath>

unsigned misplaced_tiles(state8_t state) { int mt = 0; for (int i = 0; i < 9; i++) { if (state.cont(i) != i) mt++; }; return(mt); }

unsigned manhattan(state8_t state) { int mh = 0; for (int i = 0; i < 9; i++) { mh = mh + (abs(state.cont(i)%3 - i%3) + abs(state.cont(i)/3 - i/3)); } }
