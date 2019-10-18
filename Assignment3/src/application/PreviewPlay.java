package application;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PreviewPlay implements Runnable {
	public void run() {
		Media media = new Media(new File("audiofiles/temppreview.wav").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		mediaPlayer.setOnEndOfMedia(() -> {
			File tempPreview = new File("audiofiles/temppreview.wav");
			tempPreview.delete();
			MenuController.getButtonPreview().setDisable(false);
		});
	}
}
