package it.cnr.contab.devutil00.bulk;

/**
 * OggettoBulk utilizzato da SQLTraceBP
 */
public class SQLTracerBulk extends it.cnr.jada.bulk.OggettoBulk {
	private boolean sqlTracerEnabled;
	private java.lang.String[] traceUsers;
	private boolean dumpStackTraceEnabled;

	public final static java.util.Dictionary DUMP_STACK_TRACE_KEYS;

	static {
		DUMP_STACK_TRACE_KEYS = new it.cnr.jada.util.OrderedHashtable();
		DUMP_STACK_TRACE_KEYS.put(Boolean.TRUE,"Abilitato");
		DUMP_STACK_TRACE_KEYS.put(Boolean.FALSE,"Disabilitato");
	};
	private byte[] zippedTrace;
public SQLTracerBulk() {
	super();
}
public java.util.Dictionary getDumpStackTraceKeys() {
	return DUMP_STACK_TRACE_KEYS;
}
public java.io.Reader getTraceReader() throws java.io.IOException {
	if (zippedTrace == null) return null;
	return new java.io.InputStreamReader(new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(zippedTrace)));
}
public java.lang.String[] getTraceUsers() {
	return traceUsers;
}
public boolean isDumpStackTraceEnabled() {
	return dumpStackTraceEnabled;
}
public boolean isSqlTracerEnabled() {
	return sqlTracerEnabled;
}
public void setDumpStackTraceEnabled(boolean newDumpStackTraceEnabled) {
	dumpStackTraceEnabled = newDumpStackTraceEnabled;
}
public void setSqlTracerEnabled(boolean newSqlTracerEnabled) {
	sqlTracerEnabled = newSqlTracerEnabled;
}
public void setTraceUsers(java.lang.String[] newTraceUsers) {
	traceUsers = newTraceUsers;
}
public void setZippedTrace(byte[] zippedTrace) {
	this.zippedTrace = zippedTrace;
}
}
