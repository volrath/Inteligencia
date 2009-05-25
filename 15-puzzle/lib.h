#include <iostream>
#include <iomanip>
#include <string.h>
#include "lib.cpp"

// Search Algorithms
bool informed_search(state15_t initial_state, node_t * node, int alg, int heu);
bool iterative_deepening_search(state15_t initial_state, node_t *root, int heu);

// pattern database
void pdb_gen05(state15_t state, pattern_t *pt);
void pdb_gen1115(state15_t state, pattern_t *pt);

// Heuristics
unsigned misplaced_tiles(state15_t state);
unsigned manhattan(state15_t state);
unsigned pdb_heuristic(state15_t state);
//unsigned (*heuristics[2]) (state15_t state);

// Global variables
/* #ifndef GLOBAL_PDB */
/* #define GLOBAL_PDB */


/* #endif */
//pt_hash_t pdb05;

