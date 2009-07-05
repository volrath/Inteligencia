from sys import maxint, argv

class CSPSolver(object):
    """
    """
    def __init__(self, problem):
        self.problem = problem

    def forward_checking(self, var_repr, domain):
        """
        Execute forward_checking over a given `domain` when a variable
        `var_num` is recently assignated.
        Returns a new domain free of restrictions
        """
        new_domain = domain.copy()
        val_var_repr = self.problem.assignment[var_repr]
        for vn in self.problem.unassigned_variables:
            new_domain[vn] = filter(lambda val: \
                                    not self.problem.check_conflicts((var_repr, val_var_repr),
                                                                     (vn, val)), domain[vn])
        return new_domain

    def mrv_select(self, domain):
        """
        Returns the number of the variable with the minimum remaining value
        in a given `domain`
        """
        var_repr = None
        min_domain_size = maxint
        for vn in self.problem.unassigned_variables:
            if len(domain[vn]) < min_domain_size:
                var_repr = vn
                min_domain_size = len(domain[vn])
        return var_repr

    def solve(self, domain):
        """
        """
        if self.problem.is_solved:
            self.problem.print_result()
            return
##         import pdb
##         pdb.set_trace()
        var_repr = self.mrv_select(domain)
        for value in domain[var_repr]:
            self.problem.assign(var_repr, value)
            new_domain = self.forward_checking(var_repr, domain)
            self.solve(new_domain)
            self.problem.assign(var_repr, None)


def solver(problem_class, *args, **kwargs):
    """
    Solves a problem with CSPSolver
    """
    problem = problem_class(*args, **kwargs)
    solver = CSPSolver(problem)
    solver.solve(problem.complete_domain)
