#include <ext/hash_map>
#include "lib.h"
#include <stdio.h>
#include <stdlib.h>
#include <queue>

using namespace std;

void pdb_gen07(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
    for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) >= 6 ? 0xF : state.cont(i));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) >= 6 ? 0xF : state.cont(i+HALF_NUM_TILES));
  }
}

void pdb_gen815(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) < HALF_NUM_TILES ? ZERO : state.cont(i));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) < HALF_NUM_TILES ? ZERO : state.cont(i+HALF_NUM_TILES));
  }
}

void successors07(state15_t state, state15_t  ** scs, bool * is_important) {

  short as = state.allowed_steps(), k = ZERO;
  for (int i = ZERO; i < BR; i++, as = as >> 1) {
    scs[i] = NULL;
    if (as & 1 == 1) {
      // Clone the current state
      bool important = false;
      state15_t * clone = (state15_t *)malloc(sizeof(state15_t));
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
      scs[k] = clone;k++;
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

int pdb_bfs(state15_t *state, int cost, pt_hash_t *closed, state15_t ** scs07, bool is_important07[BR]) {
  // Insert this state into the pattern database
  pattern_t pt;
  pdb_gen07(*state, &pt);
  // ...
  closed->insert(make_pair(pt, cost));

  successors07(*state, scs07, is_important07);
  /*  for (int i = 0; scs07[i] != NULL && i < BR; i++){
    pdb_gen07(*scs07[i], &pt);
    if (!closed->count(pt))
      pdb_bfs(scs07[i], cost + (is_important07[i] ? 1 : 0), closed);
      }*/

  //free(scs07);
  return(0);
}

int main(int argc, char **argv) {
  state15_t state;
  pair <state15_t*,int> statep;
  pt_hash_t closed;
  pattern_t pt;
  state15_t * scs07[BR];
  bool is_important07[BR];
  queue< pair<state15_t*,int> > stqueue;
  stqueue.push(make_pair(&state,0));

  while(stqueue.size() != 0) {
    //cout << stqueue.size() << endl;
    statep = stqueue.front();
    stqueue.pop();
    pdb_bfs(statep.first, statep.second, &closed,scs07,is_important07);
    for(int i = 0; scs07[i] != NULL && i < BR; i++) {
      pdb_gen07(*scs07[i], &pt);
      if (!closed.count(pt))
	stqueue.push(make_pair(scs07[i], statep.second + (is_important07[i] ? 1 : 0)));   
    }
    //   pattern_t p;
    //   pdb_gen815(state, &p);
    //   cout << p << endl;
    //   cout << state;
  }
  return(0);
}
