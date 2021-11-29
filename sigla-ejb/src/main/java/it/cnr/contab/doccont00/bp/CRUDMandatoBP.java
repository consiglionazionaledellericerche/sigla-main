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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.IDocumentoAmministrativoSpesaComponentSession;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.utenze00.action.GestioneUtenteAction;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Mandato
 */

public class CRUDMandatoBP extends CRUDAbstractMandatoBP implements IDocumentoAmministrativoBP, IDefferedUpdateSaldiBP {
    public static final String MANDATO_VARIAZIONE_BP = "CRUDMandatoVariazioneBP";
    private final SimpleDetailCRUDController documentiPassivi = new SimpleDetailCRUDController("DocumentiPassivi", V_doc_passivo_obbligazioneBulk.class, "docPassiviColl", this);
    private final CRUDMandatoRigaController documentiPassiviSelezionati = new CRUDMandatoRigaController("DocumentiPassiviSelezionati", Mandato_rigaIBulk.class, "mandato_rigaColl", this);
    private final SimpleDetailCRUDController documentiAttiviPerRegolarizzazione = new SimpleDetailCRUDController("DocumentiAttiviPerRegolarizzazione", V_doc_attivo_accertamentoBulk.class, "docGenericiPerRegolarizzazione", this);
    private final SimpleDetailCRUDController scadenzeAccertamentoPerRegolarizzazione = new SimpleDetailCRUDController("ScadenzeAccertamentoPerRegolarizzazione", Accertamento_scadenzarioBulk.class, "scadenzeAccertamentoPerRegolarizzazione", this);
    private final SimpleDetailCRUDController codiciSiopeCollegati = new SimpleDetailCRUDController("codiciSiopeCollegati", Mandato_siopeBulk.class, "mandato_siopeColl", documentiPassiviSelezionati);
    private final SimpleDetailCRUDController codiciSiopeCollegabili = new SimpleDetailCRUDController("codici_siopeColl", Codici_siopeBulk.class, "codici_siopeColl", documentiPassiviSelezionati);
    private final SimpleDetailCRUDController cupCollegati = new SimpleDetailCRUDController("cupCollegati", MandatoCupIBulk.class, "mandatoCupColl", documentiPassiviSelezionati) {
        public void validate(ActionContext context, OggettoBulk model) throws ValidationException {
            validateCupCollegati(context, model);
        }

        @Override
        public boolean isShrinkable() {
            return super.isShrinkable() || isDaVariare();
        }

        @Override
        public boolean isGrowable() {
            return super.isGrowable() || isDaVariare();
        }

        @Override
        public int getStatus() {
            if (isDaVariare())
                return EDIT;
            return super.getStatus();
        }
    };
    private final SimpleDetailCRUDController siopeCupCollegati = new SimpleDetailCRUDController("siopeCupCollegati", MandatoSiopeCupIBulk.class, "mandatoSiopeCupColl", codiciSiopeCollegati) {
        public void validate(ActionContext context, OggettoBulk model) throws ValidationException {
            validateSiopeCupCollegati(context, model);
        }

        @Override
        public boolean isShrinkable() {
            return super.isShrinkable() || isDaVariare();
        }

        @Override
        public boolean isGrowable() {
            return super.isGrowable() || isDaVariare();
        }

        @Override
        public int getStatus() {
            if (isDaVariare())
                return EDIT;
            return super.getStatus();
        }
    };
    protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
    boolean isAbilitatoCrudMandatoVariazioneBP = Boolean.FALSE;
    private boolean siope_attiva = false;
    private boolean cup_attivo = false;
    private boolean siope_cup_attivo = false;
    private boolean tesoreria_unica = false;
    private MandatoBulk mandatoRiaccredito;

    public CRUDMandatoBP() {
        super();
        setTab("tab", "tabMandato");
    }

    public CRUDMandatoBP(String function) {
        super(function);
        setTab("tab", "tabMandato");
    }

    public CRUDMandatoBP(String function, MandatoBulk mandatoRiaccredito) {
        super(function);
        setTab("tab", "tabMandato");
        this.mandatoRiaccredito = mandatoRiaccredito;
    }

    @Override
    public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        final MandatoBulk mandatoBulk = (MandatoBulk) super.initializeModelForInsert(actioncontext, oggettobulk);
        if (Optional.ofNullable(mandatoRiaccredito).isPresent()) {
            mandatoBulk.setStatoVarSos(StatoVariazioneSostituzione.SOSTITUZIONE_DEFINITIVA.value());
            mandatoBulk.setIm_mandato(mandatoRiaccredito.getIm_mandato());
            mandatoBulk.setDs_mandato(mandatoRiaccredito.getDs_mandato());
        }
        return mandatoBulk;
    }

    @Override
    public String getFormTitle() {
        if (isDaVariare()) {
            return " - Variazione";
        } else {
            return super.getFormTitle();
        }
    }

    /**
     * Metodo utilizzato per gestire l'aggiunta dei documenti passivi.
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */

    public void aggiungiDocPassivi(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoIBulk mandato = (MandatoIBulk) getModel();
            if (getDocumentiPassivi().getSelectedModels(context).size() != 0) {
                mandato = (MandatoIBulk) ((MandatoComponentSession) createComponentSession()).aggiungiDocPassivi(context.getUserContext(), mandato, getDocumentiPassivi().getSelectedModels(context));
                setModel(context, mandato);
                getDocumentiPassivi().getSelection().clear();
                resyncChildren(context);
            } else
                setMessage("Non sono stati selezionati documenti passivi");
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Metodo utilizzato per caricare l'elenco dei doc. amm. attivi associati all'accertamento.
     *
     * @param context <code>ActionContext</code> in uso.
     */

    public void caricaDocAttiviPerRegolarizzazione(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoIBulk mandato = (MandatoIBulk) getModel();
            mandato = ((MandatoComponentSession) createComponentSession()).listaDocAttiviPerRegolarizzazione(context.getUserContext(), mandato);
            setModel(context, mandato);
            getDocumentiAttiviPerRegolarizzazione().getSelection().clear();
            resyncChildren(context);
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Metodo utilizzato per gestire il caricamento dei documenti passivi.
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     * @throws <code>BusinessProcessException</code>
     */

    public void cercaDocPassivi(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        MandatoIBulk mandatoI = (MandatoIBulk) getModel();
        try {

            MandatoComponentSession session = (MandatoComponentSession) createComponentSession();
            // MandatoBulk mandato = session.listaDocPassivi( context.getUserContext(), (MandatoBulk) getModel() );
            mandatoI = (MandatoIBulk) session.listaDocPassivi(context.getUserContext(), (MandatoBulk) getModel());

            setModel(context, mandatoI);
            resyncChildren(context);
        } catch (Exception e) {
            mandatoI.setDocPassiviColl(new ArrayList());
            setModel(context, mandatoI);
            resyncChildren(context);
            throw handleException(e);
        }
    }

    public void create(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            if (getDocumentiAttiviPerRegolarizzazione().getSelectedModels(context).size() > 0) {
                ((MandatoIBulk) getModel()).setDocGenericiSelezionatiPerRegolarizzazione(getDocumentiAttiviPerRegolarizzazione().getSelectedModels(context));
                ((MandatoIBulk) getModel()).setGeneraReversaleDaDocAmm(true);
            } else {
                ((MandatoIBulk) getModel()).setGeneraReversaleDaDocAmm(false);
                if (((MandatoIBulk) getModel()).getTi_mandato().equals(MandatoIBulk.TIPO_REGOLARIZZAZIONE)) {
                    if (getScadenzeAccertamentoPerRegolarizzazione().getSelectedModels(context).size() > 0)
                        ((MandatoIBulk) getModel()).setScadenzeAccertamentoSelezionatePerRegolarizzazione(getScadenzeAccertamentoPerRegolarizzazione().getSelectedModels(context));
                    else
                        throw new ValidationException("Operazione non possibile! Non e' stata selezionata nessuna scadenza dell'accertamento.");
                }
            }

            super.create(context);

            if (((MandatoIBulk) getModel()).getTi_mandato().equals(MandatoIBulk.TIPO_REGOLARIZZAZIONE) &&
                    ((MandatoIBulk) getModel()).getVar_bilancio() != null)
                ((MandatoComponentSession) createComponentSession()).esitaVariazioneBilancioDiRegolarizzazione(context.getUserContext(), ((MandatoIBulk) getModel()));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per creare una toolbar applicativa personalizzata.
     *
     * @return toolbar Toolbar in uso
     */

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        final Properties properties = Config.getHandler().getProperties(getClass());
        return Stream.concat(Arrays.asList(super.createToolbar()).stream(),
                Arrays.asList(
                        new Button(properties, "CRUDToolbar.printpdf"),
                        new Button(properties, "CRUDToolbar.contabile"),
                        new Button(properties, "CRUDToolbar.davariare"),
                        new Button(properties, "CRUDToolbar.save.variazione.sostituzione")
                ).stream()).toArray(Button[]::new);
    }

    public boolean isDaVariareButtonHidden() {
        if (!isAbilitatoCrudMandatoVariazioneBP)
            return Boolean.TRUE;
        final Optional<MandatoBulk> mandatoBulk1 = Optional.ofNullable(getModel())
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .filter(mandatoBulk -> mandatoBulk.getCrudStatus() == OggettoBulk.NORMAL);
        return !isSupervisore() ||
                mandatoBulk1
                        .flatMap(mandatoBulk -> Optional.ofNullable(mandatoBulk.getEsitoOperazione()))
                        .map(s -> !Arrays.asList(
                                EsitoOperazione.ACQUISITO.value(),
                                EsitoOperazione.PAGATO.value(),
                                EsitoOperazione.REGOLARIZZATO.value()
                        ).contains(s)).orElse(Boolean.TRUE)
                || mandatoBulk1
                .map(mandatoBulk -> {
                    return Optional.ofNullable(mandatoBulk.getStatoVarSos())
                            .map(s -> Arrays.asList(
                                    StatoVariazioneSostituzione.DA_VARIARE.value(),
                                    StatoVariazioneSostituzione.VARIAZIONE_TRASMESSA.value()
                            ).contains(s))
                            .orElse(Boolean.FALSE);
                }).orElse(Boolean.TRUE);
    }

    public boolean isDaVariare() {
        return isSupervisore() &&
                Optional.ofNullable(getModel())
                        .filter(MandatoBulk.class::isInstance)
                        .map(MandatoBulk.class::cast)
                        .flatMap(mandatoBulk -> Optional.ofNullable(mandatoBulk.getStatoVarSos()))
                        .map(s -> Arrays.asList(
                                StatoVariazioneSostituzione.DA_VARIARE.value()
                        ).contains(s)).orElse(Boolean.FALSE);
    }

    public boolean isSalvaVariazioneSostituzioneButtonHidden() {
        return !isDaVariare();
    }

    public void impostaMandatoDaVariare(ActionContext actionContext) throws it.cnr.jada.action.BusinessProcessException {
        final MandatoBulk mandatoBulk = Optional.ofNullable(getModel())
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Mandato non trovato!"));
        CRUDMandatoVariazioneBP crudMandatoVariazioneBP =
                Optional.ofNullable(actionContext.createBusinessProcess("CRUDMandatoVariazioneBP"))
                        .filter(CRUDMandatoVariazioneBP.class::isInstance)
                        .map(CRUDMandatoVariazioneBP.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Non è possibile procedere alla variazione del Manadato"));
        crudMandatoVariazioneBP.setModel(actionContext, mandatoBulk);
        crudMandatoVariazioneBP.setDaVariare(actionContext);
        actionContext.closeBusinessProcess();
        actionContext.addBusinessProcess(crudMandatoVariazioneBP);
    }


    @Override
    public void basicEdit(ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit) throws BusinessProcessException {
        super.basicEdit(context, bulk, doInitializeForEdit);
        final MandatoBulk mandatoBulk = Optional.ofNullable(getModel())
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Mandato non trovato!"));

        mandatoBulk.setCdUoScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

        if (Optional.ofNullable(mandatoBulk.getStatoVarSos())
                .map(s -> s.equals(StatoVariazioneSostituzione.DA_VARIARE.value()))
                .orElse(Boolean.FALSE)) {
            if (!isAbilitatoCrudMandatoVariazioneBP) {
                setModel(context, createEmptyModelForSearch(context));
                setStatus(SEARCH);
                setMessage(ERROR_MESSAGE, "Mandato in stato 'DA VARIARE', accesso non consentito!");
            } else {
                CRUDMandatoVariazioneBP crudMandatoVariazioneBP =
                        Optional.ofNullable(context.createBusinessProcess("CRUDMandatoVariazioneBP", new Object[]{"M"}))
                                .filter(CRUDMandatoVariazioneBP.class::isInstance)
                                .map(CRUDMandatoVariazioneBP.class::cast)
                                .orElseThrow(() -> new BusinessProcessException("Non è possibile procedere alla variazione del Mandato"));
                crudMandatoVariazioneBP.setModel(context, mandatoBulk);
                context.closeBusinessProcess();
                context.addBusinessProcess(crudMandatoVariazioneBP);
            }
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/11/2002 11.44.11)
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDocumentiAttiviPerRegolarizzazione() {
        return documentiAttiviPerRegolarizzazione;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>documentiPassivi</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDocumentiPassivi() {
        return documentiPassivi;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>documentiPassiviSelezionati</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDocumentiPassiviSelezionati() {
        return documentiPassiviSelezionati;
    }

    /**
     * Abilito il bottone di dettaglio della fattura solo se il mandato e' in fase di modifica/inserimento
     * <p>
     * isEditable 	= FALSE se il mandato e' in visualizzazione
     * = TRUE se il mandato e' in modifica/inserimento
     */

    public boolean isDettaglioFatturaPerDoc_passivoEnabled() {
        return isEditable() && (isEditing() || isInserting()) && (getDocumentiPassivi().getSelection().getFocus() >= 0);
    }

    /**
     * Abilito il bottone di dettaglio della fattura solo se il mandato e' in fase di modifica/inserimento
     * <p>
     * isEditable 	= FALSE se il mandato e' in visualizzazione
     * = TRUE se il mandato e' in modifica/inserimento
     */

    public boolean isDettaglioFatturaPerMandato_rigaEnabled() {
        return isEditable() && (isEditing() || isInserting()) && (getDocumentiPassiviSelezionati().getSelection().getFocus() >= 0);
    }

    /**
     * Abilito il bottone di disponibilità di cassa per capitolo
     * <p>
     * isEditable 	= FALSE se il mandato non ha righe
     * = TRUE se il mandato ha righe
     */

    public boolean isDispCassaCapitoloButtonEnabled() {
        boolean soloPgiro = true;
        for (Iterator i = ((MandatoBulk) getModel()).getMandato_rigaColl().iterator(); i.hasNext(); )
            if (!((Mandato_rigaBulk) i.next()).getFl_pgiro().booleanValue()) {
                soloPgiro = false;
                break;
            }
        return !soloPgiro && ((MandatoBulk) getModel()).getMandato_rigaColl().size() > 0;
    }

    /**
     * Abilito il tab di ricerca dei documenti solo se il mandato e' in fase di modifica/inserimento
     * e non è stato pagato o annullato.
     * <p>
     * isEditable 	= FALSE se il mandato e' in visualizzazione
     * = TRUE se il mandato e' in modifica/inserimento
     */
    public boolean isRicercaDocumentiTabEnabled() {
        return isEditable() && !((MandatoBulk) getModel()).isPagato() && !((MandatoBulk) getModel()).isAnnullato();
    }

    /**
     * Inzializza il ricevente nello stato di SEARCH.
     *
     * @param context <code>ActionContext</code> in uso.
     */
    public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            super.resetForSearch(context);
            setTab("tab", "tabMandato");
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    /**
     * Effettua un salvataggio del modello corrente.
     * Valido solo se il ricevente è nello stato di INSERT o EDIT.
     *
     * @param context <code>ActionContext</code> in uso.
     */
    public void save(ActionContext context) throws ValidationException, BusinessProcessException {
        final MandatoBulk mandatoBulk = Optional.ofNullable(getModel())
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .orElseThrow(() -> new ValidationException("Modello non trovato!"));
        final boolean daVariare = isDaVariare();
        final String statoTrasmissione = mandatoBulk.getStato_trasmissione();

        final boolean isProbabileVariataDtPagamentoRich = this.isViewing() && !mandatoBulk.isRODtPagamentoRichiesta();

        if (daVariare || isProbabileVariataDtPagamentoRich) {
            setStatus(EDIT);
            if (daVariare) {
                mandatoBulk.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                mandatoBulk.setStatoVarSos(StatoVariazioneSostituzione.VARIAZIONE_DEFINITIVA.value());
            }
        }
        try {
            super.save(context);
        } catch (Exception _ex) {
            if (daVariare || isProbabileVariataDtPagamentoRich) {
                setStatus(VIEW);
                if (daVariare) {
                    mandatoBulk.setStatoVarSos(StatoVariazioneSostituzione.DA_VARIARE.value());
                    mandatoBulk.setStato_trasmissione(statoTrasmissione);
                }
            }
            throw handleException(_ex);
        }
        this.setTab("tab", "tabMandato");
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>codiciSiopeCollegati</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCodiciSiopeCollegati() {
        return codiciSiopeCollegati;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>codiciSiopeCollegabili</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCodiciSiopeCollegabili() {
        return codiciSiopeCollegabili;
    }

    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        try {
            setSiope_attiva(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_siope().booleanValue());
            setCup_attivo(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_cup().booleanValue());
            setSiope_cup_attivo(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_siope_cup().booleanValue());
            setTesoreria_unica(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_tesoreria_unica().booleanValue());
            setSupervisore(Utility.createUtenteComponentSession().isSupervisore(actioncontext.getUserContext()));
            final CNRUserInfo cnrUserInfo = Optional.ofNullable(actioncontext)
                    .flatMap(actionContext -> Optional.ofNullable(actionContext.getUserInfo()))
                    .filter(CNRUserInfo.class::isInstance)
                    .map(CNRUserInfo.class::cast)
                    .orElseThrow(() -> new BusinessProcessException("Cannot find UserInfo in context"));
            final Unita_organizzativa_enteBulk uoEnte = Optional.ofNullable(Utility.createUnita_organizzativaComponentSession().getUoEnte(actioncontext.getUserContext()))
                    .filter(Unita_organizzativa_enteBulk.class::isInstance)
                    .map(Unita_organizzativa_enteBulk.class::cast)
                    .orElseThrow(() -> new BusinessProcessException("Unita ENTE non trovata"));

            isAbilitatoCrudMandatoVariazioneBP = Optional.ofNullable(GestioneUtenteAction.getComponentSession()
                    .validaBPPerUtente(actioncontext.getUserContext(),
                            cnrUserInfo.getUtente(),
                            uoEnte.getCd_unita_organizzativa(), MANDATO_VARIAZIONE_BP)).isPresent();

        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    public boolean isSiope_attiva() {
        return siope_attiva;
    }

    private void setSiope_attiva(boolean siope_attiva) {
        this.siope_attiva = siope_attiva;
    }

    public boolean isSiopeBloccante(ActionContext actioncontext) throws BusinessProcessException {
        try {

            return ((MandatoBulk) getModel()).getUnita_organizzativa().getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_SAC) ||
                    ((MandatoBulk) getModel()).getUnita_organizzativa().equalsByPrimaryKey(Utility.createUnita_organizzativaComponentSession().getUoEnte(actioncontext.getUserContext()));
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public boolean isAggiungiRimuoviCodiciSiopeEnabled() {
        return (!isInputReadonly() &&
                getStatus() != VIEW &&
                ((MandatoBulk) getModel()).getStato_trasmissione() != null &&
                ((MandatoBulk) getModel()).getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO)) ||
                isDaVariare();
    }

    public void selezionaRigaSiopeDaCompletare(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
        MandatoBulk mandato = (MandatoBulk) getModel();
        Mandato_rigaBulk rigaDaCompletare = null;
        if (mandato != null) {
            mandato:
            for (Iterator i = mandato.getMandato_rigaColl().iterator(); i.hasNext(); ) {
                Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
                if (!riga.getTipoAssociazioneSiope().equals(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO)) {
                    rigaDaCompletare = riga;
                    break mandato;
                }
            }
        }
        if (rigaDaCompletare != null) {
            documentiPassiviSelezionati.getSelection().setFocus(documentiPassiviSelezionati.getDetails().indexOf(rigaDaCompletare));
            documentiPassiviSelezionati.setModelIndex(actioncontext, documentiPassiviSelezionati.getDetails().indexOf(rigaDaCompletare));
            resyncChildren(actioncontext);
        }
    }

    public SimpleDetailCRUDController getScadenzeAccertamentoPerRegolarizzazione() {
        return scadenzeAccertamentoPerRegolarizzazione;
    }

    /**
     * Metodo utilizzato per caricare l'elenco delle scadenze dell'accertamento per la regolarizzazione.
     *
     * @param context <code>ActionContext</code> in uso.
     */

    public void caricaScadenzeAccertamentoPerRegolarizzazione(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MandatoIBulk mandato = (MandatoIBulk) getModel();
            mandato = ((MandatoComponentSession) createComponentSession()).listaScadenzeAccertamentoPerRegolarizzazione(context.getUserContext(), mandato);
            setModel(context, mandato);
            getScadenzeAccertamentoPerRegolarizzazione().getSelection().clear();
            resyncChildren(context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public SimpleDetailCRUDController getCupCollegati() {
        return cupCollegati;
    }

    private void validateCupCollegati(ActionContext context, OggettoBulk model) throws ValidationException {
        try {
            if (getCupCollegati() != null && getCupCollegati().getModel() != null) {
                getCupCollegati().getModel().validate(getModel());
                completeSearchTools(context, this);
            }
        } catch (BusinessProcessException e) {
            handleException(e);
        }

        MandatoCupBulk bulk = (MandatoCupBulk) model;
        BigDecimal tot_col = BigDecimal.ZERO;
        if (bulk != null && bulk.getMandato_riga() != null && bulk.getMandato_riga().getMandatoCupColl() != null && !bulk.getMandato_riga().getMandatoCupColl().isEmpty()) {
            if (bulk.getCdCup() == null)
                throw new ValidationException("Attenzione. Il codice Cup è obbligatorio");
            if (bulk.getImporto() == null)
                throw new ValidationException("Attenzione. L'importo associato al codice Cup è obbligatorio");

            BulkList list = bulk.getMandato_riga().getMandatoCupColl();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                MandatoCupBulk l = (MandatoCupBulk) i.next();
                if (l.getCdCup() != null) {
                    if (bulk != l && bulk.getCdCup().compareTo(l.getCdCup()) == 0)
                        throw new ValidationException("Attenzione. Ogni Cup può essere utilizzato una sola volta per ogni riga del mandato. ");
                    tot_col = tot_col.add(l.getImporto());
                }
            }
            if (tot_col.compareTo(bulk.getMandato_riga().getIm_mandato_riga()) > 0)
                throw new ValidationException("Attenzione. Il totale associato al CUP è superiore all'importo della riga del mandato.");
        }

    }

    private void validateSiopeCupCollegati(ActionContext context, OggettoBulk model) throws ValidationException {
        try {
            if (getSiopeCupCollegati() != null && getSiopeCupCollegati().getModel() != null) {
                getSiopeCupCollegati().getModel().validate(getModel());
                completeSearchTools(context, this);
            }
        } catch (BusinessProcessException e) {
            handleException(e);
        }

        MandatoSiopeCupBulk bulk = (MandatoSiopeCupBulk) model;
        BigDecimal tot_col = BigDecimal.ZERO;
        if (bulk != null && bulk.getMandatoSiope() != null && bulk.getMandatoSiope().getMandatoSiopeCupColl() != null && !bulk.getMandatoSiope().getMandatoSiopeCupColl().isEmpty()) {
            if (bulk.getCdCup() == null)
                throw new ValidationException("Attenzione. Il codice Cup è obbligatorio");
            if (bulk.getImporto() == null)
                throw new ValidationException("Attenzione. L'importo associato al codice Cup è obbligatorio");

            BulkList list = bulk.getMandatoSiope().getMandatoSiopeCupColl();
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                MandatoSiopeCupBulk l = (MandatoSiopeCupBulk) i.next();
                if (l.getCdCup() != null) {
                    if (bulk != l && bulk.getCdCup().compareTo(l.getCdCup()) == 0)
                        throw new ValidationException("Attenzione. Ogni Cup può essere utilizzato una sola volta per ogni riga di mandato/siope. ");
                    tot_col = tot_col.add(l.getImporto());
                }
            }
            if (tot_col.compareTo(bulk.getMandatoSiope().getImporto()) > 0)
                throw new ValidationException("Attenzione. Il totale associato al CUP è superiore all'importo della riga del mandato associato al siope.");
        }
    }

    public boolean isCup_attivo() {
        return cup_attivo;
    }

    public void setCup_attivo(boolean cup_attivo) {
        this.cup_attivo = cup_attivo;
    }

    public boolean isNewButtonEnabled() {
        if (((MandatoBulk) getModel()).getUnita_organizzativa().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0 &&
                (((MandatoBulk) getModel()).getTi_mandato() == null ||
                        !((MandatoBulk) getModel()).getTi_mandato().equals(((MandatoBulk) getModel()).TIPO_REGOLARIZZAZIONE)))
            return false;
        else
            return super.isNewButtonEnabled();

    }

    public boolean isDeleteButtonEnabled() {
        if (((MandatoBulk) getModel()).getUnita_organizzativa().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0 &&
                ((MandatoBulk) getModel()).getCd_uo_origine() != null && (((MandatoBulk) getModel()).getCd_uo_origine().compareTo(((MandatoBulk) getModel()).getCd_unita_organizzativa()) == 0) &&
                (((MandatoBulk) getModel()).getTi_mandato() == null ||
                        !((MandatoBulk) getModel()).getTi_mandato().equals(((MandatoBulk) getModel()).TIPO_REGOLARIZZAZIONE)))
            return false;
        else
            return super.isDeleteButtonEnabled();

    }

    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
                    .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");

            this.attivoSiopeplus = Optional.ofNullable(sess.getVal01(
                    context.getUserContext(),
                    CNRUserInfo.getEsercizio(context),
                    null,
                    Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                    Configurazione_cnrBulk.SK_ATTIVO_SIOPEPLUS))
                    .map(s -> Boolean.valueOf(s))
                    .orElse(Boolean.FALSE);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
        super.init(config, context);
        //se entro dalla 999 in gestione mandato non devo entrare in inserimento
        if (((MandatoBulk) getModel()).getUnita_organizzativa().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0 &&
                !isSearching() &&
                !((MandatoBulk) getModel()).getTi_mandato().equals(((MandatoBulk) getModel()).TIPO_REGOLARIZZAZIONE)) {
            setStatus(SEARCH);
            resetForSearch(context);
        }

    }

    public boolean isSiope_cup_attivo() {
        return siope_cup_attivo;
    }

    public void setSiope_cup_attivo(boolean siope_cup_attivo) {
        this.siope_cup_attivo = siope_cup_attivo;
    }

    public SimpleDetailCRUDController getSiopeCupCollegati() {
        return siopeCupCollegati;
    }

    public boolean isTesoreria_unica() {
        return tesoreria_unica;
    }

    public void setTesoreria_unica(boolean tesoreria_unica) {
        this.tesoreria_unica = tesoreria_unica;
    }


    private MandatoComponentSession getMandatoComponentSession() throws BusinessProcessException {
        return Optional.ofNullable(createComponentSession())
                .filter(MandatoComponentSession.class::isInstance)
                .map(MandatoComponentSession.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Errore nella creazione dell'EJB"));
    }

    private CRUDComponentSession getCRUDComponentSession() throws BusinessProcessException {
        return Optional.ofNullable(createComponentSession("JADAEJB_CRUDComponentSession"))
                .filter(CRUDComponentSession.class::isInstance)
                .map(CRUDComponentSession.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Errore nella creazione dell'EJB"));
    }

    private IDocumentoAmministrativoSpesaComponentSession getDocumentoAmministrativoSpesaComponentSession(String jndiName) throws BusinessProcessException {
        return Optional.ofNullable(createComponentSession(jndiName))
                .filter(IDocumentoAmministrativoSpesaComponentSession.class::isInstance)
                .map(IDocumentoAmministrativoSpesaComponentSession.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Errore nella creazione dell'EJB"));
    }


    private RemoteIterator findObbligazioni(UserContext context, IDocumentoAmministrativoSpesaBulk documentoAmministrativoSpesaBulk, Filtro_ricerca_obbligazioniVBulk filtro) throws BusinessProcessException, RemoteException, ComponentException {
        if (!Optional.ofNullable(documentoAmministrativoSpesaBulk).isPresent())
            return null;
        switch (documentoAmministrativoSpesaBulk.getCd_tipo_doc_amm()) {
            case Numerazione_doc_ammBulk.TIPO_MISSIONE: {
                return getDocumentoAmministrativoSpesaComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession")
                        .cercaObbligazioni(context, filtro);
            }
            case Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA: {
                return getDocumentoAmministrativoSpesaComponentSession("CNRDOCAMM00_EJB_FatturaPassivaComponentSession")
                        .cercaObbligazioni(context, filtro);
            }
            case Numerazione_doc_ammBulk.TIPO_COMPENSO: {
                return getDocumentoAmministrativoSpesaComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession")
                        .cercaObbligazioni(context, filtro);
            }
            case Numerazione_doc_ammBulk.TIPO_ANTICIPO: {
                return getDocumentoAmministrativoSpesaComponentSession("CNRMISSIONI00_EJB_AnticipoComponentSession")
                        .cercaObbligazioni(context, filtro);
            }
            case Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S: {
                return getDocumentoAmministrativoSpesaComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession")
                        .cercaObbligazioni(context, filtro);
            }
            default:
                return null;
        }
    }

    private Obbligazione_scadenzarioBulk cambiaObbligazione(UserContext context, Mandato_rigaBulk mandato_rigaBulk,
                                                            IDocumentoAmministrativoSpesaBulk documentoAmministrativoSpesaBulk,
                                                            Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk) throws BusinessProcessException, RemoteException, ComponentException {
        switch (documentoAmministrativoSpesaBulk.getCd_tipo_doc_amm()) {
            case Numerazione_doc_ammBulk.TIPO_MISSIONE: {
                final IDocumentoAmministrativoSpesaComponentSession missioneComponentSession =
                        getDocumentoAmministrativoSpesaComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession");
                MissioneBulk missioneBulk = Optional.ofNullable(
                        missioneComponentSession.inizializzaBulkPerModifica(context,
                                Optional.ofNullable(documentoAmministrativoSpesaBulk)
                                        .filter(MissioneBulk.class::isInstance)
                                        .map(MissioneBulk.class::cast)
                                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Missione!"))))
                        .filter(MissioneBulk.class::isInstance)
                        .map(MissioneBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Missione!"));

                final Obbligazione_scadenzarioBulk obbligazione_scadenzario = missioneBulk.getObbligazione_scadenzario();
                obbligazione_scadenzario.setIm_associato_doc_amm(BigDecimal.ZERO);
                obbligazione_scadenzario.setIm_associato_doc_contabile(BigDecimal.ZERO);
                obbligazione_scadenzario.setToBeUpdated();
                getCRUDComponentSession().modificaConBulk(context, obbligazione_scadenzario);

                missioneBulk.setObbligazione_scadenzario(obbligazione_scadenzarioBulk);
                missioneBulk.setToBeUpdated();
                return Optional.ofNullable(missioneComponentSession.modificaConBulk(context, missioneBulk))
                        .filter(MissioneBulk.class::isInstance)
                        .map(MissioneBulk.class::cast)
                        .map(MissioneBulk::getObbligazione_scadenzario)
                        .orElseThrow(() -> new BusinessProcessException("Impegno sulla missione non trovato!"));

            }
            case Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA: {
                final IDocumentoAmministrativoSpesaComponentSession fatturaPassivaComponentSession =
                        getDocumentoAmministrativoSpesaComponentSession("CNRDOCAMM00_EJB_FatturaPassivaComponentSession");
                Fattura_passivaBulk fatturaPassivaBulk = Optional.ofNullable(
                        fatturaPassivaComponentSession.inizializzaBulkPerModifica(context,
                                Optional.ofNullable(documentoAmministrativoSpesaBulk)
                                        .filter(Fattura_passivaBulk.class::isInstance)
                                        .map(Fattura_passivaBulk.class::cast)
                                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Fattura!"))))
                        .filter(Fattura_passivaBulk.class::isInstance)
                        .map(Fattura_passivaBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Fattura!"));
                final BulkList<Fattura_passiva_rigaBulk> fattura_passiva_dettColl = fatturaPassivaBulk.getFattura_passiva_dettColl();
                final AtomicReference<Fattura_passiva_rigaBulk> fatturaPassivaRigaBulkAtomicReference = new AtomicReference<>();
                for (Fattura_passiva_rigaBulk fattura_passiva_rigaBulk : fattura_passiva_dettColl) {
                    if (fattura_passiva_rigaBulk.getObbligazione_scadenziario().equalsByPrimaryKey(
                            new Obbligazione_scadenzarioBulk(
                                    mandato_rigaBulk.getCd_cds(),
                                    mandato_rigaBulk.getEsercizio_obbligazione(),
                                    mandato_rigaBulk.getEsercizio_ori_obbligazione(),
                                    mandato_rigaBulk.getPg_obbligazione(),
                                    mandato_rigaBulk.getPg_obbligazione_scadenzario()
                            )
                    )) {
                        fatturaPassivaBulk.removeFromFattura_passiva_obbligazioniHash(fattura_passiva_rigaBulk);
                        fatturaPassivaBulk.getDocumentiContabiliCancellati().add(fattura_passiva_rigaBulk.getObbligazione_scadenziario());

                        fatturaPassivaBulk.addToFattura_passiva_obbligazioniHash(obbligazione_scadenzarioBulk, fattura_passiva_rigaBulk);

                        fattura_passiva_rigaBulk.setObbligazione_scadenziario(obbligazione_scadenzarioBulk);
                        fattura_passiva_rigaBulk.setToBeUpdated();
                        fatturaPassivaRigaBulkAtomicReference.set(fattura_passiva_rigaBulk);

                    }
                }
                fatturaPassivaBulk.setToBeUpdated();
                final Fattura_passivaBulk fattura_passivaBulkNew = Optional.ofNullable(fatturaPassivaComponentSession.modificaConBulk(context, fatturaPassivaBulk))
                        .filter(Fattura_passivaBulk.class::isInstance)
                        .map(Fattura_passivaBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Errore nell'aggiornamento della Fattura Passiva"));
                return fattura_passivaBulkNew
                        .getFattura_passiva_dettColl()
                        .stream()
                        .filter(fattura_passiva_rigaBulk -> fattura_passiva_rigaBulk.equalsByPrimaryKey(fatturaPassivaRigaBulkAtomicReference.get()))
                        .findAny()
                        .map(Fattura_passiva_rigaBulk::getObbligazione_scadenziario)
                        .orElseThrow(() -> new BusinessProcessException("Impegno sulla riga non trovato!"));
            }
            case Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S: {
                final IDocumentoAmministrativoSpesaComponentSession documentoGenericoComponentSession =
                        getDocumentoAmministrativoSpesaComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession");
                Documento_genericoBulk documentoGenericoPassivoBulk = Optional.ofNullable(
                        documentoGenericoComponentSession.inizializzaBulkPerModifica(context,
                                Optional.ofNullable(documentoAmministrativoSpesaBulk)
                                        .filter(Documento_genericoBulk.class::isInstance)
                                        .map(Documento_genericoBulk.class::cast)
                                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Generico!"))))
                        .filter(Documento_genericoBulk.class::isInstance)
                        .map(Documento_genericoBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Generico!"));
                final BulkList<Documento_generico_rigaBulk> documento_generico_dettColl = documentoGenericoPassivoBulk.getDocumento_generico_dettColl();
                final AtomicReference<Documento_generico_rigaBulk> documentoGenericoRigaBulkAtomicReference = new AtomicReference<>();
                for (Documento_generico_rigaBulk documentoGenericoRigaBulk : documento_generico_dettColl) {
                    if (documentoGenericoRigaBulk.getObbligazione_scadenziario().equalsByPrimaryKey(
                            new Obbligazione_scadenzarioBulk(
                                    mandato_rigaBulk.getCd_cds(),
                                    mandato_rigaBulk.getEsercizio_obbligazione(),
                                    mandato_rigaBulk.getEsercizio_ori_obbligazione(),
                                    mandato_rigaBulk.getPg_obbligazione(),
                                    mandato_rigaBulk.getPg_obbligazione_scadenzario()
                            )
                    )) {
                        documentoGenericoPassivoBulk.removeFromDocumento_generico_obbligazioniHash(documentoGenericoRigaBulk);
                        documentoGenericoPassivoBulk.getDocumentiContabiliCancellati().add(documentoGenericoRigaBulk.getObbligazione_scadenziario());

                        documentoGenericoPassivoBulk.addToDocumento_generico_obbligazioniHash(obbligazione_scadenzarioBulk, documentoGenericoRigaBulk);

                        documentoGenericoRigaBulk.setObbligazione_scadenziario(obbligazione_scadenzarioBulk);
                        documentoGenericoRigaBulk.setToBeUpdated();
                        documentoGenericoRigaBulkAtomicReference.set(documentoGenericoRigaBulk);
                    }
                }
                documentoGenericoPassivoBulk.setToBeUpdated();
                final Documento_genericoBulk documento_genericoBulkNew = Optional.ofNullable(documentoGenericoComponentSession.modificaConBulk(context, documentoGenericoPassivoBulk))
                        .filter(Documento_genericoBulk.class::isInstance)
                        .map(Documento_genericoBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Generico!"));
                return documento_genericoBulkNew
                        .getDocumento_generico_dettColl()
                        .stream()
                        .filter(documentoGenericoRigaBulk -> documentoGenericoRigaBulk.equalsByPrimaryKey(documentoGenericoRigaBulkAtomicReference.get()))
                        .findAny()
                        .map(Documento_generico_rigaBulk::getObbligazione_scadenziario)
                        .orElseThrow(() -> new BusinessProcessException("Impegno sulla riga non trovato!"));
            }
            case Numerazione_doc_ammBulk.TIPO_COMPENSO: {
                final IDocumentoAmministrativoSpesaComponentSession compensoComponentSession =
                        getDocumentoAmministrativoSpesaComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession");
                CompensoBulk compensoBulk = Optional.ofNullable(
                        compensoComponentSession.inizializzaBulkPerModifica(context,
                                Optional.ofNullable(documentoAmministrativoSpesaBulk)
                                        .filter(CompensoBulk.class::isInstance)
                                        .map(CompensoBulk.class::cast)
                                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Compenso!"))))
                        .filter(CompensoBulk.class::isInstance)
                        .map(CompensoBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Compenso!"));

                final Obbligazione_scadenzarioBulk obbligazione_scadenzario = compensoBulk.getObbligazioneScadenzario();
                obbligazione_scadenzario.setIm_associato_doc_amm(BigDecimal.ZERO);
                obbligazione_scadenzario.setIm_associato_doc_contabile(BigDecimal.ZERO);
                obbligazione_scadenzario.setToBeUpdated();
                getCRUDComponentSession().modificaConBulk(context, obbligazione_scadenzario);

                compensoBulk.setObbligazioneScadenzario(obbligazione_scadenzarioBulk);
                compensoBulk.setToBeUpdated();
                return Optional.ofNullable(compensoComponentSession.modificaConBulk(context, compensoBulk))
                        .filter(CompensoBulk.class::isInstance)
                        .map(CompensoBulk.class::cast)
                        .map(CompensoBulk::getObbligazioneScadenzario)
                        .orElseThrow(() -> new BusinessProcessException("Impegno sulla missione non trovato!"));
            }
            case Numerazione_doc_ammBulk.TIPO_ANTICIPO: {
                final IDocumentoAmministrativoSpesaComponentSession anticipoComponentSession =
                        getDocumentoAmministrativoSpesaComponentSession("CNRMISSIONI00_EJB_AnticipoComponentSession");
                AnticipoBulk anticipoBulk = Optional.ofNullable(
                        anticipoComponentSession.inizializzaBulkPerModifica(context,
                                Optional.ofNullable(documentoAmministrativoSpesaBulk)
                                        .filter(AnticipoBulk.class::isInstance)
                                        .map(AnticipoBulk.class::cast)
                                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Anticipo!"))))
                        .filter(AnticipoBulk.class::isInstance)
                        .map(AnticipoBulk.class::cast)
                        .orElseThrow(() -> new BusinessProcessException("Documento amministrativo non di tipo Anticipo!"));

                final Obbligazione_scadenzarioBulk obbligazione_scadenzario = anticipoBulk.getScadenza_obbligazione();
                obbligazione_scadenzario.setIm_associato_doc_amm(BigDecimal.ZERO);
                obbligazione_scadenzario.setIm_associato_doc_contabile(BigDecimal.ZERO);
                obbligazione_scadenzario.setToBeUpdated();
                getCRUDComponentSession().modificaConBulk(context, obbligazione_scadenzario);

                anticipoBulk.setScadenza_obbligazione(obbligazione_scadenzarioBulk);
                anticipoBulk.setToBeUpdated();
                return Optional.ofNullable(anticipoComponentSession.modificaConBulk(context, anticipoBulk))
                        .filter(AnticipoBulk.class::isInstance)
                        .map(AnticipoBulk.class::cast)
                        .map(AnticipoBulk::getScadenza_obbligazione)
                        .orElseThrow(() -> new BusinessProcessException("Impegno sull' Anticipo non trovato!"));
            }
            default: {
                return null;
            }
        }
    }

    private Mandato_rigaBulk getCurrentMandato_rigaBulk() throws BusinessProcessException {
        return Optional.ofNullable(getDocumentiPassiviSelezionati().getModel())
                .filter(Mandato_rigaBulk.class::isInstance)
                .map(Mandato_rigaBulk.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Riga non selezionata!"));
    }

    /**
     * É stato richiesto un cambio Impegno sulla riga di Mandato
     *
     * @param context
     * @param filtro
     * @return
     * @throws BusinessProcessException
     */
    @Override
    public RemoteIterator findObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro) throws BusinessProcessException {
        try {
            return findObbligazioni(context, getMandatoComponentSession().getDocumentoAmministrativoSpesaBulk(context, getCurrentMandato_rigaBulk()), filtro);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    /**
     * @param actionContext
     * @param clauses
     * @param bulk
     * @param context
     * @param property
     * @return
     * @throws BusinessProcessException
     */
    @Override
    public RemoteIterator findObbligazioniAttributes(ActionContext actionContext, CompoundFindClause clauses, OggettoBulk bulk, OggettoBulk context, String property) throws BusinessProcessException {
        try {
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
                    actionContext,
                    getMandatoComponentSession().cerca(
                            actionContext.getUserContext(),
                            clauses,
                            bulk,
                            context,
                            property));
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    public void cambiaObbligazioneScadenzario(ActionContext context, Mandato_rigaBulk mandato_rigaBulk, Obbligazione_scadenzarioBulk scadenza) throws BusinessProcessException {
        try {
            /**
             * Cambio l'impegno sul documento amministrativo collegato
             */
            final Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk =
                    Optional.ofNullable(
                            cambiaObbligazione(context.getUserContext(), mandato_rigaBulk, getDocumentoAmministrativoCorrente(), scadenza)
                    ).orElse(scadenza);

            final Mandato_rigaBulk mandatoRigaClone = (Mandato_rigaBulk) mandato_rigaBulk.clone();
            mandatoRigaClone.setMandato_siopeColl(new BulkList());
            mandatoRigaClone.setMandatoCupColl(new BulkList());

            Optional.ofNullable(
                    getDocumentiPassiviSelezionati().removeDetail(getDocumentiPassiviSelezionati().getModelIndex())
            )
                    .filter(Mandato_rigaBulk.class::isInstance)
                    .map(Mandato_rigaBulk.class::cast)
                    .map(Mandato_rigaBulk::getMandato_siopeColl)
                    .map(BulkList::stream)
                    .orElse(Stream.empty())
                    .forEach(mandato_siopeBulk -> {
                        mandato_siopeBulk.setToBeDeleted();
                        for (MandatoSiopeCupBulk mandatoSiopeCupBulk : mandato_siopeBulk.getMandatoSiopeCupColl()) {
                            mandatoSiopeCupBulk.setToBeDeleted();
                        }
                    });

            getDocumentiPassiviSelezionati().
                    setModelIndex(context, getDocumentiPassiviSelezionati().addDetail(mandatoRigaClone));

            mandatoRigaClone.setEsercizio_obbligazione(obbligazione_scadenzarioBulk.getEsercizio());
            mandatoRigaClone.setEsercizio_ori_obbligazione(obbligazione_scadenzarioBulk.getEsercizio_originale());
            mandatoRigaClone.setPg_obbligazione(obbligazione_scadenzarioBulk.getPg_obbligazione());
            mandatoRigaClone.setPg_obbligazione_scadenzario(obbligazione_scadenzarioBulk.getPg_obbligazione_scadenzario());
            mandatoRigaClone.setElemento_voce(obbligazione_scadenzarioBulk.getObbligazione().getElemento_voce());
            mandatoRigaClone.getMandato_siopeColl().clear();
            mandatoRigaClone.setCodici_siopeColl(
                    getMandatoComponentSession()
                            .setCodiciSIOPECollegabili(context.getUserContext(), mandatoRigaClone)
                            .getCodici_siopeColl()
            );
            codiciSiopeCollegabili.resync(context);
            getModel().setToBeUpdated();
            mandatoRigaClone.setCrudStatus(OggettoBulk.TO_BE_CREATED);
            setDirty(true);
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }

    @Override
    public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {
        return null;
    }

    @Override
    public IDocumentoAmministrativoBulk getBringBackDocAmm() {
        return getDocumentoAmministrativoCorrente();
    }

    @Override
    public Risultato_eliminazioneVBulk getDeleteManager() {
        return Optional.ofNullable(deleteManager)
                .map(risultato_eliminazioneVBulk -> {
                    risultato_eliminazioneVBulk.reset();
                    return risultato_eliminazioneVBulk;
                })
                .orElseGet(() -> new it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk());
    }

    @Override
    public IDocumentoAmministrativoSpesaBulk getDocumentoAmministrativoCorrente() {
        try {
            return getMandatoComponentSession().getDocumentoAmministrativoSpesaBulk(
                    null,
                    getCurrentMandato_rigaBulk()
            );
        } catch (ComponentException | RemoteException | BusinessProcessException e) {
            throw new DetailedRuntimeException(e);
        }
    }

    @Override
    public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
        return null;
    }

    @Override
    public boolean isAutoGenerated() {
        return false;
    }

    @Override
    public boolean isDeleting() {
        return false;
    }

    @Override
    public boolean isManualModify() {
        return false;
    }

    @Override
    public void setIsDeleting(boolean newIsDeleting) {

    }

    @Override
    public void validaObbligazionePerDocAmm(ActionContext actionContext, OggettoBulk bulk) throws BusinessProcessException {

    }

    @Override
    public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
        return Optional.ofNullable(getModel())
                .filter(IDefferUpdateSaldi.class::isInstance)
                .map(IDefferUpdateSaldi.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Modello non presente o non implementa IDefferUpdateSaldi"));
    }

    @Override
    public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {
        return this;
    }
}