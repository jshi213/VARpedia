package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private static Scene _staticScene;
	
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
			File dirQuiz = new File("Quiz");
			dirQuiz.mkdir();
			//loading scene with fxml files
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/Menu.fxml"));
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			scene.getStylesheets().add(getClass().getResource("/resources/application.css").toExternalForm());
			_staticScene = scene;
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(event -> {
				primaryStage.close();
			    deleteFolder(dirTemp);
			    deleteFolder(dirAudio);
			    deleteFolder(dirPhotos);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	public static Scene getScene() {
		return _staticScene;
	}
	
	
}
