package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Main interface
            Label title = new Label("Huffman Coding");
            title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold;");

            Button compressButton = new Button("Compress");
            compressButton.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: #ffffff; -fx-font-size: 14px; "
                    + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");
            Button decompressButton = new Button("Decompress");
            decompressButton.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: #ffffff; -fx-font-size: 14px; "
                    + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");

            compressButton.setOnAction(e -> {
            	new CompressController();
            });
            
            decompressButton.setOnAction(e -> {
            	new DecompressController();
            });
            
            VBox actionButtons = new VBox(15, title, compressButton, decompressButton);
            actionButtons.setSpacing(15);
            actionButtons.setAlignment(Pos.CENTER);

            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #1565c0;");
            root.setCenter(actionButtons);
            
            Scene scene = new Scene(root, 400, 400);
            primaryStage.setTitle("Huffman Coding");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
