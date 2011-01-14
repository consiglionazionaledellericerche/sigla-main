package it.cnr.contab.pdg00.bp;

import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_stipendi_cofi_dettBulk;
import it.cnr.contab.pdg00.ejb.ElaboraFileStipendiComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
/**
 * Insert the type's description here.
 * Creation date: (29/09/2006 12.00)
 * @author: Matilde D'urso
 */
public class ElaboraFileStipendiBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController v_stipendi_cofi_dett = new SimpleDetailCRUDController("v_stipendi_cofi_dett",V_stipendi_cofi_dettBulk.class,"v_stipendi_cofi_dett",this) {
	};
	private final SimpleDetailCRUDController batch_log_riga = new SimpleDetailCRUDController("batch_log_riga",Batch_log_rigaBulk.class,"batch_log_riga",this) {
	};
/**
 * ElaboraFileStipendiBP constructor comment.
 */
public ElaboraFileStipendiBP() {
	super();
}
/**
 * ElaboraFileStipendiBP constructor comment.
 * @param function java.lang.String
 */
public ElaboraFileStipendiBP(String function) {
	super(function);
}
public final it.cnr.jada.util.action.SimpleDetailCRUDController getV_stipendi_cofi_dett() {
	return v_stipendi_cofi_dett;
}
public final it.cnr.jada.util.action.SimpleDetailCRUDController getBatch_log_riga() {
	return batch_log_riga;
}
public ElaboraFileStipendiComponentSession createComponentSession() throws BusinessProcessException
{
return (ElaboraFileStipendiComponentSession)createComponentSession("CNRPDG00_EJB_ElaboraFileStipendiComponentSession",ElaboraFileStipendiComponentSession.class);
}
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
public Forward refresh(ActionContext context) throws BusinessProcessException {
	
	try {
		V_stipendi_cofi_dettBulk d = (V_stipendi_cofi_dettBulk)getModel();
		d.getV_stipendi_cofi_dett().clear();
		setModel(context,(createComponentSession().cercaFileStipendi(context.getUserContext(), d)));
		return context.findDefaultForward();
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
}
public void doCercaBatch(ActionContext context,V_stipendi_cofi_dettBulk dett) throws BusinessProcessException{
	try{
		ElaboraFileStipendiComponentSession sess = (ElaboraFileStipendiComponentSession)createComponentSession();
		setModel(context,sess.cercaBatch(context.getUserContext(), dett));
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}	
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.start");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.reset");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.details");
	return toolbar;
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {

	try {
		v_stipendi_cofi_dett.setMultiSelection(false);
		batch_log_riga.setMultiSelection(false);
	} catch(Throwable e) {
		throw handleException(e);
	}
	super.init(config,context);
}
public void doElaboraFile(ActionContext context,V_stipendi_cofi_dettBulk dett) throws BusinessProcessException {

	try{
		ElaboraFileStipendiComponentSession sess = (ElaboraFileStipendiComponentSession)createComponentSession();
		setModel(context,sess.elaboraFile(context.getUserContext(), dett));
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
public void doReset(ActionContext context,V_stipendi_cofi_dettBulk dett) throws BusinessProcessException{
	try{
		ElaboraFileStipendiComponentSession sess = (ElaboraFileStipendiComponentSession)createComponentSession();
		setModel(context,sess.annullaElaborazione(context.getUserContext(), dett));
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
}
