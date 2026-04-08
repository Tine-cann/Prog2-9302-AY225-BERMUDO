# Overview
This repository contains two implementations (Java and JavaScript) of a program that computes the determinant of a hardcoded 3×3 matrix using cofactor expansion along the first row.

## Student Information
Name: Kristine O. Bermudo
Course: BSIT-GD
Subject: Programming 2
Assignment: Programming Assignment 1 — 3×3 Matrix Determinant Solver
Date: April 8, 2026

## Repository Structure
```
.
├── DeterminantSolver.java        # Java implementation
├── determinant_solver.js         # JavaScript implementation (Node.js)
└── README.md                     # Documentation file
```

## Features
- Prints the assigned 3×3 matrix in a formatted style.
- Computes each 2×2 minor step-by-step.
- Shows the arithmetic for each minor.
- Applies the cofactor expansion rule with alternating signs.
- Displays the final determinant value.
- Warns if the matrix is singular (determinant = 0).

### Example Output
```
====================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: Kristine O. Bermudo
  Assigned Matrix:
====================================================
┌               ┐
│   1   4   2  │
│   3   2   5  │
│   6   1   3  │
└               ┘
====================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 — Minor M₁₁: det([2,5],[1,3]) = (2×3) - (5×1) = 1
  Step 2 — Minor M₁₂: det([3,5],[6,3]) = (3×3) - (5×6) = -21
  Step 3 — Minor M₁₃: det([3,2],[6,1]) = (3×1) - (2×6) = -9

  Cofactor C₁₁ = (+1) × 1 × 1 = 1
  Cofactor C₁₂ = (-1) × 4 × -21 = 84
  Cofactor C₁₃ = (+1) × 2 × -9 = -18

  det(M) = 1 + (84) + -18
====================================================
  ✓  DETERMINANT = 67
====================================================
```

### Notes
Determinant is computed using cofactor expansion along the first row.
The program is hardcoded for the assigned matrix:
```
[1 4 2]
[3 2 5]
[6 1 3]
```