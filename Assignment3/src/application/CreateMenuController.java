package application;

import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CreateMenuController {
	
	
	@FXML
	private TextArea textAreaResults;

	@FXML
	private void initialize() {
		String line = "";
		try {
			line = new String(Files.readAllBytes(Paths.get("initialtext")));
			textAreaResults.setText(line);
			textAreaResults.setWrapText(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
