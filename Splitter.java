import java.io.*;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.sql.*;
import java.util.zip.GZIPInputStream;

/**
 * Apache-login parsija
 */
public class Splitter {

    private static final String start = "INSERT weblog (ip,date,request,response,bytes,referer,browser) values ";
    
    public static void main(String args[]) throws Exception {
	
	Connection conn =
	    DriverManager.getConnection("jdbc:mysql://130.234.169.15/ixonos?" +
					"user=joell&password=hohfah3I");

	Statement stmt = conn.createStatement();
	SQLBuilder sqlstr = new SQLBuilder(start);

	for (String filename: args) {
	    InputStream in =new GZIPInputStream(new FileInputStream(filename));
	    Scanner scanner = new Scanner(in, "UTF-8");
	    int linenum = 1;
	    String line = "";
	    
	    try {
		while (true) {
		    line = scanner.nextLine();
		    LogLine entry = new LogLine(line);

		    sqlstr.addElement(entry);

		    if ((linenum % 100) == 0) {
			stmt.executeUpdate(sqlstr.toString());
			sqlstr.clear();
		    }
		    
		    linenum++;
		}
	    } catch (NoSuchElementException foo) {
		// Tiedosto kaiketi loppu, kaikki ok.
	    } catch (Exception e) {
		System.err.println("Error at: "+filename+":"+linenum);
		System.err.println("Content: "+line);
		throw e;
	    } finally {
		scanner.close();

		// One more time
		stmt.executeUpdate(sqlstr.toString());
		sqlstr.clear();

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
