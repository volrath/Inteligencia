# -*- coding: utf-8 -*-

from os import remove, error, path
from commands import getoutput

ZCHAFF_PATH = '' # without trailing slash!
FORMAT_OUTPUT = True

def solver(problem_class, *args, **kwargs):
    """
    Solves a given `problem` with its given arguments.
    """
    zchaff_exec = path.join(ZCHAFF_PATH or './zchaff', 'zchaff')
    if not path.isfile(zchaff_exec):
        print 'No se encontró el archivo de zChaff.'
        import sys
        sys.exit(0)

    problem = problem_class(*args, **kwargs)
    cnf_file = open('cnf_%s' % problem, 'w')
    cnf_file.write('p cnf %s %s\nc\nc Clauses:\nc\n' % (problem.num_variables,
                                                       problem.num_clauses))
    for clause in problem.clauses:
        cnf_file.write('%s 0\n' % ' '.join([str(s) for s in clause]))
    cnf_file.close()

    output = getoutput(zchaff_exec + ' %s' % cnf_file.name)
    try:
        remove(cnf_file.name)
    except error:
        print 'Warning: No se pudo borrar el archivo auxiliar utilizado para zchaff'

    if not FORMAT_OUTPUT:
        print output
        import sys
        sys.exit(0)
    # Else, format the output with the problem specific format
    satisfiable = output.find('Satisfiable')
    if satisfiable != -1:
        problem.write_solution([int(n) for n in \
                                output[satisfiable+12:].split(' Random')[0].split()])
    else:
        problem.write_solution(False)
