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
population_t::population_t(const char *f_name, int ps, float mc, float ncp) {
  srand(time(NULL));

  pop_size = ps; mutate_chance = mc; new_children_perc = ncp;
  hypos = new hypothesis_t*[pop_size];

  for (int i = 0; i < pop_size; i++)
    hypos[i] = new hypothesis_t(training_set, ts_size);

  // read the data
  struct stat results;
  int size;
  if (stat(f_name, &results) == 0)
    ts_size = results.st_size/sizeof(long);
  ifstream file("data/adult.bin", ios::in | ios::binary);
  training_set = new long[size];

  file.read((char*)training_set,sizeof(long)*size);
  file.close();
};

// Handles the selection, then commands the crossover and posible
// mutation
void population_t::next_generation() {
  hypothesis_t* new_population[pop_size];
  top_percent_selection(pop_size, new_children_perc, hypos, new_population);

  for (int i = 0; i < floor(new_children_perc * pop_size); i++) {
    vector<rule_t *> * parent1 = new vector<rule_t*>();
    vector<rule_t *> * parent2 = new vector<rule_t*>();
    vector<rule_t *> * child1 = new vector<rule_t *>();
    vector<rule_t *> * child2 = new vector<rule_t *>();
    basic_probabilistic_selection(pop_size, new_children_perc, new_population, parent1);
    basic_probabilistic_selection(pop_size, new_children_perc, new_population, parent2);

    //cout << "ALGO" << endl;
    //cout << parent1->size() << endl;
    //cout << parent2->size() << endl;
    gabil_crossover(*parent1, *parent2, child1, child2);
    //cout << "Parent1 Size: " << parent1->size() << " Parent2 Size:" << parent2->size() << " Child1 Size:" << child1->size() << " Child2 Size: " << child2->size() << endl;
    //cout << "Floor " << floor(new_children_perc * pop_size) << " i: " << i <<endl;
    new_population[(int)(ceil(((1 - new_children_perc) * pop_size)) + i)] = new hypothesis_t(*child1, training_set, ts_size);
    new_population[(int)(ceil(((1 - new_children_perc) * pop_size)) + i + 1)] = new hypothesis_t(*child2, training_set, ts_size);
    if (RAND < MUTATE_CHANCE)
      new_population[(int)(ceil(((1 - new_children_perc) * pop_size)) + i)]->mutate();
    if (RAND < MUTATE_CHANCE)
      new_population[(int)(ceil(((1 - new_children_perc) * pop_size)) + i + 1)]->mutate();
  }

  //delete [] hypos;
  memcpy(hypos, new_population, sizeof(hypothesis_t*) * pop_size);
};

// Return the fittest of the current population
hypothesis_t* population_t::get_fittest() {
  sort(hypos, hypos + pop_size, compare); // dont remember why this works, but it does...
  return hypos[0];
};

/*
 *         HYPOTHESIS
 */
// Creates a new random hypothesis
hypothesis_t::hypothesis_t(long *training_set, int ts_size) {
  int rules_per_dna = (rand()%RULES_PER_DNA)+1;
  for (int i = 0; i < rules_per_dna; i++) {
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
      cont = true;
      and_p1 = (*it)->p1_ & training_set[i];
      and_p2 = (*it)->p2_ & training_set[i+1];
      
      for (int j = 0; j < NUM_ATTRS_P1; j++) {
        if ((and_p1 & andP1[j]) == 0){
          cont = false;
          break;
        }
      }
      for (int j = 0; cont && j < NUM_ATTRS_P2; j++) {
        if ((and_p2 & andP2[j]) == 0){
          cont = false;
          break;
        }         
      }
      if ( cont && (((*it)->p2_ << 59) == (training_set[i+1] << 59))){
        corrects += 1;
        break;
      }
    }
    //    corrects += 1;
  }
  fitness = ((double)corrects / (double)(ts_size / 2));
};
