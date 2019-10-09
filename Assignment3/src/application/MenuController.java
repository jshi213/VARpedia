package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

	@FXML
	private Text text;
	
	@FXML
	private Button buttonCreate, buttonList;

	@FXML
	private Pane rootPane;

	private String[] list;
	
	
	@FXML
	private void initialize() {
		rootPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #757575)");
	}
	
	@FXML
	private void handleButtonList(ActionEvent event) throws IOException {
		// check if the Creations folder exists/if it isn't and if true, change to the list scene
		File dir = new File("Creations/");
		int length = 0;
		list = dir.list();
		for (String fileName : list) {
			File file = new File("Creations/"+fileName);
			if (!file.isHidden()){
				length++;
			}
		}
		
		if (dir.exists() && length > 0) {
			Parent createParent = FXMLLoader.load(getClass().getResource("List.fxml"));
			Scene createScene =  new Scene(createParent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(createScene);
			stage.show();	
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("There are no creations to view");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
			});
		}
	}
	
	@FXML
	private void handleButtonCreate(ActionEvent event) throws IOException {
		File dir = new File("audiofiles");
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				children[i].delete();
			}
		}
		Parent createParent = FXMLLoader.load(getClass().getResource("Search.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
		
		
	}
	
	@FXML
	private void handleButtonQuiz(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("Quiz.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
		
	}
	
}
