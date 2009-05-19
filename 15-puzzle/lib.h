#include <iostream>
#include <iomanip>
#include <string.h>
#include "lib.cpp"

// Heuristics
unsigned misplaced_tiles(state15_t state);
unsigned manhattan(state15_t state);
//unsigned (*heuristics[2]) (state15_t state);

// Search Algorithms
bool informed_search(state15_t initial_state, node_t * node, int alg, int heu);
bool iterative_deepening_search(state15_t initial_state, node_t *root, int heu);

// pattern database
void pdb_gen18(state15_t state, unsigned *pt1, unsigned *pt2);
void pdb_gen915(state15_t state, unsigned *pt1, unsigned *pt2);
int pdb_bfs(state15_t *state, int cost, hash_t *closed);
void successors18(state15_t state, state15_t ** scs, bool * important);
void successors915(state15_t state, state15_t ** scs, bool * important);
