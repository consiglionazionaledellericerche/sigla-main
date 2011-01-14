package it.cnr.contab.devutil00.action;

/**
 * Insert the type's description here.
 * Creation date: (11/07/2001 15:45:52)
 * @author: CNRADM
 */
public class SQLTraceAction extends it.cnr.jada.util.action.BulkAction {
	private java.lang.String newTraceUser;
	private java.lang.String[] traceUsers;
/**
 * SQLTraceAction constructor comment.
 */
public SQLTraceAction() {
	super();
}
public it.cnr.jada.action.Forward doAddUser(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.contab.devutil00.bp.SQLTraceBP bp = (it.cnr.contab.devutil00.bp.SQLTraceBP)context.getBusinessProcess();
		if (newTraceUser != null)
			bp.getAdminSession().addSQLTraceUser(newTraceUser.toUpperCase());
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public it.cnr.jada.action.Forward doAggiorna(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.contab.devutil00.bp.SQLTraceBP bp = (it.cnr.contab.devutil00.bp.SQLTraceBP)context.getBusinessProcess();
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.action.BusinessProcessException e) {
		return handleException(context,e);
	} 
}
public it.cnr.jada.action.Forward doApplica(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.contab.devutil00.bp.SQLTraceBP bp = (it.cnr.contab.devutil00.bp.SQLTraceBP)context.getBusinessProcess();
		bp.fillModel(context);
		bp.apply(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.action.BusinessProcessException e) {
		return handleException(context,e);
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	}
}
public it.cnr.jada.action.Forward doRemoveUsers(it.cnr.jada.action.ActionContext context) {
	try {
		it.cnr.contab.devutil00.bp.SQLTraceBP bp = (it.cnr.contab.devutil00.bp.SQLTraceBP)context.getBusinessProcess();
		if (traceUsers != null) {
			it.cnr.jada.ejb.AdminSession admin = bp.getAdminSession();
			for (int i = 0;i < traceUsers.length;i++)
				if (traceUsers[i] != null) 
					admin.removeSQLTraceUser(traceUsers[i]);
		}
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2001 13:52:30)
 * @return java.lang.String
 */
public java.lang.String getNewTraceUser() {
	return newTraceUser;
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2001 13:53:00)
 * @return java.lang.String[]
 */
public java.lang.String[] getTraceUsers() {
	return traceUsers;
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2001 13:52:30)
 * @param newNewTraceUser java.lang.String
 */
public void setNewTraceUser(java.lang.String newNewTraceUser) {
	newTraceUser = newNewTraceUser;
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2001 13:53:00)
 * @param newTraceUsers java.lang.String[]
 */
public void setTraceUsers(java.lang.String[] newTraceUsers) {
	traceUsers = newTraceUsers;
}
}
