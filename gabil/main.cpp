#include "gabil.h"

using namespace std;

int main(int argc, char **argv) {
  hypothesis_t *fittest;
  population_t *population;
  float best_fitness = 0.;
  int it = 0;

  // read file, parsed if possible.

  population = new population_t();
  while (true) { // dont know which stop condition =S
    cout << "Generando poblacion " << ++it << endl;
    fittest = population->get_fittest();
    if (fittest->fitness > best_fitness)
      best_fitness = fittest->fitness;

    cout << "    Fitness:     " << (float)fittest->fitness * 100 << " %%" << endl;
    cout << "    Best so far: " << (float)best_fitness * 100 << " %%" << endl;
  }
}
