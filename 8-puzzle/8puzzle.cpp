#include "lib.h"
#include <queue>

#define NUM_PIECES 9

#define GBFS   "GBFS"
#define ASTAR  "ASTAR"
#define IDA    "IDA*"

#define H_MAN  "manhattan"
#define H_MIS  "misplaced"

using namespace std;


int construct_initial(char ** input, state8_t *state) {
  int nums[9], j = 1; 
  for (int i = ZERO; i < NUM_PIECES; i++) {
    nums[i] = atoi(input[j+1]); j++;
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
  8puzzle [algorithm] 1 2 3 4 5 6 7 8 0 [heuristic]
 */
int main (int argc, char **argv) {
  int alg = 0, heu = 1, expanded_nodes = 0;
  if (argc == 12) {
    if (strcmp(argv[1], GBFS) == 0)     alg = 1;
    else if (strcmp(argv[1], IDA) == 0) alg = 2;

    if (strcmp(argv[11], H_MIS) == 0)   heu = 1;
  }
  else if (argc == 11) {
    if (strcmp(argv[1], GBFS) == 0)     alg = 1;
    else if (strcmp(argv[1], IDA) == 0) alg = 2;
  }
  else {
    std::cout << "Modo de uso: 15puzzle <algorithm> 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 [heuristic]" << std::endl;
    return(0);
  }

  state8_t state;
  if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }
  if (!can_be_resolved(state)) { cout << "This puzzle cannot be resolved!" << endl; return(0); }

  node_t *path = new node_t(0, 0, &state, true);
  bool did_it;
  time_t execution_time = time(0);
  if (alg == 2) did_it = iterative_deepening_search(path, &expanded_nodes, heu);
  else          did_it = informed_search(state, path, &expanded_nodes, alg, heu);
  if (did_it) {
    execution_time = time(0) - execution_time;
    //while(path != NULL) { cout << *path << endl; path = path->next(); }
    while(path != NULL) { cout << "[" << path->g() << "," << path->m() << "], "; path = path->next(); }
    cout << endl << "Expanded nodes: " << expanded_nodes;
    cout << endl << "Execution time: " << execution_time << "s";
    cout << endl;
  } else {
    cout << "Couldn't find a path.." << endl;
  }
  return(ZERO);
}
