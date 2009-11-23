#include "gabil.h"
#include <sys/stat.h>
#include <fstream>

using namespace std;

int main(int argc, char **argv) {
  hypothesis_t *fittest, *best_so_far;
  population_t *population;
  struct stat results;
  int size;
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
