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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.coepcoan00.bp.EconomicaAvereDetailCRUDController;
import it.cnr.contab.coepcoan00.bp.EconomicaDareDetailCRUDController;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraBulk;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.DetailedException;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * <!-- @TODO: da completare -->
 */

public abstract class CRUDFatturaAttivaBP
        extends AllegatiCRUDBP<AllegatoFatturaAttivaBulk, Fattura_attivaBulk>
        implements IDocumentoAmministrativoBP,
        IGenericSearchDocAmmBP,
        IDefferedUpdateSaldiBP,
        VoidableBP, IDocAmmEconomicaBP {

    private final SimpleDetailCRUDController crudRiferimentiBanca = new SimpleDetailCRUDController("RifBanca", Fattura_attiva_rigaBulk.class, "riferimenti_bancari", this);
    private final SimpleDetailCRUDController consuntivoController = new SimpleDetailCRUDController("Consuntivo", Consuntivo_rigaVBulk.class, "fattura_attiva_consuntivoColl", this);

    private final AccertamentiCRUDController accertamentiController = new AccertamentiCRUDController("Accertamenti", it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk.class, "fattura_attiva_accertamentiHash", this);
    private final SimpleDetailCRUDController dettaglioAccertamentoController;
    private final FatturaAttivaRigaIntrastatCRUDController dettaglioIntrastatController = new FatturaAttivaRigaIntrastatCRUDController("Intrastat", Fattura_attiva_intraBulk.class, "fattura_attiva_intrastatColl", this);
    private final CollapsableDetailCRUDController movimentiDare = new EconomicaDareDetailCRUDController(this);
    private final CollapsableDetailCRUDController movimentiAvere = new EconomicaAvereDetailCRUDController(this);

    protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
    private boolean isDeleting = false;
    private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
    private boolean annoDiCompetenza = true;
    private boolean annoSolareInScrivania = true;
    private boolean riportaAvantiIndietro = false;
    private boolean carryingThrough = false;
    private boolean ribaltato;
    private boolean isDetailDoubling = false;
    private boolean isGestoreBancaFatturaAttiva;
    private boolean contoEnte;
    private DocumentiCollegatiDocAmmService docCollService;

    public CRUDFatturaAttivaBP() {
        this(Fattura_attiva_rigaBulk.class);
    }

    /**
     * @param dettAccertamentiControllerClass
     */
    public CRUDFatturaAttivaBP(Class dettAccertamentiControllerClass) {
        super("Tr");

        dettaglioAccertamentoController = new SimpleDetailCRUDController("DettaglioAccertamenti", dettAccertamentiControllerClass, "fattura_attiva_accertamentiHash", accertamentiController) {

            public java.util.List getDetails() {

                Fattura_attivaBulk fattura = (Fattura_attivaBulk) CRUDFatturaAttivaBP.this.getModel();
                java.util.Vector lista = new java.util.Vector();
                if (fattura != null) {
                    java.util.Hashtable h = fattura.getFattura_attiva_accertamentiHash();
                    if (h != null && getParentModel() != null)
                        lista = (java.util.Vector) h.get(getParentModel());
                }
                return lista;
            }
        };
    }


    public CRUDFatturaAttivaBP(String function) throws BusinessProcessException {
        this(function, Fattura_attiva_rigaBulk.class);
    }

    /**
     * @param function                        La funzione con cui è stato creato il BusinessProcess
     * @param dettAccertamentiControllerClass
     * @throws BusinessProcessException
     */
    public CRUDFatturaAttivaBP(String function, Class dettAccertamentiControllerClass) throws BusinessProcessException {
        super(function + "Tr");

        dettaglioAccertamentoController = new SimpleDetailCRUDController("DettaglioAccertamenti", dettAccertamentiControllerClass, "fattura_attiva_accertamentiHash", accertamentiController) {

            public java.util.List getDetails() {

                Fattura_attivaBulk fattura = (Fattura_attivaBulk) CRUDFatturaAttivaBP.this.getModel();
                java.util.Vector lista = new java.util.Vector();
                if (fattura != null) {
                    java.util.Hashtable h = fattura.getFattura_attiva_accertamentiHash();
                    if (h != null && getParentModel() != null)
                        lista = (java.util.Vector) h.get(getParentModel());
                }
                return lista;
            }
        };
    }

    protected void basicEdit(it.cnr.jada.action.ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        try {
            Fattura_attivaBulk fa = (Fattura_attivaBulk) bulk;
            setAnnoDiCompetenza(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() == fa.getEsercizio().intValue());
            super.basicEdit(context, bulk, doInitializeForEdit);
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    protected boolean basicRiportaButtonHidden() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return isAnnoSolareInScrivania() ||
                !isRiportaAvantiIndietro() ||
                isDeleting() ||
                isModelVoided() ||
                (fa != null && (fa.isPagata() || fa.isCongelata())) ||
                !isEditing();
    }

    public void create(it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            archiviaAllegati(context);
            getModel().setToBeCreated();
            setModel(
                    context,
                    ((FatturaAttivaSingolaComponentSession) createComponentSession()).creaConBulk(
                            context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    public void gestioneAllegatiFatturazioneElettronica(it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {
        try {
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) getModel();
            int crudStatus = fattura.getCrudStatus();
            if (fattura != null && fattura.isDocumentoFatturazioneElettronica() && isRistampaFatturaElettronicaButtonHidden()) {
                SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(
                        context.getUserContext(),
                        fattura
                );
            }
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    public OggettoBulk createEmptyModel(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

        setAnnoDiCompetenza(true);
        return super.createEmptyModel(context);
    }

    public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        int crudStatus = getModel().getCrudStatus();
        try {
            getModel().setToBeDeleted();
            createComponentSession().eliminaConBulk(context.getUserContext(), getModel());
        } catch (Exception e) {
            getModel().setCrudStatus(crudStatus);
            throw handleException(e);
        }
    }

    public void edit(
            it.cnr.jada.action.ActionContext context,
            OggettoBulk bulk,
            boolean doInitializeForEdit)
            throws it.cnr.jada.action.BusinessProcessException {

        setCarryingThrough(false);
        super.edit(context, bulk, doInitializeForEdit);
        if (getModel() instanceof Fattura_attiva_IBulk)
            ((Fattura_attiva_IBulk) getModel()).setDocumentoModificabile(!isInputReadonly());
    }

    /**
     * Effettua una operazione di ricerca per un attributo di un modello.
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param filtro      modello che fa da contesto alla ricerca (il modello del FormController padre del
     *                    controller che ha scatenato la ricerca)
     * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
     **/
    public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.action.BusinessProcessException {
        try {

            it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession fpcs = (it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession) createComponentSession();
            return fpcs.cercaObbligazioni(userContext, filtro);

        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    /**
     * Effettua una operazione di ricerca per un attributo di un modello.
     *
     * @param actionContext contesto dell'azione in corso
     * @param clauses       Albero di clausole da utilizzare per la ricerca
     * @param bulk          prototipo del modello di cui si effettua la ricerca
     * @param context       modello che fa da contesto alla ricerca (il modello del FormController padre del
     *                      controller che ha scatenato la ricerca)
     * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
     **/
    public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
        return null;
    }

    /**
     * Restituisce il valore della proprietà 'accertamentiController'
     *
     * @return Il valore della proprietà 'accertamentiController'
     */
    public final AccertamentiCRUDController getAccertamentiController() {
        return accertamentiController;
    }

    public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {

        if (getAccertamentiController() == null)
            return null;
        return (Accertamento_scadenzarioBulk) getAccertamentiController().getModel();
    }

    public IDocumentoAmministrativoBulk getBringBackDocAmm() {

        return getDocumentoAmministrativoCorrente();
    }

    public String getColumnsetForGenericSearch() {
        if (getParent() != null &&
                (getParent() instanceof DocumentiAmministrativiProtocollabiliBP ||
                        getParent() instanceof DocumentiAmministrativiRistampabiliBP))
            return "protocollazioneIvaSet";
        if (getParent() != null &&
                (getParent() instanceof DocumentiAmministrativiFatturazioneElettronicaBP))
            return "fatturazioneElettronicaSet";
        return "default";
    }

    /**
     * Restituisce il valore della proprietà 'consuntivoController'
     *
     * @return Il valore della proprietà 'consuntivoController'
     */
    public final SimpleDetailCRUDController getConsuntivoController() {
        return consuntivoController;

    }

    /**
     * Restituisce il valore della proprietà 'crudRiferimentiBanca'
     *
     * @return Il valore della proprietà 'crudRiferimentiBanca'
     */
    public final SimpleDetailCRUDController getCrudRiferimentiBanca() {
        return crudRiferimentiBanca;
    }

    public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {

        if (isDeleting() && getParent() != null)
            return getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
        return (IDefferUpdateSaldi) getDocumentoAmministrativoCorrente();
    }

    public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

        if (isDeleting() && getParent() != null)
            return ((IDefferedUpdateSaldiBP) getParent()).getDefferedUpdateSaldiParentBP();
        return this;
    }

    public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {

        if (deleteManager == null)
            deleteManager = new it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk();
        else
            deleteManager.reset();
        return deleteManager;
    }

    /**
     * Restituisce il valore della proprietà 'dettaglio'
     *
     * @return Il valore della proprietà 'dettaglio'
     */
    public abstract FatturaAttivaRigaCRUDController getDettaglio();

    /**
     * Restituisce il valore della proprietà 'dettaglioAccertamentoController'
     *
     * @return Il valore della proprietà 'dettaglioAccertamentoController'
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglioAccertamentoController() {
        return dettaglioAccertamentoController;
    }

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {

        return (IDocumentoAmministrativoBulk) getModel();
    }

    public abstract it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente();

    public String getPropertyForGenericSearch() {

        return "cliente";
    }

    /**
     * Restituisce il valore della proprietà 'userConfirm'
     *
     * @return Il valore della proprietà 'userConfirm'
     */
    public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
        return userConfirm;
    }

    /**
     * Imposta il valore della proprietà 'userConfirm'
     *
     * @param newUserConfirm Il valore da assegnare a 'userConfirm'
     */
    public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter newUserConfirm) {
        userConfirm = newUserConfirm;
    }

    /**
     * Imposta come attivi i tab di default.
     *
     * @param context <code>ActionContext</code>
     */

    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

        super.init(config, context);

        try {
            int solaris = Fattura_attivaBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR);
            int esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
            setAnnoSolareInScrivania(solaris == esercizioScrivania);
            setRibaltato(initRibaltato(context));
            if (!isAnnoSolareInScrivania()) {
                String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
                try {
                    FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession) createComponentSession();
                    boolean esercizioScrivaniaAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania)));
                    boolean esercizioSuccessivoAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania + 1)));
                    setRiportaAvantiIndietro(esercizioScrivaniaAperto && esercizioSuccessivoAperto && isRibaltato());
                } catch (Throwable t) {
                    handleException(t);
                }
            } else
                setRiportaAvantiIndietro(false);
        } catch (javax.ejb.EJBException e) {
            setAnnoSolareInScrivania(false);
        }

        resetTabs();
    }

    public OggettoBulk initializeModelForEdit(ActionContext context, OggettoBulk bulk) throws BusinessProcessException {

        try {
            if (bulk != null) {
                Fattura_attivaBulk fa = (Fattura_attivaBulk) bulk;
                fa.setDettagliCancellati(new java.util.Vector());
                fa.setDocumentiContabiliCancellati(new java.util.Vector());
            }
            return super.initializeModelForEdit(context, bulk);
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    public it.cnr.jada.ejb.CRUDComponentSession initializeModelForGenericSearch(
            it.cnr.jada.util.action.BulkBP bp,
            it.cnr.jada.action.ActionContext context)
            throws BusinessProcessException {

        return createComponentSession();
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2003 16.43.02)
     *
     * @return boolean
     */
    public boolean isAnnoDiCompetenza() {
        return annoDiCompetenza;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2003 16.43.02)
     *
     * @param newAnnoDiCompetenza boolean
     */
    public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
        annoDiCompetenza = newAnnoDiCompetenza;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 17.59.57)
     *
     * @return boolean
     */
    public boolean isAnnoSolareInScrivania() {
        return annoSolareInScrivania;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 17.59.57)
     *
     * @param newAnnoSolareInScrivania boolean
     */
    public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
        annoSolareInScrivania = newAnnoSolareInScrivania;
    }

    public abstract boolean isAutoGenerated();

    public boolean isBringbackButtonEnabled() {
        return super.isBringbackButtonEnabled() || isDeleting();
    }

    public boolean isBringbackButtonHidden() {
        return super.isBringbackButtonHidden() || !isDeleting();
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 17.59.57)
     *
     * @return boolean
     */
    public boolean isCarryingThrough() {
        return carryingThrough;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 17.59.57)
     *
     * @param newCarryingThrough boolean
     */
    public void setCarryingThrough(boolean newCarryingThrough) {
        carryingThrough = newCarryingThrough;
    }

    public boolean isDeleteButtonEnabled() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return super.isDeleteButtonEnabled() &&
                !isDeleting() &&
                !isModelVoided() &&
                (fa != null && !fa.isPagata() && !fa.isCongelata() && !isSearching() &&
                        ((isAnnoDiCompetenza() && !fa.isRiportata()) ||
                                // Gennaro Borriello - (02/11/2004 16.48.21)
                                // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
                                (!isAnnoDiCompetenza() && fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportataInScrivania()))));

    }

    public boolean isDeleting() {
        return isDeleting;
    }

    //Gennaro Borriello - (03/11/2004 19.04.48)
//Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
//DA UN ES. PRECEDENTE a quello di scrivania.
    public boolean isInputReadonly() {
        Fattura_attivaBulk fatt = (Fattura_attivaBulk) getModel();
        return super.isInputReadonly() ||
                isDeleting() ||
                isModelVoided() ||
                (!isAnnoDiCompetenza() && isEditing()) ||
                (fatt != null && (fatt.isPagata() ||
                        (
                                (isAnnoDiCompetenza() && fatt.isRiportata()))
                        || fatt.isCongelata()) &&
                        !isDirty() && !isSearching());
//	|| (fatt.getProtocollo_iva_generale()!=null) && !this.isSearching();
    }

    public boolean isManualModify() {

        return !((Fattura_attivaBulk) getModel()).isCongelata();
    }

    public boolean isModelVoided() {

        return !isSearching() && getModel() != null && ((Voidable) getModel()).isAnnullato();
    }

    public boolean isNewButtonEnabled() {
        return super.isNewButtonEnabled() &&
                getModel() != null &&
                ((Fattura_attivaBulk) getModel()).getEsercizio() != null &&
                (Fattura_attivaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR) <=
						((Fattura_attivaBulk) getModel()).getEsercizio().intValue() || !isAnnoDiCompetenza());
    }

    public boolean isRiportaAvantiButtonEnabled() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return isCarryingThrough() || !fa.isRiportata();
    }

    public boolean isRiportaAvantiButtonHidden() {

        return basicRiportaButtonHidden();
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 17.59.57)
     *
     * @return boolean
     */
    public boolean isRiportaAvantiIndietro() {
        return riportaAvantiIndietro;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/07/2003 17.59.57)
     *
     * @param newRiportaAvantiIndietro boolean
     */
    public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
        riportaAvantiIndietro = newRiportaAvantiIndietro;
    }

    public boolean isRiportaIndietroButtonEnabled() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return isEditing() &&
                !isDeleting() &&
                !isModelVoided() &&
                !isDirty() &&
                !fa.isPagata() &&
                !isCarryingThrough();
    }

    public boolean isVisualizzaDocumentoFatturaElettronicaButtonHidden() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return (fa == null || fa.getPg_fattura_attiva() == null || !fa.isDocumentoFatturazioneElettronica());
    }

    public boolean isRistampaFatturaElettronicaButtonHidden() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return (fa == null || fa.getPg_fattura_attiva() == null || !fa.isDocumentoFatturazioneElettronica() || fa.isFatturaElettronicaAllaFirma() || fa.isFatturaElettronicaPredispostaAllaFirma() || fa.isFatturaElettronicaScartata());
    }

    public boolean isVisualizzaXmlFatturaElettronicaButtonHidden() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return (fa == null || fa.getPg_fattura_attiva() == null || !fa.isDocumentoFatturazioneElettronica() || fa.isFatturaElettronicaAllaFirma() || fa.isNotaCreditoDaNonInviareASdi());
    }

    public boolean isRiportaIndietroButtonHidden() {

        Fattura_attivaBulk fa = (Fattura_attivaBulk) getModel();
        return basicRiportaButtonHidden() ||
                (fa != null && !(fa.isRiportata() || isCarryingThrough()));
    }

    public boolean isSaveButtonEnabled() {


        boolean isReadonly = (isDeleting() ||
                isModelVoided() ||
                (getModel() != null && (((Fattura_attivaBulk) getModel()).isPagata()) &&
                        !isDirty() && !isSearching()));

        return !isReadonly &&
                super.isSaveButtonEnabled() &&
                !((Fattura_attivaBulk) getModel()).isRiportata() &&
                !((Fattura_attivaBulk) getModel()).isCongelata() &&
                (isAnnoDiCompetenza() || carryingThrough || isDetailDoubleable());
    }

    public boolean isUndoBringBackButtonEnabled() {

        return super.isUndoBringBackButtonEnabled() || isDeleting() || isViewing();
    }

    public boolean isUndoBringBackButtonHidden() {

        return super.isUndoBringBackButtonHidden() || !isDeleting();
    }

    /**
     * Attiva oltre al normale reset il metodo di set dei tab di default.
     *
     * @param context <code>ActionContext</code>
     * @see resetTabs
     */

    public void reset(ActionContext context) throws BusinessProcessException {

        if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() !=
                Fattura_attivaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR))
            resetForSearch(context);
        else {
            setCarryingThrough(false);
            super.reset(context);
            resetTabs();
        }
    }

    /**
     * Attiva oltre al normale reset il metodo di set dei tab di default.
     *
     * @param context <code>ActionContext</code>
     * @see resetTabs
     */

    public void resetForSearch(ActionContext context) throws BusinessProcessException {

        setCarryingThrough(false);
        super.resetForSearch(context);
        resetTabs();
    }

    /**
     * Imposta come attivi i tab di default.
     *
     * @param context <code>ActionContext</code>
     */

    public void resetTabs() {
        setTab("tab", "tabFatturaAttiva");
        setTab("tabEconomica", "tabDare");
    }

    public void riportaAvanti(ActionContext context)
            throws ValidationException, BusinessProcessException {

        try {
            FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession) createComponentSession();
            Fattura_attivaBulk faCarried =
                    (Fattura_attivaBulk) session.riportaAvanti(
                            context.getUserContext(),
                            (IDocumentoAmministrativoBulk) getModel(),
                            getUserConfirm());
            setModel(context, faCarried);
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    public void riportaIndietro(ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        if (isDirty()) {
            setMessage("Il documento è stato modificato! Operazione annullata.");
            return;
        }

        rollbackUserTransaction();
        setCarryingThrough(true);

        try {
            FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession) createComponentSession();
            Fattura_attivaBulk faCarried = (Fattura_attivaBulk) session.riportaIndietro(
                    context.getUserContext(),
                    (IDocumentoAmministrativoBulk) getModel());
            basicEdit(context, faCarried, true);
            setDirty(true);

        } catch (Throwable t) {
            setCarryingThrough(false);
            rollbackUserTransaction();
            throw handleException(t);
        }
    }

    /**
     * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Rollback to savePoint
     * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata
     * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
     * modifiche apportate al doc. amministrativo che ha aperto il compenso
     *
     * @param    context            il Context che ha generato la richiesta
     * @param    savePointName    il nome del savePoint
     */
    public void rollbackToSavePoint(ActionContext context, String savePointName) throws BusinessProcessException {

        try {

            FatturaAttivaSingolaComponentSession sess = (FatturaAttivaSingolaComponentSession) createComponentSession();
            sess.rollbackToSavePoint(context.getUserContext(), savePointName);

        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    public void salvaRiportandoAvanti(ActionContext context)
            throws ValidationException, BusinessProcessException {

        Fattura_attivaBulk faClone = (Fattura_attivaBulk) getModel();
        try {
            setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
            completeSearchTools(context, this);
            validate(context);
            saveChildren(context);

            update(context);
            riportaAvanti(context);
        } catch (BusinessProcessException e) {
            rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
            //Il setModel è necessario perchè update setta il modello. se riportaAvanti fallisce il pg_ver_rec
            //rimane disallineato.
            setModel(context, faClone);
            throw handleException(e);
        }

        if (getMessage() == null)
            setMessage("Salvataggio e riporto all'esercizio successivo eseguito in modo corretto.");

        commitUserTransaction();
        setCarryingThrough(false);

        try {
            basicEdit(context, getModel(), true);
        } catch (BusinessProcessException e) {
            setModel(context, null);
            setDirty(false);
            throw e;
        }
    }

    public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {

        super.save(context);

        Fattura_attivaBulk documento = (Fattura_attivaBulk) getModel();
        if (documento.isDocumentoFatturazioneElettronica()) {
            for (Iterator i = documento.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) i.next();
                if (riga.getAccertamento_scadenzario() != null &&
                        riga.getAccertamento_scadenzario().getAccertamento() != null) {
                    AccertamentoBulk accertamento = riga.getAccertamento_scadenzario().getAccertamento();
                    if (accertamento.getContratto() == null) {
                        setMessage("Attenzione! Per alcune righe del documento sono collegati accertamenti che non hanno il riferimento al contratto e quindi al Codice CUP o al Codice CIG comunicato dal Committente. " +
                                "Il committente potrebbe rifiutare il documento in mancanza di tali dati. Salvataggio Effettuato.");
                        break;
                    } else {
                        if (accertamento.getContratto().getCdCigFatturaAttiva() == null || (accertamento.getContratto().getCup() == null || accertamento.getContratto().getCup().getCdCup() == null)) {
                            setMessage("Attenzione! Per alcune righe del documento sono collegati accertamenti su contratti che non hanno indicato il Codice CUP o il Codice CIG comunicato dal Committente. " +
                                    "Il committente potrebbe rifiutare il documento in mancanza di tali dati. Salvataggio Effettuato.");
                            break;
                        }
                    }
                }
            }
        }
        setCarryingThrough(false);
        resetTabs();
    }

    public void setIsDeleting(boolean newIsDeleting) {
        isDeleting = newIsDeleting;
    }

    /**
     * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
     * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
     * anche quelli del documento amministrativo.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Imposta savePoint
     * Pre:  Una richiesta di impostare un savepoint e' stata generata
     * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
     *
     * @param    context            il Context che ha generato la richiesta
     * @param    savePointName    il nome del savePoint
     */
    public void setSavePoint(ActionContext context, String savePointName) throws BusinessProcessException {

        try {

            FatturaAttivaSingolaComponentSession sess = (FatturaAttivaSingolaComponentSession) createComponentSession();
            sess.setSavePoint(context.getUserContext(), savePointName);

        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    public void update(ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            archiviaAllegati(context);
            getModel().setToBeUpdated();
            setModel(
                    context,
                    ((FatturaAttivaSingolaComponentSession) createComponentSession()).modificaConBulk(
                            context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    /**
     * Validazione dell'obbligazione in fase di creazione di una nuova obbligazione
     * o modifica di una già creata.
     * Il metodo viene chiamato sul riporta dell'Obbligazione in modo da validare
     * istantaneamente l'oggetto creato.
     * Chi non ne ha bisogno lo lasci vuoto.
     **/
    public void validaObbligazionePerDocAmm(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        return;
    }

    protected void writeToolbar(javax.servlet.jsp.JspWriter writer, it.cnr.jada.util.jsp.Button[] buttons) throws java.io.IOException, javax.servlet.ServletException {

        it.cnr.jada.util.jsp.Button riportaAvantiButton = buttons[buttons.length - 1];
        riportaAvantiButton.setSeparator(isRiportaIndietroButtonHidden() && !isRiportaAvantiButtonHidden());
        super.writeToolbar(writer, buttons);
    }

    public boolean initRibaltato(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            return (((RicercaDocContComponentSession) createComponentSession("CNRCHIUSURA00_EJB_RicercaDocContComponentSession", RicercaDocContComponentSession.class)).isRibaltato(context.getUserContext()));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public boolean isRibaltato() {
        return ribaltato;
    }

    public void setRibaltato(boolean b) {
        ribaltato = b;
    }

    private Fattura_attiva_rigaIBulk copyByRigaDocumento(ActionContext context, Fattura_attiva_rigaIBulk nuovoDettaglio, Fattura_attiva_rigaIBulk origine) {
        //associo la riga creata al documento
        origine.setUser(context.getUserContext().getUser());
        if (origine.getVoce_iva() != null)
            nuovoDettaglio.setVoce_iva((Voce_ivaBulk) origine.getVoce_iva().clone());
//	nuovoDettaglio.setEsercizio( origine.getEsercizio());
//	nuovoDettaglio.setCd_cds( origine.getCd_cds());
//	nuovoDettaglio.setCd_unita_organizzativa( origine.getCd_unita_organizzativa());
        nuovoDettaglio.setStato_cofi(origine.getStato_cofi());
        nuovoDettaglio.setDt_da_competenza_coge(origine.getDt_da_competenza_coge());
        nuovoDettaglio.setDt_a_competenza_coge(origine.getDt_a_competenza_coge());
        nuovoDettaglio.setDs_riga_fattura(origine.getDs_riga_fattura());
        nuovoDettaglio.setTi_associato_manrev(origine.getTi_associato_manrev());
        nuovoDettaglio.setTariffario(origine.getTariffario());
        nuovoDettaglio.setBene_servizio(origine.getBene_servizio());
        nuovoDettaglio.setTrovato(origine.getTrovato());
        nuovoDettaglio.setToBeCreated();
        return nuovoDettaglio;
    }

    public void sdoppiaDettaglioInAutomatico(ActionContext context) throws ValidationException, BusinessProcessException {
        try {
            it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP.getVirtualComponentSession(context, true);
            FatturaAttivaSingolaComponentSession session = (FatturaAttivaSingolaComponentSession) createComponentSession();
            Fattura_attivaBulk documento = (Fattura_attivaBulk) getModel();
            Fattura_attiva_rigaIBulk dettaglioSelezionato = (Fattura_attiva_rigaIBulk) getDettaglio().getModel();
            Accertamento_scadenzarioBulk scadenzaNuova = null;

            if (dettaglioSelezionato == null) return;
            if (documento.getStato_cofi() != null && documento.getStato_cofi().equals(Fattura_attivaBulk.STATO_PAGATO))
                setMessage("Non è possibile sdoppiare righe in un documento pagato.");
            if (dettaglioSelezionato.getIm_riga_sdoppia() == null ||
                    //dettaglioSelezionato.getIm_riga_sdoppia().equals(Utility.ZERO) ||
                    dettaglioSelezionato.getIm_riga_sdoppia().compareTo(dettaglioSelezionato.getSaldo()) > 1) {
                setMessage("Il nuovo importo della riga da sdoppiare deve essere positivo ed inferiore " +
                        "al saldo originario.");
                return;
            }
            BigDecimal importoIva = BigDecimal.ZERO;
            if (dettaglioSelezionato != null) {
                // Calcolo iva rapportato al nuovo totale per cui si esegue lo sdoppiato
                if (documento.quadraturaInDeroga())
                    importoIva = importoIva.add(dettaglioSelezionato.getIm_riga_sdoppia().subtract(dettaglioSelezionato.getIm_riga_sdoppia().multiply(new BigDecimal(100)).divide(new BigDecimal(100).add(dettaglioSelezionato.getVoce_iva().getPercentuale()), 2, BigDecimal.ROUND_HALF_UP)));
            }

            Accertamento_scadenzarioBulk scadenzaVecchia = dettaglioSelezionato.getAccertamento_scadenzario();

            BigDecimal newImportoRigaVecchia = dettaglioSelezionato.getIm_riga_sdoppia().add(dettaglioSelezionato.getIm_totale_divisa().subtract(dettaglioSelezionato.getSaldo()));
            BigDecimal newImportoRigaNuova = dettaglioSelezionato.getSaldo().subtract(dettaglioSelezionato.getIm_riga_sdoppia());

            BigDecimal newPrezzoRigaVecchia = newImportoRigaVecchia.divide(dettaglioSelezionato.getQuantita().multiply(dettaglioSelezionato.getVoce_iva().getPercentuale().divide(new BigDecimal(100)).add(new java.math.BigDecimal(1))), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal newPrezzoRigaNuova = dettaglioSelezionato.getPrezzo_unitario().subtract(newPrezzoRigaVecchia);

            if (dettaglioSelezionato.getAccertamento_scadenzario() != null) {
                // se importoIva è diverso da zero vuole dire che è in split_payment ed ho calcolato la quota parte dell'iva  da considerare per sdoppiare le scadenze dell'accertamento
                if (importoIva.compareTo(BigDecimal.ZERO) != 0)
                    scadenzaNuova = (Accertamento_scadenzarioBulk) h.sdoppiaScadenzaInAutomatico(context.getUserContext(),
                            scadenzaVecchia,
                            dettaglioSelezionato.getIm_riga_sdoppia().subtract(importoIva));
                else
                    scadenzaNuova = (Accertamento_scadenzarioBulk) h.sdoppiaScadenzaInAutomatico(context.getUserContext(),
                            scadenzaVecchia,
                            scadenzaVecchia.getIm_scadenza().subtract(dettaglioSelezionato.getSaldo()).add(dettaglioSelezionato.getIm_riga_sdoppia()));

                //ricarico l'accertamento e recupero i riferimenti alle scadenze
                AccertamentoBulk accertamento = (AccertamentoBulk) h.inizializzaBulkPerModifica(context.getUserContext(),
                        scadenzaNuova.getAccertamento());

                if (!accertamento.getAccertamento_scadenzarioColl().containsByPrimaryKey(scadenzaVecchia) ||
                        !accertamento.getAccertamento_scadenzarioColl().containsByPrimaryKey(scadenzaNuova))
                    throw new ValidationException("Errore nello sdoppiamento della scadenza dell'accertamento.");

                scadenzaVecchia = accertamento.getAccertamento_scadenzarioColl().get(accertamento.getAccertamento_scadenzarioColl().indexOfByPrimaryKey(scadenzaVecchia));
                scadenzaNuova = accertamento.getAccertamento_scadenzarioColl().get(accertamento.getAccertamento_scadenzarioColl().indexOfByPrimaryKey(scadenzaNuova));
            }

            //creo la nuova riga di dettaglio e la associo al documento
            Fattura_attiva_rigaIBulk nuovoDettaglio = new Fattura_attiva_rigaIBulk();

            getDettaglio().addDetail(nuovoDettaglio);

            nuovoDettaglio = copyByRigaDocumento(context, nuovoDettaglio, dettaglioSelezionato);
            nuovoDettaglio.setQuantita(dettaglioSelezionato.getQuantita());
            nuovoDettaglio.setPrezzo_unitario(newPrezzoRigaNuova);

            nuovoDettaglio.calcolaCampiDiRiga();
            if (nuovoDettaglio.getIm_totale_divisa().compareTo(newImportoRigaNuova) != 0) {
                nuovoDettaglio.setIm_iva(nuovoDettaglio.getIm_iva().add(newImportoRigaNuova.subtract(nuovoDettaglio.getIm_totale_divisa())));
                nuovoDettaglio.setIm_totale_divisa(newImportoRigaNuova);
                nuovoDettaglio.setFl_iva_forzata(Boolean.TRUE);
                nuovoDettaglio.calcolaCampiDiRiga();
            }
            nuovoDettaglio.setIm_diponibile_nc(nuovoDettaglio.getSaldo());

            //Aggiorno la vecchia riga di dettaglio ed in particolare l'importo della riga da sdoppiare
            //del doc amministrativo
            BigDecimal oldImpTotaleDivisa = dettaglioSelezionato.getIm_totale_divisa();

            dettaglioSelezionato.setPrezzo_unitario(newPrezzoRigaVecchia);
            dettaglioSelezionato.calcolaCampiDiRiga();
            if (dettaglioSelezionato.getIm_totale_divisa().compareTo(newImportoRigaVecchia) != 0) {
                dettaglioSelezionato.setIm_iva(dettaglioSelezionato.getIm_iva().add(newImportoRigaVecchia.subtract(dettaglioSelezionato.getIm_totale_divisa())));
                dettaglioSelezionato.setIm_totale_divisa(newImportoRigaVecchia);
                dettaglioSelezionato.setFl_iva_forzata(Boolean.TRUE);
                dettaglioSelezionato.calcolaCampiDiRiga();
            }

            dettaglioSelezionato.setIm_diponibile_nc(dettaglioSelezionato.getIm_diponibile_nc().add(dettaglioSelezionato.getIm_totale_divisa().subtract(oldImpTotaleDivisa)));

            dettaglioSelezionato.setToBeUpdated();

            if (scadenzaVecchia != null) {
                for (Iterator i = documento.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                    Fattura_attiva_rigaIBulk riga = (Fattura_attiva_rigaIBulk) i.next();
                    if (riga.getAccertamento_scadenzario() != null &&
                            riga.getAccertamento_scadenzario().equalsByPrimaryKey(scadenzaVecchia)) {
                        riga.setAccertamento_scadenzario(scadenzaVecchia);
                        documento.addToDefferredSaldi(scadenzaVecchia.getAccertamento(), scadenzaVecchia.getAccertamento().getSaldiInfo());
                    }
                }
            }
            if (scadenzaNuova != null) {
                BulkList selectedModels = new BulkList();
                selectedModels.add(nuovoDettaglio);
                documento = session.contabilizzaDettagliSelezionati(context.getUserContext(), documento, selectedModels, scadenzaNuova);
                documento.addToFattura_attiva_accertamentiHash(scadenzaNuova, nuovoDettaglio);
                documento.addToDefferredSaldi(scadenzaNuova.getAccertamento(), scadenzaNuova.getAccertamento().getSaldiInfo());
            }

            documento = (Fattura_attivaBulk) session.rebuildDocumento(context.getUserContext(), documento);

            getAccertamentiController().getSelection().clear();
            getAccertamentiController().setModelIndex(context, -1);
            getAccertamentiController().setModelIndex(
                    context,
                    it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(getAccertamentiController().getDetails(), dettaglioSelezionato));

            documento.setDetailDoubled(true);

            setModel(context, documento);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Boolean
     * individua le condizioni per cui è possibile sdoppiare i dettagli del
     * documento
     * <p>
     * false: - se annullato
     * - se eliminato
     * - se interamente incassato
     * - se, indipendentemente dall'anno, è stata riportata all'esercizio successivo
     * - se non di anno corrente e non riportata all'esercizio successivo
     *
     * @return Returns the isDetailDoubleable.
     */
    public boolean isDetailDoubleable() {
        if (getModel() instanceof Fattura_attiva_IBulk) {
            Fattura_attiva_IBulk fattura = (Fattura_attiva_IBulk) getModel();
            return (!super.isInputReadonly() &&
                    !isModelVoided() &&
                    !isDeleting() &&
                    !(fattura != null && fattura.getStato_cofi() != null && fattura.isPagata()) &&
                    !(fattura.isRiportata())
                    //			!(!isAnnoDiCompetenza() && !fattura.isRiportataInScrivania()) &&
                    //			!(fattura.getTipo_documento()!=null && !fattura.getTipo_documento().getFl_utilizzo_doc_generico().booleanValue())
            ) && !this.isSearching();
        }
        return false;
    }

    /**
     * Boolean
     * true: è in corso lo sdoppiamento di una riga di dettaglio
     *
     * @return Returns the isDetailDoubling.
     */
    public boolean isDetailDoubling() {
        return isDetailDoubling;
    }

    /**
     * Setta lo stato del BP impostandolo in "Sdoppiamento riga di dettaglio"
     *
     * @param isDetailDoubling The isDetailDoubling to set.
     */
    public void setDetailDoubling(boolean isDetailDoubling) {
        this.isDetailDoubling = isDetailDoubling;
    }


    public boolean isROBank_ModPag(UserContext context, Fattura_attivaBulk fattura) throws ComponentException, RemoteException {

        if (isGestoreOk(context) && !isAnnoDiCompetenza()) {
            if (fattura != null && fattura.getStato_cofi() != null && (fattura.getStato_cofi().equals(Fattura_attivaBulk.STATO_CONTABILIZZATO) || fattura.getStato_cofi().equals(Fattura_attivaBulk.STATO_PARZIALE))) {
                for (Iterator dettagli = fattura.getFattura_attiva_dettColl().iterator(); dettagli.hasNext(); ) {
                    Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) dettagli.next();
                    if (riga != null && riga.getStato_cofi().equals(Fattura_attiva_rigaBulk.STATO_CONTABILIZZATO))

                        return false;
                }
            }
        }
        return isInputReadonly();
    }

    public boolean isROBank(UserContext context, Fattura_attivaBulk fattura) throws ComponentException, RemoteException {
        if (isROBank_ModPag(context, fattura) || isContoEnte())
            return true;
        return isInputReadonly();
    }

    public boolean isGestoreBancaFatturaAttiva() {
        return isGestoreBancaFatturaAttiva;
    }

    public void setGestoreBancaFatturaAttiva(boolean b) {
        isGestoreBancaFatturaAttiva = b;
    }

    public boolean isGestoreOk(UserContext context) throws ComponentException, RemoteException {
        setGestoreBancaFatturaAttiva(UtenteBulk.isAbilitatoModificaModPag(context));
        return isGestoreBancaFatturaAttiva;
    }

    public FatturaAttivaRigaIntrastatCRUDController getDettaglioIntrastatController() {
        return dettaglioIntrastatController;
    }

    public boolean isContoEnte() {
        return contoEnte;
    }

    public void setContoEnte(boolean b) {
        contoEnte = b;
    }

    public void ricercaDatiTrovato(ActionContext context) throws Exception {
        FatturaPassivaComponentSession h;
        Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) getDettaglio().getModel();
        try {
            h = (FatturaPassivaComponentSession) createComponentSession("CNRDOCAMM00_EJB_FatturaPassivaComponentSession", FatturaPassivaComponentSession.class);
            TrovatoBulk trovato = h.ricercaDatiTrovatoValido(context.getUserContext(), riga.getPg_trovato());
            riga.setTrovato(trovato);
        } catch (java.rmi.RemoteException e) {
            riga.setTrovato(new TrovatoBulk());
            handleException(e);
        } catch (BusinessProcessException e) {
            riga.setTrovato(new TrovatoBulk());
            handleException(e);
        } catch (Exception e) {
            riga.setTrovato(new TrovatoBulk());
            throw e;
        }
    }

    public DocumentiCollegatiDocAmmService getDocCollService() {
        return docCollService;
    }

    public void setDocCollService(DocumentiCollegatiDocAmmService docCollService) {
        this.docCollService = docCollService;
    }

    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        docCollService = SpringUtil.getBean("documentiCollegatiDocAmmService",
                DocumentiCollegatiDocAmmService.class);
    }

    public boolean isDocumentiCollegatiButtonHidden() {
        try {
            Boolean hidden = Boolean.TRUE;
            if (getStatus() == SEARCH)
                return hidden;
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) getModel();
            if (fattura != null && fattura.getPg_fattura_attiva() != null && fattura.isDocumentoFatturazioneElettronica()) {
                return docCollService.getNodeRefDocumentoAttivo(fattura) == null;
            }
            return hidden;
        } catch (DetailedException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    //	public String getDocumentiCollegatiFileName(){
//		Fattura_attivaBulk fattura = (Fattura_attivaBulk)getModel();
//		if (mandato != null){
//			return "Contabile ".
//					concat(String.valueOf(mandato.getEsercizio())).
//					concat("-").concat(mandato.getCd_cds()==null?"":mandato.getCd_cds()).
//					concat("-").concat(String.valueOf(mandato.getPg_mandato())).
//					concat(" .pdf");
//		}
//		return null;
//	}
    public void gestioneBeneBolloVirtuale(ActionContext actioncontext) throws BusinessProcessException {
        Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaIBulk) getDettaglio().getModel();
        if (riga.getBene_servizio().getFl_bollo() && !Fattura_attivaBulk.TARIFFARIO.equals(riga.getFattura_attiva().getTi_causale_emissione())) {
            try {
                BigDecimal importoBollo = ((FatturaAttivaSingolaComponentSession) createComponentSession()).getImportoBolloVirtuale(actioncontext.getUserContext(), riga.getFattura_attiva());
                if (importoBollo != null) {
                    riga.setPrezzo_unitario(importoBollo);
                    riga.setQuantita(BigDecimal.ONE);
                }
			} catch (ComponentException | RemoteException | EJBException | DetailedRuntimeException e) {
                throw handleException(e);
            }
        }
    }

    public void visualizzaDocumentoAttivo(ActionContext actioncontext) throws Exception {
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) getModel();
        InputStream is = docCollService.getStreamDocumento(fattura);
        if (is != null) {
            ((HttpActionContext) actioncontext).getResponse().setContentType("application/pdf");
            OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
            ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
            byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
            int buflength;
            while ((buflength = is.read(buffer)) > 0) {
                os.write(buffer, 0, buflength);
            }
            is.close();
            os.flush();
        }
    }

    @Override
    protected String getStorePath(Fattura_attivaBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
                allegatoParentBulk.getCd_uo_origine(),
                "Fatture Attive",
                Optional.ofNullable(allegatoParentBulk.getEsercizio())
                        .map(esercizio -> String.valueOf(esercizio))
                        .orElse("0"),
                "Fattura " + allegatoParentBulk.getEsercizio().toString() + Utility.lpad(allegatoParentBulk.getPg_fattura_attiva().toString(), 10, '0')
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
    }

    @Override
    protected Class<AllegatoFatturaAttivaBulk> getAllegatoClass() {
        return AllegatoFatturaAttivaBulk.class;
    }

    @Override
    protected boolean excludeChild(StorageObject storageObject) {
        if (storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).stream()
                .anyMatch(s -> s.equalsIgnoreCase(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ALLEGATI_NON_INVIATI_SDI.value())))
            return false;
        return super.excludeChild(storageObject);
    }

    @Override
    public String getAllegatiFormName() {
        super.getAllegatiFormName();
        return "fatturaAttiva";
    }

    public void scaricaFatturaAttivaHtml(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {

        Fattura_attivaBulk fattura = (Fattura_attivaBulk) getModel();
        Source xmlDoc = new StreamSource(docCollService.getStreamXmlFatturaAttiva(fattura));
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Source xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.2.1.xsl"));
        HttpServletResponse response = ((HttpActionContext) actioncontext).getResponse();
        OutputStream os = response.getOutputStream();
        response.setContentType("text/html");
        Transformer trasform = tFactory.newTransformer(xslDoc);
        trasform.transform(xmlDoc, new StreamResult(os));
        os.flush();
    }

    public void scaricaFatturaAttivaFirmata(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) getModel();
        StorageObject storageObject = docCollService.getFileFirmatoFatturaAttiva(fattura);

        InputStream is = SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject);
        ((HttpActionContext) actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
        ((HttpActionContext) actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
        OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
        ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
        byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
        int buflength;
        while ((buflength = is.read(buffer)) > 0) {
            os.write(buffer, 0, buflength);
        }
        is.close();
        os.flush();
    }

    public void ristampaFatturaElettronica(it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {
        try {
            Fattura_attivaBulk fattura = (Fattura_attivaBulk) getModel();
            if (fattura != null && fattura.isDocumentoFatturazioneElettronica() && !isRistampaFatturaElettronicaButtonHidden()) {
                File file = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(
                        context.getUserContext(),
                        fattura
                );
                if (file != null) {
                    ((HttpActionContext) context).getResponse().setContentLength((int) file.length());
                    ((HttpActionContext) context).getResponse().setContentType("application/pdf");
                    OutputStream os = ((HttpActionContext) context).getResponse().getOutputStream();
                    ((HttpActionContext) context).getResponse().setDateHeader("Expires", 0);
                    byte[] buffer = new byte[((HttpActionContext) context).getResponse().getBufferSize()];
                    int buflength;
                    InputStream is = new FileInputStream(file);
                    while ((buflength = is.read(buffer)) > 0) {
                        os.write(buffer, 0, buflength);
                    }
                    is.close();
                    os.flush();
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    public CollapsableDetailCRUDController getMovimentiDare() {
        return movimentiDare;
    }

    public CollapsableDetailCRUDController getMovimentiAvere() {
        return movimentiAvere;
    }

}
