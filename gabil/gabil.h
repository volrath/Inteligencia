#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <algorithm>
#include <vector>
#include <list>

#define POP_SIZE 1000
#define RULES_PER_DNA 1
#define RULE_LENGTH 124
#define NUM_ATTRS_P1 8
#define NUM_ATTRS_P2 6

#define MUTATE_CHANCE .02
#define NEW_CHILDREN_PERC .6

#define RAND ((double) (rand() % 100000)) / 100000.
#define LONG_RAND ((unsigned long)rand() << 32 | (unsigned long)rand())

using namespace std;

struct rule_t {
  unsigned long p1_, p2_;
};

// GENETIC STUFF    genetics.cpp
class hypothesis_t {
public:
  vector <rule_t *> rules;
  double fitness;

  hypothesis_t(long *, int);
  hypothesis_t(vector<rule_t*>, long*, int);
  hypothesis_t(vector<rule_t*>, vector<rule_t*>, long*, int);
  void mutate();
  float calc_fitness(long *, int);
};

class population_t {
public:
  hypothesis_t* hypos[POP_SIZE];
  long *training_set;
  int ts_size;
  
  population_t(long *, int);
  void next_generation();
  hypothesis_t *get_fittest();
};

/*
 *  ALGORITHMS LIBRARY    ga_variants.cpp
 */
bool compare(hypothesis_t *a, hypothesis_t *b);

// Crossovers
void gabil_crossover(vector<rule_t*>, vector<rule_t*>, vector<rule_t*>*, vector<rule_t*> *);
// ...

// Selections
void top_percent_selection(hypothesis_t**, hypothesis_t**);
void basic_probabilistic_selection(hypothesis_t**, vector<rule_t*> *);
// ...
