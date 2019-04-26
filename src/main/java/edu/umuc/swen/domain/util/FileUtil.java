package edu.umuc.swen.domain.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author ezerbo
 *
 */
public class FileUtil {

	private FileUtil() {
	}
	
	/**
	 * @param fileName File to write content to
	 * @param content Content to be written to file
	 */
	public static void writeToFile(String fileName, String content) {
		try (PrintWriter writter = new PrintWriter(fileName)) { //Convenient way to not worry about closing resources.
			writter.print(content);
		} catch (Exception e) {
			System.err.println(String.format("Unable to write to '%s', message: '%s'",
					fileName, e.getMessage()));
		}
	}
	
	/**
	 * Loads the 
	 * 
	 * @param fileName File to load data from
	 * @return content of the file
	 */
	public static String loadFromFile(String fileName) {
		try {
			String content = new String(Files.readAllBytes(Paths.get(fileName)));
			return content.replaceAll("\r", "");
		} catch (IOException e) {
			String errorMessage = String.format("Unable to read from '%s', message: '%s'",
					fileName, e.getMessage());
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		
	}
}
