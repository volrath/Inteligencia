#include "eval.h"

int numeval = 0;

void getParents(double  poblacion[1000][10], double parents[2][10]){
  int i;
  int imadre = 0;
  int ipadre = 9;
  int itemp;
  double evalpadre = -999;
  double evalmadre = -999;
  double temp;
  for(i = 0; i < 1000; i++){
    numeval++;
    temp = evaluar(poblacion[i]);
    if((((temp > evalpadre)) && ((double)rand()/((double)(RAND_MAX)+(double)(1)) < 0.6)) || ((double)rand()/((double)(RAND_MAX)+(double)(1)) < 0.2)){
      evalmadre = evalpadre;
      imadre = ipadre;
      ipadre = i;
      evalpadre = temp;
    }else if(((temp > evalmadre)  && ((double)rand()/((double)(RAND_MAX)+(double)(1)) < 0.6)) ||  ((double)rand()/((double)(RAND_MAX)+(double)(1)) < 0.2)){
      imadre = i;
      evalmadre = temp;      
    }
  }
  memcpy(parents[0],poblacion[ipadre],sizeof(double)*10);
  memcpy(parents[1],poblacion[imadre],sizeof(double)*10); 
}

void reproducir(double padre[10], double madre[10], double hijo[10]){
  int corte = rand()%10;
  double plus;
  int i;
  for(i = 0; i < corte; i++){
    plus = (rand()%10 < 5? 1 : -1) *((double)((double)0.1/(double)(10^(rand()%5))));
    if(padre[i] + plus >= -1 && padre[i] + plus <= 1){
      hijo[i] = padre[i] + plus;
    }else{
      hijo[i] = padre[i];
    }
  }
  for(; i < 10; i++){
    plus = (rand()%10 < 5? 1 : -1) *((double)((double)0.1/(double)(10^(rand()%5))));
    if(madre[i] + plus >= -1 && madre[i] + plus <= 1){
      hijo[i] = madre[i] + plus;
    }else{
      hijo[i] = madre[i];
    }
  }
}

void mutar(double hijo[10]){
  int corte = rand()%10;
  double plus;
  int i = 0;
  plus = (rand()%10 < 5? 1 : -1) *((double)((double)0.1/(double)(10^(rand()%5))));
  if(hijo[corte] + plus <= 1 && hijo[corte] + plus >= -1){
    hijo[corte] = hijo[corte] + plus;
  }
}

double geneticAlgorithm(double bestA[10]){
  srand(time(NULL));
  double poblacion [1000][10];
  double poblacionN [1000][10];
  double parents[2][10];
  double hijo [10];
  double bestsofarP[10];
  double resultN[10];
  double bestsofar = -999;
  double prevresult = -999;
  double temp;
  int repetidos = 0;
  int j = 0;
  int i = 0;
  for(i = 0; i < 1000; i++) {
    for(j = 0; j < 10; j++) {
      poblacion[i][j] = (rand()%10 < 5 ? -1 : 1)*(double)rand() / ((double)(RAND_MAX)+(double)(1));
    }
  }
  int k,n = 0;
  double result = -999;
  while(result  < 9 && numeval <= NUMEVAL){
      prevresult = result;
      for(i = 0; i < 1000; i++){
	getParents(poblacion,parents);	
	reproducir(parents[0],parents[1],hijo);
	if(rand()%10 < 1){
	  mutar(hijo);
	}
	temp = evaluar(hijo);
	numeval++;
	if(temp > result){
	  result = temp;
	  memcpy(&resultN, &hijo, sizeof(double)*10);
	}
	memcpy(&poblacionN[i], &hijo,sizeof(double)*10);
      }
      memcpy(&poblacion,&poblacionN,sizeof(double)*10000);
      if(result > bestsofar) {
	repetidos = 0;
	bestsofar = result; 
	memcpy(bestsofarP, resultN,sizeof(double)*10);
      }else if(result == prevresult) {
	if(repetidos == 5){
	  result = bestsofar;
	  break;
	}
	repetidos++;
      }else if(result != prevresult) {
	repetidos = 0;
      }
      printf("BestSoFar: %lf Prev: %lf, Result: %lf \n",bestsofar,prevresult,result);
    }
  memcpy(bestA,bestsofarP,sizeof(double)*10);
  return result;
}

int main(){
  double result;
  double bestsofar = -999;
  double bestA[10];
  double ss[10];
  int i;
  int k;
  while(result < 9 && numeval <= NUMEVAL){
    result = geneticAlgorithm(ss);
    if(result > bestsofar) {
      bestsofar = result;
      memcpy(bestA,ss,sizeof(double)*10);
    }
    printf("\nBestSoFarGeneral: %lf No. Eval: %d\n", bestsofar, numeval);
    for(k = 0; k<10;k++){
      printf("\nBSF[%d]: %lf",k,bestA[k]);
    }
  }
}
