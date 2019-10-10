package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

	@FXML
	private Text text;
	
	@FXML
	private Button buttonCreate, buttonList, buttonSearch, buttonSave;

	@FXML
	private Pane rootPane;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab searchTab, audioSelectionTab, audioCombinationTab, imageTab, listTab;
	
	@FXML
	private TextField textFieldTerm, textFieldTerm1, textFieldAudioName;
	
	@FXML
	private TextArea textAreaResults;
	
	@FXML
	private MenuButton menuButtonVoices;
	
	@FXML
	private MenuButton buttonVoiceRate;
	
	@FXML
	private MenuItem voice1;
	
	@FXML
	private MenuItem voice2;
	
	@FXML
	private MenuItem voice3;
	
	@FXML
	private ListView<String> audioList;
	
	@FXML
	private ProgressIndicator progressIndicator;
	
	@FXML
	private ListView<String> listViewAudioFiles, listViewSelected;
	
	@FXML
	private ObservableList<String> listAudioFiles, listSelected;
	
	@FXML
	private Button buttonEnter, buttonImageCreate;
	
	@FXML
	private TextField textFieldNumber, textFieldName;
	
	@FXML 
	private Text textName;
	
	@FXML
	private ListView<String> listView;
	
	@FXML
	private ObservableList<String> list;
	
	private static String _selected;
	
	private int number;
	private static Alert staticImageAlert;
	
	private ExecutorService team = Executors.newSingleThreadExecutor(); 
	private static String _searchTerm;
	private WikitProcess _wikitProcess;
	private static Alert _staticAlert;
	private static boolean _processStatus;
	private String _selectedText;
	private String _voiceSelection;
	private String _voiceRate = "";
	private ObservableList<String> audioFiles;
	private static TextArea staticTextAreaResults;
	private static ProgressIndicator _staticProgressIndicator;
	private MediaPlayer mediaPlayer;
	
	
	@FXML
	private void initialize() {
		rootPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #757575)");
		
		setGlobalEventHandler(rootPane);
		staticTextAreaResults = textAreaResults;
		_staticProgressIndicator = progressIndicator;
		
		buttonVoiceRate.setText("1x");
		//initializing default voice
		_voiceSelection = "(voice_kal_diphone)\n";
		//initializing text area to display results in
		try {
			textAreaResults.setWrapText(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		audioCombinationTab.setOnSelectionChanged (e -> {
			// add all the audio files created into the audio files list view
			File dir = new File("audiofiles/");
			File[] fileList = dir.listFiles();
			String listofaudiofiles = "";
			for (File file : fileList) {
				String fileName = file.getName();
				String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
				if(!fileWithoutExt.contains("combined") && !fileWithoutExt.startsWith(".")) {
					listofaudiofiles = listofaudiofiles + fileWithoutExt + "\n";
				}
			}
			String[] listofaudiofilesarray = listofaudiofiles.split("\n");
			listAudioFiles = listViewAudioFiles.getItems();
			listAudioFiles.setAll(listofaudiofilesarray);
			audioCombinationTab.setDisable(false); 
		});
		
		listTab.setOnSelectionChanged(e -> {
			String listofcreations = "";
			File dir = new File("Creations/");
			File[] fileList = dir.listFiles();
			for (File file : fileList) {
				String fileName = file.getName();
				String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
				if (!fileWithoutExt.startsWith(".")) {
				listofcreations = listofcreations + fileWithoutExt + "\n";
				}
			}	
			String[] listofcreationsarray = listofcreations.split("\n");
			list = listView.getItems();
			list.setAll(listofcreationsarray);
		});
		
		audioSelectionTab.setOnSelectionChanged(e -> {
			// add all the audio files created into the audio files list view
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
			audioFiles = audioList.getItems();
			audioFiles.setAll(listofaudiofilesarray);
		});
	}
	
	private void setGlobalEventHandler(Node root) {
	    root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.ENTER) {
	        	if(tabPane.getSelectionModel().getSelectedItem() == searchTab || tabPane.getSelectionModel().getSelectedItem() == audioSelectionTab) {
	        		buttonSearch.fire();
	        		ev.consume(); 
	        	}
	        }
	    });
	}
	
	@FXML
	private void handleButtonList(ActionEvent event) throws IOException {
		// check if the Creations folder exists/if it isn't and if true, change to the list scene
		File dir = new File("Creations/");
		if (dir.exists() && dir.list().length > 0) {
			tabPane.getSelectionModel().select(5);
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("There are no creations to view");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
			});
		}
	}
	
	@FXML
	private void handleButtonCreate(ActionEvent event) throws IOException {
		File dir = new File("audiofiles");
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				children[i].delete();
			}
		}
		tabPane.getSelectionModel().select(1);
		
		
	}
	
	//search tab
	@FXML
	private void handleButtonSearch(ActionEvent event) throws IOException {
		textAreaResults.clear();
		if(tabPane.getSelectionModel().getSelectedItem() == searchTab) {
			_searchTerm = textFieldTerm.getText();
		} else {
			_searchTerm = textFieldTerm1.getText();
		}
		textFieldTerm1.setText(_searchTerm);
		//checking for empty search field
		if(_searchTerm.isEmpty() || _searchTerm.trim().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No search term entered");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a search term");
			alert.showAndWait();
			return;
		}
		//starting new thread for wikit search by using WikitProcess class
		textFieldTerm.clear();
		progressIndicator.setVisible(true);
		WikitProcess wikitProcess = new WikitProcess(_searchTerm);
		_wikitProcess = wikitProcess;
		_processStatus = true;
		team.submit(wikitProcess);
		tabPane.getSelectionModel().select(2);
	}
	
	@FXML
	private void handleButtonSearchBack(ActionEvent event) throws IOException {
		if(_processStatus) {
			_wikitProcess.setCancel();
		}
		tabPane.getSelectionModel().selectPrevious();
	}
	
	public static ProgressIndicator getProgressIndicator() {
		return _staticProgressIndicator;
	}
	
	public static Alert getAlert() {
		return _staticAlert;
	}

	public static void setProcessStatus() {
		_processStatus = false;
		
	}
	
	public static String getSearchTerm() {
		return _searchTerm;
	}
	
	
	//audio selection tab
	public static void setResults() {
		//initializing text area to display results in
				String line = "";
				try {
					line = new String(Files.readAllBytes(Paths.get("temporaryfiles/initialtext")));
					staticTextAreaResults.setText(line);
					staticTextAreaResults.setWrapText(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	@FXML
	private void handleButtonPreview(ActionEvent event) throws IOException {
		FileWriter writer = new FileWriter("temporaryfiles/preview.scm", false);
		writer.write(_voiceSelection);
		writer.write(_voiceRate);
		writer.close();
		_selectedText = textAreaResults.getSelectedText();
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Selection error");
			alert.setHeaderText(null);
			alert.setContentText("The selected text is too long, please select under 40 words");
			alert.showAndWait();
			return;
		}
		FileWriter writer2 = new FileWriter("temporaryfiles/preview.scm", true);
		writer2.write("(SayText \"" + _selectedText + "\")");
		writer2.close();
		
		
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "festival -b temporaryfiles/preview.scm");
		pb1.start();
	}
	
	@FXML
	private void handleVoice1(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Voice 1");
		_voiceSelection = "(voice_kal_diphone)\n";
	}
	
	@FXML
	private void handleVoice2(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Voice 2");
		_voiceSelection = "(voice_akl_nz_jdt_diphone)\n";
	}
	
	@FXML
	private void handleVoice3(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Voice 3");
		_voiceSelection = "(voice_akl_nz_cw_cg_cg)\n";
	}
	
	@FXML
	private void handleSpeedOne(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 2)\n";
		buttonVoiceRate.setText("0.5x");
	}
	
	@FXML
	private void handleSpeedTwo(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 1.333)\n";
		buttonVoiceRate.setText("0.75x");
	}
	
	@FXML
	private void handleSpeedThree(ActionEvent event) {
		_voiceRate = "";
		buttonVoiceRate.setText("1x");
	}
	
	@FXML
	private void handleSpeedFour(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 0.666)\n";
		buttonVoiceRate.setText("1.5x");
	}
	
	@FXML
	private void handleSpeedFive(ActionEvent event) {
		_voiceRate = "(Parameter.set 'Duration_Stretch 0.5\n)";
		buttonVoiceRate.setText("2x");
	}
	
	@FXML
	private void handleButtonSave() throws IOException {
		//checking if selected text is too long
		_selectedText = textAreaResults.getSelectedText();
		if (_selectedText.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Selection error");
			alert.setHeaderText(null);
			alert.setContentText("No text has been selected");
			alert.showAndWait();
			return;
		}
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Selection error");
			alert.setHeaderText(null);
			alert.setContentText("The selected text is too long, please select under 40 words");
			alert.showAndWait();
			return;
		}
		String audiofileName = textFieldAudioName.getText();
		//checking if audiofile name is not just spaces or empty string
		if(audiofileName.isEmpty() || audiofileName.trim().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Enter a name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a name for you creation");
			alert.showAndWait();
			return;
		}
		//checking if audiofile with same name already exists
		File tempFile = new File("audiofiles/"+audiofileName+".wav");
		if(tempFile.exists()) {
			Alert alert2 = new Alert(AlertType.WARNING);
			alert2.setTitle("Already exists");
			alert2.setHeaderText(null);
			alert2.setContentText("Audio with same name already exists, please choose a new name");
			alert2.showAndWait();
			return;
		}
		FileWriter scmwriter = new FileWriter("temporaryfiles/audiofile.scm", false);
		scmwriter.write(_voiceSelection);
		scmwriter.write(_voiceRate);
		scmwriter.close();
		FileWriter writer = new FileWriter("temporaryfiles/audiotext", false);
		writer.write(_selectedText);
		writer.close();
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "text2wave temporaryfiles/audiotext -o audiofiles/" + audiofileName + ".wav -eval temporaryfiles/audiofile.scm");
		pb1.start();
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Successfully created");
		infoAlert.setHeaderText(null);
		infoAlert.setContentText("Your audio file has been saved");
		infoAlert.showAndWait();
		// add all the audio files created into the audio files list view
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
		audioFiles = audioList.getItems();
		audioFiles.setAll(listofaudiofilesarray);
		return;
	}
	
	@FXML
	private void handleButtonCreateMenuBack(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().selectPrevious();
	}
	
	@FXML
	private void handleButtonNext(ActionEvent event) throws IOException {
		// if no audio files have been created, prompt the user to create one
		String files = "";
		File dir = new File("audiofiles/");
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			String fileName = file.getName();
			if (!fileName.startsWith(".")) {
				files = files + fileName + " ";
			}
		}
		
		if (files.length() > 0) {
			// add all the audio files created into the audio files list view
			String listofaudiofiles = "";
			for (File file : fileList) {
				String fileName = file.getName();
				String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
				if(!fileWithoutExt.contains("combined") && !fileWithoutExt.startsWith(".")) {
					listofaudiofiles = listofaudiofiles + fileWithoutExt + "\n";
				}
			}
			String[] listofaudiofilesarray = listofaudiofiles.split("\n");
			listAudioFiles = listViewAudioFiles.getItems();
			listAudioFiles.setAll(listofaudiofilesarray);
			tabPane.getSelectionModel().clearAndSelect(3);
			audioCombinationTab.setDisable(false); 
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please create an audio file before continuing");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
			});
		}
	}
	
	//audiocombination tab
	@FXML
	private void handleButtonAdd() {
		// get the selected item from the list of audio files and and it to the selected items (to be combined) list view
		if (listViewAudioFiles.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No audio files selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select an audio file to move across");
			alert.showAndWait();
		} else {
			String selected = listViewAudioFiles.getSelectionModel().getSelectedItem().toString();
			listSelected = listViewSelected.getItems();
			listSelected.add(selected);
			listAudioFiles.remove(selected);
		}
	}
	
	@FXML
	private void handleButtonMoveBack() {
		// get the selected item from the to be combined list view and remove it from that listview
		if (listViewSelected.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No audio files selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select an audio file to move back across");
			alert.showAndWait();
		} else {
			String selected = listViewSelected.getSelectionModel().getSelectedItem().toString();
			listSelected = listViewSelected.getItems();
			listSelected.remove(selected);
			listAudioFiles.add(selected);
		}
	}
	
	@FXML
	private void handleButtonPlay() {
		String audioToPlay = listViewAudioFiles.getSelectionModel().getSelectedItem();
		if(audioToPlay == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No audio files selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select an audio file to play");
			alert.showAndWait();
		} else {
			Media media = new Media(new File("audiofiles/" + audioToPlay + ".wav").toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
		}
	}
	
	@FXML 
	private void handleButtonCombineBack(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().selectPrevious();
	}
	
	private void combine() throws IOException {
		//getting the list of selected audiofiles and generating strings for input into bash process
		ObservableList<String> audioToCombine = listViewSelected.getItems();
		int audioCount = audioToCombine.size();
		String commandFileInputs = "";
		for (String audioFile : audioToCombine) {
			commandFileInputs = commandFileInputs + "-i " + "audiofiles/" + audioFile + ".wav ";
		}
		String commandFilterInput = "";
		for(int i = 0; i < audioCount; i++) {
			commandFilterInput = commandFilterInput + "[" + Integer.toString(i) + ":0]";
		}
		
		//starting bash process to combine the selected files with ffmpeg
		ProcessBuilder pb1 = new ProcessBuilder();
		pb1.command("bash", "-c", "ffmpeg " + commandFileInputs +  "\\" + 
				"-filter_complex '" + commandFilterInput + "concat=n=" + Integer.toString(audioCount) + ":v=0:a=1[out]' \\" + 
				"-map '[out]' audiofiles/combined.wav -y");
		pb1.start();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Successfully combined");
		alert.setHeaderText(null);
		alert.setContentText("Selected audio files have been combined");
		alert.showAndWait();
	}
	
	@FXML
	private void handleButtonCombineNext(ActionEvent event) throws IOException {
		if (listViewSelected.getItems().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No audio files selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select at least one audio file to be in the video");
			alert.showAndWait();
		}
		else {
			combine();
			imageTab.setDisable(false);
			tabPane.getSelectionModel().select(4);
		}
	}
	
	//image selection tab
	@FXML
	private void handleButtonEnter() {
		number = -1;
		String numberField = textFieldNumber.getText();
		if (!numberField.isEmpty() && numberField.trim().length() != 0){
			number = Integer.valueOf(textFieldNumber.getText());
		}
		// alert if less than 0 or greater than 10, display next question if valid number
		if (number > 10 || number < 1) {
			String error = "Please enter a number between 1 and 10";
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(error);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					textFieldNumber.setText("");
				}
			});
		} else { 
			textName.setVisible(true);
			textFieldName.setVisible(true);
			buttonImageCreate.setVisible(true);
			textFieldNumber.setEditable(false);
		}
	
	}
	
	@FXML
	private void handleButtonImageCreate(ActionEvent event) {
		//alert if invalid name
		String creation = textFieldName.getText();
		if(creation.isEmpty() || creation.trim().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Enter a name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a name for you creation");
			alert.showAndWait();
			return;
		}
		File tempFile = new File("Creations/"+creation+".mp4");
		if(tempFile.exists()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Already exists");
			alert.setHeaderText(null);
			alert.setContentText("Creation with same name already exists, please choose a new name");
			alert.showAndWait();
			return;
		}
		//loading alert while creation gets created
		Alert loadingAlert = new Alert(AlertType.INFORMATION);
		staticImageAlert = loadingAlert;
		loadingAlert.setTitle("Loading...");
		loadingAlert.setHeaderText(null);
		loadingAlert.setContentText("Your creation is being generated...");
		loadingAlert.show();
		FlickrProcess flickrProcess = new FlickrProcess(number, creation);
		team.submit(flickrProcess);
		tabPane.getSelectionModel().select(0);
	}
	
	@FXML
	private void handleButtonBack(ActionEvent event) throws IOException {
		// return to the previous scene where you select the audio
		tabPane.getSelectionModel().selectPrevious();
	}
	
	public static Alert getImageAlert() {
		return staticImageAlert;
	}
	
	//list tab
	@FXML
	private void handleButtonListBack(ActionEvent event) throws IOException {
		_selected = null;
		tabPane.getSelectionModel().select(0);
	}
	
	@FXML
	private void handleButtonListPlay() throws IOException {
		if (_selected == null || _selected == "") {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please select a creation.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			return;
		}
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Play.fxml"));
		stage.setOnCloseRequest(event -> {
			stage.close();
		    PlayController.stop();
		});
		Parent layout = loader.load();
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void handleButtonDelete() {
		if (_selected == null || _selected == "") {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please select a creation.");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			return;
		}
		else {
			String confirmation = "Are you sure you want to delete " + _selected + "?";
			Alert alert = new Alert(AlertType.CONFIRMATION, confirmation);
			alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				File fileDelete = new File("Creations/" + _selected + ".mp4");
				fileDelete.delete();
				_selected = null;
				String listofcreations = "";
				File dir = new File("Creations/");
				File[] fileList = dir.listFiles();
				for (File file : fileList) {
					String fileName = file.getName();
					String fileWithoutExt =  fileName.substring(0, file.getName().length()-4);
					if (!fileWithoutExt.startsWith(".")) {
					listofcreations = listofcreations + fileWithoutExt + "\n";
					}
				}	
				String[] listofcreationsarray = listofcreations.split("\n");
				list = listView.getItems();
				list.setAll(listofcreationsarray);
			}});
		}
	}
	
	@FXML
	private void handleItemSelection() {
		 _selected = listView.getSelectionModel().getSelectedItem();
		 System.out.println(_selected);
	}
	
	public static String getSelectedItem() {
		return _selected;
	}
	
	
}
