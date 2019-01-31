package it.cnr.contab.docamm00.actions;

import java.util.List;

import it.cnr.contab.docamm00.bp.CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class SelezionatoreDocumentiAmministrativiFatturazioneElettronicaAction extends SelezionatoreListaAction {
/**
 * DocumentiAmministrativiProtocollabiliAction constructor comment.
 */
public SelezionatoreDocumentiAmministrativiFatturazioneElettronicaAction() {
	super();
}

public Forward doPredisponiPerLaFirma(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OggettoBulk bulk = bp.getModel();
	Fattura_attivaBulk fattura = ((Fattura_attivaBulk)bulk);
	try {
		fillModel(context);
		bp.setModel(context, bulk);
//		try {
			bp.setSelection(context);
			bp.predisponiPerLaFirma(context);							
//		} finally {
//			bp.openIterator(context);				
//		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

public Forward doSignOTP(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();

	try {
		fillModel(context);
		bp.setSelection(context);
		List<Fattura_attivaBulk> selectedElements = bp.getSelectedElements(context);
		if (selectedElements == null || selectedElements.isEmpty())
			throw new ApplicationException("Selezionare almeno un elemento!");
		bp.recuperoFilePerFirma(context, selectedElements);
		BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
		firmaOTPBP.setModel(context, new FirmaOTPBulk());
		context.addHookForward("firmaOTP",this,"doBackFirmaOTP");
		return context.addBusinessProcess(firmaOTPBP);
	} catch(Exception e) {
		return handleException(context,e);
	}
}


public Forward doBackFirmaOTP(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OggettoBulk bulk = bp.getModel();
	HookForward caller = (HookForward)context.getCaller();
	FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
	try {
		fillModel(context);
		bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
		bp.setModel(context, bulk);
		bp.setSelection(context);
		bp.firmaOTP(context, firmaOTPBulk);			
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}	

@Override
public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
	return actioncontext.findDefaultForward();
}
public Forward doCambiaVisibilita(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OggettoBulk bulk = bp.getModel();
	Fattura_attivaBulk fattura = ((Fattura_attivaBulk)bulk);
	try {
		fillModel(context);
		String stato = fattura.getStatoFattElett();
		bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
		fattura = ((Fattura_attiva_IBulk)bulk);
		fattura.setStatoFattElett(stato);
		bp.setModel(context, bulk);
		bp.openIterator(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
