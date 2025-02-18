package application;

import java.util.Arrays;

public class KnownPlaintextAttack {
    private static final int MOD_VALUE = 256;

    public static void main(String[] args) {
        // Define the plaintext and ciphertext matrices
        int[][] P = {
                {14, 21, 47},
                {10, 20, 47},
                {11, 25, 52}
        };

        int[][] C = {
                {123, 220, 62},
                {75, 152, 221},
                {206, 39, 145}
        };

        try {
            // Compute the modular inverse of the plaintext matrix
            int[][] PInverse = invertMatrixMod(P);

            // Compute the key matrix K = (C * P^-1) mod MOD_VALUE
            int[][] K = multiplyMatricesMod(C, PInverse);

            // Print the key matrix
            System.out.println("Key Matrix (K):");
            for (int[] row : K) {
                System.out.println(Arrays.toString(row));
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Multiply two matrices modulo MOD_VALUE.
     */
    public static int[][] multiplyMatricesMod(int[][] A, int[][] B) {
        int rowsA = A.length, colsA = A[0].length, colsB = B[0].length;
        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] = (result[i][j] + A[i][k] * B[k][j]) % MOD_VALUE;
                }
            }
        }
        return result;
    }

    /**
     * Compute the modular inverse of a 3x3 matrix modulo MOD_VALUE.
     */
    public static int[][] invertMatrixMod(int[][] matrix) {
        if (matrix.length != 3 || matrix[0].length != 3) {
            throw new IllegalArgumentException("Only 3x3 matrices are supported.");
        }

        // Calculate determinant modulo MOD_VALUE
        int determinant = computeDeterminant(matrix);
        determinant = (determinant % MOD_VALUE + MOD_VALUE) % MOD_VALUE;

        // Compute modular multiplicative inverse of the determinant
        int determinantInverse = modInverse(determinant, MOD_VALUE);
        if (determinantInverse == -1) {
            throw new IllegalArgumentException("Matrix is not invertible under mod " + MOD_VALUE);
        }

        // Calculate the adjugate matrix
        int[][] adjugate = calculateAdjugate(matrix);

        // Multiply adjugate by determinant inverse modulo MOD_VALUE
        int[][] inverse = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inverse[i][j] = (adjugate[i][j] * determinantInverse) % MOD_VALUE;
                if (inverse[i][j] < 0) {
                    inverse[i][j] += MOD_VALUE;
                }
            }
        }
        return inverse;
    }

    /**
     * Compute the determinant of a 3x3 matrix.
     */
    public static int computeDeterminant(int[][] matrix) {
        return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2])
             - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[2][0] * matrix[1][2])
             + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1]);
    }

    /**
     * Calculate the adjugate of a 3x3 matrix.
     */
    public static int[][] calculateAdjugate(int[][] matrix) {
        int[][] adjugate = new int[3][3];

        adjugate[0][0] = (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2]) % MOD_VALUE;
        adjugate[0][1] = (matrix[0][2] * matrix[2][1] - matrix[0][1] * matrix[2][2]) % MOD_VALUE;
        adjugate[0][2] = (matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) % MOD_VALUE;

        adjugate[1][0] = (matrix[1][2] * matrix[2][0] - matrix[1][0] * matrix[2][2]) % MOD_VALUE;
        adjugate[1][1] = (matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) % MOD_VALUE;
        adjugate[1][2] = (matrix[1][0] * matrix[0][2] - matrix[0][0] * matrix[1][2]) % MOD_VALUE;

        adjugate[2][0] = (matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1]) % MOD_VALUE;
        adjugate[2][1] = (matrix[2][0] * matrix[0][1] - matrix[0][0] * matrix[2][1]) % MOD_VALUE;
        adjugate[2][2] = (matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]) % MOD_VALUE;

        // Ensure all elements are non-negative
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (adjugate[i][j] < 0) {
                    adjugate[i][j] += MOD_VALUE;
                }
            }
        }

        return adjugate;
    }

    /**
     * Compute the modular multiplicative inverse of a number under a given modulus.
     */
    public static int modInverse(int a, int mod) {
        for (int x = 1; x < mod; x++) {
            if ((a * x) % mod == 1) {
                return x;
            }
        }
        return -1; // No modular inverse exists
    }
}
