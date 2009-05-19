#include <ext/hash_map>
#include "lib.h"
#include <stdio.h>
#include <stdlib.h>

using namespace std;

namespace __gnu_cxx {
  template<> class hash<state15_t> {
  public:
    size_t operator()( const state15_t &s ) const { return(s.p1_^s.p2_); }
  };
};

class hash_t : public __gnu_cxx::hash_map<state15_t, node_t> { };  // class

void pdb_gen18(state15_t state, unsigned *pt1, unsigned *pt2) {
  *pt1 = ZERO; *pt2 = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    *pt1 = *pt1 << 4; *pt2 = *pt2 << 4;
    *pt1 = *pt1 + (state.cont(i) > HALF_NUM_TILES ? ZERO : state.cont(i));
    *pt2 = *pt2 + (state.cont(i+HALF_NUM_TILES) > HALF_NUM_TILES ? ZERO : state.cont(i+HALF_NUM_TILES));
  }
}

void pdb_gen915(state15_t state, unsigned *pt1, unsigned *pt2) {
  *pt1 = ZERO; *pt2 = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    *pt1 = *pt1 << 4; *pt2 = *pt2 << 4;
    *pt1 = *pt1 + (state.cont(i) <= HALF_NUM_TILES ? ZERO : state.cont(i));
    *pt2 = *pt2 + (state.cont(i+HALF_NUM_TILES) <= HALF_NUM_TILES ? ZERO : state.cont(i+HALF_NUM_TILES));
  }
}

int construct_initial(char ** input, state15_t *state) {
  int nums[NUM_TILES];
  for (int i = ZERO; i < NUM_TILES; i++) {
    nums[i] = atoi(input[i+1]);
    if (nums[i] < 0 || nums[i] > 15)
      return(0);
  }
  state->set_state(nums);
  return(1);
}

int pdb_bfs(state15_t *state, int cost, hash_t *closed) {
  // Insert this state into the pattern database
  unsigned int pt_state1 = ZERO, pt_state2 = ZERO;
  pdb_gen18(*state, &pt_state1, &pt_state2);
  cout << pt_state1 << pt_state2 << endl;
  // ...

  state15_t *scs[BR];
  state->successors(scs);
  for (int i = 0; scs[i] != NULL; i++) {
    if (!closed->count(*scs[i])) {
      closed->insert(make_pair(*scs[i], cost));
      pdb_bfs(scs[i], cost + 1, closed);
    }
  }

  free(scs);
  return(0);
}

void print(unsigned p1_, unsigned p2_) {
  unsigned p = p1_;
  for( int i = ZERO; i < NUM_TILES; ++i ) {
    cout << std::setw(2) << (p&0xF) << ' ';
    p = p>>TILE_SIZE;
    if( i%4 == 3 ) cout << endl;
    if( i == 7 ) p = p2_;
  }
}

int main(int argc, char **argv) {
  state15_t state;
  hash_t closed;

  if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }

  //pdb_bfs(&state, 0, &closed);
  unsigned pt1, pt2;
  pdb_gen915(state, &pt1, &pt2);
  print(pt1, pt2);
  cout << endl << endl;
  cout << state;
  return(0);
}
