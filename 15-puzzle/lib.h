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
