package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

//class to control compress screen
public class CompressController {
    private Huffman huffman;
    private FileChooser fileChooser;

    public CompressController() {
        huffman = new Huffman(); // Initialize Huffman object
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.setInitialDirectory(new File("C:\\Users\\ACTC\\Desktop"));

		addFilters(fileChooser, "extensions.txt");
        // choose the file
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            showProgressScreen(file);
        }
    }

    // method to show progress screen 
    private void showProgressScreen(File file) {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Compressing File");

        Label progressLabel = new Label("Wait until Compressing");
        progressLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #007bff;");

        VBox progressBox = new VBox(20, progressLabel, progressBar);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setStyle("-fx-padding: 20; -fx-background-color: #1e3d59;");

        Scene progressScene = new Scene(progressBox, 400, 150);
        progressStage.setScene(progressScene);

        // Task to run compression in the background
        Task<Void> compressionTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                huffman.compress(file); // Perform compression
                return null;
            }

            @Override
            protected void succeeded() {
                progressStage.close();
                showResultsScreen(file);
            }

            @Override
            protected void failed() {
                progressStage.close();
                showError("Compression Failed", getException().getLocalizedMessage());
            }
        };

        progressStage.show();
        new Thread(compressionTask).start();
    }

    // method to show statistics screen after compressing
    private void showResultsScreen(File file) {
        Stage resultsStage = new Stage();
        resultsStage.setTitle("Compression Results");

        // Text area for the header
        TextArea headerArea = new TextArea(huffman.getHeader().toString());
        headerArea.setEditable(false);
        headerArea.setPrefHeight(200);
        headerArea.setStyle("-fx-background-color: #ffffff; -fx-border-color: #007bff; -fx-border-width: 2px;");

        VBox headerBox = new VBox(10, headerArea);
        headerBox.setStyle("-fx-padding: 10; -fx-background-color: #1e3d59;");

        // Labels for size details
        Label originalSizeLabel = new Label("Original Size: " + huffman.getOrginalSize() + " bytes");
        Label headerSizeLabel = new Label("Header Size: " + huffman.getHeaderSize() + " bytes");
        Label compressedSizeLabel = new Label("Compressed Size: " + huffman.getCompressedSize() + " bytes");
        Label fileTypeLabel = new Label("File type: " + huffman.getFileType());
        double compressionRate = ((double) (huffman.getOrginalSize() - huffman.getCompressedSize()) / huffman.getOrginalSize()) * 100;
        Label compressionRateLabel = new Label(String.format("Compression Rate: %.2f%%", compressionRate));

        originalSizeLabel.setStyle("-fx-text-fill: #ffffff;");
        compressedSizeLabel.setStyle("-fx-text-fill: #ffffff;");
        compressionRateLabel.setStyle("-fx-text-fill: #ffffff;");
        fileTypeLabel.setStyle("-fx-text-fill: #ffffff;");
        headerSizeLabel.setStyle("-fx-text-fill: #ffffff;");

        VBox sizeBox = new VBox(10,headerSizeLabel, originalSizeLabel, compressedSizeLabel, fileTypeLabel, compressionRateLabel);
        sizeBox.setStyle("-fx-padding: 10; -fx-background-color: #1e3d59; -fx-border-color: #007bff; -fx-border-width: 2px;");

        // Table for frequency array
        TableView<Frequency> frequencyTable = new TableView<>();
        frequencyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Frequency, Character> charColumn = new TableColumn<>("Character");
        charColumn.setCellValueFactory(data -> data.getValue().characterProperty());

        TableColumn<Frequency, Integer> freqColumn = new TableColumn<>("Frequency");
        freqColumn.setCellValueFactory(data -> data.getValue().frequencyProperty().asObject());

        TableColumn<Frequency, String> codeColumn = new TableColumn<>("Huffman Code");
        codeColumn.setCellValueFactory(data -> data.getValue().codeProperty());

        TableColumn<Frequency, Integer> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(data -> data.getValue().sizeProperty().asObject());

        frequencyTable.getColumns().addAll(charColumn, freqColumn, codeColumn, sizeColumn);
        frequencyTable.getItems().addAll(huffman.getFrequencyData());
        frequencyTable.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #007bff;");


        VBox root = new VBox(20, headerBox, sizeBox, frequencyTable);
        root.setStyle("-fx-padding: 20; -fx-background-color: #1565c0;;");
        Scene resultsScene = new Scene(root, 800, 600);
        resultsStage.setScene(resultsScene);
        resultsStage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #1e3d59; -fx-border-color: #007bff;");
        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #ffffff;");
        alert.showAndWait();
    }
    
    private void addFilters(FileChooser fileChooser, String filePath) {
            List<String> extensions = new ArrayList<>();
            
            try (Scanner scanner = new Scanner(new File(filePath))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        extensions.add("*" + line);
                    }
                }

                if (!extensions.isEmpty()) {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Except .huf", extensions));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
}
