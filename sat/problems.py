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
        return [[self._sudIndex2CNFIndex(i/9 + 1, i%9 + 1,
                                         int(plain_sudoku[i]))]
                for i in range(0, len(plain_sudoku)) if plain_sudoku[i] != '.']

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

    def write_solution(self, cnf_solution):
        """
        Writes a solution in a specified format given the zchaff's cnf output
        """
        if cnf_solution:
            print ''.join([str(self._cnfIndex2SudIndex(k)[2])
                           for k in cnf_solution if k > 0])
        else:
            print '0' * 81


class CardPuzzle(object):
    """
    A card puzzle problem for zchaff
    """
    def __init__(self, num_holes, num_cards, cards):
        self.num_holes = num_holes
        self.num_cards = num_cards
        self.cards = cards

    def __str__(self):
        return 'card_puzzle'
    __repr__ = __str__

    @property
    def num_variables(self):
        return self.num_cards

    @property
    def num_clauses(self):
        return 2 * self.num_holes

    def _get_clauses(self):
        """
        """
        clauses = []
        for i in range(0, self.num_clauses):
            clauses.append([])
        card_number = 1
        for card in self.cards:
            fcard = self._flip(card)
            for h in range(0, self.num_clauses):
                if not card[h]: # if the hole number `h` in the `card` is not open
                    clauses[h].append(card_number)
                if not fcard[h]: # the same of above but in the flipped card
                    clauses[h].append(-card_number)
            card_number += 1
        return clauses
    clauses = property(_get_clauses)

    def _flip(self, card):
        """
        flip a card and return a tuple with the holes flipped
        """
        return card[self.num_holes:] + card[:self.num_holes]

    def write_solution(self, cnf_solution):
        """
        Writes a solution in a specified format given the zchaff's cnf output
        """
        if cnf_solution:
            print ' '.join(map(str, cnf_solution))
        else:
            print 0

class Solitaire(object):
    """
    """
    def __init__(self, board_size, board):
        self.board_size = board_size
        self.clauses = self._get_static_clauses() + \
                       self._get_instance_clauses(board)
        self.num_variables = board_size * board_size * 3

    def __str__(self):
        return 'solitaire'
    __repr__ = __str__

    @property
    def num_clauses(self):
        return len(self.clauses)

    def _get_instance_clauses(self, board):
        """
        Ensures that for every non empty position in the board, the ball
        can be preserved or removed to reach the final solution.
        """
        clauses = []
        for i in range(0, len(board)):
            if board[i] == 0:
                clauses.append([self._solIndex2CNFIndex(i/self.board_size+1,
                                                        i%self.board_size+1,
                                                        0)])
            else:
                clauses.append([
                    self._solIndex2CNFIndex(i/self.board_size + 1,
                                            i%self.board_size + 1,
                                            board[i]),
                    self._solIndex2CNFIndex(i/self.board_size + 1,
                                            i%self.board_size + 1,
                                            0),
                    ])
        return clauses

    def _get_static_clauses(self):
        return self._non_vacuity() + self._uniqueness() + \
               self._rows_restrictions() + self._columns_restrictions()

    def _solIndex2CNFIndex(self, i, j, k):
        return 3*self.board_size * (i-1) + 3 * (j-1) + (k+1)

    def _cnfIndex2SolIndex(self, i):
        return ((i-1)/3/self.board_size + 1,
                (((i-1)/3) % self.board_size) + 1,
                (i-1) % 3)

    def _non_vacuity(self):
        """
        """
        clauses = []
        for i in range(1, self.board_size+1):
            clause = []
            for j in range(1, self.board_size+1):
                clause = [self._solIndex2CNFIndex(i, j, k) for k in range(0, 3)]
                clauses.append(clause)
        return clauses

    def _uniqueness(self):
        """
        """
        clauses = []
        for i in range(1, self.board_size+1):
            clauses_aux = []
            for j in range(1, self.board_size+1):
                clauses_aux = [[-self._solIndex2CNFIndex(i,j,d1), -self._solIndex2CNFIndex(i,j,d2)]
                               for d1 in range(0, 2) for d2 in range(d1+1, 3)]
                clauses.extend(clauses_aux)
        return clauses

    def _rows_restrictions(self):
        """
        Ensures every row has at least one ball
        """
        clauses = []
        for i in range(1,self.board_size+1):
            clauses.append([self._solIndex2CNFIndex(i, j, d)
                            for j in range(1, self.board_size+1)
                            for d in range(1, 3)])
        return clauses

    def _columns_restrictions(self):
        """
        Ensures that each column has balls of the same color
        """
        clauses = []
        for j in range(1,self.board_size+1):
            clauses_aux = []
            for d in range(1,3):
                clauses_aux = [[-self._solIndex2CNFIndex(i1,j,d), -self._solIndex2CNFIndex(i2,j,d)]
                               for i1 in range(1,self.board_size) for i2 in range(i1+1,self.board_size+1)]
                clauses.extend(clauses_aux)
        return clauses

    def write_solution(self, cnf_solution):
        """
        Writes a solution in a specified format given the zchaff's cnf output
        """
        print '\nResultado:'
        if cnf_solution:
            print 1#cnf_solution
        else:
            print 0
