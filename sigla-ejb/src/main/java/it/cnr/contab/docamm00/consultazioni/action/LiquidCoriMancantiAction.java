package it.cnr.contab.docamm00.consultazioni.action;

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.RemoveException;

import it.cnr.contab.docamm00.consultazioni.bp.ConsLiquidCoriMancantiBP;
import it.cnr.contab.docamm00.consultazioni.bulk.V_liquid_cori_mancantiBulk;
import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.OptionBP;

public class LiquidCoriMancantiAction extends it.cnr.jada.util.action.BulkAction{
public LiquidCoriMancantiAction() {
	super();
}

/**
 * Gestisce il cambiamento del mese impostando le relative dati inizio e fine
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	it.cnr.contab.docamm00.consultazioni.bp.LiquidCoriMancantiBP bp= (it.cnr.contab.docamm00.consultazioni.bp.LiquidCoriMancantiBP) context.getBusinessProcess();
	try {
		bp.fillModel(context); 
		bp.validaDate(context,(V_liquid_cori_mancantiBulk) bp.getModel());
		V_liquid_cori_mancantiBulk cori = (V_liquid_cori_mancantiBulk) bp.getModel();
		ConsLiquidCoriMancantiBP consBP = (ConsLiquidCoriMancantiBP)context.createBusinessProcess("ConsLiquidCoriMancantiBP",new Object[] {"Tr"});
		context.addBusinessProcess(consBP);
		consBP.openIterator(context,cori);
	    return context.findDefaultForward();
	} catch (Exception e) {
			return handleException(context,e); 
	} 
}
public Forward doCloseForm(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
				return doConfirmCloseForm(actioncontext, 4);
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}
}
