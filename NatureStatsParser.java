import java.util.regex.*;
import java.math.BigInteger;

/**
 * Parses nature part of quarter year statistics dumbed by Eladith's patch.
 */
public class NatureStatsParser implements LineParser {

    private static final String sqlStart = "INSERT nature_stats (game_id,gamedate,water,city_tiles,trees) values ";

    private static final String matchingRegEx = "^linkki: Luonto: water_tiles: (\\d+) city_owned_tiles: (\\d+) trees: (\\d+)$";
    private static final Pattern matchingPattern;

    public QuarterHeadingParser heading;
    public Integer water;
    public Integer cityTiles;
    public Integer trees;


    static {
	matchingPattern = Pattern.compile(matchingRegEx);
    }

    /**
     * A reference to quarter heading is needed to get a right date
     * because it comes before this line.
     */
    NatureStatsParser(QuarterHeadingParser heading) {
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
	if (!matcher.matches()) {
	    return false; // this is not right pattern
	}

	this.water = new Integer(matcher.group(1));
	this.cityTiles = new Integer(matcher.group(2));
	this.trees = new Integer(matcher.group(3));

	return true; // Success.
    }

    public void appendSQL(SQLBuilder sql) {

	// Company info, database drops this if it's unchanged
	sql.appendRaw(sqlStart);
	sql.appendRaw("(@cur_game,");
	sql.appendDate(heading.day);
        sql.appendRaw(',');
	sql.appendNumber(water);
	sql.appendRaw(',');
	sql.appendNumber(cityTiles);
	sql.appendRaw(',');
	sql.appendNumber(trees);
	sql.appendRaw(");");
    }

    public void clear() {

	this.water = null;
	this.cityTiles = null;
	this.trees = null;
    }

    public String parserName() {
	return "Nature stats";
    }

    public String engineerDebug() {
	return "quarter nature. trees: "+ this.trees;
    }
}
