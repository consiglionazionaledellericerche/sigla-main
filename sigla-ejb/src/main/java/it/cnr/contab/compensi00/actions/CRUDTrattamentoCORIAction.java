package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.action.*;


/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10.17.22)
 * @author: Roberto Fantino
 */
public class CRUDTrattamentoCORIAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * TrattamentoCORIAction constructor comment.
 */
public CRUDTrattamentoCORIAction() {
	super();
}
public Forward doBlankSearchFind_trattamento(ActionContext context, Trattamento_coriBulk trattCORI) {

	trattCORI.setTipoTrattamento(new Tipo_trattamentoBulk());
	trattCORI.setRighe(new it.cnr.jada.bulk.BulkList());

	return context.findDefaultForward();
}
public Forward doBringBackSearchFind_trattamento(ActionContext context, Trattamento_coriBulk trattCORI, Tipo_trattamentoBulk tipoTrattamento) {

	try{
		if(tipoTrattamento != null) {
			trattCORI.setTipoTrattamento(tipoTrattamento);
			CRUDTrattamentoCORIBP bp = (CRUDTrattamentoCORIBP)getBusinessProcess(context);
			bp.setDirty(true);
			bp.fillAllRows(context, trattCORI);
		}

		return context.findDefaultForward();
	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
public Forward doFreeSearchFind_contributo_ritenuta(ActionContext context) {

	try{

		Tipo_contributo_ritenutaBulk tipoCori = new Tipo_contributo_ritenutaBulk();
		TipoContributoRitenutaComponentSession sess = (TipoContributoRitenutaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCOMPENSI00_EJB_TipoContributoRitenutaComponentSession", TipoContributoRitenutaComponentSession.class);
		tipoCori.setClassificazioneCoriColl(sess.loadClassificazioneCori(context.getUserContext()));
		tipoCori.setClassificazioneMontantiColl(sess.loadClassificazioneMontanti(context.getUserContext()));

		return freeSearch(context, getFormField(context, "main.find_contributo_ritenuta"), tipoCori);
		
	}catch(it.cnr.jada.comp.ComponentException ex){
		return handleException(context, ex);
	}catch(java.rmi.RemoteException ex){
		return handleException(context, ex);
	}catch(javax.ejb.EJBException ex){
		return handleException(context, ex);
	}
}
}
