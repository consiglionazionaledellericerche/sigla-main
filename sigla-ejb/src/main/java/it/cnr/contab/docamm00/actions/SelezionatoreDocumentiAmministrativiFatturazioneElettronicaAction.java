package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.DocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.docamm00.bp.CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

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
public Forward doSignOTP(ActionContext context) {
	try {
		BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
		firmaOTPBP.setModel(context, new FirmaOTPBulk());
		context.addHookForward("firmaOTP",this,"doBackFirmaOTP");
		return context.addBusinessProcess(firmaOTPBP);
	} catch(Exception e) {
		return handleException(context,e);
	}
}

public Forward doMultipleSelectionFirma(ActionContext actioncontext)
		throws BusinessProcessException
	{
		return doBringBackFirma(actioncontext);
	}

public Forward basicDoBringBack(ActionContext actioncontext)
		throws BusinessProcessException
	{
		return null;
	}

public Forward doBringBackFirma(ActionContext actioncontext)
		throws BusinessProcessException
	{
		try
		{
			SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
			selezionatorelistabp.saveSelection(actioncontext);
			if(selezionatorelistabp.getSelection().isEmpty() && selezionatorelistabp.getSelection().getFocus() < 0)
			{
				selezionatorelistabp.setMessage(2, "E' necessario selezionare almeno un elemento");
				return actioncontext.findDefaultForward();
			} else
			{
				return basicDoBringBackFirma(actioncontext);
			}
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}

public Forward basicDoBringBackFirma(ActionContext actioncontext)
		throws BusinessProcessException
	{
		SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
		actioncontext.addHookForward("seleziona",this,"doSignOTP");
		HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
		if(selezionatorelistabp.getSelectionListener() == null)
		{
			hookforward.addParameter("selectedElements", selezionatorelistabp.getSelectedElements(actioncontext));
			hookforward.addParameter("selection", selezionatorelistabp.getSelection());
			hookforward.addParameter("focusedElement", selezionatorelistabp.getFocusedElement(actioncontext));
		}
		return hookforward;
	}

public Forward doBackFirmaOTP(ActionContext context) {
	CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP bp = (CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	HookForward caller = (HookForward)context.getCaller();
	FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
	try {
		bp.firmaOTP(context, firmaOTPBulk);			
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}	


private Forward doStampaProtocollati(
	ActionContext context,
	Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro)
	throws BusinessProcessException {

	DocumentiAmministrativiFatturazioneElettronicaBP bp = (DocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
	OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp(), new Object[] { "Th" });
	printbp.setReportName("/docamm/docamm/fatturaattiva_ncd.jasper");
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("Ti_stampa");
	param.setValoreParam("S");
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("id_report");
	param.setValoreParam(filtro.getPgStampa().toString());
	param.setParamType("java.lang.Long");
	printbp.addToPrintSpoolerParam(param);
	
	context.addHookForward("close", this, "doStampaAnnullata");
	printbp.setMessage(
		it.cnr.jada.util.action.OptionBP.MESSAGE,
		"Il protocollo IVA è stato assegnato correttamente. Per confermare eseguire la stampa.");

	return context.addBusinessProcess(printbp);
}
/**
 * Gestisce la selezione dopo una richiesta di ricerca.
 *
 * L'implementazione di default utilizza il metodo astratto <code>read</code>
 * di <code>CRUDBusinessProcess</code>.
 * Se la ricerca fornisce più di un risultato viene creato un
 * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
 * Al business process viene anche chiesto l'elenco delle colonne da
 * visualizzare.
 */
//private it.cnr.jada.util.action.SelectionListener getSelectionListener(
//		ActionContext context,
//		it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP aDocAmmBP,
//		FatturaAttivaSingolaComponentSession faSession,
//		Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk aFiltro) {
//
//	final DocumentiAmministrativiFatturazioneElettronicaBP docAmmFattEleBP = (DocumentiAmministrativiFatturazioneElettronicaBP)context.getBusinessProcess();
//	final it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP docAmmBP = aDocAmmBP;
//	final FatturaAttivaSingolaComponentSession session = faSession;
//	final Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro = aFiltro;
//	final Fattura_attivaBulk instance = (Fattura_attivaBulk)filtro.getInstance();
	
//	return new it.cnr.jada.util.action.SelectionListener() {
//
//		Integer counter = null;
//		
//		public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
//			try {
//				session.annullaSelezionePerStampa(
//					context.getUserContext(),
//					instance);
//			} catch(it.cnr.jada.comp.ComponentException e) {
//				throw docAmmBP.handleException(e);
//			} catch(java.rmi.RemoteException e) {
//				throw docAmmBP.handleException(e);
//			}
//		}
//		public void deselectAll(it.cnr.jada.action.ActionContext context) {
//				//Non deve fare nulla
//		}
//		public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
//
//			return currentSelection;
//		}
//		public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
//			try {
//				session.inizializzaSelezionePerStampa(
//					context.getUserContext(),
//					instance);
//			} catch(it.cnr.jada.comp.ComponentException e) {
//				throw docAmmBP.handleException(e);
//			} catch(java.rmi.RemoteException e) {
//				throw docAmmBP.handleException(e);
//			}
//		}
//		public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
//			try {
//				Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk nuovoFiltro = session.selezionaTuttiPerStampa(
//					context.getUserContext(),
//					filtro);
//				docAmmFattEleBP.setModel(context, nuovoFiltro);
//			} catch(it.cnr.jada.comp.ComponentException e) {
//				throw docAmmBP.handleException(e);
//			} catch(java.rmi.RemoteException e) {
//				throw docAmmBP.handleException(e);
//			}
//		}
//		public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
//			try {
//				if (counter == null) {
//					counter = new Integer(0);
//					filtro.setPgProtocollazioneIVA(session.callGetPgPerProtocolloIVA(context.getUserContext()));
//					filtro.setPgStampa(session.callGetPgPerStampa(context.getUserContext()));
//				}
//				counter = session.modificaSelezionePerStampa(
//					context.getUserContext(),
//					instance,
//					bulks,
//					oldSelection,
//					newSelection,
//					filtro.getPgProtocollazioneIVA(),
//					counter,
//					filtro.getPgStampa(),
//					filtro.getDt_stampa());
//				return newSelection;
//			} catch(it.cnr.jada.comp.ComponentException e) {
//				throw docAmmBP.handleException(e);
//			} catch(java.rmi.RemoteException e) {
//				throw docAmmBP.handleException(e);
//			}
//		}
//	};
//}
}
