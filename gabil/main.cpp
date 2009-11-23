#include "gabil.h"

using namespace std;

int main(int argc, char **argv) {
  hypothesis_t *fittest, *best_so_far;
  population_t *population;
  int it = 0;
 
  // read file, parsed if possible.

  /*
   * Args
   */
  int pop_size;
  float mutate_chance, new_children_perc;
  if (argc != 1) {
    pop_size = atoi(argv[1]);
    sscanf(argv[2], "%f", &mutate_chance);
    sscanf(argv[3], "%f", &new_children_perc);
  } else {    
    pop_size = POP_SIZE;
    mutate_chance = MUTATE_CHANCE;
    new_children_perc = NEW_CHILDREN_PERC;
  }

  population = new population_t("data/adult.bin", pop_size, mutate_chance, new_children_perc);

  while (true) { // dont know which stop condition =S
    cout << "Generando poblacion " << ++it << endl;
    fittest = population->get_fittest();
    if (fittest->fitness > best_so_far->fitness)
    best_so_far = fittest;
    
    cout << "    Fitness:     " << (float)fittest->fitness * 100 << " %%" << endl;
    cout << "    Best so far: " << (float)best_so_far->fitness * 100 << " %%" << endl;
    population->next_generation();
 }
}
