#include "gabil.h"
#include <fstream>

using namespace std;

int main(int argc, char **argv) {
  hypothesis_t *fittest;
  population_t *population;
  float best_fitness = 0.;
  int it = 0;

  // read file, parsed if possible.
//   ifstream::pos_type size;
//   ifstream file("data/adult.bin", ios::in | ios::binary);
//   size = file.tellg();
//   char *creprs = new char[size];

//   file.seekg(0, ios::beg);
//   file.read(creprs, size);
//   file.close();

//   // Put it on a 'long array'
//   unsigned long *reprs = (unsigned long *)creprs;
//   delete [] creprs;

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
