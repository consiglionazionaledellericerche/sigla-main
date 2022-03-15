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

/*
 * Created on Apr 21, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.bp;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_entrata_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_spesa_gestBulk;
import it.cnr.contab.pdg01.ejb.CRUDPdgVariazioneRigaGestComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author rpagano
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdgVariazioneRigaGestBP extends SimpleCRUDBP {
    private Parametri_cnrBulk parametriCnr;
    private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;
    private Pdg_variazioneBulk pdg_variazione;
    private DipartimentoBulk dipartimentoSrivania;
    private String uoScrivania;
    private String tipoGestione;

    private SimpleDetailCRUDController righeVariazioneEtrGest = new SimpleDetailCRUDController("RigheVariazioneEtrGest", Pdg_variazione_riga_entrata_gestBulk.class, "righeVariazioneEtrGest", this) {
        protected void setModel(ActionContext actioncontext, OggettoBulk oggettobulk) {
            super.setModel(actioncontext, oggettobulk);
        }

        public boolean isShrinkable() {
            return super.isShrinkable() &&
                    isDettaglioGestionaleEnable((Pdg_variazione_riga_gestBulk) getModel());
        }

        public void removeAll(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
            List list = getDetails();
            for (int i = list.size() - 1; i >= 0; i--) {
                Pdg_variazione_riga_gestBulk dett = (Pdg_variazione_riga_gestBulk) list.get(i);
                if (isDettaglioGestionaleEnable(dett))
                    removeDetail(dett, i);
            }
            getParentController().setDirty(true);
            reset(actioncontext);
        }

        /**
         * Metodo per aggiungere alla toolbar del Controller un tasto necessario per apporre
         * il visto da parte del dipartimento.
         * @param context Il contesto dell'azione
         */
        @Override
        public void writeHTMLToolbar(
                javax.servlet.jsp.PageContext context,
                boolean reset,
                boolean find,
                boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

            super.writeHTMLToolbar(context, reset, find, delete, false);

            if (getParentController() != null &&
                    ((Ass_pdg_variazione_cdrBulk) getParentController().getModel()).getPdg_variazione().isApprovata() &&
                    getDipartimentoSrivania() != null &&
                    getDipartimentoSrivania().getCd_dipartimento() != null) {
                String command = "javascript:submitForm('doApponiVistoDipartimento')";
                it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                        context,
                        "img/properties16.gif",
                        !(getDetails().isEmpty() || ((CRUDBP) getParentController()).isSearching()) ? command : null,
                        true, "Apponi Visto",
                        HttpActionContext.isFromBootstrap(context));
            }
            super.closeButtonGROUPToolbar(context);
        }

        protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
            Pdg_variazione_riga_gestBulk dett = (Pdg_variazione_riga_gestBulk) oggettobulk;
            validaRiga(actioncontext, dett);
        }
    };

    private SimpleDetailCRUDController righeVariazioneSpeGest = new SimpleDetailCRUDController("RigheVariazioneSpeGest", Pdg_variazione_riga_spesa_gestBulk.class, "righeVariazioneSpeGest", this) {
        protected void setModel(ActionContext actioncontext, OggettoBulk oggettobulk) {
            super.setModel(actioncontext, oggettobulk);
        }

        public boolean isShrinkable() {
            return super.isShrinkable() &&
                    isDettaglioGestionaleEnable((Pdg_variazione_riga_gestBulk) getModel());
        }

        public void removeAll(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
            List list = getDetails();
            for (int i = list.size() - 1; i >= 0; i--) {
                Pdg_variazione_riga_gestBulk dett = (Pdg_variazione_riga_gestBulk) list.get(i);
                if (isDettaglioGestionaleEnable(dett))
                    removeDetail(dett, i);
            }
            getParentController().setDirty(true);
            reset(actioncontext);
        }

        /**
         * Metodo per aggiungere alla toolbar del Controller un tasto necessario per apporre
         * il visto da parte del dipartimento.
         * @param context Il contesto dell'azione
         */
        @Override
        public void writeHTMLToolbar(
                javax.servlet.jsp.PageContext context,
                boolean reset,
                boolean find,
                boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

            super.writeHTMLToolbar(context, reset, find, delete, false);

            if (getParentController() != null &&
                    ((Ass_pdg_variazione_cdrBulk) getParentController().getModel()).getPdg_variazione().isApprovata() &&
                    getDipartimentoSrivania() != null &&
                    getDipartimentoSrivania().getCd_dipartimento() != null) {
                String command = "javascript:submitForm('doApponiVistoDipartimento')";
                it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                        context,
                        "img/properties16.gif",
                        !(getDetails().isEmpty() || ((CRUDBP) getParentController()).isSearching()) ? command : null,
                        true, "Apponi Visto",
                        HttpActionContext.isFromBootstrap(context));
            }
            super.closeButtonGROUPToolbar(context);
        }

        protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
            Pdg_variazione_riga_gestBulk dett = (Pdg_variazione_riga_gestBulk) oggettobulk;
            validaRiga(actioncontext, dett);
            if (dett != null && dett.getIm_spese_gest_accentrata_int() != null && dett.getIm_spese_gest_decentrata_int() != null &&
                    dett.getIm_spese_gest_accentrata_int().compareTo(BigDecimal.ZERO) != 0 && dett.getIm_spese_gest_decentrata_int().compareTo(BigDecimal.ZERO) != 0)
                throw new ValidationException("Non è possibile indicare sulla stessa riga di variazione sia le spese Decentrate Esterne che le spese Accentrate Esterne, inserire un nuovo dettaglio.");
            else if (dett != null && dett.getIm_spese_gest_accentrata_est() != null && dett.getIm_spese_gest_decentrata_est() != null &&
                    dett.getIm_spese_gest_accentrata_est().compareTo(BigDecimal.ZERO) != 0 && dett.getIm_spese_gest_decentrata_est().compareTo(BigDecimal.ZERO) != 0)
                throw new ValidationException("Non è possibile indicare sulla stessa riga di variazione sia le spese Decentrate Esterne che le spese Accentrate Esterne, inserire un nuovo dettaglio.");
            else if (dett != null && dett.getIm_spese_gest_accentrata_int() != null && dett.getIm_spese_gest_accentrata_int().compareTo(BigDecimal.ZERO) < 0)
                throw new ValidationException("Non è possibile indicare un importo negativo per le spese Accentrare Interne, le restituzioni si registrano chiamando a partecipare alla variazione il CdR della SAC titolare della Spesa.");
            else if (dett != null && dett.getIm_spese_gest_accentrata_est() != null && dett.getIm_spese_gest_accentrata_est().compareTo(BigDecimal.ZERO) < 0)
                throw new ValidationException("Non è possibile indicare un importo negativo per le spese Accentrare Esterne, le restituzioni si registrano chiamando a partecipare alla variazione il CdR della SAC titolare della Spesa.");
            else {
                super.validate(actioncontext, oggettobulk);
                calcolaTotaleQuotaSpesaRipartita();
            }
        }

        public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
            calcolaTotaleQuotaSpesaRipartita();
            Ass_pdg_variazione_cdrBulk pdg = ((Ass_pdg_variazione_cdrBulk) getParentModel());
            Pdg_variazione_riga_gestBulk varRiga = (Pdg_variazione_riga_gestBulk) oggettobulk;
            pdg.setTotale_quota_spesa(Utility.nvl(pdg.getTotale_quota_spesa()).subtract(Utility.nvl(varRiga.getIm_variazione())));
            return super.removeDetail(oggettobulk, i);
        }

        public void calcolaTotaleQuotaSpesaRipartita() {
            Ass_pdg_variazione_cdrBulk pdg = ((Ass_pdg_variazione_cdrBulk) getParentModel());
            pdg.setTotale_quota_spesa(Utility.ZERO);
            for (Iterator righeVar = getDetails().iterator(); righeVar.hasNext(); ) {
                Pdg_variazione_riga_gestBulk varRiga = (Pdg_variazione_riga_gestBulk) righeVar.next();
                pdg.setTotale_quota_spesa(Utility.nvl(pdg.getTotale_quota_spesa()).add(Utility.nvl(varRiga.getIm_variazione())));
            }
        }
    };

    public CRUDPdgVariazioneRigaGestBP() {
        super();
    }


    public CRUDPdgVariazioneRigaGestBP(String function) {
        super(function);
    }

    public CRUDPdgVariazioneRigaGestBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr, Pdg_variazioneBulk pdg_variazione, String tipoGestione) {
        super(function);
        setCentro_responsabilita(cdr);
        setPdg_variazione(pdg_variazione);
        setTipoGestione(tipoGestione);
    }

    public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        super.save(context);
        Ass_pdg_variazione_cdrBulk ass = (Ass_pdg_variazione_cdrBulk) this.getModel();
        if (ass.getPdg_variazione().isApprovata()) {
            setStatus(VIEW);
        }
    }

    protected void validaRiga(ActionContext actioncontext,
                              Pdg_variazione_riga_gestBulk oggettobulk) throws ValidationException {
        for (java.util.Iterator i = ((Ass_pdg_variazione_cdrBulk) this.getModel()).getRigheVariazioneEtrGest().iterator(); i.hasNext(); ) {
            Pdg_variazione_riga_gestBulk riga = (Pdg_variazione_riga_gestBulk) i.next();
            if (!riga.equals(oggettobulk) &&
                    riga.getEsercizio().compareTo(oggettobulk.getEsercizio()) == 0 &&
                    riga.getCd_cdr_assegnatario().compareTo(oggettobulk.getCd_cdr_assegnatario()) == 0 &&
                    ((
                            (riga.getCd_cds_area() != null && oggettobulk.getCd_cds_area() != null) &&
                                    (riga.getCd_cds_area().compareTo(oggettobulk.getCd_cds_area()) == 0)) ||
                            (riga.getCd_cds_area() == null && oggettobulk.getCd_cds_area() == null) ||
                            (riga.getCd_cds_area() != null && riga.getCd_cds_area().compareTo(riga.getCdr_assegnatario().getCd_cds()) == 0 &&
                                    oggettobulk.getCd_cds_area() == null) ||
                            (oggettobulk.getCd_cds_area() != null && oggettobulk.getCd_cds_area().compareTo(oggettobulk.getCdr_assegnatario().getCd_cds()) == 0 &&
                                    riga.getCd_cds_area() == null)) &&
                    oggettobulk.getLinea_attivita() != null && riga.getLinea_attivita() != null &&
                    oggettobulk.getCd_linea_attivita() != null && riga.getCd_linea_attivita() != null &&
                    riga.getCd_linea_attivita().compareTo(oggettobulk.getCd_linea_attivita()) == 0 &&
                    riga.getCd_elemento_voce() != null && oggettobulk.getCd_elemento_voce() != null &&
                    riga.getCd_elemento_voce().compareTo(oggettobulk.getCd_elemento_voce()) == 0)
                throw new ValidationException("Attenzione: combinazione Esercizio/CdR/Area/G.A.E./Voce già inserita!");
        }

        Ass_pdg_variazione_cdrBulk ass = (Ass_pdg_variazione_cdrBulk) this.getModel();
        for (java.util.Iterator i = ((Ass_pdg_variazione_cdrBulk) this.getModel()).getRigheVariazioneSpeGest().iterator(); i.hasNext(); ) {
            Pdg_variazione_riga_gestBulk riga = (Pdg_variazione_riga_gestBulk) i.next();
            if (!riga.equals(oggettobulk) &&
                    riga.getEsercizio().compareTo(oggettobulk.getEsercizio()) == 0 &&
                    riga.getCd_cdr_assegnatario().compareTo(oggettobulk.getCd_cdr_assegnatario()) == 0 &&
                    ((
                            (riga.getCd_cds_area() != null && oggettobulk.getCd_cds_area() != null) &&
                                    (riga.getCd_cds_area().compareTo(oggettobulk.getCd_cds_area()) == 0)) ||
                            (riga.getCd_cds_area() == null && oggettobulk.getCd_cds_area() == null) ||
                            (riga.getCd_cds_area() != null && riga.getCd_cds_area().compareTo(riga.getCdr_assegnatario().getCd_cds()) == 0 &&
                                    oggettobulk.getCd_cds_area() == null) ||
                            (oggettobulk.getCd_cds_area() != null && oggettobulk.getCd_cds_area().compareTo(oggettobulk.getCdr_assegnatario().getCd_cds()) == 0 &&
                                    riga.getCd_cds_area() == null)) &&
                    oggettobulk.getLinea_attivita() != null && riga.getLinea_attivita() != null &&
                    oggettobulk.getCd_linea_attivita() != null && riga.getCd_linea_attivita() != null &&
                    riga.getCd_linea_attivita().compareTo(oggettobulk.getCd_linea_attivita()) == 0 &&
                    riga.getCd_elemento_voce() != null && oggettobulk.getCd_elemento_voce() != null &&
                    riga.getCd_elemento_voce().compareTo(oggettobulk.getCd_elemento_voce()) == 0)
                throw new ValidationException("Attenzione: combinazione Esercizio/CdR/Area/G.A.E./Voce già inserita!");
        }
    }

    /**
     * Imposta come attivi i tab di default.
     *
     * @param context <code>ActionContext</code>
     */
    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        super.init(config, context);
        resetTabs();
    }

    protected void resetTabs() {
        setTab("tab", "tabTotaliGest");
    }

    public boolean isNewButtonHidden() {
        return true;
    }

    public boolean isFreeSearchButtonHidden() {
        return true;
    }

    public boolean isSearchButtonHidden() {
        return true;
    }

    public boolean isDeleteDettagliButtonEnabled() {
        return isSaveButtonEnabled() &&
                ((isGestioneSpesa() &&
                        getRigheVariazioneSpeGest().countDetails() != 0) ||
                        (!isGestioneSpesa() &&
                                getRigheVariazioneEtrGest().countDetails() != 0));
    }

    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        edit(actioncontext, new Ass_pdg_variazione_cdrBulk(pdg_variazione.getEsercizio(), pdg_variazione.getPg_variazione_pdg(), centro_responsabilita.getCd_centro_responsabilita()));
        setUoScrivania(CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext()));
        if (it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(actioncontext) != null)
            setDipartimentoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(actioncontext));
        try {
            setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
        } catch (ComponentException e1) {
            throw handleException(e1);
        } catch (RemoteException e1) {
            throw handleException(e1);
        }
    }

    public Pdg_variazioneBulk getPdg_variazione() {
        return pdg_variazione;
    }

    public void setPdg_variazione(Pdg_variazioneBulk bulk) {
        pdg_variazione = bulk;
    }

    public String getUoScrivania() {
        return uoScrivania;
    }

    public void setUoScrivania(String string) {
        uoScrivania = string;
    }

    public String getTitle() {
        if (isGestioneSpesa())
            return ("Pdg Gestionale - Variazione Spesa");
        return ("Pdg Gestionale - Variazione Entrata");
    }

    /*
     * da verificare se eliminare
     */
    public boolean isDettagliGestionaliEnable(Pdg_variazioneBulk variazione) {
        return true;
    }

    /*
     * da verificare se eliminare
     */
    public boolean isDettaglioGestionaleEnable(Pdg_variazione_riga_gestBulk riga) {
        try {
            return isDettagliGestionaliEnable(riga.getPdg_variazione());
        } catch (NullPointerException e) {
            return true;
        }
    }

    /**
     * Ritorna TRUE se il CDR assegnatario appartiene ad una UO di tipo Area
     *
     * @param Pdg_variazione_riga_gestBulk La riga di variazione
     * @return boolean
     */
    public boolean isUoArea() {
        Ass_pdg_variazione_cdrBulk bulk = (Ass_pdg_variazione_cdrBulk) getModel();
        return bulk != null &&
                bulk.getCentro_responsabilita() != null &&
                bulk.getCentro_responsabilita().getUnita_padre() != null &&
                bulk.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA) == 0;
    }

    public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
        return centro_responsabilita;
    }


    public void setCentro_responsabilita(
            it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita) {
        this.centro_responsabilita = centro_responsabilita;
    }

    public SimpleDetailCRUDController getRigheVariazioneEtrGest() {
        return righeVariazioneEtrGest;
    }


    public void setRigheVariazioneEtrGest(SimpleDetailCRUDController righeVariazioneEtrGest) {
        this.righeVariazioneEtrGest = righeVariazioneEtrGest;
    }


    public SimpleDetailCRUDController getRigheVariazioneSpeGest() {
        return righeVariazioneSpeGest;
    }

    public void setRigheVariazioneSpeGest(
            SimpleDetailCRUDController righeVariazioneSpeGest) {
        this.righeVariazioneSpeGest = righeVariazioneSpeGest;
    }

    public SimpleDetailCRUDController getRigheVariazioneGestionale() {
        if (isGestioneSpesa())
            return righeVariazioneSpeGest;
        return righeVariazioneEtrGest;
    }

    private String getTipoGestione() {
        return tipoGestione;
    }

    private void setTipoGestione(String tipoGestione) {
        this.tipoGestione = tipoGestione;
    }

    public boolean isGestioneSpesa() {
        return getTipoGestione().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE);
    }

    public DipartimentoBulk getDipartimentoSrivania() {
        return dipartimentoSrivania;
    }

    private void setDipartimentoSrivania(
            DipartimentoBulk dipartimentoSrivania) {
        this.dipartimentoSrivania = dipartimentoSrivania;
    }

    public Pdg_variazione_riga_gestBulk preparaNuovaRiga(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        Pdg_variazione_riga_gestBulk riga = new Pdg_variazione_riga_spesa_gestBulk();
        Ass_pdg_variazione_cdrBulk bulk = (Ass_pdg_variazione_cdrBulk) getModel();
        if (isVariazioneApprovata(context)) {
            PdGVariazioniComponentSession comp = Utility.createPdGVariazioniComponentSession();
            try {
                Pdg_variazione_riga_spesa_gestBulk rigaDB = (Pdg_variazione_riga_spesa_gestBulk) comp.recuperoRigaLiquidazioneIva(context.getUserContext(), bulk);
                if (rigaDB != null) {
                    Elemento_voceBulk elementoVoce = (Elemento_voceBulk) createComponentSession().findByPrimaryKey(context.getUserContext(),
                            new Elemento_voceBulk(rigaDB.getElemento_voce().getCd_elemento_voce(), rigaDB.getElemento_voce().getEsercizio(), rigaDB.getElemento_voce().getTi_appartenenza(), rigaDB.getElemento_voce().getTi_gestione()));
                    if (elementoVoce.getV_classificazione_voci() != null) {
                        V_classificazione_vociBulk vClassDB = elementoVoce.getV_classificazione_voci();
                        V_classificazione_vociBulk vClass = (V_classificazione_vociBulk) createComponentSession().findByPrimaryKey(context.getUserContext(),
                                new V_classificazione_vociBulk(vClassDB.getId_classificazione()));
                        elementoVoce.setV_classificazione_voci(vClass);
                    }
                    riga.setElemento_voce(elementoVoce);
                }
            } catch (RemoteException e1) {
                throw handleException(e1);
            } catch (ComponentException e1) {
                throw handleException(e1);
            }
        }
        return riga;
    }

    public boolean isVariazioneApprovata(ActionContext context) {
        Ass_pdg_variazione_cdrBulk bulk = (Ass_pdg_variazione_cdrBulk) getModel();
        if (bulk.getPdg_variazione().isApprovata()) {
            return true;
        }
        return false;
    }


    public void apponiVistoDipartimento(ActionContext context, Pdg_variazione_riga_gestBulk riga) throws it.cnr.jada.action.BusinessProcessException {
        try {
            PdGVariazioniComponentSession comp = Utility.createPdGVariazioniComponentSession();
            riga = (Pdg_variazione_riga_gestBulk) comp.apponiVistoDipartimento(context.getUserContext(), riga, CNRUserInfo.getDipartimento(context));
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public Parametri_cnrBulk getParametriCnr() {
        return parametriCnr;
    }

    private void setParametriCnr(Parametri_cnrBulk parametriCnr) {
        this.parametriCnr = parametriCnr;
    }

    public void valorizzaProgettoLineaAttivita(ActionContext actioncontext, Pdg_variazione_riga_gestBulk riga) throws BusinessProcessException {
        try {
            if (riga.getLinea_attivita() != null && riga.getLinea_attivita().getCd_linea_attivita() != null)
                riga.setProgetto(((CRUDPdgVariazioneRigaGestComponentSession) createComponentSession()).getProgettoLineaAttivita(actioncontext.getUserContext(), riga));
            else
                riga.setProgetto(null);
        } catch (DetailedRuntimeException e) {
            throw new BusinessProcessException(e);
        } catch (ComponentException e) {
            throw new BusinessProcessException(e);
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }
}
