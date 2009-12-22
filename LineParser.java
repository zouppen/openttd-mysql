public interface LineParser {

    /**
     * Tries to parse a line. Returns false if it's not possible to parse.
     * In the case of true this method clears its previous state ad it is
     * guaranteed to have "fresh" values. In the case of false the object's
     * content is undefined.
     */
    public boolean match(String line);

    /**
     * Appends its contents to a sqlbuilder. Returns full statements already
     * terminated by a semicolon.
     */
    public void appendSQL(SQLBuilder sql);

    /**
     * Appends debugger friendly string which represents what kind of line
     * this is.
     */
    public String parserName();

    /**
     * Name tells everything...
     */
    public String engineerDebug();
}
