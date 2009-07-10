#include <limits.h>
#include <queue>
#include <ext/hash_map>
#include "lib.h"
using namespace std;

extern pt_hash_t pdb05, pdb610, pdb1115;

class value_comparison{
public:
  //int alg_;
  //  value_comparison(int alg = ZERO) : alg_(alg) {}
  value_comparison() {}
  bool operator() (const node_t *lhs, const node_t *rhs) const{
    //if (alg_ == 1) return(lhs->h() > rhs->h()); else return(lhs->f() > rhs->f());
    return(lhs->f() > rhs->f());
  }
};
typedef priority_queue<node_t*,vector<node_t*>, value_comparison> pq_t;

unsigned (*heuristics[3]) (state15_t state) = { misplaced_tiles, manhattan, pdb_heuristic };

bool informed_search(state15_t initial_state, node_t *root, int *en, int alg, int heu) {
  hash_t closed;
  pq_t open;

  root->set_h(heuristics[heu](initial_state));
  root->set_prev(NULL);

  open.push(root);
  node_t *pActual, *pAux;
  node_t * scs[BR];

  while(!open.empty()) {
    (*en)++;
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
    for (int i = ZERO; i < BR && scs[i] != NULL; i++) {
      pAux = scs[i];

      if (closed.count(*(pAux->state()))) continue;

      pAux->set_h(heuristics[heu](*(pAux->state())));
      pAux->set_prev(pActual);
      open.push(pAux);
    }
  }
  return(false);
}


bool limited_informed_search(node_t *node, int *en, hash_t *closed, int heu, int limit, int *best_f) {
  if (node->goal_test())
    return(true);
  if (node->f() > limit) { 
    *best_f = *best_f < node->f() ? *best_f : node->f(); 
    delete node->state(); delete node; 
    return false;
  }
  //cout << "BestF" << *best_f << endl;
  if (closed->count(*(node->state()))){
    delete node->state();
    delete node;
    return (false);
  }

  node_t * scs[BR];
  int h = ZERO;
  bool found = false;

  closed->insert(make_pair(*(node->state()), *node));
  (*en)++;

  node->successors(scs);
  for (int i = ZERO; i < BR && scs[i] != NULL && found != true; i++) {
    if (closed->count(*(scs[i]->state()))) { delete scs[i]->state(); delete scs[i]; continue; }
    node->set_next(scs[i]);
    scs[i]->set_prev(node);
    scs[i]->set_h(heuristics[heu](*(scs[i]->state())));
    found = limited_informed_search(scs[i], en, closed, heu, limit, best_f);    
  }
  //cout << (node->g()); cout << endl;
  //for (int i = ZERO; i < BR && scs[i] != NULL; i++) if(scs[i]->f() > 200){cout << *(scs[i]->state()) << " " << heuristics[heu](*(scs[i]->state())); cout << endl;}
  //if(!found){ delete node->state(); delete node; return false;}
  return(found);
}

bool iterative_deepening_search(node_t *root, int *en, int heu) {
  int limit, best_f;
  bool find_result = false;

  root->set_h(heuristics[heu](*(root->state())));
  limit = root->h();
  while (!find_result) {
    best_f = INT_MAX;
    hash_t * closed = new hash_t();
    find_result = limited_informed_search(root, en, closed, heu, limit, &best_f);
    limit = best_f;
    delete closed;
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
