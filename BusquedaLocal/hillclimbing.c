#include "eval.h"

int numeval = 0;

void getNeighbours(double currentNode[10], double neighbours[10][10]){
  int i = 0;
  int j = 0;
  double plus = (rand()%10 < 5 ? -1 : 1)*0.0001;
  for(i = 0; i < 10; i++){
    for(j = 0; j < i; j++){
      neighbours[i][j] = currentNode[j];
    }
    if(currentNode[j]+plus <= 1 && currentNode[j]+plus >= -1){
      neighbours[i][j] = (currentNode[j]) + plus;
    }
    for(j = i+1; j < 10; j++){
      neighbours[i][j] = currentNode[j];
    }
  }
}

double hillClimbing(double  x[10], double bsf[10]){
  double currentNode[10];
  memcpy(currentNode, x,sizeof(double)*10);
  double neighbors[10][10];
  int maxRange = 0;
  double nextEval = -9999;
  double nextNode[10];
  double currentEval = evaluar(currentNode);
  double temp;
  int i =0;
  while(numeval <= NUMEVAL){
    getNeighbours(currentNode,neighbors);
    nextEval = -99999;
    int i = 0;
    for(i = 0; i < 10; i++){
      numeval++;
      temp = evaluar(neighbors[i]);
      if (temp > nextEval){
	memcpy(nextNode,neighbors[i],sizeof(double)*10);
	nextEval = temp;
      }
    }

    if (nextEval <= currentEval){
	memcpy(bsf,currentNode,sizeof(double)*10);
	return currentEval;
    }
    memcpy(currentNode,nextNode,sizeof(double)*10);
    currentEval = nextEval;
  }
}

int main(){
  double result;
  double bestsofar = -999;
  double resultA[10];
  double bestA[10];
  double randA[10];
  int i;
  while(result < 8 && numeval <= NUMEVAL){
    srand(time(NULL));
    for(i = 0; i < 10; i++){
      randA[i] = (rand()%10 < 5 ? -1 : 1)*(double)rand() / ((double)(RAND_MAX)+(double)(1));
    }
    result = hillClimbing(randA,resultA);
    if(result > bestsofar){
      bestsofar = result;
      memcpy(bestA,resultA,sizeof(double)*10);
      for(i = 0; i < 10; i++){
	printf("\nBSF[%d]: %lf",i,bestA[i]);      
      }
      printf("\nBestSoFarGeneral: %lf No. Eval: %d\n", bestsofar,numeval);
    }
  }
  printf("No. Eval FINAL: %d\n", numeval);
}
