package application;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class SaveAudioProcess extends Task<String> {
	
	private String _audioFileName;
	private MenuController _menuControllerInstance;
	
	public SaveAudioProcess(String audioFileName, MenuController menuControllerInstance) {
		_audioFileName = audioFileName;
		_menuControllerInstance = menuControllerInstance;
	}
	
	@Override
	protected String call() {
		try {
			ProcessBuilder pb1 = new ProcessBuilder();
			pb1.command("bash", "-c", "text2wave temporaryfiles/audiotext -o audiofiles/" + _audioFileName + ".wav -eval temporaryfiles/audiofile.scm");
			pb1.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void done() {
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Runnable listRefresher = new AudioListRefresh(_audioFileName, _menuControllerInstance);
		Platform.runLater(listRefresher);
	}
}