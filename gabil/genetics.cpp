#include "gabil.h"

bool compare(hypothesis_t *a, hypothesis_t *b) {
	return a->fitness > b->fitness;
}

/*
 *         POPULATION
 */
population_t::population_t() {};

void population_t::next_generation() {};

hypothesis_t* population_t::get_fittest() {};

/*
 *         HYPOTHESIS
 */
hypothesis_t::hypothesis_t() {};

hypothesis_t::hypothesis_t(rule_t *parent1, rule_t *parent2) {};

float hypothesis_t::calc_fitness() {};

