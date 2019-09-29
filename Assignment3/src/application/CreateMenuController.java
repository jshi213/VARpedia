package application;

import java.io.File;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateMenuController {
	
	
	@FXML
	private TextArea textAreaResults;
	
	@FXML
	private MenuButton menuButtonVoices;
	
	@FXML
	private MenuButton buttonVoiceRate;
	
	@FXML
	private MenuItem voice1;
	
	@FXML
	private MenuItem voice2;
	
	@FXML
	private MenuItem voice3;
	
	@FXML
	private Button buttonSave;
	
	@FXML
	private TextField textFieldAudioName;
	
	private String _selectedText;
	private String _voiceSelection;
	private String _voiceRate = "";
	

	@FXML
	private void initialize() throws IOException {
		buttonVoiceRate.setText("1x");
		//initializing default voice
		_voiceSelection = "(voice_kal_diphone)\n";
		//initializing text area to display results in
		String line = "";
		try {
			line = new String(Files.readAllBytes(Paths.get("temporaryfiles/initialtext")));
			textAreaResults.setText(line);
			textAreaResults.setWrapText(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleButtonPreview(ActionEvent event) throws IOException {
		FileWriter writer = new FileWriter("temporaryfiles/preview.scm", false);
		writer.write(_voiceSelection);
		writer.write(_voiceRate);
		writer.close();
		_selectedText = textAreaResults.getSelectedText();
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Selection error");
			alert.setHeaderText(null);
			alert.setContentText("The selected text is too long, please select under 40 words");
			alert.showAndWait();
			return;
		}
		FileWriter writer2 = new FileWriter("temporaryfiles/preview.scm", true);
		writer2.write("(SayText \"" + _selectedText + "\")");
		writer2.close();
		
		
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "festival -b temporaryfiles/preview.scm");
		pb1.start();
	}
	
	@FXML
	private void handleVoice1(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Voice 1");
		_voiceSelection = "(voice_kal_diphone)\n";
	}
	
	@FXML
	private void handleVoice2(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Voice 2");
		_voiceSelection = "(voice_akl_nz_jdt_diphone)\n";
	}
	
	@FXML
	private void handleVoice3(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Voice 3");
		_voiceSelection = "(voice_akl_nz_cw_cg_cg)\n";
	}
	
	@FXML
	private void handleSpeedOne(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 2)\n";
		buttonVoiceRate.setText("0.5x");
	}
	
	@FXML
	private void handleSpeedTwo(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 1.333)\n";
		buttonVoiceRate.setText("0.75x");
	}
	
	@FXML
	private void handleSpeedThree(ActionEvent event) {
		_voiceRate = "";
		buttonVoiceRate.setText("1x");
	}
	
	@FXML
	private void handleSpeedFour(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 0.666)\n";
		buttonVoiceRate.setText("1.5x");
	}
	
	@FXML
	private void handleSpeedFive(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 0.5\n)";
		buttonVoiceRate.setText("2x");
	}
	
	@FXML
	private void handleButtonSave() throws IOException {
		//checking if selected text is too long
		_selectedText = textAreaResults.getSelectedText();
		if (_selectedText.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Selection error");
			alert.setHeaderText(null);
			alert.setContentText("No text has been selected");
			alert.showAndWait();
			return;
		}
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Selection error");
			alert.setHeaderText(null);
			alert.setContentText("The selected text is too long, please select under 40 words");
			alert.showAndWait();
			return;
		}
		String audiofileName = textFieldAudioName.getText();
		//checking if audiofile name is not just spaces or empty string
		if(audiofileName.isEmpty() || audiofileName.trim().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Enter a name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a name for you creation");
			alert.showAndWait();
			return;
		}
		//checking if audiofile with same name already exists
		File tempFile = new File("audiofiles/"+audiofileName+".wav");
		if(tempFile.exists()) {
			Alert alert2 = new Alert(AlertType.WARNING);
			alert2.setTitle("Already exists");
			alert2.setHeaderText(null);
			alert2.setContentText("Audio with same name already exists, please choose a new name");
			alert2.showAndWait();
			return;
		}
		FileWriter scmwriter = new FileWriter("temporaryfiles/audiofile.scm", false);
		scmwriter.write(_voiceSelection);
		scmwriter.write(_voiceRate);
		scmwriter.close();
		FileWriter writer = new FileWriter("temporaryfiles/audiotext", false);
		writer.write(_selectedText);
		writer.close();
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "text2wave temporaryfiles/audiotext -o audiofiles/" + audiofileName + ".wav -eval temporaryfiles/audiofile.scm");
		Process process = pb1.start();
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Successfully created");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Your audio file has been saved");
		infoAlert.showAndWait();
		return;
	}
	
	@FXML
	private void handleButtonBack(ActionEvent event) throws IOException {
		
		Parent createParent = FXMLLoader.load(getClass().getResource("Search.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
	
	@FXML
	private void handleButtonNext(ActionEvent event) throws IOException {
		// if no audio files have been created, prompt the user to create one
		String files = "";
		File dir = new File("audiofiles/");
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			String fileName = file.getName();
			if (!fileName.startsWith(".")) {
				files = files + fileName + " ";
			}
		}
		
		if (files.length() > 0) {
			Parent createParent = FXMLLoader.load(getClass().getResource("AudioSelection.fxml"));
			Scene createScene =  new Scene(createParent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(createScene);
			stage.show();
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please create an audio file before continuing");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
			});
		}
	}
}
