#include "gabil.h"

using namespace std;

int main(int argc, char **argv) {
  hypothesis_t *fittest, *best_so_far;
  population_t *population;
  int it = 0;
 
  /*
   * Args
   */
  int pop_size;
  float mutate_chance, new_children_perc;
  bool verbose = true;
  if (argc != 1) {
    pop_size = atoi(argv[1]);
    sscanf(argv[2], "%f", &mutate_chance);
    sscanf(argv[3], "%f", &new_children_perc);
    verbose = false;
  } else {
    pop_size = POP_SIZE;
    mutate_chance = MUTATE_CHANCE;
    new_children_perc = NEW_CHILDREN_PERC;
  }

  population = new population_t("data/adult.bin", pop_size, mutate_chance, new_children_perc);
  fittest = population->get_fittest(); best_so_far = fittest;
  while (it < 100) {
    it++;
    population->next_generation();
    if (verbose)
      cout << "Generando poblacion " << it << endl;
    fittest = population->get_fittest();
    if (fittest->fitness > best_so_far->fitness)
    best_so_far = fittest;

    if (verbose)
      cout << "    Fitness:     " << (float)fittest->fitness * 100 << " %%" << endl;
    else
      cout << (float)fittest->fitness << endl;

    if (verbose)
      cout << "    Best so far: " << (float)best_so_far->fitness * 100 << " %%" << endl;
  }
  cout << (float)best_so_far->fitness << endl;
  for(int i = 0; i < best_so_far->rules.size(); i++){
    cout << "Regla" << i << ": p1_ = "<< best_so_far->rules[i]->p1_ << " p2 = " << best_so_far->rules[i]->p2_ <<endl;
  }

  string  f_name = "data/adulttest.bin";
  struct stat results;
  int ts_size = 0;
  if (stat(f_name.c_str(), &results) == 0)
    ts_size = results.st_size/sizeof(long);
  ifstream file(f_name.c_str(), ios::in | ios::binary);
  long * training_set = new long[ts_size];

  file.read((char*)training_set,sizeof(long)*ts_size);
  file.close();
  
  best_so_far->calc_fitness(training_set, ts_size);
  cout << "Fitness Test: " << best_so_far->fitness << endl;
}
