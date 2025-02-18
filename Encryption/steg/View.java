package steg;


import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class View extends Pane{
	
	private Controller controller;

	public View(Controller controller) {
		this.controller = controller;
		
		setPrefSize(500*2, 550);
		
		Image image = new Image("file:/C:/Users/ACTC/eclipse-workspace/encryptionAss1/src/steg/me.JPG");
		ImageView originalView = new ImageView(image);
		originalView.setFitWidth(500);
		originalView.setFitHeight(500);
		
		ImageView modifiedView = new ImageView();
		modifiedView.setFitWidth(500);
		modifiedView.setFitHeight(500);
        modifiedView.setTranslateX(500);

		TextField field = new TextField("Enter Message");
		field.setTranslateY(500);
		field.setOnAction(e -> controller.onEncode());
		
		Button btnDecode = new Button("Decode");
		btnDecode.setTranslateX(500);
		btnDecode.setTranslateY(500);
		btnDecode.setOnAction(e -> controller.onDecode());
		
		controller.injectUI(originalView, modifiedView, field);
		
		getChildren().addAll(originalView, modifiedView, field, btnDecode);
	}
	
}
