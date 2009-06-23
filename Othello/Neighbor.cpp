#include <string>

class Neighbor{
public:
  int first;
  int second;
  int neighbor;
  Neighbor(int f, int s, int dir){
    this->first = f;
    this->second = s;
    this->neighbor = dir;
  }
  Neighbor(){
    this-> first = 0;
    this-> second = 0;
    this-> neighbor = 0;
  }
  ~Neighbor(){

  }
};
