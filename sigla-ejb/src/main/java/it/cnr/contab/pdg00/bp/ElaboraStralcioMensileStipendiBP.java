package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.cdip.bulk.V_cnr_estrazione_coriBulk;
import it.cnr.contab.pdg00.ejb.ElaboraFileStipendiComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
/**
 * Insert the type's description here.
 * Creation date: (03/03/2009 12.00)
 * @author: Matilde D'urso
 */
public class ElaboraStralcioMensileStipendiBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController v_cnr_estrazione_cori = new SimpleDetailCRUDController("v_cnr_estrazione_cori",V_cnr_estrazione_coriBulk.class,"v_cnr_estrazione_cori",this) {
	};
/**
 * ElaboraFileStipendiBP constructor comment.
 */
public ElaboraStralcioMensileStipendiBP() {
	super();
}
public ElaboraStralcioMensileStipendiBP(String function) {
	super(function);
}
public final it.cnr.jada.util.action.SimpleDetailCRUDController getV_cnr_estrazione_cori() {
	return v_cnr_estrazione_cori;
}
public ElaboraFileStipendiComponentSession createComponentSession() throws BusinessProcessException
{
return (ElaboraFileStipendiComponentSession)createComponentSession("CNRPDG00_EJB_ElaboraFileStipendiComponentSession",ElaboraFileStipendiComponentSession.class);
}
public Forward refresh(ActionContext context) throws BusinessProcessException {
	
	try {
		V_cnr_estrazione_coriBulk e = (V_cnr_estrazione_coriBulk)getModel();
		e.getV_cnr_estrazione_cori().clear();
		setModel(context,(createComponentSession().cercaCnrEstrazioneCori(context.getUserContext(), e)));
		return context.findDefaultForward();
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.details");
	return toolbar;
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {

	v_cnr_estrazione_cori.setMultiSelection(false);
	super.init(config,context);
	refresh(context);
}

public void doElaboraStralcioMensile(ActionContext context,V_cnr_estrazione_coriBulk dett) throws BusinessProcessException {

	try{
		ElaboraFileStipendiComponentSession sess = (ElaboraFileStipendiComponentSession)createComponentSession();
		setModel(context,sess.elaboraStralcioMensile(context.getUserContext(), dett));
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
public boolean isMeseValido(ActionContext context,V_cnr_estrazione_coriBulk dett) throws BusinessProcessException {
	
	for (java.util.Iterator i = dett.getV_cnr_estrazione_cori().iterator();i.hasNext();) 
	{
		V_cnr_estrazione_coriBulk bulk = (V_cnr_estrazione_coriBulk)i.next();
		if (bulk.getMese().compareTo(dett.getMese())>=0)
			return false;
	}
  return true;
}
public boolean esisteStralcioNegativo(ActionContext context,V_cnr_estrazione_coriBulk dett) throws BusinessProcessException {

	try{
		ElaboraFileStipendiComponentSession sess = (ElaboraFileStipendiComponentSession)createComponentSession();
		return sess.esisteStralcioNegativo(context.getUserContext(), dett);
	}catch(javax.ejb.EJBException e){
		throw handleException(e);
	} catch(java.rmi.RemoteException re){
		throw handleException(re);
	} catch(ComponentException ce){
		throw handleException(ce);
	}
}
/*
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
}*/
}
