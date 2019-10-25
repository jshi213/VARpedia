package application.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Shape;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * The PlayController class is a controller class for Play.fxml which contains handlers and
 * listeners for all video player elements.
 */
public class PlayController implements Initializable {
	
	@FXML
	private Pane mediaViewPane;
	
	@FXML
	private Pane mainPane;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Slider timeSlider, volumeSlider;
	
	@FXML
	private Button playButton;
	
	@FXML
	private Shape playSymbol, pauseSymbol1, pauseSymbol2;
	
	@FXML
	private MediaView mediaView;
	
	private MediaPlayer mediaPlayer;
	private Media media;
	private Duration _duration;
	private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private boolean _sliderFlag;
	
	private static MediaPlayer _staticMediaPlayer;
	
	/**
	 *Initializes the new stage with media elements and listeners for the controls.
	 *
	 *@param location
	 *@param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String creationName = MenuController.getSelectedItem() + ".mp4";
		System.out.println(creationName);
		mediaViewPane.setStyle("-fx-background-color: #000000;");
		String path = new File("Creations/" + creationName).getAbsolutePath();
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		mediaView.setPreserveRatio(true);
		mediaPlayer.setAutoPlay(true);
		_staticMediaPlayer = mediaPlayer;

		//various mediaplayer listeners
		mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() 
		{
			public void invalidated(Observable ov) {
				updateValues();
			}
		});

		mediaPlayer.setOnPlaying(new Runnable() {
			public void run() {
				if (stopRequested) {
					mediaPlayer.pause();
					stopRequested = false;
				} else {
					playSymbol.setVisible(false);
					pauseSymbol1.setVisible(true);
					pauseSymbol2.setVisible(true);
				}
			}
		});

		mediaPlayer.setOnPaused(new Runnable() {
			public void run() {
				playSymbol.setVisible(true);
				pauseSymbol1.setVisible(false);
				pauseSymbol2.setVisible(false);
			}
		});

		mediaPlayer.setOnReady(new Runnable() {
			public void run() {
				_duration = mediaPlayer.getMedia().getDuration();
				updateValues();
			}
		});

		mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				if (!repeat) {
					stopRequested = true;
					atEndOfMedia = true;
				}
			}
		});

		//timeslider listener
		timeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (timeSlider.isValueChanging() || _sliderFlag) {
					// multiply duration by percentage calculated by slider position
					mediaPlayer.seek(_duration.multiply(timeSlider.getValue() / 100.0));
				}
			}
		});

		//volumeslider listener
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volumeSlider.isValueChanging()) {
					mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
				}
			}
		});
	}

	
	/**
	 * Reads the current time of the mediaplayer and updates the timeslider and timelabel. Also updates
	 * the player volume if the volumeslider is changed.
	 */
	protected void updateValues() {
		if (timeLabel != null && timeSlider != null && volumeSlider != null) {
			Platform.runLater(new Runnable() {
				public void run() {
					Duration currentTime = mediaPlayer.getCurrentTime();
					timeLabel.setText(formatTime(currentTime, _duration));
					timeSlider.setDisable(_duration.isUnknown());
					if (!timeSlider.isDisabled() 
							&& _duration.greaterThan(Duration.ZERO) 
							&& !timeSlider.isValueChanging()) {
						timeSlider.setValue((currentTime.toSeconds()/_duration.toSeconds()) * 100.0);
					}
					if (!volumeSlider.isValueChanging()) {
						volumeSlider.setValue((int)Math.round(mediaPlayer.getVolume() * 100));
					}
				}
			});
		}
	}

	/**
	 * Returns the formatted time for the timelabel when given the current time and total duration.
	 * 
	 * @param elapsed   Elapsed time for the player.
	 * @param duration  Total duration of media.
	 * @return			The formatted time as a String object.
	 */
	private static String formatTime(Duration elapsed, Duration duration) {
		int intElapsed = (int)Math.floor(elapsed.toSeconds());
		int elapsedHours = intElapsed / (60 * 60);
		if (elapsedHours > 0) {
			intElapsed -= elapsedHours * 60 * 60;
		}
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 
				- elapsedMinutes * 60;

		if (duration.greaterThan(Duration.ZERO)) {
			int intDuration = (int)Math.floor(duration.toSeconds());
			int durationHours = intDuration / (60 * 60);
			if (durationHours > 0) {
				intDuration -= durationHours * 60 * 60;
			}
			int durationMinutes = intDuration / 60;
			int durationSeconds = intDuration - durationHours * 60 * 60 - 
					durationMinutes * 60;
			if (durationHours > 0) {
				return String.format("%d:%02d:%02d/%d:%02d:%02d", 
						elapsedHours, elapsedMinutes, elapsedSeconds,
						durationHours, durationMinutes, durationSeconds);
			} else {
				return String.format("%02d:%02d/%02d:%02d",
						elapsedMinutes, elapsedSeconds,durationMinutes, 
						durationSeconds);
			}
		} else {
			if (elapsedHours > 0) {
				return String.format("%d:%02d:%02d", elapsedHours, 
						elapsedMinutes, elapsedSeconds);
			} else {
				return String.format("%02d:%02d",elapsedMinutes, 
						elapsedSeconds);
			}
		}
	}
	
	/**
	 * Handler for the pause/play button of the player.
	 * 
	 * @param event
	 */
	@FXML
	public void handleButtonPlay(ActionEvent event) {
		Status status = mediaPlayer.getStatus();

		if (status == Status.UNKNOWN  || status == Status.HALTED)
		{
			// don't do anything in these states
			return;
		}

		if ( status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)
		{
			// rewind the movie if we're sitting at the end
			if (atEndOfMedia) {
				mediaPlayer.seek(mediaPlayer.getStartTime());
				atEndOfMedia = false;
			}
			mediaPlayer.play();
		} else {
			mediaPlayer.pause();
		}
	}
	
	
	/**
	 * Stops the mediaplayer.
	 */
	public static void stop() {
		_staticMediaPlayer.stop();
	}
	
	/**
	 * Handler for when the slider is clicked, and the current time of the mediaplayer needs to be updated.
	 * 
	 */
	@FXML
	public void handleMousePressed() {
		mediaPlayer.seek(_duration.multiply(timeSlider.getValue() / 100.0));
	}
}
