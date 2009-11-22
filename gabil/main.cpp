#include "gabil.h"
#include <sys/stat.h>
#include <fstream>

using namespace std;

int main(int argc, char **argv) {
  hypothesis_t *fittest;
  population_t *population;
  struct stat results;
  int size;
  float best_fitness = 0.;
  int it = 0;
 
  // read file, parsed if possible.
  if (stat("data/adult.bin", &results) == 0){
    size = results.st_size/sizeof(long);
  }
  else{}
    // An error occurred
  ifstream file("data/adult.bin", ios::in | ios::binary);
  long * creprs = new long[size];

  file.read((char*)creprs,sizeof(long)*size);
  file.close();

  population = new population_t(creprs, size);

  fittest = population->get_fittest();

  vector<rule_t*> parent1 = vector<rule_t*>();
  vector<rule_t*> parent2 = vector<rule_t*>();
  vector<rule_t*> * offspring1 = new vector<rule_t*>();
  vector<rule_t*> * offspring2 = new vector<rule_t*>();

  parent1.push_back(population->hypos[0]->rules[0]);
  parent2.push_back(population->hypos[1]->rules[0]);

  cout << "Individuo 1:" << parent1[0]->p1_ << " " << parent1[0]->p2_ << endl;
  cout << "Individuo 2:" << parent2[0]->p1_ << " " << parent2[0]->p2_ << endl;
  
  gabil_crossover(parent1,parent2,offspring1,offspring2);

  //cout << "Hijo 1:" << offspring1->size() << endl;
  //cout << "Hijo 2:" << offspring2->size() << endl;
  cout << "Hijo 1:" << (*offspring1)[0]->p1_ << " " << (*offspring1)[0]->p2_ << endl;
  cout << "Hijo 2:" << (*offspring2)[0]->p1_ << " " << (*offspring2)[0]->p2_ << endl;

//   while (true) { // dont know which stop condition =S
//     cout << "Generando poblacion " << ++it << endl;
//     fittest = population->get_fittest();
//     if (fittest->fitness > best_fitness)
//       best_fitness = fittest->fitness;

//     cout << "    Fitness:     " << (float)fittest->fitness * 100 << " %%" << endl;
//     cout << "    Best so far: " << (float)best_fitness * 100 << " %%" << endl;
//  }
}
