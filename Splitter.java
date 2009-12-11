import java.io.*;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.sql.*;

/**
 * Apache-login parsija
 */
public class Splitter {
    
    public static void main(String args[]) throws Exception {
	
	Connection conn =
	    DriverManager.getConnection("jdbc:mysql://130.234.169.15/ixonos?" +
					"user=joell&password=hohfah3I");

	Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					      ResultSet.CONCUR_UPDATABLE);

	ResultSet res = getEmptyResult(stmt);

	for (String filename: args) {
	    Scanner scanner = new Scanner(new File(filename), "UTF-8");
	    int linenum = 1;
	    String line = "";
	    
	    try {
		while (true) {
		    line = scanner.nextLine();
		    LogLine entry = new LogLine(line);

		    // Quite nice insertion
		    res.updateString("ip", entry.ip);
		    res.updateTimestamp("date", new Timestamp(entry.date.getTime()));
		    res.updateString("request", entry.request);
		    res.updateInt("response", entry.response);
		    res.updateInt("bytes", entry.bytes);
		    res.updateString("referer", entry.referer);
		    res.updateString("browser", entry.browser);
		    res.insertRow();

		    //System.out.println(entry.engineerDebug());
		    linenum++;

		    if ((linenum % 50) == 0) {
			res.close();
			res = getEmptyResult(stmt);
		    }
		}
	    } catch (NoSuchElementException foo) {
		// Tiedosto kaiketi loppu, kaikki ok.
	    } catch (Exception e) {
		System.err.println("Stopped to line "+linenum);
		System.err.println("Content: "+line);
		throw e;
	    } finally {
		scanner.close();
	    }
	}
    }

    private static ResultSet getEmptyResult(Statement stmt)
	throws java.sql.SQLException {
	
	ResultSet koe = stmt.executeQuery("select * from weblog limit 0;");
	koe.moveToInsertRow();
	return koe;
    }
}
