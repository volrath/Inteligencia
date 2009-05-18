#include "lib.h"
#include <queue>

#define CZERO '0'

#define GBFS   "gbfs"
#define ASTAR  "astar"
#define IDA    "ida"

#define H_MAN  "manhattan"
#define H_MIS  "misplaced"

using namespace std;


int construct_initial(char ** input, state15_t *state) {
  int nums[NUM_TILES];
  for (int i = ZERO; i < NUM_TILES; i++) {
    nums[i] = *input[i+1] - CZERO;
    if (nums[i] < 0 || nums[i] > 15)
      return(0);
  }
  state->set_state(nums);
  return(1);
}

bool can_be_resolved(state15_t state) {
  int k = ZERO;
  for (int i = ZERO; i < 15; i++) {
    for (int j = i+1; j < NUM_TILES; j++) { if (state.cont(j) != 0 && state.cont(i) > state.cont(j)) k++; }
  }
  return (k%2 == ZERO);
}

/*
  Use:
  15puzzle 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 [algorithm] [heuristic]
 */
// int main (int argc, char **argv) {
//   int alg = ZERO, heu = ZERO;
//   if (argc == 19) {
//     if (strcmp(argv[10], GBFS) == ZERO)     alg = 1;
//     else if (strcmp(argv[10], IDA) == ZERO) alg = 2;

//     if (strcmp(argv[11], H_MIS) == ZERO)   heu = 1;
//   }
//   else if (argc == 18) {
//     if (strcmp(argv[10], GBFS) == ZERO)     alg = 1;
//     else if (strcmp(argv[10], IDA) == ZERO) alg = 2;
//   }
//   else if (argc == 17) {}
//   else {
//     std::cout << "You dumb ass..." << std::endl;
//     return(0);
//   }

//   state15_t state;
//   if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }
//   if (!can_be_resolved(state)) { cout << "This puzzle cannot be resolved!" << endl; return(0); }

//   node_t *path = new node_t(ZERO, ZERO, &state, true);
//   bool did_it;
//   if (alg == 2) did_it = iterative_deepening_search(state, path, heu);
//   else          did_it = informed_search(state, path, alg, heu);
//   if (did_it) {
//     //while(path != NULL) { cout << *path << endl; path = path->next(); }
//     while(path != NULL) { cout << path->m() << ", "; path = path->next(); }
//     cout << endl;
//   } else {
//     cout << "Couldn't find a path.." << endl;
//   }
//   return(ZERO);
// }


int main(int argc, char **argv) {
  state15_t state;
  state15_t *scs[BR];

  if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }

  state.successors(scs);
  for (int i = 0; scs[i] != NULL; i++)
    cout << *scs[i] << endl;

  return(0);
}
