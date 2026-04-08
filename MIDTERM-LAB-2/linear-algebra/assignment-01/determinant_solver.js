/**
 * =====================================================
 * Student Name    : Kristine O. Bermudo
 * Course          : Math 101 — Linear Algebra
 * Assignment      : Programming Assignment 1 — 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : 04-08-2026
 * GitHub Repo     : https://github.com/Tine-cann/uphsd-cs-bermudo-kristine
 * Runtime         : Node.js (run with: node determinant_solver.js)
 *
 * Description:
 *   JavaScript equivalent of DeterminantSolver.java. This script computes
 *   the determinant of the same hardcoded 3x3 matrix using cofactor expansion
 *   along the first row. All intermediate steps are logged to the console
 *   using console.log() for complete transparency of the solution process.
 * =====================================================
 */

// ── SECTION 1: Matrix Declaration ───────────────────────────────────
const matrix = [
    [1, 4, 2],   // Row 1
    [3, 2, 5],   // Row 2
    [6, 1, 3]    // Row 3
];

// ── SECTION 2: Matrix Printer ────────────────────────────────────────
function printMatrix(m) {
    console.log(`┌               ┐`);
    m.forEach(row => {
        const fmt = row.map(v => v.toString().padStart(3)).join("  ");
        console.log(`│ ${fmt}  │`);
    });
    console.log(`└               ┘`);
}

// ── SECTION 3: 2×2 Determinant Helper ───────────────────────────────
function computeMinor(a, b, c, d) {
    return (a * d) - (b * c);
}

// ── SECTION 4: Step-by-Step Determinant Solver ──────────────────────
function solveDeterminant(m) {
    const line = "=".repeat(52);

    console.log(line);
    console.log("  3x3 MATRIX DETERMINANT SOLVER");
    console.log("  Student: Kristine O. Bermudo");
    console.log("  Assigned Matrix:");
    console.log(line);
    printMatrix(m);
    console.log(line);
    console.log();
    console.log("Expanding along Row 1 (cofactor expansion):");
    console.log();

    const minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
    console.log(
        `  Step 1 — Minor M₁₁: det([${m[1][1]},${m[1][2]}],[${m[2][1]},${m[2][2]}])` +
        ` = (${m[1][1]}×${m[2][2]}) - (${m[1][2]}×${m[2][1]}) = ${minor11}`
    );

    const minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
    console.log(
        `  Step 2 — Minor M₁₂: det([${m[1][0]},${m[1][2]}],[${m[2][0]},${m[2][2]}])` +
        ` = (${m[1][0]}×${m[2][2]}) - (${m[1][2]}×${m[2][0]}) = ${minor12}`
    );

    const minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
    console.log(
        `  Step 3 — Minor M₁₃: det([${m[1][0]},${m[1][1]}],[${m[2][0]},${m[2][1]}])` +
        ` = (${m[1][0]}×${m[2][1]}) - (${m[1][1]}×${m[2][0]}) = ${minor13}`
    );

    const c11 =  m[0][0] * minor11;
    const c12 = -m[0][1] * minor12;
    const c13 =  m[0][2] * minor13;

    console.log();
    console.log(`  Cofactor C₁₁ = (+1) × ${m[0][0]} × ${minor11} = ${c11}`);
    console.log(`  Cofactor C₁₂ = (-1) × ${m[0][1]} × ${minor12} = ${c12}`);
    console.log(`  Cofactor C₁₃ = (+1) × ${m[0][2]} × ${minor13} = ${c13}`);

    const det = c11 + c12 + c13;
    console.log();
    console.log(`  det(M) = ${c11} + (${c12}) + ${c13}`);
    console.log(line);
    console.log(`  ✓  DETERMINANT = ${det}`);

    if (det === 0) {
        console.log("  ⚠ The matrix is SINGULAR — it has no inverse.");
    }
    console.log(line);
}

// ── SECTION 5: Program Entry Point ──────────────────────────────────
solveDeterminant(matrix);
