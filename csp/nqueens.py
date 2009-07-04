from random import choice, shuffle

class NQueens(object):
    """
    """
    def __init__(self, n):
        self.n = n
        self.assignment = [None] * n # the queens

    def _get_unassigned_variables(self):
        """
        Returns the list of unassigned variables =)
        """
        return [index for index, value in enumerate(self.assignment)
                if value is None]
    unassigned_variables = property(_get_unassigned_variables)

    def _get_complete_domain(self):
        """
        Returns a complete domain
        """
        domain = dict().fromkeys(range(self.n))
        for key in domain:
            domain[key] = range(self.n)
        return domain
    complete_domain = property(_get_complete_domain)

    def assign(self, var, value):
        """
        Make an assignment in this problem's assignment representation
        """
        self.assignment[var] = value

    @property
    def is_solved(self):
        return self.assignment.count(None) == 0

    def check_conflicts(self, pair1, pair2, *args, **kwargs):
        """
        Check if there's a conflict between two variables, each variables
        represented by a pair of the form: (var_num, value)
        """
        return pair1[1] == pair2[1] or \
               abs(pair1[0] - pair2[0]) == abs(pair1[1] - pair2[1])
        
    def print_result(self, result=None):
        """
        Just print.
        """
        result = result or self.assignment
        print ' '.join([str(pair) for pair in enumerate(result)])


class NQueensLS(NQueens):
    """
    """
    def __init__(self, n):
        self.n = n
        self.assignment = range(n)
        shuffle(self.assignment) # generates a random initial state

    def _get_num_conflicts(self, assignment=None):
        """
        """
        assignment = assignment or self.assignment
        restrictions = 0
        for q1 in range(self.n):
            for q2 in range(q1+1, self.n):
                restrictions += 1 if self.check_conflicts((q1, assignment[q1]),
                                                          (q2, assignment[q2])) \
                                                          else 0
        return restrictions

    def _get_num_conflicts_for(self, var_num, value=None):
        """
        Gets the number of conflicts in which the `var_num` is involved (taking
        only the current assignment)
        """
        value = value or self.assignment[var_num]
        restrictions = 0
        for q in range(self.n):
            restrictions += 1 if q != var_num and self.check_conflicts((var_num, value),
                                                      (q, self.assignment[q])) \
                                                      else 0
        return restrictions

    def _select_conflicted_variable(self):
        """
        Selects a 'randomly' chosen conflicted variables
        """
        try:
            return choice([q for q in range(self.n)
                           if self._get_num_conflicts_for(q) != 0])
        except IndexError:
            return None

    def _get_min_conflicts_position_for(self, var_num):
        """
        Returns the position where the variable `var_num` presents least
        conflicts.
        """
        return min([self._get_num_conflicts_for(var_num, value)
                    for value in range(self.n)])            

    def min_conflicts(self, *args, **kwargs):
        """
        Here, the selection is based on the min_conflicts algorithm, we'll
        select the queen that results in the minimum number of conflicts
        with the other variables.
        """
        current = []
        current[:] = self.assignment[:]
        for i in range(100000):
            if self._get_num_conflicts(assignment=current) == 0:
                self.print_result(current)
                return
            var_num = self._select_conflicted_variable()
            current[var_num] = self._get_min_conflicts_position_for(var_num)
        print 'Fuck'


if __name__ == "__main__":
    nqueens_problem = NQueens(int(argv[1]))
    solver = CSPSolver(nqueens_problem)
    solver.solve(nqueens_problem.complete_domain)
