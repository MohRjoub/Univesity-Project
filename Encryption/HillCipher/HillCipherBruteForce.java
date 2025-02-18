package application;

import java.io.*;
import java.util.*;

public class HillCipherBruteForce {
	private static final int MOD_VALUE = 256; // Modulo value for Hill cipher

	public static void main(String[] args) {
		// Read the known plaintext and ciphertext from files
		int[][] knownPlaintext;
		try {
			knownPlaintext = readImagePixelsFromFile("plain.txt");
			int[][] knownCiphertext = readImagePixelsFromFile("cipher.txt");
			// Perform brute force attack
			hillCipherBruteForceAttack(knownPlaintext, knownCiphertext);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Method to read the image pixels from a file into a 2D array
	public static int[][] readImagePixelsFromFile(String filePath) throws IOException {
		File file = new File(filePath);

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			// Read the dimensions of the image from the first line
			String[] dimensions = reader.readLine().split(" ");
			int width = Integer.parseInt(dimensions[0]);
			int height = Integer.parseInt(dimensions[1]);

			// Create a 2D array to hold the ARGB values
			int[][] pixels = new int[height][width];

			// Read each pixel value and populate the array
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					pixels[y][x] = Integer.parseInt(reader.readLine());
				}
			}

			return pixels;
		}
	}


	// Perform brute force attack on the Hill cipher
	public static void hillCipherBruteForceAttack(int[][] knownPlaintext, int[][] knownCiphertext) {
		// Try all possible 3x3 matrices as potential keys
		for (int i = 0; i < MOD_VALUE; i++) {
			if (i != 1) continue;
			for (int j = 0; j < MOD_VALUE; j++) {
				if (j != 0) continue;
				for (int k = 0; k < MOD_VALUE; k++) {
					if (k != 0) continue;
					for (int l = 0; l < MOD_VALUE; l++) {
						if (l != 0) continue;
						for (int m = 0; m < MOD_VALUE; m++) {
							if (m != 1) continue;
							for (int n = 0; n < MOD_VALUE; n++) {
								if (n != 0) continue;
								for (int o = 0; o < MOD_VALUE; o++) {
									if (o != 0) continue;
									for (int p = 0; p < MOD_VALUE; p++) {
										if (p != 0) continue;
										for (int q = 0; q < MOD_VALUE; q++) {
											if (q != 3) continue;
											int[][] candidateKey = { { i, j, k }, { l, m, n }, { o, p, q } };
											int [][] key = {{1,0,0}, {0,1,0}, {0,0,3}};
											
											// Decrypt using candidate key
											int[][] decryptedText = decryptWithKey(candidateKey, knownCiphertext);

											// Check if decrypted text matches the known plaintext
											if (Arrays.deepEquals(decryptedText, knownPlaintext)) {
												System.out.println("Identity matrix found!");
												printMatrix(candidateKey);
												return;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("No matching key found.");
	}

	// Decrypt the ciphertext with the given key
	public static int[][] decryptWithKey(int[][] key, int[][] ciphertext) {
		int rows = ciphertext.length;
		int cols = ciphertext[0].length;
		int[][] decryptedImage = new int[rows][cols];

		int[][] inverseKey = calculateInverseMatrix(key);
		if (inverseKey == null) {
//            System.out.println("Key has no inverse, cannot decrypt.");
			return null;
		}

		// Process image block-by-block (3x3)
		for (int i = 0; i < rows; i += 3) {
			for (int j = 0; j < cols; j += 1) {
				extractBlock(decryptedImage, ciphertext, i, j, key);
			}
		}

		return decryptedImage;
	}

	// Extract a 3x3 block from the given image, padding if necessary
	public static int[] extractBlock(int[][] decryptedImage, int[][] image, int startRow, int startCol, int[][] key) {
		int[] block = new int[3];
		for (int dy = 0; dy < 3; dy++) {
			if (startRow + dy >= image.length)
				continue;
			for (int dx = 0; dx < 3; dx++) {
				if (startCol + dx >= image[0].length)
					continue;

				int argb = image[startRow + dy][startCol + dx];
				int alpha = (argb >> 24) & 0xFF;
				int red = (argb >> 16) & 0xFF;
				int green = (argb >> 8) & 0xFF;
				int blue = argb & 0xFF;

				block[0] = red;
				block[1] = green;
				block[2] = blue;
				int[] processedBlock = decryptBlock(block, key);

				// Normalize to ensure pixel values are within [0, 255]
				int newRed = (processedBlock[0] + MOD_VALUE) % MOD_VALUE;
				int newGreen = (processedBlock[1] + MOD_VALUE) % MOD_VALUE;
				int newBlue = (processedBlock[2] + MOD_VALUE) % MOD_VALUE;

				int newArgb = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
				decryptedImage[startRow + dy][startCol + dx] = newArgb;
			}
		}
		return block;
	}

	private static int[] decryptBlock(int[] block, int[][] keyMatrix) {
		int[][] inverseMatrix = calculateInverseMatrix(keyMatrix);
		int[] result = new int[3];
		for (int i = 0; i < 3; i++) {
			int sum = 0;
			for (int j = 0; j < 3; j++) {
				sum = (sum + inverseMatrix[i][j] * block[j]) % MOD_VALUE;
			}
			result[i] = (sum + MOD_VALUE) % MOD_VALUE; // Normalize to positive
		}
		return result;
	}

	// Calculate the inverse of a matrix (mod MOD_VALUE)
	public static int[][] calculateInverseMatrix(int[][] matrix) {
		int det = calculateDeterminant(matrix);
		int detInv = findModularMultiplicativeInverse(det, MOD_VALUE);

		if (detInv == -1) {
			return null; // No inverse exists
		}

		int[][] inverse = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int[][] minor = getMinor(matrix, i, j);
				int cofactor = ((i + j) % 2 == 0 ? 1 : -1) * (minor[0][0] * minor[1][1] - minor[0][1] * minor[1][0]);
				inverse[j][i] = ((cofactor * detInv) % MOD_VALUE + MOD_VALUE) % MOD_VALUE; // Transpose and normalize
			}
		}

		return inverse;
	}

	// Calculate the determinant of a 3x3 matrix
	public static int calculateDeterminant(int[][] matrix) {
		return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2])
				- matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
				+ matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
	}

	// Find modular multiplicative inverse
	public static int findModularMultiplicativeInverse(int a, int m) {
		if (gcd(a, m) != 1) {
			return -1; // No inverse exists
		}
		for (int x = 1; x < m; x++) {
			if ((a * x) % m == 1) {
				return x;
			}
		}
		return -1;
	}

	// Find greatest common divisor (GCD)
	public static int gcd(int a, int b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}

	// Get the minor of a matrix (excluding row i and column j)
	public static int[][] getMinor(int[][] matrix, int row, int col) {
		int[][] minor = new int[2][2];
		int minorRow = 0;
		for (int i = 0; i < 3; i++) {
			if (i == row)
				continue;
			int minorCol = 0;
			for (int j = 0; j < 3; j++) {
				if (j == col)
					continue;
				minor[minorRow][minorCol] = matrix[i][j];
				minorCol++;
			}
			minorRow++;
		}
		return minor;
	}

	// Print matrix
	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
}
