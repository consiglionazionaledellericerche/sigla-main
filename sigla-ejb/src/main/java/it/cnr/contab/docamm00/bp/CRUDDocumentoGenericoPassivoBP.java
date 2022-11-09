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

/**
 * Documento generico Attivo BP
 */

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.coepcoan00.bp.CRUDScritturaPDoppiaBP;
import it.cnr.contab.coepcoan00.bp.EconomicaAvereDetailCRUDController;
import it.cnr.contab.coepcoan00.bp.EconomicaDareDetailCRUDController;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDDocumentoGenericoPassivoBP
        extends SimpleCRUDBP
        implements IDocumentoAmministrativoBP, IGenericSearchDocAmmBP, IDefferedUpdateSaldiBP, VoidableBP, IDocumentoAmministrativoSpesaBP, IDocAmmEconomicaBP {

    private final SimpleDetailCRUDController dettaglio = new DocumentoGenericoPassivoRigaCRUDController("Dettaglio", Documento_generico_rigaBulk.class, "documento_generico_dettColl", this);
    private final ObbligazioniCRUDController obbligazioniController =
            new ObbligazioniCRUDController("Obbligazioni", it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class, "documento_generico_obbligazioniHash", this);
    private final SimpleDetailCRUDController dettaglioObbligazioneController;
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
    private boolean attivaEconomicaParallela = false;
    private boolean supervisore = false;

    public CRUDDocumentoGenericoPassivoBP() {
        super();

        setTab("tab", "tabDocumentoPassivo");
        setTab("tabDocumentoPassivo", "tabDocumentoPassivo");

        dettaglioObbligazioneController = new SimpleDetailCRUDController("DettaglioObbligazioni", Documento_generico_rigaBulk.class, "documento_generico_obbligazioniHash", obbligazioniController) {

            public java.util.List getDetails() {

                Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoPassivoBP.this.getModel();
                java.util.Vector lista = new java.util.Vector();
                if (doc != null) {
                    java.util.Hashtable h = doc.getDocumento_generico_obbligazioniHash();
                    if (h != null && getParentModel() != null)
                        lista = (java.util.Vector) h.get(getParentModel());
                }
                return lista;
            }
        };


    }

    public CRUDDocumentoGenericoPassivoBP(String function) throws BusinessProcessException {
        super(function + "Tr");
        dettaglioObbligazioneController = new SimpleDetailCRUDController("DettaglioObbligazioni", Documento_generico_rigaBulk.class, "documento_generico_obbligazioniHash", obbligazioniController) {

            public java.util.List getDetails() {

                Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoPassivoBP.this.getModel();
                java.util.Vector lista = new java.util.Vector();
                if (doc != null) {
                    java.util.Hashtable h = doc.getDocumento_generico_obbligazioniHash();
                    if (h != null && getParentModel() != null)
                        lista = (java.util.Vector) h.get(getParentModel());
                }
                return lista;
            }
        };


    }

    protected void basicEdit(it.cnr.jada.action.ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        try {
            Documento_genericoBulk doc = (Documento_genericoBulk) bulk;
            setAnnoDiCompetenza(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() == doc.getEsercizio().intValue());
            super.basicEdit(context, doc, doInitializeForEdit);
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    protected boolean basicRiportaButtonHidden() {
        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return isAnnoSolareInScrivania() ||
                !isRiportaAvantiIndietro() ||
                isDeleting() ||
                isModelVoided() ||
                (doc != null && doc.isPagata()) ||
                !isEditing();
    }

    public void create(it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            getModel().setToBeCreated();
            setModel(
                    context,
                    ((DocumentoGenericoComponentSession) createComponentSession()).creaConBulk(
                            context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
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

    public it.cnr.jada.bulk.OggettoBulk createNewBulk(
            it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {
        //al momento della creazione di un nuovo oggetto bulk imposta il tipo di documento
        Documento_genericoBulk generico =
                (Documento_genericoBulk) super.createNewBulk(context);
        if (generico.getCd_tipo_documento_amm() == null) {
            //generico.setCd_tipo_documento_amm(generico.GENERICO_S);
            generico.setTi_entrate_spese(Documento_genericoBulk.SPESE);
        }
        return generico;
    }

    public it.cnr.jada.bulk.OggettoBulk createNewSearchBulk(
            it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        return createNewBulk(context);
    }

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[11];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.freeSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.new");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.bringBack");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.undoBringBack");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaIndietro");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaAvanti");
        toolbar = IDocAmmEconomicaBP.addPartitario(toolbar, attivaEconomicaParallela, isEditing(), getModel());
        return toolbar;
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
    }

    /**
     * Effettua una operazione di ricerca per un attributo di un modello.
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param filtro      modello che fa da contesto alla ricerca (il modello del FormController padre del
     *                    controller che ha scatenato la ricerca)
     * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
     **/
    public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.UserContext userContext, Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.action.BusinessProcessException {

        try {

            DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession) createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);
            return sess.cercaObbligazioni(userContext, filtro);

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
    public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {

        try {

            DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession) createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
                    actionContext,
                    sess.cerca(
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

    public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {

        return null;
    }

    public IDocumentoAmministrativoBulk getBringBackDocAmm() {

        return getDocumentoAmministrativoCorrente();
    }

    public String getColumnsetForGenericSearch() {

        return "filtro_ricerca_documenti";
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
    public final SimpleDetailCRUDController getDettaglio() {
        return dettaglio;
    }

    /**
     * Restituisce il valore della proprietà 'dettaglioObbligazioneController'
     *
     * @return Il valore della proprietà 'dettaglioObbligazioneController'
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglioObbligazioneController() {
        return dettaglioObbligazioneController;
    }

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {

        return (IDocumentoAmministrativoBulk) getModel();
    }

    public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
        if (getObbligazioniController() == null)
            return null;
        return (Obbligazione_scadenzarioBulk) getObbligazioniController().getModel();
    }

    /**
     * Restituisce il valore della proprietà 'obbligazioniController'
     *
     * @return Il valore della proprietà 'obbligazioniController'
     */
    public final ObbligazioniCRUDController getObbligazioniController() {
        return obbligazioniController;
    }

    public String getPropertyForGenericSearch() {

        return null;
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
            int solaris = Documento_genericoBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR);
            int esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
            attivaEconomicaParallela = Utility.createConfigurazioneCnrComponentSession().isAttivaEconomicaParallela(context.getUserContext());
            setSupervisore(Utility.createUtenteComponentSession().isSupervisore(context.getUserContext()));
            setAnnoSolareInScrivania(solaris == esercizioScrivania);
            setRibaltato(initRibaltato(context));
            if (!isAnnoSolareInScrivania()) {
                String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
                try {
                    DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession();
                    boolean esercizioScrivaniaAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania)));
                    boolean esercizioSuccessivoAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania + 1)));
                    setRiportaAvantiIndietro(esercizioScrivaniaAperto && esercizioSuccessivoAperto && isRibaltato());
                } catch (Throwable t) {
                    handleException(t);
                }
            } else
                setRiportaAvantiIndietro(false);
        } catch (EJBException | RemoteException | ComponentException e) {
            setAnnoSolareInScrivania(false);
        }
        resetTabs();
    }

    public it.cnr.jada.ejb.CRUDComponentSession initializeModelForGenericSearch(
            it.cnr.jada.util.action.BulkBP bp,
            it.cnr.jada.action.ActionContext context)
            throws BusinessProcessException {

        DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession();

        Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk) bp.getModel();
        Documento_genericoBulk dg = (Documento_genericoBulk) filtro.getInstance();

        try {
            Documento_genericoBulk clause = (Documento_genericoBulk) dg.clone();
            clause.setTi_entrate_spese(Documento_genericoBulk.ENTRATE);
            java.util.Collection tipiDocumentoEntrata = session.findTipi_doc_for_search(context.getUserContext(), clause);
            dg.setTipi_doc_for_search(tipiDocumentoEntrata);
        } catch (Throwable t) {
            throw new BusinessProcessException(t.getMessage(), t);
        }

        return session;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2003 16.48.19)
     *
     * @return boolean
     */
    public boolean isAnnoDiCompetenza() {
        return annoDiCompetenza;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2003 16.48.19)
     *
     * @param newAnnoDiCompetenza boolean
     */
    public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
        annoDiCompetenza = newAnnoDiCompetenza;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.46.07)
     *
     * @return boolean
     */
    public boolean isAnnoSolareInScrivania() {
        return annoSolareInScrivania;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.46.07)
     *
     * @param newAnnoSolareInScrivania boolean
     */
    public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
        annoSolareInScrivania = newAnnoSolareInScrivania;
    }

    public boolean isAutoGenerated() {
        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.46.07)
     *
     * @return boolean
     */
    public boolean isCarryingThrough() {
        return carryingThrough;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.46.07)
     *
     * @param newCarryingThrough boolean
     */
    public void setCarryingThrough(boolean newCarryingThrough) {
        carryingThrough = newCarryingThrough;
    }

    public boolean isDeleteButtonEnabled() {
        Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoPassivoBP.this.getModel();
        return super.isDeleteButtonEnabled() &&
                (doc.getStato_cofi() != null && doc.getStato_cofi().equals(Documento_genericoBulk.STATO_CONTABILIZZATO)) &&
                !isModelVoided() &&
                ((isAnnoDiCompetenza() && !doc.isRiportata()) ||
                        // Gennaro Borriello - (02/11/2004 16.48.21)
                        // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
                        (!isAnnoDiCompetenza() && doc.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(doc.getRiportataInScrivania())));
        //&& (!doc.getTi_associato_manrev().equals(doc.PARZIALMENTE_ASSOCIATO_A_MANDATO));;
    }

    public boolean isDeleting() {
        return isDeleting;
    }

    public boolean isFreeSearchButtonHidden() {
        return super.isFreeSearchButtonHidden() || isSpesaBP();
    }

    public boolean isInputReadonly() {
        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return (
                super.isInputReadonly()
                        || isDeleting()
                        || isModelVoided()
                        || !isAnnoDiCompetenza()
                        //Gennaro Borriello - (03/11/2004 19.04.48)
                        // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
                        //	DA UN ES. PRECEDENTE a quello di scrivania.
                        || (doc != null && (doc.isPagata() ||
                        (isAnnoDiCompetenza() && doc.isRiportata()))
                )
                        || (doc.getTipo_documento() != null
                        && !(doc.getTipo_documento() != null && doc.getTipo_documento().getFl_utilizzo_doc_generico() != null && doc.getTipo_documento().getFl_utilizzo_doc_generico().booleanValue())))
                && !this.isSearching();
    }

    public boolean isManualModify() {
        Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoPassivoBP.this.getModel();
        return doc.getTipo_documento() != null && !doc.getTipo_documento().getDs_tipo_documento_amm().equalsIgnoreCase("TRASF_S");
    }

    public boolean isModelVoided() {

        return !isSearching() && getModel() != null && ((Voidable) getModel()).isAnnullato();
    }

    public boolean isRiportaAvantiButtonEnabled() {

        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return isCarryingThrough() || !doc.isRiportata();
    }

    public boolean isRiportaAvantiButtonHidden() {

        return basicRiportaButtonHidden();
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.46.07)
     *
     * @return boolean
     */
    public boolean isRiportaAvantiIndietro() {
        return riportaAvantiIndietro;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.46.07)
     *
     * @param newRiportaAvantiIndietro boolean
     */
    public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
        riportaAvantiIndietro = newRiportaAvantiIndietro;
    }

    public boolean isRiportaIndietroButtonEnabled() {

        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return isEditing() &&
                !isDeleting() &&
                !isModelVoided() &&
                !isDirty() &&
                !doc.isPagata() &&
                !isCarryingThrough();
    }

    public boolean isRiportaIndietroButtonHidden() {

        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return basicRiportaButtonHidden() ||
                (doc != null && !(doc.isRiportata() || isCarryingThrough()));
    }

    public boolean isSaveButtonEnabled() {
        Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoPassivoBP.this.getModel();
        //return super.isSaveButtonEnabled() && isEditable() && (doc.getStato_cofi()!=null && (doc.getStato_cofi().equals(doc.STATO_PARZIALE) || doc.getStato_cofi().equals(doc.STATO_CONTABILIZZATO))) && !isModelVoided();
        return super.isSaveButtonEnabled() &&
                isEditable() &&
                !isModelVoided() &&
			/* RP per consentire salvataggio delle associazioni con l'inventario
			 * tutti i dati risultano comunque non aggiornabili
			!doc.isPagata() && */
                ((isAnnoDiCompetenza() && !doc.isRiportata())
                        || carryingThrough
                        // Consentire salvataggio
                        || (!doc.isEditable() && doc.getCrudStatus() != 5)
                        // Gennaro Borriello - (02/11/2004 16.48.21)
                        // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
                        || (doc.COMPLETAMENTE_RIPORTATO.equals(doc.getRiportataInScrivania()) && !isAnnoDiCompetenza()));

    }

    public boolean isSpesaBP() {

        return getParent() != null && (getParent() instanceof it.cnr.contab.fondecon00.bp.FondoSpesaBP);
    }

    /**
     * Attiva oltre al normale reset il metodo di set dei tab di default.
     *
     * @param context <code>ActionContext</code>
     * @see resetTabs
     */

    public void reset(ActionContext context) throws BusinessProcessException {

        setCarryingThrough(false);
        super.reset(context);
        resetTabs();
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
        setTab("tab", "tabDocumentoPassivo");
		setTab("tabEconomica", "tabDare");
    }

    public void riportaAvanti(ActionContext context)
            throws ValidationException, BusinessProcessException {

        try {
            DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession();
            Documento_genericoBulk docCarried =
                    (Documento_genericoBulk) session.riportaAvanti(
                            context.getUserContext(),
                            (IDocumentoAmministrativoBulk) getModel(),
                            getUserConfirm());
            setModel(context, docCarried);
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
            DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession();
            Documento_genericoBulk docCarried = (Documento_genericoBulk) session.riportaIndietro(
                    context.getUserContext(),
                    (IDocumentoAmministrativoBulk) getModel());
            basicEdit(context, docCarried, true);
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

            DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession) createComponentSession();
            sess.rollbackToSavePoint(context.getUserContext(), savePointName);

        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    public void salvaRiportandoAvanti(ActionContext context)
            throws ValidationException, BusinessProcessException {

        Documento_genericoBulk docClone = (Documento_genericoBulk) getModel();
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
            setModel(context, docClone);
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

            DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession) createComponentSession();
            sess.setSavePoint(context.getUserContext(), savePointName);

        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    public void update(it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            getModel().setToBeUpdated();
            setModel(
                    context,
                    ((DocumentoGenericoComponentSession) createComponentSession()).modificaConBulk(
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
        writeInventarioToolbar(writer);
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

    public void writeInventarioToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException, javax.servlet.ServletException {

        if (!isSearching() && !isDeleting()) {
            if (this.getParentRoot().isBootstrap()) {
                writer.println("<!-- TOOLBAR INVENTARIO -->");
                writer.println("<div id=\"inventarioToolbar\" class=\"btn-toolbar\" role=\"toolbar\" aria-label=\"Toolbar with button groups\">");
                JSPUtils.toolbarBootstrap(writer, Arrays.asList(createInventarioToolbar()), this);
                writer.println("</div>");
                writer.println("<!-- FINE TOOLBAR INVENTARIO -->");
            } else {
                openToolbar(writer);
                it.cnr.jada.util.jsp.JSPUtils.toolbar(writer, createInventarioToolbar(), this, this.getParentRoot().isBootstrap());
                closeToolbar(writer);
            }
        }
    }

    protected it.cnr.jada.util.jsp.Button[] createInventarioToolbar() {

        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.inventaria");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.associaInventario");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.inventariaPerAumento");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.beni_coll");
        return toolbar;
    }

    public boolean isAssociaInventarioButtonEnabled() {

        return (isEditing() || isInserting());
    }

    public boolean isAssociaInventarioButtonHidden() {
        return isSearching() || isDeleting();
    }

    public boolean isInventariaButtonEnabled() {

        return (isEditing() || isInserting());
    }

    public boolean isInventariaButtonHidden() {
        return isSearching() || isDeleting();
    }

    public boolean isInventariaPerAumentoButtonEnabled() {

        return (isEditing() || isInserting());
			/*getModel() != null &&
			!getDettaglio().getDetails().isEmpty() &&
			!((Documento_genericoBulk)getModel()).isGenerataDaCompenso() &&
			(isAnnoDiCompetenza())*/

    }

    public boolean isInventariaPerAumentoButtonHidden() {
        return isSearching() || isDeleting();
    }

    public boolean isBeni_collButtonEnabled() {
        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        if (doc.getHa_beniColl() == null)
            return false;
        else
            return
                    getModel() != null &&
                            !getDettaglio().getDetails().isEmpty() &&
                            (doc.getHa_beniColl().booleanValue());
    }

    public boolean isBeni_collButtonHidden() {
        return isSearching() || isDeleting();
    }

    /**
     * Il metodo è stato sovrascritto per consentire all'utente di modificare lo stato della liquidazione
     * quando il documento non risulta essere modificabile
     */
    public void writeFormInput(javax.servlet.jsp.JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3) throws java.io.IOException {
        Documento_genericoBulk doc = null;
        if (getModel() != null)
            doc = (Documento_genericoBulk) getModel();
        if (doc != null &&
                doc.isRiportataInScrivania() &&
                !doc.isPagata() &&
                isInputReadonly() &&
                s1.equals("stato_liquidazione")) {
            getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, "onChange=\"submitForm('doOnStatoLiquidazioneChange')\"", getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
        } else if (doc != null &&
                doc.isRiportataInScrivania() &&
                !doc.isPagata() &&
                isInputReadonly() &&
                s1.equals("causale")) {
            getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, "onChange=\"submitForm('doOnCausaleChange')\"", getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());

        } else if (doc != null && doc.isRiportataInScrivania() && !doc.isPagata()
                && isInputReadonly() && s1.equals("sospeso")) {
            getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag,
                    s2, "",
                    getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
        } else
            super.writeFormInput(jspwriter, s, s1, flag, s2, s3);
    }

    public void writeFormFieldDoc1210(javax.servlet.jsp.JspWriter out, String name) throws java.io.IOException {

        Documento_genericoBulk dg = (Documento_genericoBulk) getModel();


        boolean isReadonly = isInputReadonly();

        if (dg.COMPLETAMENTE_RIPORTATO.equals(dg.getRiportataInScrivania())) {
            isReadonly = isDeleting()
                    || isModelVoided()
                    || (dg != null && (dg.isPagata() && !isSearching()));
        }


        getBulkInfo().writeFormField(out, dg, null, name, getInputPrefix(), 1, 1, getStatus(), isReadonly, getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

    private static final String[] TAB_TESTATA = new String[]{ "tabDocumentoPassivo","Documento Generico","/docamm00/tab_documento_passivo.jsp" };
    private static final String[] TAB_DETTAGLIO = new String[]{ "tabDocumentoPassivoDettaglio","Dettaglio","/docamm00/tab_documento_passivo_dettaglio.jsp" };
    private static final String[] TAB_OBBLIGAZIONE = new String[]{ "tabDocumentoGenericoObbligazioni","Impegni","/docamm00/tab_documento_generico_obbligazioni.jsp" };
    private static final String[] TAB_LETTERA_PAGAMENTO_ESTERO = new String[]{ "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_generico_lettera_pagam_estero.jsp"};

    public String[][] getTabs() {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;
        pages.put(i++, TAB_TESTATA);
        pages.put(i++, TAB_DETTAGLIO);
        pages.put(i++, TAB_OBBLIGAZIONE);
        pages.put(i++, TAB_LETTERA_PAGAMENTO_ESTERO);
        if (attivaEconomicaParallela) {
            pages.put(i++, CRUDScritturaPDoppiaBP.TAB_ECONOMICA);
        }
        String[][] tabs = new String[i][3];
        for (int j = 0; j < i; j++)
            tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
        return tabs;
    }

    public CollapsableDetailCRUDController getMovimentiDare() {
		return movimentiDare;
	}

	public CollapsableDetailCRUDController getMovimentiAvere() {
		return movimentiAvere;
	}

    public boolean isSupervisore() {
        return supervisore;
    }

    public void setSupervisore(boolean supervisore) {
        this.supervisore = supervisore;
    }

    public boolean isButtonGeneraScritturaVisible() {
        return this.isSupervisore();
    }
}
