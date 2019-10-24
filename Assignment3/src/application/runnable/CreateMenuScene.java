package application.runnable;

import application.controller.MenuController;

public class CreateMenuScene implements Runnable {
	public void run() {
		MenuController.getProgressIndicator().setVisible(false);
		MenuController.setResults();
	}
}
