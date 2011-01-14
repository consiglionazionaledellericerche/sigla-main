package it.cnr.contab.devutil00.bp;

/**
 * BusinessProcess per la gestione del tracing SQL
 */
public class SQLTraceBP extends it.cnr.jada.util.action.BulkBP {
	private it.cnr.contab.devutil00.bulk.SQLTracerBulk sqlTracer = new it.cnr.contab.devutil00.bulk.SQLTracerBulk();
public SQLTraceBP() throws it.cnr.jada.action.BusinessProcessException {
	super();
}
public SQLTraceBP(String function) {
	super(function);
}
public void apply(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		it.cnr.jada.ejb.AdminSession admin = getAdminSession();
		admin.setSqlTracerEnabled(sqlTracer.isSqlTracerEnabled());
		admin.setDumpStackTraceEnabled(sqlTracer.isDumpStackTraceEnabled());
		refresh(context);
	} catch(javax.ejb.EJBException e) {
	} catch(java.rmi.RemoteException e) {
	}
}
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext acionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}
public it.cnr.jada.ejb.AdminSession getAdminSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
	return (it.cnr.jada.ejb.AdminSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_AdminSession",it.cnr.jada.ejb.AdminSession.class);
}
public it.cnr.contab.devutil00.bulk.SQLTracerBulk getSqlTracer() {
	return sqlTracer;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	setModel(context,sqlTracer);
	refresh(context);
	super.init(config,context);
}
public void refresh(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		it.cnr.jada.ejb.AdminSession admin = getAdminSession();
		sqlTracer.setSqlTracerEnabled(admin.isSqlTracerEnabled());
		sqlTracer.setDumpStackTraceEnabled(admin.isDumpStackTraceEnabled());
		sqlTracer.setTraceUsers(admin.getTraceUsers());
		sqlTracer.setZippedTrace(admin.getZippedTrace(context.getUserContext()));
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
public void setSqlTracer(it.cnr.contab.devutil00.bulk.SQLTracerBulk newSqlTracer) {
	sqlTracer = newSqlTracer;
}
}
