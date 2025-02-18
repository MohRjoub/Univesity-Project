package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

// class to control start the game 
public class mainController implements Initializable {
	@FXML
	private Button startButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		startButton.setOnMousePressed(e -> startButton.setStyle(startButton.getStyle() +" -fx-translate-y: 1px; "));
		startButton.setOnMouseReleased(e -> startButton.setStyle(startButton.getStyle().replace(" -fx-translate-y: 1px;", "")));

		// Duration.millis(200) used for the time needed for applying the scale
		// Create ScaleTransition for mouse enter
		ScaleTransition hoverEnter = new ScaleTransition(Duration.millis(200), startButton);
		hoverEnter.setToX(1.1); // Scale up to 110% on X
		hoverEnter.setToY(1.1); // Scale up to 110% on Y

		// Create ScaleTransition for mouse exit
		ScaleTransition hoverExit = new ScaleTransition(Duration.millis(200), startButton);
		hoverExit.setToX(1.0); 
		hoverExit.setToY(1.0); 
		
		startButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> hoverEnter.playFromStart());
		startButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> hoverExit.playFromStart());
		
		// start the game action
		startButton.setOnAction(e -> {
			try {
				startButton.getScene().getWindow().hide();
				Stage home = new Stage();
				home.setResizable(false);
				Parent root = FXMLLoader.load(getClass().getResource("/FXML/players.fxml"));
				Scene scene = new Scene(root);
                Image icon = new Image("file:C://Users//ACTC//Downloads//icon.png");
                home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}

		});

	}

}
