#include <queue>
#include <ext/hash_map>
#include "lib.h"
using namespace std;

class value_comparison{
public:
  int heu_;
  value_comparison(int heu = 0) : heu_(heu) {}
  bool operator() (const node_t *lhs, const node_t *rhs) const{
    if (heu_ == 1) return(lhs->h() > rhs->h()); else return(lhs->f() > rhs->f());
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

int informed_search(state8_t initial_state, node_t *root, int alg, int heu) {
  hash_t closed;
  pq_t open(heu);

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
      return(1);
    }

    pActual->successors(scs);
    for (int i = 0; i < BR; i++) {
      if (scs[i] != NULL) { 
	pAux = scs[i];

	if (closed.count(*(pAux->state()))) continue;

	pAux->set_h(heuristics[heu](*(pAux->state())));
	pAux->set_prev(pActual);
	open.push(pAux);
      }
    }
  }
  return(0);
}
