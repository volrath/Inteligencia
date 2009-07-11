#include <limits.h>
#include <queue>
#include <ext/hash_map>
#include "lib.h"
using namespace std;

class value_comparison{
public:
  int alg_;
  value_comparison(int alg = 0) : alg_(alg) {}
  bool operator() (const node_t *lhs, const node_t *rhs) const{
    if (alg_ == 1) return(lhs->h() > rhs->h()); else return(lhs->f() > rhs->f());
  }
};
typedef priority_queue<node_t*,vector<node_t*>, value_comparison> pq_t;

namespace __gnu_cxx {
  template<> class hash<state8_t> {
  public:
    size_t operator()( const state8_t &s ) const { return(s.p1_^s.p2_); }
  };
};

class hash_t : public __gnu_cxx::hash_map<state8_t, node_t> { };  // class

unsigned (*heuristics[2]) (state8_t state) = { misplaced_tiles, manhattan };

bool informed_search(state8_t initial_state, node_t *root, int *en, int alg, int heu) {
  hash_t closed;
  pq_t open(alg);

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
    for (int i = 0; i < BR; i++) {
      if (scs[i] != NULL) {
	pAux = scs[i];
	
	if (closed.count(*(pAux->state()))){
	  hash_t::iterator node_found = closed.find(*(pAux->state()));
	  if(node_found->second.g() > pAux->g()){
	    closed.erase(node_found);
	  }else{
	    delete pAux->state();
	    delete pAux;
	    continue;
	  }
	}

	pAux->set_h(heuristics[heu](*(pAux->state())));
	pAux->set_prev(pActual);
	open.push(pAux);
      }
    }
  }
  return(false);
}

bool limited_informed_search(node_t *node, int *en, hash_t *closed, int heu, int limit, int *best_f, bool is_root) {
  if (node->goal_test())
    return(true);
  if (node->f() > limit) { 
    *best_f = *best_f < node->f() ? *best_f : node->f(); 
    delete node->state(); delete node; 
    return false;
  }
  //cout << "BestF" << *best_f << endl;
  if (closed->count(*(node->state()))) {
    hash_t::iterator node_found = closed->find(*(node->state()));
    if(node_found->second.g() > node->g()){
      closed->erase(node_found);
    }else{
      delete node->state();
      delete node;
      return (false);
    }
  }

  node_t * scs[BR];
  int h = ZERO;
  bool found = false;

  closed->insert(make_pair(*(node->state()), *node));
  (*en)++;

  node->successors(scs);
  for (int i = ZERO; i < BR && scs[i] != NULL && found != true; i++) {
    if (closed->count(*(scs[i]->state()))) { delete scs[i]->state(); delete scs[i]; scs[i] = NULL; continue; }
    node->set_next(scs[i]);
    scs[i]->set_prev(node);
    scs[i]->set_h(heuristics[heu](*(scs[i]->state())));
    found = limited_informed_search(scs[i], en, closed, heu, limit, best_f, false);
  }
  if(node->g() == 47){ cout << *(node->state()) << endl;}
  if(!found && !is_root){ delete node->state(); delete node; return false;}
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
    find_result = limited_informed_search(root, en, closed, heu, limit, &best_f, true);
    limit = best_f;
    delete closed;
  }

  return(true);
}
