#include <limits.h>
#include <queue>
#include <ext/hash_map>
#include "lib.h"
using namespace std;

extern pt_hash_t pdb05, pdb610, pdb1115;

class value_comparison{
public:
  int alg_;
  value_comparison(int alg = ZERO) : alg_(alg) {}
  bool operator() (const node_t *lhs, const node_t *rhs) const{
    if (alg_ == 1) return(lhs->h() > rhs->h()); else return(lhs->f() > rhs->f());
  }
};
typedef priority_queue<node_t*,vector<node_t*>, value_comparison> pq_t;

unsigned (*heuristics[3]) (state15_t state) = { misplaced_tiles, manhattan, pdb_heuristic };

bool informed_search(state15_t initial_state, node_t *root, int alg, int heu) {
  hash_t closed;
  pq_t open(alg);

  root->set_h(heuristics[heu](initial_state));
  root->set_prev(NULL);

  open.push(root);
  node_t *pActual, *pAux;
  node_t * scs[BR];

  while(!open.empty()) {
    pActual = open.top(); open.pop();
    closed.insert(make_pair(*(pActual->state()), *pActual));

    if (pActual->goal_test()) {
      if (pActual == root) return(true);
      pActual->set_next(NULL);
      pAux = pActual;
      if (pActual->prev() == NULL)
	pActual->set_prev(root);
      pActual = pActual->prev();
      while (pActual != NULL) { pActual->set_next(pAux); pAux = pActual; pActual = pActual->prev(); }
      return(true);
    }

    pActual->successors(scs);
    for (int i = ZERO; i < BR; i++) {
      if (scs[i] != NULL) { 
	pAux = scs[i];

	if (closed.count(*(pAux->state()))) continue;

	pAux->set_h(heuristics[heu](*(pAux->state())));
	pAux->set_prev(pActual);
	open.push(pAux);
      }
    }
  }
  return(false);
}

bool limited_informed_search(state15_t initial_state, node_t *root, int heu, int *limit) {
  hash_t closed;
  pq_t open(ZERO);
  unsigned best_f = INT_MAX;
  int h = ZERO;

  root->set_h(heuristics[heu](initial_state));
  root->set_prev(NULL);

  open.push(root);
  node_t *pActual, *pAux;
  node_t * scs[BR];

  while(!open.empty()) {
    pActual = open.top(); open.pop();
    closed.insert(make_pair(*(pActual->state()), *pActual));

    if (pActual->goal_test()) {
      pActual->set_next(NULL);
      pAux = pActual;
      if (pActual->prev() == NULL)
	pActual->set_prev(root);
      pActual = pActual->prev();
      while (pActual != NULL) { pActual->set_next(pAux); pAux = pActual; pActual = pActual->prev(); }
      return(true);
    }

    pActual->successors(scs);
    if (pActual->f() <= *limit) {
      for (int i = ZERO; i < BR; i++) {
	if (scs[i] != NULL) { 
	  pAux = scs[i];
	  
	  if (closed.count(*(pAux->state()))) continue;
	  
	  pAux->set_h(heuristics[heu](*(pAux->state())));
	  pAux->set_prev(pActual);
	  open.push(pAux);
	}
      }
    } else {
      for (int i = ZERO; i < BR; i++) {
	if (scs[i] != NULL) {
	  h = scs[i]->f()+heuristics[heu](*(scs[i]->state()));
	  best_f = best_f<h ? best_f : h;
	}
      }
      best_f = best_f>pActual->f() ? best_f : h;
    }
  }
  *limit = best_f;
  for (int i = ZERO; i < BR && scs[i] == NULL; i ++) free(scs[i]);
  return(false);
}


bool iterative_deepening_search(state15_t initial_state, node_t *root, int heu) {
  int *limit = (int *)malloc(sizeof(int)); *limit = ZERO;
  bool find_result = false;
  
  while (!find_result) {
    find_result = limited_informed_search(initial_state, root, heu, limit);
  }

  return(true);
}

void pdb_gen05(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
    for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) >= 0 && state.cont(i) < 6 ? state.cont(i) : 0xF);
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) >= 0 && state.cont(i+HALF_NUM_TILES) < 6 ? state.cont(i+HALF_NUM_TILES) : 0xF);
  }
}

void pdb_gen610(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) < 6 || state.cont(i) > 10 ? ZERO : state.cont(i));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) < 6 || state.cont(i+HALF_NUM_TILES) > 10 ? ZERO : state.cont(i+HALF_NUM_TILES));
  }
}

void pdb_gen1115(state15_t state, pattern_t *p) {
  (*p).p1_ = ZERO; (*p).p2_ = ZERO;
  for (int i = 7; i >= ZERO; --i) {
    (*p).p1_ = (*p).p1_ << 4; (*p).p2_ = (*p).p2_ << 4;
    (*p).p1_ = (*p).p1_ + (state.cont(i) < 11 ? ZERO : state.cont(i));
    (*p).p2_ = (*p).p2_ + (state.cont(i+HALF_NUM_TILES) < 11 ? ZERO : state.cont(i+HALF_NUM_TILES));
  }
}
