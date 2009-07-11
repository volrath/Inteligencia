// Game of Othello -- Cut from full code
// Universidad Simon Bolivar, 2005.
// Author: Blai Bonet
// Last Revision: 05/17/06
// Modified by: Kristoffer Pantic 05-38675
 
#include <iostream>
#include <vector>
#include <algorithm>
#include "assert.h"

#define MAX(s,t)      ((s)>(t)?(s):(t))
#define MIN(s,t)      ((s)<(t)?(s):(t))
#define MAXVALUE      1000
#define DIM           36
#define N             6
#define LIMIT     14

const int rows[][7] = { { 4, 5, 6, 7, 8, 9,-1 }, { 4, 5, 6, 7, 8, 9,-1 }, { 4, 5, 6, 7, 8, 9,-1 },
                        { 4, 5, 6, 7, 8, 9,-1 }, { 4, 5, 6, 7, 8, 9,-1 }, { 4, 5, 6, 7, 8, 9,-1 },
                        {10,11,12,13,14,15,-1 }, {10,11,12,13,14,15,-1 }, {10,11,12,13,14,15,-1 },
                        {10,11,12,13,14,15,-1 }, {10,11,12,13,14,15,-1 }, {10,11,12,13,14,15,-1 },
                        {16,17, 0, 1,18,19,-1 }, {16,17, 0, 1,18,19,-1 },
                        {16,17, 0, 1,18,19,-1 }, {16,17, 0, 1,18,19,-1 },
                        {20,21, 2, 3,22,23,-1 }, {20,21, 2, 3,22,23,-1 },
                        {20,21, 2, 3,22,23,-1 }, {20,21, 2, 3,22,23,-1 },
                        {24,25,26,27,28,29,-1 }, {24,25,26,27,28,29,-1 }, {24,25,26,27,28,29,-1 },
                        {24,25,26,27,28,29,-1 }, {24,25,26,27,28,29,-1 }, {24,25,26,27,28,29,-1 },
                        {30,31,32,33,34,35,-1 }, {30,31,32,33,34,35,-1 }, {30,31,32,33,34,35,-1 },
                        {30,31,32,33,34,35,-1 }, {30,31,32,33,34,35,-1 }, {30,31,32,33,34,35,-1 } };
const int cols[][7] = { { 4,10,16,20,24,30,-1 }, { 5,11,17,21,25,31,-1 }, { 6,12, 0, 2,26,32,-1 },
                        { 7,13, 1, 3,27,33,-1 }, { 8,14,18,22,28,34,-1 }, { 9,15,19,23,29,35,-1 },
                        { 4,10,16,20,24,30,-1 }, { 5,11,17,21,25,31,-1 }, { 6,12, 0, 2,26,32,-1 },
                        { 7,13, 1, 3,27,33,-1 }, { 8,14,18,22,28,34,-1 }, { 9,15,19,23,29,35,-1 },
                        { 4,10,16,20,24,30,-1 }, { 5,11,17,21,25,31,-1 },
                        { 8,14,18,22,28,34,-1 }, { 9,15,19,23,29,35,-1 },
                        { 4,10,16,20,24,30,-1 }, { 5,11,17,21,25,31,-1 },
                        { 8,14,18,22,28,34,-1 }, { 9,15,19,23,29,35,-1 },
                        { 4,10,16,20,24,30,-1 }, { 5,11,17,21,25,31,-1 }, { 6,12, 0, 2,26,32,-1 },
                        { 7,13, 1, 3,27,33,-1 }, { 8,14,18,22,28,34,-1 }, { 9,15,19,23,29,35,-1 },
                        { 4,10,16,20,24,30,-1 }, { 5,11,17,21,25,31,-1 }, { 6,12, 0, 2,26,32,-1 },
                        { 7,13, 1, 3,27,33,-1 }, { 8,14,18,22,28,34,-1 }, { 9,15,19,23,29,35,-1 } };
const int dia1[][7] = { { 4,11, 0, 3,28,35,-1 }, { 5,12, 1,22,29,-1,-1 }, { 6,13,18,23,-1,-1,-1 },
                        { 7,14,19,-1,-1,-1,-1 }, { 8,15,-1,-1,-1,-1,-1 }, { 9,-1,-1,-1,-1,-1,-1 },
                        {10,17, 2,27,34,-1,-1 }, { 4,11, 0, 3,28,35,-1 }, { 5,12, 1,22,29,-1,-1 },
                        { 6,13,18,23,-1,-1,-1 }, { 7,14,19,-1,-1,-1,-1 }, { 8,15,-1,-1,-1,-1,-1 },
                        {16,21,26,33,-1,-1,-1 }, {10,17, 2,27,34,-1,-1 },
                        { 6,13,18,23,-1,-1,-1 }, { 7,14,19,-1,-1,-1,-1 },
                        {20,25,32,-1,-1,-1,-1 }, {16,21,26,33,-1,-1,-1 },
                        { 5,12, 1,22,29,-1,-1 }, { 6,13,18,23,-1,-1,-1 },
                        {24,31,-1,-1,-1,-1,-1 }, {20,25,32,-1,-1,-1,-1 }, {16,21,26,33,-1,-1,-1 },
                        {10,17, 2,27,34,-1,-1 }, { 4,11, 0, 3,28,35,-1 }, { 5,12, 1,22,29,-1,-1 },
                        {30,-1,-1,-1,-1,-1,-1 }, {24,31,-1,-1,-1,-1,-1 }, {20,25,32,-1,-1,-1,-1 },
                        {16,21,26,33,-1,-1,-1 }, {10,17, 2,27,34,-1,-1 }, { 4,11, 0, 3,28,35,-1 } };
const int dia2[][7] = { { 4,-1,-1,-1,-1,-1,-1 }, { 5,10,-1,-1,-1,-1,-1 }, { 6,11,16,-1,-1,-1,-1 },
                        { 7,12,17,20,-1,-1,-1 }, { 8,13, 0,21,24,-1,-1 }, { 9,14, 1, 2,25,30,-1 },
                        { 5,10,-1,-1,-1,-1,-1 }, { 6,11,16,-1,-1,-1,-1 }, { 7,12,17,20,-1,-1,-1 },
                        { 8,13, 0,21,24,-1,-1 }, { 9,14, 1, 2,25,30,-1 }, {15,18, 3,26,31,-1,-1 },
                        { 6,11,16,-1,-1,-1,-1 }, { 7,12,17,20,-1,-1,-1 },
                        {15,18, 3,26,31,-1,-1 }, {19,22,27,32,-1,-1,-1 },
                        { 7,12,17,20,-1,-1,-1 }, { 8,13, 0,21,24,-1,-1 },
                        {19,22,27,32,-1,-1,-1 }, {23,28,33,-1,-1,-1,-1 },
                        { 8,13, 0,21,24,-1,-1 }, { 9,14, 1, 2,25,30,-1 }, {15,18, 3,26,31,-1,-1 },
                        {19,22,27,32,-1,-1,-1 }, {23,28,33,-1,-1,-1,-1 }, {29,34,-1,-1,-1,-1,-1 },
                        { 9,14, 1, 2,25,30,-1 }, {15,18, 3,26,31,-1,-1 }, {19,22,27,32,-1,-1,-1 },
                        {23,28,33,-1,-1,-1,-1 }, {29,34,-1,-1,-1,-1,-1 }, {35,-1,-1,-1,-1,-1,-1 } };

// moves on the principal variation
static int PV[] = { 12, 21, 26, 13, 22, 18,  7,  6,  5, 27, 33, 23, 17, 11, 19, 15,
                    14, 31, 20, 32, 30, 10, 25, 24, 34, 28, 16,  4, 29, 35, 36,  8, 9 };

const int valueTablero[] = {50,50,50,50,
			    1000, -30, 100, 100, -30, 1000,
			    -30, -50, -30,-30, -50, -30,
			    100, -30,-30, 100,
			    100, -30, -30, 100, 
			    -30, -50, -30, -30, -50, -30,
			    1000, -30, 100, 100, -30, 1000};
const int ordenEvaluacion[] = {4, 9, 30, 35,6,7, 16, 19, 20, 23, 32, 33,
			       12, 13, 17, 18, 21, 22, 26, 27, 5,8, 
			       10, 15, 24, 29, 31, 34, 11, 14, 25, 28};

class state_t
{
  unsigned char t_;
  unsigned free_;
  unsigned pos_;
  bool player_;
public:
  explicit state_t( unsigned char t = 6 ) : t_(t), free_(0), pos_(0), player_(true){ }

  unsigned char t() const { return(t_); }
  unsigned free() const { return(free_); }
  unsigned pos() const { return(pos_); }
  void set_player(bool player) { this->player_ = player;};

  bool is_color( bool color, int pos ) const { if( color ) return( pos<4?t_&(1<<pos):pos_&(1<<(pos-4)) ); else return( !(pos<4?t_&(1<<pos):pos_&(1<<(pos-4))) ); }
  bool is_black( int pos ) const { return(is_color(true,pos)); }
  bool is_white( int pos ) const { return(is_color(false,pos)); }
  bool is_free( int pos ) const { return(pos<4?false:!(free_&(1<<(pos-4)))); }
  bool is_full() const { return(~free_==0); }

  double value() const;
  bool terminal() const;
  bool outflank( bool color, int pos ) const;
  bool is_black_move( int pos ) const { return( (pos == DIM) || outflank(true,pos) ); }
  bool is_white_move( int pos ) const { return( (pos == DIM) || outflank(false,pos) ); }

  void set_color( bool color, int pos );
  state_t move( bool color, int pos ) const;
  state_t black_move( int pos ) { return(move(true,pos)); }
  state_t white_move( int pos ) { return(move(false,pos)); }
  state_t bestNextMove() const;
  double maxValue(double, double, short int, short) const;
  double minValue(double, double, short int, short) const;

  bool operator<( const state_t &s ) const { return(this->value() < s.value());}//(free_ < s.free_) || ((free_ == s.free_) && (pos_ < s.pos_)) ); }
  void print( std::ostream &os, int depth ) const;
  void pretty_print( std::ostream &os, int depth ) const;
  void print_bits( std::ostream &os ) const;
  void set_state(int[]);
  int nummoves() const;
  int numfree() const;
};

inline int
state_t::nummoves() const
{ 
  int nummoves = 0; 
  for(int i = 4; i < DIM; i++){
    if(outflank(this->player_,i)) 
      nummoves++; 
  } 
  return nummoves;
} 

inline int
state_t::numfree() const
{
  int numfree = 0;
  for(int i = 4; i < DIM; i++){
    if(is_free(i)){
      numfree++;
    }
  }
  return numfree;
}

inline double
state_t::value() const
{
  double v = 0;
  double valuemoves = 0;
  double valuepos = 0;
  for( int pos = 0; pos < DIM; ++pos ) {
    if( !is_free(pos) ) {
      v += (is_black(pos)? (this->player_ ? 1: 0): (this->player_ ? 0: 1));
      valuepos += is_color(this->player_,pos)?valueTablero[pos]:0;
    }
    if( outflank(this->player_,pos)){
      valuemoves++;
    }
  }
  v *= 10;
  v += valuepos;
  v += valuemoves * 100;
  return(v);
}

inline bool
state_t::terminal( void ) const
{
  if( is_full() ) return(true);
  for( unsigned b = 0; b < DIM; ++b )
    if( is_black_move(b) || is_white_move(b) ) return(false);
  return(true);
}

inline bool
state_t::outflank( bool color, int pos ) const
{
  if( !is_free(pos) ) return(false);

  const int *p = 0;

  // check rows
  const int *r = rows[pos-4];
  while( *r != pos ) ++r;
  if( *(r+1) != -1 ) {
    for( p = r+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > r+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( r != rows[pos-4] ) {
    for( p = r-1; (p >= rows[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < r-1) && (p >= rows[pos-4]) && !is_free(*p) ) return(true);
  }

  // check cols
  const int *c = cols[pos-4];
  while( *c != pos ) ++c;
  if( *(c+1) != -1 ) {
    for( p = c+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > c+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( c != cols[pos-4] ) {
    for( p = c-1; (p >= cols[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < c-1) && (p >= cols[pos-4]) && !is_free(*p) ) return(true);
  }

  const int *d1 = dia1[pos-4];
  while( *d1 != pos ) ++d1;
  if( *(d1+1) != -1 ) {
    for( p = d1+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d1+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( d1 != dia1[pos-4] ) {
    for( p = d1-1; (p >= dia1[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d1-1) && (p >= dia1[pos-4]) && !is_free(*p) ) return(true);
  }

  const int *d2 = dia2[pos-4];
  while( *d2 != pos ) ++d2;
  if( *(d2+1) != -1 ) {
    for( p = d2+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d2+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( d2 != dia2[pos-4] ) {
    for( p = d2-1; (p >= dia2[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d2-1) && (p >= dia2[pos-4]) && !is_free(*p) ) return(true);
  }

  return(false);
}

inline void
state_t::set_color( bool color, int pos )
{
  if( color ) {
    if( pos < 4 )
      t_ |= (1<<pos);
    else {
      free_ |= (1<<pos-4);
      pos_ |= (1<<pos-4);
    }
  }
  else {
    if( pos < 4 )
      t_ &= ~(1<<pos);
    else {
      free_ |= (1<<pos-4);
      pos_ &= ~(1<<pos-4);
    }
  }
}

inline state_t
state_t::move( bool color, int pos ) const
{
  state_t s(*this);
  s.player_ = !s.player_;
  if( pos == DIM ) return(s);

  //assert( outflank(color,pos) );
  s.set_color(color,pos);

  // process rows
  const int *p = 0;
  const int *r = rows[pos-4];
  while( *r != pos ) ++r;
  if( *(r+1) != -1 ) {
    for( p = r+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > r+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = r+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( r != rows[pos-4] ) {
    for( p = r-1; (p >= rows[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < r-1) && (p >= rows[pos-4]) && !is_free(*p) )
      for( const int *q = r-1; q > p; --q ) s.set_color(color,*q);
  }

  // process columns
  const int *c = cols[pos-4];
  while( *c != pos ) ++c;
  if( *(c+1) != -1 ) {
    for( p = c+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > c+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = c+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( c != cols[pos-4] ) {
    for( p = c-1; (p >= cols[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < c-1) && (p >= cols[pos-4]) && !is_free(*p) )
      for( const int *q = c-1; q > p; --q ) s.set_color(color,*q);
  }

  const int *d1 = dia1[pos-4];
  while( *d1 != pos ) ++d1;
  if( *(d1+1) != -1 ) {
    for( p = d1+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d1+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = d1+1; q < p; ++q )s.set_color(color,*q);
  }
  if( d1 != dia1[pos-4] ) {
    for( p = d1-1; (p >= dia1[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d1-1) && (p >= dia1[pos-4]) && !is_free(*p) )
      for( const int *q = d1-1; q > p; --q )s.set_color(color,*q);
  }

  const int *d2 = dia2[pos-4];
  while( *d2 != pos ) ++d2;
  if( *(d2+1) != -1 ) {
    for( p = d2+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d2+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = d2+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( d2 != dia2[pos-4] ) {
    for( p = d2-1; (p >= dia2[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d2-1) && (p >= dia2[pos-4]) && !is_free(*p) )
      for( const int *q = d2-1; q > p; --q )s.set_color(color,*q);
  }

  return(s);
}

inline void
state_t::print(std::ostream &os, int depth) const
{
  int pos = 4;
  for( int i = 0; i < N; ++i ) {
    for( int j = 0; j < N; ++j )
      if( ((i != 2) && (i != 3)) || ((j != 2) && (j != 3)) ) {
        os << (is_free(pos)?"0 ":(is_black(pos)?"2 ":"1 "));
        ++pos;
      }
      else {
        assert( ((i == 2) || (i == 3)) && ((j == 2) || (j == 3)) );
        int p = ((i-2)<<1) + (j-2);
        os << (is_free(p)?"0 ":(is_black(p)?"2 ":"1 "));
      }
  }
}

inline void
state_t::pretty_print( std::ostream &os, int depth ) const
{
  os << "+";
  for( int j = 0; j < N; ++j ) os << "-";
  os << "+" << std::endl;

  int pos = 4;
  for( int i = 0; i < N; ++i ) {
    os << "|";
    for( int j = 0; j < N; ++j )
      if( ((i != 2) && (i != 3)) || ((j != 2) && (j != 3)) ) {
        os << (is_free(pos)?'0':(is_black(pos)?'2':'1'));
        ++pos;
      }
      else {
        assert( ((i == 2) || (i == 3)) && ((j == 2) || (j == 3)) );
        int p = ((i-2)<<1) + (j-2);
        os << (is_free(p)?'0':(is_black(p)?'2':'1'));
      }
    os << "|" << std::endl;
  }

  os << "+";
  for( int j = 0; j < N; ++j ) os << "-";
  os << "+" << std::endl;
}

inline void
state_t::print_bits( std::ostream &os ) const
{
  for( int i = 3; i >= 0; --i ) os << (t_&(1<<i)?'1':'0');
  os << ":";
  for( int i = 31; i >= 0; --i ) os << (pos_&(1<<i)?'1':'0');
  os << ":";
  for( int i = 31; i >= 0; --i ) os << (free_&(1<<i)?'1':'0');
}

inline state_t
state_t::bestNextMove() const
{
  state_t best = *this;
  state_t temp;
  double v = -9999;
  double previousv = v;
  double alpha = -9999;
  double beta = 9999;
  short limit = this->numfree() <= 12 ? 40 : LIMIT;
  if(this->terminal()){
    return *this;
  }
  std::vector<state_t> moves;
  for(int i = 0; i < DIM; i++){
    if(this->outflank(this->player_,i)){
      moves.push_back(this->move(this->player_,i));
    }
  }
  if(moves.empty()) {
    temp = state_t();
    temp.t_ = this->t_;
    temp.free_ = this->free_;
    temp.pos_ = this->pos_;
    temp.player_ = !this->player_;
    v = std::max(v, temp.minValue(alpha,beta,1,limit));
    if(v != previousv) {
      best = temp;
      previousv = v;
    }
    if(v >= beta) {
      return temp;
    }
    alpha = std::max(alpha, v);
  }else{
    int tam = moves.size();
    for(int i = 0; i < tam; i++){
      temp = moves.at(i);
      v = std::max(v, temp.minValue(alpha,beta,1,limit));
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

inline double
state_t::maxValue(double alpha, double beta, short nivel, short limit) const
{
	if(this->terminal() || nivel >= limit){
	  return this->value();
	}
	double v = -9999;
	std::vector<state_t> moves = std::vector<state_t>();
	for(int i = 0; i < 32; i++) {
	  if(this->outflank(this->player_,ordenEvaluacion[i])) {
	    moves.push_back(this->move(this->player_,ordenEvaluacion[i]));
	  }
	}
	if(moves.empty()){
		state_t temp = state_t();
		temp.t_ = this->t_;
		temp.free_ = this->free_;
		temp.pos_ = this->pos_;
		temp.player_ = !this->player_;
		v = std::max(v, temp.minValue(alpha,beta,nivel+1,limit));
		if(v >= beta){
			return v;
		}
		alpha = std::max(alpha, v);
	}else{
	  int tam = moves.size();
	  for(int i = 0; i < tam; i++){
	    v = std::max(v, moves.at(i).minValue(alpha,beta,nivel+1,limit));
	    if(v >= beta) {
	      return v;
	    }
	    alpha = std::max(alpha, v);
	  }
	}
	return v;
}

inline double
state_t::minValue(double alpha, double beta, short nivel, short limit) const
{
	if(this->terminal() || nivel >= limit) {
	  return this->value();
	}
	double v = 9999;
	std::vector<state_t> moves = std::vector<state_t>();
	for(int i = 0; i < 32; i++) {
	  if(this->outflank(this->player_,ordenEvaluacion[i])) {
	    moves.push_back(this->move(this->player_,ordenEvaluacion[i]));
	  }
	}
	if(moves.empty()){
	  state_t temp = state_t();
	  temp.t_ = this->t_;
	  temp.free_ = this->free_;
	  temp.pos_ = this->pos_;
	  temp.player_ = !this->player_;
	  v = std::min(v, temp.maxValue(alpha,beta,nivel+1,limit));
	  if(v <= alpha){
	    return v;
	  }
	  beta = std::min(beta,v);		
	}else{
	  int tam = moves.size();
	  for(int i = 0; i < tam; i++){
	    v = std::min(v,moves.at(i).maxValue(alpha,beta,nivel+1,limit));
	    if(v <= alpha){
	      return v;
	    }
	    beta = std::min(beta,v);
	  }
	}
	return v;
}

inline void
state_t::set_state(int state[36]){
	this->t_ = 0;
	this->free_ = 0;
	this->pos_ = 0;
	int j = 0;
	for(int i = 0; i < 36;i++){
	  if(i < 14){
	    j = i + 4;
	  }else if(i > 21){
	    j = i;
	  }else if(i == 14 || i == 15){
	    j = i - 14;
	  }else if(i == 20 || i == 21){
	    j = i - 18;
	  }else if(i > 15 && i < 20){
	    j = i + 2;
	  }
	  switch(state[i]){
	  case 0: break;
	  case 1: this->set_color(false,j);
	    break;
	  case 2: this->set_color(true,j);
	    break;
	  }
	}
}
