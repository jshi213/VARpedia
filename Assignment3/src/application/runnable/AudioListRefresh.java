package application.runnable;

import java.io.File;

import application.controller.MenuController;
import application.helper.AlertFactory;
import javafx.scene.control.Alert.AlertType;

public class AudioListRefresh implements Runnable {
	
	private MenuController _controllerInstance;
	private String _audioFileName;
	private AlertFactory _alertGenerator = new AlertFactory();
	
	public AudioListRefresh(String audioFileName, MenuController controllerInstance) {
		_controllerInstance = controllerInstance;
		_audioFileName = audioFileName;
	}
	
	public void run() {
		File savedAudio = new File("audiofiles/"+_audioFileName+".wav");
		if(savedAudio.length() < 1024) {
			savedAudio.delete();
			_alertGenerator.generateAlert(AlertType.WARNING, "Synthesis error", null, "The selected text could not be synthesized, please try selecting a different sentence or change voices");
			return;
		}
		// add the audio file created into the audio files list view
		String listofaudiofiles = "";
		File dir = new File("audiofiles/");
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			String fileName = file.getName();
			String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
			if(!fileWithoutExt.contains("combined") && !fileWithoutExt.startsWith(".")) {
				listofaudiofiles = listofaudiofiles + fileWithoutExt + "\n";
			}
		}
		String[] listofaudiofilesarray = listofaudiofiles.split("\n");
		_controllerInstance.setAudioFiles(listofaudiofilesarray);
	}
}
