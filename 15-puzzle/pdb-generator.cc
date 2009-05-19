#include <ext/hash_map>
#include "lib.h"
#include <stdio.h>
#include <stdlib.h>

using namespace std;

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

void successors18(state15_t state, state15_t ** scs, bool * is_important) {
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

void successors915(state15_t state, state15_t ** scs, bool *is_important) {
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

// int main(int argc, char **argv) {
//   state15_t state;
//   hash_t closed;

//   if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }

//   //pdb_bfs(&state, 0, &closed);
//   unsigned pt1, pt2;
//   pdb_gen915(state, &pt1, &pt2);
//   print(pt1, pt2);
//   cout << endl << endl;
//   cout << state;
//   return(0);
// }
