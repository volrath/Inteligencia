#include "lib.h"
#include <queue>

#define ZERO '0'
#define NUM_PIECES 9

#define GBFS   "gbfs"
#define ASTAR  "astar"
#define IDA    "ida"

#define H_MAN  "manhattan"
#define H_MIS  "misplaced"

using namespace std;


int construct_initial(char ** input, state8_t *state) {
  int nums[9];
  for (int i = 0; i < NUM_PIECES; i++) {
    nums[i] = *input[i+1] - ZERO;
    if (nums[i] < 0 || nums[i] > 8)
      return(0);
  }
  state->set_state(nums);
  return(1);
}

bool can_be_resolved(state8_t state) {
  int k = 0;
  for (int i = 0; i < 8; i++) {
    for (int j = i+1; j < 9; j++) { if (state.cont(j) != 0 && state.cont(i) > state.cont(j)) k++; }
  }
  return (k%2 == 0);
}

/*
  Use:
  8puzzle 1 2 3 4 5 6 7 8 0 [algorithm] [heuristic]
 */
int main (int argc, char **argv) {
  int alg = 0, heu = 0;
  if (argc == 12) {
    if (strcmp(argv[10], GBFS) == 0)     alg = 1;
    else if (strcmp(argv[10], IDA) == 0) alg = 2;

    if (strcmp(argv[11], H_MIS) == 0)   heu = 1;
  }
  else if (argc == 11) {
    if (strcmp(argv[10], GBFS) == 0)     alg = 1;
    else if (strcmp(argv[10], IDA) == 0) alg = 2;
  }
  else if (argc == 10) {}
  else {
    std::cout << "You dumb ass..." << std::endl;
    return(0);
  }

  state8_t state;
  if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }
  if (!can_be_resolved(state)) { cout << "This puzzle cannot be resolved!" << endl; return(0); }

  node_t *path = new node_t(0, 0, &state, true);
  if (informed_search(state, path, alg, heu)) {
    //while(path != NULL) { cout << *path << endl; path = path->next(); }
    while(path != NULL) { cout << path->m() << ", "; path = path->next(); }
    cout << endl;
  } else {
    cout << "Couldn't find a path.." << endl;
  }
  return 0;
}
