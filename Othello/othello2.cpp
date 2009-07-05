#include <stdio.h>
#include <string>
#include <stdlib.h>
#include "othello_cut.cpp"

int main(int argc , char ** argv){
    int * state = new int[36];
    for(int i = 1; i < argc; i++){
        state[i-1] = atoi(argv[i]);
    }
    state_t stat = state_t();
    stat.set_state(state);
    state_t best = stat.bestNextMove();
    best.print(std::cout,10);
}
