package application.runnable;

import java.io.File;

import application.controller.MenuController;
import application.helper.AlertFactory;
import javafx.scene.control.Alert.AlertType;

/**
 * Runnable class that is run after the FlickrProcess task class is finished. 
 * Changes to the main menu tab and resets the imagecreation tab, then deletes
 * audio files used for the creation.
 */
public class MenuScene implements Runnable {
	
	private AlertFactory _alertGenerator = new AlertFactory();
	
	public void run() {
		MenuController.getImageTab().setDisable(true);
		MenuController.getCreateProgress().setVisible(false);
		MenuController.getTabPane().getSelectionModel().select(0);
		_alertGenerator.generateAlert(AlertType.INFORMATION, "Creation complete", null, "Your creation is now ready to view");
		File dir = new File("audiofiles");
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				children[i].delete();
			}
		}
	}

}
