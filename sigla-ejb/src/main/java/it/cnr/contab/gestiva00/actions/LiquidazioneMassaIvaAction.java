package it.cnr.contab.gestiva00.actions;

import it.cnr.contab.gestiva00.ejb.*;
import java.util.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.MTUWrapper;

/**
 * Insert the type's description here.
 * Creation date: (2/24/2003 11:34:52 AM)
 * @author: CNRADM
 */
public class LiquidazioneMassaIvaAction extends LiquidazioneDefinitivaIvaAction {
/**
 * LiquidazioneMassaIvaAction constructor comment.
 */
public LiquidazioneMassaIvaAction() {
	super();
}
protected void aggiornaRegistriStampati(ActionContext context)
	throws Throwable {

	//Does nothing by default
}
protected Forward basicDoCerca(
		ActionContext context)
		throws Throwable {
		LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
		Liquidazione_massa_ivaVBulk bulk = (Liquidazione_massa_ivaVBulk)bp.getModel();
		String message=null;
		if (bulk.getTipoImpegnoFlag().equals(bulk.IMPEGNI_RESIDUO))
		{
			 message = "Si è scelto di generare movimentazioni a Residuo."
					+ "Si desidera continuare?";
		}
		else
		{
			 message = "Si è scelto di generare movimentazioni a Competenza."
					+ "Si desidera continuare?";
		}
	    return openConfirm(context,message,it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaCerca");
}
public Forward doConfermaCerca(ActionContext context,int option) {

	try {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			return basicDoCercaConfermato(context);
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
protected Forward basicDoCercaConfermato(
		ActionContext context)
		throws Throwable {

		LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
		Stampa_registri_ivaVBulk bulk = (Stampa_registri_ivaVBulk)bp.getModel();

		bulk.setRistampa(false);
	       	
		try {
			MTUWrapper wrapper = manageStampa(context, bulk);

			Liquidazione_massa_ivaVBulk stampaBulk= (Liquidazione_massa_ivaVBulk) wrapper.getBulk();
			//stampaBulk = ((LiquidazioneMassaIvaBP)bp).aggiornaProspetti(context,stampaBulk);
			//stampaBulk.aggiornaTotali();
			bp.setModel(context, stampaBulk);

			String message = getMessageFrom(wrapper);
			bp.commitUserTransaction();
			if (message != null)
				bp.setMessage(message);

			bp.resetForSearch(context);
			
		} catch (Throwable t) {
			bp.rollbackUserTransaction();
			return handleException(context, t);
		}

		return context.findDefaultForward();
	}

protected Forward basicDoRistampa(ActionContext context) 
	throws Throwable {

	LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
	bp.setMessage(
				it.cnr.jada.util.action.OptionBP.ERROR_MESSAGE, 
				"Questo tipo di liquidazione non è ristampabile");
	return context.findDefaultForward();
}
public Forward doOnTipoChange(ActionContext context) {

    try {
        fillModel(context);
        LiquidazioneMassaIvaBP bp = (LiquidazioneMassaIvaBP) context.getBusinessProcess();
        Liquidazione_massa_ivaVBulk liquidazione = (Liquidazione_massa_ivaVBulk) bp.getModel();
		aggiornaRegistriStampati(context);
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
