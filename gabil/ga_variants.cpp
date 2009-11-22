#include "gabil.h"

using namespace std;

// Crossovers...
void gabil_crossover(vector<rule_t*> parent1, vector<rule_t*> parent2) {
  
};

// Selections...
void top_percent_selection(hypothesis_t** origin, hypothesis_t** destination) {
  sort(origin, origin + POP_SIZE, compare);
  memcpy(destination, origin, sizeof(hypothesis_t*) * ceil((1 - NEW_CHILDREN_PERC) * POP_SIZE));
};

void basic_probabilistic_selection(hypothesis_t**origin, vector<rule_t*>selected) {};
