#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <algorithm>
#include <vector>
#include <list>

#define POP_SIZE 100
#define RULES_PER_DNA 1

#define MUTATE_CHANCE .02
#define RAND ((double) (rand() % 10000 / 10000.))
#define NEW_CHILDREN_PERC .6

struct rule_t {
  unsigned long p1_, p2_;
};

class hypothesis_t {
public:
  rule_t rules[RULES_PER_DNA];
  float fitness;

  hypothesis_t();
  hypothesis_t(rule_t*, rule_t*);
  float calc_fitness();
};

class population_t {
public:
  hypothesis_t* hypos[POP_SIZE];
  
  population_t();
  void next_generation();
  hypothesis_t *get_fittest();
};
