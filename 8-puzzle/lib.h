#include <iostream>
#include <iomanip>
#include <string.h>
#include "lib.cpp"

// Heuristics
unsigned misplaced_tiles(state8_t state);

// Search Algorithms
int informed_search(state8_t initial_state, node_t * node);
