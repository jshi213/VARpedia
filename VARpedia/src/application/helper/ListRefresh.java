package application.helper;

import java.io.File;

/**
 * The ListRefresh class is a helper class used to read various directories to be listed.
 */
public class ListRefresh {
	
	/**
	 * Reads the audiofiles directory and returns a list of the files in the directory.
	 * 
	 * @return The list of files as a String array.
	 */
	public String[] refreshAudioFilesLists() {
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
		return listofaudiofilesarray;
	}
	
	/**
	 * Reads the Creations directory and returns a list of the files in the directory.
	 * 
	 * @return The list of files as a String array.
	 */
	public String[] refreshCreationsFileList() {
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
		return listofcreationsarray;
	}
}
