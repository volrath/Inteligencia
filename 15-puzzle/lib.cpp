#include <iostream>
#include <iomanip>
#include <string.h>
#include <ext/hash_map>
#include <fstream>

#define HSTEP 1
#define VSTEP 4
#define BR 4
#define LEFT   "L"
#define RIGHT  "R"
#define UP     "U"
#define DOWN   "D"

#define PDB05_FILE    "pdb05.bin"
#define PDB1015_FILE  "pdb1015.bin"
#define PATTERN_SIZE 8
#define INT_SIZE 4

#define NUM_TILES 16
#define HALF_NUM_TILES 8
#define TILE_SIZE 4
#define GOAL1 0x76543210
#define GOAL2 0xFEDCBA98
#define ZERO 0


// bit representation for movements: LRUD
static unsigned m1_ = 3758005178, m2_ = 1467408382; // m1_ = 0101 1101 1101 1001 0111 1111 1111 1011
                                                    // m2_ = 0111 1111 1111 1011 0110 1110 1110 1010

struct state15_t {
  unsigned p1_, p2_;
  state15_t() : p1_(ZERO), p2_(ZERO) { for( int i = 7; i >= ZERO; --i ) { p1_ = p1_<<4; p1_ += i; p2_ = p2_<<4; p2_ += i+HALF_NUM_TILES; } }
  bool operator==( const state15_t &s ) const { return((p1_==s.p1_)&&(p2_==s.p2_)); }
  unsigned bpos() const
  {
    unsigned p = p1_;
    for( int i = ZERO; i < HALF_NUM_TILES; ++i, p = p>>TILE_SIZE ) if( (p&0xF) == ZERO ) return(i);
    p = p2_;
    for( int i = ZERO; i < HALF_NUM_TILES; ++i, p = p>>TILE_SIZE ) if( (p&0xF) == ZERO ) return(HALF_NUM_TILES+i);
    return((unsigned)-1);
  }
  // Helpers
  unsigned cont( unsigned p ) const { return( (p<HALF_NUM_TILES?(p1_>>(p<<2)):(p2_>>((p-HALF_NUM_TILES)<<2))) & 0xF ); }
  short allowed_steps() { unsigned bp = bpos(); return ( (bp<HALF_NUM_TILES?(m1_>>(bp<<2)):(m2_>>((bp-HALF_NUM_TILES)<<2))) & 0xF ); }
  void set( unsigned p, unsigned t ) { if( p < HALF_NUM_TILES ) p1_ = (p1_&~(0xF<<(p<<2)))|(t<<(p<<2)); else p2_ = (p2_&~(0xF<<((p-HALF_NUM_TILES)<<2)))|(t<<((p-HALF_NUM_TILES)<<2)); }

  // Movements
  void left() { unsigned bp = bpos(), t = cont(bp-HSTEP); set(bp-HSTEP, ZERO); set(bp, t); }
  void right() { unsigned bp = bpos(), t = cont(HSTEP+bp); set(HSTEP+bp,ZERO); set(bp,t); }
  void up() { unsigned bp = bpos(), t = cont(bp-VSTEP); set(bp-VSTEP,ZERO); set(bp,t); }
  void down() { unsigned bp = bpos(), t = cont(VSTEP+bp); set(VSTEP+bp,ZERO); set(bp,t); }

  // Problem methods
  void successors(state15_t ** successors) {
    memset(successors, ZERO, sizeof(successors));

    short as = allowed_steps(), k = ZERO;
    for (int i = ZERO; i < BR; i++, as = as >> 1) {
      successors[i] = NULL;
      if (as & 1 == 1) {
	// Clone the current state
	state15_t * clone = (state15_t *)malloc(sizeof(state15_t));
	clone->p1_ = p1_; clone->p2_ = p2_;
	if (i == 0)      clone->left();
	else if (i == 1) clone->right();
	else if (i == 2) clone->up();
	else if (i == 3) clone->down();
	successors[k] = clone; k++;
      }
    }
  }

  bool is_goal() { return ((p1_ == GOAL1) && (p2_ == GOAL2)); }

  void set_state(int *input) { p1_ = ZERO; p2_ = ZERO; for(int i = 7; i >= ZERO; i--) { p1_ = p1_<<TILE_SIZE; p1_ += input[i]; p2_ = p2_<<TILE_SIZE; p2_ += input[i+HALF_NUM_TILES]; } }

  void print( std::ostream &os ) const
  {
    unsigned p = p1_;
    for( int i = ZERO; i < NUM_TILES; ++i ) {
      os << std::setw(2) << (p&0xF) << ' ';
      p = p>>TILE_SIZE;
      if( i%4 == 3 ) os << std::endl;
      if( i == 7 ) p = p2_;
    }
  }
};

inline std::ostream& operator<<( std::ostream &os, const state15_t &s ) { s.print(os); return(os); }

class node_t {
public:
  char g_, h_, m_;
  bool open_;
  state15_t *state_;
  node_t *prev_, *next_;  // to be used in the priority queue

  node_t( unsigned g = 0, unsigned h = 0, state15_t *state = NULL, bool open = true ) : g_(g), h_(h), open_(open), state_(state), prev_(ZERO), next_(ZERO) { }
  char m() const { return(m_); }
  unsigned g() const { return(g_); }
  unsigned h() const { return(h_); }
  unsigned f() const { return(g_+h_); }
  bool open() const { return(open_); }
  bool closed() const { return(!open()); }
  state15_t* state() const { return(state_); }
  node_t* prev() const { return(prev_); }
  node_t* next() const { return(next_); }
  void set_m( char m ) { m_ = m; }
  void set_g( unsigned g ) { g_ = g; }
  void set_h( unsigned h ) { h_ = h; }
  void set_open( bool open = false ) { open_ = open; }
  void set_state( state15_t *state ) { state_ = state; }
  void set_prev( node_t *prev ) { prev_ = prev; }
  void set_next( node_t *next ) { next_ = next; }
  void print( std::ostream &os ) const { os << "g=" << g() << ", h=" << h() << ", open=" << (open()?"true":"false") << ", movement=" << m() << std::endl << *state() << std::endl; }

  // problem methods
  bool goal_test() { return state()->is_goal(); }

  void successors(node_t ** successors) {
    memset(successors, ZERO, sizeof(successors));

    short as = state()->allowed_steps(), k = ZERO;
    for (int i = ZERO; i < BR; i++, as = as >> 1) {
      successors[i] = NULL;
      if (as & 1 == 1) {
	// Clone the current state
	state15_t * clone_st = (state15_t *)malloc(sizeof(state15_t));
	node_t *clone_nd = new node_t(g() + 1, ZERO, clone_st);
	clone_st->p1_ = state()->p1_; clone_st->p2_ = state()->p2_;

	if (i == 0)      { clone_st->left(); clone_nd->set_m(*LEFT); }
	else if (i == 1) { clone_st->right(); clone_nd->set_m(*RIGHT); }
	else if (i == 2) { clone_st->up(); clone_nd->set_m(*UP); }
	else if (i == 3) { clone_st->down(); clone_nd->set_m(*DOWN); }
	successors[k] = clone_nd; k++;
      }
    }
  }

protected:
};
inline std::ostream& operator<<( std::ostream &os, const node_t &n ) { n.print(os); return(os); }

struct pattern_t {
  unsigned p1_, p2_;
  pattern_t() : p1_(ZERO), p2_(ZERO) { }
  bool operator==( const pattern_t &p ) const { return((p1_==p.p1_)&&(p2_==p.p2_)); }
  void print( std::ostream &os ) const {
    unsigned p = p1_;
    for( int i = ZERO; i < NUM_TILES; ++i ) {
      std::cout << std::setw(2) << (p&0xF) << ' ';
      p = p>>TILE_SIZE;
      if( i == 7 ) p = p2_;
    }
    std::cout << std::endl;
  }
};
inline std::ostream& operator<<( std::ostream &os, const pattern_t &s ) { s.print(os); return(os); }

namespace __gnu_cxx {
  template<> class hash<state15_t> {
  public:
    size_t operator()( const state15_t &s ) const { return(s.p1_^s.p2_); }
  };
};

class hash_t : public __gnu_cxx::hash_map<state15_t, node_t> { };  // class

namespace __gnu_cxx {
  template<> class hash<pattern_t> {
  public:
    size_t operator()( const pattern_t &s ) const { return(s.p1_^s.p2_); }
  };
};
class pt_hash_t : public __gnu_cxx::hash_map<pattern_t, int> { };  // class
