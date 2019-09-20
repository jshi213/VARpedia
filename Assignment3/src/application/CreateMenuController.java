package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

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
	
	@FXML
	private void handleButtonPreview(ActionEvent event) throws IOException {
		String previewText = textAreaResults.getSelectedText();
		System.out.println(previewText);
		PrintWriter writer = new PrintWriter("previewtext", "UTF-8");
		writer.println(previewText);
		writer.close();
		
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "text2wave previewtext -o temporary");
		Process process2 = pb1.start();
	}
}
