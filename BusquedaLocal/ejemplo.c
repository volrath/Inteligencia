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
  numeval++;
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

int comparar_pos(double a[10], double b[10]){
  numeval += 2;
  return (double) (evaluar(b) - evaluar(a));
}

void getParents(double  poblacion[5000][10], double parents[100][10]){
  int i;
  int imadre = 0;
  int ipadre = 9;
  int itemp;
  double evalpadre = -999;
  double evalmadre = -999;
  double temp;
   qsort(poblacion,5000,sizeof(double)*10,comparar_pos);  
  memcpy(parents,poblacion,sizeof(double)*1000);
}

void reproducir(double padre[10], double madre[10], double hijo[10]){
  int corte = rand()%10;
  int plus = (rand()%10 < 5? -1: 1) * 0.0001;
  int i;
  //printf("CORTE:%d",corte);
  for(i = 0; i < corte; i++){
      hijo[i] = padre[i];
  }
  for(; i < 10; i++){
      hijo[i] = madre[i];    
  }
}

void mutar(double hijo[10]){
  int corte = rand()%10;
  int plus;
  int i = 0;
  plus = (rand()%10 < 5? 1 : -1) * 0.0001;
  if(hijo[corte] + plus <= 1 && hijo[corte] + plus >= -1){
    hijo[corte] = hijo[corte] + plus;
  }
}

double geneticAlgorithm(double best[10]){
  srand(time(NULL));
  double poblacion [5000][10];
  double poblacionN [5000][10];
  double parents[100][10];
  double hijo [10];
  double bestsofarP[10];
  double resultN[10];
  double bestsofar = -999;
  double prevresult = -999;
  double temp;
  int repetidos = 0;
  int ipadre;
  int imadre;
  int j = 0;
  int i = 0;
  for(i = 0; i < 5000; i++){
    for(j = 0; j < 10; j++){
      poblacion[i][j] = (rand()%10 < 5 ? -1 : 1)*(double)rand() / ((double)(RAND_MAX)+(double)(1));
    }
  }
  int k,n = 0;
  double result = -999;
    while(result  < 10 && numeval <= NUMEVAL) {
      prevresult = result;
      srand(time(NULL));
      getParents(poblacion,parents);	
      for(i = 0; i < 5000; i++){
	ipadre = rand()%100;
	imadre = rand()%100;
	reproducir(parents[ipadre],parents[imadre],hijo);
	if(rand()%10 < 1){
	  mutar(hijo);
	}
	temp = evaluar(hijo);
	numeval++;
	if(temp > result){
	  result = temp;
	  memcpy(&resultN,&hijo,sizeof(double)*10);
	}
	memcpy(&poblacionN[i], &hijo,sizeof(double)*10);
      }
      memcpy(&poblacion,&poblacionN,sizeof(double)*50000);
      if(result > bestsofar) {
	repetidos = 0;
	bestsofar = result; 
	memcpy(bestsofarP, resultN,sizeof(double)*10);
      }else if(result == prevresult) {
	repetidos++;
	if(repetidos == 5){
	  result = bestsofar;
	  break;
	}
      }else if(result != prevresult){
	repetidos = 0;
      }
      printf("BestSoFar: %lf Prev: %lf, Result: %lf\n",bestsofar,prevresult,result);
    }
    printf("PosBestSoFar: ");
    for(k = 0; k < 10; k++){
      printf("bestsofar[%d] : %lf",k,bestsofarP[k]);
    }
    memcpy(best,bestsofarP,sizeof(double)*10);
    return result;
}

int main() {
  double result;
  double bestsofar = -999;
  double bestA[10];
  double ss[10];
  int i;
  while(result < 9 && numeval <= NUMEVAL) {
    srand(time(NULL));
    result = geneticAlgorithm(ss);
    if(result > bestsofar) {
      bestsofar = result;
      memcpy(bestA,ss,sizeof(double)*10);
    }
    printf("\nBestSoFarGeneral: %lf No. Eval: %d \n", bestsofar,numeval);
  }

  while(result < 10 && numeval <= NUMEVAL) {
    srand(time(NULL));
    result = simulatedAnnealing(300000,ss);
    if(result > bestsofar) {
      bestsofar = result;
      memcpy(bestA,ss,sizeof(double)*10);
    }
    for(i = 0; i < 10; i++){
      printf("\nBSF[%d]: %lf",i,bestA[i]);
    }
    printf("\nBestSoFarGeneral: %lf No. Eval: %d \n", bestsofar,numeval);
  }  
}
