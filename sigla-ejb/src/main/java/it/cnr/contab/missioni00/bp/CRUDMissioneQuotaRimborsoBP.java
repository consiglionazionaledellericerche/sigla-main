package it.cnr.contab.missioni00.bp;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 11.35.56)
 * @author: Paola sala
 */
public class CRUDMissioneQuotaRimborsoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDMissioneDiariaBP constructor comment.
 */
public CRUDMissioneQuotaRimborsoBP() {
	super();
}
/**
 * CRUDMissioneDiariaBP constructor comment.
 * @param function java.lang.String
 */
public CRUDMissioneQuotaRimborsoBP(String function) {
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
		MissioneQuotaRimborsoBulk aRimborso = (MissioneQuotaRimborsoBulk)getModel();
		if(aRimborso.getDataFineValidita()!=null && aRimborso.getDataFineValidita().compareTo(CompensoBulk.getDataOdierna())<=0){
			setStatus(VIEW);		
		}
	}
}
}
