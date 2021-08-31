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

package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bulk.ParametriSelezioneOrdiniAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.TipoOrdineKey;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Optional;

public class ParametriSelezioneOrdiniAcqBP extends BulkBP {

    public static final String VIS_ORDINI_RIGA_CONS = "visOrdiniConsegna";
    public static final String EVA_FORZATA_ORDINI = "evaForzataOrdini";

    private String tipoSelezione;

    public String getTipoSelezione() {
        return tipoSelezione;
    }

    public void setTipoSelezione(String tipoSelezione) {
        this.tipoSelezione = tipoSelezione;
    }

    public ParametriSelezioneOrdiniAcqBP() {
        this("");
    }

    public ParametriSelezioneOrdiniAcqBP(String s) {
        super(s);
    }

    public ParametriSelezioneOrdiniAcqBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

        try {
            return createNewBulk(context).initializeForSearch(this, context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext context) throws BusinessProcessException {
        super.init(config, context);
        try {

            this.setTipoSelezione(config.getInitParameter("tipoSelezione"));
            ParametriSelezioneOrdiniAcqBulk bulk = createEmptyModelForSearch(context);
            bulk = (ParametriSelezioneOrdiniAcqBulk) ((OrdineAcqComponentSession) createComponentSession(context)).initializeAbilitazioneOrdiniAcq(context.getUserContext(), bulk);
            setModel(context, bulk);
            setDirty(false);
            resetChildren(context);
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    /**
     * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
     */
    public ParametriSelezioneOrdiniAcqBulk createNewBulk(ActionContext context) throws BusinessProcessException {
        try {
            ParametriSelezioneOrdiniAcqBulk bulk = new ParametriSelezioneOrdiniAcqBulk();
            bulk.setUser(context.getUserInfo().getUserid());
            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.ejb.CRUDComponentSession createComponentSession(ActionContext actionContext) throws BusinessProcessException {

        return (OrdineAcqComponentSession) createComponentSession(
                "CNRORDMAG00_EJB_OrdineAcqComponentSession",
                OrdineAcqComponentSession.class);

    }

    public boolean checkFiltriObbligatori(ParametriSelezioneOrdiniAcqBulk parametriSelezioneOrdiniAcqBulk) {
        if (EVA_FORZATA_ORDINI.equalsIgnoreCase(getTipoSelezione())) {
                if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getUnitaOperativaAbilitata()).map(UnitaOperativaOrdBulk::getCdUnitaOrganizzativa).isPresent())
                    return true;
            return false;
        }
        return true;

    }

    public boolean isIndicatoAlmenoUnCriterioDiSelezione(ParametriSelezioneOrdiniAcqBulk parametriSelezioneOrdiniAcqBulk) {
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getUnitaOperativaAbilitata()).map(UnitaOperativaOrdBulk::getCdUnitaOrganizzativa).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getMagazzinoAbilitato()).map(MagazzinoBulk::getCdMagazzino).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getNumerazioneOrd()).map(NumerazioneOrdBulk::getCdNumeratore).isPresent())
            return true;

        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getUnitaOperativaOrdine()).map(UnitaOperativaOrdBulk::getCdUnitaOrganizzativa).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getUnitaOperativaRicevente()).map(UnitaOperativaOrdBulk::getCdUnitaOrganizzativa).isPresent())
            return true;

        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getTerzo()).map(TerzoBulk::getCd_anag).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getDaBeneServizio()).map(Bene_servizioBulk::getCd_bene_servizio).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getaBeneServizio()).map(Bene_servizioBulk::getCd_bene_servizio).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getDaDataOrdine()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getaDataOrdine()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getDaNumeroOrdine()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getaNumeroOrdine()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getDaDataOrdineDef()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getaDataOrdineDef()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getDaDataPrevConsegna()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getaDataPrevConsegna()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getTipoConsegna()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getStatoOrdine()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getStatoConsegna()).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getImpegno()).map(Obbligazione_scadenzarioBulk::getPg_obbligazione).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getContratto()).map(ContrattoBulk::getPg_contratto).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getCig()).map(CigBulk::getCdCig).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getCup()).map(CupBulk::getCdCup).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getRup()).map(V_persona_fisicaBulk::getCd_terzo).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getProceduraAmministrativa()).map(Procedure_amministrativeBulk::getCd_proc_amm).isPresent())
            return true;
        if (Optional.ofNullable(parametriSelezioneOrdiniAcqBulk.getTipoOrdine()).map(TipoOrdineKey::getCdTipoOrdine).isPresent())
            return true;
        return false;

    }


    public RemoteIterator ricercaOrdiniAcq(ActionContext actioncontext) throws BusinessProcessException {
        try {
            OrdineAcqComponentSession cs = (OrdineAcqComponentSession) createComponentSession(actioncontext);
            if (cs == null) return null;
            ParametriSelezioneOrdiniAcqBulk parametriSelezioneOrdiniAcqBulk = (ParametriSelezioneOrdiniAcqBulk) getModel();
            //if (parametriSelezioneOrdiniAcqBulk.isIndicatoAlmenoUnCriterioDiSelezione()){
            if (!checkFiltriObbligatori(parametriSelezioneOrdiniAcqBulk))
                throw new ApplicationException("E' necessario indicare Unità Operativa e Magazzino");
            if (isIndicatoAlmenoUnCriterioDiSelezione(parametriSelezioneOrdiniAcqBulk)) {
                return cs.ricercaOrdiniAcqCons(actioncontext.getUserContext(), parametriSelezioneOrdiniAcqBulk,this.tipoSelezione);
            }
            throw new ApplicationException("E' necessario indicare almeno un criterio di selezione");
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }

    }

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        return toolbar;
    }

    @Override
    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause clauses, OggettoBulk bulk,
                               OggettoBulk oggettobulk1, String property) throws BusinessProcessException {
        try {
            it.cnr.jada.ejb.CRUDComponentSession cs = createComponentSession(actioncontext);
            if (cs == null) return null;
            return EJBCommonServices.openRemoteIterator(
                    actioncontext,
                    cs.cerca(
                            actioncontext.getUserContext(),
                            clauses,
                            bulk,
                            getModel(),
                            property));
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }

    }
}
