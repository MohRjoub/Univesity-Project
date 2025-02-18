package steg;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class stegApp extends Application {
	
	private Model makeModel() {
		return new Model(new BasicEncoder(), new BasicDecoder());
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(new View(new Controller(makeModel())));
			primaryStage.setTitle("StegApp");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		System.out.println(7 | 1);
	}
}
