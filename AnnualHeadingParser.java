import java.util.regex.*;

/**
 * Parses the headings of annual reports.
 * This is supported in Eladith's openttd-output patch.
 * 
 * Outputs no SQL.
 */
public class AnnualHeadingParser implements LineParser {

    private static final String matchingRegEx = "^linkki: Tilinpaatokset vuodelle (\\d+)$";
    private static final int matchingGroups = 1;
    private static final Pattern matchingPattern;

    public Integer year;

    static {
	matchingPattern = Pattern.compile(matchingRegEx);
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

	Matcher matcher = matchingPattern.matcher(line);
	if (!matcher.matches() || matchingGroups != matcher.groupCount()) {
	    return false; // this is not right pattern
	}

	this.year = Integer.parseInt(matcher.group(1));
	
	return true; // Success.
    }

    public void appendSQL(SQLBuilder sql) {
    }

    public void clear() {
	this.year = null;
    }

    public String parserName() {
	return "Annual heading";
    }

    public String engineerDebug() {
	return "Year: "+ this.year;
    }    
}
