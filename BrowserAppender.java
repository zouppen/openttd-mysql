public class BrowserAppender implements Appender {

    public LogLine a;

    BrowserAppender(LogLine a) {
	this.a = a;
    }

    public void appendSQL(SQLBuilder sb) {
	sb.append('(');
	sb.appendString(a.browser);
	sb.append(')');
    }

    public boolean hasData() {
	return (a.browser != null);
    }
}
