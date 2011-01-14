package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.tabrif.bulk.Detrazioni_lavoroBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (29/11/2001 13.42.48)
 * @author: Vincenzo Bisquadro
 */

public class CRUDDetrazioniLavoroBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * E constructor comment.
 */
public CRUDDetrazioniLavoroBP() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2001 13.55.08)
 */
public CRUDDetrazioniLavoroBP(String function) {
	super(function);
}
public void edit(ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {

	super.edit(context, bulk);
		
	if (getStatus()!=VIEW){
		Detrazioni_lavoroBulk detraz = (Detrazioni_lavoroBulk)getModel();

		if(!detraz.getDt_fine_validita().equals(EsercizioHome.DATA_INFINITO)){
			setStatus(VIEW);		
	 		throw new BusinessProcessException(new it.cnr.jada.comp.ApplicationException("E' possibile modificare solo l'ultimo record !"));
		}
	}
}
}
