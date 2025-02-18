package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
// class to control two player mode action
public class GameProccessTwoController implements Initializable{
	@FXML
	private ScrollPane coinsBox;
	@FXML
	private Button resetButton;
	@FXML
	private Button showResultButton;
	@FXML
	private Label turnIndicator;
	
	private GridPane coinsPane;
	
	private int[] coins;
	private int[] firstPlayerCoins;
	private int[] secondPlayerCoins;
	
	private Button firstCoins;
	private Button lastCoin;
	
	private Integer i;
	private Integer j;
	private boolean firstTurn = true;
	private boolean secondTurn = false;
	private int firstIndex = 0;
	private int secondIndex = 0;
	private String playerOne;
	private String playerTwo;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		i = new Integer(0);
		
        Image icon = new Image("file:C://Users//ACTC//Downloads//icon.png");

		addHover(showResultButton);
		addHover(resetButton);
		
		resetButton.setOnMousePressed(e -> resetButton.setStyle(resetButton.getStyle() + " -fx-translate-y: 1px; "));
		resetButton.setOnMouseReleased(e -> resetButton.setStyle(resetButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		showResultButton.setOnMousePressed(e -> showResultButton.setStyle(showResultButton.getStyle() +" -fx-translate-y: 1px; "));
		showResultButton.setOnMouseReleased(e -> showResultButton.setStyle(showResultButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		

		coinsPane = new GridPane();
		coinsPane.setMaxHeight(148.5);
		coinsPane.setPrefHeight(148.5);
		coinsPane.setMinWidth(680.0);
		coinsPane.setAlignment(Pos.CENTER);
		coinsBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #000000; -fx-background-color: #fffff0");
		
		//reset game action
		resetButton.setOnAction(e -> {
			try {
				resetButton.getScene().getWindow().hide();
				Stage home = new Stage();
				home.setResizable(false);
				FXMLLoader inpput = new FXMLLoader(getClass().getResource("/FXML/input2.fxml"));
				Parent root = inpput.load();
				InputTwoController controller = inpput.getController();
				controller.setPlayerOne(playerOne);
				controller.setPlayerTwo(playerTwo);
				Scene scene = new Scene(root);
                home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		});
		
		// show score action
		showResultButton.setOnAction(e -> {
			try {
				Stage home = new Stage();
				home.setResizable(false);
				FXMLLoader gameProccess = new FXMLLoader(getClass().getResource("/FXML/result.fxml"));
				Parent root = gameProccess.load();
				resultController controller = gameProccess.getController();
				controller.setFirstPlayerName(playerOne);
				controller.setSecondPlayerName(playerTwo);
				controller.setfirstPlayerCoins(firstPlayerCoins);
				controller.setsecondPlayerCoins(secondPlayerCoins);
				Scene scene = new Scene(root);
				home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}

		});

	}

	public int[] getCoins() {
		return coins;
	}
	// method to initialize the game screen and load the coins 
	public void setCoins(int[] coins) {
		this.coins = coins;
		firstPlayerCoins = new int[coins.length/2];
		secondPlayerCoins = new int[coins.length/2];
		
		for (int i = 0; i < coins.length; i++) {
			Button coin = new Button(String.valueOf(coins[i]));
			coin.setAlignment(Pos.CENTER);
			coin.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #efaf23;"
					+ " -fx-background-color: #efaf23; -fx-padding: 1;");
			coin.setMaxWidth(Double.MAX_VALUE);
			coin.setMinSize(50, 50);
			coin.setFont(new Font("Arial", 20));
			StackPane stackPane = new StackPane();
			stackPane.getChildren().add(coin);
			stackPane.setPadding(new Insets(5));

			coinsPane.add(stackPane, i % 10, i / 10);
			if (i > 0 && i < coins.length -1) {
				coin.setDisable(true);
			}
		}
		coinsBox.setContent(coinsPane);
		ObservableList<Node> coinsButtons = coinsPane.getChildren();
		j = new Integer(coinsButtons.size() - 1);

		
		firstCoins = (Button)(((StackPane) coinsButtons.get(i)).getChildren()).get(0);
		
		firstCoins.setOnAction(e -> {
			if (firstTurn) {
				firstTurn = false;
				secondTurn = true;
				firstPlayerCoins[firstIndex++] = Integer.parseInt(firstCoins.getText());
				turnIndicator.setText(playerTwo.replace("Score", "Turn"));
			} else {
				secondTurn = false;
				firstTurn = true;
				secondPlayerCoins[secondIndex++] = Integer.parseInt(firstCoins.getText());
				turnIndicator.setText(playerOne.replace("Score", "Turn"));
			}
				firstCoins.setDisable(true);
				turn(coinsButtons, firstCoins);
		});
		
		
		lastCoin = (Button)(((StackPane) coinsButtons.get(j)).getChildren()).get(0);
		
		lastCoin.setOnAction(e -> {
			if (firstTurn) {
				firstTurn = false;
				secondTurn = true;
				firstPlayerCoins[firstIndex++] = Integer.parseInt(lastCoin.getText());
				turnIndicator.setText(playerTwo.replace("Score", "Turn"));
			} else {
				secondTurn = false;
				firstTurn = true;
				secondPlayerCoins[secondIndex++] = Integer.parseInt(lastCoin.getText());
				turnIndicator.setText(playerOne.replace("Score", "Turn"));
			}
				lastCoin.setDisable(true);
				turn(coinsButtons, lastCoin);
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
		// method to change players turn 
		private void turn(ObservableList<Node> buttons, Button button) {
			
			if (button == firstCoins && ++i < j) {
				
				firstCoins = (Button) (((StackPane) buttons.get(i)).getChildren()).get(0);
				firstCoins.setDisable(false);
				firstCoins.setOnAction(e -> {
					firstCoins.setDisable(true);
					
					if (firstTurn) {
						
						firstTurn = false;
						secondTurn = true;
						firstPlayerCoins[firstIndex++] = Integer.parseInt(firstCoins.getText());
						turnIndicator.setText(playerTwo.replace("Score", "Turn"));
						
					} else {
						
						secondTurn = false;
						firstTurn = true;
						secondPlayerCoins[secondIndex++] = Integer.parseInt(firstCoins.getText());
						turnIndicator.setText(playerOne.replace("Score", "Turn"));
						
					}
					
					turn(buttons, firstCoins);
				
				});
			
			} else if (--j > i && button == lastCoin) {
			
				lastCoin = (Button) (((StackPane) buttons.get(j)).getChildren()).get(0);
				lastCoin.setDisable(false);
				lastCoin.setOnAction(e -> {
					lastCoin.setDisable(true);
				
					if (firstTurn) {
						
						firstTurn = false;
						secondTurn = true;
						firstPlayerCoins[firstIndex++] = Integer.parseInt(lastCoin.getText());
						turnIndicator.setText(playerTwo.replace("Score", "Turn"));
						
					} else {
						
						secondTurn = false;
						firstTurn = true;
						secondPlayerCoins[secondIndex++] = Integer.parseInt(lastCoin.getText());
						turnIndicator.setText(playerOne.replace("Score", "Turn"));
					
					}
					
					turn(buttons, lastCoin);
					
				});
				
			} else if (++i > j + 2 && --j < i + 2) {
				
				ButtonType doneButton = new ButtonType("Done");
				Alert winAlert = new Alert(Alert.AlertType.INFORMATION, "", doneButton);
				((Stage) winAlert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:C://Users//ACTC//Downloads//icon.png"));
				winAlert.setHeaderText("Winner");
				DialogPane dialogPane = winAlert.getDialogPane();
				dialogPane.setStyle("-fx-background-color: #fffff0; -fx-border-width: 2; -fx-border-color: #efaf23; -fx-border-radius: 10;");
				
				Region header = (Region) dialogPane.lookup(".header-panel");
				if (header != null) {
					header.setStyle("-fx-background-color: #efaf23;"); // Set your preferred color
				}
				Node doneButtonNode = dialogPane.lookupButton(doneButton);
				doneButtonNode.setStyle("-fx-background-color: #efaf23; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
								+ "-fx-background-radius: 20;");

				doneButtonNode.setOnMouseEntered(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("efaf23", "d9a023")));
				doneButtonNode.setOnMouseExited(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("d9a023", "efaf23")));
				turnIndicator.setText("Finished");
				
				int firstScore = 0;
				int secondScore = 0;
				
				for (int i = 0; i < firstPlayerCoins.length; i++) {
					firstScore += firstPlayerCoins[i];
					secondScore += secondPlayerCoins[i];
				}
				
				if (firstScore > secondScore) {
					
					winAlert.setContentText(playerOne.replace("Score", "Win!!"));
					winAlert.show();
					
				} else if (secondScore > firstScore) {
					
					winAlert.setContentText(playerTwo.replace("Score", "Win!!"));
					winAlert.show();
					
				} else {
					
					winAlert.setContentText("The result is equal");
					winAlert.show();
					
				}
			}
		}
		public String getPlayerOne() {
			return playerOne;
		}

		public void setPlayerOne(String playerOne) {
			this.playerOne = playerOne;
			turnIndicator.setText(playerOne.replace("Score", "Turn"));
			turnIndicator.setStyle("-fx-text-fill: #efaf23; -fx-font-size: 14px; -fx-font-weight: bold;");
		}

		public String getPlayerTwo() {
			return playerTwo;
		}

		public void setPlayerTwo(String playerTwo) {
			this.playerTwo = playerTwo;
		}
}
