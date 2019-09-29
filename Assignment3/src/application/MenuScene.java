package application;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuScene implements Runnable {
	public void run() {
		Parent menuParent;
		try {
			menuParent = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene menuScene =  new Scene(menuParent);
			Stage stage = SearchController.getStage();
			stage.setScene(menuScene);
			stage.show();
			ImageSelectorController.getAlert().close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}

}
