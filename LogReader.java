import java.io.*;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.sql.*;
import java.util.regex.*;

/**
 * OpenTTD-palvelinlogin parsija
 */
public class Splitter {

    public static void main(String args[]) throws Exception {
	
	Connection conn =
	    DriverManager.getConnection("jdbc:mysql://192.168.24.1/openttd?" +
					"user=karvanoppa&password=taidu6oK");

	Statement stmt = conn.createStatement();
	SQLBuilder sqlstr = new SQLBuilder();

	// messages need to be feeded to this program with tail -f 
	Scanner scanner = new Scanner(System.in, "UTF-8");
	String line = "";
	SQLBuilder sqlLine = new SQLBuilder();

	ArrayList<LineParser> lineparsers = new ArrayList<LineParser>();

	// Add all line parsers to this arraylist in the order they
	// should be tried. If one doesn't match, trying the next one.
	lineparsers.add(new MsgParser());

	// Neverending loop. Waiting new lines forever.
	while (true) {
	    try {
		line = scanner.nextLine();
		
		LineParser thisParser = null;

		// Looking for a good parser
		for ( Lineparser cur : lineParsers ) {
		    if (cur.match(line)) {
			thisParser = cur;
			break;
		    }
		}

		if ( thisParser == null) continue; // Quietly skip a line.

		System.out.println("Got a line. " + thisParser.parserName +
				   " ... " + thisParser.engineerDebug());

		sqlLine.clear();
		thisParser.appendSQL(sqlLine);
		stmt.executeUpdate(sqlLine.toString());
		
	    } catch (Exception e) {
		System.err.println("Content: "+line);
		throw e;
	    }
	}
    }
}
