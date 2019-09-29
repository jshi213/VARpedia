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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ImageSelectorController {
	
	private ExecutorService team = Executors.newSingleThreadExecutor(); 
	
	@FXML
	private Button buttonEnter, buttonCreate;
	
	@FXML
	private TextField textFieldNumber, textFieldName;
	
	@FXML 
	private Text textName;
	
	@FXML
	private Pane rootPane;
	
	private int number;
	private static Alert _staticAlert;
	
	@FXML
	private void initialize() {
		rootPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #757575)");
	}
	
	@FXML
	private void handleButtonEnter() {
		number = -1;
		String numberField = textFieldNumber.getText();
		if (!numberField.isEmpty() && numberField.trim().length() != 0){
			number = Integer.valueOf(textFieldNumber.getText());
		}
		// alert if less than 0 or greater than 10, display next question if valid number
		if (number > 10 || number < 1) {
			String error = "Please enter a number between 1 and 10";
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(error);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					textFieldNumber.setText("");
				}
			});
		} else { 
			textName.setVisible(true);
			textFieldName.setVisible(true);
			buttonCreate.setVisible(true);
			textFieldNumber.setEditable(false);
		}
	
	}
	
	@FXML
	private void handleButtonCreate(ActionEvent event) {
		//alert if invalid name
		String creation = textFieldName.getText();
		if(creation.isEmpty() || creation.trim().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Enter a name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a name for you creation");
			alert.showAndWait();
			return;
		}
		File tempFile = new File("Creations/"+creation+".mp4");
		if(tempFile.exists()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Already exists");
			alert.setHeaderText(null);
			alert.setContentText("Creation with same name already exists, please choose a new name");
			alert.showAndWait();
			return;
		}
		//loading alert while creation gets created
		Alert loadingAlert = new Alert(AlertType.INFORMATION);
		_staticAlert = loadingAlert;
		loadingAlert.setTitle("Loading...");
		loadingAlert.setHeaderText(null);
		loadingAlert.setContentText("Your creation is being generated...");
		loadingAlert.show();
		FlickrProcess flickrProcess = new FlickrProcess(number, creation);
		team.submit(flickrProcess);
		
	}
	
	@FXML
	private void handleButtonBack(ActionEvent event) throws IOException {
		// return to the previous scene where you select the audio
		Parent createParent = FXMLLoader.load(getClass().getResource("AudioSelection.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
	
	public static Alert getAlert() {
		return _staticAlert;
	}
}
