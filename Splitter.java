import java.io.*;
import java.util.Scanner;
 
/**
 * Apache-login parsija
 */
public class Splitter {

	public static void main(String args[]) {
		for (String filename: args) {
			Scanner scanner = Scanner(new File(filename), "UTF-8");
			
			try {
				String line = scanner.getLine();
				LogLine entry = new LogLine(line);
				System.out.println(entry.engineerDebug());
				
			} catch (NoSuchElementException) {
				// Tiedosto kaiketi loppu, kaikki ok.
			}
		}
	}
}