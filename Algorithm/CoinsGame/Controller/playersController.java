package Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
// class to to control select play mode
public class playersController implements Initializable {
	@FXML
	private Button onePlayerButton;
	@FXML
	private Button twoPlayerButton;
	private ButtonType doneButton;
	private ButtonType cancelButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		doneButton = new ButtonType("Done");
		cancelButton = new ButtonType("Cancel");

        Image icon = new Image("file:C://Users//ACTC//Downloads//icon.png");

		addHover(onePlayerButton);
		addHover(twoPlayerButton);
		
		onePlayerButton.setOnMousePressed(e -> onePlayerButton.setStyle(onePlayerButton.getStyle() +" -fx-translate-y: 1px; "));
		onePlayerButton.setOnMouseReleased(e -> onePlayerButton.setStyle(onePlayerButton.getStyle().replace(" -fx-translate-y: 1px;", "")));

		twoPlayerButton.setOnMousePressed(e -> twoPlayerButton.setStyle(twoPlayerButton.getStyle() +" -fx-translate-y: 1px; "));
		twoPlayerButton.setOnMouseReleased(e -> twoPlayerButton.setStyle(twoPlayerButton.getStyle().replace(" -fx-translate-y: 1px;", "")));

		// one player action
		onePlayerButton.setOnAction(e -> {

			try {
				onePlayerButton.getScene().getWindow().hide();
				Stage home = new Stage();
				home.setResizable(false);
				Parent root = FXMLLoader.load(getClass().getResource("/FXML/input.fxml"));
				Scene scene = new Scene(root);
                home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		});
		// two player action
		twoPlayerButton.setOnAction(e2 -> {
			
			try {
				// alert to Enter Players Name
				Alert dialog = new Alert(Alert.AlertType.NONE, "", doneButton, cancelButton);
				dialog.setTitle("Enter Players Name");
				((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(icon);
				
				TextField firstPlayerTF = new TextField();
				TextField secondPlayerTF = new TextField();
				
				Label message = new Label("Enter Players Name");
				
				VBox content = new VBox(10, message, firstPlayerTF, secondPlayerTF);
				content.setStyle("-fx-padding: 15; -fx-background-color: #fffff0;");
				content.setPrefWidth(300);
				content.setPrefHeight(200);
				content.setAlignment(Pos.CENTER);

				message.setStyle("-fx-text-fill: #efaf23; -fx-font-size: 16px; -fx-font-weight: bold;");

				firstPlayerTF.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: #333; -fx-padding: 5; -fx-border-color: #efaf23; -fx-border-radius: 5;");
				firstPlayerTF.setPromptText("First Player Name");

				secondPlayerTF.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: #333; -fx-padding: 5; -fx-border-color: #efaf23; -fx-border-radius: 5;");
				secondPlayerTF.setPromptText("Second Player Name");

				DialogPane dialogPane = dialog.getDialogPane();
				dialogPane.setContent(content);
				dialogPane.setStyle("-fx-background-color: #fffff0; -fx-border-width: 2; -fx-border-color: #efaf23; -fx-border-radius: 10;");

				Node doneButtonNode = dialogPane.lookupButton(doneButton);
				doneButtonNode.setDisable(true);
				doneButtonNode.setStyle("-fx-background-color: #efaf23; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
						+ "-fx-background-radius: 20;");
				
				doneButtonNode.setOnMouseEntered(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("efaf23", "d9a023")));
				doneButtonNode.setOnMouseExited(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("d9a023", "efaf23")));

				Node cancelButtonNode = dialogPane.lookupButton(cancelButton);
				cancelButtonNode.setStyle("-fx-background-color: #ccc; -fx-text-fill: #333; -fx-font-size: 14px; -fx-background-radius: 20;");
				cancelButtonNode.setOnMouseEntered(e -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("ccc", "bbb")));
				cancelButtonNode.setOnMouseExited(e -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("bbb", "ccc")));

				String firstPlayerName;
				String secondPlayerName;
				

				// Add listeners to the text fields to determine when to enable the done button
				firstPlayerTF.textProperty().addListener(e -> {
					doneButtonNode.setDisable(
				        firstPlayerTF.getText().trim().isEmpty() || secondPlayerTF.getText().trim().isEmpty()
				    );
				});

				secondPlayerTF.textProperty().addListener(e -> {
					doneButtonNode.setDisable(
				        firstPlayerTF.getText().trim().isEmpty() || secondPlayerTF.getText().trim().isEmpty()
				    );
				});
				Optional<ButtonType> result = dialog.showAndWait();
				if (result.isPresent() && result.get() == doneButton &&
						(!firstPlayerTF.getText().trim().equals("")) && (!secondPlayerTF.getText().trim().equals(""))) {
				firstPlayerName = firstPlayerTF.getText().trim();
				secondPlayerName = secondPlayerTF.getText().trim();
				onePlayerButton.getScene().getWindow().hide();
				Stage home = new Stage();
				home.setResizable(false);
				FXMLLoader input = new FXMLLoader(getClass().getResource("/FXML/input2.fxml"));
				Parent root = input.load();
				InputTwoController controller = input.getController();
				controller.setPlayerOne(firstPlayerName + " Score");
				controller.setPlayerTwo(secondPlayerName + " Score");
				Scene scene = new Scene(root);
                home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
				}
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		});
	}

	// method to add hover effect to buttons
	private void addHover(Button button) {
		ScaleTransition hoverEnter = new ScaleTransition(Duration.millis(200), button);
		hoverEnter.setToX(1.1);
		hoverEnter.setToY(1.1);

		ScaleTransition hoverExit = new ScaleTransition(Duration.millis(200), button);
		hoverExit.setToX(1.0);
		hoverExit.setToY(1.0);
		
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> hoverEnter.playFromStart());
		button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> hoverExit.playFromStart());
	}

}
