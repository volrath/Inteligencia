#include <stdio.h>
#include <string>
#include <stdlib.h>
#include "OthelloState.cpp"

OthelloState othello1 (int argc, std::string argv){
  int * state = new int[36];
  for(int i = 0; i < argc; i++){
    state[i] = atoi(argv.substr(i*2,1).c_str());
  }
  OthelloState otate = OthelloState(state,1);
  return otate.bestNextMove();
}

OthelloState othello2 (int argc, std::string argv){
  int * state = new int[36];
  for(int i = 0; i < argc; i++){
    state[i] = atoi(argv.substr(i*2,1).c_str());
  }
  OthelloState otate = OthelloState(state,2);
  return otate.bestNextMove();
}


int main(int argc, char ** argv){
    int * state = new int[36];
    for(int i = 1; i < argc; i++){
        state[i-1] = atoi(argv[i]);
    }
    OthelloState otate = OthelloState(state, 2);
    //    while(!otate.noMoreMoves()){
        otate = othello1(36, otate.toString());
	/*  if(otate.noMoreMoves()){
            break;
        }
        printf("%s\n",otate.toPrettyString().c_str());
        otate = othello2(36, otate.toString());
        printf("%s\n",otate.toPrettyString().c_str());
	}*/
	std::cout << otate.toPrettyString();
}
