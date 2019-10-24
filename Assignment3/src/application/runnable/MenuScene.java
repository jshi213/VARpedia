package application.runnable;

import java.io.File;

import application.controller.MenuController;
import application.helper.AlertFactory;
import javafx.scene.control.Alert.AlertType;

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
