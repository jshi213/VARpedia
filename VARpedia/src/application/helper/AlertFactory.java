package application.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

/**
 * The Alert factory class contains a method to generate alerts that must be acknowledged by the user.
 */
public class AlertFactory {
	
	/**
	 * This method generates a pop-up alert with the given properties.
	 * 
	 * @param alertType 
	 * @param title
	 * @param header
	 * @param content
	 */
	public void generateAlert(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.setResizable(true);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		
	}
}
