package application.helper;

import java.io.File;

public class ListRefresh {
	
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
