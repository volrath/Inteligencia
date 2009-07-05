import sys

# Auxiliar Function.
def is_aux(variable):
    """
    Says if a variable is auxiliar
    """
    return variable.find('_var') != -1

# The real deal.
class Cryptarithmetic(object):
    """
    variables: set of letters
    assignation: hash of letters => integer value
    constraints: tuples of the form: ([letters], [operators])
                 where letters are from the variable's list and operators
                 are {'+', '-', '=', '+10x'}
    """
    def __init__(self, words):
        """
        `words` is a string list of length 3.
        Generates the set of variables, the assignation hash and the list of
        constraints
        """
        self.variables = set()
        for word in words:
            self.variables = self.variables.union(list(word))
        self.assignment = dict().fromkeys(list(self.variables))
        self.constraints = self._generate_constraints(*tuple(words))

    def _add_variable(self, variable):
        """
        Add a variable to the context
        """
        self.assignment[variable] = None
        self.variables.add(variable)
        self.extra_variables.append(variable)

    def _generate_constraints(self, op1, op2, result):
        """
        Generates a list of constraints given the three words.

        Given only three words, i can classify the constraints using the
        number of variables involve:
         * 2 vars: V1 = W2
         * 3 vars: V1 + V/W2 = V3
         * 4 vars: V1 + V2 = V3 + 10xW4 OR... FUCK... V1 + V2 + W3 = V4
         * 5 vars: V1 + V2 + W3 = V4 + 10xW5

        So, except for the 4vars state, I can just record the exact position
        of the variables involve and drop the operators that connect them.

        In the case of the 4vars, I can use RE to know if the var#3 is of
        the form $_varX.
        """
        self.op1 = list(op1)
        self.op2 = list(op2)
        self.result = list(result)
        self.op1.reverse(); self.op2.reverse(); self.result.reverse()
        self.extra_variables = []
        constraints = []
        extra_var = ''; extra_count = 1
        min_word = min(self.op1, self.op2, key=(lambda l: len(l)))
        for i in range(len(min_word)):
            const_vars = [self.op1[i], self.op2[i]]
            const_vars.append(extra_var) if extra_var else None
            const_vars.extend([self.result[i], '$_var%s' % extra_count])
            #const_self.ops  = ['+']
            #const_self.ops.append('+') if extra_var else None
            #const_self.ops.extend(['=', '+10x'])
            extra_var = '$_var%s' % extra_count; extra_count += 1
            self._add_variable(extra_var)
            constraints.append(tuple(const_vars))
        if len(self.op1) != len(self.op2):
            max_word = max(self.op1, self.op2, key=(lambda l: len(l)))
            len_min_word = len(min_word)
            constraints.append((max_word[len_min_word], extra_var, self.result[len_min_word]))
            for i in range(1, abs(len(self.op1)-len(self.op2))-1):
                constraints.append(max_word[len_min_word+1], self.result[len_min_word+1])
        else:
            if len(self.result) > len(self.op1):
                constraints.append((self.result[-1], extra_var))
        return constraints

    def _get_complete_domain(self):
        """
        Returns a complete domain
        """
        domain = dict().fromkeys(self.variables)
        for key in domain:
            domain[key] = range(2) if is_aux(key) else range(10)
        domain[self.op1[-1]] = range(1, 10)
        domain[self.op2[-1]] = range(1, 10)
        domain[self.result[-1]] = range(1, 10)
        if len(self.result) > max(len(self.op1), len(self.op2)):
            domain[self.result[-1]] = [1]
            if len(self.op1) == len(self.op2):
                domain[self.extra_variables[-1]] = [1]
        return domain
    complete_domain = property(_get_complete_domain)

    def _get_unassigned_variables(self):
        """
        Returns the list of unassigned variables =)
        """
        return [key for key, value in self.assignment.iteritems()
                if value is None]
    unassigned_variables = property(_get_unassigned_variables)

    def _check_constraint(self, const, assignment=None):
        """
        Checks a constraint, where all the variables in the constraint are
        assigned
        Returns True if the constraint is satisfied
        """
        assignment = assignment or self.assignment
        len_constraint = len(const)
        satisfied = False
        if len_constraint == 2:
            return assignment[const[0]] == assignment[const[1]]
        elif len_constraint == 3:
            return assignment[const[0]] + assignment[const[1]] == assignment[const[2]]
        elif len_constraint == 4:
            return assignment[const[0]] + assignment[const[1]] == \
                   assignment[const[2]] + 10 * assignment[const[3]] # CHECK THIS, LOOK AT LINE Nro 38
        elif len_constraint == 5:
            return assignment[const[0]] + assignment[const[1]] + assignment[const[2]] == \
                   assignment[const[3]] + 10 * assignment[const[4]]

    def _complete_constraints_with(self, pair1, pair2, assignment=None):
        """
        Returns all the constraints in which the variable `pair1` and pair2
        (as defined in check_conflicts) are involved and it's completely
        assigned.
        """
        assignment = assignment or self.assignment
        return [const for const in self.constraints if pair1[0] in const \
                and pair2[0] in const \
                and all(map((lambda var: assignment[var] is not None), const))]

    def assign(self, var, value):
        """
        Make an assignment in this problem's assignment representation
        """
        self.assignment[var] = value

    @property
    def is_solved(self):
        """
        Check if every letter has an assignation
        """
        for value in self.assignment.itervalues():
            if value is None:
                return False
        return True

    def check_conflicts(self, pair1, pair2, *args, **kwargs):
        """
        Check if there's a conflict between two variables, each variables
        represented by a pair of the form: (var_num, value)
        
        Returns True if there's a conflict
        """
        # Check alldiff
        if not is_aux(pair1[0]) and not is_aux(pair2[0]) \
               and pair1[1] == pair2[1]:
            return True
        new_assignment = self.assignment.copy()
        new_assignment[pair2[0]] = pair2[1]
        # return not all the constraints satisfied
        return not all([self._check_constraint(constraint, new_assignment)
                        for constraint in \
                        self._complete_constraints_with(pair1, pair2, new_assignment)])

    def print_result(self, result=None):
        """
        Just print.
        """
        result = result or self.assignment
        print ' '.join(['='.join(map(str, pair))
                        for pair in result.iteritems()
                        if pair[0] not in self.extra_variables])
