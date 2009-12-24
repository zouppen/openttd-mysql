import java.util.regex.*;
import java.text.ParsePosition;
import java.math.BigInteger;

public class CompanyParser implements LineParser {

    private static final String sqlStart = "INSERT company (company_id,money,loan,value,trains,roadvs,planes,ships) values ";

    private static final String matchingRegEx = "^#:(\\d+).*Money: (\\d+)  Loan: (\\d+)  Value: (\\d+)  \\(T:(\\d+), R:(\\d+), P:(\\d+), S:(\\d+)\\).*$";
    private static final int matchingGroups = 8;
    private static final Pattern matchingPattern;

    public Integer company_id;
    public BigInteger money;
    public BigInteger loan;
    public BigInteger value;
    public Integer trains;
    public Integer roadvs;
    public Integer planes;
    public Integer ships;

    static {
	matchingPattern = Pattern.compile(matchingRegEx);
    }

    /**
     * Tries to parse a line. Returns false if it's not possible to parse.
     * In the case of true this method clears its previous state ad it is
     * guaranteed to have "fresh" values. In the case of false the object's
     * content is undefined.
     */
    public boolean match(String line) {

	ParsePosition position = new ParsePosition(0);

	Matcher matcher = matchingPattern.matcher(line);
	if (!matcher.matches() || matchingGroups != matcher.groupCount()) {
	    return false; // this is not right pattern
	}
	
	this.company_id = new Integer(matcher.group(1));
	this.money = new BigInteger(matcher.group(2));
	this.loan = new BigInteger(matcher.group(3));
	this.value = new BigInteger(matcher.group(4));
	this.trains = new Integer(matcher.group(5));
	this.roadvs = new Integer(matcher.group(6));
	this.planes = new Integer(matcher.group(7));
	this.ships = new Integer(matcher.group(8));

	return true; // Success.
    }

    public void appendSQL(SQLBuilder sql) {

	sql.appendRaw(sqlStart);
	sql.appendRaw('(');
	sql.appendNumber(company_id);
	sql.appendRaw(',');
	sql.appendNumber(money);
	sql.appendRaw(',');
	sql.appendNumber(loan);
	sql.appendRaw(',');
	sql.appendNumber(value);
	sql.appendRaw(',');
	sql.appendNumber(trains);
	sql.appendRaw(',');
	sql.appendNumber(roadvs);
	sql.appendRaw(',');
	sql.appendNumber(planes);
	sql.appendRaw(',');
	sql.appendNumber(ships);
	sql.appendRaw(");");
    }

    public void clear() {

	this.company_id = null;
	this.money = null;
	this.loan = null;
	this.value = null;
	this.trains = null;
	this.roadvs = null;
	this.planes = null;
	this.ships = null;
    }

    public String parserName() {
	return "Company";
    }

    public String engineerDebug() {
	return "company: "+ this.company_id + " money: " + this.money;
    }
}
