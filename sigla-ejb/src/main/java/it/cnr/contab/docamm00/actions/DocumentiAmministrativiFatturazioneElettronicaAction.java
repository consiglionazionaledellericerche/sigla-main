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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.bp.DocumentiAmministrativiFatturazioneElettronicaBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.IGenericSearchDocAmmBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.VDocammElettroniciAttiviBulk;
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
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:39:49 PM)
 *
 * @author: Roberto Peli
 */
public class DocumentiAmministrativiFatturazioneElettronicaAction extends ListaDocumentiAmministrativiAction {
    /**
     * DocumentiAmministrativiProtocollabiliAction constructor comment.
     */
    public DocumentiAmministrativiFatturazioneElettronicaAction() {
        super();
    }

    /**
     * Gestisce una richiesta di ricerca.
     * <p>
     * L'implementazione di default utilizza il metodo astratto <code>read</code>
     * di <code>CRUDBusinessProcess</code>.
     * Se la ricerca fornisce più di un risultato viene creato un
     * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
     * Al business process viene anche chiesto l'elenco delle colonne da
     * visualizzare.
     */
    public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {

        DocumentiAmministrativiFatturazioneElettronicaBP bp = (DocumentiAmministrativiFatturazioneElettronicaBP) context.getBusinessProcess();
        try {
            fillModel(context);
            completaSoggetto(context);
            Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro = (Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk) bp.getModel();

            OggettoBulk instance = (OggettoBulk) filtro.getInstance();
            Unita_organizzativaBulk unita_organizzativa = CNRUserInfo.getUnita_organizzativa(context);
            CompoundFindClause clauses = new CompoundFindClause();
            clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
            clauses.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
            clauses.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, unita_organizzativa.getCd_unita_organizzativa());
            clauses.addClause("AND", "protocollo_iva", SQLBuilder.ISNULL, null);
            clauses.addClause("AND", "protocollo_iva_generale", SQLBuilder.ISNULL, null);
            clauses.addClause("AND", "codiceUnivocoUfficioIpa", SQLBuilder.ISNOTNULL, null);
            if (filtro.isDaFirmare()) {
                clauses.addClause("AND", "statoInvioSdi", SQLBuilder.EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_ALLA_FIRMA);
            } else if (filtro.isFirmata()) {
                clauses.addClause("AND", "statoInvioSdi", SQLBuilder.NOT_EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_ALLA_FIRMA);
            }
            if (filtro.getCodiceUnivocoUfficioIpa() != null) {
                clauses.addClause("AND", "codiceUnivocoUfficioIpa", SQLBuilder.EQUALS, filtro.getCodiceUnivocoUfficioIpa());
            }
            clauses.addClause("AND", "dt_emissione", SQLBuilder.ISNULL, null);
            clauses.addClause("AND", "stato_cofi", SQLBuilder.NOT_EQUALS, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.STATO_ANNULLATO);

            filtro.setSQLClauses(clauses);

            it.cnr.jada.util.RemoteIterator ri = bp.find(context, clauses, instance);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
            } else {
                bp.setModel(context, filtro);

                IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, (IDocumentoAmministrativoBulk) instance);
                SelezionatoreListaBP nbp = (SelezionatoreListaBP) context.createBusinessProcess("Selezionatore", new Object[]{"Tn"});
                nbp.setMultiSelection(true);
                nbp.setIterator(context, ri);
                BulkInfo bulkInfo = BulkInfo.getBulkInfo(instance.getClass());
                nbp.setBulkInfo(bulkInfo);

                if (docAmmBP instanceof IGenericSearchDocAmmBP) {
                    String columnsetName = ((IGenericSearchDocAmmBP) docAmmBP).getColumnsetForGenericSearch();
                    if (columnsetName != null)
                        nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary(columnsetName));
                }
                context.addHookForward("seleziona", this, "doRiportaSelezione");
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            try {
                ((BusinessProcess) bp).rollbackUserTransaction();
            } catch (BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione dopo una richiesta di ricerca.
     * <p>
     * L'implementazione di default utilizza il metodo astratto <code>read</code>
     * di <code>CRUDBusinessProcess</code>.
     * Se la ricerca fornisce più di un risultato viene creato un
     * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
     * Al business process viene anche chiesto l'elenco delle colonne da
     * visualizzare.
     */
    public Forward doRiportaSelezione(ActionContext context) {

        DocumentiAmministrativiFatturazioneElettronicaBP bp = (DocumentiAmministrativiFatturazioneElettronicaBP) context.getBusinessProcess();
        try {
            Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro = (Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk) bp.getModel();
            IDocumentoAmministrativoBulk docAmm = (IDocumentoAmministrativoBulk) filtro.getInstance();
            if (docAmm instanceof Fattura_attivaBulk) {
                Fattura_attivaBulk fa = (Fattura_attivaBulk) docAmm;
                IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
                FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession) docAmmBP.createComponentSession();
            } else
                throw new it.cnr.jada.comp.ApplicationException("Documento amministrativo selezionato NON valido!");
        } catch (Exception e) {
            try {
                ((BusinessProcess) bp).rollbackUserTransaction();
            } catch (BusinessProcessException ex) {
                return handleException(context, ex);
            }
            return handleException(context, e);
        }

        return context.findDefaultForward();
    }

    /**
     * Gestisce la selezione dopo una richiesta di ricerca.
     * <p>
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
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doSearchSoggetto(ActionContext context) {
        try {
            DocumentiAmministrativiFatturazioneElettronicaBP bp = (DocumentiAmministrativiFatturazioneElettronicaBP) context.getBusinessProcess();
            Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro = (Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk) bp.getModel();
            IDocumentoAmministrativoBulk docAmm = filtro.getInstance();
            IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);

            if (!(docAmmBP instanceof IGenericSearchDocAmmBP) || ((IGenericSearchDocAmmBP) docAmmBP).getPropertyForGenericSearch() == null) {
                filtro.setSoggetto(null);
                throw new it.cnr.jada.comp.ApplicationException("Il soggetto non è una clausola valida per il gruppo selezionato!");
            }

            IGenericSearchDocAmmBP docAmmGenericSearchBP = (IGenericSearchDocAmmBP) docAmmBP;
            String property = docAmmGenericSearchBP.getPropertyForGenericSearch();
            it.cnr.contab.anagraf00.core.bulk.TerzoBulk soggetto = filtro.getSoggetto();
            if (soggetto == null)
                soggetto = basicDoBlankSearchSoggetto(context);
            return selectFromSearchResult(
                    context,
                    getFormField(context, "main.soggetto"),
                    bp.find(
                            context,
                            null,
                            soggetto,
                            bp.getModel(),
                            property),
                    "default");
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchSoggetto(ActionContext context,
                                             Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro,
                                             TerzoBulk soggettoTrovato)
            throws java.rmi.RemoteException {

        filtro.setSoggetto(soggettoTrovato);
        IDocumentoAmministrativoBulk docAmm = filtro.getInstance();
        if (docAmm != null) {
            try {
                IDocumentoAmministrativoBP docAmmBP = getBusinessProcessForDocAmm(context, docAmm);
                if (docAmmBP instanceof IGenericSearchDocAmmBP && ((IGenericSearchDocAmmBP) docAmmBP).getPropertyForGenericSearch() != null) {
                    IGenericSearchDocAmmBP genericSearchBP = (IGenericSearchDocAmmBP) docAmmBP;
                    Introspector.invoke(
                            docAmm,
                            Introspector.buildMetodName("set", genericSearchBP.getPropertyForGenericSearch()),
                            soggettoTrovato);
                }
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }


    /**
     * Gestisce la selezione dopo una richiesta di ricerca.
     * <p>
     * L'implementazione di default utilizza il metodo astratto <code>read</code>
     * di <code>CRUDBusinessProcess</code>.
     * Se la ricerca fornisce più di un risultato viene creato un
     * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
     * Al business process viene anche chiesto l'elenco delle colonne da
     * visualizzare.
     */

    private Forward doStampaProtocollati(
            ActionContext context,
            Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk filtro)
            throws BusinessProcessException {

        DocumentiAmministrativiFatturazioneElettronicaBP bp = (DocumentiAmministrativiFatturazioneElettronicaBP) context.getBusinessProcess();
        OfflineReportPrintBP printbp = (OfflineReportPrintBP) context.createBusinessProcess(bp.getPrintbp(), new Object[]{"Th"});
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
}
