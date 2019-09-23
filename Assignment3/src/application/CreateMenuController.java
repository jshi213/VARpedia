package application;

import java.io.FileWriter;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CreateMenuController {
	
	
	@FXML
	private TextArea textAreaResults;
	
	@FXML
	private MenuItem voice1;
	
	@FXML
	private MenuItem voice2;
	
	@FXML
	private MenuItem voice3;

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
		FileWriter writer = new FileWriter("preview.scm", false);
		writer.write("(voice_akl_nz_jdt_diphone)");
		writer.close();
		String previewText = textAreaResults.getSelectedText();
		FileWriter writer2 = new FileWriter("preview.scm", true);
		writer2.write("(SayText \"" + previewText + "\")");
		writer2.close();
		
		
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "festival -b preview.scm");
		pb1.start();
	}
	
	@FXML
	private void handleVoice1(ActionEvent event) throws IOException {
		FileWriter writer = new FileWriter("preview.scm", false);
		writer.write("(voice_akl_nz_jdt_diphone)");
		writer.close();
	}
	
	@FXML
	private void handleVoice2(ActionEvent event) throws IOException {
		FileWriter writer = new FileWriter("preview.scm", false);
		writer.write("(voice_kal_diphone)");
		writer.close();
	}
	
	@FXML
	private void handleVoice3(ActionEvent event) throws IOException {
		FileWriter writer = new FileWriter("preview.scm", false);
		writer.write("(voice_akl_nz_cw_cg_cg)");
		writer.close();
	}
}
