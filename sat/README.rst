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
de salida de zchaff sólo hay que cambiar a False la variable
`FORMAT_OUTPUT` al inicio del archivo *solver.py*.

Reglas de traducción
====================
Sudoku
------
Para traducir la representación del input indicada en el enunciado se
crearon estructuras (3-tuplas) que representan variables del tipo
A_{i,j,k}* y funciones auxiliares que transforman una 3-tupla en una
variable X que pertenece al rango [1,729] utilizadas en el archivo
CNF.

De esta forma, al momento de generar todas las clausulas (tanto
estáticas como de instancia) de un sudoku, se utilizan las variables
representadas en tuplas, y al momento de escribirlas en el archivo que
va a ser input de zchaff se transforman a un entero, para cumplir las
especificaciones del formato requerido en el archivo cnf.

* Una variable A_{i,j,k} es una variable booleana que dice que en la
fila i, columna j del tablero sudoku se encuentra escrito el número k.

Card Puzzle
-----------
Se traduce cada carta como una variable, por lo tanto hay n variables
numeradas del 1 al n y cada variable representa si la carta está
colocada de la manera en la que fué definida, negar una variable es
semánticamente equivalente a decir que la carta esta colocada al
revés. Luego se crean clausulas para conseguir tapar cada uno de los
2*i agujeros, en cada clausula se escribe que cartas pueden tapar un
agujero en especifico, ya sea en su posición original o volteada.

La traducción del resultado devuelto por zchaff es casi inexistente,
pués es identica a la pedida en el enunciado.

Solitario
---------
Al igual que en el juego sudoku, se realizó una representación de
variables del tipo A_{i,j,k} donde los subindices i y j representan la
posición en fila-columna del tablero, y k representa 0: no hay metra,
1: hay una metra azul, 2 hay una metra roja.

De forma similar, al momento de escribir las clausulas en el archivo
cnf, las variables representadas en 3-tuplas se transforman a enteros
en el rango [1-(m*m*3)] donde m es el tamaño del tablero.

Para el caso de este juego, en el output de zChaff sólo se analiza si
el resultado fué 'Satisfiable', si lo fué se imprime 1, de lo
contrario se imprime 0.


Sudoku Hard
===========
Las estadísticas de la resolución de las 95 instancias difíciles de
sudoku se adjuntan en el archivo sudoku-hard-statistics.
