#include <ext/hash_map>
#include "lib.h"
#include <stdio.h>
#include <stdlib.h>

using namespace std;

int construct_initial2(char ** input, state15_t *state) {
  int nums[NUM_TILES];
  for (int i = ZERO; i < NUM_TILES; i++) {
    nums[i] = atoi(input[i+1]);
    if (nums[i] < 0 || nums[i] > 15)
      return(0);
  }
  state->set_state(nums);
  return(1);
}

void pdb_gen05(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) >= 0 && state.cont(i) < 5 ? state.cont(i) : 0xF);
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) >= 0 && state.cont(i+HALF_NUM_TILES) < 5 ? state.cont(i+HALF_NUM_TILES) : 0xF);
  }
}

void pdb_gen1015(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) < HALF_NUM_TILES ? ZERO : state.cont(i));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) < HALF_NUM_TILES ? ZERO : state.cont(i+HALF_NUM_TILES));
  }
}

void successors05(state15_t state, state15_t ** scs, bool * is_important) {
  memset(scs, ZERO, sizeof(scs));
  memset(is_important, ZERO, sizeof(bool));

  short as = state.allowed_steps(), k = ZERO;
  for (int i = ZERO; i < BR; i++, as = as >> 1) {
    scs[i] = NULL;
    if (as & 1 == 1) {
      // Clone the current state
      state15_t * clone = (state15_t *)malloc(sizeof(state15_t));
      bool important = false;
      clone->p1_ = state.p1_; clone->p2_ = state.p2_;
      if (i == 0) {
	is_important[k] = (clone->cont(clone->bpos()-HSTEP) <= HALF_NUM_TILES ? true : false);
	clone->left();
      }
      else if (i == 1) {
	is_important[k] = (clone->cont(clone->bpos()+HSTEP) <= HALF_NUM_TILES ? true : false);
	clone->right();
      }
      else if (i == 2) {
	is_important[k] = (clone->cont(clone->bpos()-VSTEP) <= HALF_NUM_TILES ? true : false);
	clone->up();
      }
      else if (i == 3) {
	is_important[k] = (clone->cont(clone->bpos()+VSTEP) <= HALF_NUM_TILES ? true : false);
	clone->down();
      }
      scs[k] = clone; k++;
    }
  }
}

void successors815(state15_t state, state15_t ** scs, bool *is_important) {
  memset(scs, ZERO, sizeof(scs));
  memset(is_important, ZERO, sizeof(bool));

  short as = state.allowed_steps(), k = ZERO;
  for (int i = ZERO; i < BR; i++, as = as >> 1) {
    scs[i] = NULL;
    if (as & 1 == 1) {
      // Clone the current state
      state15_t * clone = (state15_t *)malloc(sizeof(state15_t));
      bool important = false;
      clone->p1_ = state.p1_; clone->p2_ = state.p2_;
      if (i == 0) {
	is_important[k] = (clone->cont(clone->bpos()-HSTEP) > HALF_NUM_TILES ? true : false);
	clone->left();
      }
      else if (i == 1) {
	is_important[k] = (clone->cont(clone->bpos()+HSTEP) > HALF_NUM_TILES ? true : false);
	clone->right();
      }
      else if (i == 2) {
	is_important[k] = (clone->cont(clone->bpos()-VSTEP) > HALF_NUM_TILES ? true : false);
	clone->up();
      }
      else if (i == 3) {
	is_important[k] = (clone->cont(clone->bpos()+VSTEP) > HALF_NUM_TILES ? true : false);
	clone->down();
      }
      scs[k] = clone; k++;
    }
  }
}

namespace __gnu_cxx {
  template<> class hash<pattern_t> {
  public:
    size_t operator()( const pattern_t &s ) const { return(s.p1_^s.p2_); }
  };
};
class pt_hash_t : public __gnu_cxx::hash_map<pattern_t, int> { };  // class

int pdb_bfs(state15_t *state, int cost, pt_hash_t *closed) {
  // Insert this state into the pattern database
  pattern_t pt;
  if (cost > 22657) {
    cout << *state << endl;
    cout << "hola" << endl;
    pdb_gen05(*state, &pt);
    cout << pt << endl;
  }
  else
    pdb_gen05(*state, &pt);
  //cout << cost << " -- " << pt.p1_ << pt.p2_ << endl;
  // ...
  closed->insert(make_pair(pt, cost));

  state15_t *scs05[BR];
  bool is_important05[BR];
  successors05(*state, scs05, is_important05);
  for (int i = 0; scs05[i] != NULL && i < BR; i++) {
    pdb_gen05(*scs05[i], &pt);
    if (!closed->count(pt))
      pdb_bfs(scs05[i], cost + (is_important05[i] ? 1 : 0), closed);
    //free(scs05[i]);
  }
  return(0);
}

int main(int argc, char **argv) {
  state15_t state;
  pt_hash_t closed;

  pdb_bfs(&state, 0, &closed);
//   if (!construct_initial2(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }
//   pattern_t p;
//   pdb_gen05(state, &p);
//   cout << p << endl;
//   cout << state;
//   state15_t *scs[BR]; bool im[BR];
//   successors05(state, scs, im);
//   for (int i = 0; scs[i] != NULL; i++)
//     cout << *scs[i] << endl;
  return(0);
}
