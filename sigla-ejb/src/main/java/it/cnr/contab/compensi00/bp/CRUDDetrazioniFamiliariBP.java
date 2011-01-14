package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.tabrif.bulk.Detrazioni_familiariBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 14.37.28)
 * @author: Roberto Fantino
 */
public class CRUDDetrazioniFamiliariBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDDetrazioniFamiliariBP constructor comment.
 */
public CRUDDetrazioniFamiliariBP() {
	super();
}
/**
 * CRUDDetrazioniFamiliariBP constructor comment.
 * @param function java.lang.String
 */
public CRUDDetrazioniFamiliariBP(String function) {
	super(function);
}
public void edit(ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {

	super.edit(context, bulk);
		
	if (getStatus()!=VIEW){
		Detrazioni_familiariBulk detraz = (Detrazioni_familiariBulk)getModel();

		if(!detraz.getDt_fine_validita().equals(EsercizioHome.DATA_INFINITO)){
			setStatus(VIEW);		
	 		throw new BusinessProcessException(new it.cnr.jada.comp.ApplicationException("E' possibile modificare solo l'ultimo record !"));
		}
	}
}
}
