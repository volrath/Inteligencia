#include <ext/hash_map>
#include "lib.h"
using namespace std;

namespace __gnu_cxx {
  template<> class hash<state15_t> {
  public:
    size_t operator()( const state15_t &s ) const { return(s.p1_^s.p2_); }
  };
};

class hash_t : public __gnu_cxx::hash_map<state15_t, node_t> { };  // class

int pdb_bfs(state15 *state, int cost; hash_t *closed) {
  // Insert this state into the pattern database
  // ...

  state15_t *scs[BR];
  state->successors(scs);
  for (int i = 0; scs[i] != NULL; i++) {
    if (!closed->count(*scs[i])) {
      closed->insert(make_pair(*scs[i], cost));
      pdb_bfs(scs[i], cost + 1; closed);
    }
  }

  free(scs);
  return(0);
}

int main(int argc, char **argv) {
  state15_t state;
  hash_t closed;

  //pdb_bfs(&state, 0, &closed);

  return(0);
}
