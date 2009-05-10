
int misplaced_tiles(state8_t state) { int mt = 0; for (int i = 0; i < 9; i++) { if (state.cont(i) != i) mt++; }; return(mt); }
