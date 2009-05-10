#include <iostream>
#include <queue>

class value_comparison{
public:
  value_comparison(){}
  bool operator() (const value_t* lhs, const value_t* rhs) const{
    return(lhs->second.f() < rhs->second.f());
  }
};
typedef priority_queue<value_t*,vector<value_t*>,value_comparison> pq_t;

void astar(value_t vl, int heuristic) {
  hash_t visited;
  pq_t pq;

  vl.second.set_g(0);
  vl.second.set_h(misplaced_tiles(vl.first));

  path_followed.insert(vl);
  pq.push(vl);
  value_t *actual, *aux = new value_t;
  state8_t scs[4];

  while(!pq.empty()) {
    actual = pq.pop();
    if (actual.first.goal_test()) break;
    if (visited.find(actual)) continue;
    actual.successors(scs);
    for (int i = 0; i < BR; i++) {
      if (scs[i] != NULL) { 
	aux = make_pair(scs[i], new node_t(actual.second.g() + 1, misplaced_tiles(scs[i])));
	pq.push(aux);
    }
  }
}
