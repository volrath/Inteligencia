Sanity Check
============
All of them with IDA*

Test 1: rand 50
-------
 4  1  5  3 
 6  9  2  7 
 8 13 10 11 
12  0 14 15

pdb:
U, U, L, U, R, R, D, L, U, L, 
Expanded nodes: 37
Execution time: 0s

manhattan:
U, U, L, U, R, R, D, L, U, L, 
Expanded nodes: 2081
Execution time: 0s

reduction ratio: 56.243243243243242


Test 2: rand 75
-------
 4  2  7  6 
 5  1 15  3 
 8  9 14  0 
12 10 13 11

pdb:
L, U, U, R, D, L, U, L, D, D, D, R, U, R, D, L, U, L, U, L, U, 
Expanded nodes: 674
Execution time: 0s

manhattan:
didn't finish


Test 3: rand 60
-------
 5  2  8  3 
 1  6  4  7 
 0 14 10 11 
12  9 13 15

pdb:
U, R, R, U, L, D, R, D, L, U, L, D, R, D, R, U, U, L, L, U, R, D, L, U, 
Expanded nodes: 9228
Execution time: 0s          -- This one ran FAST

manhattan:
didn't finish


Test 4: rand 55
-------
 1  5  2  3 
 4  9  6 11 
 8  7 14 10 
12 13  0 15

pdb:
U, L, U, R, D, R, U, L, L, U, L, 
Expanded nodes: 277
Execution time: 0s

manhattan:
U, L, U, R, D, R, U, L, L, U, L, 
Expanded nodes: 4819
Execution time: 0s

reduction ration: 17.397111913357399


Test 5: rand 55
-------
 1  6  5  3 
 4  2  7 11 
 8  9 10  0 
12 13 14 15

pdb:
U, L, U, L, D, R, U, L, L, 
Expanded nodes: 40
Execution time: 0s

manhattan:
U, L, U, L, D, R, U, L, L, 
Expanded nodes: 979
Execution time: 0s

reduction ratio: 24.475000000000001


Test 6: rand 56
-------
 1  2  6  7 
 4  0  3  5 
 8  9 10 11 
12 13 14 15

pdb:
U, R, D, R, U, L, L, D, R, U, L, L, 
Expanded nodes: 55
Execution time: 0s

manhattan:
U, R, D, R, U, L, L, D, R, U, L, L, 
Expanded nodes: 16783
Execution time: 1s

reduction ratio: 305.14545454545453


Test 7: rand 57
-------
 4  2  9  3 
 0  8  1  7 
10  5  6 11 
12 13 14 15

pdb:
R, R, U, L, D, D, L, U, R, R, D, L, U, L, U,
Expanded nodes: 881
Execution time: 0s

manahattan:
R, D, L, U, R, R, U, L, D, R, D, L, U, L, U,   --  This one threw a different solution!
Expanded nodes: 85949
Execution time: 0s

reduction ratio: 97.558456299659483


Test 8: rand 57
-------
 1  0  6  3 
 4 12  5  7 
 8 10  2 11 
13  9 14 15

pdb:
R, D, D, L, U, L, D, R, D, L, U, U, R, U, R, D, L, U, L, 
Expanded nodes: 717
Execution time: 0s

manhattan:
R, D, D, L, U, L, D, R, D, L, U, U, R, U, R, D, L, U, L, 
Expanded nodes: 1315486
Execution time: 17s

reduction ratio: 1834.7085076708508


Test 9: rand 57  --  BEST SO FAR
-------
 5  6  4  3 
 9  8  1 11 
10  0  7  2 
12 13 14 15

pdb:
L, U, R, U, R, D, D, R, U, L, D, L, L, U, U, R, R, D, L, L, U, 
Expanded nodes: 3005
Execution time: 0s

manhattan:
L, U, R, U, R, D, D, R, U, L, D, L, L, U, U, R, R, D, L, L, U, 
Expanded nodes: 7090451
Execution time: 55s

reduction ratio: 2359.5510815307821


Test 10: rand 57  --  I think this is really good
--------
 1  5  2  3 
 4  9  6  7 
 8 10 11 15 
12 13  0 14

pdb:
R, U, L, L, U, U, L, 
Expanded nodes: 19
Execution time: 0s

manhattan:
didn't finish...


Test 11: rand 56
--------
 6  5  1  3 
 4  9  0  2 
12 10 11  7 
13  8 14 15

pdb:
R, D, L, L, D, L, U, R, U, U, L, D, R, U, R, D, L, L, U, 
Expanded nodes: 678
Execution time: 0s

manhattan:
R, D, L, L, D, L, U, R, U, U, L, D, R, U, R, D, L, L, U, 
Expanded nodes: 1524565
Execution time: 15s

reduction ratio: 2248.6209439528025