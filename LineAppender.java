public class LineAppender implements Appender {

    public LogLine a;

    LineAppender(LogLine a) {
	this.a = a;
    }

    public void appendSQL(SQLBuilder sb) {
	sb.append('(');
	sb.appendString(a.server);
	sb.append(',');
	sb.appendString(a.service);
	sb.append(',');
	sb.appendString(a.ip);
	sb.append(',');
	sb.appendDate(a.date);
	sb.append(',');
	sb.appendString(a.request);
	sb.append(',');
	sb.appendInteger(a.response);
	sb.append(',');
	sb.appendInteger(a.bytes);
	sb.append(',');
	sb.appendString(a.referer);
	sb.append(',');
	sb.appendString(a.browser);
	sb.append(')');
    }

    public boolean hasData() {
	return true; // always has new data
    }
}
