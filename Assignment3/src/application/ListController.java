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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class ListController {

	@FXML
	private ListView<String> listView;
	
	@FXML
	private ObservableList<String> list;
	
	private String selected;

	@FXML
	private void initialize() {
		String listofcreations = "";
		File dir = new File("Creations/");
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			String fileName = file.getName();
			String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
			listofcreations = listofcreations + fileWithoutExt + "\n";
		}
		String[] listofcreationsarray = listofcreations.split("\n");
		list = listView.getItems();
		list.setAll(listofcreationsarray);

	}
	
	@FXML
	private void handleButtonBack(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
	
	@FXML
	private void handleButtonPlay() throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Play.fxml"));
		Parent layout = loader.load();
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void handleButtonDelete() {
		if (selected == null || selected == "") {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Please select a creation.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
			});
		}
		else {
			String confirmation = "Are you sure you want to delete " + selected + "?";
			Alert alert = new Alert(AlertType.CONFIRMATION, confirmation);
			alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				File file = new File("Creations/" + selected + ".mp4");
				file.delete();
				initialize();
			}});
		}
	}
	
	@FXML
	private void handleItemSelection() {
		 selected = listView.getSelectionModel().getSelectedItem();
	}
}