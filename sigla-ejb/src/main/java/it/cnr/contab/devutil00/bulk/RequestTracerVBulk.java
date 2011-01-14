package it.cnr.contab.devutil00.bulk;

public class RequestTracerVBulk extends it.cnr.jada.bulk.OggettoBulk {
	private java.util.List tracingUsers;
/**
 * 
 * @return java.util.Set
 */
public java.util.List getTracingUsers() {
	return tracingUsers;
}
/**
 * 
 * @param newTracingUsers java.util.Set
 */
public void setTracingUsers(java.util.Enumeration tracingUsers) {
	this.tracingUsers = new java.util.ArrayList();
	if (tracingUsers == null) return;
	for (;tracingUsers.hasMoreElements();)
		this.tracingUsers.add(tracingUsers.nextElement());
}
/**
 * 
 * @param newTracingUsers java.util.Set
 */
public void setTracingUsers(java.util.List newTracingUsers) {
	tracingUsers = newTracingUsers;
}
}
