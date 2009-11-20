#include "gabil.h"

using namespace std;

bool compare(hypothesis_t *a, hypothesis_t *b) {
  return a->fitness > b->fitness;
}

/*
 *         POPULATION
 */
// Generates a random population and initializes everything needed for
// the population
population_t::population_t() {
  for (int i = 0; i < POP_SIZE; i++)
    hypos[i] = new hypothesis_t();

  // Maybe read the data, parser the data, whatever
};

// Handles the selection, then commands the crossover and posible
// mutation
void population_t::next_generation() {

};

// Return the fittest of the current population
hypothesis_t* population_t::get_fittest() {
  sort(hypos, hypos + POP_SIZE, compare); // dont remember why this works, but it do...
  return hypos[0];
};

/*
 *         HYPOTHESIS
 */
// Creates a new random hypothesis
hypothesis_t::hypothesis_t() {
  rules = new rule_t[RULES_PER_DNA];
  for (int i = 0; i < RULES_PER_DNA; i++) {
    rules[i].p1_ = LONG_RAND;
    rules[i].p2_ = LONG_RAND;
  }
};

// Creates a new hypothesis, doing crossover between the parents
hypothesis_t::hypothesis_t(rule_t *parent1, rule_t *parent2) {
  two_point_crossover(rules);
};

// Does a simple point mutation
void hypothesis_t::mutate() {
  int rand_bit = rand() % 124;

  if (rand_bit < 64)
    rules[rand() % RULES_PER_DNA].p1_ ^ (1 << rand_bit);
  else
    rules[rand() % RULES_PER_DNA].p2_ ^ (1 << rand_bit % 64);
};

float hypothesis_t::calc_fitness() {};
