#!/usr/bin/python
import sys

# CNFIndex = Integer
# SudIndex = (Integer, Integer, Integer)
# Clause   = [CNFIndex]

# Helper functions
def sudIndex2CNFIndex(i, j, k):
    return 81 * (i-1) + 9 * (j-1) + (k-1) + 1

def cnfIndex2SudIndex(i):
    return (((i-1)/9/9) + 1, (((i-1)/9) % 9) + 1, ((i-1) % 9) + 1)

#
# Static Clauses
def non_vacuity():
    """
    returns [Clause]
    """
    clauses = []
    for i in range(1,10):
        clause = []
        for j in range(1,10):
            clause = [sudIndex2CNFIndex(i, j, k) for k in range(1,10)]
            clauses.append(clause)
    return clauses

def uniqueness():
    """
    """
    clauses = []
    for i in range(1,10):
        clauses_aux = []
        for j in range(1,10):
            clauses_aux = [[-sudIndex2CNFIndex(i,j,d1), -sudIndex2CNFIndex(i,j,d2)]
                           for d1 in range(1,9) for d2 in range(d1+1, 10)]
            clauses.extend(clauses_aux)
    return clauses

def rows_restrictions():
    """
    """
    clauses = []
    for i in range(1,10):
        clauses_aux = []
        for d in range(1,10):
            clauses_aux = [[-sudIndex2CNFIndex(i,j1,d), -sudIndex2CNFIndex(i,j2,d)]
                           for j1 in range(1,9) for j2 in range(j1+1,10)]
            clauses.extend(clauses_aux)
    return clauses

def columns_restrictions():
    """
    """
    clauses = []
    for j in range(1,10):
        clauses_aux = []
        for d in range(1,10):
            clauses_aux = [[-sudIndex2CNFIndex(i1,j,d), -sudIndex2CNFIndex(i2,j,d)]
                           for i1 in range(1,9) for i2 in range(i1+1,10)]
            clauses.extend(clauses_aux)
    return clauses

def blocks_restrictions():
    """
    """
    clauses = []
    for z in range(1,10):
        for i in range(0,3):
            for j in range(0,3):
                for x in range(1,4):
                    clauses_aux = [[-sudIndex2CNFIndex(3*i+x, 3*j+y, z),
                                    -sudIndex2CNFIndex(3*i+x, 3*j+k, z)]
                                   for y in range(1,3) for k in range(y+1, 4)]
                    clauses.extend(clauses_aux)

    for z in range(1,10):
        for i in range(0,3):
            for j in range(0,3):
                for l in range(1,4):
                    for y in range(1,4):
                        clauses_aux = [[-sudIndex2CNFIndex(3*i+x, 3*j+y, z),
                                        -sudIndex2CNFIndex(3*i+k, 3*j+l, z)]
                                       for x in range(1,3) for k in range(x+1, 4)]
                        clauses.extend(clauses_aux)
    return clauses

#
# Instance clauses
def instance_clauses(plain_sudoku):
    """
    """
    clauses = []
    for i in range(0, len(plain_sudoku)):
        if plain_sudoku[i] != '.':
            clauses.append([sudIndex2CNFIndex((int(i)/9)+1, (int(i)%9 +1),
                                              int(plain_sudoku[i]))])
    return clauses

                        
#
# Main function
def main():
    # check for the main argument and check it's length
    if len(sys.argv) != 2 or len(sys.argv[1]) != 81:
        sys.stderr.write(
"""
Modo de uso:
    > sudoku `sudoku_plain_representation`

donde `sudoku_plain_representation` es una cadena de caracteres
de longitud 81 descrita en el enunciado del proyecto

"""
                        )
    # Get static clauses
    clauses = non_vacuity() + uniqueness() + rows_restrictions() + \
              columns_restrictions() + blocks_restrictions()
    # Get instance clauses
    clauses.extend(instance_clauses(sys.argv[1]))

    # Print clauses in the zChaff's file
    zc_file = open('zcInput', 'w')
    zc_file.write('p cnf 729 %s\nc\nc Clauses:\nc\n' % len(clauses))
    for clause in clauses:
        zc_file.write('%s 0\n' % ' '.join([str(si) for si in clause]))
    zc_file.close()

if __name__ == "__main__": main()
