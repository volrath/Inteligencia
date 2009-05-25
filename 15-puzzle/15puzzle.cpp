#include "lib.h"
#include <queue>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define CZERO '0'

#define GBFS   "gbfs"
#define ASTAR  "astar"
#define IDA    "ida"

#define H_MAN  "manhattan"
#define H_MIS  "misplaced"

using namespace std;

extern pt_hash_t pdb05, pdb610, pdb1115;

int construct_initial(char ** input, state15_t *state) {
  int nums[NUM_TILES];
  for (int i = ZERO; i < NUM_TILES; i++) {
    nums[i] = atoi(input[i+1]);
    if (nums[i] < 0 || nums[i] > 15)
      return(0);
  }
  state->set_state(nums);
  return(1);
}

/*
  Use:
  15puzzle 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0 [algorithm] [heuristic]
 */
int main (int argc, char **argv) {
  int alg = 0, heu = 2, expanded_nodes = 0;
//   if (argc == 19) {
//     if (strcmp(argv[10], GBFS) == ZERO)     alg = 1;
//     else if (strcmp(argv[10], IDA) == ZERO) alg = 2;

//     if (strcmp(argv[11], H_MIS) == ZERO)   heu = 1;
//   }
//   else if (argc == 18) {
//     if (strcmp(argv[10], GBFS) == ZERO)     alg = 1;
//     else if (strcmp(argv[10], ASTAR) == ZERO) alg = 0;
//   }
//   else if (argc == 17) {}
//   else {
//     std::cout << "You dumb ass..." << std::endl;
//     return(0);
//   }

  state15_t state;
  if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }

  // Load heurisitic
  time_t load_time = time(0);
  pattern_t pt;
  int cost;
  ifstream pdb_file;

  cout << "Loading pattern database 0-5" << endl;
  pdb_file.open(PDB05_FILE, ios::in | ios::binary);
  while(!pdb_file.eof()) {
    pdb_file.read(reinterpret_cast<char *>(&pt), PATTERN_SIZE);
    pdb_file.read(reinterpret_cast<char *>(&cost), INT_SIZE);
    pdb05.insert(make_pair(pt, cost));
  }
  pdb_file.close();

  cout << "Loading pattern database 6-10" << endl;
  pdb_file.open(PDB610_FILE, ios::in | ios::binary);
  while(!pdb_file.eof()) {
    pdb_file.read(reinterpret_cast<char *>(&pt), PATTERN_SIZE);
    pdb_file.read(reinterpret_cast<char *>(&cost), INT_SIZE);
    pdb610.insert(make_pair(pt, cost));
  }
  pdb_file.close();

  cout << "Loading pattern database 11-15" << endl;
  pdb_file.open(PDB1115_FILE, ios::in | ios::binary);
  while(!pdb_file.eof()) {
    pdb_file.read(reinterpret_cast<char *>(&pt), PATTERN_SIZE);
    pdb_file.read(reinterpret_cast<char *>(&cost), INT_SIZE);
    pdb1115.insert(make_pair(pt, cost));
  }
  pdb_file.close();

  load_time = time(0) - load_time;
  cout << "All set up... loading time = " << load_time << "s" << endl;
  // -----------------------------------------------------------------

  node_t *path = new node_t(ZERO, ZERO, &state, true);
  bool did_it;
  time_t execution_time = time(0);
  if (alg == 2) did_it = iterative_deepening_search(state, path, &expanded_nodes, heu);
  else          did_it = informed_search(state, path, &expanded_nodes, alg, heu);
  if (did_it) {
    execution_time = execution_time - time(0);
    //while(path != NULL) { cout << *path << endl; path = path->next(); }
    while(path != NULL) { cout << path->m() << ", "; path = path->next(); }
    cout << endl << "Expanded nodes: " << expanded_nodes;
    cout << endl << "Execution time: " << execution_time << "s";
    cout << endl;
  } else {
    cout << "Couldn't find a path.." << endl;
  }
  return(ZERO);
}


// int main(int argc, char **argv) {
//   state15_t state;
//   state15_t *scs[BR];
//   bool important[BR];

//   if (!construct_initial(argv, &state)) { std::cout << "Error initializing" << std::endl; return(0); }

//   // Load heurisitic
//   cout << "Loading pattern database" << endl;
//   time_t load_time = time(0);
//   pattern_t pt;
//   int cost;
//   ifstream pdb_file;

//   pdb_file.open(PDB05_FILE, ios::in | ios::binary);
//   while(!pdb_file.eof()) {
//     pdb_file.read(reinterpret_cast<char *>(&pt), PATTERN_SIZE);
//     pdb_file.read(reinterpret_cast<char *>(&cost), INT_SIZE);
//     pdb05.insert(make_pair(pt, cost));
//   }
//   load_time = time(0) - load_time;
//   pdb_file.close();
//   cout << "All set up... loading time = " << load_time << "s" << endl;
//   // -----------------------------------------------------------------

//   cout << state << endl;
//   cout << pdb_heuristic(state) << endl;

//   return(0);
// }
