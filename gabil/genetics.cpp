#include "gabil.h"

using namespace std;

int attributesP1[] = {4,8,4,16,5,7,14,6};
int attributesP2[] = {5,2,4,3,4,41};
unsigned long int andP1[] = {15,4080,61440,4294901760,133143986176,17454747090944,288212783965667328,18158513697557839872u};
unsigned long int andP2[] = {31,96,1920,14336,245760,576460752303161344};

bool compare(hypothesis_t *a, hypothesis_t *b) {
  return a->fitness > b->fitness;
}

/*
 *         POPULATION
 */
// Generates a random population and initializes everything needed for
// the population
population_t::population_t(long *ts, int size) {
  srand(time(NULL));
  
  training_set = ts; ts_size = size;

  for (int i = 0; i < POP_SIZE; i++)
    hypos[i] = new hypothesis_t(training_set, ts_size);

  // Maybe read the data, parser the data, whatever
};

// Handles the selection, then commands the crossover and posible
// mutation
void population_t::next_generation() {
  hypothesis_t* new_population[POP_SIZE];
  top_percent_selection(hypos, new_population);

  for (int i = 0; i < floor(NEW_CHILDREN_PERC * POP_SIZE); i++) {
    vector<rule_t *> parent1, parent2, child1, child2;
    basic_probabilistic_selection(new_population, parent1);
    basic_probabilistic_selection(new_population, parent2);

    gabil_crossover(parent1, parent2, &child1, &child2);
    new_population[(int)(ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE)) + i)] = new hypothesis_t(child1, training_set, ts_size);
    new_population[(int)(ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE)) + i + 1)] = new hypothesis_t(child1, training_set, ts_size);
    if (RAND < MUTATE_CHANCE)
      new_population[(int)(ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE)) + i)]->mutate();
    if (RAND < MUTATE_CHANCE)
      new_population[(int)(ceil(((1 - NEW_CHILDREN_PERC) * POP_SIZE)) + i + 1)]->mutate();
  }

  delete [] hypos;
  memcpy(new_population, hypos, sizeof(hypothesis_t*) * POP_SIZE);
};

// Return the fittest of the current population
hypothesis_t* population_t::get_fittest() {
  sort(hypos, hypos + POP_SIZE, compare); // dont remember why this works, but it does...
  return hypos[0];
};

/*
 *         HYPOTHESIS
 */
// Creates a new random hypothesis
hypothesis_t::hypothesis_t(long *training_set, int ts_size) {
  for (int i = 0; i < RULES_PER_DNA; i++) {
    rule_t * rule = new rule_t();
    rule->p1_ = LONG_RAND;
    rule->p2_ = LONG_RAND;
    rules.push_back(rule);
  }

  calc_fitness(training_set, ts_size);
};

// Creates a new hypothesis given a set of rules
hypothesis_t::hypothesis_t(vector<rule_t *>rs, long *training_set, int ts_size) {
  rules = rs;
  calc_fitness(training_set, ts_size);
}

// Creates a new hypothesis, doing crossover between the parents
hypothesis_t::hypothesis_t(vector<rule_t*> parent1, vector<rule_t*> parent2, long *training_set, int ts_size) {
  //gabil_crossover(parent1, parent2);
  calc_fitness(training_set, ts_size);
};

// Does a simple point mutation
void hypothesis_t::mutate() {
  int rand_bit = rand() % 124;

  if (rand_bit < 64)
    rules[rand() % RULES_PER_DNA]->p1_ ^ (1 << rand_bit);
  else
    rules[rand() % RULES_PER_DNA]->p2_ ^ (1 << rand_bit % 64);
};

float hypothesis_t::calc_fitness(long *training_set, int ts_size) {
  vector<rule_t *>::iterator it;
  unsigned long and_p1, and_p2;
  int corrects = 0; bool cont = true;
  
  for (int i = 0; i < ts_size; i += 2) {
    for (it = rules.begin(); it < rules.end(); it++) {
      and_p1 = (*it)->p1_ & training_set[i];
      and_p2 = (*it)->p2_ & training_set[i+1];

      for (int j = 0; j < NUM_ATTRS_P1; j++) {
	if ((and_p1 & andP1[j]) == 0)
	  goto loop;
      }
      for (int j = 0; j < NUM_ATTRS_P2; j++) {
	if ((and_p2 & andP2[j]) == 0)
	  goto loop;
      }
    }
    if (((*it)->p2_ << 59) == (training_set[i+1] << 59))
      corrects += 1;
  loop:
    continue;
  }
  fitness = ((double)corrects / (double)(ts_size / 2));
};
