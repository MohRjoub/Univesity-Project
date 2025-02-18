package Controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
//class to read the coins to two player system
public class InputTwoController implements Initializable {
	@FXML
	private Button randomButton;
	@FXML
	private Button fileButton;
	@FXML
	private Button userButton;
	@FXML
	private Button backButton;
	private ButtonType doneButton;
	private ButtonType cancelButton;
	private String playerOne;
	private String playerTwo;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		doneButton = new ButtonType("Done");
		cancelButton = new ButtonType("Cancel");
		
        Image icon = new Image("file:C://Users//ACTC//Downloads//icon.png");

		addHover(randomButton);
		addHover(fileButton);
		addHover(userButton);
		
		randomButton.setOnMousePressed(e -> randomButton.setStyle(randomButton.getStyle() +" -fx-translate-y: 1px; "));
		randomButton.setOnMouseReleased(e -> randomButton.setStyle(randomButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		fileButton.setOnMousePressed(e -> fileButton.setStyle(fileButton.getStyle() +" -fx-translate-y: 1px; "));
		fileButton.setOnMouseReleased(e -> fileButton.setStyle(fileButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		userButton.setOnMousePressed(e -> userButton.setStyle(userButton.getStyle() +" -fx-translate-y: 1px; "));
		userButton.setOnMouseReleased(e -> userButton.setStyle(userButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		// random fill action
		randomButton.setOnAction(e3 -> {

			// create alert to get number of coins

			Alert dialog = new Alert(Alert.AlertType.NONE, "", doneButton, cancelButton);
			dialog.setTitle("Enter Coins number");
			((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(icon);

			// Create a Spinner with integer values to allow the user to choose number of coins
			Spinner<Integer> numOfCoinsSpinner = new Spinner<>();
			numOfCoinsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE, 2, 2));
			numOfCoinsSpinner.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #efaf23; -fx-border-radius: 5; -fx-padding: 5;");

			Label message = new Label("Select Coins number:");
			
			// Create a Spinner with integer values to allow the user to choose range of coins
			Spinner<Integer> startOfCoinsSpinner = new Spinner<>();
			startOfCoinsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
			startOfCoinsSpinner.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #efaf23; -fx-border-radius: 5; -fx-padding: 5;");

			Label rangeMessage = new Label("Select Coins Range:");

			VBox content = new VBox(10, message, numOfCoinsSpinner, rangeMessage, startOfCoinsSpinner);
			content.setStyle("-fx-padding: 15; -fx-background-color: #fffff0;");
			content.setPrefWidth(300);
			content.setPrefHeight(200);
			content.setAlignment(Pos.CENTER);

			message.setStyle("-fx-text-fill: #efaf23; -fx-font-size: 16px; -fx-font-weight: bold;");
			rangeMessage.setStyle("-fx-text-fill: #efaf23; -fx-font-size: 16px; -fx-font-weight: bold;");

			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.setContent(content);
			dialogPane.setStyle("-fx-background-color: #fffff0; -fx-border-width: 2; -fx-border-color: #efaf23; -fx-border-radius: 10;");

			Node doneButtonNode = dialogPane.lookupButton(doneButton);
			doneButtonNode.setStyle("-fx-background-color: #efaf23; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
							+ "-fx-background-radius: 20;");

			doneButtonNode.setOnMouseEntered(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("efaf23", "d9a023")));
			doneButtonNode.setOnMouseExited(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("d9a023", "efaf23")));

			Node cancelButtonNode = dialogPane.lookupButton(cancelButton);
			cancelButtonNode.setStyle("-fx-background-color: #ccc; -fx-text-fill: #333; -fx-font-size: 14px; -fx-background-radius: 20;");
			cancelButtonNode.setOnMouseEntered(e -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("ccc", "bbb")));
			cancelButtonNode.setOnMouseExited(e -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("bbb", "ccc")));

			Optional<ButtonType> result = dialog.showAndWait();
			if (result.isPresent() && result.get() == doneButton) {

				// get the value that represent number Of Coins
				Integer numOfCoins = numOfCoinsSpinner.getValue();
				Integer rangeOfCoins = startOfCoinsSpinner.getValue();
				int[] coins = new int[numOfCoins];
				Random random = new Random();
				// fill coins array with random coins
				for (int i = 0; i < numOfCoins; i++) {
					coins[i] = random.nextInt(rangeOfCoins) + 1;
				}
				try {
					randomButton.getScene().getWindow().hide();
					Stage home = new Stage();
					home.setResizable(false);
					FXMLLoader gameProccess = new FXMLLoader(getClass().getResource("/FXML/gameProccess2.fxml"));
					Parent root = gameProccess.load();
					GameProccessTwoController controller = gameProccess.getController();
					controller.setPlayerOne(playerOne);
					controller.setPlayerTwo(playerTwo);
					controller.setCoins(coins);
					Scene scene = new Scene(root);
					home.getIcons().add(icon);
					home.setScene(scene);
					home.show();
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println(e1.getMessage());
				}
			}
		});
		// manual fill action
		userButton.setOnAction(e1 -> {
			Alert dialog = new Alert(Alert.AlertType.NONE, "", doneButton, cancelButton);
			dialog.setTitle("Enter Coins Separated by Space");
			((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(icon);
			
			TextField coinsTF = new TextField();
			Label message = new Label("Enter Even Number of Coins Separated by Space");
			VBox content = new VBox(10, message, coinsTF);
			content.setStyle("-fx-padding: 15; -fx-background-color: #fffff0;");
			content.setPrefWidth(400);
			content.setPrefHeight(300);
			content.setAlignment(Pos.CENTER);

			message.setStyle("-fx-text-fill: #efaf23; -fx-font-size: 16px; -fx-font-weight: bold;");

			coinsTF.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: #333; -fx-padding: 5; -fx-border-color: #efaf23; -fx-border-radius: 5;");
			coinsTF.setPromptText("1 2 5 15 13 20");

			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.setContent(content);
			dialogPane.setStyle("-fx-background-color: #fffff0; -fx-border-width: 2; -fx-border-color: #efaf23; -fx-border-radius: 10;");

			Node doneButtonNode = dialogPane.lookupButton(doneButton);
			doneButtonNode.setStyle("-fx-background-color: #efaf23; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
					+ "-fx-background-radius: 20;");
			
			doneButtonNode.setOnMouseEntered(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("efaf23", "d9a023")));
			doneButtonNode.setOnMouseExited(e -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("d9a023", "efaf23")));

			Node cancelButtonNode = dialogPane.lookupButton(cancelButton);
			cancelButtonNode.setStyle("-fx-background-color: #ccc; -fx-text-fill: #333; -fx-font-size: 14px; -fx-background-radius: 20;");
			cancelButtonNode.setOnMouseEntered(e -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("ccc", "bbb")));
			cancelButtonNode.setOnMouseExited(e -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("bbb", "ccc")));

			doneButtonNode.setDisable(true);
			
			 
			
			coinsTF.textProperty().addListener((obs, oldText, newText) -> {
			    // allow done button when the TextField is not empty and has even number and all chars are numbers
				boolean isNumber = false;
				String[] coinsString = coinsTF.getText().split("\\s+");
				for (int i = 0; i < coinsString.length; i++) {
					for (int j = 0; j < coinsString[i].trim().length(); j++) {
						if (Character.isDigit(coinsString[i].trim().charAt(j))) {
							coinsTF.setStyle("-fx-background-color: #f9f9f9; -fx-text-fill: #333; -fx-padding: 5;"
									+ " -fx-border-color: #efaf23; -fx-border-radius: 5;");
							isNumber = true;
						} else {
							isNumber = false;
							break;
						}
					}
					if (!isNumber || Long.parseLong(coinsString[i].trim()) > Integer.MAX_VALUE) {
						doneButtonNode.setDisable(true);
						isNumber = false;
						coinsTF.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: red; -fx-border-width: 2px;"
								+ " -fx-border-radius: 5; -fx-text-fill: #333; -fx-padding: 5;");
						break;
					}
				}

			    doneButtonNode.setDisable(coinsTF.getText().trim().isEmpty() || !(coinsString.length % 2 ==0) || !isNumber);
			});
						
			Optional<ButtonType> result = dialog.showAndWait();

			if (result.isPresent() && result.get() == doneButton) {
				String[] coinsString = coinsTF.getText().trim().split("\\s+");
				int[] coins = new int[coinsString.length];
				int i = 0;
				while (i < coins.length) {
					coins[i] = Integer.parseInt(coinsString[i].trim());
					i++;
				}
				
				try {
					randomButton.getScene().getWindow().hide();
					Stage home = new Stage();
					home.setResizable(false);
					FXMLLoader gameProccess = new FXMLLoader(getClass().getResource("/FXML/gameProccess2.fxml"));
					Parent root = gameProccess.load();
					GameProccessTwoController controller = gameProccess.getController();
					controller.setPlayerOne(playerOne);
					controller.setPlayerTwo(playerTwo);
					controller.setCoins(coins);
					Scene scene = new Scene(root);
					home.getIcons().add(icon);
					home.setScene(scene);
					home.show();
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			}
		});
		
		// file fill action
		fileButton.setOnAction(e -> {

			// tell the user what should the file format
			Alert confirm = new Alert(AlertType.INFORMATION, "", doneButton, cancelButton);
			confirm.setHeaderText("Pay attention to the file format");
			confirm.setContentText("File format should be like this:" + "\n Number of coins(even)" + "\n conin1" + "\n conin2"
					+ "\n conin3" + "\n etc...");
			DialogPane dialogPane = confirm.getDialogPane();
			dialogPane.setStyle("-fx-background-color: #fffff0; -fx-border-width: 2; -fx-border-color: #efaf23; -fx-border-radius: 10;");

			Region header = (Region) dialogPane.lookup(".header-panel");
			if (header != null) {
				header.setStyle("-fx-background-color: #efaf23;"); // Set your preferred color
			}
			
			Node doneButtonNode = dialogPane.lookupButton(doneButton);
			doneButtonNode.setStyle("-fx-background-color: #efaf23; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
					+ "-fx-background-radius: 20;");
			
			doneButtonNode.setOnMouseEntered(a -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("efaf23", "d9a023")));
			doneButtonNode.setOnMouseExited(a -> doneButtonNode.setStyle(doneButtonNode.getStyle().replace("d9a023", "efaf23")));

			Node cancelButtonNode = dialogPane.lookupButton(cancelButton);
			cancelButtonNode.setStyle("-fx-background-color: #ccc; -fx-text-fill: #333; -fx-font-size: 14px; -fx-background-radius: 20;");
			cancelButtonNode.setOnMouseEntered(a -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("ccc", "bbb")));
			cancelButtonNode.setOnMouseExited(a -> cancelButtonNode.setStyle(cancelButtonNode.getStyle().replace("bbb", "ccc")));
			
			Optional<ButtonType> result = confirm.showAndWait();

			if (result.isPresent() && result.get() == doneButton) {

			// allow user to choose file contain number of coins and the coins
			FileChooser coinsFileChooser = new FileChooser();
			coinsFileChooser.setTitle("Select a Text File");
			coinsFileChooser.setInitialDirectory(new File("C:\\Users\\ACTC\\Desktop"));

			// Add a filter to show only text files
			FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Text Files only (*.txt)", "*.txt");
			coinsFileChooser.getExtensionFilters().add(textFilter);

			File coinsFile = coinsFileChooser.showOpenDialog(randomButton.getScene().getWindow());

				if (coinsFile != null) {
					Scanner in;
					try {

						in = new Scanner(coinsFile);

						long lineCount = Files.lines(coinsFile.toPath()).count();

						int numberOfCoins = Integer.parseInt(in.nextLine());

						if (lineCount - 1 < numberOfCoins) {
							confirm.setAlertType(AlertType.ERROR);
							confirm.setContentText("No Enough Coins");
							confirm.showAndWait();
						} else {

						if (numberOfCoins % 2 == 0) {

							int[] coins = new int[numberOfCoins];
							int i = 0;
							while (i < numberOfCoins) {
								int coin = Integer.parseInt(in.nextLine().trim());
								if(coin > 0) {
									coins[i] = coin;
									i++;
								} else {
									confirm.setAlertType(AlertType.ERROR);
									confirm.setContentText(coin + " Not Allowed Enter Positive Coins Only");
									confirm.showAndWait();
									return;
								}
							}

							try {
								randomButton.getScene().getWindow().hide();
								Stage home = new Stage();
								home.setResizable(false);
								FXMLLoader gameProccess = new FXMLLoader(getClass().getResource("/FXML/gameProccess2.fxml"));
								Parent root = gameProccess.load();
								GameProccessTwoController controller = gameProccess.getController();
								controller.setPlayerOne(playerOne);
								controller.setPlayerTwo(playerTwo);
								controller.setCoins(coins);
								Scene scene = new Scene(root);
								home.getIcons().add(icon);
								home.setScene(scene);
								home.show();
							} catch (Exception e1) {
								System.out.println(e1.getMessage());
							}
						} else {
							confirm.setAlertType(AlertType.ERROR);
							confirm.setContentText("Number of Coins not Even");
							confirm.showAndWait();
						}
					  }
					} catch (NumberFormatException e2) {
						confirm.setAlertType(AlertType.ERROR);
						confirm.setContentText("File Format not Correct or iteger range exceed");
						confirm.showAndWait();
					}

					catch (Exception e1) {
						confirm.setAlertType(AlertType.ERROR);
						confirm.setContentText(e1.getMessage());
						confirm.showAndWait();
						System.out.println(e1.getMessage());
					}
				}
			}
		});
		
		backButton.setOnAction(e -> {
			try {
				backButton.getScene().getWindow().hide();
				Stage home = new Stage();
				home.setResizable(false);
				Parent root = FXMLLoader.load(getClass().getResource("/FXML/players.fxml"));
				Scene scene = new Scene(root);
                home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
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

	public String getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(String playerOne) {
		this.playerOne = playerOne;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(String playerTwo) {
		this.playerTwo = playerTwo;
	}

}
