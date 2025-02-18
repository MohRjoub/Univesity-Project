
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {
    private static Stage stage = new Stage();
    @Override
    public void start(Stage stage) throws IOException {
        new UIGenerator();
        stage = Driver.stage;
        Driver.stage.setTitle("Decision Tree");
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Driver.stage = stage;
    }

    // public static void main(String[] args) {
    //     launch();
    // }
}