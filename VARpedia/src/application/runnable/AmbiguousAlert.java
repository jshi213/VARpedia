package application.runnable;

import javafx.scene.control.Alert.AlertType;
import application.helper.AlertFactory;

/**
 * Runnable class that is run when WikitProcess is unable to generate results.
 * Generates an alert to the user.
 */
public class AmbiguousAlert implements Runnable {
	
	private AlertFactory _alertGenerator = new AlertFactory();
	
	public void run() {
		_alertGenerator.generateAlert(AlertType.WARNING, "Search error", null, "Search term not found, please try again");
	}
}
