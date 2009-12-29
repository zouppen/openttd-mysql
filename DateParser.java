import java.util.regex.*;
import java.text.ParsePosition;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

/**
 * Parses OpenTTD getdate command output.
 */
public class DateParser implements LineParser {

    private static final String sqlStart = "INSERT gamedate (ingame) values ";

    private static final String matchingRegEx = "^Date: (.*)$";
    private static final int matchingGroups = 1;
    private static final Pattern matchingPattern;
    private static final DateFormat ottdFormat;

    public Date day;

    static {
	matchingPattern = Pattern.compile(matchingRegEx);
	ottdFormat = new SimpleDateFormat("dd-MM-yyy", Locale.ENGLISH);
    }
    
    /**
     * Tries to parse a line. Returns false if it's not possible to parse.
     * In the case of true this method clears its previous state ad it is
     * guaranteed to have "fresh" values. In the case of false the object's
     * content is undefined.
     *
     * Throws an exception if date format is not supported. This helps to 
     * spot bugs but may turn parser unstable. Fix if this is problematic.
     */
    public boolean match(String line) throws Exception {

	ParsePosition position = new ParsePosition(0);

	Matcher matcher = matchingPattern.matcher(line);
	if (!matcher.matches() || matchingGroups != matcher.groupCount()) {
	    return false; // this is not right pattern
	}
	
	this.day = ottdFormat.parse(matcher.group(1),position);
	if (position.getErrorIndex() != -1)
	    throw new Exception("syntax error in date format.");

	return true; // Success.
    }

    public void appendSQL(SQLBuilder sql) {
	sql.appendRaw(sqlStart);
	sql.appendRaw('(');
	sql.appendDate(day);
	sql.appendRaw(");");
    }

    public void clear() {
	this.day = null;
    }

    public String parserName() {
	return "Game day";
    }

    public String engineerDebug() {
	return "Day: "+ this.day;
    }    
}
