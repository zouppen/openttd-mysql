import java.io.*;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.sql.*;
import java.util.zip.GZIPInputStream;
import java.util.regex.*;

/**
 * Apache-login parsija
 */
public class Splitter {

    //private static final String start = "INSERT weblog (server,service,ip,date,request,response,bytes,referer,browser) values ";
    private static final String start_ip = "INSERT IGNORE ip (ip) values ";
    private static final String start_browser = "INSERT IGNORE browser_raw (browser) values ";

    private static final String filenameRegex =
	"^.*/(.*)/(.*)\\.\\d{4}-\\d{2}-\\d{2}\\.gz";
    
    public static void main(String args[]) throws Exception {
	
	Connection conn =
	    DriverManager.getConnection("jdbc:mysql://130.234.169.15/ixonos?" +
					"user=joell&password=hohfah3I");

	Statement stmt = conn.createStatement();
	//SQLBuilder sqlstr = new SQLBuilder(start);
	SQLBuilder sqlstr_browser = new SQLBuilder(start_browser);
	SQLBuilder sqlstr_ip = new SQLBuilder(start_ip);
	Pattern filenamePattern = Pattern.compile(filenameRegex);

	for (String filename: args) {
	    System.out.println(filename);

	    Matcher matcher = filenamePattern.matcher(filename);
	    if (!matcher.matches() || matcher.groupCount() != 2) {
		throw new Exception("Filename pattern is not clear. Must be hostname/service.year-month-day.gz");
	    }
	    
	    String server = matcher.group(1);
	    String service = matcher.group(2);

	    InputStream in =new GZIPInputStream(new FileInputStream(filename));
	    Scanner scanner = new Scanner(in, "UTF-8");
	    int linenum = 1;
	    String line = "";
	    
	    try {
		while (true) {
		    line = scanner.nextLine();
		    LogLine entry = new LogLine(server,service,line);
		    //Appender ap_ip = new LineAppender(entry);
		    Appender ap_ip = new IPAppender(entry);
		    Appender ap_browser = new BrowserAppender(entry);

		    //sqlstr.addElement_browser(entry);
		    sqlstr_ip.addElement(ap_ip);
		    sqlstr_browser.addElement(ap_browser);

		    if ((linenum % 100) == 0) {
			String command = sqlstr_ip.toString();
			if (!"".equals(command)) {
			    stmt.executeUpdate(command);
			}

			command = sqlstr_browser.toString();
			if (!"".equals(command)) {
			    stmt.executeUpdate(command);
			}
			
			//sqlstr.clear();
			sqlstr_ip.clear();
			sqlstr_browser.clear();
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
		
		// One more time, flush the sql buffer
		String command = sqlstr_ip.toString();
		if (!"".equals(command)) {
		    stmt.executeUpdate(command);
		}
		
		command = sqlstr_browser.toString();
		if (!"".equals(command)) {
		    stmt.executeUpdate(command);
		}
		
		//sqlstr.clear();
		sqlstr_ip.clear();
		sqlstr_browser.clear();
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
