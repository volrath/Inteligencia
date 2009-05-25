#include <ext/hash_map>
#include "lib.h"
#include <stdio.h>
#include <stdlib.h>
#include <queue>

using namespace std;

void successors05(state15_t state, state15_t ** scs, bool * is_important) {
  memset(scs, ZERO, sizeof(scs));
  memset(is_important, ZERO, sizeof(bool));

  short as = state.allowed_steps(), k = ZERO;
  for (int i = ZERO; i < BR; i++, as = as >> 1) {
    scs[i] = NULL;
    if (as & 1 == 1) {
      // Clone the current state
      bool important = false;
      state15_t * clone = (state15_t *)malloc(sizeof(state15_t));
      clone->p1_ = state.p1_; clone->p2_ = state.p2_;
      if (i == 0) {
	is_important[k] = (clone->cont(clone->bpos()-HSTEP) < 6 ? true : false);
	clone->left();
      }
      else if (i == 1) {
	is_important[k] = (clone->cont(clone->bpos()+HSTEP) < 6 ? true : false);
	clone->right();
      }
      else if (i == 2) {
	is_important[k] = (clone->cont(clone->bpos()-VSTEP) < 6 ? true : false);
	clone->up();
      }
      else if (i == 3) {
	is_important[k] = (clone->cont(clone->bpos()+VSTEP) < 6 ? true : false);
	clone->down();
      }
      scs[k] = clone;k++;
    }
  }
}

void successors610(state15_t state, state15_t ** scs, bool *is_important) {
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
	is_important[k] = (clone->cont(clone->bpos()-HSTEP) >= 6 && clone->cont(clone->bpos()-HSTEP) <= 10 ? true : false);
	clone->left();
      }
      else if (i == 1) {
	is_important[k] = (clone->cont(clone->bpos()+HSTEP) >= 6 && clone->cont(clone->bpos()+HSTEP) <= 10 ? true : false);
	clone->right();
      }
      else if (i == 2) {
	is_important[k] = (clone->cont(clone->bpos()-VSTEP) >= 6 && clone->cont(clone->bpos()-VSTEP) <= 10 ? true : false);
	clone->up();
      }
      else if (i == 3) {
	is_important[k] = (clone->cont(clone->bpos()+VSTEP) >= 6 && clone->cont(clone->bpos()+VSTEP) <= 10 ? true : false);
	clone->down();
      }
      scs[k] = clone; k++;
    }
  }
}

void successors1115(state15_t state, state15_t ** scs, bool *is_important) {
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
	is_important[k] = (clone->cont(clone->bpos()-HSTEP) >= 11 ? true : false);
	clone->left();
      }
      else if (i == 1) {
	is_important[k] = (clone->cont(clone->bpos()+HSTEP) >= 11 ? true : false);
	clone->right();
      }
      else if (i == 2) {
	is_important[k] = (clone->cont(clone->bpos()-VSTEP) >= 11 ? true : false);
	clone->up();
      }
      else if (i == 3) {
	is_important[k] = (clone->cont(clone->bpos()+VSTEP) >= 11 ? true : false);
	clone->down();
      }
      scs[k] = clone; k++;
    }
  }
}

void priv_pdb_gen610(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) == 0 ? 0 : (state.cont(i) < 6 || state.cont(i) > 10 ? 1 : state.cont(i)));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) == 0 ? 0 : (state.cont(i+HALF_NUM_TILES) < 6 || state.cont(i+HALF_NUM_TILES) > 10 ? 1 : state.cont(i+HALF_NUM_TILES)));
  }
}

void priv_pdb_gen1115(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) == 0 ? 0 : (state.cont(i) < 11 ? 1 : state.cont(i)));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) == 0 ? 0 : (state.cont(i+HALF_NUM_TILES) < 11 ? 1 : state.cont(i+HALF_NUM_TILES)));
  }
}

int main(int argc, char **argv) {
  state15_t state;
  pair <state15_t*,int> statep;
  pt_hash_t closed, inserted;
  pattern_t pt;
  state15_t * scs1115[BR];
  bool is_important1115[BR];
  queue< pair<state15_t*,int> > stqueue;
  ofstream pdb ("pdb610.bin", ios::out | ios::binary);

  stqueue.push(make_pair(&state,0));

  while(!stqueue.empty()) {
    //cout << stqueue.size() << endl;
    statep = stqueue.front(); stqueue.pop();

    // Insert this pattern into the pattern database
    priv_pdb_gen1115(*(statep.first), &pt);
    if (closed.count(pt))
      continue;

    // Mark the current pattern as closed
    closed.insert(make_pair(pt, statep.second));

    pdb_gen1115(*(statep.first), &pt);
    if (!inserted.count(pt)) {
      inserted.insert(make_pair(pt, statep.second));
      //pdb.write(reinterpret_cast<char *>(&pt), PATTERN_SIZE); pdb.write(reinterpret_cast<char *>(&(statep.second)), INT_SIZE);
      cout << pt << endl;
    }

    // Follow up on its successors
    successors1115(*(statep.first), scs1115, is_important1115);
    for(int i = 0; i < BR && scs1115[i] != NULL; i++) {
      priv_pdb_gen1115(*scs1115[i], &pt);
      if (!closed.count(pt))
	stqueue.push(make_pair(scs1115[i], statep.second + (is_important1115[i] ? 1 : 0)));   
    }
  }
  cout << inserted.size() << endl;
  pdb.close();
  return(0);
}
