package it.cnr.contab.devutil00.bp;

import it.cnr.contab.devutil00.bulk.*;
/**
 * BusinessProcess per la gestione del tracing SQL
 */
public class RequestTraceBP extends it.cnr.jada.util.action.BulkBP {
/**
 * @see it.cnr.jada.util.action.BulkBP
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	setModel(context,new RequestTracerVBulk());
	refresh(context);
	super.init(config,context);
}
public void refresh(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	RequestTracerVBulk requestTracer = (RequestTracerVBulk)getModel();
	requestTracer.setTracingUsers(context.getRequestTracingUsers());
}
}
