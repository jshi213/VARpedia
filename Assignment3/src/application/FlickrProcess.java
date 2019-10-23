package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class FlickrProcess extends Task<Void> {
	
	private int _number;
	private String _creation;
	private String _term = MenuController.getSearchTerm();
	private boolean _musicDecision;
	private String _musicFile;
	
	public FlickrProcess(int number, String creation, boolean musicDecision, String musicFile) {
		_number = number;
		_creation = creation;
		_musicDecision = musicDecision;
		_musicFile = musicFile;
	}

	@Override
	protected Void call() throws Exception {
		try {
			String apiKey = "42961b511f71c25efbd3ddcd55e7aed7";
			String sharedSecret = "0a413e7f84de4033";

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = MenuController.getSearchTerm();
			int resultsPerPage = _number;
			int page = 0;
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
	        
	        for (Photo photo: results) {
	        	try {
	        		BufferedImage image = photos.getImage(photo,Size.LARGE);
		        	String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
		        	File outputfile = new File("downloads",filename);
		        	ImageIO.write(image, "jpg", outputfile);
	        	} catch (FlickrException fe) {
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void done() {
		// after retrieving photos, create the video for the creation
		File tempScript = tempScript();

		try {
			ProcessBuilder builder = new ProcessBuilder("bash", tempScript.toString());
			builder.inheritIO(); 
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			tempScript.delete();
			MenuScene menuScene = new MenuScene();
			Platform.runLater(menuScene);
		}
 
	}
	
	private File tempScript() {
		// find the number of seconds to play each image for
		double durationInSeconds = 0;
		try {
			File file = new File("audiofiles/combined.wav");
			AudioInputStream audioInputStream;
			audioInputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format =audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			durationInSeconds = (frames+0.0) / format.getFrameRate();
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
		
		double secondsPerImage = durationInSeconds / _number;
		// create temporary shell script to run commands for video creation
		try {
			File tempScript = File.createTempFile("script", null);
			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("cd downloads");
			// create video
			if (_musicDecision == true) {
				printWriter.println("ffmpeg -i ../audiofiles/combined.wav -i " + _musicFile + " -filter_complex amerge -c:a libmp3lame -q:a 4 speechmusic.mp3 &>/dev/null");
				printWriter.println("cat *.jpg | ffmpeg -f image2pipe -framerate 1/" + secondsPerImage + " -i - -i speechmusic.mp3 -c:v libx264 -pix_fmt yuv420p -vf \"pad=ceil(iw/2)*2:ceil(ih/2)*2\" -r 25 -max_muxing_queue_size 1024 -y audioslideshow.mp4 &>/dev/null");
				printWriter.println();
			}
			else {			
				printWriter.println("cat *.jpg | ffmpeg -f image2pipe -framerate 1/" + secondsPerImage + " -i - -i ../audiofiles/combined.wav -c:v libx264 -pix_fmt yuv420p -vf \"pad=ceil(iw/2)*2:ceil(ih/2)*2\" -r 25 -max_muxing_queue_size 1024 -y audioslideshow.mp4 &>/dev/null");
			}
			printWriter.println("cat *.jpg | ffmpeg -f image2pipe -framerate 1/" + secondsPerImage + " -i - -c:v libx264 -pix_fmt yuv420p -vf \"pad=ceil(iw/2)*2:ceil(ih/2)*2\" -r 25 -max_muxing_queue_size 1024 -y ../Quiz/" + _creation + "-" + _term + ".mp4 &>/dev/null");
			printWriter.println("ffmpeg -i audioslideshow.mp4 -vf \"drawtext=fontfile=myfont.ttf:fontsize=60: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=" + MenuController.getSearchTerm() + "\" -codec:a copy ../Creations/" + _creation + ".mp4 &>/dev/null");
			printWriter.println("rm -f slideshow.mp4 audioslideshow.mp4 speechmusic.mp3 *.jpg");
			printWriter.close();
			return tempScript;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	

}
