package it.cnr.contab.missioni00.bp;

import it.cnr.contab.missioni00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
/**
 * CRUD per la tabella TariffariAuto
 * Creation date: (20/11/2001 15.55.08)
 * @author: Vincenzo Bisquadro
 */
public class CRUDMissioneTariffarioAutoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * Costruttore standard di CRUDMissioneTariffarioAutoBP
 */
public CRUDMissioneTariffarioAutoBP() {
	super();
}
/**
 * Costruttore di CRUDMissioneTariffarioAutoBP cui viene passato in ingresso:
 * 		function
 *
 * @param function java.lang.String
 */
public CRUDMissioneTariffarioAutoBP(String function) {
	super(function);
}
/**
 * Non devo poter modificare un record che abbia data fine validita
 * inferiore alla data odierna oppure un record con data cancellazione valorizzata
 *
 * Posso modificare tutti i record che hanno data fine validita
 * maggiore o uguale della data odierna e data cancellazione nulla
 *
*/
public void basicEdit(ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

	super.basicEdit(context, bulk, doInitializeForEdit);
		
	if (!isViewing()){
		Missione_tariffario_autoBulk tariffario = (Missione_tariffario_autoBulk) getModel();
		if(tariffario.getDt_cancellazione() != null){
			setStatus(VIEW);
 		    setMessage("Codice tariffario " + tariffario.getCd_tariffa_auto() + " cancellato!");
		}else if(tariffario.getDataFineValidita()!=null && tariffario.getDataFineValidita().compareTo(CompensoBulk.getDataOdierna())<=0){
			setStatus(VIEW);
		}
	}
}
}
