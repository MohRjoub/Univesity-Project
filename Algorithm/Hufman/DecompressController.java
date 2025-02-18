package application;

import java.io.File;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
// class to control decompress screen
public class DecompressController {
	 private Huffman huffman;
	    private FileChooser fileChooser;

	    public DecompressController() {
	        huffman = new Huffman(); // Initialize Huffman object
	        fileChooser = new FileChooser();
	        fileChooser.setTitle("Select a File");
	        fileChooser.setInitialDirectory(new File("C:\\Users\\ACTC\\Desktop"));
	    
	        // Add a filter to show only .huf files
	     	FileChooser.ExtensionFilter hufFilter = new FileChooser.ExtensionFilter("Huff Files only (*.huf)", "*.huf");
	     	fileChooser.getExtensionFilters().add(hufFilter);
	        File file = fileChooser.showOpenDialog(new Stage());

	        if (file != null) {
	            showProgressScreen(file);
	        }
	    }

	    private void showProgressScreen(File file) {
	        Stage progressStage = new Stage();
	        progressStage.initModality(Modality.APPLICATION_MODAL);
	        progressStage.setTitle("Decompressing File");

	        Label progressLabel = new Label("Decompressing...");
	        progressLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");

	        ProgressBar progressBar = new ProgressBar();
	        progressBar.setPrefWidth(300);
	        progressBar.setStyle("-fx-accent: #007bff;");

	        VBox progressBox = new VBox(20, progressLabel, progressBar);
	        progressBox.setAlignment(Pos.CENTER);
	        progressBox.setStyle("-fx-padding: 20; -fx-background-color: #1e3d59;");

	        Scene progressScene = new Scene(progressBox, 400, 150);
	        progressStage.setScene(progressScene);

	        // Task to perform decompression in the background
	        Task<Void> compressionTask = new Task<>() {
	            @Override
	            protected Void call() throws Exception {
	                huffman.decompress(file); // Perform decompression
	                return null;
	            }

	            @Override
	            protected void succeeded() {
	                progressStage.close();
	                Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Decompression Complete");
	                alert.setHeaderText(null);
	                alert.setContentText("The file has been successfully decompressed.");
	                alert.getDialogPane().setStyle("-fx-background-color: #1e3d59; -fx-border-color: #007bff;");
	                alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");
	                alert.showAndWait();
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

	    // method to show alert when error occur
	    private void showError(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.getDialogPane().setStyle("-fx-background-color: #1e3d59; -fx-border-color: #007bff;");
	        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #ffffff;");
	        alert.showAndWait();
	    }
}
