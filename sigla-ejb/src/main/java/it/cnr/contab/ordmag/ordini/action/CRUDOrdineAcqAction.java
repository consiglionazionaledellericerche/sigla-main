/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.*;

import javax.persistence.PersistenceException;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bp.CRUDConfigAnagContrattoBP;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.TitoloDiCreditoDebitoBP;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.CategoriaGruppoInventComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.config00.contratto.bulk.Dettaglio_contrattoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;

public class CRUDOrdineAcqAction extends it.cnr.jada.util.action.CRUDAction {

    public CRUDOrdineAcqAction() {
        super();
    }

    ///**
// * Gestisce una richiesta di ricerca del searchtool "sospeso"
// *
// * @param context	L'ActionContext della richiesta
// * @param doc	L'OggettoBulk padre del searchtool
// * @param sospesoTrovato	L'OggettoBulk selezionato dall'utente
// * @return Il Forward alla pagina di risposta
// * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
// */
    public Forward doBringBackSearchFindUnitaMisura(ActionContext context,
                                                    OrdineAcqRigaBulk riga,
                                                    UnitaMisuraBulk unitaMisura)
            throws java.rmi.RemoteException {

        riga.setUnitaMisura(unitaMisura);
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);

        try {
            if (unitaMisura != null && riga.getBeneServizio() != null && riga.getBeneServizio().getUnitaMisura() != null && unitaMisura.getCdUnitaMisura().equals(riga.getBeneServizio().getUnitaMisura().getCdUnitaMisura())) {
                riga.setCoefConv(BigDecimal.ONE);
            } else {
                riga.setCoefConv(null);
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFindNumerazioneOrd(ActionContext context,
                                                       OrdineAcqBulk ordine,
                                                       NumerazioneOrdBulk num)
            throws java.rmi.RemoteException {

        ordine.setNumerazioneOrd(num);
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);

        try {
            if (num != null) {
                ordine.setTiAttivita(num.getTi_istituz_commerc());
                ordine.setPercProrata(num.getPercProrata());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchFind_contratto(ActionContext context, OrdineAcqBulk ordine) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            ordine.setContratto(new ContrattoBulk());
            ordine.setResponsabileProcPers(null);
            ordine.setCup(null);
            ordine.setCig(null);
            ordine.setDirettorePers(null);
            ordine.setFirmatarioPers(null);
            ordine.setProcedureAmministrative(null);
            ordine.setTerzoCdr(null);
            ordine.setReferenteEsterno(null);
            ordine.setFl_mepa(false);
            // cancella anche il fornitore
            ordine.setFornitore(new TerzoBulk());
            ordine.setRagioneSociale(null);
            ordine.setCodiceFiscale(null);
            ordine.setPartitaIva(null);
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackCRUDFind_contratto(ActionContext context,
                                                 OrdineAcqBulk ordine,
                                                 ContrattoBulk contratto)
            throws java.rmi.RemoteException {
        return doBringBackSearchFind_contratto(context, ordine, contratto);
    }

    public Forward doBringBackSearchFind_contratto(ActionContext context,
                                                   OrdineAcqBulk ordine,
                                                   ContrattoBulk contratto)
            throws java.rmi.RemoteException {

        ordine.setContratto(contratto);
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        if (contratto != null) {
            ordine.setResponsabileProcPers(contratto.getResponsabile());
            ordine.setCup(contratto.getCup());
            ordine.setCig(contratto.getCig());
            ordine.setDirettorePers(contratto.getDirettore());
            ordine.setFirmatarioPers(contratto.getFirmatario());
            ordine.setProcedureAmministrative(contratto.getProcedura_amministrativa());
            ordine.setTerzoCdr(contratto.getFigura_giuridica_interna());
            ordine.setFl_mepa(contratto.getFl_mepa());
            ordine.setReferenteEsterno(contratto.getResp_esterno());
            if (ordine.getFornitore() == null || (ordine.getFornitore() != null && ordine.getFornitore().getCd_terzo() == null))
                ordine.setFornitore(contratto.getFigura_giuridica_esterna());
            doBringBackSearchFindFornitore(context, ordine, contratto.getFigura_giuridica_esterna());
        }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchFindBeneServizio(ActionContext context, OrdineAcqRigaBulk riga) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            riga.setBeneServizio(new Bene_servizioBulk());
            riga.setUnitaMisura(null);
            riga.setCoefConv(null);
            riga.setDspTipoConsegna(null);
            riga.setDspConto(null);
            riga.setPrezzoUnitario(null);
            riga.setTipoConsegnaDefault(null);
            riga.setVoceIva(null);
            riga.setDettaglioContratto(null);
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFindBeneServizio(ActionContext context,
                                                     OrdineAcqRigaBulk riga,
                                                     Bene_servizioBulk bene)
            throws java.rmi.RemoteException {

        riga.setBeneServizio(bene);
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        if (bene != null) {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            riga.setDsBeneServizio(bene.getDs_bene_servizio());
            riga.setCdBeneServizio(bene.getCd_bene_servizio());
            ContrattoBulk contrattoBulk = riga.getOrdineAcq().getContratto();
            if (contrattoBulk != null) {
                try {
                    if (contrattoBulk.isDettaglioContrattoPerArticoli() || contrattoBulk.isDettaglioContrattoPerCategoriaGruppo() ){
                        Dettaglio_contrattoBulk dettaglio_contrattoBulk = bp.recuperoDettaglioContratto(context, riga);
                        riga.setDettaglioContratto(dettaglio_contrattoBulk);
                    }
                    if (contrattoBulk.isDettaglioContrattoPerArticoli()){
                        riga.setUnitaMisura(riga.getDettaglioContratto().getUnitaMisura());
                        riga.setCoefConv(riga.getDettaglioContratto().getCoefConv());
                        riga.setPrezzoUnitario(riga.getDettaglioContratto().getPrezzoUnitario());
                    } else {
                        if (bene.getUnitaMisura() != null) {
                            riga.setUnitaMisura(bene.getUnitaMisura());
                            riga.setCoefConv(BigDecimal.ONE);
                        }
                    }
                } catch (BusinessProcessException e) {
                    handleException(context, e);
                }
            } else {
                if (bene.getUnitaMisura() != null) {
                    riga.setUnitaMisura(bene.getUnitaMisura());
                    riga.setCoefConv(BigDecimal.ONE);
                }
            }
            if (bene.getTipoGestione() != null) {
                riga.setDspTipoConsegna(bene.getTipoGestione());
                riga.setTipoConsegnaDefault(bene.getTipoGestione());
            }
            if (bene.getVoce_iva() != null) {
                riga.setVoceIva(bene.getVoce_iva());
            }
            if (bene.getCategoria_gruppo() != null) {
                try {
                    ContoBulk conto = bp.recuperoContoDefault(context, bene.getCategoria_gruppo());
                    riga.setDspConto(conto);
                } catch (BusinessProcessException e) {
                    handleException(context, e);
                }
            }
        }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchFindObbligazioneScadenzario(ActionContext context, OrdineAcqConsegnaBulk cons) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            if (cons.getObbligazioneScadenzario() != null && cons.getObbligazioneScadenzario().getPg_obbligazione() != null) {
                ordine.removeFromOrdineObbligazioniHash(cons);
                cons.setObbligazioneScadenzario(new Obbligazione_scadenzarioBulk());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFindObbligazioneScadenzario(ActionContext context,
                                                                OrdineAcqConsegnaBulk cons,
                                                                Obbligazione_scadenzarioBulk obblScad)
            throws java.rmi.RemoteException {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        cons.setObbligazioneScadenzario(obblScad);
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        if (obblScad != null) {
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            ordine.addToOrdineObbligazioniHash(obblScad, cons);

            cons.setObbligazioneInseritaSuConsegna(true);
        }
        return context.findDefaultForward();
    }

    public Forward doBringBackSearchFindMagazzino(ActionContext context,
                                                  OrdineAcqRigaBulk riga,
                                                  MagazzinoBulk magazzino)
            throws java.rmi.RemoteException {

        riga.setDspMagazzino(magazzino);
        gestioneConsegnaNonPresente(riga);
        for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
            OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
            consegna.setMagazzino(riga.getDspMagazzino());
            consegna.setToBeUpdated();
        }
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        if (magazzino != null) {
            riga.setDspLuogoConsegna(magazzino.getLuogoConsegnaMag());
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setLuogoConsegnaMag(riga.getDspLuogoConsegna());
                consegna.setToBeUpdated();
            }
        }
        return context.findDefaultForward();
    }

    public Forward doBringBackSearchFindMagazzino(ActionContext context,
                                                  OrdineAcqConsegnaBulk cons,
                                                  MagazzinoBulk magazzino)
            throws java.rmi.RemoteException {

        cons.setMagazzino(magazzino);
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
        if (riga.getRigheConsegnaColl().size() == 1) {
            riga.setDspMagazzino(cons.getMagazzino());
        }
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        if (magazzino != null) {
            cons.setLuogoConsegnaMag(magazzino.getLuogoConsegnaMag());
            if (riga.getRigheConsegnaColl().size() == 1) {
                riga.setDspLuogoConsegna(cons.getLuogoConsegnaMag());
            }
        }
        return context.findDefaultForward();
    }

    public Forward doBlankSearchFindMagazzino(ActionContext context, OrdineAcqRigaBulk riga) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            riga.setDspMagazzino(new MagazzinoBulk());
            riga.setDspLuogoConsegna(new LuogoConsegnaMagBulk());
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setMagazzino(riga.getDspMagazzino());
                consegna.setLuogoConsegnaMag(riga.getDspLuogoConsegna());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }


    public Forward doBlankSearchFindMagazzino(ActionContext context, OrdineAcqConsegnaBulk cons) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            cons.setMagazzino(new MagazzinoBulk());
            cons.setLuogoConsegnaMag(new LuogoConsegnaMagBulk());
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                riga.setDspMagazzino(cons.getMagazzino());
                riga.setDspLuogoConsegna(cons.getLuogoConsegnaMag());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchCercaConto(ActionContext context, OrdineAcqConsegnaBulk cons) throws java.rmi.RemoteException {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            cons.setContoBulk(new ContoBulk());
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                riga.setDspConto(cons.getContoBulk());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchCercaConto(ActionContext context, OrdineAcqConsegnaBulk cons, ContoBulk contoBulk) throws java.rmi.RemoteException {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            cons.setContoBulk(contoBulk);
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                riga.setDspConto(cons.getContoBulk());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchCercaDspConto(ActionContext context, OrdineAcqRigaBulk riga) throws java.rmi.RemoteException {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            riga.setDspConto(new ContoBulk());
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setContoBulk(riga.getDspConto());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchCercaDspConto(ActionContext context, OrdineAcqRigaBulk rigaBulk, ContoBulk contoBulk) throws java.rmi.RemoteException {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            rigaBulk.setDspConto(contoBulk);
            for (java.util.Iterator j = rigaBulk.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setContoBulk(rigaBulk.getDspConto());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }


    public Forward doBringBackSearchFindUnitaOperativaOrdDest(ActionContext context,
                                                              OrdineAcqRigaBulk riga,
                                                              UnitaOperativaOrdBulk uop)
            throws java.rmi.RemoteException {

        riga.setDspUopDest(uop);
        gestioneConsegnaNonPresente(riga);
        for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
            OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
            consegna.setUnitaOperativaOrd(riga.getDspUopDest());
            consegna.setToBeUpdated();
        }
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        return context.findDefaultForward();
    }

    public Forward doBringBackSearchFindUnitaOperativaOrdDest(ActionContext context,
                                                              OrdineAcqConsegnaBulk cons,
                                                              UnitaOperativaOrdBulk uop)
            throws java.rmi.RemoteException {

        cons.setUnitaOperativaOrd(uop);
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
        if (riga.getRigheConsegnaColl().size() == 1) {
            riga.setDspUopDest(cons.getUnitaOperativaOrd());
        }
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        return context.findDefaultForward();
    }

    public Forward doBlankSearchFindUnitaOperativaOrdDest(ActionContext context, OrdineAcqRigaBulk riga) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            riga.setDspUopDest(new UnitaOperativaOrdBulk());
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setUnitaOperativaOrd(riga.getDspUopDest());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchFindUnitaOperativaOrdDest(ActionContext context, OrdineAcqConsegnaBulk cons) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            cons.setUnitaOperativaOrd(new UnitaOperativaOrdBulk());
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                riga.setDspUopDest(cons.getUnitaOperativaOrd());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFindLuogoConsegnaMag(ActionContext context,
                                                         OrdineAcqRigaBulk riga,
                                                         LuogoConsegnaMagBulk luogo)
            throws java.rmi.RemoteException {

        riga.setDspLuogoConsegna(luogo);
        gestioneConsegnaNonPresente(riga);
        for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
            OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
            consegna.setLuogoConsegnaMag(riga.getDspLuogoConsegna());
            consegna.setToBeUpdated();
        }
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        return context.findDefaultForward();
    }

    public Forward doBringBackSearchFindLuogoConsegnaMag(ActionContext context,
                                                         OrdineAcqConsegnaBulk cons,
                                                         LuogoConsegnaMagBulk luogo)
            throws java.rmi.RemoteException {

        cons.setLuogoConsegnaMag(luogo);
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
        if (riga.getRigheConsegnaColl().size() == 1) {
            riga.setDspLuogoConsegna(cons.getLuogoConsegnaMag());
        }
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        return context.findDefaultForward();
    }

    public Forward doBlankSearchFindLuogoConsegnaMag(ActionContext context, OrdineAcqRigaBulk riga) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            riga.setDspLuogoConsegna(new LuogoConsegnaMagBulk());
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setLuogoConsegnaMag(riga.getDspLuogoConsegna());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchFindLuogoConsegnaMag(ActionContext context, OrdineAcqConsegnaBulk cons) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            cons.setLuogoConsegnaMag(new LuogoConsegnaMagBulk());
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                riga.setDspLuogoConsegna(cons.getLuogoConsegnaMag());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchFindUnitaOperativaOrd(ActionContext context, OrdineAcqBulk ordine) throws java.rmi.RemoteException {

        try {
            //imposta i valori di default per il tariffario
            ordine.setUnitaOperativaOrd(new UnitaOperativaOrdBulk());
            ordine.setNumerazioneOrd(new NumerazioneOrdBulk());
            ordine.setTiAttivita(null);
            ordine.setPercProrata(null);
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFindUnitaOperativaOrd(ActionContext context,
                                                          OrdineAcqBulk ordine,
                                                          UnitaOperativaOrdBulk uop)
            throws java.rmi.RemoteException {

        ordine.setUnitaOperativaOrd(uop);
        ((CRUDBP) context.getBusinessProcess()).setDirty(true);
        if (uop != null) {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();

            try {
                OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
                h.completaOrdine(context.getUserContext(), ordine);
                try {
                    bp.setModel(context, ordine);
                } catch (BusinessProcessException e) {
                }
            } catch (BusinessProcessException e) {
                return handleException(context, e);
            } catch (java.rmi.RemoteException e) {
                return handleException(context, e);
            } catch (PersistenceException e) {
                return handleException(context, e);
            } catch (PersistencyException e) {
                return handleException(context, e);
            } catch (ComponentException e) {
                return handleException(context, e);
            }
        }
//		try{
//			if (riga.getUnitaMisura()!=null && riga.getUnitaMisura().getCdUnitaMisura()!=null && riga.getBeneServizio() != null && riga.getBeneServizio().getUnitaMisura() != null && riga.getUnitaMisura().getCdUnitaMisura().equals(riga.getBeneServizio().getUnitaMisura().getCdUnitaMisura())) {
//				riga.setCoefConv(BigDecimal.ONE);
//			} else {
//				riga.setCoefConv(null);
//			}
//			return context.findDefaultForward();
//
//		} catch(Exception e) {
//			return handleException(context,e);
//		}
        return context.findDefaultForward();
    }

    public Forward doTab(ActionContext context, String tabName, String pageName) {
        try {
            fillModel(context);
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();

            if (ordine == null)
                return super.doTab(context, tabName, pageName);

            if (bp.isEditable() && !bp.isSearching() && bp.getTab(tabName).equalsIgnoreCase("tabOrdineAcq")) {
            }
            if ("tabOrdineAllegati".equalsIgnoreCase(bp.getTab(tabName))) {
                fillModel(context);
            }
            if ("tabOrdineObbligazioni".equalsIgnoreCase(bp.getTab(tabName))) {
                try {
                    fillModel(context);
                    if (!bp.isSearching())
                        controllaQuadraturaObbligazioni(context, ordine);
                } catch (it.cnr.jada.comp.ApplicationException e) {
                    bp.setErrorMessage(e.getMessage());
                }
            }
            return super.doTab(context, tabName, pageName);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private void controllaQuadraturaObbligazioni(ActionContext context, OrdineAcqBulk ordine)
            throws it.cnr.jada.comp.ComponentException {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);

        try {
            OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
            h.controllaQuadraturaObbligazioni(context.getUserContext(), ordine);
        } catch (PersistencyException e) {
            bp.handleException(e);
        } catch (java.rmi.RemoteException e) {
            bp.handleException(e);
        } catch (BusinessProcessException e) {
            bp.handleException(e);
        }
    }

    @Override
    public Forward doSalva(ActionContext actioncontext) throws RemoteException {
        try {
            fillModel(actioncontext);
            gestioneSalvataggio(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (ValidationException validationexception) {
            getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
        return actioncontext.findDefaultForward();
    }

    private void gestioneSalvataggio(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) actioncontext.getBusinessProcess();
        bp.getObbligazioniController().setModelIndex(actioncontext, -1);
        getBusinessProcess(actioncontext).save(actioncontext);
        postSalvataggio(actioncontext);
    }

    public Forward doSalvaDefinitivo(ActionContext actioncontext) throws RemoteException {
        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(actioncontext);
            fillModel(actioncontext);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            if (ordine.isStatoDefinitivo()) {
                ordine.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
                java.sql.Timestamp dataReg = null;
                try {
                    dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
                } catch (javax.ejb.EJBException e) {
                    throw new it.cnr.jada.DetailedRuntimeException(e);
                }

                ordine.setDataOrdineDef(dataReg);
            }
            if (ordine.isStatoInserito()) {
                try {
                    OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
                    if (h.isUtenteAbilitatoValidazioneOrdine(actioncontext.getUserContext(), ordine)) {
//					ordine.setStato(OrdineAcqBulk.STATO_INVIATA_ORDINE);
                    } else {
                        ordine.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
                        java.sql.Timestamp dataReg = null;
                        try {
                            dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
                        } catch (javax.ejb.EJBException e) {
                            throw new it.cnr.jada.DetailedRuntimeException(e);
                        }

                        ordine.setDataOrdineDef(dataReg);
                    }
                    try {
                        bp.setModel(actioncontext, ordine);
                    } catch (BusinessProcessException e) {
                    }
                } catch (java.rmi.RemoteException e) {
                    return handleException(actioncontext, e);
                } catch (PersistenceException e) {
                    return handleException(actioncontext, e);
                }

            }
            gestioneSalvataggio(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (ValidationException validationexception) {
            getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
        return actioncontext.findDefaultForward();
    }

    protected void postSalvataggio(ActionContext context) throws BusinessProcessException {
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
        bp.gestionePostSalvataggio(context);
    }

    public Forward doBringBackSearchFindFornitore(ActionContext context,
                                                  OrdineAcqBulk ordine,
                                                  TerzoBulk fornitoreTrovato)
            throws java.rmi.RemoteException {

        try {
            if (fornitoreTrovato != null) {
                ordine.setFornitore(fornitoreTrovato);
                ordine.setNome(fornitoreTrovato.getAnagrafico().getNome());
                ordine.setCognome(fornitoreTrovato.getAnagrafico().getCognome());
                ordine.setRagioneSociale(fornitoreTrovato.getAnagrafico().getRagione_sociale());
                ordine.setCodiceFiscale(fornitoreTrovato.getAnagrafico().getCodice_fiscale());
                ordine.setPartitaIva(fornitoreTrovato.getAnagrafico().getPartita_iva());
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doFreeSearchFindFornitore(ActionContext context) {
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        TerzoBulk tb = new TerzoBulk();
        tb.setAnagrafico(new AnagraficoBulk());
        return freeSearch(context, bp.getFormField("fornitore"), tb);
    }

    public Forward doBlankSearchFindFornitore(ActionContext context,
                                              OrdineAcqBulk ordine)
            throws java.rmi.RemoteException {

        try {
            TerzoBulk tb = new TerzoBulk();
            tb.setAnagrafico(new AnagraficoBulk());
            ordine.setFornitore(tb);
            ordine.setNome(null);
            ordine.setCognome(null);
            ordine.setRagioneSociale(null);
            ordine.setCodiceFiscale(null);
            ordine.setPartitaIva(null);
            if (ordine.getContratto() != null && ordine.getContratto().getPg_contratto() != null) {
                ordine.setContratto(new ContrattoBulk());
                doBlankSearchFind_contratto(context, ordine);
            }
            return context.findDefaultForward();

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doRicercaObbligazione(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            java.util.List models = bp.getRighe().getSelectedModels(context);
            Forward forward = context.findDefaultForward();
            if (models == null || models.isEmpty())
                bp.setErrorMessage("Per procedere, selezionare i dettagli da associare agli impegni!");
            else {
                OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
                if (ordine.getFornitore() == null || ordine.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                    throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario impostare un fornitore!");

                controllaSelezionePerContabilizzazione(context, models.iterator());
                try {
                    List lista = recuperoListaCapitoli(context, models.iterator());
                    forward = basicDoRicercaObbligazione(context, ordine, models, lista);
                } catch (ApplicationException e) {
                    throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
                }
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    protected void controllaSelezionePerContabilizzazione(ActionContext context, java.util.Iterator selectedModels)
            throws it.cnr.jada.comp.ApplicationException {

        if (selectedModels != null) {
            while (selectedModels.hasNext()) {
                OrdineAcqRigaBulk rigaSelected = (OrdineAcqRigaBulk) selectedModels.next();
                if (rigaSelected.getDspObbligazioneScadenzario() != null && rigaSelected.getDspObbligazioneScadenzario().getEsercizio_originale() != null) {
                    throw new it.cnr.jada.comp.ApplicationException("Il dettaglio \"" + rigaSelected.getRiga() + "\" è già stato associato ad impegno! Modificare la selezione.");
                }
                if (rigaSelected.getRigheConsegnaColl() != null && !rigaSelected.getRigheConsegnaColl().isEmpty()) {
                    for (Object bulk : rigaSelected.getRigheConsegnaColl()) {
                        OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) bulk;
                        if (cons.getObbligazioneScadenzario() != null && cons.getObbligazioneScadenzario().getEsercizio_originale() != null) {
                            throw new it.cnr.jada.comp.ApplicationException("Il dettaglio \"" + rigaSelected.getRiga() + "\" è già stato associato ad impegno! Modificare la selezione.");
                        }
                    }

                }
            }
        }
    }

    protected java.util.List recuperoListaCapitoli(ActionContext context, java.util.Iterator selectedModels)
            throws ComponentException, PersistencyException, IntrospectionException, RemoteException, BusinessProcessException {

        if (selectedModels != null) {
            java.util.List titoliCapitoli = new ArrayList<>();
            java.util.Vector categorieGruppo = new java.util.Vector();
            int count = 0;

            while (selectedModels.hasNext()) {
                count += 1;
                OrdineAcqRigaBulk rigaSelected = (OrdineAcqRigaBulk) selectedModels.next();
                Bene_servizioBulk beneServizio = rigaSelected.getBeneServizio();
                if (beneServizio == null)
                    throw new it.cnr.jada.comp.ApplicationException("Valorizzare il bene/servizio per il dettaglio " + ((rigaSelected.getRiga() == null) ? "" : "\"" + rigaSelected.getRiga() + "\"") + "! Operazione interrotta.");
                if (beneServizio.getCategoria_gruppo() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Il bene/servizio \"" + beneServizio.getDs_bene_servizio() + "\" non ha definito alcuna categoria di appartenenza! Operazione interrotta.");
                else if (categorieGruppo.isEmpty())
                    categorieGruppo.add(beneServizio.getCategoria_gruppo());
                else
                    for (java.util.Iterator i = ((java.util.Vector) categorieGruppo.clone()).iterator(); i.hasNext(); ) {
                        Categoria_gruppo_inventBulk cat = (Categoria_gruppo_inventBulk) i.next();
                        if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(categorieGruppo, beneServizio.getCategoria_gruppo()))
                            categorieGruppo.add(beneServizio.getCategoria_gruppo());
                    }

            }
            CategoriaGruppoInventComponentSession h = (CategoriaGruppoInventComponentSession)
                    context.getBusinessProcess().createComponentSession(
                            "CNRDOCAMM00_EJB_CategoriaGruppoInventComponentSession",
                            CategoriaGruppoInventComponentSession.class);
            for (java.util.Iterator i = ((java.util.Vector) categorieGruppo.clone()).iterator(); i.hasNext(); ) {
                Categoria_gruppo_inventBulk cat = (Categoria_gruppo_inventBulk) i.next();
                java.util.List titoliCapitoliCatGrp = h.findAssVoceFList(context.getUserContext(), cat);
                if (titoliCapitoliCatGrp == null)
                    throw new it.cnr.jada.comp.ApplicationException("Alla categoria " + cat.getCd_categoria_gruppo() + "\" non è stato attribuita l'associazione al capitolo di spesa");
                if (titoliCapitoli.isEmpty()) {
                    for (java.util.Iterator k = titoliCapitoliCatGrp.iterator(); k.hasNext(); ) {
                        Categoria_gruppo_voceBulk assVoce = (Categoria_gruppo_voceBulk) k.next();
                        titoliCapitoli.add(assVoce.getElemento_voce());
                    }
                } else
                    for (java.util.Iterator k = titoliCapitoliCatGrp.iterator(); k.hasNext(); ) {
                        Categoria_gruppo_voceBulk assVoce = (Categoria_gruppo_voceBulk) k.next();
                        if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(titoliCapitoli, assVoce.getElemento_voce()))
                            titoliCapitoli.add(assVoce.getElemento_voce());
                    }
            }

            if (titoliCapitoli != null && !titoliCapitoli.isEmpty())
                return titoliCapitoli;
        }
        return null;
    }

    protected java.math.BigDecimal calcolaTotaleSelezionati(
            java.util.List selectedModels,
            boolean escludiIVA)
            throws it.cnr.jada.comp.ApplicationException {

        java.math.BigDecimal importo = new java.math.BigDecimal(0);
        boolean escludiIVAInt = false;
        boolean escludiIVAOld = escludiIVA;
        if (selectedModels != null) {
            for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                escludiIVA = escludiIVAOld;
                OrdineAcqRigaBulk rigaSelected = (OrdineAcqRigaBulk) i.next();

                java.math.BigDecimal imTotale = (escludiIVA) ?
                        rigaSelected.getImImponibile() :
                        rigaSelected.getImTotaleRiga();
                importo = importo.add(imTotale);
            }
        }

        importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        return importo;
    }

    protected java.math.BigDecimal calcolaTotaleObbligazioniAssociate(
            java.util.List selectedModels,
            boolean escludiIVA)
            throws it.cnr.jada.comp.ApplicationException {

        java.math.BigDecimal importo = new java.math.BigDecimal(0);
        boolean escludiIVAInt = false;
        boolean escludiIVAOld = escludiIVA;
        if (selectedModels != null) {
            for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                escludiIVA = escludiIVAOld;
                OrdineAcqConsegnaBulk consSelected = (OrdineAcqConsegnaBulk) i.next();

                java.math.BigDecimal imTotale = (escludiIVA) ?
                        consSelected.getImImponibile() :
                        consSelected.getImTotaleConsegna();
                importo = importo.add(imTotale);
            }
        }

        importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        return importo;
    }

    private Forward basicDoRicercaObbligazione(
            ActionContext context,
            OrdineAcqBulk ordine,
            java.util.List models,
            java.util.List listaCapitoli) {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
        try {
            Unita_organizzativaBulk uoImpegno = null;
            Iterator righeSelezionate = models.iterator();
            if (righeSelezionate != null) {
                while (righeSelezionate.hasNext()) {
                    OrdineAcqRigaBulk rigaSelected = (OrdineAcqRigaBulk) righeSelezionate.next();
                    for (Object consegna : rigaSelected.getRigheConsegnaColl()) {
                        OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) consegna;
                        OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
                        Unita_organizzativaBulk uo = h.recuperoUoPerImpegno(context.getUserContext(), cons);
                        if (uoImpegno == null) {
                            uoImpegno = uo;
                        } else if (!uoImpegno.equalsByPrimaryKey(uo)) {
                            throw new it.cnr.jada.comp.ApplicationException("Selezione non valida. Esistono diverse unità organizzative collegate alle righe selezionate, correggere la selezione.");
                        }
                    }
                    if (uoImpegno == null) {
                        throw new it.cnr.jada.comp.ApplicationException("Selezionare righe con un'unità organizzativa.");
                    }
                }
            }

            Filtro_ricerca_obbligazioniVBulk filtro = new Filtro_ricerca_obbligazioniVBulk();
            filtro.setFornitore(ordine.getFornitore());
            filtro.setDs_obbligazione(ordine.getNumero()== null ? "Acquisto" : ordine.getDescrizioneObbligazione());
            filtro.setIm_importo(calcolaTotaleSelezionati(models, false));
            filtro.setListaVociSelezionabili(listaCapitoli);
            filtro.setContratto(ordine.getContratto());
            filtro.setCd_unita_organizzativa(uoImpegno.getCd_unita_organizzativa());
            if (filtro.getData_scadenziario() == null)
                filtro.setFl_data_scadenziario(Boolean.FALSE);
            if (models == null || models.isEmpty())
                filtro.setFl_importo(Boolean.FALSE);

            BulkBP robp = (BulkBP) context.getUserInfo().createBusinessProcess(context, "RicercaObbligazioniBP", new Object[]{"MRSWTh"});
            robp.setModel(context, filtro);
            context.addHookForward("bringback", this, "doContabilizza");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(robp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    protected Forward basicDoBringBackOpenObbligazioniWindow(
            ActionContext context,
            Obbligazione_scadenzarioBulk newObblig) {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        List righeModel = new ArrayList<>();
        try {
            TerzoBulk creditore = newObblig.getObbligazione().getCreditore();
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            if (!ordine.getFornitore().equalsByPrimaryKey(creditore) &&
                    !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore dell'ordine!");
/* 	Rospuc 15/01/2015 Controllo SOSPESO  compatibilità dell'obbligazione con il titolo capitolo selezionato 
   	SOSPESO PER ESERCIZIO 2015	*/
            java.util.List dettagliDaContabilizzare = (java.util.List) ordine.getObbligazioniHash().get(newObblig);
            if (dettagliDaContabilizzare != null && !dettagliDaContabilizzare.isEmpty()) {
                for (java.util.Iterator i = dettagliDaContabilizzare.iterator(); i.hasNext(); ) {
                    Object rigaObj = i.next();
                    OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) rigaObj;
                    righeModel.add(cons.getOrdineAcqRiga());
                }

                List titoloCapitoloValidolist = recuperoListaCapitoli(context, righeModel.iterator());
                Elemento_voceBulk titoloCapitoloObbligazione = newObblig.getObbligazione().getElemento_voce();
                //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
                Boolean compatibile = null;
                if (titoloCapitoloValidolist != null && titoloCapitoloValidolist.size() != 0)
                    for (Iterator i = titoloCapitoloValidolist.iterator(); (i.hasNext() && (compatibile == null || !compatibile)); ) {
                        Elemento_voceBulk bulk = (Elemento_voceBulk) i.next();
                        if (bulk.getCd_elemento_voce().compareTo(titoloCapitoloObbligazione.getCd_elemento_voce()) == 0)
                            compatibile = new Boolean(true);
                        else
                            compatibile = new Boolean(false);
                    }
                if (compatibile != null && !compatibile)
                    throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo della categoria");//+ titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
            }

            Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            if (obbligazione != null) {
                resyncObbligazione(context, obbligazione, newObblig);
            } else {
                basicDoContabilizzaRiga(context, newObblig, null);
            }
        } catch (Throwable t) {
            it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi defSaldiBulk = bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
            if (newObblig.getObbligazione().getPg_ver_rec().equals((Long) newObblig.getObbligazione().getSaldiInfo().get("pg_ver_rec")))
                defSaldiBulk.removeFromDefferredSaldi(newObblig.getObbligazione());
            try {
                CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
            } catch (Exception e) {
                handleException(context, e);
            }
            return handleException(context, t);
        }
        return context.findDefaultForward();
    }

    private void basicDoContabilizzaRiga(
            ActionContext context,
            Obbligazione_scadenzarioBulk obbligazione,
            java.util.List selectedModels)
            throws it.cnr.jada.comp.ComponentException {

        if (obbligazione != null && selectedModels != null) {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();

            java.util.Vector dettagliDaContabilizzare = new java.util.Vector();
            List consegneDaContabilizzare = new ArrayList<>();
            if (selectedModels != null && !selectedModels.isEmpty()) {
                for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                    Object rigaObj = i.next();
                    OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) rigaObj;
                    for (Object bulk : riga.getRigheConsegnaColl()) {
                        OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) bulk;
                        dettagliDaContabilizzare.add(cons);
                    }
                }
            }

            ObbligazioniTable obbs = ((OrdineAcqBulk) bp.getModel()).getObbligazioniHash();
            if (obbs != null) {
                java.util.List dettagliContabilizzati = (java.util.List) obbs.get(obbligazione);
                if (dettagliContabilizzati != null && !dettagliContabilizzati.isEmpty())
                    dettagliDaContabilizzare.addAll(dettagliContabilizzati);
            }
//			Elemento_voceBulk titoloCapitoloValido = controllaSelezionePerTitoloCapitolo(context, dettagliDaContabilizzare.iterator());
            Elemento_voceBulk titoloCapitoloObbligazione = obbligazione.getObbligazione().getElemento_voce();

            // MI - controllo se l'obbligazione ha voce coerente con il tipo di bene
            if (selectedModels != null && !selectedModels.isEmpty()) {
                for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                    Object rigaObj = i.next();
                    OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) rigaObj;
                    if (!titoloCapitoloObbligazione.getFl_inv_beni_patr().equals(riga.getBeneServizio().getFl_gestione_inventario())) {
                        if (riga.getBeneServizio().getFl_gestione_inventario().booleanValue())
                            throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno selezionato non è utilizzabile per beni patrimoniali da inventariare!");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno selezionato non è utilizzabile per beni/servizi da non inventariare!");
                    }
                }
            }
            try {
                List titoloCapitoloValidolist;
                if (dettagliDaContabilizzare != null && !dettagliDaContabilizzare.isEmpty()) {
                    titoloCapitoloValidolist = recuperoListaCapitoli(context, selectedModels.iterator());

                    //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
                    Boolean compatibile = null;
                    if (titoloCapitoloValidolist != null && titoloCapitoloValidolist.size() != 0)
                        for (Iterator i = titoloCapitoloValidolist.iterator(); (i.hasNext() && (compatibile == null || !compatibile)); ) {
                            Elemento_voceBulk bulk = (Elemento_voceBulk) i.next();
                            if (bulk.getCd_elemento_voce().compareTo(titoloCapitoloObbligazione.getCd_elemento_voce()) == 0)
                                compatibile = new Boolean(true);
                            else
                                compatibile = new Boolean(false);
                        }
                    if (compatibile != null && !compatibile)
                        throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo della categoria");//+ titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
                }
            } catch (PersistencyException e1) {
                bp.handleException(e1);
            } catch (IntrospectionException e1) {
                bp.handleException(e1);
            } catch (RemoteException e1) {
                bp.handleException(e1);
            } catch (BusinessProcessException e1) {
                bp.handleException(e1);
            }
            //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
//			if (titoloCapitoloValido != null &&
//				!(titoloCapitoloObbligazione.getCd_elemento_voce().startsWith(titoloCapitoloValido.getCd_elemento_voce()) ||
//				titoloCapitoloValido.getCd_elemento_voce().startsWith(titoloCapitoloObbligazione.getCd_elemento_voce())))
//				throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo \"" + titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
            try {
                OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
                OrdineAcqBulk ordine = h.contabilizzaDettagliSelezionati(
                        context.getUserContext(),
                        (OrdineAcqBulk) bp.getModel(),
                        selectedModels,
                        obbligazione);
                try {
                    bp.setModel(context, ordine);
                    bp.setDirty(true);
                } catch (BusinessProcessException e) {
                }
            } catch (java.rmi.RemoteException e) {
                bp.handleException(e);
            } catch (BusinessProcessException e) {
                bp.handleException(e);
            }

            doCalcolaTotalePerObbligazione(context, obbligazione);
        }
    }

    private void basicDoContabilizzaConsegne(
            ActionContext context,
            Obbligazione_scadenzarioBulk obbligazione,
            java.util.List selectedModels)
            throws it.cnr.jada.comp.ComponentException {

        java.util.List righeModel = new ArrayList<>();

        if (obbligazione != null && selectedModels != null) {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();

            java.util.Vector dettagliDaContabilizzare = new java.util.Vector();
            dettagliDaContabilizzare.addAll(selectedModels);
            ObbligazioniTable obbs = ((OrdineAcqBulk) bp.getModel()).getObbligazioniHash();
            if (obbs != null) {
                java.util.List dettagliContabilizzati = (java.util.List) obbs.get(obbligazione);
                if (dettagliContabilizzati != null && !dettagliContabilizzati.isEmpty())
                    dettagliDaContabilizzare.addAll(dettagliContabilizzati);
            }
//			Elemento_voceBulk titoloCapitoloValido = controllaSelezionePerTitoloCapitolo(context, dettagliDaContabilizzare.iterator());
            Elemento_voceBulk titoloCapitoloObbligazione = obbligazione.getObbligazione().getElemento_voce();

            // MI - controllo se l'obbligazione ha voce coerente con il tipo di bene
            if (selectedModels != null && !selectedModels.isEmpty()) {
                for (java.util.Iterator i = selectedModels.iterator(); i.hasNext(); ) {
                    Object rigaObj = i.next();
                    OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) rigaObj;
                    righeModel.add(cons.getOrdineAcqRiga());
                    if (!titoloCapitoloObbligazione.getFl_inv_beni_patr().equals(cons.getOrdineAcqRiga().getBeneServizio().getFl_gestione_inventario())) {
                        if (cons.getOrdineAcqRiga().getBeneServizio().getFl_gestione_inventario().booleanValue())
                            throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno selezionato non è utilizzabile per beni patrimoniali da inventariare!");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno selezionato non è utilizzabile per beni/servizi da non inventariare!");
                    }
                }
            }
            try {
                List titoloCapitoloValidolist;
                if (dettagliDaContabilizzare != null && !dettagliDaContabilizzare.isEmpty()) {
                    titoloCapitoloValidolist = recuperoListaCapitoli(context, righeModel.iterator());

                    //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
                    Boolean compatibile = null;
                    if (titoloCapitoloValidolist != null && titoloCapitoloValidolist.size() != 0)
                        for (Iterator i = titoloCapitoloValidolist.iterator(); (i.hasNext() && (compatibile == null || !compatibile)); ) {
                            Elemento_voceBulk bulk = (Elemento_voceBulk) i.next();
                            if (bulk.getCd_elemento_voce().compareTo(titoloCapitoloObbligazione.getCd_elemento_voce()) == 0)
                                compatibile = new Boolean(true);
                            else
                                compatibile = new Boolean(false);
                        }
                    if (compatibile != null && !compatibile)
                        throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo della categoria");//+ titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
                }
            } catch (PersistencyException e1) {
                bp.handleException(e1);
            } catch (IntrospectionException e1) {
                bp.handleException(e1);
            } catch (RemoteException e1) {
                bp.handleException(e1);
            } catch (BusinessProcessException e1) {
                bp.handleException(e1);
            }
            //Controllo la compatibilità dell'obbligazione con il titolo capitolo selezionato
//			if (titoloCapitoloValido != null &&
//				!(titoloCapitoloObbligazione.getCd_elemento_voce().startsWith(titoloCapitoloValido.getCd_elemento_voce()) ||
//				titoloCapitoloValido.getCd_elemento_voce().startsWith(titoloCapitoloObbligazione.getCd_elemento_voce())))
//				throw new it.cnr.jada.comp.ApplicationException("L'impegno selezionato non è compatibile con il titolo capitolo \"" + titoloCapitoloValido.getCd_ds_elemento_voce() + "\"!");
            try {
                OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
                OrdineAcqBulk ordine = h.contabilizzaConsegneSelezionate(
                        context.getUserContext(),
                        (OrdineAcqBulk) bp.getModel(),
                        selectedModels,
                        obbligazione);
                try {
                    bp.setModel(context, ordine);
                    bp.setDirty(true);
                } catch (BusinessProcessException e) {
                }
            } catch (java.rmi.RemoteException e) {
                bp.handleException(e);
            } catch (BusinessProcessException e) {
                bp.handleException(e);
            }

            doCalcolaTotalePerObbligazione(context, obbligazione);
        }
    }

    public Forward doAddToCRUDMain_Obbligazioni(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            bp.getRighe().getSelection().clearSelection();
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();

            if (ordine.getFornitore() == null || ordine.getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
                throw new it.cnr.jada.comp.ApplicationException("Per eseguire questa operazione è necessario selezionare un fornitore!");
            recuperoListaCapitoli(context, ordine.getRigheOrdineColl().iterator());
            return basicDoRicercaObbligazione(context, ordine, null, null);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
     * figlio del ricevente
     */
    public Forward doAddToCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            bp.getRighe().getSelection().clearSelection();
            fillModel(context);
            Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            if (obbligazione == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno a cui associare i dettagli.");

            java.util.Vector selectedModels = new java.util.Vector();
            for (java.util.Enumeration e = bp.getRighe().getElements(); e.hasMoreElements(); ) {
                OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) e.nextElement();
                if (riga.getDspObbligazioneScadenzario() != null && riga.getDspObbligazioneScadenzario().getEsercizio_originale() != null)
                    selectedModels.add(riga);
            }
            if (selectedModels.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Tutti i dettagli sono già stati contabilizzati!");
            it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
                    context,
                    new it.cnr.jada.util.ListRemoteIterator(selectedModels),
                    it.cnr.jada.bulk.BulkInfo.getBulkInfo(OrdineAcqRigaBulk.class),
                    "righiSet",
                    "doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni");
            slbp.setMultiSelection(true);
            return slbp;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackAddToCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        try {
            HookForward fwd = (HookForward) context.getCaller();
            java.util.List selectedModels = (java.util.List) fwd.getParameter("selectedElements");
            if (selectedModels != null && !selectedModels.isEmpty()) {
                CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
                Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
                if (obbligazione != null) {
                    basicDoContabilizzaRiga(context, obbligazione, selectedModels);
                    bp.setDirty(true);
                }
                doCalcolaTotalePerObbligazione(context, obbligazione);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackOpenObbligazioniWindow(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("bringback");
        if (obblig != null) {
            try {
                basicDoBringBackOpenObbligazioniWindow(context, obblig);

                CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
                bp.getObbligazioniController().reset(context);
                bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), obblig));

                doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());

                bp.setDirty(true);
                if (bp instanceof TitoloDiCreditoDebitoBP)
                    ((TitoloDiCreditoDebitoBP) bp).addToDocumentiContabiliModificati(obblig);
            } catch (Throwable t) {
                return handleException(context, t);
            }
        }
        return context.findDefaultForward();
    }

    public Forward doCalcolaTotalePerObbligazione(ActionContext context, Obbligazione_scadenzarioBulk obbligazione) {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
        if (ordine.getOrdineObbligazioniHash() != null && obbligazione != null) {
            try {
                ordine.setImportoTotalePerObbligazione(
                        calcolaTotaleObbligazioniAssociate(
                                (java.util.List) ordine.getOrdineObbligazioniHash().get(obbligazione),
                                false));
            } catch (it.cnr.jada.comp.ApplicationException e) {
                ordine.setImportoTotalePerObbligazione(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            }
        } else
            ordine.setImportoTotalePerObbligazione(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        return context.findDefaultForward();
    }

    public Forward doContabilizza(ActionContext context) {

        HookForward caller = (HookForward) context.getCaller();
        Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk) caller.getParameter("obbligazioneSelezionata");
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        java.util.List selectedModels = null;
        java.util.List selectedModelsCons = new ArrayList<>();
        try {
            selectedModels = bp.getRighe().getSelectedModels(context);
            bp.getRighe().getSelection().clearSelection();
        } catch (Throwable e) {
        }

        if (selectedModels != null) {
            for (Object oggetto : selectedModels) {
                OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) oggetto;
                for (Object oggettoCons : riga.getRigheConsegnaColl()) {
                    OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) oggettoCons;
                    selectedModelsCons.add(cons);
                }
            }
        }
        if (obblig != null) {
            try {
                OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
                TerzoBulk creditore = obblig.getObbligazione().getCreditore();
                if (!ordine.getFornitore().equalsByPrimaryKey(creditore) &&
                        !AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita())) {
                    ((IDocumentoAmministrativoBulk) ordine).addToDocumentiContabiliCancellati(obblig);
                    throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata deve appartenere ad un'obbligazione che ha come creditore il fornitore dell'ordine!");
                }
                Filtro_ricerca_obbligazioniVBulk filtro = (Filtro_ricerca_obbligazioniVBulk) caller.getParameter("filtroRicercaUtilizzato");
                if (filtro != null) {
                    Elemento_voceBulk ev = filtro.getElemento_voce();
                    if (ev != null) {
                        if (!obblig.getObbligazione().getElemento_voce().getCd_elemento_voce().startsWith(ev.getCd_elemento_voce())) {
                            if (!ev.getCd_elemento_voce().startsWith(obblig.getObbligazione().getElemento_voce().getCd_elemento_voce())) {
                                ((IDocumentoAmministrativoBulk) ordine).addToDocumentiContabiliCancellati(obblig);
                                throw new it.cnr.jada.comp.ApplicationException("Il titolo capitolo dell'impegno deve essere uguale o appartenere al titolo capitolo della categoria inventario dei beni selezionati (\"" + ev.getCd_elemento_voce() + "\")!");
                            }
                        }
                    }
                }
                Obbligazione_scadenzarioBulk obbligazione = null;
                ObbligazioniTable obbHash = ordine.getObbligazioniHash();
                if (obbHash != null && !obbHash.isEmpty())
                    obbligazione = obbHash.getKey(obblig);
                if (obbligazione != null && obbligazione.getObbligazione().isTemporaneo()) {
                    java.util.Vector models = ((java.util.Vector) obbHash.get(obbligazione));
                    java.util.Vector clone = (java.util.Vector) models.clone();
                    if (!clone.isEmpty()) {
                        scollegaDettagliDaObbligazione(context, clone);
                        clone.addAll(selectedModelsCons);
                        basicDoContabilizzaConsegne(context, obblig, clone);
                    } else {
                        obbHash.remove(obbligazione);
                        basicDoContabilizzaConsegne(context, obblig, selectedModelsCons);
                    }
                } else {
                    basicDoContabilizzaConsegne(context, obblig, selectedModelsCons);
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }
            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), obblig));

            bp.setDirty(true);
            if (!"tabOrdineAcqObbligazioni".equals(bp.getTab("tab")))
                bp.setTab("tab", "tabOrdineAcqObbligazioni");
        }
        return context.findDefaultForward();
    }

    /**
     * Richiede all'obbligazione di modificare in automatico la sua scadenza (quella
     * selezionata) portando la stessa ad importo pari alla sommatoria degli importi
     * di riga dei dettagli associati. Aggiorna la mappa dei saldi per le variazioni
     * subite dall'obbligazione
     *
     * @param context L'ActionContext della richiesta
     * @param prefix
     * @return Il Forward alla pagina di risposta
     */
    public Forward doModificaScadenzaInAutomatico(ActionContext context, String prefix) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();

            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();

            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da modificare in automatico!");
            java.util.Vector righeAssociate = (java.util.Vector) ordine.getOrdineObbligazioniHash().get(scadenza);
            if (righeAssociate == null || righeAssociate.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Associare dei dettagli prima di aggiornare in automatico la scadenza impegno!");
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            ObbligazioneAbstractComponentSession h = CRUDVirtualObbligazioneBP.getVirtualComponentSession(context, true);

            try {
                scadenza = (Obbligazione_scadenzarioBulk) h.modificaScadenzaInAutomatico(
                        context.getUserContext(),
                        scadenza,
                        ordine.getImportoTotalePerObbligazione(),
                        false);
                bp.getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk().addToDefferredSaldi(
                        scadenza.getObbligazione(),
                        scadenza.getObbligazione().getSaldiInfo());
            } catch (it.cnr.jada.comp.ComponentException e) {
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                if (e.getDetail() instanceof it.cnr.contab.doccont00.comp.SfondamentoPdGException)
                    throw new it.cnr.jada.comp.ApplicationException(e.getDetail().getMessage());
                throw e;
            }

            Forward fwd = basicDoBringBackOpenObbligazioniWindow(context, scadenza);

            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.getObbligazioniController().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(bp.getObbligazioniController().getDetails(), scadenza));
            bp.setDirty(true);

            return fwd;
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
/**
 * Gestisce la variazione manuale del valore del cambio e ricalcola tutti i totali
 */
//public Forward doOnChangeModified(ActionContext context) 
//{
//	try {
//		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)context.getBusinessProcess();
//		OrdineAcqBulk ordine = (OrdineAcqBulk)bp.getModel();
//		it.cnr.jada.bulk.PrimaryKeyHashtable vecchiTotali = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//		if (fp instanceof Fattura_passiva_IBulk) {
//			for (java.util.Iterator i = ordine.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
//				Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk)i.next();
//				java.math.BigDecimal vecchioTotale = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
//				vecchioTotale = vecchioTotale.add(dettaglio.getIm_totale_addebiti());
//				vecchiTotali.put(dettaglio, vecchioTotale);
//			}
//		}
//		java.math.BigDecimal vecchioCambio = fp.getCambio();
//		fillModel( context );
//		if (fp != null) {
//			java.math.BigDecimal cambioAttuale = fp.getCambio();
//			if (cambioAttuale == null)
//				fp.setCambio((cambioAttuale = new java.math.BigDecimal(0)));
//        	cambioAttuale = cambioAttuale.setScale(4,java.math.BigDecimal.ROUND_HALF_UP);
//        	fp.setCambio(cambioAttuale);
//	        if (cambioAttuale.compareTo(new java.math.BigDecimal(0))==0){
//	        	fp.setCambio(vecchioCambio);
//	        	throw new it.cnr.jada.comp.ApplicationException("Non è stato inserito un cambio valido (>0)");
//	        }
//			if (fp.getObbligazioniHash() != null && !fp.getObbligazioniHash().isEmpty())
//				bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze delle obbligazioni!");
//			if (fp.getAccertamentiHash() != null && !fp.getAccertamentiHash().isEmpty())
//				bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze degli accertamenti!");
//			fp.aggiornaImportiTotali();
//			basicDoCalcolaTotaleFatturaFornitoreInEur(fp);
//			for (java.util.Iterator i = fp.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
//				Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
//				if (dettaglio instanceof Fattura_passiva_rigaIBulk) {
//					java.math.BigDecimal vecchioTotale = (java.math.BigDecimal)vecchiTotali.get(dettaglio);
//					if (vecchioTotale == null)
//						vecchioTotale = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//					basicCalcolaImportoDisponibileNC(context, (Fattura_passiva_rigaIBulk)dettaglio, vecchioTotale);
//				}
//			}
//		}
//	} catch (Throwable t) {
//		return handleException(context, t);
//	}
//	return context.findDefaultForward();
//}

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
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
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

    /**
     * richiede l'apertura del pannello dell'obbligazione per la modifica della
     * scadenza selezionata
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doOpenObbligazioniWindow(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            fillModel(context);

            Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel();
            boolean viewMode = bp.isViewing();
            if (scadenza == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare l'impegno da " + (viewMode ? "visualizzare" : "modificare") + " in manuale!");
            if (bp.isDeleting() &&
                    !bp.isViewing() &&
                    !it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).equals(scadenza.getEsercizio()))
                throw new it.cnr.jada.comp.ApplicationException("La scadenza selezionata appartiene all'esercizio " + scadenza.getEsercizio().intValue() + "! Operazione annullata.");

            if (!viewMode && bp instanceof IDocumentoAmministrativoBP) {
                IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP) bp;
                viewMode = !docAmmBP.getDocumentoAmministrativoCorrente().isEditable();
                viewMode = !((CRUDOrdineAcqBP) docAmmBP).isManualModify();
            }

            String status = viewMode ? "V" : "M";
            it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP nbp = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP.getBusinessProcessFor(context, scadenza.getObbligazione(), status + "RSWTh");
            nbp.edit(context, scadenza.getObbligazione());
            ((ObbligazioneBulk) nbp.getModel()).setFromDocAmm(true);
            nbp.selezionaScadenza(scadenza, context);

            context.addHookForward("bringback", this, "doBringBackOpenObbligazioniWindow");
            HookForward hook = (HookForward) context.findForward("bringback");
            return context.addBusinessProcess(nbp);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "obbligazioni"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_Obbligazioni(ActionContext context) {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        it.cnr.jada.util.action.Selection selection = bp.getObbligazioniController().getSelection();
        try {
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare le scadenze che si desidera eliminare!");
        } catch (it.cnr.jada.comp.ApplicationException e) {
            return handleException(context, e);
        }
        java.util.List obbligazioni = bp.getObbligazioniController().getDetails();
        for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator(); i.hasNext(); ) {
            Obbligazione_scadenzarioBulk obbligazione = (Obbligazione_scadenzarioBulk) obbligazioni.get(i.nextIndex());
            java.util.Vector models = (java.util.Vector) ((OrdineAcqBulk) bp.getModel()).getOrdineObbligazioniHash().get(obbligazione);
            try {
                if (models != null && models.isEmpty()) {
                    OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
                    ordine.getOrdineObbligazioniHash().remove(obbligazione);
                    ordine.addToDocumentiContabiliCancellati(obbligazione);
                } else {
                    for (java.util.Iterator it = models.iterator(); it.hasNext(); ) {
                        OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) it.next();
                        if (cons.getStato() != null && cons.getStato().equals(OrdineAcqConsegnaBulk.STATO_EVASA)) {
                            throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare l'impegno \"" +
                                    +obbligazione.getEsercizio_originale().intValue()
                                    + "/" + obbligazione.getPg_obbligazione().longValue() +
                                    "\" perchè il dettaglio collegato \"" +
                                    cons.getRiga() + "/" + cons.getConsegna() +
                                    "\" è già stato evaso.");
                        }
                    }
                    scollegaDettagliDaObbligazione(context, (java.util.List) models.clone());
                }
            } catch (it.cnr.jada.comp.ComponentException e) {
                return handleException(context, e);
            }

            doCalcolaTotalePerObbligazione(context, null);

            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
            bp.setDirty(true);
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "obbligazioni_DettaglioObbligazioni"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRemoveFromCRUDMain_Obbligazioni_DettaglioObbligazioni(ActionContext context) {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        try {
            it.cnr.jada.util.action.Selection selection = bp.getDettaglioObbligazioneController().getSelection();
            if (selection.isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Selezionare i dettagli che si desidera scollegare!");
            java.util.List models = selection.select(bp.getDettaglioObbligazioneController().getDetails());
            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) i.next();
                if (cons.getStato() != null && cons.getStato().equals(OrdineAcqConsegnaBulk.STATO_EVASA)) {
                    throw new it.cnr.jada.comp.ApplicationException("Impossibile scollegare il dettaglio \"" +
                            cons.getRiga() + "/" + cons.getConsegna() +
                            "\" perchè esiste una consegna evasa.");
                }
            }
            scollegaDettagliDaObbligazione(context, models);
        } catch (it.cnr.jada.comp.ComponentException e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());

        OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();

        bp.getDettaglioObbligazioneController().getSelection().clear();
        bp.getDettaglioObbligazioneController().setModelIndex(context, -1);
        java.util.List dettagli = bp.getDettaglioObbligazioneController().getDetails();
        if (dettagli == null || dettagli.isEmpty()) {
            bp.getObbligazioniController().getSelection().clear();
            bp.getObbligazioniController().setModelIndex(context, -1);
        }
        bp.setDirty(true);

        return context.findDefaultForward();
    }
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di selezione dal controller "obbligazioni"
 *
 * @param context    L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
/**
 * Viene richiamato nel momento in cui si seleziona una valuta dal combo Valuta nella 
 * testata della fattura.
 * Viene ricercato il cambio valido, vengono ricalcolati i totali e ricalcolato il
 * totale in eur
 */
//public Forward doSelezionaValuta(ActionContext context) 
//{
//	try {
//		CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
//		Fattura_passivaBulk fattura = (Fattura_passivaBulk)bp.getModel();
//		DivisaBulk divisa = fattura.getValuta();
//		it.cnr.jada.bulk.PrimaryKeyHashtable vecchiTotali = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//		if (fattura instanceof Fattura_passiva_IBulk) {
//			for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
//				Fattura_passiva_rigaIBulk dettaglio = (Fattura_passiva_rigaIBulk)i.next();
//				java.math.BigDecimal vecchioTotale = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
//				vecchioTotale = vecchioTotale.add(dettaglio.getIm_totale_addebiti());
//				vecchiTotali.put(dettaglio, vecchioTotale);
//			}
//		}
//		fillModel( context );		
//		try	{
//			if (!bp.isSearching()) {
//				FatturaPassivaComponentSession h = (FatturaPassivaComponentSession)bp.createComponentSession();
//				fattura = h.cercaCambio(context.getUserContext(),fattura);
//				basicDoCalcolaTotaleFatturaFornitoreInEur(fattura);
//				for (java.util.Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
//					Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
//					if (dettaglio instanceof Fattura_passiva_rigaIBulk) {
//						java.math.BigDecimal vecchioTotale = (java.math.BigDecimal)vecchiTotali.get(dettaglio);
//						if (vecchioTotale == null)
//							vecchioTotale = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//						basicCalcolaImportoDisponibileNC(context, (Fattura_passiva_rigaIBulk)dettaglio, vecchioTotale);
//					}
//				}
//
//				bp.setModel(context,fattura);
//				if (fattura.getObbligazioniHash() != null && !fattura.getObbligazioniHash().isEmpty())
//					bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze delle obbligazioni!");
//				if (fattura.getAccertamentiHash() != null && !fattura.getAccertamentiHash().isEmpty())
//					bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze degli accertamenti!");
//			}
//			return context.findDefaultForward();
//		} catch(it.cnr.jada.comp.ComponentException e) {
//			fattura.setValuta(divisa);
//			throw e;
//		} 
//	} catch (Throwable t) {
//		return handleException(context, t);
//	}
//}
//public Fattura_passivaBulk doSelezionaValutaDefault(ActionContext context, Fattura_passivaBulk fattura) 
//		throws ComponentException {
//		
//		try {
//			CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)getBusinessProcess(context);
//			FatturaPassivaComponentSession h = (FatturaPassivaComponentSession)bp.createComponentSession();
//			fattura = h.selezionaValutaDiDefault(context.getUserContext(),fattura);
//			basicDoCalcolaTotaleFatturaFornitoreInEur(fattura);
//			if (fattura.getObbligazioniHash() != null && !fattura.getObbligazioniHash().isEmpty())
//				bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze delle obbligazioni!");
//			if (fattura.getAccertamentiHash() != null && !fattura.getAccertamentiHash().isEmpty())
//				bp.setErrorMessage("Attenzione: si ricorda che modificando il valore del cambio è necessario riportare in quadratura le scadenze degli accertamenti!");
//			return fattura;
//		} catch (BusinessProcessException e) {
//			throw new ComponentException(e);
//		} catch (java.rmi.RemoteException e) {
//			throw new ComponentException(e);
//		}
//	}

    /**
     * Risincronizza la collezione delle obbligazioni (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldObblig (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newObblig (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     * ('scollegaDettagliDaObbligazione')
     */

    private void resyncObbligazione(
            ActionContext context,
            Obbligazione_scadenzarioBulk oldObblig,
            Obbligazione_scadenzarioBulk newObblig)
            throws it.cnr.jada.comp.ComponentException {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
        java.util.Vector models = ((java.util.Vector) ordine.getOrdineObbligazioniHash().get(oldObblig));
        java.util.Vector clone = (java.util.Vector) models.clone();
        if (!clone.isEmpty())
            scollegaDettagliDaObbligazione(context, clone);
        else
            ordine.getOrdineObbligazioniHash().remove(oldObblig);

        basicDoContabilizzaConsegne(context, newObblig, clone);
    }

    /**
     * Risincronizza la collezione delle obbligazioni (richiamato dopo la modifica di
     * una scadenza associata al doc amm).
     * Se questa collezione contiene in chiave la oldObblig (scadenza vecchia), essa
     * viene eliminata e i dettagli ad essa associati vengono ricontabilizzati sulla
     * newObblig (scadenza modificata dall'utente); se non ha ancora dettagli associati
     * viene semplicemente eliminata
     * Se uno dei dettagli ha un'associazione con note di credito/debito e se non
     * sono in fase di cancellazione della fattura passiva, l'operazione viene interrotta
     */

    private void scollegaDettagliDaObbligazione(ActionContext context, java.util.List models)
            throws it.cnr.jada.comp.ComponentException {

        if (models != null) {

            for (java.util.Iterator i = models.iterator(); i.hasNext(); ) {
                OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) i.next();
//				if (riga.isConsegnaEvasa())
//					throw new it.cnr.jada.comp.ApplicationException("Non è possibile scollegare il dettaglio \"" + dettaglio.getDs_riga_fattura() + "\". Questa operazione è permessa solo per dettagli in stato \"" + dettaglio.STATO.get(dettaglio.STATO_CONTABILIZZATO) + "\".");
                CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
                OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
                ordine.removeFromOrdineObbligazioniHash(cons);
                cons.getOrdineAcqRiga().setDspObbligazioneScadenzario(null);
                cons.getOrdineAcqRiga().setToBeUpdated();
                cons.setObbligazioneScadenzario(null);
                cons.setToBeUpdated();
            }
//		} catch (it.cnr.jada.comp.ApplicationException e) {
//			try {
//				CRUDVirtualObbligazioneBP.rollbackToSafePoint(context);
//			} catch (Throwable t) {
//				throw new ComponentException(t);
//			}
//			throw e;
//		}
        }
    }

    public Forward doOnImportoChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();

            calcolaTotaleOrdine(context, ordine);

            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private void calcolaTotaleOrdine(ActionContext context, OrdineAcqBulk ordine)
            throws BusinessProcessException, RemoteException, PersistencyException {
        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
        OrdineAcqComponentSession h = (OrdineAcqComponentSession) bp.createComponentSession();
        try {
            OrdineAcqBulk ordineAggiornato = h.calcolaImportoOrdine(context.getUserContext(), ordine);
            ordine.setImImponibile(ordine.getImImponibile());
            ordine.setImIva(ordine.getImIva());
            ordine.setImIvaD(ordine.getImIvaD());
            ordine.setImTotaleOrdine(ordine.getImTotaleOrdine());
            for (java.util.Iterator i = ordine.getRigheOrdineColl().iterator(); i.hasNext(); ) {
                OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
                for (java.util.Iterator k = ordineAggiornato.getRigheOrdineColl().iterator(); k.hasNext(); ) {
                    OrdineAcqRigaBulk rigaAggiornata = (OrdineAcqRigaBulk) k.next();
                    if (riga.equalsByPrimaryKey(rigaAggiornata)) {
                        riga.setImImponibile(rigaAggiornata.getImImponibile());
                        riga.setImImponibileDivisa(rigaAggiornata.getImImponibileDivisa());
                        riga.setImIva(rigaAggiornata.getImIva());
                        riga.setImIvaD(rigaAggiornata.getImIvaD());
                        riga.setImIvaDivisa(rigaAggiornata.getImIvaDivisa());
                        riga.setImIvaNd(rigaAggiornata.getImIvaNd());
                        riga.setImTotaleRiga(rigaAggiornata.getImTotaleRiga());
                        for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                            OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                            for (java.util.Iterator x = rigaAggiornata.getRigheConsegnaColl().iterator(); x.hasNext(); ) {
                                OrdineAcqConsegnaBulk consegnaAggiornata = (OrdineAcqConsegnaBulk) x.next();
                                if (consegna.equalsByPrimaryKey(consegnaAggiornata)) {
                                    consegna.setImImponibile(consegnaAggiornata.getImImponibile());
                                    consegna.setImImponibileDivisa(consegnaAggiornata.getImImponibileDivisa());
                                    consegna.setImIva(consegnaAggiornata.getImIva());
                                    consegna.setImIvaD(consegnaAggiornata.getImIvaD());
                                    consegna.setImIvaDivisa(consegnaAggiornata.getImIvaDivisa());
                                    consegna.setImIvaNd(consegnaAggiornata.getImIvaNd());
                                    consegna.setImTotaleConsegna(consegnaAggiornata.getImTotaleConsegna());
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
            aggiornaObbligazioni(context);
        } catch (ComponentException e) {
        }
    }

    public Forward doOnDspQuantitaChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            gestioneConsegnaNonPresente(riga);
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setQuantita(riga.getDspQuantita());
                consegna.setToBeUpdated();
            }
            calcolaTotaleOrdine(context, ordine);
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private OrdineAcqRigaBulk gestioneConsegnaNonPresente(OrdineAcqRigaBulk riga) {
        if (riga.getRigheConsegnaColl() == null || riga.getRigheConsegnaColl().isEmpty()) {
            OrdineAcqConsegnaBulk consegna = new OrdineAcqConsegnaBulk();
            consegna.inizializzaConsegnaNuovaRiga();
            consegna.setRiga(riga.getRiga());
            consegna.setTipoConsegna(riga.getDspTipoConsegna());
            consegna.setDtPrevConsegna(riga.getDspDtPrevConsegna());
            consegna.setContoBulk(riga.getDspConto());
            consegna.setMagazzino(riga.getDspMagazzino());
            consegna.setLuogoConsegnaMag(riga.getDspLuogoConsegna());
            riga.addToRigheConsegnaColl(consegna);
        }
        return riga;
    }

    public Forward doOnDspTipoConsegnaChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            gestioneConsegnaNonPresente(riga);
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setTipoConsegna(riga.getDspTipoConsegna());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnDspDtPrevConsegnaChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            gestioneConsegnaNonPresente(riga);
            for (java.util.Iterator j = riga.getRigheConsegnaColl().iterator(); j.hasNext(); ) {
                OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk) j.next();
                consegna.setDtPrevConsegna(riga.getDspDtPrevConsegna());
                consegna.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnQuantitaChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) bp.getConsegne().getModel();
                riga.setDspQuantita(cons.getQuantita());
                riga.setToBeUpdated();
            }
            calcolaTotaleOrdine(context, ordine);
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnTipoConsegnaChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) bp.getConsegne().getModel();
                riga.setDspTipoConsegna(cons.getTipoConsegna());
                riga.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doOnDtPrevConsegnaChange(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) getBusinessProcess(context);
            fillModel(context);
            OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) bp.getRighe().getModel();
            if (riga.getRigheConsegnaColl().size() == 1) {
                OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) bp.getConsegne().getModel();
                riga.setDspDtPrevConsegna(cons.getDtPrevConsegna());
                riga.setToBeUpdated();
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward aggiornaObbligazioni(ActionContext context) {

        CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
        try {
            bp.getObbligazioniController().setSelection(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }

        doCalcolaTotalePerObbligazione(context, (Obbligazione_scadenzarioBulk) bp.getObbligazioniController().getModel());
        return context.findDefaultForward();
    }

    public Forward doRemoveFromCRUDMain_Righe(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            bp.getRighe().remove(context);
            calcolaTotaleOrdine(context, ordine);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doRemoveFromCRUDMain_Righe_Consegne(ActionContext context) {

        try {
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
            bp.getConsegne().remove(context);
            calcolaTotaleOrdine(context, ordine);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doVisualizzaContratto(ActionContext context) {

        try {
            fillModel(context);
            CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP) context.getBusinessProcess();
            OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();

            if (ordine == null || ordine.getContratto() == null || ordine.getContratto().getPg_contratto() == null)
                throw new it.cnr.jada.comp.ApplicationException("Non esiste alcun Contratto da visualizzare!");

            context.addHookForward("bringback", this, "doBringBackVisualizzaContratto");

            CRUDConfigAnagContrattoBP contrattoBP = (CRUDConfigAnagContrattoBP) context.createBusinessProcess(
                    "CRUDConfigAnagContrattoBP",
                    new Object[]{"V", ordine.getContratto(), "V"});

            try {
                contrattoBP.setEditable(false);
            } catch (Throwable t) {
                throw t;
            }

            return context.addBusinessProcess(contrattoBP);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackVisualizzaContratto(ActionContext context) {
        return context.findDefaultForward();
    }

    public Forward doToggleDettaglioContratto(ActionContext context) {
        CRUDOrdineAcqBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDOrdineAcqBP.class::isInstance)
                .map(CRUDOrdineAcqBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
        bp.setDettaglioContrattoCollapse(!bp.isDettaglioContrattoCollapse());
        return context.findDefaultForward();
    }


}

