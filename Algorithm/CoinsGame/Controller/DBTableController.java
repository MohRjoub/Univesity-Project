package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
//class to control the Dynamic Programming Table
public class DBTableController implements Initializable{
	
	@FXML
	private Button closeButton; // screen close button
	@FXML
	private TableView<RowData> dpTable; // Dynamic Programming Table

	
	private IntegerProperty[][] dpTableArray; // two dimensional array to hold table data
	private int[] coins; // coins array
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		addHover(closeButton);
		
		closeButton.setOnMousePressed(e -> closeButton.setStyle(closeButton.getStyle() + " -fx-translate-y: 1px; "));
		closeButton.setOnMouseReleased(e -> closeButton.setStyle(closeButton.getStyle().replace(" -fx-translate-y: 1px;", "")));

		dpTable.setEditable(false);
		
		// i coins column
        TableColumn<RowData, String> coin = new TableColumn<>("");
        coin.setCellValueFactory(new PropertyValueFactory<>("coin"));
        coin.setStyle("-fx-font-weight: bold; -fx-background-color: linear-gradient(to bottom, #e6e6e6, #dcdcdc);");
        dpTable.getColumns().add(coin);

		//create dpTable columns
		for (int j = 0; j < coins.length; j++) {
			final int index = j;
            TableColumn<RowData, Number> coinColumn = new TableColumn<>("" + coins[j]);
            TableColumn<RowData, Number> indexColumn = new TableColumn<>("j = " + (j));
            indexColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty(index));
            coinColumn.getColumns().add(indexColumn);
            dpTable.getColumns().add(coinColumn);
        }
		
		//fill dpTable rows
        ObservableList<RowData> data = FXCollections.observableArrayList();
        for (int i = 0; i < dpTableArray.length; i++) {
            data.add(new RowData(dpTableArray[i], "i = " + i + ", Coin = " + coins[i]));
		}
        
		dpTable.setItems(data);
		
		closeButton.setOnAction(e -> {
			try {
			closeButton.getScene().getWindow().hide();
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		});
		
		
	}

	public IntegerProperty[][] getDpTableArray() {	
		return dpTableArray;
	}

	public void setDpTableArray(IntegerProperty[][] dpTableArray) {
		this.dpTableArray = dpTableArray;
	}

	public void setCoins(int[] coins) {
		this.coins = coins;
	}

	// method to add hover effect to buttons
	private void addHover(Button button) {
		ScaleTransition hoverEnter = new ScaleTransition(Duration.millis(200), button);
		hoverEnter.setToX(1.1); 
		hoverEnter.setToY(1.1);

		ScaleTransition hoverExit = new ScaleTransition(Duration.millis(200), button);
		hoverExit.setToX(1.0);
		hoverExit.setToY(1.0);

		// Set mouse event handlers
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> hoverEnter.playFromStart());
		button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> hoverExit.playFromStart());
	}

}
