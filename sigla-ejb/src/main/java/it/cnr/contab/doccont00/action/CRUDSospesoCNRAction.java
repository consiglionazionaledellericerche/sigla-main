package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione Sospesi o Riscontri
 */
public class CRUDSospesoCNRAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDSospesoCNRAction() {
	super();
}
/**
 * Metodo utilizzato per gestire l'invio di una distinta.
 */
public Forward doCambiaStatoCNR(ActionContext context) {

	try 
	{
		fillModel(context);
		CRUDSospesoCNRBP bp = (CRUDSospesoCNRBP)context.getBusinessProcess();
		bp.cambiaStatoCNR( context );
		return context.findDefaultForward();
	}
	catch ( Exception e )
	{
		return handleException( context, e )	;
	}	
		
}
}
