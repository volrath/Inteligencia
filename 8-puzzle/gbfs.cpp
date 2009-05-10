#include <iostream>
#include <queue>
#include "ex_hash.cc"
using namespace std;

/*class Queue{
public:
  Queue();
  ~Queue();
  bool isEmpty();
  void add(value_t*);
  value_t* get();
private:
  value_t * first, last;
}

  Queue::Queue() {
    first = new value_t;
    first->second.next_ = NULL;
    last = first;
  }

Queue::~Queue(){
  delete first;
}

bool Queue::isEmpty(){
  return (first->second.next_ = NULL);
}

void Queue::add(value_t* n){
  n->second.next_  = NULL;
  last->second.next_ = n;
}

value_t* Queue::get(){
  
}
*/
class value_comparison{
public:
  value_comparison(){}
  bool operator() (const value_t* lhs, const value_t* rhs) const{
    return(lhs->second.f() < rhs->second.f());
  }

};

int main (int argc , char** argv){
  typedef priority_queue<value_t*,vector<value_t*>,value_comparison> my_queue;
  my_queue qp;
  value_t*  vp = new value_t;
  vp->second.g_ = 10;
  value_t*  vp2 = new value_t;
  vp2->second.g_ = 8;
  value_t* vp3 = new value_t;
  vp3->second.g_ = 100;
  value_t* vp4 = new value_t;
  vp4->second.g_ = 1;
  qp.push(vp);
  qp.push(vp2);
  qp.push(vp3);
  qp.push(vp4);
  cout << qp.top()->second.f();
  qp.pop();
  cout << qp.top()->second.f();
  qp.pop();
  cout << qp.top()->second.f();
  qp.pop();
  cout << qp.top()->second.f();
  return 0;
}
