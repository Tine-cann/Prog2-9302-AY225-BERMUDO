/**
 * =====================================================
 * Student Name    : Kristine O. Bermudo
 * Course          : Math 101 - Linear Algebra
 * Assignment      : Programming Assignment 1 - 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : 04-08-2026
 * GitHub Repo     : https://github.com/Tine-cann/uphsd-cs-bermudo-kristine
 *
 * Description:
 *   This program computes the determinant of a hardcoded 3x3 matrix assigned
 *   to Kristine O. Bermudo for Math 101. The solution is computed using cofactor
 *   expansion along the first row. Each intermediate step (2x2 minor,
 *   cofactor term, running sum) is printed to the console in a readable format.
 * =====================================================
 */
public class DeterminantSolver {

    // ── SECTION 1: Matrix Declaration ───────────────────────────────────
    // Declare the 3x3 matrix assigned to this student.
    // Values are hardcoded as a 2D integer array in row-major order.
    static int[][] matrix = {
        { 1, 4, 2 },   // Row 1 of assigned matrix
        { 3, 2, 5 },   // Row 2 of assigned matrix
        { 6, 1, 3 }    // Row 3 of assigned matrix
    };

    // ── SECTION 2: 2x2 Determinant Helper ───────────────────────────────
    static int computeMinor(int a, int b, int c, int d) {
        return (a * d) - (b * c);
    }

    // ── SECTION 3: Output Helpers ───────────────────────────────────────
    static void printSeparator() {
        for (int i = 0; i < 52; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    static void printMatrix(int[][] m) {
        System.out.println("+-----------------+");
        for (int[] row : m) {
            System.out.printf("|  %2d  %2d  %2d  |%n", row[0], row[1], row[2]);
        }
        System.out.println("+-----------------+");
    }

    // ── SECTION 4: Step-by-Step Determinant Solver ──────────────────────
    static void solveDeterminant(int[][] m) {
        printSeparator();
        System.out.println("  3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("  Student: Kristine O. Bermudo");
        System.out.println("  Assigned Matrix:");
        printSeparator();
        printMatrix(m);
        printSeparator();

        int minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
        System.out.printf("  Step 1 - Minor M11: det([%d,%d],[%d,%d]) = (%d*%d)-(%d*%d) = %d%n",
            m[1][1], m[1][2], m[2][1], m[2][2],
            m[1][1], m[2][2], m[1][2], m[2][1], minor11);

        int minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
        System.out.printf("  Step 2 - Minor M12: det([%d,%d],[%d,%d]) = (%d*%d)-(%d*%d) = %d%n",
            m[1][0], m[1][2], m[2][0], m[2][2],
            m[1][0], m[2][2], m[1][2], m[2][0], minor12);

        int minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
        System.out.printf("  Step 3 - Minor M13: det([%d,%d],[%d,%d]) = (%d*%d)-(%d*%d) = %d%n",
            m[1][0], m[1][1], m[2][0], m[2][1],
            m[1][0], m[2][1], m[1][1], m[2][0], minor13);

        int c11 =  m[0][0] * minor11;
        int c12 = -m[0][1] * minor12;
        int c13 =  m[0][2] * minor13;

        System.out.println();
        System.out.printf("  Cofactor C11 = (+1) * %d * %d = %d%n", m[0][0], minor11, c11);
        System.out.printf("  Cofactor C12 = (-1) * %d * %d = %d%n", m[0][1], minor12, c12);
        System.out.printf("  Cofactor C13 = (+1) * %d * %d = %d%n", m[0][2], minor13, c13);

        int det = c11 + c12 + c13;
        System.out.printf("%n  det(M) = %d + (%d) + %d%n", c11, c12, c13);
        printSeparator();
        System.out.printf("  DETERMINANT = %d%n", det);

        if (det == 0) {
            System.out.println("  WARNING: The matrix is SINGULAR - it has no inverse.");
        }
        printSeparator();
    }

    // ── SECTION 5: Entry Point ───────────────────────────────────────────
    public static void main(String[] args) {
        solveDeterminant(matrix);
    }
}
