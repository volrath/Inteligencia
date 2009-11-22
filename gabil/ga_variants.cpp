#include "gabil.h"

using namespace std;

// Crossovers...
void gabil_crossover(vector<rule_t*> parent1, vector<rule_t*> parent2, vector<rule_t*> child1, vector<rule_t*>child2) {
  
};

// Selections...
void top_percent_selection(hypothesis_t** origin, hypothesis_t** destination) {
  sort(origin, origin + POP_SIZE, compare);
  memcpy(destination, origin, sizeof(hypothesis_t*) * ceil((1 - NEW_CHILDREN_PERC) * POP_SIZE));
};

void basic_probabilistic_selection(hypothesis_t**origin, vector<rule_t*>selected) {
  // Assumes that the origin is sorted
  double sum_fitness = 0;
  for (int i = 0; i < (int)(ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE))); i++)
    sum_fitness += origin[i]->fitness;

  for (int i = 0; i < (int)(ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE))); i++) {
    if (RAND < (origin[i]->fitness / sum_fitness)) {
      selected = origin[i]->rules;
      break;
    }
  }

  selected = origin[rand() % (int)ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE))]->rules;
};
