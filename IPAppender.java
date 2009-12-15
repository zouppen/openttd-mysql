public class IPAppender implements Appender {

    public LogLine a;

    IPAppender(LogLine a) {
	this.a = a;
    }

    public void appendSQL(SQLBuilder sb) {
	sb.append('(');
	sb.appendString(a.ip);
	sb.append(')');
    }

    public boolean hasData() {
	return true; // Never is null
    }
}
