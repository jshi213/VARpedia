package application;

import java.io.File;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

	@FXML
	private Text text;
	
	@FXML
	private Button buttonCreate, buttonList, buttonSearch;

	@FXML
	private Pane rootPane;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab searchTab;
	
	@FXML
	private TextField textFieldTerm;
	
	private ExecutorService team = Executors.newSingleThreadExecutor(); 
	private static String _searchTerm;
	private WikitProcess _wikitProcess;
	private static Stage _staticStage;
	private static Alert _staticAlert;
	private static boolean _processStatus;
	
	
	@FXML
	private void initialize() {
		rootPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #757575)");
		setGlobalEventHandler(rootPane);
	}
	
	private void setGlobalEventHandler(Node root) {
	    root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.ENTER) {
	        	if(tabPane.getSelectionModel().getSelectedItem() == searchTab) {
	        		buttonSearch.fire();
	        		ev.consume(); 
	        	}
	        }
	    });
	}
	
	@FXML
	private void handleButtonList(ActionEvent event) throws IOException {
		// check if the Creations folder exists/if it isn't and if true, change to the list scene
		File dir = new File("Creations/");
		if (dir.exists() && dir.list().length > 0) {
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
		tabPane.getSelectionModel().selectNext();
		
		
	}
	
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
		tabPane.getSelectionModel().selectPrevious();
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
	
	public static String getSearchTerm() {
		return _searchTerm;
	}
	
}
