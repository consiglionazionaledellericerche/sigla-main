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
public class CRUDTipologiaRischioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDMissioneDiariaBP constructor comment.
 */
public CRUDTipologiaRischioBP() {
	super();
}
/**
 * CRUDMissioneDiariaBP constructor comment.
 * @param function java.lang.String
 */
public CRUDTipologiaRischioBP(String function) {
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
		Tipologia_rischioBulk tipologia = (Tipologia_rischioBulk)getModel();
		if(tipologia.getDataFineValidita()!=null && tipologia.getDataFineValidita().compareTo(CompensoBulk.getDataOdierna())<=0){
			setStatus(VIEW);		
		}
	}
}
}
