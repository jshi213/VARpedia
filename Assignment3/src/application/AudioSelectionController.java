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
import javafx.scene.control.ListView;
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
	private void handleButtonCombine() {
		
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
