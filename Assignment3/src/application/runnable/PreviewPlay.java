package application.runnable;

import java.io.File;

import application.controller.MenuController;
import application.helper.AlertFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class PreviewPlay implements Runnable {
	
	private AlertFactory _alertGenerator = new AlertFactory();
	
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
			_alertGenerator.generateAlert(AlertType.WARNING, "Synthesis error", null, "The selected text could not be synthesized, please try selecting different words or change voices");
			MenuController.getButtonPreview().setDisable(false);
		}
	}
}