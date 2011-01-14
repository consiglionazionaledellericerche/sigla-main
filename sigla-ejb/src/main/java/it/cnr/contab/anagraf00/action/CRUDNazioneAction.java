package it.cnr.contab.anagraf00.action;

import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.anagraf00.bp.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 16.10.22)
 * @author: CNRADM
 */
public class CRUDNazioneAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDNazioneAction constructor comment.
 */
public CRUDNazioneAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 16.10.52)
 * @return it.cnr.jada.action.Forward
 * @param context it.cnr.jada.action.ActionContext
 */
public Forward doOnTipoNazioneChange(ActionContext context) {

	try{
		fillModel(context);
		CRUDNazioneBP bp = (CRUDNazioneBP)getBusinessProcess(context);
		NazioneBulk nazione = (NazioneBulk)bp.getModel();
		if (nazione.isTipoIndifferente())
			nazione.setPg_nazione(new Long(0));
		else
			nazione.setPg_nazione(null);
		
		return context.findDefaultForward();

	}catch(it.cnr.jada.bulk.FillException ex){
		return handleException(context, ex);
	}
}
}
