#include "lib.h"
#include <cmath>

pt_hash_t pdb05, pdb1015;

unsigned misplaced_tiles(state15_t state) { int mt = ZERO; for (int i = ZERO; i < NUM_TILES; i++) { if (state.cont(i) != i) mt++; }; return(mt); }

unsigned manhattan(state15_t state) { int mh = ZERO; for (int i = ZERO; i < NUM_TILES; i++) { mh = mh + (abs(state.cont(i)%4 - i%4) + abs(state.cont(i)/4 - i/4)); } }

unsigned pdb_heuristic(state15_t state) {
  pattern_t pt; pdb_gen05(state, &pt);
  pt_hash_t::iterator it = ::pdb05.find(pt);
  // Do the same thing for the 1015 database and..
  return (unsigned)it->second;
}
