package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Directed.DirectedGraph;
import Directed.LinkedList;
import Directed.Dijkstra;
import Directed.DirectedEdge;
import Directed.Stack;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class CapitalsMapController {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Button findButton;
	private DirectedGraph graph;
	private Stack<String> cities;
	private Point2D[] points;
	private ComboBox<String> sourceComboBox;
	private ComboBox<String> targetComboBox;
	private LinkedList<Line> drawedLines;
	private final double R = 6371.0; // Earth's radius in kilometers

	@FXML
	private void initialize() {
		try {
			drawedLines = new LinkedList<>();
			Label sourceLabel = new Label("Source:");
			Label targetLabel = new Label("Target:");
			Label filterLabel = new Label("Filter:");
			Label pathLabel = new Label("Path:");
			Label distanceLabel = new Label("Distance:");
			Label costLabel = new Label("Cost:");
			Label timeLabel = new Label("Time:");
			
			sourceComboBox = new ComboBox<>();
			targetComboBox = new ComboBox<>();
			ComboBox<String> filterComboBox = new ComboBox<>();
			
			sourceComboBox.setPromptText("Select Start");
			targetComboBox.setPromptText("Select End");
			filterComboBox.setPromptText("Select Filter");
			filterComboBox.getItems().addAll("Time", "Cost", "Distance");
			
			TextArea pathTextArea = new TextArea();
			pathTextArea.setPrefHeight(100);
			pathTextArea.setMaxWidth(200);
			pathTextArea.setWrapText(true);
			pathTextArea.setEditable(false);
			
			TextField distanceField = new TextField();
			TextField costField = new TextField();
			TextField timeField = new TextField();
			
			Button runButton = new Button("Run");
			Button swap = new Button("Swap");
			
			HBox sourceBox = new HBox(5, sourceLabel, sourceComboBox);
			sourceBox.setAlignment(Pos.CENTER_LEFT);
			sourceBox.setPadding(new Insets(10));
			sourceBox.setStyle("-fx-background-color: #f0f0f0;");
			
			HBox targetBox = new HBox(5, targetLabel, targetComboBox);
			targetBox.setAlignment(Pos.CENTER_LEFT);
			targetBox.setPadding(new Insets(10));
			targetBox.setStyle("-fx-background-color: #f0f0f0");
			
			HBox filterBox = new HBox(5, filterLabel, filterComboBox);
			filterBox.setAlignment(Pos.CENTER_LEFT);
			filterBox.setPadding(new Insets(10));
			filterBox.setStyle("-fx-background-color: #f0f0f0");
			
			HBox distanceBox = new HBox(5, distanceLabel, distanceField);
			distanceBox.setAlignment(Pos.CENTER_LEFT);
			distanceBox.setPadding(new Insets(10));
			distanceBox.setStyle("-fx-background-color: #f0f0f0;");
			
			HBox costBox = new HBox(5, costLabel, costField);
			costBox.setAlignment(Pos.CENTER_LEFT);
			costBox.setPadding(new Insets(10));
			costBox.setStyle("-fx-background-color: #f0f0f0;");
			
			HBox timeBox = new HBox(5, timeLabel, timeField);
			timeBox.setAlignment(Pos.CENTER_LEFT);
			timeBox.setPadding(new Insets(10));
			timeBox.setStyle("-fx-background-color: #f0f0f0");
			
			HBox swapBox = new HBox(swap);
			swapBox.setAlignment(Pos.CENTER);
			swapBox.setStyle("-fx-background-color: #f0f0f0");
			
			VBox box = new VBox(10, sourceBox, swapBox, targetBox, filterBox, pathLabel, pathTextArea, distanceBox, costBox, timeBox, runButton);
			box.setAlignment(Pos.TOP_LEFT);
			box.setPadding(new Insets(15));
			box.setPrefWidth(300);
			box.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #bbbbbb; -fx-border-radius: 10;");
			
			ImageView map = (ImageView) rootPane.getChildren().get(0);
			map.setFitHeight(660);
			map.setFitWidth(1300);
			BorderPane box2 = new BorderPane();
			box2.setCenter(map);
			box2.setRight(box);
			box2.setPrefHeight(660);
			box2.setPrefWidth(1545);
			rootPane.getChildren().add(box2);
			addCapitalMarkers();
			
			// add capitals to the combo boxes
			while (!cities.isEmpty()) {
				String capitalName = cities.pop();
				sourceComboBox.getItems().add(capitalName);
				targetComboBox.getItems().add(capitalName);
			}

			swap.setOnAction(e -> {
				String temp = sourceComboBox.getValue();
				sourceComboBox.setValue(targetComboBox.getValue());
				targetComboBox.setValue(temp);
				pathTextArea.clear();
				costField.clear();
				timeField.clear();
				distanceField.clear();
				for(Line line : drawedLines) 
					rootPane.getChildren().remove(line);
			});
			
			runButton.setOnAction(e1 -> {

				for(Line line : drawedLines) 
					rootPane.getChildren().remove(line);

				String start = sourceComboBox.getValue();
				String end = targetComboBox.getValue();
				String filter = filterComboBox.getValue(); 
						
				if (start == null || end == null || filter == null) {
					pathTextArea.setText("Please select Source, Target, and Filter.");
					return;
				}

				// Get the index of the source and target city
				int v = graph.findIndex(start);
				int w = graph.findIndex(end);

				// Initialize Dijkstra for cost, time and distance
				Dijkstra dijkstra = new Dijkstra(graph, v, w, filter);
				double cost = 0, time = 0, destance = 0;
						
				

				StringBuilder pathBuilder = new StringBuilder();
				String result = "";

					if (filter.equalsIgnoreCase("Cost")) {
						if (dijkstra.hasPathTo(w)) {
							// Calculate the shortest path based on cost
							Stack<DirectedEdge> costEdges = dijkstra.pathTo(w);
							while (!costEdges.isEmpty()) {
								DirectedEdge edge = costEdges.pop();
								pathBuilder.append(graph.getContent(edge.source())).append(" -> ");
								time += edge.getTime();
								destance += edge.getDestance();
								int v1 = edge.source();
								int w1 = edge.destination();
								double startX = points[v1].getX();
								double startY = points[v1].getY();
								double endX = points[w1].getX();
								double endY = points[w1].getY();
								drawedLines.add(drawLine(startX, startY, endX, endY));
							}
							pathBuilder.append(graph.getContent(w));
							result = String.format("Shortest path by Cost: %s\nCost: %.2f £",
							pathBuilder, dijkstra.distTo(w));
							costField.setText(dijkstra.distTo(w)+"£");
							timeField.setText(time+" min");
							distanceField.setText(String.format("%.3f", destance)+" Km");
						} else if (v == w) {
							costField.setText("0£");
							timeField.setText("0 min");
							distanceField.setText("0 Km");
						} else {
							result = "No path exists for the selected cities";
							costField.setText("");
							timeField.setText("");									
							distanceField.setText("");
						}
					} else if (filter.equalsIgnoreCase("Time")) {
						if (dijkstra.hasPathTo(w)) {
							// Calculate the shortest path based on time
							Stack<DirectedEdge> timeEdges = dijkstra.pathTo(w);
							while (!timeEdges.isEmpty()) {
								DirectedEdge edge = timeEdges.pop();
								pathBuilder.append(graph.getContent(edge.source())).append(" -> ");
								cost += edge.getCost();
								destance += edge.getDestance();
								int v1 = edge.source();
								int w1 = edge.destination();
								double startX = points[v1].getX();
								double startY = points[v1].getY();
								double endX = points[w1].getX();
								double endY = points[w1].getY();
								drawedLines.add(drawLine(startX, startY, endX, endY));
							}

							pathBuilder.append(graph.getContent(w));
							result = String.format("Shortest path by Time: %s\nTime: %.2f min",
									pathBuilder, dijkstra.distTo(w));
							costField.setText(cost+"£");
							timeField.setText(dijkstra.distTo(w)+" min");
							distanceField.setText(String.format("%.3f", destance)+" Km");

						} else if (v == w) {
							costField.setText("0£");
							timeField.setText("0 min");
							distanceField.setText("0 Km");
						} else {

							result = "No path exists for the selected cities";
							costField.setText("");
							timeField.setText("");
							distanceField.setText("");

						}
					} else if (filter.equalsIgnoreCase("Distance")) {
						if (dijkstra.hasPathTo(w)) {
							// Calculate the shortest path based on distance
							Stack<DirectedEdge> distanceEdges = dijkstra.pathTo(w);
							while (!distanceEdges.isEmpty()) {
									DirectedEdge edge = distanceEdges.pop();
									pathBuilder.append(graph.getContent(edge.source())).append(" -> ");
									time += edge.getTime();
									cost += edge.getCost();
									int v1 = edge.source();
									int w1 = edge.destination();
									double startX = points[v1].getX();
									double startY = points[v1].getY();
									double endX = points[w1].getX();
									double endY = points[w1].getY();
									drawedLines.add(drawLine(startX, startY, endX, endY));
								}
							
							pathBuilder.append(graph.getContent(w));
							result = String.format("Shortest path by Distance: %s\nDistance: %.3f",
											pathBuilder, dijkstra.distTo(w));
							costField.setText(cost+"£");
							timeField.setText(time+" min");
							distanceField.setText(String.format("%.3f", dijkstra.distTo(w))+" Km");
						} else if (v == w) {
							costField.setText("0£");
							timeField.setText("0 min");
							distanceField.setText("0 Km");
						} else {
							result = "No path exists for the selected cities";
							costField.setText("");
							timeField.setText("");
							distanceField.setText("");
						}
					} else {
						result = "Invalid filter selection.";
						costField.setText("");
						timeField.setText("");
						distanceField.setText("");
					}	

				pathTextArea.setText(result);

			});

		} catch (Exception e) {
			showError("An error occurred while initializing the application." + e.getMessage());
			System.exit(0);
			return;
		}
	}

	// method to add capital on the map
	private void addCapitalMarkers() throws FileNotFoundException {
		try (Scanner in = new Scanner(new File("cities.txt"))) {

			String[] info = in.nextLine().split(" ");
			int capitalsNum = Integer.valueOf(info[0]);
			points = new Point2D[capitalsNum];
			cities = new Stack<>(capitalsNum);
			int edgesNum = Integer.valueOf(info[1]);

			graph = new DirectedGraph(capitalsNum);

			for (int i = 0; i < capitalsNum; i++) {
				String[] capitalInfo = in.nextLine().split(" ");
				String capitalName = capitalInfo[0];

				for (int j = 1; j < capitalInfo.length - 2; j++)
					capitalName += " " + capitalInfo[j];

				Point2D point = calculatePosition(Double.valueOf(capitalInfo[capitalInfo.length - 2]),
						Double.valueOf(capitalInfo[capitalInfo.length - 1]));

				Circle capital = new Circle();
				capital.setRadius(4);
				capital.setStroke(Color.RED); 
				capital.setFill(Color.RED);
				capital.setCenterX(point.getX()); 
				capital.setCenterY(point.getY()); 

				Label capitalLabel = new Label(capitalName);

				capitalLabel.setOnMouseClicked(e -> {
					if (e.getButton() == MouseButton.PRIMARY) {
						sourceComboBox.setValue(capitalLabel.getText());
					} else if (e.getButton() == MouseButton.SECONDARY) {
						targetComboBox.setValue(capitalLabel.getText());
					}

					for(Line line : drawedLines) 
						rootPane.getChildren().remove(line);
				});

				capitalLabel.setLayoutX(point.getX() + 5);
				capitalLabel.setLayoutY(point.getY() - 10);
				rootPane.getChildren().add(capitalLabel);
				rootPane.getChildren().add(capital);
				graph.setContent(i, capitalName);
				cities.push(capitalName);
				points[i] = point;
			}

			for (int i = 0; i < edgesNum; i++) {
				String[] edgeInfo = in.nextLine().split(",");
				int v = graph.findIndex(edgeInfo[0]);
				int w = graph.findIndex(edgeInfo[1]);
				Point2D fromPoint = calculateLatLon(points[v].getX(), points[v].getY());
				Point2D toPoint = calculateLatLon(points[w].getX(), points[w].getY());

				try {
					double cost = Double.parseDouble(edgeInfo[2]);
					double time = Double.parseDouble(edgeInfo[3]);
					double distance = calculateDistance(fromPoint, toPoint);

					if (cost < 0 || time < 0) {
						showError("Invalid cost or time value at line " + (i + 1));
						continue;
					}

					graph.addEdge(v, w, cost, time, distance);

				} catch (Exception e) {
					showError("Invalid cost or time value at line " + (i + 1));
				}

			}
		} catch (FileNotFoundException e) {
			showError("File not found.");
		}
	}

	// method to calculate the position of a point
	public Point2D calculatePosition(double latitude, double longitude) {
		double x = (((longitude + 180) / 360) * 1300) - 38.5;
		double y = ((1 - ((latitude + 90) / 180)) * 660) - 5;
		return new Point2D(x, y);
	}
	
	// method to calculate the latitude and longitude of a point
	public Point2D calculateLatLon(double x, double y) {
	    double longitude = (((x + 38.5) * 360) / 1300) - 180;
	    double latitude = ((1 + ((y + 5) / 660)) * 180) - 90;
	    return new Point2D(longitude, latitude);
	}
	
	// method to calculate the distance between two points
	private double calculateDistance(Point2D from, Point2D to) {
		double latSource = from.getY();
        double longSource = from.getX();
        double latDes = to.getY();
        double longDes = to.getX();

        double haversine = Math.pow(Math.sin(Math.toRadians(latDes - latSource) / 2), 2) + 
        		Math.pow(Math.sin(Math.toRadians(longDes - longSource) / 2), 2) * 
        		Math.cos(Math.toRadians(latSource)) * 
				Math.cos(Math.toRadians(latDes));
        double theta = 2 * Math.asin(Math.sqrt(haversine));
         return R * theta;
    }
	
	// method to draw a line (path) between two points
	public Line drawLine(double startX, double startY, double endX, double endY) {
		Line line = new Line();
		line.setStartX(startX);
		line.setStartY(startY);
		line.setEndX(endX);
		line.setEndY(endY);
		line.setStroke(Color.GREEN);
		rootPane.getChildren().add(line);
		
		double midX = (startX + endX) / 2;
		double midY = (startY + endY) / 2;
		
		double angle = Math.atan2(endY - startY, endX - startX);
		double arrowLength = 15;
		double arrowAngle = Math.PI / 6;
		double x1 = midX - arrowLength * Math.cos(angle - arrowAngle);
   		double y1 = midY - arrowLength * Math.sin(angle - arrowAngle);

		double x2 = midX - arrowLength * Math.cos(angle + arrowAngle);
		double y2 = midY - arrowLength * Math.sin(angle + arrowAngle);

		Line arrowLine1 = new Line();
		arrowLine1.setStartX(midX);
		arrowLine1.setStartY(midY);
		arrowLine1.setEndX(x1);
		arrowLine1.setEndY(y1);
		arrowLine1.setStroke(Color.GREEN);
		arrowLine1.setStrokeWidth(2);
		drawedLines.add(arrowLine1);

		Line arrowLine2 = new Line();
		arrowLine2.setStartX(midX);
		arrowLine2.setStartY(midY);
		arrowLine2.setEndX(x2);
		arrowLine2.setEndY(y2);
		arrowLine2.setStroke(Color.GREEN);
		arrowLine2.setStrokeWidth(2);
		drawedLines.add(arrowLine2);

		rootPane.getChildren().addAll(arrowLine1, arrowLine2);
		
		return line;
	}

	
	// method to show error
	public void showError(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error Occurred"); 
		alert.setContentText(message);
		alert.showAndWait();
	}

}