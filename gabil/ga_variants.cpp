#include "gabil.h"

using namespace std;

// Crossovers...
void gabil_crossover(vector<rule_t*> parent1, vector<rule_t*> parent2, vector<rule_t*> * offspring1, vector<rule_t*> * offspring2) {
  int d1,d2,r1,r2,r3 = 0,r4 = 0;
  d1 = rand() % 124;
  d2 = rand() % 124;
  while(d1 > d2 || (d1 == d2 && (parent1.size() == 1 || parent2.size() == 1))){
    //    cout << "d1 > d2" << endl;
    d1 = rand() % 124;
    d2 = rand() % 124;
  }
  r1 = rand() % parent1.size();
  r2 = rand() % parent1.size();
  while(r1 > r2){
    r1 = rand() % parent1.size();
    r2 = rand() % parent1.size();
    //cout << "r1 > r2" << endl;
  }
  r3 = rand() % parent2.size();
  r4 = rand() % parent2.size();
  while((d1 == d2 && r3 == r4) || (r3 > r4)) {
    //cout << "r3 == r4" << endl;
    r3 = rand() % parent2.size();
    r4 = rand() % parent2.size();
  }
  
  //cout << "d1: "<< d1 << " d2: " << d2 << " r1: " << r1 << " r2: " << r2 << " r3: " << r3 << " r4:" << r4 << endl;
  //  cout << "Parent1P1: " << parent1.p1_ << " Parent1P2: " << parent1.p2_ << " Parent2P1: " << parent2.p1_ << " Parent2P2: " << parent2.p2_ << endl;
  //  cout << "Child1P1: " << offspring1.p1_ << " Parent1P2: " << parent1.p2_ << " Parent2P1: " << parent2.p1_ << " Parent2P2: " << parent2.p2_ << endl;
  for(int i = 0; i < r1; i++){
    offspring1->push_back(parent1[i]);
  }
  rule_t * offspring1_rule1 = new rule_t();
  offspring1_rule1->p1_ = 0;
  offspring1_rule1->p2_ = 0;
  if(d1 >= 64){
    offspring1_rule1->p1_ = parent1[r1]->p1_;
    offspring1_rule1->p2_ = (0xffffffffffffffff >> (64 - (d1 - 64))) & parent1[r1]->p2_;
    offspring1_rule1->p2_ |= ((0xffffffffffffffff << (d1 - 64)) & parent2[r3]->p2_);
  }else{
    offspring1_rule1->p1_ = (0xffffffffffffffff >> (64 - d1)) & parent1[r1]->p1_;
    offspring1_rule1->p1_ |= ((0xffffffffffffffff << d1) & parent2[r3]->p1_);
    offspring1_rule1->p2_ = parent2[r3]->p2_;
  }
  if(r3 == r4){
    if(d2 >= 64){
      offspring1_rule1->p1_ = offspring1_rule1->p1_;
      offspring1_rule1->p2_ &= (0xffffffffffffffff >> (64 - (d2 - 64)));
      offspring1_rule1->p2_ |= ((0xffffffffffffffff << (d2 - 64)) & parent1[r2]->p2_);
    }else{
      offspring1_rule1->p1_ &= (0xffffffffffffffff >> (64 - d2)); 
      offspring1_rule1->p1_ |= ((0xffffffffffffffff << d2) & parent1[r2]->p1_);
      offspring1_rule1->p2_ = parent1[r2]->p2_;
    }
    offspring1->push_back(offspring1_rule1);   
  }else{
    offspring1->push_back(offspring1_rule1);
    for(int i = r3+1; i < r4; i++) {
      offspring1->push_back(parent2[i]);
    }
    rule_t * offspring1_rule2 = new rule_t();
    if(d2 >= 64){
      offspring1_rule2->p1_ = parent2[r4]->p1_;
      offspring1_rule2->p2_ = (0xffffffffffffffff >> (64 - (d1 - 64))) & parent2[r4]->p2_;
      offspring1_rule2->p2_ |= ((0xffffffffffffffff << (d1 - 64)) & parent1[r2]->p2_);
    }else{
      offspring1_rule2->p1_ = (0xffffffffffffffff >> (64 - d1)) & parent2[r4]->p1_;
      offspring1_rule2->p1_ |= ((0xffffffffffffffff << d1) & parent1[r2]->p1_);
      offspring1_rule2->p2_ = parent1[r2]->p2_;
    }
    offspring1->push_back(offspring1_rule2);
  }
  for(int i = r2+1; i < parent1.size(); i++){
    offspring1->push_back(parent1[i]);
  }


  for(int i = 0; i < r3; i++){
    offspring2->push_back(parent2[i]);
  }
  rule_t * offspring2_rule1 = new rule_t();
  offspring2_rule1->p1_ = 0;
  offspring2_rule1->p2_ = 0;
  if(d1 >= 64){
    offspring2_rule1->p1_ = parent2[r3]->p1_;
    offspring2_rule1->p2_ = (0xffffffffffffffff >> (64 - (d1 - 64))) & parent2[r3]->p2_;
    offspring2_rule1->p2_ |= ((0xffffffffffffffff << (d1 - 64)) & parent1[r1]->p2_);
  }else{
    offspring2_rule1->p1_ = (0xffffffffffffffff >> (64 - d1)) & parent2[r3]->p1_;
    offspring2_rule1->p1_ |= ((0xffffffffffffffff << d1) & parent1[r1]->p1_);
    offspring2_rule1->p2_ = parent1[r1]->p2_;
  }
  if(r1 == r2) {
    if(d2 >= 64){
      offspring2_rule1->p1_ = offspring2_rule1->p1_;
      offspring2_rule1->p2_ &= (0xffffffffffffffff >> (64 - (d2 - 64)));
      offspring2_rule1->p2_ |= ((0xffffffffffffffff << (d2 - 64)) & parent2[r4]->p2_);
    }else{
      offspring2_rule1->p1_ &= (0xffffffffffffffff >> (64 - d2)); 
      offspring2_rule1->p1_ |= ((0xffffffffffffffff << d2) & parent2[r4]->p1_);
      offspring2_rule1->p2_ = parent2[r4]->p2_;
    }
    offspring2->push_back(offspring2_rule1);
  }else{
    offspring2->push_back(offspring2_rule1);
    for(int i = r1+1; i < r2; i++) {
      offspring2->push_back(parent1[i]);
    }
    rule_t * offspring2_rule2 = new rule_t();
    if(d2 >= 64){
      offspring2_rule2->p1_ = parent1[r2]->p1_;
      offspring2_rule2->p2_ = (0xffffffffffffffff >> (64 - (d1 - 64))) & parent1[r2]->p2_;
      offspring2_rule2->p2_ |= ((0xffffffffffffffff << (d1 - 64)) & parent2[r4]->p2_);
    }else{
      offspring2_rule2->p1_ = (0xffffffffffffffff >> (64 - d1)) & parent1[r2]->p1_;
      offspring2_rule2->p1_ |= ((0xffffffffffffffff << d1) & parent2[r4]->p1_);
      offspring2_rule2->p2_ = parent2[r4]->p2_;
    }
    offspring2->push_back(offspring2_rule2);
  }
  for(int i = r4+1; i < parent2.size(); i++){
    offspring2->push_back(parent2[i]);
  }
  
};

// Selections...
void top_percent_selection(int pop_size, float new_children_perc, hypothesis_t** origin, hypothesis_t** destination) {
  sort(origin, origin + pop_size, compare);
  memcpy(destination, origin, sizeof(hypothesis_t*) * ceil((1 - new_children_perc) * pop_size));
};

void basic_probabilistic_selection(int pop_size, float new_children_perc, hypothesis_t**origin, vector<rule_t*> *selected) {
  // Assumes that the origin is sorted
  double sum_fitness = 0;
  for (int i = 0; i < (int)(ceil(((1 - new_children_perc) * pop_size))); i++)
    sum_fitness += origin[i]->fitness;

  for (int i = 0; i < (int)(ceil(((1 - new_children_perc) * pop_size))); i++) {
    if (RAND < (origin[i]->fitness / sum_fitness)) {
      //      cout << origin[i]->rules.size() << endl;
      for(int j = 0; j < origin[i]->rules.size(); j++)
        selected->push_back(origin[i]->rules[j]);
      return;
    }
  }
  int pos = rand() % (int)ceil(((1 - new_children_perc) * pop_size));
  for(int j = 0; j < origin[pos]->rules.size(); j++)
    selected->push_back(origin[pos]->rules[j]);
};
