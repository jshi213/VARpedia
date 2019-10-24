package application.runnable;

import javafx.scene.control.Alert.AlertType;
import application.helper.AlertFactory;

public class AmbiguousAlert implements Runnable {
	
	private AlertFactory _alertGenerator = new AlertFactory();
	
	public void run() {
		_alertGenerator.generateAlert(AlertType.WARNING, "Search error", null, "Search term not found, please try again");
	}
}
