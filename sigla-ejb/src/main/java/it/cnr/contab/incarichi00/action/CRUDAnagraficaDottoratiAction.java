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

package it.cnr.contab.incarichi00.action;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.bp.CRUDMinicarrieraBP;
import it.cnr.contab.compensi00.bp.MinicarrieraRataCRUDController;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.incarichi00.bp.CRUDAnagraficaDottoratiBP;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * /**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.08)
 *
 * @author: Roberto Fantino
 */

public class CRUDAnagraficaDottoratiAction extends it.cnr.jada.util.action.CRUDAction {
    /**
     * CRUDCompensoAction constructor comment.
     */
    public CRUDAnagraficaDottoratiAction() {
        super();
    }

    /**
     * Prepara la minicarriera per la ricerca di un nuovo percipiente
     */
    public Forward doBlankSearchFindTerzo(ActionContext context, AnagraficaDottoratiBulk anagraficaDottorati) {

        if (anagraficaDottorati != null) {
            anagraficaDottorati.setTerzo(new TerzoBulk());
            anagraficaDottorati.setPgBanca(null);
            anagraficaDottorati.setModalita(null);
            anagraficaDottorati.setTermini(null);
        }
        return context.findDefaultForward();

    }

    /**
     * Gestisce un HookForward di ritorno da un risultato di una ricerca del percipiente
     */

    public Forward doBringBackSearchFindTerzo(ActionContext context, AnagraficaDottoratiBulk anagraficaDottorati, TerzoBulk vTerzo) {
        try {
            if (vTerzo != null) {
                anagraficaDottorati.setTerzo(vTerzo);
                CRUDAnagraficaDottoratiBP bp = (CRUDAnagraficaDottoratiBP) getBusinessProcess(context);
                if (anagraficaDottorati != null) {
                    anagraficaDottorati.setCdTerzo(vTerzo.getCd_terzo());
                    /**anagraficaDottorati.setNome(vTerzo.getAnagrafico().getNome());
                    anagraficaDottorati.setCognome(vTerzo.getAnagrafico().getCognome());
                    anagraficaDottorati.setRagioneSociale(vTerzo.getAnagrafico().getRagione_sociale());
                    anagraficaDottorati.setPartitaIva(vTerzo.getPartita_iva_anagrafico());
                    anagraficaDottorati.setCodiceFiscale(vTerzo.getCodice_fiscale_anagrafico());*/
                    bp.completaTerzo(context, anagraficaDottorati, vTerzo);
                }
            }
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Inoltro la richiesta alla stored procedure per la generazione delle rate
     * Il modello deve essere prima validato dal metodo 'validate'
     */

    public Forward doGeneraRate(ActionContext context) {

        try {
            fillModel(context);
            CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP) getBusinessProcess(context);
            MinicarrieraBulk carriera = (MinicarrieraBulk) bp.getModel();
            carriera.validate();
            if (!carriera.isNonAssociataACompenso())
                throw new it.cnr.jada.comp.ApplicationException("Almeno una rata ha già generato un compenso. Impossibile rigenerare le rate.");

            bp.generaRate(context);
            bp.setDirty(true);
            setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Creazione delle rate eseguita in maniera corretta.");

            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la richiesta di cambiamento della modalità di pagamento e ricerca le
     * banche valide
     */

    public Forward doOnModalitaPagamentoChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDAnagraficaDottoratiBP bp = (CRUDAnagraficaDottoratiBP) getBusinessProcess(context);
            bp.findListaBanche(context);

            return context.findDefaultForward();

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Gestisce la richiesta di cambiamento del tipo di anagrafico della
     * minicarriera. Inoltre essa viene preparata per una nuova ricerca
     * del percipiente
     */

    public Forward doOnTipoAnagraficoChange(ActionContext context) {

        try {
            fillModel(context);
            CRUDAnagraficaDottoratiBP bp = (CRUDAnagraficaDottoratiBP) getBusinessProcess(context);
            AnagraficaDottoratiBulk carriera = (AnagraficaDottoratiBulk) bp.getModel();

            if (!bp.isSearching())
                return doBlankSearchFindTerzo(context, carriera);
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la richiesta di cancellazione di una o piu' rate
     */

    public Forward doRemoveFromCRUDMain_rateCRUDController(ActionContext context) {

        CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP) context.getBusinessProcess();
        MinicarrieraRataCRUDController rateController = bp.getRateCRUDController();
        it.cnr.jada.util.action.Selection selection = rateController.getSelection();
        try {
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare le rate che si desidera eliminare!");
        } catch (it.cnr.jada.comp.ApplicationException e) {
            return handleException(context, e);
        }

        try {
            rateController.remove(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        return context.findDefaultForward();
    }

    /**
     * Gestisce la ricerca delle banche valide per la modalità di pagamento selezionata
     */

    public Forward doSearchListaBanche(ActionContext context) {

        MinicarrieraBulk carriera = (MinicarrieraBulk) getBusinessProcess(context).getModel();
        String columnSet = carriera.getModalita_pagamento().getTiPagamentoColumnSet();
        return search(context, getFormField(context, "main.listaBanche"), columnSet);
    }

}
