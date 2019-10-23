package application;

import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class PreviewPlay implements Runnable {
	
	public void run() {
		try {
		Media media = new Media(new File("temporaryfiles/temppreview.wav").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		mediaPlayer.setOnEndOfMedia(() -> {
			File tempPreview = new File("temporaryfiles/temppreview.wav");
			tempPreview.delete();
			MenuController.getButtonPreview().setDisable(false);
		});
		} catch(MediaException e) {
			File tempPreview = new File("temporaryfiles/temppreview.wav");
			tempPreview.delete();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Synthesis error");
			alert.setHeaderText(null);
			alert.setContentText("The selected text could not be synthesised, please try selecting a full sentence.");
			alert.showAndWait();
			MenuController.getButtonPreview().setDisable(false);
		}
	}
}
