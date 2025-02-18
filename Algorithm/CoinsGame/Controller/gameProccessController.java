package Controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
// class to handle Optimal Strategy for a Game using Dynamic Programming
public class gameProccessController implements Initializable{
	@FXML
	private ScrollPane coinsScrollPane;// coins container
	@FXML
	private Button resetButton; // reset the game button
	@FXML
	private Button startButton; // start the game button
	@FXML
	private Button showTabelButton; 
	@FXML
	private Button showResultButton;
	@FXML
	private TextField expectedValueTF; // optimal value

	private GridPane coinsPane;
	
	private int[] coins;
	
	private ArrayList<Integer> optimalCoins1;
	private ArrayList<Integer> optimalCoins2;
	
	private int[][] dpTableArray; // two dimensional array to hold table integers
	private IntegerProperty[][] dpTable; // two dimensional array to hold table view data

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        Image icon = new Image("file:C://Users//ACTC//Downloads//icon.png");

        // add hover effect on the button
		addHover(showResultButton);
		addHover(showTabelButton);
		addHover(resetButton);
		addHover(startButton);
		
		resetButton.setOnMousePressed(e -> resetButton.setStyle(resetButton.getStyle() + " -fx-translate-y: 1px; "));
		resetButton.setOnMouseReleased(e -> resetButton.setStyle(resetButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		startButton.setOnMousePressed(e -> startButton.setStyle(startButton.getStyle() +" -fx-translate-y: 1px; "));
		startButton.setOnMouseReleased(e -> startButton.setStyle(startButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		showResultButton.setOnMousePressed(e -> showResultButton.setStyle(showResultButton.getStyle() +" -fx-translate-y: 1px; "));
		showResultButton.setOnMouseReleased(e -> showResultButton.setStyle(showResultButton.getStyle().replace(" -fx-translate-y: 1px;", "")));
		
		showTabelButton.setOnMousePressed(e -> showTabelButton.setStyle(showTabelButton.getStyle() +" -fx-translate-y: 1px; "));
		showTabelButton.setOnMouseReleased(e -> showTabelButton.setStyle(showTabelButton.getStyle().replace(" -fx-translate-y: 1px;", "")));

		expectedValueTF.setEditable(false);

		coinsPane = new GridPane();
		coinsPane.setMaxHeight(148.5);
		coinsPane.setPrefHeight(148.5);
		coinsPane.setMinWidth(680.0);
		coinsPane.setAlignment(Pos.CENTER);
		coinsScrollPane.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #000000; -fx-background-color: #fffff0");
		
		expectedValueTF.setFocusTraversable(false);
		
		
		//reset game button action
		resetButton.setOnAction(e -> {
			try {
			startButton.getScene().getWindow().hide();
			Stage home = new Stage();
			home.setResizable(false);
				Parent root = FXMLLoader.load(getClass().getResource("/FXML/input.fxml"));
				Scene scene = new Scene(root);
                home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		});
		
		// start and calculate max result action
		startButton.setOnAction(e -> {
			int result = maxProfit(coins);
			expectedValueTF.setText(result + "");
			expectedValueTF.setAlignment(Pos.CENTER);
		});
		
		// show score and chosen coins action
		showResultButton.setOnAction(e -> {
			try {
				Stage home = new Stage();
				home.setResizable(false);
				FXMLLoader resultPane = new FXMLLoader(getClass().getResource("/FXML/result.fxml"));
				Parent root = resultPane.load();
				resultController resultController = resultPane.getController();
				// get the coins that gives the max score
				getChosenCoins(coins, dpTableArray, 0, dpTableArray.length - 1);
				
				int[] firstPlayerCoins = new int[coins.length / 2];
				int[] secondPlayerCoins = new int[coins.length / 2];
				// set each player coins
//				for (int i = 0, j = 0, k = 0; i < optimalCoins.length; i++) {
//					if (optimalCoins[i] > 0) {
//						firstPlayerCoins[j++] = coins[i];
//					} else {
//						secondPlayerCoins[k++] = coins[i];
//					}
//				}
//				resultController.setfirstPlayerCoins(firstPlayerCoins);
				resultController.setsecondPlayerCoins(optimalCoins2);
				Scene scene = new Scene(root);
				home.getIcons().add(icon);
				home.setScene(scene);
				home.show();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}

		});
		
		// load and show DBTable action
		showTabelButton.setOnAction(e -> {
			if (this.dpTableArray != null) {
				try {
					Stage home = new Stage();
					home.setResizable(false);
					FXMLLoader tablePane = new FXMLLoader(getClass().getResource("/FXML/DBTabel.fxml"));
					DBTableController controller = new DBTableController();
					controller.setDpTableArray(dpTable);
					controller.setCoins(coins);
					tablePane.setController(controller);
					Parent root = tablePane.load();
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
		
	}

	public int[] getCoins() {
		return coins;
	}
	
	// method to add the coins for the game to pane
	public void setCoins(int[] coins) {
		this.coins = coins;
		for (int i = 0; i < coins.length; i++) {
			Label coin = new Label(String.valueOf(coins[i]));
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
		}
		coinsScrollPane.setContent(coinsPane);
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

		// method calculate max score using dynamic programming
		private int maxProfit(int[] coins) {

			int n = coins.length; // number of coins
			
		    dpTableArray = new int[n][n]; // used for calculation
		    dpTable = new IntegerProperty[n][n]; // used for show the table to the user
		    
		    optimalCoins1 = new ArrayList<>(); // used to hold coins that gives the max score
		    optimalCoins2 = new ArrayList<>(); // used to hold coins that gives the max score
		    
		    
		    // fill the table under the diagonal with zeros because j < i which means there is no coins
		    for (int i = 1; i < n; i++) {
				for (int j = 0; j < i; j++) {
					dpTable[i][j] = new SimpleIntegerProperty(0);
				}
			}

		    
		    for (int i = 0; i < n; i++) {
			    // initial cases where only one coin
		    	  dpTableArray[i][i] = coins[i]; //add it to the diagonal 
			      dpTable[i][i] = new SimpleIntegerProperty(coins[i]);
			     // initial cases if there are two coins
			        if (i != n-1) {
			        	 dpTableArray[i][i + 1] = Math.max(coins[i], coins[i + 1]); //  choose the maximum of them
					        dpTable[i][i + 1] = new SimpleIntegerProperty(dpTableArray[i][i + 1]);
			        }
			     // if there are more than two coins
				 // start from three coins test the max in it to until reach the total number 
		        if (i > 1) {
		        	for (int start = 0; start + i < n; start++) {
			            int end = start + i;

			            // calculate the two possible choices for the player
			            int chooseStart = coins[start] + Math.min(dpTableArray[start + 2][end], dpTableArray[start + 1][end - 1]);
			            int chooseEnd = coins[end] + Math.min(dpTableArray[start + 1][end - 1], dpTableArray[start][end - 2]);

			            // choose the maximum of the choices
			            dpTableArray[start][end] = Math.max(chooseStart, chooseEnd);
			            dpTable[start][end] = new SimpleIntegerProperty(dpTableArray[start][end]);
			        }
				}
		    }

		    // get the maximum score at the top-right corner of the DB table
		    return dpTableArray[0][n - 1];
		}
		
		// method to get coins that gives the max score
		private void getChosenCoins(int[] coins, int[][] dbTable, int start, int end) {
	        while (start <= end) {
	            int chooseStart = coins[start] + (start + 2 <= end ? Math.min(dbTable[start + 2][end], dbTable[start + 1][end - 1]) : 0);
	            int chooseEnd = coins[end] + (start <= end - 2 ? Math.min(dbTable[start + 1][end - 1], dbTable[start][end - 2]) : 0);

	            if (chooseStart >= chooseEnd) {
	            	if (chooseEnd == chooseStart) {
	            		if (coins[start] > coins[end]) {
	            			optimalCoins1.add(coins[start]);
	    	                // check if the opponent chose the i + 1 coin 
	    	                if (start + 2 <= end && dbTable[start + 2][end] <= dbTable[start + 1][end - 1]) {
	    	                	optimalCoins2.add(coins[start + 1]);
	    	                    start += 2; // the remaining range for my next turn is [i + 2, j]
	    	                } else { // he chose the j - 1 coin
	    	                	// the remaining range for my next turn is [i + 1, j - 1]
	    	                	optimalCoins2.add(coins[end - 1]);
	    	                    start += 1;
	    	                    end -= 1;
	    	                }
						} else {
							optimalCoins1.add(coins[end]);			                //check if the opponent chose the j - 1 coin 
			                if (start <= end - 2 && dbTable[start][end - 2] <= dbTable[start + 1][end - 1]) {
			                    end -= 2;// the remaining range for my next turn is [i, j - 2]
	    	                	optimalCoins2.add(coins[start]);
			                } else { // he chose the i coin
			                	// the remaining range for my next turn is [i + 1, j - 1]
			                    start += 1;
			                    end -= 1;
			                }
						}
	            	}
	            	//the coin at the start is chosen
                	optimalCoins1.add(coins[start]);
	                // check if the opponent chose the i + 1 coin 
	                if (start + 2 <= end && dbTable[start + 2][end] <= dbTable[start + 1][end - 1]) {
	                	optimalCoins2.add(coins[start + 1]);
	                    start += 2; // the remaining range for my next turn is [i + 2, j]
	                } else { // he chose the j - 1 coin
	                	// the remaining range for my next turn is [i + 1, j - 1]
	                	optimalCoins2.add(coins[end - 1]);
	                    start += 1;
	                    end -= 1;
	                }
	            } else {
	            	//the coin at the end is chosen
                	optimalCoins1.add(coins[end]);
	                //check if the opponent chose the j - 1 coin 
	                if (start <= end - 2 && dbTable[start][end - 2] <= dbTable[start + 1][end - 1]) {
	                	optimalCoins2.add(coins[end - 1]);
	                    end -= 2;// the remaining range for my next turn is [i, j - 2]
	                } else { // he chose the i coin
	                	// the remaining range for my next turn is [i + 1, j - 1]
	                	optimalCoins2.add(coins[start]);
	                	start += 1;
	                    end -= 1;
	                }
	            }
	        }

		}


}
