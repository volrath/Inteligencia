#include "lib.h"
#include <cmath>

pt_hash_t pdb05, pdb610, pdb1115;

unsigned misplaced_tiles(state15_t state) { int mt = ZERO; for (int i = ZERO; i < NUM_TILES; i++) { if (state.cont(i) != i) mt++; }; return(mt); }

unsigned manhattan(state15_t state) { 
  int mh = ZERO; 
  for (int i = ZERO; i < NUM_TILES; i++) { 
    mh = mh + (abs(state.cont(i)%4 - i%4) + abs(state.cont(i)/4 - i/4)); 
  }; 
  return(mh);
}

unsigned pdb_heuristic(state15_t state) {
  pattern_t pt;
  pdb_gen05(state, &pt);
  pt_hash_t::iterator it05 = ::pdb05.find(pt);
//   pdb_gen610(state, &pt);
//   pt_hash_t::iterator it610 = ::pdb610.find(pt);
  pdb_gen1115(state, &pt);
  pt_hash_t::iterator it1115 = ::pdb1115.find(pt);
  return (unsigned)(it05->second + it1115->second);
}
