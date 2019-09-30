/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
