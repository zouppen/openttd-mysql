import java.util.Date;
import java.lang.StringBuilder;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SQLBuilder {

    private StringBuilder sb = new StringBuilder();
    private ArrayList<LogLine> lines = new ArrayList<LogLine>();
    private final String start;
    private static final DateFormat sqlFormat;

    static {
	sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					    Locale.ENGLISH);
    }

    SQLBuilder(String start) {
	this.start = start;
    }

    public void addElement(LogLine line) {
	lines.add(line);
    }

    public void appendString(String str) {
	if (str == null) {
	    sb.append("NULL");
	    return;
	}
	
	sb.append('\'');

	for (int i=0; i<str.length(); i++) {
	    int chr = str.codePointAt(i);
	    
	    if (chr == '\'') sb.append("''");
	    else sb.appendCodePoint(chr);
	}

	sb.append('\'');
    }

    public void append(char chr) {
	sb.append(chr);
    }

    public void append(String str) {
	sb.append(str);
    }

    public void appendDate(Date date) {

	sb.append('\'');
	sb.append(sqlFormat.format(date));
	sb.append('\'');
    }

    public void appendInteger(Integer val) {
	if (val == null) sb.append("NULL");
	else sb.append(val);
    }

    public String toString() {
	sb.setLength(0);
	if (lines.size() == 0) return "";

	LogLine last = lines.remove(lines.size()-1);

	append(start);
	
	for (LogLine line : lines) {
	    line.appendSQL(this);
	    append(',');
	}

	last.appendSQL(this);
	append(';');

	return sb.toString();
    }

    public void clear() {
	lines.clear();
	sb.setLength(0);
    }

    public void deleteLast() {
	sb.deleteCharAt(sb.length()-1);
    }
}