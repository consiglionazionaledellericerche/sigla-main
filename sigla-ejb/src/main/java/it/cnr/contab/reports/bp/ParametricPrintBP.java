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

package it.cnr.contab.reports.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.PrintComponentSession;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.jsp.Button;

/**
 * Insert the type's description here.
 * Creation date: (10/01/2003 12:31:58)
 * @author: CNRADM
 */
public class ParametricPrintBP extends it.cnr.jada.util.action.BulkBP {
	private String componentSessioneName;
	private Class bulkClass;
	private java.lang.String reportName;
	public int serverPriority;

	private Boolean repotWhitDsOffLine =Boolean.FALSE;

	public Boolean getRepotWhitDsOffLine() {
		return repotWhitDsOffLine;
	}

	public void setRepotWhitDsOffLine(Boolean repotWhitDsOffLine) {
		this.repotWhitDsOffLine = repotWhitDsOffLine;
	}

	/**
 * ParametricPrintBP constructor comment.
 */
public ParametricPrintBP() {
	super();
}
/**
 * ParametricPrintBP constructor comment.
 * @param function java.lang.String
 */
public ParametricPrintBP(String function) {
	super(function);
}
public PrintComponentSession createComponentSession() throws BusinessProcessException {
	return (PrintComponentSession)createComponentSession(componentSessioneName,PrintComponentSession.class);
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		OggettoBulk bulk = (OggettoBulk)bulkClass.newInstance();
		bulk.setUser(context.getUserInfo().getUserid());
		//bulk = bulk.initializeForPrint(this,context);
		return initializeBulkForPrint(context,bulk);
	} catch(Exception e) {
		throw handleException(e);
	}
}
public it.cnr.jada.util.jsp.Button[] createToolbar() {
	Button[] toolbar = new Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.close");
	return toolbar;
}
public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2003 14:44:05)
 * @return java.lang.String
 */
public java.lang.String getReportName() {
	return reportName;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2003 15:30:16)
 * @return int
 */
public int getServerPriority() {
	return serverPriority;
}
public int getStatus() {
	return FormController.UNDEFINED;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		setReportName(config.getInitParameter("reportName"));
		if ( config.getInitParameter("repotWhitDsOffLine")!=null)
			setRepotWhitDsOffLine(Boolean.valueOf(config.getInitParameter("repotWhitDsOffLine")));
		String s = config.getInitParameter("serverPriority");
		if (s != null)
			try {
					serverPriority = Integer.parseInt(config.getInitParameter("serverPriority"));
			} catch(Throwable e) {
				throw new BusinessProcessException("Errore: priorità del server di stampa non valida. "+s);
			}
	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	}
	super.init(config,context);
	initialize(context);
}
protected void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	if (bulkClass != null)
		setModel(context,createNewBulk(context));
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public OggettoBulk initializeBulkForPrint(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	try {
		bulk.setUser(context.getUserInfo().getUserid());
		//bulk = bulk.initializeForPrint(this,context);
		return createComponentSession().inizializzaBulkPerStampa(context.getUserContext(),bulk);
	} catch(Exception e) {
		throw handleException(e);
	}
}
public OggettoBulk print(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	try {
		setModel(context,createComponentSession().stampaConBulk(context.getUserContext(),bulk));
		return getModel();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
private void setBulkClass(Class bulkClass) throws ClassNotFoundException {
	this.bulkClass = bulkClass;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'bulkClassName'
 *
 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
 * @throws ClassNotFoundException	
 */
public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
	if (bulkClassName != null)
		setBulkClass(getClass().getClassLoader().loadClass(bulkClassName));
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'componentSessioneName'
 *
 * @param newComponentSessioneName	Il valore da assegnare a 'componentSessioneName'
 */
public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
	componentSessioneName = newComponentSessioneName;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2003 14:44:05)
 * @param newReportName java.lang.String
 */
public void setReportName(java.lang.String newReportName) {
	reportName = newReportName;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2003 15:30:16)
 * @param newServerPriority int
 */
public void setServerPriority(int newServerPriority) {
	serverPriority = newServerPriority;
}
}