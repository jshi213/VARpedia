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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuizSummaryController {
	
	@FXML
	private ObservableList<String> listCorrect, listIncorrect;
	
	@FXML
	private ListView<String> listViewCorrect, listViewIncorrect;

	@FXML
	private MediaView mediaViewPlay;
	private MediaPlayer mediaPlayer;
	private Media media;
	
	@FXML
	private Text textScore;
	
	@FXML
	private void initialize() {

		textScore.setText(QuizController.score + "/" + QuizController.levels);
		
		listCorrect = listViewCorrect.getItems();
		listCorrect.setAll(QuizController.correct);
		
		listIncorrect = listViewIncorrect.getItems();
		listIncorrect.setAll(QuizController.incorrect);
	}
	
	@FXML
	private void handleListViewCorrect() {
		String selected = listViewCorrect.getSelectionModel().getSelectedItem();
		if (selected != null) {
			String path = new File("Quiz/" + selected + ".mp4").getAbsolutePath();
			media = new Media(new File(path).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaViewPlay.setMediaPlayer(mediaPlayer);
			mediaPlayer.setAutoPlay(true);
		}
	}
	
	@FXML
	private void handleListViewIncorrect() {
		String selected = listViewIncorrect.getSelectionModel().getSelectedItem();
		if (selected != null) {
			String path = new File("Quiz/" + selected + ".mp4").getAbsolutePath();
			media = new Media(new File(path).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaViewPlay.setMediaPlayer(mediaPlayer);
			mediaPlayer.setAutoPlay(true);
		}
	}
	
	@FXML
	private void handleButtonRetry(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("Quiz.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
	
	@FXML
	private void handleButtonMenu(ActionEvent event) throws IOException {
		Parent createParent = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		Scene createScene =  new Scene(createParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(createScene);
		stage.show();
	}
}
