#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <algorithm>
#include <vector>
#include <list>

#define POP_SIZE 1
#define RULES_PER_DNA 1
#define RULE_LENGTH 124
#define NUM_ATTRS_P1 8
#define NUM_ATTRS_P2 6

#define MUTATE_CHANCE .02
#define NEW_CHILDREN_PERC .6

#define RAND ((unsigned double) (rand() % 10000 / 10000.))
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

  hypothesis_t();
  hypothesis_t(rule_t*, rule_t*);
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
// Crossovers
void two_point_crossover(rule_t*);
// ...

// Selections
// ...
