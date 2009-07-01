from sys import maxint, argv
from random import choice, shuffle

class CSPSolver(object):
    """
    """
    def __init__(self, problem):
        self.problem = problem

    def forward_checking(self, var_num, domain):
        """
        Execute forward_checking over a given `domain` when a variable
        `var_num` is recently assignated.
        Returns a new domain free of restrictions
        """
        new_domain = []
        new_domain[:] = domain[:]
        val_var_num = self.problem.assignment[var_num]
        for vn in self.problem.unassigned_variables:
            new_domain[vn] = filter(lambda val: \
                                    not self.problem.check_conflicts((var_num, val_var_num),
                                                                     (vn, val)), domain[vn])
        return new_domain

    def mrv_select(self, domain):
        """
        Returns the number of the variable with the minimum remaining value
        in a given `domain`
        """
        var_num = None
        min_domain_size = maxint
        for vn in self.problem.unassigned_variables:
            if len(domain[vn]) < min_domain_size:
                var_num = vn
                min_domain_size = len(domain[vn])
        return var_num

    def solve(self, domain):
        """
        """
        if self.problem.assignment.count(None) == 0:
            self.problem.print_result()
            return
        var_num = self.mrv_select(domain)
        for value in domain[var_num]:
            self.problem.assignment[var_num] = value
            new_domain = self.forward_checking(var_num, domain)
            self.solve(new_domain)
            self.problem.assignment[var_num] = None

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
        domain = []
        for i in range(self.n):
            domain.append(range(self.n))
        return domain
    complete_domain = property(_get_complete_domain)

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
