import java.util.regex.*;
import java.math.BigInteger;

public class AnnualStatsParser implements LineParser {

    private static final String sqlStart = "INSERT company_annual (game_id,company_id,year,cost_construction,cost_new_vs,cost_train,cost_road,cost_air,cost_ship,cost_property,income_train,income_road,income_air,income_ship,loan_interest,other) values ";

    private static final String matchingRegEx = "^linkki: (\\d+)\\(.* Construction: (-?\\d+)  New Vehicles: (-?\\d+)  Train running cost: (-?\\d+) Roadveh running cost: (-?\\d+) Aircraft running cost: (-?\\d+) Ship running cost: (-?\\d+) Property maintainance: (-?\\d+) Train income: (-?\\d+) Roadveh income: (-?\\d+) Aircraft income: (-?\\d+) Ship income: (-?\\d+) Loan interest: (-?\\d+) Other: (-?\\d+)$";
	
    private static final int matchingGroups = 14;
    private static final Pattern matchingPattern;

    public AnnualHeadingParser heading;
    public Integer company_id;
    public BigInteger cost_construction;
    public BigInteger cost_new_vs;
    public BigInteger cost_train;
    public BigInteger cost_road;
    public BigInteger cost_air;
    public BigInteger cost_ship;
    public BigInteger cost_property;
    public BigInteger income_train;
    public BigInteger income_road;
    public BigInteger income_air;
    public BigInteger income_ship;
    public BigInteger loan_interest;
    public BigInteger other;

    static {
	matchingPattern = Pattern.compile(matchingRegEx);
    }

    /**
     * A reference to quarter heading is needed to get a right date
     * because it comes before this line.
     */
    AnnualStatsParser(AnnualHeadingParser heading) {
	this.heading = heading;
    }

    /**
     * Tries to parse a line. Returns false if it's not possible to parse.
     * In the case of true this method clears its previous state ad it is
     * guaranteed to have "fresh" values. In the case of false the object's
     * content is undefined.
     */
    public boolean match(String line) {

	Matcher matcher = matchingPattern.matcher(line);
	if (!matcher.matches() || matchingGroups != matcher.groupCount()) {
	    return false; // this is not right pattern
	}
	
	this.company_id = new Integer(matcher.group(1));
	this.cost_construction = new BigInteger(matcher.group(2));
	this.cost_new_vs = new BigInteger(matcher.group(3));
	this.cost_train = new BigInteger(matcher.group(4));
	this.cost_road = new BigInteger(matcher.group(5));
	this.cost_air = new BigInteger(matcher.group(6));
	this.cost_ship = new BigInteger(matcher.group(7));
	this.cost_property = new BigInteger(matcher.group(8));
	this.income_train = new BigInteger(matcher.group(9));
	this.income_road = new BigInteger(matcher.group(10));
	this.income_air = new BigInteger(matcher.group(11));
	this.income_ship = new BigInteger(matcher.group(12));
	this.loan_interest = new BigInteger(matcher.group(13));
	this.other = new BigInteger(matcher.group(14));
	
	return true; // Success.
    }

    public void appendSQL(SQLBuilder sql) {

	sql.appendRaw(sqlStart);
	sql.appendRaw("(@cur_game,");
	sql.appendNumber(company_id);
	sql.appendRaw(',');
	sql.appendNumber(heading.year);
	sql.appendRaw(',');
	sql.appendNumber(cost_construction);
	sql.appendRaw(',');
	sql.appendNumber(cost_new_vs);
	sql.appendRaw(',');
	sql.appendNumber(cost_train);
	sql.appendRaw(',');
	sql.appendNumber(cost_road);
	sql.appendRaw(',');
	sql.appendNumber(cost_air);
	sql.appendRaw(',');
	sql.appendNumber(cost_ship);
	sql.appendRaw(',');
	sql.appendNumber(cost_property);
	sql.appendRaw(',');
	sql.appendNumber(income_train);
	sql.appendRaw(',');
	sql.appendNumber(income_road);
	sql.appendRaw(',');
	sql.appendNumber(income_air);
	sql.appendRaw(',');
	sql.appendNumber(income_ship);
	sql.appendRaw(',');
	sql.appendNumber(loan_interest);
	sql.appendRaw(',');
	sql.appendNumber(other);
	sql.appendRaw(");");
    }

    public void clear() {

	this.company_id = null;
	this.cost_construction = null;
	this.cost_new_vs = null;
	this.cost_train = null;
	this.cost_road = null;
	this.cost_air = null;
	this.cost_ship = null;
	this.cost_property = null;
	this.income_train = null;
	this.income_road = null;
	this.income_air = null;
	this.income_ship = null;
	this.loan_interest = null;
	this.other = null;
    }

    public String parserName() {
	return "Annual stats";
    }

    public String engineerDebug() {
	return "Annual. company: "+ this.company_id + " train income: " + this.income_train;
    }
}
