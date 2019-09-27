package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//creating two new directories to store temporary files, audio files and creations in
			File dirTemp = new File("temporaryfiles");
			dirTemp.mkdir();
			File dirAudio = new File("audiofiles");
			dirAudio.mkdir();
			File dirCreations = new File("Creations");
			dirCreations.mkdir();
			File dirPhotos = new File("downloads");
			dirPhotos.mkdir();
			//loading scene with fxml files
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Menu.fxml"));
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
