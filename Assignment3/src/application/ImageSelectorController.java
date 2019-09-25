package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class ImageSelectorController {
	
	@FXML
	private Button buttonEnter, buttonCreate;
	
	@FXML
	private TextField textFieldNumber, textFieldName;
	
	@FXML 
	private Text textName;
	
	@FXML
	private void handleButtonEnter() {
		int number = Integer.valueOf(textFieldNumber.getText());
		// alert if less than 0 or greater than 10, display next question if valid number
		if (number > 10 || number < 0) {
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
	private void handleButtonCreate() {
		//alert if invalid name
		
		//create process
	}
}
