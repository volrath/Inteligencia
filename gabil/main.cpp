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
  while (it < 50) {
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
}
