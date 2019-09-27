package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class PlayController implements Initializable {
	
	@FXML
	private MediaView mediaView;
	private MediaPlayer mediaPlayer;
	private Media media;
	File fileUrl = new File("big_buck_bunny_1_minute.mp4"); 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String path = new File("Creations/big_buck_bunny_1_minute.mp4").getAbsolutePath();
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		mediaPlayer.setAutoPlay(true);

		

	}
}
