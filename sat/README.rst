=================
 Proyecto 4: SAT
=================

Integrantes
===========
 + 04-36723 - Daniel Barreto
 + 05-38675 - Kristoffer Pantic

Requerimientos
==============
Sólo python > 2.5.2, creo, y zChaff 2007.3.12

Instalación
===========
Cambiar la variable `ZCHAFF_PATH` que se encuentra en el inicio del
archivo *solver.py*, ahí se coloca el path completo hacia la carpeta
que contiene el ejecutable de zchaff. Si esta variable se omite, por
defecto el ejecutable se buscará en './zchaff/zchaff' (relativo a la
carpeta de ejecución del proyecto).

Si se quiere hacer que cualquiera de los programas devuelva el formato
de salida de zchaff sólo hay que cambiar la variable `FORMAT_OUTPUT`
al inicio del archivo *solver.py*.

Reglas de traducción
====================
Sudoku
------
Hablar sobre sudIndex y cnfIndex, sobre que representa cada variable

Card Puzzle
-----------
Hablar sobre que representa cada variable y sobre su interpretación
en el resultado

Solitario
---------
Bastante parecido al sudoku, kizas mencionar porque es importante
utilizar una variable para cuando no hay ninguna metra en una posición
dada

Sudoku Hard
===========
Se adjuntan en el archivo sudoku-hard-statistics.
