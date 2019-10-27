package application.runnable;

import java.io.File;

import application.controller.MenuController;
import application.helper.AlertFactory;
import application.helper.ListRefresh;
import javafx.scene.control.Alert.AlertType;

/**
 * Runnable class that is run after the SaveAudioProcess task class is finished. It refreshes the listview
 * for saved audio files in the audio creations tab.
 */
public class AudioListRefresh implements Runnable {
	
	private MenuController _controllerInstance;
	private String _audioFileName;
	private AlertFactory _alertGenerator = new AlertFactory();
	private ListRefresh _listRefresher = new ListRefresh();
	
	/**
	 * Initializes the Runnable.
	 * 
	 * @param audioFileName			The name of the saved audio file
	 * @param controllerInstance	The instance of menucontroller used to access the listview
	 */
	public AudioListRefresh(String audioFileName, MenuController controllerInstance) {
		_controllerInstance = controllerInstance;
		_audioFileName = audioFileName;
	}
	
	public void run() {
		File savedAudio = new File("audiofiles/"+_audioFileName+".wav");
		if(savedAudio.length() < 1024) {
			savedAudio.delete();
			_alertGenerator.generateAlert(AlertType.WARNING, "Synthesis error", null, "The selected "
					+ "text could not be synthesized, please try selecting different words or change voices");
			return;
		}
		// add the audio file created into the audio files list view
		_controllerInstance.setAudioFiles(_listRefresher.refreshAudioFilesLists());
	}
}
