package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateMenuScene implements Runnable {
	public void run() {
		Parent createParent;
		try {
			createParent = FXMLLoader.load(getClass().getResource("CreateMenu.fxml"));
			Scene createScene =  new Scene(createParent);
			Stage stage = SearchController.getStage();
			stage.setScene(createScene);
			stage.show();
			SearchController.getAlert().close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
}
