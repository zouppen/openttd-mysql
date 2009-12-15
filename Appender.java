public interface Appender {
    public boolean hasData();
    public void appendSQL(SQLBuilder sb);
}
