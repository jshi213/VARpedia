package application;

import java.io.File;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AudioSelectionController {
	
	@FXML
	private ListView<String> listViewAudioFiles, listViewSelected;
	
	@FXML
	private ObservableList<String> listAudioFiles, listSelected;

	@FXML
	private void initialize() {
		// add all the audio files created into the audio files list view
		String listofaudiofiles = "";
		File dir = new File("audiofiles/");
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			String fileName = file.getName();
			String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
			listofaudiofiles = listofaudiofiles + fileWithoutExt + "\n";
		}
		String[] listofaudiofilesarray = listofaudiofiles.split("\n");
		listAudioFiles = listViewAudioFiles.getItems();
		listAudioFiles.setAll(listofaudiofilesarray);
	}

	@FXML
	private void handleButtonAdd() {
		// get the selected item from the list of audio files and and it to the selected items (to be combined) list view
		String selected = listViewAudioFiles.getSelectionModel().getSelectedItem().toString();
		listSelected = listViewSelected.getItems();
		listSelected.add(selected);
		listAudioFiles.remove(selected);
	}
	
	@FXML
	private void handleButtonMoveBack() {
		// get the selected item from the to be combined list view and remove it from that listview
		String selected = listViewSelected.getSelectionModel().getSelectedItem().toString();
		listSelected = listViewSelected.getItems();
		listSelected.remove(selected);
		listAudioFiles.add(selected);
	}
	
	@FXML
	private void handleButtonPreview() {
		
	}
	
	@FXML 
	private void handleButtonBack(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("CreateMenu.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
	
	@FXML
	private void handleButtonCombine() throws IOException {
		//getting the list of selected audiofiles and generating strings for input into bash process
		ObservableList<String> audioToCombine = listViewSelected.getItems();
		int audioCount = audioToCombine.size();
		String commandFileInputs = "";
		for (String audioFile : audioToCombine) {
			commandFileInputs = commandFileInputs + "-i " + "audiofiles/" + audioFile + ".wav ";
		}
		String commandFilterInput = "";
		for(int i = 0; i < audioCount; i++) {
			commandFilterInput = commandFilterInput + "[" + Integer.toString(i) + ":0]";
		}
		
		//starting bash process to combine the selected files with ffmpeg
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "ffmpeg " + commandFileInputs +  "\\" + 
				"-filter_complex '" + commandFilterInput + "concat=n=" + Integer.toString(audioCount) + ":v=0:a=1[out]' \\" + 
				"-map '[out]' audiofiles/combined.wav");
		Process process = pb1.start();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Successfully combined");
		alert.setHeaderText(null);
		alert.setContentText("Selected audio files have been combined");
		alert.showAndWait();
	}
	
	@FXML
	private void handleButtonNext(ActionEvent event) throws IOException {
//		Parent createParent = FXMLLoader.load(getClass().getResource("ImageSelector.fxml"));
//		Scene createScene =  new Scene(createParent);
//		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		stage.setScene(createScene);
//		stage.show();
	}
}
