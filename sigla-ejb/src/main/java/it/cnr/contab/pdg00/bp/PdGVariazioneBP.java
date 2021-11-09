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

package it.cnr.contab.pdg00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazione_archivioBulk;
import it.cnr.contab.pdg00.bulk.V_pdg_variazione_riepilogoBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg01.bp.CRUDPdgVariazioneGestionaleBP;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Business Process per la gestione della testata delle variazioni al PDG
 */

public class PdGVariazioneBP extends it.cnr.jada.util.action.SimpleCRUDBP {

    private SimpleDetailCRUDController riepilogoEntrate = new SimpleDetailCRUDController("RiepilogoEntrate", V_pdg_variazione_riepilogoBulk.class, "riepilogoEntrate", this) {
    };

    private SimpleDetailCRUDController riepilogoSpese = new SimpleDetailCRUDController("RiepilogoSpese", V_pdg_variazione_riepilogoBulk.class, "riepilogoSpese", this) {
    };
    private SimpleDetailCRUDController crudArchivioCons = new SimpleDetailCRUDController("ArchivioConsultazioni", Pdg_variazione_archivioBulk.class, "archivioConsultazioni", this);
    private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita_scrivania;
    private it.cnr.contab.config00.sto.bulk.CdsBulk centro_di_spesa_scrivania;
    private Unita_organizzativaBulk uoSrivania;
    private DipartimentoBulk dipartimentoSrivania;
    private Integer annoFromPianoEconomico;
    private boolean uoRagioneria;

    private SimpleDetailCRUDController crudAssCDR = new SimpleDetailCRUDController("AssociazioneCDR", Ass_pdg_variazione_cdrBulk.class, "associazioneCDR", this) {
        public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
            if (!detail.isToBeCreated())
                validaAssociazioneCDRPerCancellazione(context, (Ass_pdg_variazione_cdrBulk) detail);
        }

        public void add(ActionContext actioncontext) throws BusinessProcessException {
            if (getParentController() instanceof CRUDPdgVariazioneGestionaleBP) {
                if (((Pdg_variazioneBulk) getParentModel()).getTipologia() == null)
                    ((SimpleCRUDBP) getParentController()).setMessage("Occorre valorizzare la tipologia della variazione prima di associare i CDR");
                else if (((Pdg_variazioneBulk) getParentModel()).getTipologia_fin() == null)
                    ((SimpleCRUDBP) getParentController()).setMessage("Occorre valorizzare l'origine delle fonti della variazione prima di associare i CDR");
                else
                    super.add(actioncontext);
            } else
                super.add(actioncontext);
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
                    ((Pdg_variazioneBulk) getParentController().getModel()).isApprovata() &&
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
    };
    private Parametri_cnrBulk parametriCnr;

    private boolean attivaGestioneVariazioniTrasferimento;
    private boolean abilitatoModificaDescVariazioni;

    public PdGVariazioneBP() {
        super();
    }

    public PdGVariazioneBP(String function) {
        super(function);
    }

    protected void resetTabs(it.cnr.jada.action.ActionContext context) {
        setTab("tab", "tabTestata");
    }

    /**
     * Crea la ProcedureComponentSession da usare per effettuare operazioni
     */
    public it.cnr.contab.util00.ejb.ProcedureComponentSession createProcedureComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (it.cnr.contab.util00.ejb.ProcedureComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTIL00_EJB_ProcedureComponentSession", it.cnr.contab.util00.ejb.ProcedureComponentSession.class);
    }

    /**
     * Crea la PdGComponentSession da usare per effettuare operazioni
     */
    public it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession createPdGPreventivoComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGPreventivoComponentSession", it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession.class);
    }

    /**
     * Crea la CdrComponentSession da usare per effettuare operazioni
     */
    public it.cnr.contab.config00.ejb.CDRComponentSession createCdrComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (it.cnr.contab.config00.ejb.CDRComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", it.cnr.contab.config00.ejb.CDRComponentSession.class);
    }

    protected void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
            setCentro_responsabilita_scrivania(createCdrComponentSession().cdrFromUserContext(context.getUserContext()));
            setAbilitatoModificaDescVariazioni(UtenteBulk.isAbilitatoModificaDescVariazioni(context.getUserContext()));
            setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
            if (it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context) != null)
                setDipartimentoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context));
            setAttivaGestioneVariazioniTrasferimento(Utility.createParametriEnteComponentSession().getParametriEnte(context.getUserContext()).getFl_variazioni_trasferimento());
            validaAccessoBP(context);

            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = Utility.createConfigurazioneCnrComponentSession();
            BigDecimal annoFrom = configSession.getIm01(context.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
            if (Optional.ofNullable(annoFrom).isPresent())
                setAnnoFromPianoEconomico(annoFrom.intValue());

            String uoRagioneria = Utility.createConfigurazioneCnrComponentSession().getUoRagioneria(context.getUserContext(),CNRUserContext.getEsercizio(context.getUserContext()));
            setUoRagioneria(Optional.ofNullable(uoRagioneria).map(el->el.equals(getCentro_responsabilita_scrivania().getCd_unita_organizzativa())).orElse(Boolean.FALSE));
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
        super.initialize(context);
    }

    protected void validaAccessoBP(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.action.BusinessProcessException {
        try {
            if (getParametriCnr().getFl_regolamento_2006())
                throw new it.cnr.jada.comp.ApplicationException("Utilizzo non consentito nel " + CNRUserContext.getEsercizio(context.getUserContext()));

            if (!isUoEnte()) {
                PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
                comp.controllaBilancioPreventivoCdsApprovato(context.getUserContext(), ((CNRUserInfo) context.getUserInfo()).getCdr());
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * @return
     */
    public SimpleDetailCRUDController getCrudAssCDR() {
        return crudAssCDR;
    }

    /**
     * @param controller
     */
    public void setCrudAssCDR(SimpleDetailCRUDController controller) {
        crudAssCDR = controller;
    }

    /**
     * @return
     */
    public SimpleDetailCRUDController getCrudArchivioCons() {
        return crudArchivioCons;
    }

    /**
     * @param controller
     */
    public void setCrudArchivioCons(SimpleDetailCRUDController controller) {
        crudArchivioCons = controller;
    }

    /**
     * @return
     */
    public it
            .cnr
            .contab
            .config00
            .sto
            .bulk
            .CdrBulk getCentro_responsabilita_scrivania() {
        return centro_responsabilita_scrivania;
    }

    /**
     * @param bulk
     */
    public void setCentro_responsabilita_scrivania(
            it.cnr.contab.config00.sto.bulk.CdrBulk bulk) {
        centro_responsabilita_scrivania = bulk;
    }

    public boolean isButtonDettagliEnabled() {
        try {
            Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk) getModel();
            Ass_pdg_variazione_cdrBulk ass_pdg_variazione = (Ass_pdg_variazione_cdrBulk) (pdg_variazione.getAssociazioneCDR().get(getCrudAssCDR().getSelection().getFocus()));
            if (ass_pdg_variazione.getCentro_responsabilita().equalsByPrimaryKey(getCentro_responsabilita_scrivania()))
                return true;
            return false;
        } catch (NullPointerException ex) {
            return false;
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            return false;
        }

    }

    /**
     * Verifica che il CDR della variazione PDG sia uguale al CDR di scrivania
     */
    public boolean isCdrScrivania() {
        if (getStatus() == SEARCH)
            return true;
        try {
            Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk) getModel();
            if (pdg_variazione.getCentro_responsabilita().equalsByPrimaryKey(getCentro_responsabilita_scrivania()))
                return true;
            return false;
        } catch (NullPointerException ex) {
            return false;
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public boolean isAnnullabile() {
        if (getStatus() == SEARCH)
            return true;
        return isApprovaButtonEnabled();
    }

    public boolean isSaveButtonEnabled() {
        Pdg_variazioneBulk pdgVariazione = (Pdg_variazioneBulk) getModel();
        if ((isUoEnte() || isCdrScrivania()) &&
                (pdgVariazione.isApprovata() || pdgVariazione.isApprovazioneFormale()) &&
                pdgVariazione.isMotivazioneVariazioneBandoPersonale() && pdgVariazione.getStorageMatricola() == null)
            return true;
        else if (!isAbilitatoModificaDescVariazioni() && pdgVariazione.isApprovata())
            return false;
        else if (isUoEnte() && this.abilitatoModificaDescVariazioni && pdgVariazione.isPropostaDefinitiva())
            return true;
        else
            return super.isSaveButtonEnabled() && (isCdrScrivania() || isUoEnte()) && !pdgVariazione.isVariazioneFirmata();
    }

    public boolean isDeleteButtonEnabled() {
        return super.isDeleteButtonEnabled() && (isCdrScrivania() || isUoEnte()) &&
                !((Pdg_variazioneBulk) getModel()).isApprovata() && !((Pdg_variazioneBulk) getModel()).isVariazioneFirmata();
    }

    /**
     * Metodo utilizzato per creare una toolbar applicativa personalizzata.
     *
     * @return toolbar Toolbar in uso
     */
    protected it.cnr.jada.util.jsp.Button[] createToolbar() {

        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[14];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.definitiveSave");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.approva");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.nonApprova");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.apponiVistoDipartimento");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.approvazioneFormale");
        return toolbar;
    }

    /**
     * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
     * Il bottone di SalvaDefinitivo è disponibile solo se:
     * - la proposta è provvisoria
     * - il CDR è di 1° Livello
     *
     * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
     */
    public boolean isSalvaDefinitivoButtonEnabled() {
        return (isSaveButtonEnabled() || (super.isSaveButtonEnabled() && ((Pdg_variazioneBulk) getModel()).isPropostaProvvisoria())) &&
                ((Pdg_variazioneBulk) getModel()).isPropostaProvvisoria() &&
                ((Pdg_variazioneBulk) getModel()).isNotNew() &&
                (getCentro_responsabilita_scrivania().getLivello().intValue() == 1 || isUoArea() || isUoSac()) &&
                ((Pdg_variazioneBulk) getModel()).getCentro_responsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds());
    }

    /**
     * Restituisce il valore della proprietà 'approvaButtonEnabled'
     * Il bottone di Approva è disponibile solo se:
     * - è attivo il bottone di salvataggio
     * - la proposta di variazione PDG è definitiva
     * - la UO che sta effettuando l'operazione è di tipo ENTE
     *
     * @return Il valore della proprietà 'approvaButtonEnabled'
     */
    public boolean isApprovaButtonEnabled() {

        return super.isSaveButtonEnabled() && ((Pdg_variazioneBulk) getModel()).isPropostaDefinitiva() &&
                (isUoEnte() ||
                        ((getCentro_responsabilita_scrivania().getLivello().intValue() == 1 || isUoArea()) &&
                                ((Pdg_variazioneBulk) getModel()).getCentro_responsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds()) &&
                                ((Pdg_variazioneBulk) getModel()).isCdsAbilitatoAdApprovare()));
    }

    /**
     * Restituisce il valore della proprietà 'nonApprovaButtonEnabled'
     * Il bottone di NonApprova è disponibile solo se:
     * - è attivo il bottone di salvataggio
     * - la proposta di variazione PDG è definitiva
     * - la UO che sta effettuando l'operazione è di tipo ENTE
     *
     * @return Il valore della proprietà 'nonApprovaButtonEnabled'
     */
    public boolean isNonApprovaButtonEnabled() {

        return super.isSaveButtonEnabled() && ((Pdg_variazioneBulk) getModel()).isPropostaDefinitiva() && isUoEnte();
    }

    /**
     * Gestione del salvataggio come definitiva di una variazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     * @throws ValidationException
     */
    public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        try {

            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) getModel();
            pdg.validate();
            pdg = comp.salvaDefinitivo(context.getUserContext(), (Pdg_variazioneBulk) getModel());
            edit(context, pdg);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Gestione del salvataggio come approvata di una variazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void approva(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {

            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            Pdg_variazioneBulk pdg = comp.approva(context.getUserContext(), (Pdg_variazioneBulk) getModel());
            edit(context, pdg);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Gestione del salvataggio come respinta di una variazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void respingi(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {

            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            Pdg_variazioneBulk pdg = comp.respingi(context.getUserContext(), (Pdg_variazioneBulk) getModel());
            edit(context, pdg);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public void inizializzaSommeCdR(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {

            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            comp.inizializzaSommeCdR(context.getUserContext(), (Pdg_variazioneBulk) getModel());
            edit(context, getModel());
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        aggiornaVariazioneDocumentale(context, (Pdg_variazioneBulk) bulk);
        super.basicEdit(context, bulk, doInitializeForEdit);

        if (getStatus() != VIEW) {
            Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) getModel();
            if (pdg != null &&
                    (pdg.isCancellatoLogicamente() || pdg.isRespinta() ||
                            (pdg.isApprovazioneFormale() && (!pdg.isMotivazioneVariazioneBandoPersonale() || pdg.getStorageMatricola() != null)))) {
                setStatus(VIEW);
            }
        }
    }

    /**
     * @return
     */
    public Unita_organizzativaBulk getUoSrivania() {
        return uoSrivania;
    }

    /**
     * @param bulk
     */
    public void setUoSrivania(Unita_organizzativaBulk bulk) {
        uoSrivania = bulk;
    }

    public boolean isUoEnte() {
        return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0);
    }

    public boolean isUoArea() {
        return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA) == 0);
    }

    public boolean isUoSac() {
        return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC) == 0);
    }

    /**
     * @return
     */
    public it
            .cnr
            .contab
            .config00
            .sto
            .bulk
            .CdsBulk getCentro_di_spesa_scrivania() {
        return centro_di_spesa_scrivania;
    }

    /**
     * @param bulk
     */
    public void setCentro_di_spesa_scrivania(
            it.cnr.contab.config00.sto.bulk.CdsBulk bulk) {
        centro_di_spesa_scrivania = bulk;
    }

    public void validaAssociazioneCDRPerCancellazione(ActionContext context, Ass_pdg_variazione_cdrBulk assBulk) throws ValidationException {
        try {
            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            comp.validaAssociazioneCDRPerCancellazione(context.getUserContext(), assBulk);
        } catch (Throwable e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public String controllaTotPropostoEntrataSpesa(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {

            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            return comp.controllaTotPropostoEntrataSpesa(context.getUserContext(), (Pdg_variazioneBulk) getModel());
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public boolean isAfButtonHidden() {
        return !isUoEnte() ||
                (getDipartimentoSrivania() != null &&
                        getDipartimentoSrivania().getCd_dipartimento() != null);
    }

    public boolean isAfButtonEnabled() {
        return true;
    }

    public boolean isAvdButtonHidden() {
        return !isUoEnte() ||
                (getDipartimentoSrivania() == null ||
                        getDipartimentoSrivania().getCd_dipartimento() == null);
    }

    public boolean isAvdButtonEnabled() {
        return true;
    }

    /**
     * @return
     */
    public Parametri_cnrBulk getParametriCnr() {
        return parametriCnr;
    }

    /**
     * @param bulk
     */
    public void setParametriCnr(Parametri_cnrBulk bulk) {
        parametriCnr = bulk;
    }

    public void statoPrecedente(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            edit(context, comp.statoPrecedente(context.getUserContext(), getModel()));
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public boolean isStatoPrecedenteButtonEnabled() {
        final Optional<Pdg_variazioneBulk> optPdg_variazioneBulk = Optional.ofNullable(getModel())
                .filter(Pdg_variazioneBulk.class::isInstance)
                .map(Pdg_variazioneBulk.class::cast);
        if (optPdg_variazioneBulk.isPresent()) {
            return (isSaveButtonEnabled() || (optPdg_variazioneBulk.get().isPropostaDefinitiva())) &&
                    optPdg_variazioneBulk.get().isPropostaDefinitiva() &&
                    optPdg_variazioneBulk.get().isNotNew() &&
                    !optPdg_variazioneBulk.get().isVariazioneFirmata() &&
                    (getCentro_responsabilita_scrivania().getLivello().intValue() == 1 || isUoArea() || isUoSac()) &&
                    optPdg_variazioneBulk.get().getCentro_responsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds());
        } else {
            return false;
        }
    }

    public boolean isAssestatoResiduoButtonHidden() {
        return true;
    }

    public boolean isEditableDettagliVariazione() {
        try {
            Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk) getModel();
            if (pdg_variazione != null && getCrudAssCDR().getSelection().getFocus() == -1) return false;
            Ass_pdg_variazione_cdrBulk ass_pdg_variazione = (Ass_pdg_variazione_cdrBulk) (pdg_variazione.getAssociazioneCDR().get(getCrudAssCDR().getSelection().getFocus()));
            return ass_pdg_variazione.getCentro_responsabilita().equalsByPrimaryKey(getCentro_responsabilita_scrivania()) &&
                    pdg_variazione.isPropostaProvvisoria();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /*
     * Serve per sapere se la variazione movimenta un Fondo di Spesa
     */
    public boolean isMovimentoSuFondi() {
        return getModel() != null &&
                ((Pdg_variazioneBulk) getModel()).getTipo_variazione() != null &&
                ((Pdg_variazioneBulk) getModel()).getTipo_variazione().isMovimentoSuFondi();
    }

    public void apponiVistoDipartimento(ActionContext context, Pdg_variazioneBulk pdg) throws it.cnr.jada.action.BusinessProcessException {
        try {
            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            pdg = (Pdg_variazioneBulk) comp.apponiVistoDipartimento(context.getUserContext(), pdg, CNRUserInfo.getDipartimento(context));
            edit(context, pdg);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public void apponiVistoDipartimento(ActionContext context, Ass_pdg_variazione_cdrBulk ass) throws it.cnr.jada.action.BusinessProcessException {
        try {
            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            ass = (Ass_pdg_variazione_cdrBulk) comp.apponiVistoDipartimento(context.getUserContext(), ass, CNRUserInfo.getDipartimento(context));
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public DipartimentoBulk getDipartimentoSrivania() {
        return dipartimentoSrivania;
    }

    private void setDipartimentoSrivania(
            DipartimentoBulk dipartimentoSrivania) {
        this.dipartimentoSrivania = dipartimentoSrivania;
    }

    public RemoteIterator findVariazioniForApposizioneVisto(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        try {
            return EJBCommonServices.openRemoteIterator(actioncontext, ((PdGVariazioniComponentSession) createComponentSession()).cercaVariazioniForApposizioneVisto(actioncontext.getUserContext(), compoundfindclause, oggettobulk));
        } catch (Exception exception) {
            throw handleException(exception);
        }
    }

    public SimpleDetailCRUDController getRiepilogoEntrate() {
        return riepilogoEntrate;
    }

    public void setRiepilogoEntrate(SimpleDetailCRUDController riepilogoEntrate) {
        this.riepilogoEntrate = riepilogoEntrate;
    }

    public SimpleDetailCRUDController getRiepilogoSpese() {
        return riepilogoSpese;
    }

    public void setRiepilogoSpese(SimpleDetailCRUDController riepilogoSpese) {
        this.riepilogoSpese = riepilogoSpese;
    }

    public boolean isAbilitatoModificaDescVariazioni() {
        Pdg_variazioneBulk pdg = (Pdg_variazioneBulk) getModel();
        if (pdg != null && pdg.isVariazioneFirmata())
            return false;
        else
            return abilitatoModificaDescVariazioni;
    }

    public void setAbilitatoModificaDescVariazioni(boolean abilitatoModificaDescVariazioni) {
        this.abilitatoModificaDescVariazioni = abilitatoModificaDescVariazioni;
    }

    public void aggiornaVariazioneDocumentale(ActionContext context, Pdg_variazioneBulk bulk) throws BusinessProcessException {
        try {
            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            comp.archiviaVariazioneDocumentale(context.getUserContext(), bulk);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isVariazioneFromLiquidazioneIvaDaModificare(ActionContext context, Pdg_variazioneBulk variazione) throws BusinessProcessException {
        try {
            PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession) createComponentSession();
            return comp.isVariazioneFromLiquidazioneIvaDaModificare(context.getUserContext(), variazione);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void aggiornaMotivazioneVariazione(ActionContext context) throws BusinessProcessException {
        Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk) this.getModel();
        pdgVar.setTiMotivazioneVariazione(Pdg_variazioneBulk.MOTIVAZIONE_GENERICO.equals(pdgVar.getMapMotivazioneVariazione())
                ? null
                : pdgVar.getMapMotivazioneVariazione());

        if (pdgVar.isMotivazioneVariazioneBandoPersonale())
            pdgVar.setIdMatricola(null);
        else if (pdgVar.isMotivazioneVariazioneProrogaPersonale() || pdgVar.isMotivazioneVariazioneAltreSpesePersonale())
            pdgVar.setIdBando(null);
        else {
            pdgVar.setIdMatricola(null);
            pdgVar.setIdBando(null);
        }
    }

    @Override
    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        OggettoBulk bulk = super.initializeModelForEdit(actioncontext, oggettobulk);
        Optional.ofNullable(bulk)
                .filter(Pdg_variazioneBulk.class::isInstance)
                .map(Pdg_variazioneBulk.class::cast)
                .ifPresent(el -> {
                    el.setMapMotivazioneVariazione(Optional.ofNullable(el.getTiMotivazioneVariazione()).orElse(Pdg_variazioneBulk.MOTIVAZIONE_GENERICO));
                    el.setStorageMatricola(el.getIdMatricola());
                    if (Optional.ofNullable(el.getProgettoRimodulazione()).isPresent())
                    	el.setProgettoRimodulatoForSearch(el.getProgettoRimodulazione().getProgetto());
                });
        return bulk;
    }

    public boolean isAttivaGestioneVariazioniTrasferimento() {
        return attivaGestioneVariazioniTrasferimento;
    }

    private void setAttivaGestioneVariazioniTrasferimento(boolean attivaGestioneVariazioniTrasferimento) {
        this.attivaGestioneVariazioniTrasferimento = attivaGestioneVariazioniTrasferimento;
    }
    
    public void findAndSetRimodulazione(ActionContext actioncontext, ProgettoBulk progetto) throws BusinessProcessException {
    	try {
    		if (Optional.ofNullable(progetto).isPresent()) {
	    		List<Progetto_rimodulazioneBulk> list = new BulkList<Progetto_rimodulazioneBulk>(this.createComponentSession().find(actioncontext.getUserContext(), ProgettoBulk.class, "findRimodulazioni", progetto.getPg_progetto()));
	    		((Pdg_variazioneBulk)this.getModel()).setProgettoRimodulazione(list.stream().filter(Progetto_rimodulazioneBulk::isStatoValidato).findFirst().orElse(null));
    		}
    	} catch (Throwable e) {
	        throw handleException(e);
	    }
    }
    
    public String[][] getTabs(HttpSession session) {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;

        pages.put(i++, new String[]{"tabTestata", "Testata", "/pdg00/tab_pdg_variazione_testata.jsp"});
        pages.put(i++, new String[]{"tabCDR", "CDR abilitati a concorrervi", "/pdg00/tab_ass_pdg_variazione_cdr.jsp"});
        
        if (Optional.ofNullable(this.getAnnoFromPianoEconomico())
        			.filter(el->el.compareTo(CNRUserContext.getEsercizio(HttpActionContext.getUserContext(session)))<=0)
        			.isPresent())
        	pages.put(i++, new String[]{"tabRimodulazione", "Rimodulazione Progetto", "/pdg00/tab_pdg_variazione_rimodulazione.jsp"});

        pages.put(i++, new String[]{"tabArchivio", "Archivio Consultazioni", "/pdg00/tab_pdg_variazione_archivio.jsp"});
        pages.put(i++, new String[]{"tabRiepilogo", "Riepilogo per CdR/Dipartimento", "/pdg00/tab_pdg_variazione_riepilogo.jsp"});

        String[][] tabs = new String[i][3];

        for (int j = 0; j < i; j++)
            tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
        return tabs;
    }
    
    protected Integer getAnnoFromPianoEconomico() {
        return annoFromPianoEconomico;
    }

    private void setAnnoFromPianoEconomico(Integer annoFromPianoEconomico) {
        this.annoFromPianoEconomico = annoFromPianoEconomico;
    }

    public boolean isUoRagioneria() {
        return uoRagioneria;
    }

    private void setUoRagioneria(boolean uoRagioneria) {
        this.uoRagioneria = uoRagioneria;
    }
}