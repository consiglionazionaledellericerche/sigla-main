package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (02/02/2004 12.32.44)
 * @author: Gennaro Borriello
 */
public class EstrazioneCUDBP extends AbstractEstrazioneFiscaleBP {
/**
 * EstrazioneCUDBP constructor comment.
 */
public EstrazioneCUDBP() {
	super();
}
/**
 * EstrazioneCUDBP constructor comment.
 * @param function java.lang.String
 */
public EstrazioneCUDBP(String function) {
	super(function);
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.extractCUD");

	return toolbar;
}
public void doElaboraCUD(ActionContext context) throws BusinessProcessException {

	try{

		EstrazioneCUDBulk cud = (EstrazioneCUDBulk)getModel();

		CompensoComponentSession sess = (CompensoComponentSession)createComponentSession();
		sess.doElaboraCUD(context.getUserContext(), cud);


	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}

	
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {

	try {
		EstrazioneCUDBulk cud = new EstrazioneCUDBulk();
		cud.setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		cud.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));

		
		setModel(context, cud);
	} catch(Throwable e) {
		throw handleException(e);
	}
	
	super.init(config,context);
}
}
