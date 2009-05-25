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

void successors1015(state15_t state, state15_t ** scs, bool *is_important) {
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
	is_important[k] = (clone->cont(clone->bpos()-HSTEP) >= 10 ? true : false);
	clone->left();
      }
      else if (i == 1) {
	is_important[k] = (clone->cont(clone->bpos()+HSTEP) >= 10 ? true : false);
	clone->right();
      }
      else if (i == 2) {
	is_important[k] = (clone->cont(clone->bpos()-VSTEP) >= 10 ? true : false);
	clone->up();
      }
      else if (i == 3) {
	is_important[k] = (clone->cont(clone->bpos()+VSTEP) >= 10 ? true : false);
	clone->down();
      }
      scs[k] = clone; k++;
    }
  }
}

int main(int argc, char **argv) {
  state15_t state;
  pair <state15_t*,int> statep;
  pt_hash_t closed;
  pattern_t pt;
  state15_t * scs1015[BR];
  bool is_important1015[BR];
  queue< pair<state15_t*,int> > stqueue;
  ofstream pdb (PDB1015_FILE, ios::out | ios::binary);

  stqueue.push(make_pair(&state,0));

  while(!stqueue.empty()) {
    //cout << stqueue.size() << endl;
    statep = stqueue.front(); stqueue.pop();

    // Insert this pattern into the pattern database
    pdb_gen1015(*(statep.first), &pt);
    if (closed.count(pt))
      continue;
    pdb.write(reinterpret_cast<char *>(&pt), PATTERN_SIZE); pdb.write(reinterpret_cast<char *>(&(statep.second)), INT_SIZE);
    //cout << *(statep.first) << statep.second << " -- " << pt << endl;

    // Mark the current pattern as closed
    closed.insert(make_pair(pt, statep.second));

    // Follow up on its successors
    successors1015(*(statep.first), scs1015, is_important1015);
    for(int i = 0; i < BR && scs1015[i] != NULL; i++) {
      pdb_gen1015(*scs1015[i], &pt);
      //if (!closed.count(pt))
      stqueue.push(make_pair(scs1015[i], statep.second + (is_important1015[i] ? 1 : 0)));   
    }
  }
  pdb.close();
  return(0);
}
