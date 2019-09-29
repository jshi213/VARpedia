package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import javafx.concurrent.Task;

public class FlickrProcess extends Task<Void> {
	
	private int _number;
	private String _creation;
	
	public FlickrProcess(int number, String creation) {
		_number = number;
		_creation = creation;
	}

	@Override
	protected Void call() throws Exception {
		try {
//			String apiKey = getAPIKey("apiKey");
			String apiKey = "42961b511f71c25efbd3ddcd55e7aed7";
//			String sharedSecret = getAPIKey("sharedSecret");
			String sharedSecret = "0a413e7f84de4033";

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = SearchController._searchTerm;
			int resultsPerPage = _number;
			int page = 0;
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
//	        System.out.println("Retrieving " + results.size()+ " results");
	        
	        for (Photo photo: results) {
	        	try {
	        		BufferedImage image = photos.getImage(photo,Size.LARGE);
		        	String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
		        	File outputfile = new File("downloads",filename);
		        	ImageIO.write(image, "jpg", outputfile);
//		        	System.out.println("Downloaded "+filename);
	        	} catch (FlickrException fe) {
//	        		System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("\nDone");
		return null;
	}
	
	@Override
	protected void done() {
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
		}
	}
	
	private File tempScript() {
		try {
			File tempScript = File.createTempFile("script", null);
			Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
			PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("#!/bin/bash");
			printWriter.println("cd downloads");
			printWriter.println("ffmpeg -framerate 1 -pattern_type glob -i '*.jpg' -c:v libx264 -vf \"pad=ceil(iw/2)*2:ceil(ih/2)*2\" -r 30 -pix_fmt yuv420p slideshow.mp4");
			printWriter.println("ffmpeg -stream_loop -1 -i slideshow.mp4 -i ../audiofiles/combined.wav -shortest -map 0:v:0 -map 1:a:0 -y audioslideshow.mp4");
			printWriter.println("ffmpeg -i audioslideshow.mp4 -vf \"drawtext=fontfile=myfont.ttf:fontsize=60: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=" + SearchController._searchTerm + "\" -codec:a copy ../Creations/" + _creation + ".mp4");
			printWriter.println("rm -f slideshow.mp4 audioslideshow.mp4 *.jpg");
			printWriter.println("rm -rf ../audiofiles ../temporaryfiles ../downloads");
			printWriter.close();
			return tempScript;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static String getAPIKey(String key) throws Exception {
//		// TODO fix the following based on where you will have your config file stored
//
//		String config = System.getProperty("user.dir") 
//				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 
//		
////		String config = System.getProperty("user.home")
////				+ System.getProperty("file.separator")+ "bin" 
////				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 
//		File file = new File(config); 
//		BufferedReader br = new BufferedReader(new FileReader(file)); 
//		
//		String line;
//		while ( (line = br.readLine()) != null ) {
//			if (line.trim().startsWith(key)) {
//				br.close();
//				return line.substring(line.indexOf("=")+1).trim();
//			}
//		}
//		br.close();
//		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
//	}
	

}
