package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AmbiguousAlert implements Runnable {
	public void run() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Search error");
		alert.setHeaderText(null);
		alert.setContentText("Search term not found, please try again");
		alert.showAndWait();
		SearchController.getAlert().close();
	}
}
