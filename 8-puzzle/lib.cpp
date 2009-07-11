#include <iostream>
#include <iomanip>
#include <string.h>
#include <ext/hash_map>

#define HSTEP 1
#define VSTEP 3
#define BR 4
#define ZERO 0
#define LEFT   "L"
#define RIGHT  "R"
#define UP     "U"
#define DOWN   "D"


// bit representation for movements: LRUD
static unsigned short m1_ = 0xE9BA;
static unsigned m2_       = 0x000576DF; // 0101 1101  1001 0111  1111 1011  0110 1110  1010

struct state8_t {
  unsigned short p1_;
  unsigned int p2_;
  state8_t() : p1_(0), p2_(0) { p2_ = p2_ << 16;  p2_ += 8;
    for( int i = 3; i >= 0; --i ) { p1_ = p1_<<4;  p1_ += i; p2_ = p2_ << 4; p2_ += i + 4; }}
  bool operator==( const state8_t &s ) const { return((p1_==s.p1_)&&(p2_==s.p2_)); }
  unsigned bpos() const
  {
    unsigned p = p1_;
    for( int i = 0; i < 4; ++i, p = p>>4 ) if( (p&0xF) == 0 ) return(i);
    p = p2_;
    for( int i = 0; i <= 4; ++i, p = p>>4 ){ if( (p&0xF) == 0 ) return(4+i);}
    return((unsigned)-1);
  }
  unsigned cont( unsigned p ) const { return( (p<4?(p1_>>(p<<2)):(p2_>>((p-4)<<2))) & 0xF ); }
  void set( unsigned p, unsigned t ) { if( p < 4 ) p1_ = (p1_&~(0xF<<(p<<2)))|(t<<(p<<2)); else p2_ = (p2_&~(0xF<<((p-4)<<2)))|(t<<((p-4)<<2)); }
  short allowed_steps() { unsigned bp = bpos(); return ( (bp<4?(m1_>>(bp<<2)):(m2_>>((bp-4)<<2))) & 0xF ); }

  void left() { unsigned bp = bpos(), t = cont(bp-HSTEP); set(bp-HSTEP, 0); set(bp, t); }
  void right() { unsigned bp = bpos(), t = cont(HSTEP+bp); set(HSTEP+bp,0); set(bp,t); }
  void up() { unsigned bp = bpos(), t = cont(bp-VSTEP); set(bp-VSTEP,0); set(bp,t); }
  void down() { unsigned bp = bpos(), t = cont(VSTEP+bp); set(VSTEP+bp,0); set(bp,t); }

  // Problem methods
  void successors(state8_t ** successors) {
    memset(successors, 0, sizeof(successors));

    short as = allowed_steps(), k = 0;
    for (int i = 0; i < BR; i++, as = as >> 1) {
      successors[i] = NULL;
      if (as & 1 == 1) {
	// Clone the current state
	state8_t * clone = (state8_t *)malloc(sizeof(state8_t));
	clone->p1_ = p1_; clone->p2_ = p2_;
	if (i == 0)      clone->left();
	else if (i == 1) clone->right();
	else if (i == 2) clone->up();
	else if (i == 3) clone->down();
	successors[k] = clone; k++;
      }
    }
  }

  bool is_goal() { return ((p1_ == 12816) && (p2_ == 554580)); }

  void set_state(int *input) {
    p1_ = 0; p2_ = 0;
    p2_ << 4; p2_ += input[8];
    for (int i = 3; i >= 0; i--) { p1_ = p1_<<4; p1_ += input[i]; p2_ = p2_<<4; p2_ += input[i+4]; }
  }

  void print( std::ostream &os ) const
  {
    unsigned p = p1_;
    for( int i = 0; i < 9; ++i ) {
      os << std::setw(2) << (p&0xF) << ' ';
      p = p>>4;
      if( (i +1) % 3 == 0) os << std::endl;
      if( i == 3 ) p = p2_;
    }
  }
};

inline std::ostream& operator<<( std::ostream &os, const state8_t &s ) { s.print(os); return(os); }

class node_t {
public:
  char g_, h_, m_;
  bool open_;
  state8_t *state_;
  node_t *prev_, *next_;  // to be used in the priority queue

  node_t( unsigned g = 0, unsigned h = 0, state8_t *state = NULL, bool open = true ) : g_(g), h_(h), open_(open), state_(state), prev_(0), next_(0) { }
  char m() const { return(m_); }
  unsigned g() const { return(g_); }
  unsigned h() const { return(h_); }
  unsigned f() const { return(g_+h_); }
  bool open() const { return(open_); }
  bool closed() const { return(!open()); }
  state8_t* state() const { return(state_); }
  node_t* prev() const { return(prev_); }
  node_t* next() const { return(next_); }
  void set_m( char m ) { m_ = m; }
  void set_g( unsigned g ) { g_ = g; }
  void set_h( unsigned h ) { h_ = h; }
  void set_open( bool open = false ) { open_ = open; }
  void set_state( state8_t *state ) { state_ = state; }
  void set_prev( node_t *prev ) { prev_ = prev; }
  void set_next( node_t *next ) { next_ = next; }
  void print( std::ostream &os ) const { os << "g=" << g() << ", h=" << h() << ", open=" << (open()?"true":"false") << ", movement=" << m() << std::endl << *state() << std::endl; }

  // problem methods
  bool goal_test() { return state()->is_goal(); }

  void successors(node_t ** successors) {
    memset(successors, 0, sizeof(successors));

    short as = state()->allowed_steps(), k = 0;
    for (int i = 0; i < BR; i++, as = as >> 1) {
      successors[i] = NULL;
      if (as & 1 == 1) {
	// Clone the current state
	state8_t * clone_st = (state8_t *)malloc(sizeof(state8_t));
	node_t *clone_nd = new node_t(g() + 1, 0, clone_st);
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


