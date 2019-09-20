package application;

import java.io.BufferedReader;
import java.io.FileReader;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CreateMenuController {
	
	
	@FXML
	private TextArea textAreaResults;
	
	@FXML
	private void initialize() {
		String line;
		FileReader readfile;
		try {
			readfile = new FileReader("initialtext");
		BufferedReader readbuffer = new BufferedReader(readfile);
		while ((line = readbuffer.readLine()) != null) {
				line = readbuffer.readLine();
		}
		readbuffer.close();
		textAreaResults.setText(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
