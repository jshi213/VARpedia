package application;

import java.io.File;

import application.controller.MenuController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AudioListRefresh implements Runnable {
	
	private MenuController _controllerInstance;
	private String _audioFileName;
	
	public AudioListRefresh(String audioFileName, MenuController controllerInstance) {
		_controllerInstance = controllerInstance;
		_audioFileName = audioFileName;
	}
	
	public void run() {
		File savedAudio = new File("audiofiles/"+_audioFileName+".wav");
		if(savedAudio.length() < 1024) {
			savedAudio.delete();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Synthesis error");
			alert.setHeaderText(null);
			alert.setContentText("The selected text could not be synthesized, please try selecting a full sentence.");
			alert.showAndWait();
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
