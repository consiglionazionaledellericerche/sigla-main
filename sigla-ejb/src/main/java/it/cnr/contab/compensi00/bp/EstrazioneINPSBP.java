package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (16/03/2004 15.31.25)
 * @author: Gennaro Borriello
 */
public class EstrazioneINPSBP extends AbstractEstrazioneFiscaleBP {
/**
 * EstrazioneINPSBP constructor comment.
 */
public EstrazioneINPSBP() {
	super();
}
/**
 * EstrazioneINPSBP constructor comment.
 * @param function java.lang.String
 */
public EstrazioneINPSBP(String function) {
	super(function);
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.extractINPS");

	return toolbar;
}
public void doElaboraINPS(ActionContext context) throws BusinessProcessException {

	try{

		EstrazioneINPSBulk inps = (EstrazioneINPSBulk)getModel();
		
		CompensoComponentSession sess = (CompensoComponentSession)createComponentSession();
		sess.doElaboraCUD(context.getUserContext(), inps);


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
		EstrazioneINPSBulk inps = new EstrazioneINPSBulk();		
		inps.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		inps.setCd_uo(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
	
		setModel(context, inps);
	} catch(Throwable e) {
		throw handleException(e);
	}
	
	super.init(config,context);
}
}
