package application.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Main;
import application.helper.*;
import application.process.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

	@FXML
	private Text text, textName, textMusic, textScore, textCorrect, textIncorrect, textScoreSummary;
	
	@FXML
	private Button buttonCreate, buttonList, buttonSearch, buttonSearch1, buttonSave, buttonNext, buttonPreview, buttonAudioPlay, buttonEnter, buttonImageCreate, buttonMusicEnter, buttonPlay, buttonDelete, buttonQuizEnter, buttonQuizNext;

	@FXML
	private Pane rootPane, paneQuiz, paneSummary, paneNoCreations;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab menuTab, searchTab, audioSelectionTab, audioCombinationTab, imageTab, listTab, quizTab, quizSummaryTab;
	
	@FXML
	private TextField textFieldTerm, textFieldTerm1, textFieldAudioName, textFieldName, textFieldAnswer;
	
	@FXML
	private TextArea textAreaResults;
	
	@FXML
	private MenuButton menuButtonVoices, buttonVoiceRate, menuButtonNumber, menuButtonMusic;
	
	@FXML
	private MenuItem voice1, voice2;
	
	@FXML
	private ProgressIndicator progressIndicator, createProgress;
	
	@FXML
	private ListView<String> listViewAudioFiles, listViewSelected, audioList, listView, listViewCorrect, listViewIncorrect;
	
	@FXML
	private ObservableList<String> listAudioFiles, listSelected, list;
	
	@FXML
	private MediaView mediaViewQuiz, mediaViewSummary;
	
	private MediaPlayer mediaPlayerSummary, mediaPlayer, mediaPlayerQuiz;
	private Media mediaSummary, mediaQuiz;
	
	private String[] listQuiz;
	private String term, audioToPlay;
	private boolean musicDecision = false;
	private String musicFile;
	private String _selectedText;
	private String _voiceSelection;
	private String _voiceRate = "";
	private static String _selected;
	
	private int number;
	
	private ListRefresh _listRefresher = new ListRefresh();
	private AlertFactory _alertGenerator = new AlertFactory();
	private BindingsFactory _bindingsGenerator = new BindingsFactory();
	
	private ExecutorService team = Executors.newSingleThreadExecutor(); 
	private static String _searchTerm;
	private WikitProcess _wikitProcess;
	private static Alert _staticAlert;
	private static boolean _processStatus;
	private ObservableList<String> audioFiles;
	private static TextArea staticTextAreaResults;
	private static ProgressIndicator _staticProgressIndicator, _staticCreateProgress;
	private static TabPane _staticTabPane;
	private static Button _staticButtonPreview;
	private static Tab _staticImageTab;
	
	private static int levels, currentLevel, score;
	
	private static ArrayList<String> correct, incorrect;
	
	private boolean incorrectAttempt = false, added = false;
	
	// quiz summary fields
	@FXML
	private ObservableList<String> listCorrect, listIncorrect;
	

	
	@FXML
	private void initialize() {
		rootPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #757575)");
		
		setGlobalEventHandler(rootPane);
		staticTextAreaResults = textAreaResults;
		_staticProgressIndicator = progressIndicator;
		_staticCreateProgress = createProgress;
		_staticTabPane = tabPane;
		
		buttonVoiceRate.setText("1x");
		//initializing default voice
		_voiceSelection = "(voice_kal_diphone)\n";
		//initializing text area to display results in
		try {
			textAreaResults.setWrapText(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		searchTab.setOnSelectionChanged(e -> {
			_bindingsGenerator.bindFieldButton(textFieldTerm, buttonSearch);
		});
		audioCombinationTab.setOnSelectionChanged (e -> {
			// add all the audio files created into the audio files list view
			listAudioFiles = listViewAudioFiles.getItems();
			listAudioFiles.setAll(_listRefresher.refreshAudioFilesLists());
			audioCombinationTab.setDisable(false); 
			listViewSelected.getItems().clear();
			//buttonAudioPlay.disableProperty().bind(listViewAudioFiles.getSelectionModel().selectedItemProperty().isNotNull(), listViewSelected.getSelectionModel().selectedItemProperty().isNotNull());
		});
		
		listTab.setOnSelectionChanged(e -> {
			list = listView.getItems();
			list.setAll(_listRefresher.refreshCreationsFileList());
		});
		
		audioSelectionTab.setOnSelectionChanged(e -> {
			_bindingsGenerator.bindFieldButton(textFieldTerm1, buttonSearch1);
			_bindingsGenerator.bindTextAreaButton(textFieldAudioName, textAreaResults, buttonSave);
			// add all the audio files created into the audio files list view
			audioFiles = audioList.getItems();
			audioFiles.setAll(_listRefresher.refreshAudioFilesLists());
		});
		
		quizTab.setOnSelectionChanged(e -> {
			resetQuiz();
			File dir = new File("Quiz/");
			String[] list1 = dir.list();
			ArrayList<String> arrayListQuiz = new ArrayList<>();
			
			for (String fileName : list1) {
			//	File file = new File("Quiz/"+fileName);
				if (!fileName.startsWith(".")){
					arrayListQuiz.add(fileName);
				}
			}
			
			if (dir.exists() && arrayListQuiz.size() > 0) {
				textFieldAnswer.clear();
				textCorrect.setVisible(false);
				textIncorrect.setVisible(false);
				buttonQuizNext.setText("Next");
				buttonQuizNext.setDisable(true);
				buttonQuizEnter.setDisable(false);
				score = 0;
				listQuiz = arrayListQuiz.toArray(new String[arrayListQuiz.size()]); 
				currentLevel = 0;
				levels = listQuiz.length;
				textScore.setText("0/" + levels);
				int i = 0;
				while (i < listQuiz.length) {
					listQuiz[i] = listQuiz[i].substring(0, listQuiz[i].length()-4);
					i++;
				}
				if (listQuiz.length > 0 && !listQuiz[currentLevel].startsWith(".")) {
					String path = new File("Quiz/" + listQuiz[currentLevel] + ".mp4").getAbsolutePath();
					mediaQuiz = new Media(new File(path).toURI().toString());
					mediaPlayer = new MediaPlayer(mediaQuiz);
					mediaViewQuiz.setMediaPlayer(mediaPlayer);
					mediaPlayer.setAutoPlay(true);
					mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

					String[] termNames = listQuiz[currentLevel].split("-");
					term = termNames[1];

					textScore.setText("0/" + levels);

					correct = new ArrayList<>();
					incorrect = new ArrayList<>();
				}

				if (levels == 1) { 
					buttonQuizNext.setText("Finish");
				}			 
			}	
			else {
				paneNoCreations.setVisible(true);
				paneSummary.setVisible(false);
				paneQuiz.setVisible(false);
			}
	    });
		
		imageTab.setOnSelectionChanged(e -> {
			_bindingsGenerator.bindFieldButton(textFieldName, buttonImageCreate);
		});		
	}
	
	private void setGlobalEventHandler(Node root) {
	    root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.ENTER) {
	        	if(Main.getScene().getFocusOwner() == textFieldTerm || Main.getScene().getFocusOwner() == textFieldTerm1) {
	        		buttonSearch.fire();
	        		ev.consume(); 
	        	} else if (Main.getScene().getFocusOwner() == textFieldAudioName) {
	        		buttonSave.fire();
	        		ev.consume();
	        	} else if (Main.getScene().getFocusOwner() == textFieldName) {
	        		buttonImageCreate.fire();
	        		ev.consume();
	        	} else if (Main.getScene().getFocusOwner() == textFieldAnswer) {
	        		buttonQuizEnter.fire();
	        		ev.consume();
	        	}
	        		
	        }
	    });
	}
	
	@FXML
	private void handleButtonList(ActionEvent event) throws IOException {
			tabPane.getSelectionModel().select(5);
	}
	
	@FXML
	private void handleButtonCreate(ActionEvent event) throws IOException {
		textFieldTerm1.clear(); 
		textAreaResults.clear();
		menuButtonNumber.setText("Images");
		menuButtonMusic.setText("Music");
		textFieldName.clear();
		imageTab.setDisable(true);
		audioCombinationTab.setDisable(true);
		File dir = new File("audiofiles");
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				children[i].delete();
			}
		}
		tabPane.getSelectionModel().select(1);		
	}
	
	@FXML
	private void handleButtonQuiz() {
		tabPane.getSelectionModel().select(6);
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
		_selectedText = textAreaResults.getSelectedText();
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Selection error", null, "The selected text is too long, please select under 40 words");
			return;
		}
		_selectedText = _selectedText.replace("(", "");
		_selectedText = _selectedText.replace(")", "");
		
		buttonPreview.setDisable(true);
		_staticButtonPreview = buttonPreview;
		FileWriter scmwriter = new FileWriter("temporaryfiles/preview.scm", false);
		scmwriter.write(_voiceSelection);
		scmwriter.write(_voiceRate);
		scmwriter.close();
		FileWriter writer = new FileWriter("temporaryfiles/audiotext", false);
		writer.write(_selectedText);
		writer.close();
		
		PreviewProcess previewProcess = new PreviewProcess();
		team.submit(previewProcess);
		
	}
	
	public static Button getButtonPreview() {
		return _staticButtonPreview;
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
	private void handleSpeed(ActionEvent event) {
		String speedItem = ((MenuItem) event.getSource()).getText();
		String speed = speedItem.substring(0, speedItem.length()-1);
		double speedNum = 1/Double.valueOf(speed);
		_voiceRate = "(Parameter.set 'Duration_Stretch " + speedNum + ")\n";
		buttonVoiceRate.setText(speedItem);
	}
	
	@FXML
	private void handleButtonSave() throws IOException {
		//checking if selected text is empty or too long
		_selectedText = textAreaResults.getSelectedText();
		_selectedText = _selectedText.replace("(", "");
		_selectedText = _selectedText.replace(")", "");
		if (_selectedText.isEmpty()) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Selection error", null, "No text has been selected");
			return;
		}
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Selection error", null, "The selected text is too long, please select under 40 words");
			return;
		}
		String audiofileName = textFieldAudioName.getText();

		//checking if audiofile with same name already exists
		File tempFile = new File("audiofiles/"+audiofileName+".wav");
		if(tempFile.exists()) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Already exist", null, "Audio with same name already exists, please choose a new name");
			return;
		}
		FileWriter scmwriter = new FileWriter("temporaryfiles/audiofile.scm", false);
		scmwriter.write(_voiceSelection);
		scmwriter.write(_voiceRate);
		scmwriter.close();
		FileWriter writer = new FileWriter("temporaryfiles/audiotext", false);
		writer.write(_selectedText);
		writer.close();
		
		SaveAudioProcess saveAudioProcess = new SaveAudioProcess(audiofileName, this);
		team.submit(saveAudioProcess);
		textFieldAudioName.clear();
		return;
	}
	
	public void setAudioFiles(String[] listofaudiofilesarray) {
		audioFiles = audioList.getItems();
		audioFiles.setAll(listofaudiofilesarray);
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
			listAudioFiles = listViewAudioFiles.getItems();
			listAudioFiles.setAll(_listRefresher.refreshAudioFilesLists());
			tabPane.getSelectionModel().clearAndSelect(3);
			audioCombinationTab.setDisable(false); 
		} else {
			_alertGenerator.generateAlert(AlertType.WARNING, "No audio files", null, "Please create an audio file before continuing");
		}
	}
	
	//audiocombination tab
	@FXML
	private void handleButtonAdd() {
		// get the selected item from the list of audio files and and it to the selected items (to be combined) list view
		if (listViewAudioFiles.getSelectionModel().getSelectedItem() != null) {
			String selected = listViewAudioFiles.getSelectionModel().getSelectedItem().toString();
			listSelected = listViewSelected.getItems();
			listSelected.add(selected);
			listAudioFiles.remove(selected);
		}
	}
	
	@FXML
	private void handleButtonMoveBack() {
		// get the selected item from the to be combined list view and remove it from that listview
		if (listViewSelected.getSelectionModel().getSelectedItem() != null) {
			String selected = listViewSelected.getSelectionModel().getSelectedItem().toString();
			listSelected = listViewSelected.getItems();
			listSelected.remove(selected);
			listAudioFiles.add(selected);
		}
	}
	@FXML
	private void handleListViewAudioSelected() {
		if (listViewSelected.getSelectionModel().getSelectedItem() != null) {
		audioToPlay = listViewSelected.getSelectionModel().getSelectedItem();
		buttonAudioPlay.setDisable(false);
		}
	}
	
	@FXML
	private void handleListViewAudioFiles() {
		if (listViewAudioFiles.getSelectionModel().getSelectedItem() != null) {
		audioToPlay = listViewAudioFiles.getSelectionModel().getSelectedItem();
		buttonAudioPlay.setDisable(false);
		}
	}
	@FXML
	private void handleButtonPlay() {
			buttonAudioPlay.setDisable(true);
			Media media = new Media(new File("audiofiles/" + audioToPlay + ".wav").toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			listViewAudioFiles.getSelectionModel().select(null);
			listViewSelected.getSelectionModel().select(null);
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
		_alertGenerator.generateAlert(AlertType.INFORMATION, "Successfully combined", null, "Selected audio files have been combined");
	}
	
	@FXML
	private void handleButtonCombineNext(ActionEvent event) throws IOException {
		if (listViewSelected.getItems().isEmpty()) {
			_alertGenerator.generateAlert(AlertType.WARNING, "No audio files selected", null, "Please select at least one audio file to be in the video");
		}
		else {
			combine();
			imageTab.setDisable(false);
			tabPane.getSelectionModel().select(4);
		}
	}
	
	//image selection tab
	@FXML
	private void handleNumber(ActionEvent event) {
		String itemNumber = ((MenuItem) event.getSource()).getText();
		menuButtonNumber.setText(itemNumber);
		number = Integer.valueOf(itemNumber);
		setVisibleMusic();
	}

	@FXML
	private void setVisibleMusic() {
		menuButtonMusic.setDisable(false);
	}

	@FXML
	private void handleMenuNoMusic() {
		menuButtonMusic.setText("No Music");
		musicDecision = false;
		setVisibleName();
	}
	
	@FXML
	private void handleMenuMusic1() {
		menuButtonMusic.setText("Life-World");
		musicDecision = true;
		musicFile = new File("Music/VJ_Memes_-_Life-World.mp3").getAbsolutePath();
		setVisibleName();
	}
	
	@FXML
	private void handleMenuMusic2() {
		menuButtonMusic.setText("Marcello");
		musicDecision = true;
		musicFile = new File("Music/VJ_Memes_-_Marcello.mp3").getAbsolutePath();
		setVisibleName();
	}
	
	@FXML
	private void setVisibleName() {
		textFieldName.setDisable(false);
	}
	
	
	@FXML
	private void handleButtonImageCreate(ActionEvent event) {
		String creation = textFieldName.getText();
		
		createProgress.setVisible(true);

		File tempFile = new File("Creations/"+creation+".mp4");
		if(tempFile.exists()) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Already exists", null, "Creation with same name already exists, please choose a new name");
			return;
		}

		FlickrProcess flickrProcess = new FlickrProcess(number, creation, musicDecision, musicFile);
		team.submit(flickrProcess);
		textFieldTerm1.clear();
		textAreaResults.clear();
		audioList.refresh();
		textFieldAudioName.clear();
		
		listViewAudioFiles.refresh();
		listViewSelected.refresh();
		listViewSelected.getItems().clear();
		
	//	audioCombinationTab
		
		menuButtonMusic.setText("Music");
		menuButtonNumber.setText("Images");
		textFieldName.clear();
		_staticImageTab = imageTab;
		audioCombinationTab.setDisable(true);
	}
	
	public static Tab getImageTab() {
		return _staticImageTab;
	}
	
	@FXML
	private void handleButtonBack(ActionEvent event) throws IOException {
		// return to the previous scene where you select the audio
		tabPane.getSelectionModel().selectPrevious();
	}
	
	//list tab
	@FXML
	private void handleButtonListBack(ActionEvent event) throws IOException {
		_selected = null;
		tabPane.getSelectionModel().select(0);
	}
	
	@FXML
	private void handleButtonListPlay() throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/resources/Play.fxml"));
		stage.setOnCloseRequest(event -> {
			stage.close();
		    PlayController.stop();
		});
		Parent layout = loader.load();
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();
		buttonDelete.setDisable(true);
		buttonPlay.setDisable(true);
	}
	
	@FXML
	private void handleButtonDelete() {
			String confirmation = "Are you sure you want to delete " + _selected + "?";
			Alert alert = new Alert(AlertType.CONFIRMATION, confirmation);
			alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				File fileDelete = new File("Creations/" + _selected + ".mp4");
				fileDelete.delete();
				File dirQuiz = new File("Quiz/");
				File[] files = dirQuiz.listFiles(); 
				for (File f : files) {
					if (f.getName().startsWith(_selected)) {
						f.delete();
					}
				}
				_selected = null;
				list = listView.getItems();
				list.setAll(_listRefresher.refreshCreationsFileList());
			}});
			buttonPlay.setDisable(true);
			buttonDelete.setDisable(true);
	}

	@FXML
	private void handleItemSelection() {
		_selected = listView.getSelectionModel().getSelectedItem();
		if (_selected != null) {
			buttonDelete.setDisable(false);
			buttonPlay.setDisable(false);
		}
	}

	public static String getSelectedItem() {
		return _selected;
	}
	
	public static ProgressIndicator getCreateProgress() {
		return _staticCreateProgress;
	}
	
	public static TabPane getTabPane() {
		return _staticTabPane;
	}
	
	
	// quiz handlers 
	
	// check if answer entered is correct, display whether it is correct or incorrect and adjust score accordingly
	@FXML
	private void handleButtonQuizEnter() {
		buttonQuizNext.setDisable(false);
		if (textFieldAnswer.getText().equals(term)) {
			textIncorrect.setVisible(false);
			textCorrect.setVisible(true);
			buttonQuizEnter.setDisable(true);
			
			if (!incorrectAttempt) {
				correct.add(listQuiz[currentLevel]);
				score++;
			}
		}
		else {
			incorrectAttempt = true;
			textCorrect.setVisible(false);
			textIncorrect.setVisible(true);
			if (!added) {
				incorrect.add(listQuiz[currentLevel]);
			}
			added = true;
		}
		textScore.setText(score + "/" + levels);		
	}
	
	// go back to the main menu
	@FXML
	private void handleButtonQuizBack(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().select(0);
	}
	
	// go to the next question or to the summary if on the last question
	@FXML
	private void handleButtonQuizNext(ActionEvent event) throws IOException {	
		incorrectAttempt = false;
		added = false;
		buttonQuizNext.setDisable(true);
		if (buttonQuizNext.getText().equals("Finish")) {
			setSummary();
		}
		else {
			currentLevel++;
			if (currentLevel == levels - 1) {
				buttonQuizNext.setText("Finish");
			}
			textCorrect.setVisible(false);
			textIncorrect.setVisible(false);
			textFieldAnswer.clear();
			buttonQuizEnter.setDisable(false);
			String path = new File("Quiz/" + listQuiz[currentLevel] + ".mp4").getAbsolutePath();
			mediaQuiz = new Media(new File(path).toURI().toString());
			mediaPlayerQuiz = new MediaPlayer(mediaQuiz);
			mediaViewQuiz.setMediaPlayer(mediaPlayerQuiz);
			mediaPlayerQuiz.setAutoPlay(true);


			String[] termNames = listQuiz[currentLevel].split("-");
			term = termNames[1];
		}		
	}
	
	// set the summary scene for the quiz
	@FXML
	private void setSummary() {
		paneSummary.setVisible(true);
		paneQuiz.setVisible(false);	
		textScoreSummary.setText(score + "/" + levels);		
		listCorrect = listViewCorrect.getItems();
		listCorrect.setAll(correct);		
		listIncorrect = listViewIncorrect.getItems();
		listIncorrect.setAll(incorrect);
	}
	
	// reset the scene for the quiz
	@FXML
	private void resetQuiz() {
		paneNoCreations.setVisible(false);
		paneSummary.setVisible(false);
		paneQuiz.setVisible(true);
	}
	
	// quiz summary handlers
	
	@FXML
	private void handleListViewIncorrect() {
		String selected = listViewIncorrect.getSelectionModel().getSelectedItem().toString();
		listViewPlay(selected);
	}
	
	@FXML
	private void handleListViewCorrect() {
		String selected = listViewCorrect.getSelectionModel().getSelectedItem().toString();
		listViewPlay(selected);
	}
	
	private void listViewPlay(String selected) {
		//String selected = listViewIncorrect.getSelectionModel().getSelectedItem();
		if (selected != null) {
			String path = new File("Quiz/" + selected + ".mp4").getAbsolutePath();
			mediaSummary = new Media(new File(path).toURI().toString());
			mediaPlayerSummary = new MediaPlayer(mediaSummary);
			mediaViewSummary.setMediaPlayer(mediaPlayerSummary);
			mediaPlayerSummary.setAutoPlay(true);
		}
	}
	
	
	// start the quiz from the beginning
	@FXML
	private void handleButtonRetry(ActionEvent event) throws IOException {
		EventHandler<Event> e = quizTab.getOnSelectionChanged();
		e.handle(event);
		resetQuiz();
	}
	
	// return to the main menu
	@FXML
	private void handleButtonMenu(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().select(0);
		resetQuiz();
	}
	
}
