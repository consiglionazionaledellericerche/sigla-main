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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Codici_rapporti_inpsBulk;
import it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk;
import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.docamm00.actions.EconomicaAction;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.bp.CRUDMandatoBP;
import it.cnr.contab.doccont00.bp.CRUDReversaleBP;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.OptionBP;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.GregorianCalendar;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.08)
 *
 * @author: Roberto Fantino
 */
public class CRUDCompensoAction extends EconomicaAction {
    /**
     * CRUDCompensoAction constructor comment.
     */
    public CRUDCompensoAction() {
        super();
    }

    public Forward basicDoBringBackOpenObbligazioniWindow(ActionContext context, Obbligazione_scadenzarioBulk scadenza) {
        CRUDCompensoBP bp = null;
        try {
            if (scadenza == null)
                return context.findDefaultForward();
            bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            TerzoBulk creditore = scadenza.getObbligazione().getCreditore();
            if (!compenso.getTerzo().equalsByPrimaryKey(creditore) &&
                    !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita()))
                setMessage(context, FormBP.ERROR_MESSAGE, "La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore del compenso!");

            Obbligazione_scadenzarioBulk oldScad = compenso.getObbligazioneScadenzario();
            bp.elaboraScadenze(context, oldScad, scadenza);
            return context.findDefaultForward();
        } catch (Throwable t) {
            it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi defSaldiBulk = bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
            if (scadenza.getObbligazione().getPg_ver_rec().equals(scadenza.getObbligazione().getSaldiInfo().get("pg_ver_rec")))
                defSaldiBulk.removeFromDefferredSaldi(scadenza.getObbligazione());
            try {
                it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
            } catch (Exception e) {
                handleException(context, e);
            }
            return handleException(context, t);
        }
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
    protected Forward basicDoCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        try {
            fillModel(context);
            it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
            it.cnr.jada.bulk.OggettoBulk model = bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, model);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
                //} else if (ri.countElements() == 1) {
                //it.cnr.jada.bulk.OggettoBulk bulk = (it.cnr.jada.bulk.OggettoBulk)ri.nextElement();
                //return basicDoRiportaSelezione(context, bulk);
            } else {
                bp.setModel(context, model);
                it.cnr.jada.util.action.SelezionatoreListaBP nbp = (it.cnr.jada.util.action.SelezionatoreListaBP) context.createBusinessProcess("Selezionatore");
                nbp.setIterator(context, ri);
                nbp.setBulkInfo(bp.getBulkInfo());
                nbp.setColumns(getBusinessProcess(context).getSearchResultColumns());
                context.addHookForward("seleziona", this, "doRiportaSelezione");
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    private Forward basicDoEliminaCompenso(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.delete(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            CompensoComponentSession session = (CompensoComponentSession) bp.createComponentSession();
            if (session.isCompensoAnnullato(context.getUserContext(), compenso)) {
                bp.edit(context, compenso);
                bp.setMessage("Annullamento effettuato");
            } else {
                bp.reset(context);
                bp.setMessage("Cancellazione effettuata");
            }
            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doVerificaEsistenzaTrovato(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.ricercaDatiTrovato(context);
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
//	try {
//		CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
//		Fattura_passivaBulk fattura = (Fattura_passivaBulk)bp.getModel();
//		java.sql.Timestamp dataEmissione = fattura.getDt_fattura_fornitore();
//		try	{
//			fillModel( context );
//			if (!bp.isSearching())
//				fattura.validateDate();
//			creaEsercizioPerFatturaFornitore(context, fattura);
//			
//			return context.findDefaultForward();
//		} catch(Throwable e) {
//			fattura.setDt_fattura_fornitore(dataEmissione);
//			bp.setModel(context,fattura);
//			throw e;
//		}
//	} catch(Throwable e) {
//		return handleException(context, e);
//	}
    }

    private Forward basicDoLoadDocContAssociati(ActionContext context) throws BusinessProcessException {

        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        CompensoBulk compenso = (CompensoBulk) bp.getModel();

        if (!CompensoBulk.STATO_PAGATO.equals(compenso.getStato_cofi()) || compenso.getDocContAssociati() != null)
            return context.findDefaultForward();

        bp.loadDocContAssociati(context);
        return context.findDefaultForward();
    }

    /**
     * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
     */
    protected Forward basicDoRiportaSelezione(ActionContext context, it.cnr.jada.bulk.OggettoBulk selezione) throws java.rmi.RemoteException {

        try {
            if (selezione != null) {
                CRUDCompensoBP bp = (CRUDCompensoBP) context.getBusinessProcess();
                bp.edit(context, selezione);
                selezione = bp.getModel();
                //if (!CompensoBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((CompensoBulk)selezione).getRiportata()))
                //throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta riportato! Operazione annullata.");

                // Borriello: integrazione Err. CNR 775
                Integer esScriv = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

                if ((((CompensoBulk) selezione).getEsercizio().compareTo(esScriv) == 0) && ((CompensoBulk) selezione).isRiportata()) {
                    throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo risulta (parzialmente) riportato! Operazione annullata.");
                }

                //	Nuova gestione dello stato <code>getRiportata()</code>
                if ((((CompensoBulk) selezione).getEsercizio().compareTo(esScriv) != 0) && (!CompensoBulk.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(((CompensoBulk) selezione).getRiportataInScrivania()))) {
                    throw new it.cnr.jada.comp.ApplicationException("Il documento amministrativo non risulta completamente riportato! Operazione annullata.");
                }

                eliminaCompensoClone(context, (CompensoBulk) selezione);

                context.closeBusinessProcess();
                HookForward forward = (HookForward) context.findForward("bringback");
                forward.addParameter("documentoAmministrativoSelezionato", selezione);
                return forward;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    private Forward basicDoVisualizzaMandato(ActionContext context, MandatoBulk mandato) throws BusinessProcessException {

        CRUDMandatoBP mandatoBP = (CRUDMandatoBP) context.getUserInfo().createBusinessProcess(context, "CRUDMandatoBP", new Object[]{"VRSWTh"});
        mandatoBP.edit(context, mandato);

        return context.addBusinessProcess(mandatoBP);
    }

    private Forward basicDoVisualizzaMandatoReversale(ActionContext context, V_doc_cont_compBulk docCont) throws BusinessProcessException {

        Forward fwd = null;
        if (V_doc_cont_compBulk.TIPO_DOC_CONT_MANDATO.equals(docCont.getTipo_doc_cont()))
            fwd = basicDoVisualizzaMandato(context, (MandatoBulk) docCont.getManRev());
        if (V_doc_cont_compBulk.TIPO_DOC_CONT_REVERSALE.equals(docCont.getTipo_doc_cont()))
            fwd = basicDoVisualizzaReversale(context, (ReversaleBulk) docCont.getManRev());

        if (fwd == null)
            return context.findDefaultForward();
        return fwd;
    }

    private Forward basicDoVisualizzaReversale(ActionContext context, ReversaleBulk reversale) throws BusinessProcessException {

        CRUDReversaleBP reversaleBP = (CRUDReversaleBP) context.getUserInfo().createBusinessProcess(context, "CRUDReversaleBP", new Object[]{"VRSWTh"});
        reversaleBP.edit(context, reversale);

        return context.addBusinessProcess(reversaleBP);
    }

    public Forward doAnnullaModificaCORI(ActionContext context) {

        try {

            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.getContributiCRUDController().setModelIndex(context, -1);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Gestisce un comando "riporta".
     */
    public Forward doAnnullaRiporta(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.rollbackToSavePoint(context, "COMP_DA_DOC_AMM_UNDO_SP");

            return super.doAnnullaRiporta(context);
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    private void doAzzeraTipoRapporto(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            compenso.setTipiRapporto(null);
            compenso.setTipoRapporto(null);
            doAzzeraTipoTrattamento(context, compenso);
        }
    }

    private void doAzzeraTipoTrattamento(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            compenso.setTipiTrattamento(null);
            compenso.setTipoTrattamento(null);
            compenso.setTipiPrestazioneCompenso(null);
            compenso.setTipoPrestazioneCompenso(null);
            compenso.resetDatiLiquidazione();
        }
    }

    public Forward doBlankSearchFind_regione_irap(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setRegioneIrap(new RegioneBulk());
            compenso.setStatoCompensoToEseguiCalcolo();
            bp.setDirty(true);
        }
        return context.findDefaultForward();

    }

    public Forward doBlankSearchFind_terzo(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            V_terzo_per_compensoBulk v_terzo = new V_terzo_per_compensoBulk();
            compenso.setV_terzo(v_terzo);
            compenso.setCd_terzo(null);
            compenso.setNome(null);
            compenso.setCognome(null);
            compenso.setRagione_sociale(null);
            compenso.setPartita_iva(null);
            compenso.setCodice_fiscale(null);
            compenso.setPg_comune_add(null);
            compenso.setCd_provincia_add(null);
            compenso.setCd_regione_add(null);

            compenso.setTermini(null);
            compenso.setTerminiPagamento(null);
            compenso.setModalita(null);
            compenso.setModalitaPagamento(null);
            compenso.setBanca(null);
            compenso.setTipiRapporto(null);
            compenso.setTipoRapporto(null);
            compenso.setTipiTrattamento(null);
            compenso.setTipoTrattamento(null);
            compenso.setTipoPrestazioneCompenso(null);
            compenso.setCodici_rapporti_inps(null);
            compenso.setVisualizzaCodici_rapporti_inps(false);
            compenso.setCodici_attivita_inps(null);
            compenso.setVisualizzaCodici_attivita_inps(false);
            compenso.setCodici_altra_forma_ass_inps(null);
            compenso.setVisualizzaCodici_altra_forma_ass_inps(false);
            compenso.setComune_inps(null);
            compenso.setIncarichi_repertorio_anno(null);
            compenso.setContratto(null);
            compenso.setOggetto_contratto(null);

            compenso.setPignorato(null);
            compenso.setVisualizzaPignorato(false);

            compenso.setStatoCompensoToEseguiCalcolo();
        }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchCodici_rapporti_inps(ActionContext context, CompensoBulk compenso) {
        if (compenso != null) {
            compenso.setCodici_rapporti_inps(new Codici_rapporti_inpsBulk());
            compenso.setCodici_attivita_inps(null);
            compenso.setVisualizzaCodici_attivita_inps(false);
        }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchFind_tipologia_rischio(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setTipologiaRischio(new Tipologia_rischioBulk());
            compenso.setStatoCompensoToEseguiCalcolo();
            bp.setDirty(true);
        }
        return context.findDefaultForward();

    }

    public Forward doBlankSearchFind_voce_iva(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setVoceIva(new Voce_ivaBulk());
            compenso.setStatoCompensoToEseguiCalcolo();
            bp.setDirty(true);
        }
        return context.findDefaultForward();

    }

    public Forward doBringBackCORIDett(ActionContext context) {

        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        bp.getContributiCRUDController().getSelection().clear();
        bp.getContributiCRUDController().setModelIndex(context, -1);

        return context.findDefaultForward();
    }

    public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {

        try {
            HookForward caller = (HookForward) context.getCaller();
            Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("bringback");

            if (obblig != null)
                return basicDoBringBackOpenObbligazioniWindow(context, obblig);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doBringBackRicercaObbligazioniWindow(ActionContext context) {

        try {
            HookForward caller = (HookForward) context.getCaller();
            Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("obbligazioneSelezionata");

            return basicDoBringBackOpenObbligazioniWindow(context, obblig);

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doBringBackSearchFind_regione_irap(ActionContext context, CompensoBulk compenso, RegioneBulk reg) {

        if (reg != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setRegioneIrap(reg);
            compenso.setStatoCompensoToEseguiCalcolo();
            bp.setDirty(true);
        }

        return context.findDefaultForward();

    }

    public Forward doBringBackSearchFind_terzo(ActionContext context, CompensoBulk compenso, V_terzo_per_compensoBulk vTerzo) {

        try {

            if (vTerzo != null) {
                doBlankSearchFind_terzo(context, compenso);
                CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

                bp.completaTerzo(context, compenso, vTerzo);
                ((CompensoBulk) bp.getModel()).setStatoCompensoToEseguiCalcolo();
            }
            return context.findDefaultForward();

        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFind_tipologia_rischio(ActionContext context, CompensoBulk compenso, Tipologia_rischioBulk rischio) {

        if (rischio != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setTipologiaRischio(rischio);
            compenso.setStatoCompensoToEseguiCalcolo();
            bp.setDirty(true);
        }

        return context.findDefaultForward();

    }

    public Forward doBringBackSearchFind_voce_iva(ActionContext context, CompensoBulk compenso, Voce_ivaBulk voce) {

        if (voce != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setVoceIva(voce);
            compenso.setStatoCompensoToEseguiCalcolo();
            bp.setDirty(true);
        }

        return context.findDefaultForward();

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

        CRUDCompensoBP bp = (CRUDCompensoBP) context.getBusinessProcess();

        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP())
            return basicDoCerca(context);
        return super.doCerca(context);
    }

    public Forward doConfermaModificaCORI(ActionContext context) {

        try {

            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (compenso.isStatoCompensoEseguiCalcolo()) {
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "E' necessario eseguire il calcolo");
                return context.findDefaultForward();
            }
            fillModel(context);
            bp.doConfermaModificaCORI(context);
            impostaStatoCompenso(context);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Significato dei codici di errore:
     * --> 1 terzo non valido
     * --> 2 tipo rapporto non valido
     * --> 3 tipo trattamento non valido
     **/
    public Forward doConfermaModificaDataCompetenzaCoge(ActionContext context, OptionBP optionBP) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            java.sql.Timestamp oldDataCompCoge = (java.sql.Timestamp) optionBP.getAttribute("oldDataCompCoge");
            int errorCodeTerzo = ((Integer) optionBP.getAttribute("errorCodeTerzo")).intValue();

            if (optionBP.getOption() == OptionBP.YES_BUTTON) {
                compenso.setStatoCompensoToEseguiCalcolo();
                switch (errorCodeTerzo) {
                    case 6: {
                        doAzzeraTipoRapporto(context, compenso);
                        bp.findTipiRapporto(context);
                        break;
                    }
                }
            } else
                compenso.setDt_da_competenza_coge(oldDataCompCoge);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Significato dei codici di errore:
     * --> 1 terzo non valido
     * --> 2 tipo rapporto non valido
     * --> 3 tipo trattamento non valido
     **/
    public Forward doConfermaModificaDataRegistrazione(ActionContext context, OptionBP optionBP) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            java.sql.Timestamp oldDataReg = (java.sql.Timestamp) optionBP.getAttribute("dataReg");
            int errorCodeTerzo = ((Integer) optionBP.getAttribute("errorCodeTerzo")).intValue();

            if (optionBP.getOption() == OptionBP.YES_BUTTON) {
                compenso.setStatoCompensoToEseguiCalcolo();
                switch (errorCodeTerzo) {
                    case 2: {
                        doBlankSearchFind_terzo(context, compenso);
                        break;
                    }
                    case 8: {
                        doAzzeraTipoTrattamento(context, compenso);
                        bp.findTipiTrattamento(context);
                        break;
                    }
                }
            } else
                compenso.setDt_registrazione(oldDataReg);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Gestisce un comando "close".
     */
    public Forward doConfirmCloseForm(ActionContext context, int option) throws BusinessProcessException {

        if (option == OptionBP.YES_BUTTON) {

            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.rollbackToSavePoint(context, "COMP_DA_DOC_AMM_UNDO_SP");

            context.closeBusinessProcess();
            HookForward closeForward = (HookForward) context.findForward("close");
            if (closeForward != null)
                return closeForward;

        }
        return context.findDefaultForward();
    }

    public Forward doContabilizzaCompensoCOFI(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (bp.isDirty()) {
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Sono state effettuate delle modifiche. Prima di proseguire e' necessario salvare.");
                return context.findDefaultForward();
            }
            if (compenso.getFl_compenso_mcarriera_tassep()) {
                if (!compenso.getMinicarriera().getEsercizio().equals(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()))) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Poichè la Minicarriera è a tassazione separata, l'esercizio della Minicarriera deve essere uguale a quello del pagamento.");
                    return context.findDefaultForward();
                }
            }
            if (!compenso.getFl_compenso_mcarriera_tassep()) {
                if (!bp.isAccontoAddComOkPerContabil(context.getUserContext(), compenso)) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Per un corretto calcolo dell'Acconto dell'Addizionale Comunale, prima di proseguire e' necessario eseguire nuovamente il calcolo del compenso.");
                    compenso.setStatoCompensoToEseguiCalcolo();
                    return context.findDefaultForward();
                }
            }
            if (compenso.getTipoTrattamento().getFl_utilizzabile_art35()) {
                if (compenso.getPartita_iva() == null) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Per la tipologia di trattamento utilizzata è necessario che il Terzo abbia la Partita Iva (Art.35 DL n.223/2006).");
                    return context.findDefaultForward();
                }
                if (compenso.getCodice_fiscale() == null) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Per la tipologia di trattamento utilizzata è necessario che il Terzo abbia il Codice Fiscale valorizzato.");
                    return context.findDefaultForward();
                }
                if (!compenso.getFl_generata_fattura()) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Per la tipologia di trattamento utilizzata è necessaria la generazione della fattura (Art.35 DL n.223/2006).");
                    return context.findDefaultForward();
                }
                if (!bp.isCompensoValidoXContabil(context, compenso)) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Compenso non valido (Art.35 DL n.223/2006).");
                    return context.findDefaultForward();
                }
            }

            if (bp.isIncaricoRequired(context, compenso) && (compenso.getIncarichi_repertorio_anno() == null || compenso.getIncarichi_repertorio_anno().getCrudStatus() == OggettoBulk.UNDEFINED)) {
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Inserire il contratto.");
                return context.findDefaultForward();
            }

            if (compenso.isContrattoEnabled() && (compenso.getContratto() == null || compenso.getContratto().getCrudStatus() == OggettoBulk.UNDEFINED)) {
                setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Inserire il contratto.");
                return context.findDefaultForward();
            }

            if (!compenso.getFl_compenso_conguaglio()) {
                if (!bp.isSospensioneIrpefOkPerContabil(context.getUserContext(), compenso)) {
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Per un corretto calcolo della Sospensione IRPEF, prima di proseguire e' necessario eseguire nuovamente il calcolo del compenso.");
                    compenso.setStatoCompensoToEseguiCalcolo();
                    return context.findDefaultForward();
                }
            }
//		else
//		{
//			if (!bp.isSospensioneIrpefOkPerContabil(context.getUserContext(), compenso))
//			{
//				setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Per un corretto calcolo della Sospensione IRPEF, prima di proseguire e' necessario eliminare il conguaglio ed elaborarlo nuovamente.");
//				return context.findDefaultForward();
//			}
//		}	
            bp.contabilizzaCompensoCOFI(context);
            ((CompensoBulk) bp.getModel()).setStatoCompensoToContabilizzaCofi();

            setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Contabilizzazione eseguita in modo corretto.");

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doCreaObbligazione(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            context.addHookForward("bringback", this, "doBringBackOpenObbligazioniWindow");
            context.addHookForward("close", this, "doBringBackOpenObbligazioniWindow");

            it.cnr.contab.doccont00.bp.CRUDObbligazioneBP obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP) context.getUserInfo().createBusinessProcess(context, "CRUDObbligazioneBP", new Object[]{"MRSWTh"});
            obbligazioneBP.reset(context);

            ObbligazioneBulk obbligazione = (ObbligazioneBulk) obbligazioneBP.getModel();
            obbligazione.setStato_obbligazione(ObbligazioneOrdBulk.STATO_OBB_DEFINITIVO);
            obbligazione.setCreditore(compenso.getTerzo() != null ? compenso.getTerzo() : new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
            obbligazione.setDs_obbligazione("Impegno per compenso");
            obbligazione.setDt_registrazione(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
            obbligazione.setFl_calcolo_automatico(Boolean.TRUE);
            obbligazione.setIm_obbligazione(compenso.getImportoObbligazione());
            obbligazione.setRiportato("N");

            Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk(obbligazione);
            obbligazione.addToObbligazione_scadenzarioColl(scadenza);
            scadenza.setDs_scadenza("Scadenza per compenso");
            scadenza.setDt_scadenza(new java.sql.Timestamp(System.currentTimeMillis()));
            scadenza.setIm_scadenza(compenso.getImportoObbligazione());
            scadenza.setUser(obbligazione.getUser());
            scadenza.setToBeCreated();

            return context.addBusinessProcess(obbligazioneBP);
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

        try {
            fillModel(context);
        } catch (it.cnr.jada.bulk.FillException e) {
            return handleException(context, e);
        }

        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        return basicDoEliminaCompenso(context);
    }

    public Forward doEliminaObbligazione(ActionContext context) {

        try {

            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = bp.doEliminaObbligazione(context);

            if (compenso.getImportoObbligazione().compareTo(new java.math.BigDecimal(0)) <= 0)
                compenso.setStatoCompensoToObbligazioneSincronizzata();
            else
                compenso.setStatoCompensoToSincronizzaObbligazione();

            setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Impegno scollegato.");
            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Azione che si scatena al clicked del bottone ESEGUI CALCOLO
     * <p>
     * Chiama la Procedura Oracle e eseguo le seguenti operazioni:
     * 1. Importo Lordo <= 0 e non esiste obblig.
     * --> imposto STATO_COMPENSO_OBBLIGAZIONE_SINCORNIZZATA
     * 2. Importo Lordo <= 0 e esiste obblig.
     * --> Elimino obbligazione e imposto STATO_COMPENSO_OBBLIGAZIONE_SINCORNIZZATA
     * 3. Importo Lordo > 0 e esiste obblig. con importo uguale a importo lordo compenso
     * --> imposto STATO_COMPENSO_OBBLIGAZIONE_SINCORNIZZATA
     * 4. Importo Lordo > 0 e non esiste obblig. oppure obblig. ha importo diverso da importo lordo compenso
     * --> imposto STATO_COMPENSO_SINCORNIZZA_OBBLIGAZIONE
     **/

    public Forward doEseguiCalcolo(ActionContext context) {
        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.completeSearchTools(context, bp);
            bp.eseguiCalcolo(context);

            impostaStatoCompenso(context);

            setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Calcolo eseguito in modo corretto.");
            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doLoadDocContAssociati(ActionContext context) {

        try {

            return basicDoLoadDocContAssociati(context);

        } catch (BusinessProcessException ex) {
            return handleException(context, ex);
        }
    }

    public Forward doModificaAutomaticaObbligazione(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            fillModel(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            Obbligazione_scadenzarioBulk scadenza = compenso.getObbligazioneScadenzario();

            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita! Obbligazione inesistente");

            it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

            try {
                if (scadenza.getIm_scadenza().compareTo(compenso.getImportoObbligazione()) == 0)
                    compenso.setStatoCompensoToObbligazioneSincronizzata();

                scadenza = (Obbligazione_scadenzarioBulk) h.modificaScadenzaInAutomatico(context.getUserContext(), scadenza, compenso.getImportoObbligazione(), false);
                bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk().addToDefferredSaldi(
                        scadenza.getObbligazione(),
                        scadenza.getObbligazione().getSaldiInfo());
                compenso.setObbligazioneScadenzario(scadenza);
                if (scadenza.getObbligazione() != null && scadenza.getObbligazione().getContratto() != null) {
                    compenso.setCig(scadenza.getObbligazione().getContratto().getCig());
                } else {
                    compenso.setCig(null);
                }

                compenso.setStatoCompensoToObbligazioneSincronizzata();
                compenso.setStato_cofi(CompensoBulk.STATO_CONTABILIZZATO);

            } catch (it.cnr.jada.comp.ComponentException e) {
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.SfondamentoPdGException)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                throw e;
            }

            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doModificaManualeObbligazione(ActionContext context) {

        try {

            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            fillModel(context);

            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (compenso.isStatoCompensoEseguiCalcolo())
                throw new it.cnr.jada.comp.ApplicationException("E' necessario eseguire il calcolo prima di continuare.");

            Obbligazione_scadenzarioBulk scadenza = compenso.getObbligazioneScadenzario();
            boolean viewMode = bp.isViewing();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Obbligazione inesistente.");

            if (compenso.isROPerChiusura())
                viewMode = true;

            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, scadenza.getObbligazione(), status + "RSWTh");
            nbp.edit(context, scadenza.getObbligazione());
            nbp.selezionaScadenza(scadenza, context);

            context.addHookForward("bringback", this, "doBringBackOpenObbligazioniWindow");
            context.addHookForward("close", this, "doBringBackRicercaObbligazioniWindow");

            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(nbp);
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doOnCheckDisponibilitaCassaFailed(ActionContext context, int option) {

        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            try {
                boolean modified = fillModel(context);
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaDiCassaRequired(Boolean.FALSE);
                bp.setUserConfirm(userConfirmation);
                if (bp.isBringBack())
                    doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
                else
                    doSalva(context);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di una obbligazione che ha sfondato
     * la disponibilità per il contratto
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di sfondamento
     * @return <code>Forward</code>
     * @throws <code>RemoteException</code>
     */
    public Forward doOnCheckDisponibilitaContrattoFailed(ActionContext context, int option) {
        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            try {
                boolean modified = fillModel(context);
                OptionRequestParameter userConfirmation = new OptionRequestParameter();
                userConfirmation.setCheckDisponibilitaContrattoRequired(Boolean.FALSE);
                bp.setUserConfirm(userConfirmation);
                if (bp.isBringBack())
                    doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
                else
                    doSalva(context);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    public Forward doOnDtACompetenzaCogeChange(ActionContext context) {


        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        CompensoBulk compenso = (CompensoBulk) bp.getModel();
        java.sql.Timestamp oldDataCompCoge = compenso.getDt_a_competenza_coge();
        java.sql.Timestamp competenzaABck = compenso.getDt_a_competenza_coge();

        try {
            try {
                fillModel(context);
                if (bp.isSearching())
                    return context.findDefaultForward();

                if (compenso.getDt_a_competenza_coge() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Non è possibile annullare la data di competenza");

                GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
                GregorianCalendar old_data_a = (GregorianCalendar) GregorianCalendar.getInstance();
                data_a.setTime(((CompensoBulk) bp.getModel()).getDt_a_competenza_coge());
                if (oldDataCompCoge != null)
                    old_data_a.setTime(oldDataCompCoge);

                int annoCompetenzaA = data_a.get(java.util.GregorianCalendar.YEAR);
                int annoOldCompetenzaA = old_data_a.get(java.util.GregorianCalendar.YEAR);

                if (bp.isTerzoCervellone(context.getUserContext(), (CompensoBulk) bp.getModel()) &&
                        annoCompetenzaA != annoOldCompetenzaA) {
                    ((CompensoBulk) bp.getModel()).setDt_a_competenza_coge(oldDataCompCoge);
                    throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Non è possibile cambiare l'anno di competenza poichè il Terzo scelto potrebbe essere soggetto ad Agevolazioni per 'Rientro dei Cervelli'.");
                }
                //r.p. 20/10/2008 commentato perchè non vengono valorizzate in automatico data inizio e fine competenze
                //compenso.validaDate();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setDt_a_competenza_coge(oldDataCompCoge);
                throw e;
            } catch (it.cnr.jada.comp.ApplicationException e) {
                compenso.setDt_a_competenza_coge(oldDataCompCoge);
                throw e;
            }

            int errorCodeTerzo = bp.validaTerzo(context, false);
            if (errorCodeTerzo == 6) {
                String msg = null;
                switch (errorCodeTerzo) {
                    case 6: {
                        msg = "Il tipo rapporto selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
                        break;
                    }
                }

                OptionBP option = openConfirm(context, msg, OptionBP.CONFIRM_YES_NO, "doConfermaModificaDataCompetenzaCoge");
                option.addAttribute("oldDataCompCoge", oldDataCompCoge);
                option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
                return option;
            }

            ((CompensoBulk) bp.getModel()).setStatoCompensoToEseguiCalcolo();
            bp.findTipiRapporto(context);
            bp.ripristinaSelezioneTipoRapporto();

            java.sql.Timestamp CompetenzaA = compenso.getDt_a_competenza_coge();
            java.util.GregorianCalendar tsOdiernoGregorian = new GregorianCalendar();
            tsOdiernoGregorian.setTime(new Date(CompetenzaA.getTime()));

            Integer esercizioCompetenzaA = new Integer(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
            String cds = compenso.getCd_cds();

            if (((CompensoComponentSession) bp.createComponentSession()).isEsercizioChiusoPerDataCompetenza(context.getUserContext(), esercizioCompetenzaA, cds))
                throw new it.cnr.jada.comp.ApplicationException("Le date \"Competenza da\" e \"Competenza a\" non possono appartenere ad un esercizio chiuso");

            compenso.setStatoCompensoToEseguiCalcolo();
            return context.findDefaultForward();
        } catch (Throwable e) {
            compenso.setDt_a_competenza_coge(competenzaABck);
            return handleException(context, e);
        }
    }

    public Forward doOnDtDaCompetenzaCogeChange(ActionContext context) {
        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        CompensoBulk compenso = (CompensoBulk) bp.getModel();
        java.sql.Timestamp oldDataCompCoge = compenso.getDt_da_competenza_coge();
        java.sql.Timestamp competenzaDaBck = compenso.getDt_da_competenza_coge();
        try {

            fillModel(context);
            if (bp.isSearching())
                return context.findDefaultForward();
            if (compenso.getDt_da_competenza_coge() == null)
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile annullare la data di competenza");

            GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
            GregorianCalendar old_data_da = (GregorianCalendar) GregorianCalendar.getInstance();
            data_da.setTime(((CompensoBulk) bp.getModel()).getDt_da_competenza_coge());
            if (oldDataCompCoge != null)
                old_data_da.setTime(oldDataCompCoge);

            int annoCompetenzaDa = data_da.get(java.util.GregorianCalendar.YEAR);
            int annoOldCompetenzaDa = old_data_da.get(java.util.GregorianCalendar.YEAR);

            if (bp.isTerzoCervellone(context.getUserContext(), (CompensoBulk) bp.getModel()) &&
                    annoCompetenzaDa != annoOldCompetenzaDa) {
                ((CompensoBulk) bp.getModel()).setDt_da_competenza_coge(oldDataCompCoge);
                throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Non è possibile cambiare l'anno di competenza poichè il Terzo scelto potrebbe essere soggetto ad Agevolazioni per 'Rientro dei Cervelli'.");
            }
            //r.p. 20/10/2008 commentato perchè non vengono valorizzate in automatico data inizio e fine competenze
		/*try{
			((CompensoBulk)bp.getModel()).validaDate();
		} catch(it.cnr.jada.comp.ApplicationException e) {
			((CompensoBulk)bp.getModel()).setDt_da_competenza_coge(oldDataCompCoge);
			throw e;
		}*/

            int errorCodeTerzo = bp.validaTerzo(context, false);
            if (errorCodeTerzo == 6) {
                String msg = null;
                switch (errorCodeTerzo) {
                    case 6: {
                        msg = "Il tipo rapporto selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
                        break;
                    }
                }

                OptionBP option = openConfirm(context, msg, OptionBP.CONFIRM_YES_NO, "doConfermaModificaDataCompetenzaCoge");
                option.addAttribute("oldDataCompCoge", oldDataCompCoge);
                option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
                return option;
            }

            ((CompensoBulk) bp.getModel()).setStatoCompensoToEseguiCalcolo();
            bp.findTipiRapporto(context);
            bp.ripristinaSelezioneTipoRapporto();


            java.sql.Timestamp CompetenzaDa = compenso.getDt_da_competenza_coge();
            java.util.GregorianCalendar tsOdiernoGregorian = new GregorianCalendar();
            tsOdiernoGregorian.setTime(new Date(CompetenzaDa.getTime()));

            Integer esercizioCompetenzaDa = new Integer(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
            String cds = compenso.getCd_cds();

            if (((CompensoComponentSession) bp.createComponentSession()).isEsercizioChiusoPerDataCompetenza(context.getUserContext(), esercizioCompetenzaDa, cds))
                throw new it.cnr.jada.comp.ApplicationException("Le date \"Competenza da\" e \"Competenza a\" non possono appartenere ad un esercizio chiuso");


            return context.findDefaultForward();

        } catch (Throwable ex) {
            compenso.setDt_da_competenza_coge(competenzaDaBck);
            return handleException(context, ex);
        }
    }

    public Forward doOnDtRegistrazioneChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            java.sql.Timestamp oldDataReg = ((CompensoBulk) bp.getModel()).getDt_registrazione();
            fillModel(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            try {
                compenso.validaDate();
            } catch (it.cnr.jada.comp.ApplicationException e) {
                compenso.setDt_registrazione(oldDataReg);
                throw e;
            }

            int errorCodeTerzo = bp.validaTerzo(context, false);
            if (errorCodeTerzo == 2 || errorCodeTerzo == 8) {
                String msg = null;
                switch (errorCodeTerzo) {
                    case 2: {
                        msg = "Il terzo selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
                        break;
                    }
                    case 8: {
                        msg = "Il tipo trattamento selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
                        break;
                    }
                }

                OptionBP option = openConfirm(context, msg, OptionBP.CONFIRM_YES_NO, "doConfermaModificaDataRegistrazione");
                option.addAttribute("dataReg", oldDataReg);
                option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
                return option;
            }

            compenso.setStatoCompensoToEseguiCalcolo();
            bp.valorizzaInfoDocEle(context, compenso);
            compenso.resetDatiFattura();
            doAzzeraTipoTrattamento(context, compenso);
            bp.findTipiTrattamento(context);
            bp.ripristinaSelezioneTipoTrattamento(context);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doOnFlSenzaCalcoliChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();

            compenso.setStatoCompensoToEseguiCalcolo();
            doAzzeraTipoTrattamento(context, compenso);
            bp.findTipiTrattamento(context);
            compenso.setIncarichi_repertorio_anno(null);
            compenso.setContratto(null);
            compenso.setOggetto_contratto(null);
            if (!compenso.getFl_generata_fattura())
                compenso.resetDatiFattura();

            // Puo' valere TRUE solo se il compenso è senza calcoli
            if (!compenso.isSenzaCalcoli() && compenso.getFl_recupero_rate().booleanValue())
                compenso.setFl_recupero_rate(Boolean.FALSE);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doOnFlGenerataFatturaChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (!compenso.getFl_generata_fattura())
                compenso.resetDatiFattura();
            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }

    }

    public Forward doOnFlLiquidazioneDifferitaChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (compenso.getFl_liquidazione_differita() && compenso.getDt_fattura_fornitore() != null) {
                java.sql.Timestamp data_limite = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getDt01(context.getUserContext(), new Integer(0), "*", "COSTANTI", "LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
                java.sql.Timestamp data_limite_sup = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getDt02(context.getUserContext(), new Integer(0), "*", "COSTANTI", "LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
                if (compenso.getDt_fattura_fornitore().compareTo(data_limite) < 0 || compenso.getDt_fattura_fornitore().compareTo(data_limite_sup) > 0) {
                    compenso.setFl_liquidazione_differita(false);
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Non è possibile indicare la liquidazione differita con la data fattura fornitore indicata.");
                }
            }
            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }

    }

    public Forward doOnImLordoPercipienteChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getIm_lordo_percipiente();

            try {
                fillModel(context);
                if (Utility.nvl(compenso.getQuota_esente_inps()).compareTo(Utility.nvl(compenso.getIm_lordo_percipiente())) == 1) {
                    compenso.setIm_lordo_percipiente(oldImp);
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'importo Lordo non può superare la Quota esente INPS.");
                    return context.findDefaultForward();
                }
                compenso.setStatoCompensoToEseguiCalcolo();
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setIm_lordo_percipiente(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnQuotaEsenteINPSChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getQuota_esente_inps();

            try {
                fillModel(context);
                if (Utility.nvl(compenso.getQuota_esente_inps()).compareTo(Utility.nvl(compenso.getIm_lordo_percipiente())) == 1) {
                    compenso.setQuota_esente_inps(oldImp);
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "La Quota esente INPS non può superare l'importo lordo.");
                    return context.findDefaultForward();
                }
                compenso.setStatoCompensoToEseguiCalcolo();
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setQuota_esente_inps(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnImNoFiscaleChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getIm_no_fiscale();

            try {
                fillModel(context);
                compenso.setStatoCompensoToEseguiCalcolo();
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setIm_no_fiscale(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnImponibileInailChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getImponibile_inail();

            try {
                fillModel(context);
                compenso.setStatoCompensoToEseguiCalcolo();
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setImponibile_inail(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Alla selezione della Modalita di Pagamento ricerco le banche
     */

    public Forward doOnModalitaPagamentoChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            bp.findListaBanche(context);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doOnQuotaEsenteChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getQuota_esente();

            try {
                fillModel(context);
                compenso.setStatoCompensoToEseguiCalcolo();
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setQuota_esente(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnQuotaEsenteNoIvaChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getQuota_esente_no_iva();

            try {
                fillModel(context);
                compenso.setStatoCompensoToEseguiCalcolo();
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setQuota_esente_no_iva(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnTipoAnagraficoChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (bp.isSearching())
                return context.findDefaultForward();

            compenso.setStatoCompensoToEseguiCalcolo();
            return doBlankSearchFind_terzo(context, compenso);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doOnTipoIstituzCommercChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();
            compenso.setFl_liquidazione_differita(false);
            compenso.setStatoCompensoToEseguiCalcolo();
            doAzzeraTipoTrattamento(context, compenso);
            bp.findTipiTrattamento(context);
            compenso.setIncarichi_repertorio_anno(null);
            compenso.setContratto(null);
            compenso.setOggetto_contratto(null);
            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doOnStatoPagamentoFondoEcoChange(ActionContext context) {
        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (bp.isSearching())
                return context.findDefaultForward();
            compenso.setFl_liquidazione_differita(false);
            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }

    }

    /**
     * Alla selezione del Tipo Rapporto vengono caricati i relativi tipi trattamento
     */

    public Forward doOnTipoRapportoChange(ActionContext context) {

        try {
            fillModel(context);
            PostTipoRapportoChange(context);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Alla selezione del Tipo Trattamento imposta i dati relativi alla Liquidazione
     */

    public Forward doOnTipoTrattamentoChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (!compenso.getTipoTrattamento().getFl_visibile_a_tutti() && !UtenteBulk.isAbilitatoAllTrattamenti(context.getUserContext())) {
                doAzzeraTipoTrattamento(context, compenso);
                bp.findTipiTrattamento(context);
                throw new it.cnr.jada.comp.ApplicationException(
                        "Utente non abilitato all'utilizzo del trattamento selezionato!");
            }

            compenso.setTipoPrestazioneCompenso(null);
            compenso.setIncarichi_repertorio_anno(null);
            compenso.setImporto_utilizzato(null);
            compenso.setContratto(null);
            compenso.setOggetto_contratto(null);
            bp.findTipiPrestazioneCompenso(context);

            if (compenso.getTipoTrattamento() != null && compenso.getTipoTrattamento().getFl_pignorato_obbl()) {
                compenso.setVisualizzaPignorato(true);
                compenso.setPignorato(new TerzoBulk());
            } else {
                compenso.setPignorato(null);
                compenso.setVisualizzaPignorato(false);
            }

            bp.onTipoTrattamentoChange(context);

            ((CompensoBulk) bp.getModel()).setStatoCompensoToEseguiCalcolo();

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }


    public Forward doOnTipoPrestazioneCompensoChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            if (compenso.getTipoPrestazioneCompenso() == null) {
                compenso.setIncarichi_repertorio_anno(null);
                compenso.setImporto_utilizzato(null);
                compenso.setContratto(null);
                compenso.setOggetto_contratto(null);
            }
            if (compenso.getTipoPrestazioneCompenso() != null && !compenso.getTipoPrestazioneCompenso().getFl_incarico()) {
                compenso.setIncarichi_repertorio_anno(null);
                compenso.setImporto_utilizzato(null);
            }

            if (compenso.getTipoPrestazioneCompenso() != null && !compenso.getTipoPrestazioneCompenso().getFl_contratto()) {
                compenso.setContratto(null);
                compenso.setOggetto_contratto(null);
            }

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doBringBackSearchCodici_rapporti_inps(ActionContext context, CompensoBulk compenso, Codici_rapporti_inpsBulk codici_rapporti_inps) throws BusinessProcessException {

        if (codici_rapporti_inps != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setCodici_rapporti_inps(codici_rapporti_inps);
            bp.onTipoRapportoInpsChange(context);
            bp.setDirty(true);
        }
        return context.findDefaultForward();

    }
/*public Forward doOnTipoRapportoInpsChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDCompensoBP bp = (CRUDCompensoBP)getBusinessProcess(context);

		bp.onTipoRapportoInpsChange(context);

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}*/

    /**
     * Gestisce la creazione/ricerca di un'obbligazione
     */
    public Forward doRicercaObbligazione(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (compenso.getTerzo() == null)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un fornitore!");

            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
            filtro.setFornitore(compenso.getTerzo());
            filtro.setIm_importo(compenso.getImportoObbligazione());
            filtro.setCd_unita_organizzativa(compenso.getCd_unita_organizzativa());
            filtro.setFl_importo(Boolean.TRUE);
            filtro.setData_scadenziario(null);
            filtro.setFl_data_scadenziario(Boolean.FALSE);
            filtro.setDs_obbligazione("Impegno per compenso");
            filtro.setDs_scadenza("Scadenza per compenso");

            BulkBP robp = (BulkBP) context.getUserInfo().createBusinessProcess(context, "RicercaObbligazioniBP", new Object[]{"MRSWTh"});
            robp.setModel(context, filtro);
            context.addHookForward("bringback", this, "doBringBackRicercaObbligazioniWindow");

            return context.addBusinessProcess(robp);

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Riporta avanti". Ciò implica salvare il compenso,
     * riportare avanti l'obbligazione e committare
     */
    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {
        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

        try {
            fillModel(context);

            bp.salvaRiportandoAvanti(context);

            return context.findDefaultForward();
        } catch (it.cnr.jada.bulk.ValidationException e) {
            bp.setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "Riporta indietro". Ciò implica riportare
     * indietro l'obbligazione senza committare
     */

    public Forward doRiportaIndietro(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

            bp.riportaIndietro(context);

            return context.findDefaultForward();
        } catch (Throwable t) {
            return handleException(context, t);
        }

    }

    /**
     * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
     */
    public Forward doRiportaSelezione(ActionContext context) throws java.rmi.RemoteException {

        CRUDBP bp = (CRUDBP) context.getBusinessProcess();
        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP()) {
            HookForward caller = (HookForward) context.getCaller();
            it.cnr.jada.bulk.OggettoBulk selezione = (it.cnr.jada.bulk.OggettoBulk) caller.getParameter("focusedElement");
            return basicDoRiportaSelezione(context, selezione);
        }
        return super.doRiportaSelezione(context);
    }

    /**
     * Gestisce la selezione del bottone di ricerca delle Banche
     */

    public Forward doSearchListaBanche(ActionContext context) {

        CompensoBulk compenso = (CompensoBulk) getBusinessProcess(context).getModel();
        String columnSet = compenso.getModalitaPagamento().getTiPagamentoColumnSet();
        return search(context, getFormField(context, "main.listaBanche"), columnSet);
    }

    /**
     * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
     */
    public Forward doTab(ActionContext context, String tabName, String pageName) {

        try {

            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (!bp.isSearching()) {

                if ("tabCompenso".equalsIgnoreCase(bp.getTab(tabName))) {
                    compenso.validaTestata();
                }
                if ("tabCompensoTerzo".equalsIgnoreCase(pageName)) {
                    basicDoLoadDocContAssociati(context);
                }
                if ("tabCompensoDatiLiquidazione".equalsIgnoreCase(bp.getTab(tabName))) {
                }
                if ("tabCompensoContributiRitenute".equalsIgnoreCase(bp.getTab(tabName))) {
                    bp.getContributiCRUDController().setModelIndex(context, -1);
                }
                if ("tabCompensoObbligazioni".equalsIgnoreCase(bp.getTab(tabName))) {
                }
                if ("tabCompensoDocumentiAssociati".equalsIgnoreCase(pageName)) {
                    basicDoLoadDocContAssociati(context);
                }
            }

            return super.doTab(context, tabName, pageName);

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doVisualizzaDettagli(ActionContext context) {

        try {
            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

            Contributo_ritenutaBulk coriBulk = (Contributo_ritenutaBulk) bp.getContributiCRUDController().getModel();
            if (coriBulk == null)
                throw new MessageToUser("Selezionare un contributo/ritenuta valido");

            if (coriBulk.getDettagli().isEmpty())
                throw new MessageToUser("Il contributo/ritenuta selezionato non ha dettagli");


            it.cnr.jada.util.action.SelezionatoreListaBP slbp = select(
                    context,
                    new it.cnr.jada.util.ListRemoteIterator((java.util.List) coriBulk.getDettagli()),
                    it.cnr.jada.bulk.BulkInfo.getBulkInfo(Contributo_ritenuta_detBulk.class),
                    null,
                    "doBringBackCORIDett");
            slbp.setMultiSelection(false);
            slbp.disableSelection();
            return slbp;

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doVisualizzaDocContPrincipale(ActionContext context) {

        try {

            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            if (compenso == null)
                throw new MessageToUser("Non è stato creato il compenso");

            V_doc_cont_compBulk docContPrincipale = compenso.getDocContPrincipale();
            if (docContPrincipale == null)
                throw new MessageToUser("Non esiste il documento contabile principale");

            return basicDoVisualizzaMandatoReversale(context, docContPrincipale);

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doVisualizzaDocumentoContabile(ActionContext context) {

        try {

            fillModel(context);
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            V_doc_cont_compBulk docCont = (V_doc_cont_compBulk) bp.getDocContAssociatiCRUDController().getModel();

            if (docCont == null)
                throw new MessageToUser("Selezionare un Mandato o una Reversale");

            if (docCont.getManRev() == null)
                throw new MessageToUser("Non esiste il Mandato/Reversale");

            return basicDoVisualizzaMandatoReversale(context, docCont);

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    private void eliminaCompensoClone(ActionContext context, CompensoBulk compenso) throws Throwable {

        if (compenso != null) {
            Long pgCompensoClone = compenso.getPgCompensoPerClone();
            if (pgCompensoClone != null) {
                Long pgCompenso = compenso.getPg_compenso();
                CompensoComponentSession session = (CompensoComponentSession) ((CRUDCompensoBP) getBusinessProcess(context)).createComponentSession();
                session.eliminaCompensoTemporaneo(context.getUserContext(), compenso, pgCompensoClone);
                compenso.setPg_compenso(pgCompenso);
                compenso.setPgCompensoPerClone(null);
            }
        }
    }

    private void eliminaCompensoTemporaneo(ActionContext context, CompensoBulk compenso) throws Throwable {

        if (compenso != null && compenso.isTemporaneo()) {
            CompensoComponentSession session = (CompensoComponentSession) ((CRUDCompensoBP) getBusinessProcess(context)).createComponentSession();
            session.eliminaCompensoTemporaneo(context.getUserContext(), compenso, compenso.getPg_compenso());
        }
    }

    private void impostaStatoCompenso(ActionContext context) throws BusinessProcessException {

        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        CompensoBulk compenso = (CompensoBulk) bp.getModel();

        if (compenso.getImportoObbligazione().compareTo(new java.math.BigDecimal(0)) <= 0) {
            if (compenso.getObbligazioneScadenzario() != null)
                compenso = bp.doEliminaObbligazione(context);
            compenso.setStato_cofi(CompensoBulk.STATO_CONTABILIZZATO);
            compenso.setStatoCompensoToObbligazioneSincronizzata();
        } else if (compenso.getObbligazioneScadenzario() != null && compenso.getImportoObbligazione().compareTo(compenso.getObbligazioneScadenzario().getIm_scadenza()) == 0)
            compenso.setStatoCompensoToObbligazioneSincronizzata();
        else {
            compenso.setStatoCompensoToSincronizzaObbligazione();
            compenso.setStato_cofi(CompensoBulk.STATO_INIZIALE);
        }
    }

    /**
     * Gestisce un comando "riporta".
     */
    protected Forward riporta(ActionContext context, it.cnr.jada.bulk.OggettoBulk model) {

        try {
            eliminaCompensoClone(context, (CompensoBulk) model);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        return super.riporta(context, model);
    }

    public Forward doBringBackSearchIncarichi_repertorio_anno(ActionContext context, CompensoBulk compenso, Incarichi_repertorio_annoBulk incarico_anno) throws BusinessProcessException {
        if (incarico_anno != null)
            try {
                fillModel(context);
                CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);

                if (incarico_anno != null)
                    bp.validaIncaricoAnno(context, incarico_anno);

                if (compenso.getTipoRapporto() == null) {
                    compenso.impostaTipoRapporto(incarico_anno.getIncarichi_repertorio().getTipo_rapporto());
                    PostTipoRapportoChange(context);
                    //P.R. Reinizializzo l'oggetto perchè il metodo precedente ha risettato
                    //     il model
                    compenso = (CompensoBulk) bp.getModel();
                }
                if (compenso.getTipoTrattamento() == null)
                    bp.findTipiTrattamento(context);

                compenso.setIncarichi_repertorio_anno(incarico_anno);
                //compenso.setIncarichi_oggetto(incarico_anno.getIncarichi_repertorio().getOggetto());
                compenso.setImporto_utilizzato(bp.prendiUtilizzato(context, compenso, incarico_anno));
                if (compenso.getImporto_utilizzato().compareTo(compenso.getImporto_complessivo()) >= 0)
                    setMessage(context, FormBP.ERROR_MESSAGE, "Contratto già completamente utilizzato. Non sarà possibile completare la registrazione del compenso.");

                //bp.completaIncarico(context, compenso,incarico_anno);
                bp.setDirty(true);
            } catch (Throwable ex) {
                return handleException(context, ex);
            }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchIncarichi_repertorio_anno(ActionContext context, CompensoBulk compenso) {
        if (compenso != null) {
            compenso.setIncarichi_repertorio_anno(new Incarichi_repertorio_annoBulk());
        }
        return context.findDefaultForward();
    }

    public void PostTipoRapportoChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();

            //Azzero la quota esente INPS
            compenso.setQuota_esente_inps(new java.math.BigDecimal(0));

            doAzzeraTipoTrattamento(context, compenso);
            bp.findTipiTrattamento(context);
            compenso.setTipiPrestazioneCompenso(null);
            compenso.setTipoPrestazioneCompenso(null);
            compenso.setIncarichi_repertorio_anno(null);
            compenso.setContratto(null);
            compenso.setOggetto_contratto(null);
            //P.R. Reinizializzo l'oggetto perchè il metodo precedente ha risettato
            //     il model
            compenso = (CompensoBulk) bp.getModel();

            compenso.setStatoCompensoToEseguiCalcolo();

            compenso.setCodici_rapporti_inps(null);
            compenso.setVisualizzaCodici_rapporti_inps(false);
            compenso.setCodici_attivita_inps(null);
            compenso.setVisualizzaCodici_attivita_inps(false);
            compenso.setCodici_altra_forma_ass_inps(null);
            compenso.setVisualizzaCodici_altra_forma_ass_inps(false);
            compenso.setComune_inps(null);

            compenso.setPignorato(null);
            compenso.setVisualizzaPignorato(false);

        } catch (Throwable ex) {
            handleException(context, ex);
        }
    }

    public Forward doOnImNettoDaTrattenereChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            java.math.BigDecimal oldImp = compenso.getIm_netto_da_trattenere();

            try {
                fillModel(context);
                if (compenso.isStatoCompensoEseguiCalcolo()) {
                    compenso.setIm_netto_da_trattenere(oldImp);
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "E' necessario eseguire prima il calcolo.");
                    return context.findDefaultForward();
                }
                if (Utility.nvl(compenso.getIm_netto_da_trattenere()).compareTo(new BigDecimal(0)) < 0) {
                    compenso.setIm_netto_da_trattenere(new BigDecimal(0));
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'importo Netto da sospendere non può essere negativo.");
                    return context.findDefaultForward();
                }
                if (Utility.nvl(compenso.getIm_netto_percipiente()).compareTo(Utility.nvl(compenso.getIm_netto_da_trattenere())) < 0) {
                    compenso.setIm_netto_da_trattenere(oldImp);
                    setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "L'importo Netto da sospendere non può superare l'importo netto da pagare.");
                    return context.findDefaultForward();
                }
                return context.findDefaultForward();
            } catch (it.cnr.jada.bulk.FillException e) {
                compenso.setIm_netto_da_trattenere(oldImp);
                bp.setModel(context, compenso);
                throw e;
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnStatoLiquidazioneChange(ActionContext context) {
        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            fillModel(context);
            if (compenso.getStato_liquidazione() != null && compenso.getStato_liquidazione().equals(compenso.LIQ)) {
                if (compenso.getCausale() != null) {
                    compenso.setCausale(null);
                }
            } else if (compenso.getStato_liquidazione() != null && compenso.getStato_liquidazione().equals(compenso.SOSP)) {
                compenso.setCausale(compenso.ATTLIQ);
            } else if (compenso.getStato_liquidazione() != null && compenso.getStato_liquidazione().equals(compenso.NOLIQ)) {
                compenso.setCausale(compenso.CONT);
            }
            bp.setModel(context, compenso);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    public Forward doOnCausaleChange(ActionContext context) {
        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            CompensoBulk compenso = (CompensoBulk) bp.getModel();
            String oldCausale = compenso.getCausale();
            fillModel(context);
            if (compenso.getStato_liquidazione() != null && compenso.getStato_liquidazione().equals(compenso.LIQ)) {
                if (compenso.getCausale() != null) {
                    compenso.setCausale(null);
                    throw new it.cnr.jada.comp.ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            } else if (compenso.getStato_liquidazione() != null && compenso.getStato_liquidazione().equals(compenso.NOLIQ)) {

                if (compenso.getCausale() != null && !compenso.getCausale().equals(compenso.CONT)) {
					compenso.setCausale(oldCausale);
                    throw new it.cnr.jada.comp.ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            } else if (compenso.getStato_liquidazione() != null && compenso.getStato_liquidazione().equals(compenso.SOSP)) {
                if (compenso.getCausale() != null && (!compenso.getCausale().equals(compenso.ATTLIQ) && !compenso.getCausale().equals(compenso.CONT))) {
					compenso.setCausale(oldCausale);
                    throw new it.cnr.jada.comp.ApplicationException("Causale non valida, per lo stato della Liquidazione");
                }
            }

            bp.setModel(context, compenso);
        } catch (Throwable t) {
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    public Forward doBringBackCRUDCrea_cig(ActionContext context, CompensoBulk compenso, CigBulk cig) {
        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
        try {
            if (cig != null) {
                compenso.setCig(cig);
            }
            return context.findDefaultForward();
        } catch (it.cnr.jada.action.MessageToUser e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    @Override
    public Forward doSalva(ActionContext actioncontext) throws RemoteException {
        CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(actioncontext);
        if (bp.isViewing()) {
            setMessage(actioncontext, FormBP.ERROR_MESSAGE, "Nessuna modifica da salvare.");
        }
        return super.doSalva(actioncontext);
    }

    public Forward doBringBackSearchCig(ActionContext context, CompensoBulk compenso, CigBulk cig) throws BusinessProcessException {

        if (cig != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setCig(cig);
            bp.setDirty(true);
        } else {
            compenso.setCig(null);
        }
        return context.findDefaultForward();
    }

    public Forward doOnMotivoAssenzaCigChange(ActionContext context) {

        try {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            fillModel(context);
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchCig(ActionContext context, CompensoBulk compenso) {

        if (compenso != null) {
            CRUDCompensoBP bp = (CRUDCompensoBP) getBusinessProcess(context);
            compenso.setCig(null);
            bp.setDirty(true);
        }
        return context.findDefaultForward();

    }

}
