=================
 Proyecto 5: CSP
=================

Integrantes
===========
 + 04-36723 - Daniel Barreto
 + 05-38675 - Kristoffer Pantic

Requerimientos
==============
Sólo python > 2.4

Modo de ejecución
=================
$> python crypto <OPERANDO1> <OPERANDO2> <RESULTADO>

$> python nreinas <NUMERO_DE_REINAS>

Comentarios *IMPORTANTES*
=========================
Para el programa nreinas se realizaron dos implementaciones: 

 + Una como un CSP normal en la cual se empieza con un tablero de
   reinas sin asignación, y simplemente se van escongiendo reinas
   a ubicar usando la aproximación MRV hasta conseguir todos los
   resultados

 + Y otra donde se realizó un CSP con búsqueda local como se
   encuentra explicada en el libro de Norvig en la sección 5.3
   `Local Search for Constraint Satisfaction Problems`. Para esta
   implementación se realizó el algorítmo `min_conflicts` y se
   puede resolver tableros de tamaño muy superior a los logrados
   con la otra implementación, pero dando únicamente una solución.

Para ejecutar la segunda  implementación explicada se tiene que correr
el siguiente comando:

$> python nqueens.py <NUMERO_DE_REINAS>

En el archivo nqueens.py se encuentran ambos algorítmos, el primero
modelado dentro de una clase llamada NQueens y el segundo dentro de
otra clase llamada NQueensLS,

Comentarios menos importantes
=============================
Para la implementación del programa crypto se realizó un CSP sencillo
donde las estructuras usadas fueron:

 + *variables*: Conjunto de letras de las palabras ingresadas.
 + *asignación*: Tabla de hash con letras como claves y numeros como
   valores.
 + *restricciones*: Tuplas que contienen únicamente las
   letras/variables que forman parte de la restricción, colocadas en
   el orden en el cual estarían en la restricción matemática. De esta
   forma, dependiendo de la longitud de la tupla se puede considerar
   la fórmula matemática modelada así:

   * 2 vars: V1 = W2
   * 3 vars: V1 + V/W2 = V3
   * 4 vars: V1 + V2 = V3 + 10xW4
   * 5 vars: V1 + V2 + W3 = V4 + 10xW5

   Donde las V's representan variables principales del problema y las
   W's representan variables auxiliares.
