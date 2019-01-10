package it.cnr.contab.docamm00.actions;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.bp.DocumentiAmministrativiProtocollabiliBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.IGenericSearchDocAmmBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class DocumentiAmministrativiProtocollabiliAction extends ListaDocumentiAmministrativiAction {
/**
 * DocumentiAmministrativiProtocollabiliAction constructor comment.
 */
public DocumentiAmministrativiProtocollabiliAction() {
	super();
}
/**
 * Gestisce una richiesta di ricerca.
 *
 * L'implementazione di default utilizza il metodo astratto <code>read</code>
 * di <code>CRUDBusinessProcess</code>.
 * Se la ricerca fornisce più di un risultato viene creato un
 * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
 * Al business process viene anche chiesto l'elenco delle colonne da
 * visualizzare.
 */
public Forward doCerca(ActionContext context) throws java.rmi.RemoteException,InstantiationException,javax.ejb.RemoveException {

	DocumentiAmministrativiProtocollabiliBP bp = (DocumentiAmministrativiProtocollabiliBP)context.getBusinessProcess();
	try {
		fillModel(context);
		completaSoggetto(context);
		Filtro_ricerca_doc_amm_protocollabileVBulk filtro = (Filtro_ricerca_doc_amm_protocollabileVBulk)bp.getModel();
		filtro.setPgStampa(null);
		filtro.setPgProtocollazioneIVA(null);
		
		OggettoBulk instance = (OggettoBulk)filtro.getInstance();
		Unita_organizzativaBulk unita_organizzativa = CNRUserInfo.getUnita_organizzativa(context);
		CompoundFindClause clauses = new CompoundFindClause();
		clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		clauses.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
		clauses.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, unita_organizzativa.getCd_unita_organizzativa());
		clauses.addClause("AND", "protocollo_iva", SQLBuilder.ISNULL, null);
		clauses.addClause("AND", "protocollo_iva_generale", SQLBuilder.ISNULL, null);
		clauses.addClause("AND", "flFatturaElettronica", SQLBuilder.EQUALS, Boolean.FALSE);
		
		clauses.addClause("AND", "dt_emissione", SQLBuilder.ISNULL, null);
		clauses.addClause("AND", "stato_cofi", SQLBuilder.NOT_EQUALS, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.STATO_ANNULLATO);

		filtro.setSQLClauses(clauses);
		
		it.cnr.jada.util.RemoteIterator ri = bp.find(context, clauses, instance);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		} else {
			bp.setModel(context,filtro);

			IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, (IDocumentoAmministrativoBulk)instance);
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore", new Object[] { "Th" });
			nbp.setMultiSelection(true);
			
			nbp.setSelectionListener(
					context,
					getSelectionListener(
								context, 
								(it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP)docAmmBP, 
								(FatturaAttivaSingolaComponentSession)docAmmBP.createComponentSession(),
								filtro));
			
			nbp.setIterator(context,ri);
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(instance.getClass());
			nbp.setBulkInfo(bulkInfo);
			
			if (docAmmBP instanceof IGenericSearchDocAmmBP) {
				String columnsetName = ((IGenericSearchDocAmmBP)docAmmBP).getColumnsetForGenericSearch();
				if (columnsetName != null)
					nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary(columnsetName));
			}
			context.addHookForward("seleziona",this,"doRiportaSelezione");
			return context.addBusinessProcess(nbp);
		}
	} catch(Throwable e) {
		try {
			((BusinessProcess)bp).rollbackUserTransaction();
		} catch (BusinessProcessException ex) {
			return handleException(context, ex);
		}
		return handleException(context,e);
	}
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
public Forward doRiportaSelezione(ActionContext context) {

	DocumentiAmministrativiProtocollabiliBP bp = (DocumentiAmministrativiProtocollabiliBP)context.getBusinessProcess();
	try {
		Filtro_ricerca_doc_amm_protocollabileVBulk filtro = (Filtro_ricerca_doc_amm_protocollabileVBulk)bp.getModel();
		IDocumentoAmministrativoBulk docAmm = (IDocumentoAmministrativoBulk)filtro.getInstance();
		if (docAmm instanceof Fattura_attivaBulk) {
			Fattura_attivaBulk fa = (Fattura_attivaBulk)docAmm;
			IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
			FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession)docAmmBP.createComponentSession();
			if (session.esistonoDatiPerProtocollazioneIva(context.getUserContext(), filtro.getPgProtocollazioneIVA())) {
				session.protocolla(
							context.getUserContext(), 
							filtro.getDt_stampa(),
							filtro.getPgProtocollazioneIVA());
			
				return doStampaProtocollati(context, filtro);
			}
		} else
			throw new it.cnr.jada.comp.ApplicationException("Documento amministrativo selezionato NON valido!");
	} catch(Exception e) {
		try {
			((BusinessProcess)bp).rollbackUserTransaction();
		} catch (BusinessProcessException ex) {
			return handleException(context, ex);
		}
		return handleException(context,e);
	}

	return context.findDefaultForward();
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
public Forward doStampaAnnullata(ActionContext context) {

	try {
		context.getBusinessProcess().rollbackUserTransaction();
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
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

private Forward doStampaProtocollati(
	ActionContext context,
	Filtro_ricerca_doc_amm_protocollabileVBulk filtro)
	throws BusinessProcessException {

	DocumentiAmministrativiProtocollabiliBP bp = (DocumentiAmministrativiProtocollabiliBP)context.getBusinessProcess();
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
private it.cnr.jada.util.action.SelectionListener getSelectionListener(
		ActionContext context,
		it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP aDocAmmBP,
		FatturaAttivaSingolaComponentSession faSession,
		Filtro_ricerca_doc_amm_protocollabileVBulk aFiltro) {

	final DocumentiAmministrativiProtocollabiliBP docAmmProtocollabileBP = (DocumentiAmministrativiProtocollabiliBP)context.getBusinessProcess();
	final it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP docAmmBP = aDocAmmBP;
	final FatturaAttivaSingolaComponentSession session = faSession;
	final Filtro_ricerca_doc_amm_protocollabileVBulk filtro = aFiltro;
	final Fattura_attivaBulk instance = (Fattura_attivaBulk)filtro.getInstance();
	
	return new it.cnr.jada.util.action.SelectionListener() {

		Integer counter = null;
		
		public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
			try {
				session.annullaSelezionePerStampa(
					context.getUserContext(),
					instance);
			} catch(it.cnr.jada.comp.ComponentException e) {
				throw docAmmBP.handleException(e);
			} catch(java.rmi.RemoteException e) {
				throw docAmmBP.handleException(e);
			}
		}
		public void deselectAll(it.cnr.jada.action.ActionContext context) {
				//Non deve fare nulla
		}
		public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {

			return currentSelection;
		}
		public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
			try {
				session.inizializzaSelezionePerStampa(
					context.getUserContext(),
					instance);
			} catch(it.cnr.jada.comp.ComponentException e) {
				throw docAmmBP.handleException(e);
			} catch(java.rmi.RemoteException e) {
				throw docAmmBP.handleException(e);
			}
		}
		public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
			try {
				Filtro_ricerca_doc_amm_protocollabileVBulk nuovoFiltro = session.selezionaTuttiPerStampa(
					context.getUserContext(),
					filtro);
				docAmmProtocollabileBP.setModel(context, nuovoFiltro);
			} catch(it.cnr.jada.comp.ComponentException e) {
				throw docAmmBP.handleException(e);
			} catch(java.rmi.RemoteException e) {
				throw docAmmBP.handleException(e);
			}
		}
		public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
			try {
				if (counter == null) {
					counter = new Integer(0);
					filtro.setPgProtocollazioneIVA(session.callGetPgPerProtocolloIVA(context.getUserContext()));
					filtro.setPgStampa(session.callGetPgPerStampa(context.getUserContext()));
				}
				counter = session.modificaSelezionePerStampa(
					context.getUserContext(),
					instance,
					bulks,
					oldSelection,
					newSelection,
					filtro.getPgProtocollazioneIVA(),
					counter,
					filtro.getPgStampa(),
					filtro.getDt_stampa());
				return newSelection;
			} catch(it.cnr.jada.comp.ComponentException e) {
				throw docAmmBP.handleException(e);
			} catch(java.rmi.RemoteException e) {
				throw docAmmBP.handleException(e);
			}
		}
	};
}
}
