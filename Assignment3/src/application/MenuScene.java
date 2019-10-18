package application;

import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MenuScene implements Runnable {
	public void run() {
		MenuController.getCreateProgress().setVisible(false);
		MenuController.getTabPane().getSelectionModel().select(0);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Creation complete");
		alert.setHeaderText(null);
		alert.setContentText("Your creation is now ready to view");
		alert.showAndWait();
		File dir = new File("audiofiles");
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				children[i].delete();
			}
		}
	}

} 
