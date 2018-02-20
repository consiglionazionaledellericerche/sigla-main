package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.*;
import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class CostiStipendialiMensiliAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
public CostiStipendialiMensiliAction() {
	super();
}
/**
 * Annulla la scrittura analitica dei mesi selezionati.
 * Invoca CostiDipendenteComponent per annullare la scrittura analitica
 * dei mesi selezionati.
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doAnnullaScrittura(ActionContext context) throws BusinessProcessException {
	try {
		CostiStipendialiMensiliBP bp = (CostiStipendialiMensiliBP)context.getBusinessProcess();
		bp.saveSelection(context);

		if (bp.getSelection().isEmpty() && bp.getFocusedElement() == null)
			setErrorMessage(context,"Per poter annullare la contabilizzazione è necessario selezionare almeno un mese.");
		for (java.util.Iterator i = bp.iterator();i.hasNext();) {
			V_cdp_stato_mensilitaBulk stato_mese = (V_cdp_stato_mensilitaBulk)i.next();
			bp.createComponentSession().annullaScritturaAnalitica(context.getUserContext(),stato_mese.getMese().intValue());
			stato_mese.setFl_stato_scarico(Boolean.TRUE);
		}

		bp.refresh(context);

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doBringBack(ActionContext context) throws BusinessProcessException {
	// Reimplementato per impedire che la selezione di un elemento provochi
	// la chiusura del bp (come da default in SelezionatoreListaAction
	return context.findDefaultForward();
}
/**
 * Apre la finestra per la ripartizione dei costi stipendiali mensili
 * sul mese selezionato.
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doConfiguraRipartizione(ActionContext context) throws BusinessProcessException {
	CostiStipendialiMensiliBP bp = (CostiStipendialiMensiliBP)context.getBusinessProcess();
	bp.saveSelection(context);
	V_cdp_stato_mensilitaBulk cdp_stato_mensilita = (V_cdp_stato_mensilitaBulk)bp.getFocusedElement();
	if (cdp_stato_mensilita != null)
		try {
			BusinessProcess costiDipenenteBP = context.createBusinessProcess("CostiDipendenteMensiliBP",new Object[] { "M", cdp_stato_mensilita.getMese() });
			return context.addBusinessProcess(costiDipenenteBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	else 
		setErrorMessage(context,"Per poter effettuare la ripartizione è necessario selezionare un mese.");
	return context.findDefaultForward();
}
/**
 * Genera la scrittura analitica dei mesi selezionati.
 * Invoca CostiDipendenteComponent per generare la scrittura analitica
 * dei mesi selezionati.
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doGeneraScrittura(ActionContext context) throws BusinessProcessException {
	try {
		CostiStipendialiMensiliBP bp = (CostiStipendialiMensiliBP)context.getBusinessProcess();
		bp.saveSelection(context);

		if (bp.getSelection().isEmpty() && bp.getFocusedElement() == null)
			setErrorMessage(context,"Per poter effettuare la contabilizzazione è necessario selezionare almeno un mese.");
		for (java.util.Iterator i = bp.iterator();i.hasNext();) {
			V_cdp_stato_mensilitaBulk stato_mese = (V_cdp_stato_mensilitaBulk)i.next();
			bp.createComponentSession().generaScritturaAnalitica(context.getUserContext(),stato_mese.getMese().intValue());
			stato_mese.setFl_stato_scarico(Boolean.TRUE);
		}

		bp.refresh(context);

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
