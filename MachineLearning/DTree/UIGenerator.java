import java.io.File;


import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

public class UIGenerator {
    private VBox start;
    private Pane treePane;
    private ScrollPane scrollPane;
    public UIGenerator() {
        start = new VBox();
        treePane = new Pane();
        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\ACTC\\Downloads"));
        Button selectFile = new Button("Select File");
        Button generateTree = new Button("Generate Tree");
        ComboBox<String> targetClass = new ComboBox<>();
        targetClass.setPromptText("Select Target Class");
        TextField splitRatio = new TextField();
        splitRatio.setMaxWidth(200);
        splitRatio.setPromptText("Enter Test Data Split Ratio");
        start.getChildren().addAll(selectFile, targetClass, splitRatio, generateTree);
        start.setAlignment(javafx.geometry.Pos.CENTER);
        start.setSpacing(10);
        Scene scene = new Scene(start, 300, 300);
        Driver.getStage().setScene(scene);
        selectFile.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(Driver.getStage());
            if(file != null) {
                DataHandler.readData(file.getAbsolutePath());
                String[] columns = DataHandler.getColumns();
                targetClass.getItems().addAll(columns);
            }
        });

        generateTree.setOnAction(e -> {
            try {
                if(targetClass.getValue() != null && !splitRatio.getText().isEmpty()) {
                    double ratio = Double.parseDouble(splitRatio.getText());
                    if (ratio < 0 || ratio > 1) {
                        splitRatio.clear();
                        splitRatio.setPromptText("Invalid Ratio (0-1)");
                        return;
                    }
                    DataHandler.splitData(ratio);
                    DecisionTree tree = new DecisionTree(DataHandler.getTrainingData(), targetClass.getValue());
                    TestData.testData(tree, DataHandler.getTestData());
                    treePane.getChildren().clear();
                    drawTree(tree.getRoot(), 800, 50, 600);
                    scrollPane.setContent(treePane);
                    // create scene with screen size
                    double screenWidth = Screen.getPrimary().getBounds().getWidth();
                    double screenHeight = Screen.getPrimary().getBounds().getHeight();
                    Scene treeScene = new Scene(scrollPane, screenWidth, screenHeight); 

                    Driver.getStage().setScene(treeScene);
                    Driver.getStage().fullScreenProperty();
                    Driver.getStage().setX(0);
                    Driver.getStage().setY(0);

                }
            } catch (NumberFormatException e1) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a valid number for test data split ratio");
                alert.showAndWait();
            } catch (Exception e1) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("An Error Occurred while generating the tree");
                alert.showAndWait();
            }
        });

    }

    public void drawTree(TNode node, double x, double y, double horizontalSpacing) {
        if (node == null) {
            return;
        }
    
        Label label = new Label(" " + node.getAttribute() + " ");
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setMinWidth(40);
        if (node.isLeaf()) {
            label.setStyle("-fx-background-color: lightgreen; -fx-border-color: black; -fx-border-width: 1px;" +
            " -fx-text-size: 30px; -fx-text-alignment: center;"); 
        } else {
            label.setStyle("-fx-background-color: lightcoral; -fx-border-color: black; -fx-border-width: 1px;" +
             " -fx-text-size: 30px; -fx-text-alignment: center;");
            
        }
        treePane.getChildren().add(label);
        
        double verticalSpacing = 200;
    
        int childIndex = -1;
        int side = -1; 
        for (String childKey : node.getChildren().keySet()) {
            TNode child = node.getChildren().get(childKey);
            double childX = x + ((childIndex+2)/2) * horizontalSpacing * side;

            if (child != null) {
                side *= -1;

                Line line = new Line(x + 20, y + 20, childX + 20, y + verticalSpacing);
                line.setStrokeWidth(2);
                double midX = (line.getStartX() + line.getEndX()) / 2;
                double midY = (line.getStartY() + line.getEndY()) / 2;
                Label childLabel = new Label(" " + childKey + " ");
                childLabel.setLayoutX(midX - 50);
                childLabel.setLayoutY(midY);
                treePane.getChildren().addAll(childLabel, line);
                childIndex++;
            }
    
            drawTree(child, childX, y + verticalSpacing, horizontalSpacing /1.5);
        }
    }   
    
}
