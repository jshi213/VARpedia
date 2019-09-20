package application;

import java.io.File;

import javafx.fxml.FXML;

public class ListController {

		@FXML
		private void intitialize() {
			
			String listofcreations = "";
			int i = 1;
			File dir = new File("Creations");
			File[] fileList = dir.listFiles();
			for (File files : fileList) {
				listofcreations = listofcreations + files.getName().substring(0, files.getName().length()-4) + "\n|";
				i++;
			}
			
		}
}