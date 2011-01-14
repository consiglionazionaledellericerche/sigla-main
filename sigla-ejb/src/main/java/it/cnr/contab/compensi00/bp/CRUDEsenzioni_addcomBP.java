package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 11.35.56)
 * @author: Paola sala
 */
public class CRUDEsenzioni_addcomBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDEsenzioni_addcomBP constructor comment.
 */
public CRUDEsenzioni_addcomBP() {
	super();
}
/**
 * CRUDEsenzioni_addcomBP constructor comment.
 * @param function java.lang.String
 */
public CRUDEsenzioni_addcomBP(String function) {
	super(function);
}
/**
 * Non devo poter modificare un record che abbia data fine validita
 * inferiore alla data odierna
 * Posso modificare tutti i record che hanno data fine validita
 * maggiore o uguale della data odierna
 *
*/
public void basicEdit(ActionContext context,OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

	super.basicEdit(context, bulk, doInitializeForEdit);

	if (!isViewing()){
		Esenzioni_addcomBulk esenzioni = (Esenzioni_addcomBulk)getModel();
		if(esenzioni.getDt_fine_validita()!=null && esenzioni.getDt_fine_validita().compareTo(CompensoBulk.getDataOdierna())<=0){
			setStatus(VIEW);		
		}
	}
}
}
