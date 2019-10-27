package application.runnable;

import application.controller.MenuController;

/**
 * Runnable class that is run after the WikitProcess task class is finished. 
 * Calls the setResults() method in MenuController to read initialtext contents.
 */
public class CreateMenuScene implements Runnable {
	
	public void run() {
		MenuController.getProgressIndicator().setVisible(false);
		MenuController.setResults();
	}
}
