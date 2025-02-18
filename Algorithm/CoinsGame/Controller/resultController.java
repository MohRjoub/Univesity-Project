package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
// class to control showing score and result 
public class resultController implements Initializable{
	
	@FXML
	private Button closeButton;
	@FXML
	private TextField firstPlayerScoreTF;
	@FXML
	private TextField secondPlayerScoreTF;
	@FXML
	private ScrollPane firstPlayerCoinsBox;
	@FXML
	private ScrollPane secondPlayerCoinsBox; 
	@FXML
	private Label firstPlayerName;
	@FXML
	private Label secondPlayerName;
	
	private GridPane coins1Pane;
	private GridPane coins2Pane;

	
	private int[] firstPlayerCoins;
	private int[] secondPlayerCoins;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		coins1Pane = new GridPane();
		coins2Pane = new GridPane();
		
		addHover(closeButton);
		
		closeButton.setOnMousePressed(e -> closeButton.setStyle(closeButton.getStyle() + " -fx-translate-y: 1px; "));
		closeButton.setOnMouseReleased(e -> closeButton.setStyle(closeButton.getStyle().replace(" -fx-translate-y: 1px;", "")));

		firstPlayerScoreTF.setEditable(false);
		secondPlayerScoreTF.setEditable(false);
		
		firstPlayerScoreTF.setText("0"); // initial score 
		firstPlayerScoreTF.setAlignment(Pos.CENTER);
		secondPlayerScoreTF.setText("0");// initial score 
		secondPlayerScoreTF.setAlignment(Pos.CENTER);

		closeButton.setOnAction(e -> {
			try {
			closeButton.getScene().getWindow().hide();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		});	
		
	}

	public int[] getCoins1() {
		return firstPlayerCoins;
	}

	
	
	public Label getFirstPlayerName() {
		return firstPlayerName;
	}

	public void setFirstPlayerName(String firstPlayerName) {
		this.firstPlayerName.setText(firstPlayerName);
	}

	public Label getSecondPlayerName() {
		return secondPlayerName;
	}

	public void setSecondPlayerName(String secondPlayerName) {
		this.secondPlayerName.setText(secondPlayerName);
	}
	
	// method to set and show first player coins
//	public void setfirstPlayerCoins(ArrayList<Integer> coins) {
//		this.firstPlayerCoins = coins;
//		for (int i = 0; i < coins.length; i++) {
//			if (coins[i] == 0) break;
//			Label coin = new Label(String.valueOf(coins[i]));
//			coin.setAlignment(Pos.CENTER);
//			coin.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #efaf23;"
//					+ " -fx-background-color: #efaf23; -fx-padding: 1;");
//			coin.setMaxWidth(Double.MAX_VALUE);
//			coin.setMinSize(50, 50);
//			coin.setFont(new Font("Arial", 20));
//			StackPane stackPane = new StackPane();
//			stackPane.getChildren().add(coin);
//			stackPane.setPadding(new Insets(5));
//
//			coins1Pane.add(stackPane, 1, i);
//			if (i == 0) {
//				firstPlayerScoreTF.setText(coins[i] + "");
//				firstPlayerScoreTF.setAlignment(Pos.CENTER);
//			}
//			else {
//				firstPlayerScoreTF.setText(Integer.parseInt(firstPlayerScoreTF.getText().trim()) + coins[i] + "");
//				firstPlayerScoreTF.setAlignment(Pos.CENTER);
//			}
//		}
//		firstPlayerCoinsBox.setContent(coins1Pane);
//	}
	
	// method to set and show second player coins
	public void setsecondPlayerCoins(ArrayList<Integer> coins) {
//		this.secondPlayerCoins = coins;
		
		for (int i = 0; i < coins.size(); i++) {
//			if (coins[i] == 0) break;
			Label coin = new Label(String.valueOf(coins.get(i)));
			coin.setAlignment(Pos.CENTER);
			coin.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #efaf23;"
					+ " -fx-background-color: #efaf23; -fx-padding: 1;");
			coin.setMaxWidth(Double.MAX_VALUE);
			coin.setMinSize(50, 50);
			coin.setFont(new Font("Arial", 20));
			StackPane stackPane = new StackPane();
			stackPane.getChildren().add(coin);
			stackPane.setPadding(new Insets(5));

			coins2Pane.add(stackPane, 1, i );
			if (i == 0) {
				secondPlayerScoreTF.setText(coins.get(i) + "");
				secondPlayerScoreTF.setAlignment(Pos.CENTER);
			}
			else {
				secondPlayerScoreTF.setText(Integer.parseInt(secondPlayerScoreTF.getText().trim()) + coins.get(i) + "");
				secondPlayerScoreTF.setAlignment(Pos.CENTER);
			}

		}
		secondPlayerCoinsBox.setContent(coins2Pane);
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
