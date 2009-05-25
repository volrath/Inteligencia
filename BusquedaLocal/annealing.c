#include "ejemplo.h"

int numeval = 0;

void getRandomNeighbor(double s[10], double sn[10]){
  int corte;
  int num = rand()%10;
  int i;
  double plus;
  memcpy(sn,s,sizeof(double)*10);
  for(i = 0; i < num; i++){
    corte = rand()%10;
    plus = (rand()%10 < 5? -1 : 1)*((double)((double)0.1/(double)(10^(rand()%5))));
    if(s[corte] + plus <= 1 && s[corte] + plus >= -1){
      sn[corte] = s[corte] + plus;
    }
  }
}



double enfriamiento(double e, double en, double T){
  if(en > e){
    return 1; 
  }else{
    return exp(((e-en) < 0? (e-en): -1*(e-en))/T);
  }
}

double temp(double e, double en){
  if(e >= 5 && ((e-en) < 0? -1*(e-en): (e-en) ) < 1){
    return 6;
  } else if(e >= 9 && ((e-en) < 0? -1*(e-en): (e-en) ) > 1){
    return 0.001;
  }else if(e >= -10 &&  ((e-en) < 0? -1*(e-en): (e-en) ) > 10){
    return 0.001;
  } else if(e <= -30 &&  ((e-en) < 0? -1*(e-en): (e-en) ) > 0.1){
    return 0.001;
  }else{
    return 1;
  }
}

double simulatedAnnealing(int kmax, double ss[10]){
  double s[10];
  memcpy(s,ss,sizeof(double)*10);
  int i;
  
  double sb[10];
  double sn[10];
  double e = evaluar(s);
  memcpy(sb,s,sizeof(double)*10);
  double eb = e;
  double en;
  int k = 0;
  while(k < kmax && e < 10){
    getRandomNeighbor(s, sn);
    en = evaluar(sn);
    numeval++;
    if(en > eb){
      memcpy(sb,sn,sizeof(double)*10);
      eb = en;
    }
    if(enfriamiento(e,en,temp(e,en)) > ((double)rand() / ((double)(RAND_MAX)+(double)(1)))){
      memcpy(s,sn,sizeof(double)*10);
      e = en;
    }
    k++;
  }

  memcpy(ss,sb,sizeof(double)*10);    
  return eb;
}

int main(){
  double result;
  double bestsofar = -999;
  double bestA[10];
  double ss[10];
  srand(time(NULL)
  int i;
  while(result < 10 && numeval <= NUMEVAL){
    srand(time(NULL));  
    result = simulatedAnnealing(300000,ss);
    if(result > bestsofar) {
      bestsofar = result;
      memcpy(bestA,ss,sizeof(double)*10);
    }else{
      if(result < 0){
	memcpy(ss,bestA,sizeof(double)*10);
      }
    }
    for(i = 0; i < 10; i++){
      printf("\nBSF[%d]: %lf",i,bestA[i]);      
    }
    printf("\nBestSoFarGeneral: %lf No. Eval: %d \n", bestsofar,numeval);
  }
}
