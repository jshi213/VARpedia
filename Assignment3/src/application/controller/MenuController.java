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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * The controller class for Menu.fxml, containing functionality for all components of the main stage of the application. 
 */
public class MenuController {

	//FXML tagged javafx nodes from scenebuilder
	@FXML
	private Label textName, textMusic, textScoreSummary, textScore, textCorrect, textIncorrect;
	
	@FXML
	private Button buttonCreate, buttonList, buttonSearch, buttonSearch1, buttonSave, buttonNext, buttonPreview, buttonAudioPlay, buttonEnter, buttonImageCreate, buttonMusicEnter, buttonPlay, buttonDelete, buttonQuizEnter, buttonQuizNext;

	@FXML
	private ToggleButton themeButton;
	
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
	private ObservableList<String> listAudioFiles, listSelected, list, listCorrect, listIncorrect;
	
	@FXML
	private MediaView mediaViewQuiz, mediaViewSummary;
	
	//Non-fxml fields
	private MediaPlayer mediaPlayerSummary, mediaPlayer, mediaPlayerQuiz;
	private Media mediaSummary, mediaQuiz;
	private ExecutorService team = Executors.newSingleThreadExecutor(); 
	private String[] listQuiz;
	private String _selectedText, _voiceSelection, term, audioToPlay, _voiceRate = "", musicFile;
	private WikitProcess _wikitProcess;
	private ListRefresh _listRefresher = new ListRefresh();
	private AlertFactory _alertGenerator = new AlertFactory();
	private BindingsFactory _bindingsGenerator = new BindingsFactory();
	private boolean musicDecision = false;
	private boolean incorrectAttempt = false, added = false;
	private int number;
	private ObservableList<String> audioFiles;
	
	//static fields used for processing to and from other classes
	private static String _selected;
	private static String _staticSearchTerm;
	private static boolean _processStatus;
	private static TextArea staticTextAreaResults;
	private static ProgressIndicator _staticProgressIndicator, _staticCreateProgress;
	private static TabPane _staticTabPane;
	private static Button _staticButtonPreview;
	private static Tab _staticImageTab;
	private static int levels, currentLevel, score;
	private static ArrayList<String> correct, incorrect;
	
	/**
	 * Initializes various components of the application linked to the primary stage.
	 */
	@FXML
	private void initialize() {
		//call method to add handler for enter key
		setGlobalEventHandler(rootPane);
		
		//Initializing application components and fields
		staticTextAreaResults = textAreaResults;
		_staticProgressIndicator = progressIndicator;
		_staticCreateProgress = createProgress;
		_staticTabPane = tabPane;
		_voiceSelection = "(voice_kal_diphone)\n";
		try {
			textAreaResults.setWrapText(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//binding button and field when searchtab is selected
		searchTab.setOnSelectionChanged(e -> {
			_bindingsGenerator.bindFieldButton(textFieldTerm, buttonSearch);
		});
		
		audioCombinationTab.setOnSelectionChanged (e -> {
			// add all the audio files created into the audio files list view
			listAudioFiles = listViewAudioFiles.getItems();
			listAudioFiles.setAll(_listRefresher.refreshAudioFilesLists());
			audioCombinationTab.setDisable(false); 
			listViewSelected.getItems().clear();
		});
		
		//refreshing listview whenever viewtab is focused
		listTab.setOnSelectionChanged(e -> {
			list = listView.getItems();
			list.setAll(_listRefresher.refreshCreationsFileList());
		});
		
		audioSelectionTab.setOnSelectionChanged(e -> {
			//various bindings
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
		
	/**
	 * Adds listener for enter key so that pressing enter on specific scenarios will fire specific buttons.
	 * 
	 * @param root The root component to find other components from
	 */
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
	
////MAIN MENU TAB HANDLERS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@FXML
	private void handleThemeButton(ActionEvent event) {
		if(themeButton.isSelected()) {
		Main.getScene().getStylesheets().clear();
		Main.getScene().setUserAgentStylesheet(null);
		Main.getScene().getStylesheets().add(getClass().getResource("/resources/applicationlight.css").toExternalForm());
		themeButton.setText("Dark Theme");
		} else {
			Main.getScene().getStylesheets().clear();
			Main.getScene().setUserAgentStylesheet(null);
			Main.getScene().getStylesheets().add(getClass().getResource("/resources/application.css").toExternalForm());
			themeButton.setText("Light Theme");
		}
	}
	/**
	 * Handler for list button in main menu which changes to the viewcreations tab.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonList(ActionEvent event) throws IOException {
			tabPane.getSelectionModel().select(5);
	}
	
	/**
	 * Handler for create button in main menu which changes to the search tab and deletes audiofiles contents after
	 * resetting components used for creating last creation.
	 * 
	 * @param event
	 * @throws IOException
	 */
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
	
	/**
	 * Handler for quiz button in main menu which changes to the quiz tab.
	 */
	@FXML
	private void handleButtonQuiz() {
		tabPane.getSelectionModel().select(6);
	}
	
////SEARCH TAB HANDLERS AND METHODS//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Handler for the search button in the search tab. Starts the WikitProcess task with the input typed by the user, then changes to the
	 * audio creation tab.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonSearch(ActionEvent event) throws IOException {
		textAreaResults.clear();
		if(tabPane.getSelectionModel().getSelectedItem() == searchTab) {
			_staticSearchTerm = textFieldTerm.getText();
		} else {
			_staticSearchTerm = textFieldTerm1.getText();
		}
		textFieldTerm1.setText(_staticSearchTerm);
		textFieldTerm.clear();
		progressIndicator.setVisible(true);
		WikitProcess wikitProcess = new WikitProcess(_staticSearchTerm);
		_wikitProcess = wikitProcess;
		_processStatus = true;
		team.submit(wikitProcess);
		tabPane.getSelectionModel().select(2);
	}
	
	/**
	 * Handler for the back button in the search tab. Changes to main menu tab and flags the WikitProcess task to stop if it is currently
	 * running.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonSearchBack(ActionEvent event) throws IOException {
		if(_processStatus) {
			_wikitProcess.setCancel();
		}
		tabPane.getSelectionModel().selectPrevious();
	}
	
	/**
	 * Gets the progress indicator for searching previously copied to a static field.
	 * 
	 * @return	The copy of the search progress indicator
	 */
	public static ProgressIndicator getProgressIndicator() {
		return _staticProgressIndicator;
	}

	
	/**
	 * Sets the flag for when the process is running to false.
	 */
	public static void setProcessStatus() {
		_processStatus = false;
		
	}
	
	/**
	 * Gets the searchterm the user typed previously copied to a static field.
	 * 
	 * @return	The copy of the searchterm
	 */
	public static String getSearchTerm() {
		return _staticSearchTerm;
	}

////AUDIO SELECTION TAB HANDLERS AND METHODS/////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the text area with the text in the "initialtext" file
	 */
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

	/**
	 * Handler for the preview button in the audio creation tab. Starts the PreviewProcess task to create temporary audio file and play it.
	 * Displays alert if selected text is too long.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonPreview(ActionEvent event) throws IOException {
		_selectedText = textAreaResults.getSelectedText();
		if(_selectedText.length() - _selectedText.replaceAll(" ", "").length() > 39) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Selection error", null, "The selected text is too long, please select under 40 words");
			return;
		}
		if(_selectedText == null || _selectedText.trim().equals(" ")) {
			_alertGenerator.generateAlert(AlertType.WARNING, "Selection error", null, "Please select text to preview");
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
	
	/**
	 * Gets the preview button instance previously copied to a static field.
	 * 
	 * @return	The copy of the button
	 */
	public static Button getButtonPreview() {
		return _staticButtonPreview;
	}
	
	/**
	 * Handler for when the first menuitem is chosen from the menubutton in the audio creation tab. Sets the menubutton text to "default"
	 * and assigns the default voice to the voice selection field. 
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleVoice1(ActionEvent event) throws IOException {
		menuButtonVoices.setText("Default");
		_voiceSelection = "(voice_kal_diphone)\n";
	}
	
	/**
	 * Handler for when the second menuitem is chosen from the menubutton in the audio creation tab. Sets the menubutton text to "NZ male"
	 * and assigns the NZ male voice to the voice selection field. 
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleVoice2(ActionEvent event) throws IOException {
		menuButtonVoices.setText("NZ Male");
		_voiceSelection = "(voice_akl_nz_jdt_diphone)\n";
	}
	
	/**
	 * Handler for selections made from the voice rate menubutton. Sets the menubutton text to the chosen voice rate and assigns the chosen
	 * voice rate to the voice rate field.
	 * 
	 * @param event
	 */
	@FXML
	private void handleSpeed(ActionEvent event) {
		String speedItem = ((MenuItem) event.getSource()).getText();
		String speed = speedItem.substring(0, speedItem.length()-1);
		double speedNum = 1/Double.valueOf(speed);
		_voiceRate = "(Parameter.set 'Duration_Stretch " + speedNum + ")\n";
		buttonVoiceRate.setText(speedItem);
	}
	
	/**
	 * Handler for the save button in the audio creation tab. Saves the selected text as audio with chosen speech settings
	 * by running the SaveAudioProcess task.
	 * 
	 * @throws IOException
	 */
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
	
	/**
	 * Sets the audioFiles ObservableList<String> to the current list of audiofiles in the audiofiles directory. 
	 * 
	 * @param listofaudiofilesarray The list of audio files in the audiofiles directory.
	 */
	public void setAudioFiles(String[] listofaudiofilesarray) {
		audioFiles = audioList.getItems();
		audioFiles.setAll(listofaudiofilesarray);
	}
	
	/**
	 * Handler for the back button in the audio creation tab. Changes to the search tab.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonCreateMenuBack(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().selectPrevious();
	}
	
	/**
	 * Handler for the next button in the audio creation tab. Shows an alert if an audiofile has not yet been made, and changes to
	 * the audio combination tab otherwise. 
	 * 
	 * @param event
	 * @throws IOException
	 */
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
	
////AUDIO COMBINATION TAB HANDLERS NAD METHODS///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Handler for the right pointing button in the audio combination tab. Moves the selected audio file on the left listview to the
	 * right listview for combination. 
	 */
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
	
	/**
	 * Handler for the left pointing button in the audio combination tab. Moves the selected audio file on the right listview to the
	 * left listview to remove from combination.
	 */
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
	
	/**
	 * Handler for when audio files on the right listview have been selected. Enables the left pointing button in the audio
	 * creation tab.
	 */
	@FXML
	private void handleListViewAudioSelected() {
		if (listViewSelected.getSelectionModel().getSelectedItem() != null) {
			audioToPlay = listViewSelected.getSelectionModel().getSelectedItem();
			buttonAudioPlay.setDisable(false);
		}
	}

	/**
	 * Handler for when audio files on the left listview have been selected. Enables the right pointing button in the audio
	 * creation tab.
	 */
	@FXML
	private void handleListViewAudioFiles() {
		if (listViewAudioFiles.getSelectionModel().getSelectedItem() != null) {
			audioToPlay = listViewAudioFiles.getSelectionModel().getSelectedItem();
			buttonAudioPlay.setDisable(false);
		}
	}
	
	/**
	 * Handler for the play button in the audio creation tab. Plays the selected audio in the listviews.
	 */
	@FXML
	private void handleButtonPlay() {
			buttonAudioPlay.setDisable(true);
			Media media = new Media(new File("audiofiles/" + audioToPlay + ".wav").toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			listViewAudioFiles.getSelectionModel().select(null);
			listViewSelected.getSelectionModel().select(null);
	}
	
	    
	/**
	 * Handler for the back button in the audio combination tab. Changes to the audio creation tab. 
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML 
	private void handleButtonCombineBack(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().selectPrevious();
	}
	
	/**
	 * Combines the audio files previously moved for combination into a files named "combined.wav".
	 * 
	 * @throws IOException
	 */
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
	
	/**
	 * Handler for the next button in the audio combination tab. Calls the combine method to combine the audio files if audio files have
	 * been selected for combination. Shows alert otherwise.
	 * 
	 * @param event
	 * @throws IOException
	 */
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
	
////IMAGE SELECTION TAB HANDLERS AND METHODS/////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Hander for when a selection is made for the image number menubutton. Allows the next selection to be made and stores the number
	 * of images wanted by the user in the number field. 
	 * 
	 * @param event
	 */
	@FXML
	private void handleNumber(ActionEvent event) {
		String itemNumber = ((MenuItem) event.getSource()).getText();
		menuButtonNumber.setText(itemNumber);
		number = Integer.valueOf(itemNumber);
		menuButtonMusic.setDisable(false);
	}

	/**
	 * Handler for when the selection for the music is the no music menuitem. 
	 */
	@FXML
	private void handleMenuNoMusic() {
		menuButtonMusic.setText("No Music");
		musicDecision = false;
		setVisibleName();
	}
	
	/**
	 * Handler for when the selection for the music is the first music menuitem. 
	 */
	@FXML
	private void handleMenuMusic1() {
		menuButtonMusic.setText("Life-World");
		musicDecision = true;
		musicFile = new File("Music/VJ_Memes_-_Life-World.mp3").getAbsolutePath();
		setVisibleName();
	}
	
	/**
	 * Handler for when the selection for the music is the second music menuitem. 
	 */
	@FXML
	private void handleMenuMusic2() {
		menuButtonMusic.setText("Marcello");
		musicDecision = true;
		musicFile = new File("Music/VJ_Memes_-_Marcello.mp3").getAbsolutePath();
		setVisibleName();
	}

	/**
	 * Sets the textfield for the name of the creation to be visible. 
	 */
	private void setVisibleName() {
		textFieldName.setDisable(false);
	}
	
	
	/**
	 * Handler for the create button in the image creation tab. Combines all the user choices into finalizing the creation with the
	 * FlickrProcess task. Changes to the main menu tab and resets all relevant components for next creation. 
	 * 
	 * @param event
	 */
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
	
	/**
	 * Gets a copy of the imagetab as a tab object previously copied into a static field. 
	 * 
	 * @return The copy of the imagetab
	 */
	public static Tab getImageTab() {
		return _staticImageTab;
	}
	
	/**
	 * Handler for the back button in the image creation tab. Changes to the audio combination tab.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonBack(ActionEvent event) throws IOException {
		// return to the previous scene where you select the audio
		tabPane.getSelectionModel().selectPrevious();
	}
	
////VIEW TAB HANDLERS AND METHODS////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Handler for the back button in the view tab. Changes to the main menu tab and resets the selection field.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonListBack(ActionEvent event) throws IOException {
		_selected = null;
		tabPane.getSelectionModel().select(0);
	}
	
	/**
	 * Handler for the play button in the view tab. Opens a new stage with the video player and the selected creation loaded into 
	 * the mediaview. 
	 * 
	 * @throws IOException
	 */
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
	
	/**
	 * Handler for the delete button in the view tab. Opens a prompt for the user to confirm deletion, then deletes the selected creation
	 * if the user confirms.
	 */
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

	/**
	 * Handler for when a creation is selected from the listview. Enables the play and delete buttons. 
	 */
	@FXML
	private void handleItemSelection() {
		_selected = listView.getSelectionModel().getSelectedItem();
		if (_selected != null && !_selected.isEmpty()) {
			buttonDelete.setDisable(false);
			buttonPlay.setDisable(false);
		}
	}

	/**
	 * Gets a copy of the selected creation as a String previously copied into a static field.
	 * 
	 * @return The copy of the selection
	 */
	public static String getSelectedItem() {
		return _selected;
	}
	
	/**
	 * Gets a copy of the progress indicator previously copied into a static field. 
	 * 
	 * @return The copy of the progress indicator
	 */
	public static ProgressIndicator getCreateProgress() {
		return _staticCreateProgress;
	}
	
	/**
	 * Gets a copy of the tabpane as a tab object previously copied into a static field.
	 * 
	 * @return The copy of the tabpane
	 */
	public static TabPane getTabPane() {
		return _staticTabPane;
	}
	
////QUIZ HANDLERS AND METHODS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Handler for enter button in quiz tab. Checks if the keyword user typed is the searchterm for the video being displayed, then 
	 * displays whether it is correct or incorrect and adjusts score accordingly. 
	 */
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
	
	/**
	 * Handler for the back button in the quiz tab. Changes to the main menu.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonQuizBack(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().select(0);
	}
	
	/**
	 * Handler for the next button in the quiz tab. Displays the next question or the quiz summary if on the last question.
	 * 
	 * @param event
	 * @throws IOException
	 */
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
	
	/**
	 * Initializes and displays the summary screen for the quiz tab.
	 */
	private void setSummary() {
		paneSummary.setVisible(true);
		paneQuiz.setVisible(false);	
		textScoreSummary.setText(score + "/" + levels);		
		listCorrect = listViewCorrect.getItems();
		listCorrect.setAll(correct);		
		listIncorrect = listViewIncorrect.getItems();
		listIncorrect.setAll(incorrect);
	}
	
	/**
	 * Resets the quiz tab for another quiz.
	 */
	@FXML
	private void resetQuiz() {
		paneNoCreations.setVisible(false);
		paneSummary.setVisible(false);
		paneQuiz.setVisible(true);
	}
	
	/**
	 * Handler for when an answer is selected in the correct listview of the quiz summary screen. Plays the video for the related creation
	 * in an embedded mediaview. 
	 */
	@FXML
	private void handleListViewIncorrect() {
		String selected = listViewIncorrect.getSelectionModel().getSelectedItem().toString();
		listViewPlay(selected);
	}
	
	/**
	 * Handler for when an answer is selected in the incorrect listview of the quiz summary screen. Plays the video for the related creation
	 * in an embedded mediaview. 
	 */
	@FXML
	private void handleListViewCorrect() {
		String selected = listViewCorrect.getSelectionModel().getSelectedItem().toString();
		listViewPlay(selected);
	}
	
	/**
	 * Finds the given selection in the "Quiz" directory and plays it in an embedded mediaview.
	 * 
	 * @param selected The selected video
	 */
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
	
	/**
	 * Handler for the retry button in the quiz tab. Restarts the same quiz from the beginning by resetting the quiz tab. 
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonRetry(ActionEvent event) throws IOException {
		EventHandler<Event> e = quizTab.getOnSelectionChanged();
		e.handle(event);
		resetQuiz();
	}
	
	/**
	 * Handler for the menu button in the quiz tab. Changes to the main menu tab.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void handleButtonMenu(ActionEvent event) throws IOException {
		tabPane.getSelectionModel().select(0);
		resetQuiz();
	}
	
}
