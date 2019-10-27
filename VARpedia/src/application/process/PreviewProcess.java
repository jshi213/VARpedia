package application.process;

import application.runnable.PreviewPlay;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * The PreviewProcess class is a task class that runs on a concurrent thread to create
 * a temporary .wav file from selected results text and play it with a Runnable. 
 */
public class PreviewProcess extends Task<String> {
	
	@Override
	protected String call() {
		try {
			ProcessBuilder pb1 = new ProcessBuilder();
			pb1.command("bash", "-c", "text2wave temporaryfiles/audiotext -o "
					+ "temporaryfiles/temppreview.wav -eval temporaryfiles/preview.scm");
			pb1.start();
			PreviewPlay previewPlay = new PreviewPlay();
			Platform.runLater(previewPlay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
