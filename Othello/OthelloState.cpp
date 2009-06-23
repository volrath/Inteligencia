
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <iostream>
#include <algorithm>
#include <sstream>
#include "Neighbor.cpp"

class OthelloState {

public:
    int initState[3];
  short player;
  short numplayer;
  short numotherplayer;

	~OthelloState(){
	}

    OthelloState(){
        player = 0;
	numplayer = 0;
	numotherplayer = 0;
    }

    OthelloState(int initState[36], int player){
        int otherplayer = (player == 1)? 2 : 1;
        int x = 0;
        int y = 0;
        int temp = 0;
        numplayer = 0;
        numotherplayer = 0;
        for(; y < 2; y++){
            for(x = 0; x < 6; x++){
                temp = initState[6*y+x];
                this->initState[0] <<= 2;
                this->initState[0] += temp;
                if(temp == player){
                    numplayer++;
                }else if(temp == otherplayer){
                    numotherplayer++;
                }
            }
        }
        for(;y < 4; y++){
            for(x = 0; x < 6; x++){
                temp = initState[6*y+x];
                this->initState[1] <<= 2;
                this->initState[1] += temp;
                if(temp == player){
                    numplayer++;
                }else if(temp == otherplayer){
                    numotherplayer++;
                }
            }
        }
        for(;y < 6; y++){
            for(x = 0; x < 6; x++){
                temp = initState[6*y+x];
                this->initState[2] <<= 2;
                this->initState[2] += temp;
                if(temp == player){
                    numplayer++;
                }else if(temp == otherplayer){
                    numotherplayer++;
                }
            }
        }
        this->player = player;
    }

    int getValueFromPos(int x, int y){
        int intToUse = 0;
        if(y >= 0 && y <= 1){
            intToUse = initState[0];
            y -= 1;
        }else if(y >= 2 && y <=3 ){
            y -= 3;
            intToUse = initState[1];
        }else if(y >= 4 && y <= 5){
            intToUse = initState[2];
            y-= 5;
        }

        intToUse = intToUse >> 12*(-1*y);
        intToUse = intToUse >> 2*(-1*(x-5));
        return intToUse & 3;
    }

    void setValueAtPos(int x, int y, int value){
        int intToUse = 0;
        int temp = 0;
        int temp2 = 0;
        int otherplayer = this->player == 1? 2: 1;
        int current = getValueFromPos(x,y);
        if(current == 0 && value == this->player){
            this->numplayer++;
        }else if(current == 0 && value == otherplayer){
            this->numotherplayer++;
        }else if(current == this->player && value == 0){
            this->numplayer--;
        }else if(current == this->player && value == otherplayer){
            this->numplayer--;
            this->numotherplayer++;
        }else if(current == otherplayer && value == 0){
            this->numotherplayer--;
        }else if(current == otherplayer && value == this->player){
            this->numotherplayer--;
            this->numplayer++;
        }
        if(y >= 0 && y <= 1){
            intToUse = 0;
            y -= 1;
        }else if(y >= 2 && y <=3 ){
            y -= 3;
            intToUse = 1;
        }else if(y >= 4 && y <= 5){
            intToUse = 2;
            y-= 5;
        }

        temp += value;
        temp2 += 3;
        temp <<= 12*(-1*y) + 2*(-1*(x-5));
        temp2 <<= 12*(-1*y) + 2*(-1*(x-5));
        this->initState[intToUse] &= ~temp2;
        this->initState[intToUse] |= temp;
    }

  std::vector<Neighbor> isValidMove(int x, int y){
    std::vector<Neighbor> moves = std::vector<Neighbor>();
        int value = getValueFromPos(x,y);
        int otherplayer = player == 1 ? 2 : 1;
        if(value == 0) {
	  std::vector<Neighbor> neighbors = hasOtherPlayerNeighbor(x,y,otherplayer);
	  std::vector<Neighbor> possiblemoves = std::vector<Neighbor>();
           Neighbor n;
	   int dir;
           int xn;
           int yn;
           int temp;
           bool foundplayer;
	   int tam = neighbors.size();
           for(int x = 0; x < tam; x++){
        	   n = neighbors.at(x);
        	   possiblemoves = std::vector<Neighbor>();
        	   dir = n.neighbor;
        	   xn = n.first;
        	   yn = n.second;
        	   foundplayer = false;
        	   if(dir  == 0){
        		   for(int i = xn; i < 6; i++){
        			   temp = getValueFromPos(i,yn);
        			   if(player == temp){
        				   foundplayer = true;
        				   break;
        			   }else if(temp == otherplayer){
        				   possiblemoves.push_back(Neighbor(i,yn,0));
        			   }else if(temp == 0){
        				   possiblemoves.clear();
        				   break;
        			   }
        		   }
                }else if(dir  == 1){
                    for(int i = xn, j = yn; i < 6 && j > -1; i++, j--){
                        temp = getValueFromPos(i,j);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(i,j,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
                    }
                }else if(dir  == 2){
                    for(int i = xn, j = yn; i < 6 && j < 6; i++, j++){
                        temp = getValueFromPos(i,j);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(i,j,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
					}
                }else if(dir  == 3){
                    for(int j = yn; j > -1; j--){
                        temp = getValueFromPos(xn,j);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(xn,j,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
                    }
                }else if(dir  == 4){
                    for(int i = xn, j = yn; i > -1 && j > -1; i--, j--){
                        temp = getValueFromPos(i,j);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(i,j,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
                    }
                }else if(dir == 5){
                    for(int i = xn; i > -1; i--){
                        temp = getValueFromPos(i,yn);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(i,yn,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
                    }
                }else if(dir == 6){
                    for(int i = xn, j = yn; i > -1 && j < 6; i--, j++){
                        temp = getValueFromPos(i,j);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(i,j,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
                    }
                }else if(dir == 7){
                    for(int j = yn; j < 6; j++){
                        temp = getValueFromPos(xn,j);
                        if(player == temp){
                            foundplayer = true;
                            break;
                        }else if(temp == otherplayer){
                            possiblemoves.push_back(Neighbor(xn,j,0));
                        }else if(temp == 0){
                        	possiblemoves.clear();
                        	break;
                        }
                    }
                }
                if(foundplayer){
		  int tam = possiblemoves.size();
		  for(int m = 0; m < tam; m++){
		    moves.push_back(possiblemoves.at(m));
		  }
                }
           }
        }
        return moves;
    }

  std::vector<Neighbor> hasOtherPlayerNeighbor(int x, int y, int otherplayer){
    std::vector<int> xs = std::vector<int>();
    std::vector<int> ys = std::vector<int>();
    std::vector<Neighbor> neighbors = std::vector<Neighbor>();
        int xn = 0;
        int yn = 0;
        if(x == 0){
            xs.push_back(x);
            xs.push_back(x+1);
        }else{
            xs.push_back(x);
            xs.push_back(x+1);
            xs.push_back(x-1);
        }
        if(y == 0){
	  ys.push_back(y);
	  ys.push_back(y+1);
        }else{
            ys.push_back(y);
            ys.push_back(y+1);
            ys.push_back(y-1);
        }
	int tamx = xs.size();
	int tamy = ys.size();
        for(int i = 0; i < tamx; i++){
            for(int j = 0; j < tamy; j++){
                xn = xs.at(i);
                yn = ys.at(j);
                if(otherplayer == getValueFromPos(xn,yn)){		  
                    if(xn == x - 1){
		      if(yn == y - 1){
                        neighbors.push_back(Neighbor(xn,yn,4));
		      }else if(yn == y){
                        neighbors.push_back(Neighbor(xn,yn,5));
		      }else if(yn == y + 1){
			neighbors.push_back(Neighbor(xn,yn,6));
		      }
		    }else if(xn == x + 1){
		      if(yn == y){
                        neighbors.push_back(Neighbor(xn,yn,0));
		      }else if(yn == y + 1){
                        neighbors.push_back(Neighbor(xn,yn,2));
		      }else if(yn == y - 1){
                        neighbors.push_back(Neighbor(xn,yn,1));
		      }
		    }else if(xn == x){
		      if(yn == y - 1){
                        neighbors.push_back(Neighbor(xn,yn,3));
		      }else if(yn == y + 1){
		        neighbors.push_back(Neighbor(xn,yn,7));
		      }
		    }
                }
            }
        }
        return neighbors;
    }

  std::vector<OthelloState> getPossibleStates(){
	  std::vector<OthelloState> vstates = std::vector<OthelloState>();
	  std::vector<Neighbor> moves;
	  Neighbor ntemp;
	  int tammoves = 0;
	  int otherplayer = player == 1? 2:1;
	  for(int x = 0; x < 6; x++){
		  for(int y = 0; y < 6; y++){
			  moves = isValidMove(x,y);
			  tammoves = moves.size();
			  if(tammoves != 0){
				  OthelloState stattemp = OthelloState();
				  stattemp.initState[0] = this->initState[0];
				  stattemp.initState[1] = this->initState[1];
				  stattemp.initState[2] = this->initState[2];
				  stattemp.player = otherplayer;
				  stattemp.numplayer = this->numotherplayer;
				  stattemp.numotherplayer = this->numplayer;
				  stattemp.setValueAtPos(x, y, this->player);
				  for(int i = 0; i < tammoves; i++){
					  ntemp = moves.at(i);
					  stattemp.setValueAtPos(ntemp.first, ntemp.second, player);
				  }
				  vstates.push_back(stattemp);
			  }
		  }
	  }
	  return vstates;
    }

  std::string toString(){
    std::string result = "";
        for(int x = 0; x < 6; x++){
            for(int y = 0; y < 6; y++){
	      std::stringstream str;
	      str << getValueFromPos(y,x);
	      result += str.str()+" ";
            }
        }
        return result;
    }

  std::string toPrettyString(){
    std::string result = "";
        for(int x = 0; x < 6; x++){
            for(int y = 0; y < 6; y++){
	      std::stringstream str;
	      str << getValueFromPos(y,x);
	      result += " | "+str.str();
            }
            result+= "| \n";
        }
        return result;
    }

    bool hasPossibleMove(){
      std::vector<Neighbor> moves;
        for(int x = 0; x < 6; x++){
            for(int y = 0; y < 6; y++){
                moves = isValidMove(x,y);
                if(!moves.empty()){
                	return true;
                }
            }
        }
        return false;
    }

    OthelloState bestNextMove(){
      short v = -9999;
      short previousv = v;
      short alpha = -9999;
      short beta = 9999;
        if(this->noMoreMoves()){
            return *this;
        }
	std::vector<OthelloState> moves = this->getPossibleStates();
        for(int i = 0; i < moves.size(); i++){
            std::cout << moves[i].toPrettyString() << "\n";
        }
        OthelloState best = *this;
        OthelloState temp;
        if(moves.empty()){
        	temp = OthelloState();
        	temp.initState[0] = this->initState[0];
        	temp.initState[1] = this->initState[1];
        	temp.initState[2] = this->initState[2];
        	temp.player = player == 1? 2 : 1;
        	temp.numplayer = this->numotherplayer;
        	temp.numotherplayer = this->numplayer;
        	v = std::max(v, temp.minValue(alpha,beta));
        	if(v != previousv){
				best = temp;
                previousv = v;
            }
            if(v >= beta){
                return temp;
            }
            alpha = std::max(alpha, v);
        }else{
	  int tam = moves.size();
        	for(int i = 0; i < tam; i++){
				temp = moves.at(i);
        		v = std::max(v, temp.minValue(alpha,beta));
        	if(v != previousv){
            	best = temp;
            	previousv = v;
            }
            if(v >= beta){
            	return temp;
            }
            alpha = std::max(alpha, v);
        	}
        }
        return best;
  }

  short maxValue(short alpha, short beta){
      short v = -9999;
        if(this->noMoreMoves()){
            return this->numplayer;
        }
	std::vector<OthelloState> moves = this->getPossibleStates();
	if(moves.empty()){
	  OthelloState temp = OthelloState();
	  temp.initState[0] = this->initState[0];
	  temp.initState[1] = this->initState[1];
	  temp.initState[2] = this->initState[2];
	  temp.player = player == 1? 2 : 1;
	  temp.numplayer = this->numotherplayer;
	  temp.numotherplayer = this->numplayer;
	  v = std::max(v, temp.minValue(alpha,beta));
	  if(v >= beta){
		return v;
	  }
	  alpha = std::max(alpha, v);
	}else{
	  for(int i = 0; i < moves.size(); i++){
	    v = std::max(v, moves.at(i).minValue(alpha,beta));
            if(v >= beta){
            	return v;
            }
            alpha = std::max(alpha, v);
	  }
	}
	return v;
  }

  short minValue(short alpha, short beta){
      short v = 9999;
        if(this->noMoreMoves()){
            return this->numplayer;
        }
	std::vector<OthelloState> moves = this->getPossibleStates();
	if(moves.empty()){
	  OthelloState temp = OthelloState();
	  temp.initState[0] = this->initState[0];
	  temp.initState[1] = this->initState[1];
	  temp.initState[2] = this->initState[2];
	  temp.player = player == 1? 2 : 1;
	  temp.numplayer = this->numotherplayer;
	  temp.numotherplayer = this->numplayer;
	  v = std::min(v, temp.minValue(alpha,beta));
	  if(v <= alpha){
	    return v;
	  }
	  beta = std::min(beta, v);
	}else{
	  for(int i = 0; i < moves.size(); i++){
	    v = std::min(v, moves.at(i).maxValue(alpha,beta));
            if(v <= alpha){
            	return v;
            }
            beta = std::min(beta, v);
	  }
	}
    return v;
 }

    bool noMoreMoves(){
      OthelloState state = OthelloState();
      state.initState[0] = this->initState[0];
      state.initState[1] = this->initState[1];
      state.initState[2] = this->initState[2];
      state.player = this->player;
      if(state.hasPossibleMove()){
    	  return false;
      }
      state.player = this->player == 1 ? 2 : 1;
      if(state.hasPossibleMove()){
    	  return false;
      }
      return true;
    }
};
