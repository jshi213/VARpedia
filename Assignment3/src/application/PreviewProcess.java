package application;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class PreviewProcess extends Task<String> {
	
	
	@Override
	protected String call() {
		try {
			ProcessBuilder pb1 = new ProcessBuilder();
			pb1.command("bash", "-c", "text2wave temporaryfiles/audiotext -o audiofiles/temppreview.wav -eval temporaryfiles/preview.scm");
			pb1.start();
			PreviewPlay previewPlay = new PreviewPlay();
			Platform.runLater(previewPlay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
