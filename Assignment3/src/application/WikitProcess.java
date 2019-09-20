package application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class WikitProcess extends Task<String> {
	
	private String _searchTerm;
	
	public WikitProcess(String searchTerm) {
		_searchTerm = searchTerm;
	}
	
	@Override
	protected String call() throws Exception {
		try {
			//starting process for wikit command and reading output from command
			ProcessBuilder pb = new ProcessBuilder();

			pb.command("bash", "-c", "wikit "+_searchTerm);
			Process process = pb.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				if(line.contains("? Ambiguous") || line.contains(":^(")) {
					AmbiguousAlert ambiguousAlert = new AmbiguousAlert();
					Platform.runLater(ambiguousAlert);
				} else {
					//formatting results into sentence lines
					line = line.substring(2, line.length());
					line = line.replaceAll("(\\. )([A-Z])", "\\.\n$2");

					//writing formatted result into text file
					PrintWriter writer = new PrintWriter("initialtext", "UTF-8");
					writer.println(line);
					writer.close();
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
