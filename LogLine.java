import java.util.Date;
import java.util.regex.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

public class LogLine {

	private static final String logEntryRegEx = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
	private static final int logEntryGroups = 9;
	private static final Pattern logEntryPattern;

	private static final DateFormat apacheFormat;
	private static final DateFormat outputFormat;
	
	public String ip;
	public Date date;
	public String request;
	public String response;
	public int bytes;
	public String referer;
	public String browser;

	static {
		logEntryPattern = Pattern.compile(logEntryRegEx);
		apacheFormat = new SimpleDateFormat("dd/MMM/yyy:HH:mm:ss Z");
		outputFormat = DateFormat.getInstance();
	}

	public LogLine(String line) throws Exception {
 
		Matcher matcher = logEntryPattern.matcher(line);
		if (!matcher.matches() || logEntryGroups != matcher.groupCount()) {
			throw new Exception("syntax error.");
		}

		ParsePosition position = new ParsePosition(0);

		this.ip = matcher.group(1);
		this.date = apacheFormat.parse(matcher.group(4),position);
		this.request = matcher.group(5);
		this.response = matcher.group(6);
		this.bytes = Integer.parseInt(matcher.group(7));
		if (!matcher.group(8).equals("-"))
			this.referer = matcher.group(8);
		this.browser = matcher.group(9);

		System.out.println(matcher.group(4));
		System.out.println(position.toString());

	}

	public String engineerDebug() {
		return "IP: "+ this.ip +
			"\nDate: " + this.date.toString() +
			//"\nDate: " + outputFormat.format(this.date) +
			"\nBrowser: " + this.browser;
	}
}