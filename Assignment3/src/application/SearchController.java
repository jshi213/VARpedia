package application;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SearchController {
	
	private ExecutorService team = Executors.newSingleThreadExecutor(); 
	
	public static String _searchTerm;
	private WikitProcess _wikitProcess;
	private static Stage _staticStage;
	private static Alert _staticAlert;
	private static boolean _processStatus;
	
	@FXML
	private Text text;
	
	@FXML
	private Button buttonCreate, buttonList;

	@FXML
	private Pane rootPane;
	
	@FXML
	private TextField textFieldTerm;
	
	@FXML
	private void handleButtonSearch(ActionEvent event) throws IOException {
		_searchTerm = textFieldTerm.getText();
		//checking for empty search field
		if(_searchTerm.isEmpty() || _searchTerm.trim().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No search term entered");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a search term");
			alert.showAndWait();
			return;
		}
		//starting new thread for wikit search by using WikitProcess class
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Loading...");
		alert.setHeaderText(null);
		alert.setContentText("Your search is loading...");
		alert.show();
		_staticAlert = alert;
		WikitProcess wikitProcess = new WikitProcess(_searchTerm);
		_wikitProcess = wikitProcess;
		_processStatus = true;
		team.submit(wikitProcess);
		//loading new scene to display results
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		_staticStage = stage;
	}
	
	@FXML
	private void handleButtonSearchBack(ActionEvent event) throws IOException {
		if(_processStatus) {
			_wikitProcess.setCancel();
		}
		Parent createParent = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
	
	public static Stage getStage() {
		return _staticStage;
	}
	
	public static Alert getAlert() {
		return _staticAlert;
	}

	public static void setProcessStatus() {
		_processStatus = false;
		
	}
	
}

