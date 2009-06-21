##
# This file contains a class for each problem proposed

class Sudoku(object):
    """
    Models a sudoku problem in a sat way =P
    """
    def __init__(self, plain_sudoku):
        """
        assume that plain_sudoku is a valid string
        """
        self.clauses = self._get_static_clauses() + \
                       self._get_instance_clauses(plain_sudoku)
        self.num_variables = 729

    def __str__(self):
        return 'sudoku'
    __repr__ = __str__

    @property
    def num_clauses(self):
        return len(self.clauses)

    def _get_instance_clauses(self, plain_sudoku):
        """
        """
        clauses = []
        for i in range(0, len(plain_sudoku)):
            if plain_sudoku[i] != '.':
                clauses.append([self._sudIndex2CNFIndex((int(i)/9)+1, (int(i)%9 +1),
                                                  int(plain_sudoku[i]))])
        return clauses

    def _get_static_clauses(self):
        """
        """
        return self._non_vacuity() + self._uniqueness() + \
               self._rows_restrictions() + self._columns_restrictions() + \
               self._blocks_restrictions()

    def _sudIndex2CNFIndex(self, i, j, k):
        return 81 * (i-1) + 9 * (j-1) + (k-1) + 1

    def _cnfIndex2SudIndex(self, i):
        return (((i-1)/9/9) + 1, (((i-1)/9) % 9) + 1, ((i-1) % 9) + 1)

    def _non_vacuity(self):
        """
        returns [Clause]
        """
        clauses = []
        for i in range(1,10):
            clause = []
            for j in range(1,10):
                clause = [self._sudIndex2CNFIndex(i, j, k) for k in range(1,10)]
                clauses.append(clause)
        return clauses

    def _uniqueness(self):
        """
        """
        clauses = []
        for i in range(1,10):
            clauses_aux = []
            for j in range(1,10):
                clauses_aux = [[-self._sudIndex2CNFIndex(i,j,d1), -self._sudIndex2CNFIndex(i,j,d2)]
                               for d1 in range(1,9) for d2 in range(d1+1, 10)]
                clauses.extend(clauses_aux)
        return clauses

    def _rows_restrictions(self):
        """
        """
        clauses = []
        for i in range(1,10):
            clauses_aux = []
            for d in range(1,10):
                clauses_aux = [[-self._sudIndex2CNFIndex(i,j1,d), -self._sudIndex2CNFIndex(i,j2,d)]
                               for j1 in range(1,9) for j2 in range(j1+1,10)]
                clauses.extend(clauses_aux)
        return clauses

    def _columns_restrictions(self):
        """
        """
        clauses = []
        for j in range(1,10):
            clauses_aux = []
            for d in range(1,10):
                clauses_aux = [[-self._sudIndex2CNFIndex(i1,j,d), -self._sudIndex2CNFIndex(i2,j,d)]
                               for i1 in range(1,9) for i2 in range(i1+1,10)]
                clauses.extend(clauses_aux)
        return clauses

    def _blocks_restrictions(self):
        """
        """
        clauses = []
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for x in range(1,4):
                        clauses_aux = [[-self._sudIndex2CNFIndex(3*i+x, 3*j+y, z),
                                        -self._sudIndex2CNFIndex(3*i+x, 3*j+k, z)]
                                       for y in range(1,3) for k in range(y+1, 4)]
                        clauses.extend(clauses_aux)
        for z in range(1,10):
            for i in range(0,3):
                for j in range(0,3):
                    for l in range(1,4):
                        for y in range(1,4):
                            clauses_aux = [[-self._sudIndex2CNFIndex(3*i+x, 3*j+y, z),
                                            -self._sudIndex2CNFIndex(3*i+k, 3*j+l, z)]
                                           for x in range(1,3) for k in range(x+1, 4)]
                            clauses.extend(clauses_aux)
        return clauses
