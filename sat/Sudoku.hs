{-|
  CNF - sudoku solver
-}

module Main (
-- * Main function
  -- *
) where
import System.IO
import System.Exit

type CNFindex = Integer -- Numeros del 1 al 9, no del 0 al 8 porque la var 0 no tiene negado (hint: -0 = 0)
type SudIndex = (Integer, Integer, Integer) -- Numeros del 1 al 9, no del 0 al 8 porque la var 0 no tiene negado (hint: -0 = 0)
type Clause   = [CNFindex]

-- helpers
sudIndex2CNFindex :: SudIndex -> CNFindex
sudIndex2CNFindex (i, j, k) = 81 * (i-1) + 9 * (j-1) + (k-1) + 1

cnfIndex2SudIndex :: CNFindex -> SudIndex
cnfIndex2SudIndex i = ((div (div (i-1) 9) 9) + 1, (mod (div (i-1) 9) 9) + 1, (mod (i-1) 9) + 1)

--
-- Static clauses
--

-- non-vacuity clauses
nonVacuity :: [Clause]
nonVacuity = nonVacuity' 1 1

nonVacuity' :: Integer -> Integer -> [Clause]
nonVacuity' i j
            | j > 9 = nonVacuity' (i+1) 0
            | i > 9 = []
            | otherwise = [(nonVacuity'' i j 1)] ++ (nonVacuity' i (j+1))

nonVacuity'' :: Integer -> Integer -> Integer -> Clause
nonVacuity'' i j k
             | k <= 9 = [sudIndex2CNFindex (i, j, k)] ++ nonVacuity'' i j (k+1)
             | otherwise = []

-- Uniqueness of each cell
uniqueness :: [Clause]
uniqueness = uniqueness' 1 1

uniqueness' :: Integer -> Integer -> [Clause]
uniqueness' i j
            | i > 9 = []
            | j > 9 = uniqueness' (i+1) 1
            | otherwise = (uniqueness'' i j 1 2) ++ (uniqueness' i (j+1))

uniqueness'' :: Integer -> Integer -> Integer -> Integer -> [Clause]
uniqueness'' i j d d'
             | d <= 9 && d' <= 9 = [[-sudIndex2CNFindex (i, j, d), -sudIndex2CNFindex (i, j, d')]] ++ uniqueness'' i j d (d'+1)
             | d > 9  = []
             | d' > 9 = uniqueness'' i j (d+1) (d+2)

-- Rows and colums
rowsRestrictions :: [Clause] -- Static clause list of sudoku rows restrictions
rowsRestrictions = rowsRestrictions' 1 1

rowsRestrictions' :: Integer -> Integer -> [Clause]
rowsRestrictions' i d
                  | i > 9 = []
                  | d > 9 = rowsRestrictions' (i+1) 1 -- next row
                  | otherwise = (rowsRestrictions'' i 1 2 d) ++ (rowsRestrictions' i (d+1))

rowsRestrictions'' :: Integer -> Integer -> Integer -> Integer -> [Clause]
rowsRestrictions'' i j j' d
                   | j <= 9 && j' <= 9 = [[-sudIndex2CNFindex (i, j, d), -sudIndex2CNFindex (i, j', d)]] ++ rowsRestrictions'' i j (j'+1) d
                   | j > 9  = []
                   | j' > 9 = rowsRestrictions'' i (j+1) (j+2) d


columnsRestrictions :: [Clause] -- Static clause list of sudoku columns restrictions
columnsRestrictions = columnsRestrictions' 1 1

columnsRestrictions' :: Integer -> Integer -> [Clause]
columnsRestrictions' j d
                     | j > 9 = []
                     | d > 9 = columnsRestrictions' (j+1) 1 -- next column
                     | otherwise = (columnsRestrictions'' 1 2 j d) ++ (columnsRestrictions' j (d+1))

columnsRestrictions'' :: Integer -> Integer -> Integer -> Integer -> [Clause]
columnsRestrictions'' i i' j d
                      | i <= 9 && i' <= 9 = [[-sudIndex2CNFindex (i, j, d), -sudIndex2CNFindex (i', j, d)]] ++ columnsRestrictions'' i (i'+1) j d
                      | i > 9  = []
                      | i' > 9 = columnsRestrictions'' (i+1) (i+2) j d


-- Blocks restrictions
