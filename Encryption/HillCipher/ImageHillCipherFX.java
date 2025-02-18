package application;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ImageHillCipherFX extends Application {
    private ImageView originalImageView;
    private ImageView encryptedImageView;
    private ImageView decryptedImageView;
    private TextField[][] keyMatrixInputs;
    private int modValue = 256;
    private int[][] keyMatrix;
    private Image originalImage;
    private Image encryptedImage;

    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Hill Cipher Image Encryption");

    	VBox mainLayout = new VBox(10);
    	mainLayout.setStyle("-fx-background-color: black;");
    	mainLayout.setPadding(new Insets(10));
    	mainLayout.setAlignment(Pos.CENTER);

    	Label titleLabel = new Label("Hill Cipher Image Encryption");
    	titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: lime; -fx-font-weight: bold;");

    	VBox matrixSection = new VBox(5);
    	matrixSection.setAlignment(Pos.CENTER);
    	Label matrixLabel = new Label("Key Matrix (3x3):");
    	matrixLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: lime;");
    	matrixSection.getChildren().addAll(matrixLabel, createMatrixInputGrid());

    	Button updateSettingsButton = new Button("Update Settings");
    	updateSettingsButton.setStyle("-fx-background-color: lime; -fx-text-fill: black; -fx-font-weight: bold;");

    	setupImageViews();
    	HBox buttonBox = createButtonBox(primaryStage);
    	GridPane imageGrid = createImageGrid();

    	mainLayout.getChildren().addAll(titleLabel, matrixSection, updateSettingsButton, buttonBox, imageGrid);

    	updateSettingsButton.setOnAction(e -> updateSettings());

    	Scene scene = new Scene(mainLayout, 1000, 600);
    	scene.getStylesheets().add("hacker-style.css");
    	primaryStage.setScene(scene);
    	primaryStage.show();

    }

    private GridPane createMatrixInputGrid() {
        GridPane matrixGrid = new GridPane();
        matrixGrid.setAlignment(Pos.CENTER);
        matrixGrid.setHgap(5);
        matrixGrid.setVgap(5);
        keyMatrixInputs = new TextField[3][3];

        int[][] defaultMatrix = {
                {6, 24, 1},
                {13, 16, 10},
                {20, 17, 15}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TextField input = new TextField(String.valueOf(defaultMatrix[i][j]));
                input.setPrefWidth(60);
                keyMatrixInputs[i][j] = input;
                matrixGrid.add(input, j, i);
            }
        }

        keyMatrix = defaultMatrix;
        return matrixGrid;
    }

    private void setupImageViews() {
        originalImageView = new ImageView();
        encryptedImageView = new ImageView();
        decryptedImageView = new ImageView();

        int viewSize = 300;
        originalImageView.setFitWidth(viewSize);
        originalImageView.setFitHeight(viewSize);
        encryptedImageView.setFitWidth(viewSize);
        encryptedImageView.setFitHeight(viewSize);
        decryptedImageView.setFitWidth(viewSize);
        decryptedImageView.setFitHeight(viewSize);

        originalImageView.setPreserveRatio(true);
        encryptedImageView.setPreserveRatio(true);
        decryptedImageView.setPreserveRatio(true);
    }

    private HBox createButtonBox(Stage primaryStage) {
        Button loadButton = new Button("Load Image");
        Button encryptButton = new Button("Encrypt");
        Button decryptButton = new Button("Decrypt");

        loadButton.setOnAction(e -> loadImage(primaryStage));
        encryptButton.setOnAction(e -> encryptImage());
        decryptButton.setOnAction(e -> decryptImage());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loadButton, encryptButton, decryptButton);
        return buttonBox;
    }

    private GridPane createImageGrid() {
        GridPane imageGrid = new GridPane();
        imageGrid.setAlignment(Pos.CENTER);
        imageGrid.setHgap(10);
        imageGrid.setVgap(10);

        imageGrid.add(new Label("Original Image"), 0, 0);
        imageGrid.add(new Label("Encrypted Image"), 1, 0);
        imageGrid.add(new Label("Decrypted Image"), 2, 0);
        imageGrid.add(originalImageView, 0, 1);
        imageGrid.add(encryptedImageView, 1, 1);
        imageGrid.add(decryptedImageView, 2, 1);

        return imageGrid;
    }

    private void loadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            originalImage = new Image(file.toURI().toString());
            originalImageView.setImage(originalImage);
            encryptedImageView.setImage(null);
            decryptedImageView.setImage(null);
        }
    }

    private void encryptImage() {
            if (originalImage == null) {
                showAlert("Error", "Please load an image first");
                return;
            }

            if (!isMatrixInvertible(keyMatrix)) {
                showAlert("Error", "Current matrix is not invertible! Encryption will not be reversible.");
                return;
            }

        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();
        WritableImage output = new WritableImage(width, height);

        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y += 3) {  // Step by 3 in y direction
            for (int x = 0; x < width; x += 1) { // Step by 1 in x direction
                processBlock(reader, writer, x, y, width, height, true);
            }
        }

        encryptedImage = output;
        encryptedImageView.setImage(encryptedImage);
        
        try {
			writeImagePixelsToFile(output, "cipher.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

    private void decryptImage() {
        if (encryptedImage == null) {
            showAlert("Error", "Please encrypt an image first");
            return;
        }

        int width = (int) encryptedImage.getWidth();
        int height = (int) encryptedImage.getHeight();
        WritableImage output = new WritableImage(width, height);

        PixelReader reader = encryptedImage.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y += 3) { // Match this with encryptImage
            for (int x = 0; x < width; x += 1) {
                processBlock(reader, writer, x, y, width, height, false);
            }
        }


        decryptedImageView.setImage(output);
        
        try {
			writeImagePixelsToFile(output, "plain.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void writePixelsToFile(WritableImage writableImage, String outputFilePath) throws IOException {
        PixelReader pixelReader = writableImage.getPixelReader();

        // Create a FileWriter for the output file
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            // Loop through the image pixels
            for (int y = 0; y < writableImage.getHeight(); y++) {
                for (int x = 0; x < writableImage.getWidth(); x++) {
                    // Get ARGB values of the current pixel
                    int pixel = pixelReader.getArgb(x, y);
                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = pixel & 0xFF;

                    // Write RGB values to the file
                    writer.write(red + " " + green + " " + blue + "\n");
                }
            }
        }
    }
    
 // Method to write the image pixels to a file (ARGB values)
    public void writeImagePixelsToFile(WritableImage image, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            PixelReader reader = image.getPixelReader();
            
            // Get the width and height of the image
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();

            // Write the dimensions of the image
            writer.write(width + " " + height);
            writer.newLine();

            // Loop through each pixel in the image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = reader.getArgb(x, y);

                    // Write the ARGB value to the file
                    writer.write(Integer.toString(argb));
                    writer.newLine();
                }
            }
        }
    }


    private void processBlock(PixelReader reader, PixelWriter writer,
                              int startX, int startY, int width, int height,
                              boolean encrypt) {
        for (int dy = 0; dy < 3; dy++) {
            if (startY + dy >= height) continue;
            for (int dx = 0; dx < 3; dx++) {
                if (startX + dx >= width) continue;

                int argb = reader.getArgb(startX + dx, startY + dy);
                int alpha = (argb >> 24) & 0xFF;
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;

                int[] block = { red, green, blue };
                int[] processedBlock = encrypt ? encryptBlock(block) : decryptBlock(block);

                // Normalize to ensure pixel values are within [0, 255]
                int newRed = (processedBlock[0] + modValue) % modValue;
                int newGreen = (processedBlock[1] + modValue) % modValue;
                int newBlue = (processedBlock[2] + modValue) % modValue;
                
                int newArgb = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                writer.setArgb(startX + dx, startY + dy, newArgb);
            }
        }
    }

    private int[] encryptBlock(int[] block) {
        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                sum = (sum + keyMatrix[i][j] * block[j]) % modValue;
            }
            result[i] = (sum + modValue) % modValue; // Normalize to positive
        }
        return result;
    }

    private int[] decryptBlock(int[] block) {
        int[][] inverseMatrix = calculateInverseMatrix(keyMatrix);
        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                sum = (sum + inverseMatrix[i][j] * block[j]) % modValue;
            }
            result[i] = (sum + modValue) % modValue; // Normalize to positive
        }
        return result;
    }

    private void updateSettings() {
        try {
            int[][] newMatrix = new int[3][3];

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int value = Integer.parseInt(keyMatrixInputs[i][j].getText());

                    if (value < 0 || value > 255) {
                        showAlert("Error", "All values must be in the range of 0 to 255 and positive.");
                        return;
                    }

                    newMatrix[i][j] = value % modValue;
                }
            }

            // Check if the matrix is invertible
            if (!isMatrixInvertible(newMatrix)) {
                showAlert("Error", "Matrix is not invertible modulo " + modValue + " or invalid.");
                return;
            }

            keyMatrix = newMatrix;
            showAlert("Success", "Settings updated successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers.");
        }
    }

    private boolean isMatrixInvertible(int[][] matrix) {
        int det = calculateDeterminant(matrix);
        return findModularMultiplicativeInverse(det, modValue) != -1;
    }

    public int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    private int calculateDeterminant(int[][] matrix) {
        return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2])
                - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
    }

    private int findModularMultiplicativeInverse(int A, int M) {

        if (gcd(A, M) > 1) {
            return -1;
        }

        for (int X = 1; X < M; X++) {
            if (((A % M) * (X % M)) % M == 1) {
                return X;
            }
        }

        return 1;
    }
    

    private int[][] calculateInverseMatrix(int[][] matrix) {
        int[][] inverse = new int[3][3];
        int det = calculateDeterminant(matrix);
        int detInv = findModularMultiplicativeInverse(det, modValue);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[][] minor = getMinor(matrix, i, j);
                int cofactor = ((i + j) % 2 == 0 ? 1 : -1) *
                        (minor[0][0] * minor[1][1] - minor[0][1] * minor[1][0]);
                inverse[j][i] = ((cofactor * detInv) % modValue + modValue) % modValue; // Transpose and normalize
            }
        }

        return inverse;
    }

    private int[][] getMinor(int[][] matrix, int row, int col) {
        int[][] minor = new int[2][2];
        int minorRow = 0;
        for (int i = 0; i < 3; i++) {
            if (i == row) continue;
            int minorCol = 0;
            for (int j = 0; j < 3; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
