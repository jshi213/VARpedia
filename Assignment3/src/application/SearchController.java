package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SearchController {

	@FXML
	private Text text;
	
	@FXML
	private Button buttonCreate, buttonList;

	@FXML
	private Pane rootPane;
	
	@FXML
	private void handleButtonSearch(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("CreateMenu.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();	
	}
	
	@FXML
	private void handleButtonSearchBack(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
		
		
	}
	
}

