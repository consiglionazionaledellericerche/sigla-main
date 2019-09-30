/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.ListaDocumentiAmministrativiBP;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.comp.IDocumentoAmministrativoMgr;
import it.cnr.contab.docamm00.bp.IGenericSearchDocAmmBP;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk;
import it.cnr.contab.docamm00.bp.DocumentiAmministrativiRistampabiliBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoGenericoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 * @author: Roberto Peli
 */
public class DocumentiAmministrativiRistampabiliAction extends ListaDocumentiAmministrativiAction {
/**
 * DocumentiAmministrativiProtocollabiliAction constructor comment.
 */
public DocumentiAmministrativiRistampabiliAction() {
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

	DocumentiAmministrativiRistampabiliBP bp = (DocumentiAmministrativiRistampabiliBP)context.getBusinessProcess();
	try {
		fillModel(context);
		completaSoggetto(context);
		Filtro_ricerca_doc_amm_ristampabileVBulk filtro = (Filtro_ricerca_doc_amm_ristampabileVBulk)bp.getModel();
		filtro.setPgStampa(null);

		try {
			filtro.validateClauses();
		} catch (it.cnr.jada.bulk.ValidationException e) {
			return handleException(context, e);
		}
		
		OggettoBulk instance = (OggettoBulk)filtro.getInstance();
		Unita_organizzativaBulk unita_organizzativa = CNRUserInfo.getUnita_organizzativa(context);
		CompoundFindClause clauses = new CompoundFindClause();
		clauses.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
		clauses.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, unita_organizzativa.getCd_unita_organizzativa());
		if (filtro.isProtocolliClause()) {
			clauses.addClause("AND", "cd_tipo_sezionale", SQLBuilder.EQUALS, filtro.getTipo_sezionale().getCd_tipo_sezionale());
			clauses.addClause("AND", "protocollo_iva", SQLBuilder.GREATER_EQUALS, filtro.getDa_protocollo_iva());
			clauses.addClause("AND", "protocollo_iva", SQLBuilder.LESS_EQUALS, filtro.getA_protocollo_iva());
		} else if (filtro.isProtocolliGeneraleClause()) {
			clauses.addClause("AND", "cd_tipo_sezionale", SQLBuilder.EQUALS, filtro.getTipo_sezionale().getCd_tipo_sezionale());
			clauses.addClause("AND", "protocollo_iva_generale", SQLBuilder.GREATER_EQUALS, filtro.getDa_protocollo_iva_generale());
			clauses.addClause("AND", "protocollo_iva_generale", SQLBuilder.LESS_EQUALS, filtro.getA_protocollo_iva_generale());
		} else {
			clauses.addClause("AND", "protocollo_iva", SQLBuilder.ISNOTNULL, null);
			clauses.addClause("AND", "protocollo_iva_generale", SQLBuilder.ISNOTNULL, null);
			clauses.addClause("AND", "dt_emissione", SQLBuilder.GREATER_EQUALS, filtro.getDt_da_stampa());
			clauses.addClause("AND", "dt_emissione", SQLBuilder.LESS_EQUALS, filtro.getDt_a_stampa());
		}
		clauses.addClause("AND", "stato_cofi", SQLBuilder.NOT_EQUALS, Fattura_attivaBulk.STATO_ANNULLATO);

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
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(filtro.getInstance().getClass());
			nbp.setBulkInfo(bulkInfo);
			docAmmBP = getBusinessProcessForDocAmm(context, filtro.getInstance());

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
public Forward doOnClauseChange(ActionContext context) {

	try {
		fillModel(context);
		DocumentiAmministrativiRistampabiliBP bp = (DocumentiAmministrativiRistampabiliBP)context.getBusinessProcess();
		Filtro_ricerca_doc_amm_ristampabileVBulk filtro = (Filtro_ricerca_doc_amm_ristampabileVBulk)bp.getModel();
		if (!filtro.isProtocolliClause() || !filtro.isProtocolliGeneraleClause())
			filtro.setTipo_sezionale(null);
		filtro.setDa_protocollo_iva(null);
		filtro.setA_protocollo_iva(null);
		filtro.setDa_protocollo_iva_generale(null);
		filtro.setA_protocollo_iva_generale(null);
		filtro.setDt_da_stampa(null);
		filtro.setDt_a_stampa(null);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce una richiesta di cambiamento del tipo doc amm da ricercare.
 *
 */
public Forward doOnOptionChange(ActionContext context) {

	try {
		Forward fwd = super.doOnOptionChange(context);
		DocumentiAmministrativiRistampabiliBP bp = (DocumentiAmministrativiRistampabiliBP)context.getBusinessProcess();
		Filtro_ricerca_doc_amm_ristampabileVBulk filtro = (Filtro_ricerca_doc_amm_ristampabileVBulk)bp.getModel();
		filtro.setTipo_sezionale(null);
		bp.aggiornaSezionali(context, filtro);
		return fwd;
	} catch(Throwable e) {
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

	DocumentiAmministrativiRistampabiliBP bp = (DocumentiAmministrativiRistampabiliBP)context.getBusinessProcess();
	try {
		Filtro_ricerca_doc_amm_ristampabileVBulk filtro = (Filtro_ricerca_doc_amm_ristampabileVBulk)bp.getModel();
		IDocumentoAmministrativoBulk docAmm = (IDocumentoAmministrativoBulk)filtro.getInstance();
		if (docAmm instanceof Fattura_attivaBulk) {
			Fattura_attivaBulk fa = (Fattura_attivaBulk)docAmm;
			IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
			FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession)docAmmBP.createComponentSession();
			if (session.esistonoDatiPerStampaIva(context.getUserContext(), filtro.getPgStampa()))
				return doRistampa(context, filtro);
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

private Forward doRistampa(
	ActionContext context,
	Filtro_ricerca_doc_amm_ristampabileVBulk filtro)
	throws BusinessProcessException {

	DocumentiAmministrativiRistampabiliBP bp = (DocumentiAmministrativiRistampabiliBP)context.getBusinessProcess();
	OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp(), new Object[] { "Th" });

	printbp.setReportName("/docamm/docamm/fatturaattiva_ncd.jasper");
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("Ti_stampa");
	param.setValoreParam("R");
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("id_report");
	param.setValoreParam(filtro.getPgStampa().toString());
	param.setParamType("java.lang.Long");
	printbp.addToPrintSpoolerParam(param);
	
	context.addHookForward("close", this, "doStampaAnnullata");

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
private it.cnr.jada.util.action.SelectionListener getSelectionListener(
		ActionContext context,
		it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP aDocAmmBP,
		FatturaAttivaSingolaComponentSession faSession,
		Filtro_ricerca_doc_amm_ristampabileVBulk aFiltro) {

	final DocumentiAmministrativiRistampabiliBP docAmmProtocollabileBP = (DocumentiAmministrativiRistampabiliBP)context.getBusinessProcess();
	final it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP docAmmBP = aDocAmmBP;
	final FatturaAttivaSingolaComponentSession session = faSession;
	final Filtro_ricerca_doc_amm_ristampabileVBulk filtro = aFiltro;
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
				Filtro_ricerca_doc_amm_ristampabileVBulk nuovoFiltro = session.selezionaTuttiPerStampa(
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
					filtro.setPgStampa(session.callGetPgPerStampa(context.getUserContext()));
				}
				counter = session.modificaSelezionePerStampa(
					context.getUserContext(),
					instance,
					bulks,
					oldSelection,
					newSelection,
					null,
					counter,
					filtro.getPgStampa(),
					null);
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
