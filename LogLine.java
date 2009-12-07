import java.util.regex.*;
import java.text.ParseException;

public class LogLine {

	private static final String logEntryRegEx = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
	private static final int logEntryGroups = 9;
	private static final Pattern logEntryPattern = Pattern.compile(logEntryPattern);
	
	public String ip;
	public Date date;
	public String request;
	public String response;
	public int bytes;
	public String referer;
	public String browser;

	public LogLine(String line) throws ParseException {
 
		Matcher matcher = logEntryPattern.matcher(line);
		if (!matcher.matches() || logEntryGroups != matcher.groupCount()) {
			throw ParseException("syntax error.");
		}

		this.ip = matcher.group(1);
		// this.date = matcher.group(4);
		this.request = matcher.group(5);
		this.response = matcher.group(6);
		//this.bytes = matcher.group(7);
		if (!matcher.group(8).equals("-"))
			this.referer = matcher.group(8);
		this.browser = matcher.group(9);
	}

	public String engineerDebug() {
		return "IP: "+ this.ip + "\nBrowser: " + this.browser;
	}
}