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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuizController {
	
	private String[] list;
	
	private String term;
	public static int levels, currentLevel, score;
	
	@FXML
	private MediaView mediaViewQuiz;
	private MediaPlayer mediaPlayer;
	private Media media;
	
	@FXML
	private TextField textFieldAnswer;
	
	@FXML
	private Text textScore, textCorrect, textIncorrect;
	
	@FXML
	private Button buttonEnter, buttonNext;

	public static ArrayList<String> correct, incorrect;
	
	boolean incorrectAttempt = false;
	

	@FXML
	private void initialize() {
		score = 0;
		File dir = new File("Quiz/");
		list = dir.list();
		currentLevel = 0;
		levels = list.length;
		textScore.setText("0/" + levels);
		int i = 0;
		while (i < list.length) {
			list[i] = list[i].substring(0, list[i].length()-4);
			i++;
		}
			String path = new File("Quiz/" + list[currentLevel] + ".mp4").getAbsolutePath();
			media = new Media(new File(path).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaViewQuiz.setMediaPlayer(mediaPlayer);
			mediaPlayer.setAutoPlay(true);
			
		
			String[] termNames = list[currentLevel].split("-");
			term = termNames[1];
//		}
			textScore.setText("0/" + levels);
			
			correct = new ArrayList<>();
			incorrect = new ArrayList<>();
			
	}
	
	@FXML
	private void handleTextFieldAnswer() {
//		if (textFieldAnswer.getText().equals(term)) {
//			System.out.println("correct");
//			score++;
//			textScore.setText(score + "/" + levels);
//		}
//		else {
//			System.out.println("incorrect");
//		}
//		
	}
	
	@FXML
	private void handleButtonEnter() {
		buttonNext.setDisable(false);
		if (textFieldAnswer.getText().equals(term)) {
			textIncorrect.setVisible(false);
			textCorrect.setVisible(true);
			buttonEnter.setDisable(true);
			
			if (!incorrectAttempt) {
				correct.add(list[currentLevel]);
				score++;
			}
		}
		else {
			incorrectAttempt = true;
			textCorrect.setVisible(false);
			textIncorrect.setVisible(true);
			incorrect.add(list[currentLevel]);
		}
		textScore.setText(score + "/" + levels);
		

		
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
	private void handleButtonNext(ActionEvent event) throws IOException {		
		incorrectAttempt = false;
		buttonNext.setDisable(true);
		if (buttonNext.getText().equals("Finish")) {
			Parent createParent = FXMLLoader.load(getClass().getResource("QuizSummary.fxml"));
			Scene createScene =  new Scene(createParent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(createScene);
			stage.show();
		}
		else {
			currentLevel++;
			if (currentLevel == levels - 1) {
				buttonNext.setText("Finish");
			}
			textCorrect.setVisible(false);
			textIncorrect.setVisible(false);
			textFieldAnswer.clear();
			buttonEnter.setDisable(false);
			String path = new File("Quiz/" + list[currentLevel] + ".mp4").getAbsolutePath();
			media = new Media(new File(path).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaViewQuiz.setMediaPlayer(mediaPlayer);
			mediaPlayer.setAutoPlay(true);


			String[] termNames = list[currentLevel].split("-");
			term = termNames[1];
		}
		
	}
}
