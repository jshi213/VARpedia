package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class PlayController implements Initializable {
	
	private boolean _sliderFlag = true;
	
	@FXML
	private Pane mediaViewPane;
	
	@FXML
	private Pane mainPane;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Slider progressSlider;
	
	@FXML
	private MediaView mediaView;
	private MediaPlayer mediaPlayer;
	private Media media;
	
	private static MediaPlayer _staticMediaPlayer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String creationName = ListController.getSelectedItem() + ".mp4";
		mediaViewPane.setStyle("-fx-background-color: #000000;");
		String path = new File("Creations/" + creationName).getAbsolutePath();
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		progressSlider.maxProperty().bind(Bindings.createDoubleBinding(
					() -> mediaPlayer.getTotalDuration().toSeconds(),
					mediaPlayer.totalDurationProperty()));
		mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
					Duration newValue) {
				if(_sliderFlag) {
					progressSlider.setValue(newValue.toSeconds());
				}
				String time = "";
				time += String.format("%02d", (int)newValue.toMinutes());
				time += ":";
				time += String.format("%02d", (int)newValue.toSeconds());
				timeLabel.setText(time + "/" + String.format("%02d", (int)(media.getDuration().toMinutes())) + ":" + String.format("%02d",(int)media.getDuration().toSeconds()));
			}
		});
		mediaView.setMediaPlayer(mediaPlayer);
		mediaView.setPreserveRatio(true);
		mediaPlayer.setAutoPlay(true);
		_staticMediaPlayer = mediaPlayer;
	}
	
	@FXML
	private void handleButtonMute() {
		mediaPlayer.setMute( !mediaPlayer.isMute() );
	}
	
	@FXML
	private void handleButtonPlay() {
		if (mediaPlayer.getStatus() == Status.PLAYING) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.play();
		}
	}
	
	@FXML
	private void handleSliderChanged() {
		mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
	}
	
	@FXML
	private void handleSliderDragged () {
		mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
	}
	
	@FXML
	private void handleMousePressed() {
		_sliderFlag = false;
	}
	
	@FXML
	private void handleMouseReleased() {
		_sliderFlag = true;
	}
	
	public static void stop() {
		_staticMediaPlayer.stop();
	}
}
