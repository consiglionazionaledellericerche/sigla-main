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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.coepcoan00.bp.CRUDScritturaPDoppiaBP;
import it.cnr.contab.coepcoan00.bp.EconomicaAvereDetailCRUDController;
import it.cnr.contab.coepcoan00.bp.EconomicaDareDetailCRUDController;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.TreeMap;

public class CRUDDocumentoGenericoAttivoBP
        extends SimpleCRUDBP
        implements IDocumentoAmministrativoBP, IGenericSearchDocAmmBP, IDefferedUpdateSaldiBP, VoidableBP, IDocAmmEconomicaBP {
    private final SimpleDetailCRUDController dettaglio = new DocumentoGenericoAttivoRigaCRUDController("Dettaglio", Documento_generico_rigaBulk.class, "documento_generico_dettColl", this);

    private final SimpleDetailCRUDController dettaglioAccertamentoController;
    private final AccertamentiCRUDController accertamentiController = new AccertamentiCRUDController("Accertamenti", it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk.class, "documento_generico_accertamentiHash", this);
    private final CollapsableDetailCRUDController movimentiDare = new EconomicaDareDetailCRUDController(this);
    private final CollapsableDetailCRUDController movimentiAvere = new EconomicaAvereDetailCRUDController(this);

    protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
    private boolean isDeleting = false;
    private boolean isDetailDoubling = false;
    private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
    private boolean annoDiCompetenza = true;
    private boolean annoSolareInScrivania = true;
    private boolean riportaAvantiIndietro = false;
    private boolean carryingThrough = false;
    private boolean ribaltato;
    private boolean contoEnte;
    private boolean attivaEconomicaParallela = false;

    public CRUDDocumentoGenericoAttivoBP() {
        super();
        setTab("tab", "tabDocumentoAttivo");
        setTab("tabDocumentoAttivo", "tabDocumentoAttivo");
        dettaglioAccertamentoController = new SimpleDetailCRUDController("DettaglioAccertamenti", Documento_generico_rigaBulk.class, "documento_generico_accertamentiHash", accertamentiController) {

            public java.util.List getDetails() {

                Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoAttivoBP.this.getModel();
                java.util.Vector lista = new java.util.Vector();
                if (doc != null) {
                    java.util.Hashtable h = doc.getDocumento_generico_accertamentiHash();
                    if (h != null && getParentModel() != null)
                        lista = (java.util.Vector) h.get(getParentModel());
                }
                return lista;
            }
        };

    }

    public CRUDDocumentoGenericoAttivoBP(String function) throws BusinessProcessException {
        super(function + "Tr");

        dettaglioAccertamentoController = new SimpleDetailCRUDController("DettaglioAccertamenti", Documento_generico_rigaBulk.class, "documento_generico_accertamentiHash", accertamentiController) {

            public java.util.List getDetails() {

                Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoAttivoBP.this.getModel();
                java.util.Vector lista = new java.util.Vector();
                if (doc != null) {
                    java.util.Hashtable h = doc.getDocumento_generico_accertamentiHash();
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

        Documento_genericoBulk generico =
                (Documento_genericoBulk) super.createNewBulk(context);
        //al momento della creazione di un nuovo oggetto bulk imposta il tipo di documento
        if (generico.getCd_tipo_documento_amm() == null) {
            //generico.setCd_tipo_documento_amm(generico.GENERICO_E);
            generico.setTi_entrate_spese(Documento_genericoBulk.ENTRATE);
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
        if (getModel() instanceof Documento_genericoBulk)
            ((Documento_genericoBulk) getModel()).setDocumentoModificabile(!isInputReadonly());
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

            DocumentoGenericoComponentSession component = (DocumentoGenericoComponentSession) createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);
            return component.cercaObbligazioni(userContext, filtro);

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

    public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
        return null;
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

    public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        setDetailDoubling(false);
        return super.initializeModelForInsert(actioncontext, oggettobulk);
    }

    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        setDetailDoubling(false);
        return super.initializeModelForEdit(actioncontext, oggettobulk);
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
     * Creation date: (06/06/2003 16.48.44)
     *
     * @return boolean
     */
    public boolean isAnnoDiCompetenza() {
        return annoDiCompetenza;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2003 16.48.44)
     *
     * @param newAnnoDiCompetenza boolean
     */
    public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
        annoDiCompetenza = newAnnoDiCompetenza;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.45.54)
     *
     * @return boolean
     */
    public boolean isAnnoSolareInScrivania() {
        return annoSolareInScrivania;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.45.54)
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
     * Creation date: (02/07/2003 12.45.54)
     *
     * @return boolean
     */
    public boolean isCarryingThrough() {
        return carryingThrough;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.45.54)
     *
     * @param newCarryingThrough boolean
     */
    public void setCarryingThrough(boolean newCarryingThrough) {
        carryingThrough = newCarryingThrough;
    }

    public boolean isDeleteButtonEnabled() {
        Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoAttivoBP.this.getModel();
        //return isEditable() && isEditing() && !isModelVoided() && (!doc.getTi_associato_manrev().equals(doc.PARZIALMENTE_ASSOCIATO_A_MANDATO));
        //    && (!doc.getTi_associato_manrev().equals(doc.PARZIALMENTE_ASSOCIATO_A_MANDATO));

        return super.isDeleteButtonEnabled() &&
                (doc.getStato_cofi() != null && doc.getStato_cofi().equals(Documento_genericoBulk.STATO_CONTABILIZZATO) || doc.getStato_cofi().equals(Documento_genericoBulk.STATO_INIZIALE))
                && !isModelVoided()
                && ((isAnnoDiCompetenza() && !doc.isRiportata()) ||
                // Gennaro Borriello - (02/11/2004 16.48.21)
                // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
                (!isAnnoDiCompetenza() && doc.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(doc.getRiportataInScrivania())));
    }

    public boolean isDeleting() {
        return isDeleting;
    }

    public boolean isInputReadonly() {
        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return (super.isInputReadonly() ||
                isModelVoided() ||
                isDeleting() ||
                !isAnnoDiCompetenza() ||
                (doc != null && doc.getStato_cofi() != null && doc.isPagata()) ||
                //Gennaro Borriello - (03/11/2004 19.04.48)
                // Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
                //	DA UN ES. PRECEDENTE a quello di scrivania.
                (isAnnoDiCompetenza() && doc.isRiportata()) ||
                (doc.getTipo_documento() != null && !doc.getTipo_documento().getFl_utilizzo_doc_generico().booleanValue())) && !this.isSearching();
    }

    public boolean isManualModify() {
        Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoAttivoBP.this.getModel();
        return doc.getTipo_documento() != null && !doc.getTipo_documento().getDs_tipo_documento_amm().equalsIgnoreCase("TRASF_E");
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
     * Creation date: (02/07/2003 12.45.54)
     *
     * @return boolean
     */
    public boolean isRiportaAvantiIndietro() {
        return riportaAvantiIndietro;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/07/2003 12.45.54)
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
        Documento_genericoBulk doc = (Documento_genericoBulk) CRUDDocumentoGenericoAttivoBP.this.getModel();
        return super.isSaveButtonEnabled() &&
                isEditable() &&
                !isModelVoided() &&
			        /* RP per consentire salvataggio delle associazioni con l'inventario
					 * tutti i dati risultano comunque non aggiornabili
					 !doc.isPagata() && */
                // Gennaro Borriello - (02/11/2004 16.48.21)
                //	Allineato controllo su "Stato Riportato-Anno di Competenza" così come in
                //	<code>CRUDDocumentoGenericoPassivoBP.isSaveButtonEnabled()</code>
                ((!doc.isRiportata() && isAnnoDiCompetenza()) || carryingThrough || isDetailDoubleable());
    }

    /**
     * Attiva oltre al normale reset il metodo di set dei tab di default.
     *
     * @param context <code>ActionContext</code>
     * @see resetTabs
     */

    public void reset(ActionContext context) throws BusinessProcessException {
        super.reset(context);
        setCarryingThrough(false);
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
        setTab("tab", "tabDocumentoAttivo");
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

    public void sdoppiaDettaglioInAutomatico(ActionContext context) throws ValidationException, BusinessProcessException {
        try {
            it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP.getVirtualComponentSession(context, true);
            DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession();
            Documento_genericoBulk documento = (Documento_genericoBulk) getModel();
            Documento_generico_rigaBulk dettaglioSelezionato = (Documento_generico_rigaBulk) getDettaglio().getModel();
            Accertamento_scadenzarioBulk scadenzaNuova = null;

            if (dettaglioSelezionato == null) return;
            if (documento.getStato_cofi() != null && documento.getStato_cofi().equals(Documento_genericoBulk.STATO_PAGATO))
                setMessage("Non è possibile sdoppiare righe in un documento pagato");
            if (dettaglioSelezionato.getIm_riga() == null) {
                setMessage("Non è possibile sdoppiare righe in cui l'importo di origine non è valorizzato");
                return;
            }
            if (dettaglioSelezionato.getIm_riga_sdoppia() == null ||
                    dettaglioSelezionato.getIm_riga_sdoppia().equals(Utility.ZERO) ||
                    dettaglioSelezionato.getIm_riga_sdoppia().compareTo(dettaglioSelezionato.getIm_riga()) != -1) {
                setMessage("L'importo nuovo della riga da sdoppiare deve essere positivo ed inferiore " +
                        "all'importo originario del riga stessa");
                return;
            }

            Accertamento_scadenzarioBulk scadenzaVecchia = dettaglioSelezionato.getAccertamento_scadenziario();

            java.math.BigDecimal newImportoRigaVecchia = dettaglioSelezionato.getIm_riga_sdoppia();
            java.math.BigDecimal newImportoRigaNuova = dettaglioSelezionato.getIm_riga().subtract(dettaglioSelezionato.getIm_riga_sdoppia());

            if (dettaglioSelezionato.getAccertamento_scadenziario() != null) {
                /*
                 * L'importo della scadenza vecchia sarà pari al valore iniziale diminuito del valore originario del
                 * dettaglio e aumentato del valore nuovo.
                 * Ciò al fine di gestire il caso di utenti che collegano più dettagli di documento alla stessa scadenza
                 */
                scadenzaNuova = (Accertamento_scadenzarioBulk) h.sdoppiaScadenzaInAutomatico(context.getUserContext(),
                        scadenzaVecchia,
                        scadenzaVecchia.getIm_scadenza().subtract(Utility.nvl(dettaglioSelezionato.getIm_riga())).add(Utility.nvl(dettaglioSelezionato.getIm_riga_sdoppia())));

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
            Documento_generico_rigaBulk nuovoDettaglio = new Documento_generico_rigaBulk();
            getDettaglio().addDetail(nuovoDettaglio);
            nuovoDettaglio = copyByRigaDocumento(context, nuovoDettaglio, dettaglioSelezionato);

            //Aggiorno la vecchia riga di dettaglio ed in particolare l'importo della riga da sdoppiare
            //del doc amministrativo
            dettaglioSelezionato.setIm_riga(newImportoRigaVecchia);
            dettaglioSelezionato.setIm_riga_divisa(newImportoRigaVecchia);
            dettaglioSelezionato.setToBeUpdated();

            if (scadenzaVecchia != null) {
                dettaglioSelezionato.setAccertamento_scadenziario(scadenzaVecchia);
                documento.addToDefferredSaldi(scadenzaVecchia.getAccertamento(), scadenzaVecchia.getAccertamento().getSaldiInfo());
            }
            if (scadenzaNuova != null) {
                BulkList selectedModels = new BulkList();
                selectedModels.add(nuovoDettaglio);
                documento = session.contabilizzaDettagliSelezionati(context.getUserContext(), documento, selectedModels, scadenzaNuova);
                documento.addToDefferredSaldi(scadenzaNuova.getAccertamento(), scadenzaNuova.getAccertamento().getSaldiInfo());
            }

            documento = session.rebuildDocumento(context.getUserContext(),
                    documento);

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

    private Documento_generico_rigaBulk copyByRigaDocumento(ActionContext context, Documento_generico_rigaBulk nuovoDettaglio, Documento_generico_rigaBulk origine) {
        //associo la riga creata al documento
        origine.setUser(context.getUserContext().getUser());
        if (origine.getTerzo() != null)
            nuovoDettaglio.setTerzo((TerzoBulk) origine.getTerzo().clone());
        if (origine.getTerzo_uo_cds() != null)
            nuovoDettaglio.setTerzo_uo_cds((TerzoBulk) origine.getTerzo_uo_cds().clone());
        if (origine.getAnagrafico() != null)
            nuovoDettaglio.setAnagrafico((AnagraficoBulk) origine.getAnagrafico().clone());
        if (origine.getCessionario() != null)
            nuovoDettaglio.setCessionario((TerzoBulk) origine.getCessionario().clone());
        if (origine.getTermini_pagamento() != null)
            nuovoDettaglio.setTermini_pagamento((Rif_termini_pagamentoBulk) origine.getTermini_pagamento().clone());
        if (origine.getTermini_pagamento_uo_cds() != null)
            nuovoDettaglio.setTermini_pagamento_uo_cds((Rif_termini_pagamentoBulk) origine.getTermini_pagamento_uo_cds().clone());
        nuovoDettaglio.setModalita(origine.getModalita());
        nuovoDettaglio.setModalita_uo_cds(origine.getModalita_uo_cds());
        if (origine.getModalita_pagamento() != null)
            nuovoDettaglio.setModalita_pagamento((Rif_modalita_pagamentoBulk) origine.getModalita_pagamento().clone());
        if (origine.getModalita_pagamento_uo_cds() != null)
            nuovoDettaglio.setModalita_pagamento_uo_cds((Rif_modalita_pagamentoBulk) origine.getModalita_pagamento_uo_cds().clone());
        if (origine.getBanca() != null)
            nuovoDettaglio.setBanca((BancaBulk) origine.getBanca().clone());
        if (origine.getBanca_uo_cds() != null)
            nuovoDettaglio.setBanca_uo_cds((BancaBulk) origine.getBanca_uo_cds().clone());
        nuovoDettaglio.setRagione_sociale(origine.getRagione_sociale());
        nuovoDettaglio.setEsercizio(origine.getEsercizio());
        nuovoDettaglio.setCd_cds(origine.getCd_cds());
        nuovoDettaglio.setCd_unita_organizzativa(origine.getCd_unita_organizzativa());
        nuovoDettaglio.setCd_tipo_documento_amm(origine.getCd_tipo_documento_amm());
        nuovoDettaglio.setStato_cofi(origine.getStato_cofi());
        nuovoDettaglio.setDt_da_competenza_coge(origine.getDt_da_competenza_coge());
        nuovoDettaglio.setDt_a_competenza_coge(origine.getDt_a_competenza_coge());
        nuovoDettaglio.setDs_riga(origine.getDs_riga());
        nuovoDettaglio.setCd_modalita_pag(origine.getCd_modalita_pag());
        nuovoDettaglio.setCd_modalita_pag_uo_cds(origine.getCd_modalita_pag_uo_cds());
        nuovoDettaglio.setNome(origine.getNome());
        nuovoDettaglio.setCognome(origine.getCognome());
        nuovoDettaglio.setCodice_fiscale(origine.getCodice_fiscale());
        nuovoDettaglio.setPartita_iva(origine.getPartita_iva());
        nuovoDettaglio.setIm_riga(origine.getIm_riga().subtract(origine.getIm_riga_sdoppia()));
        nuovoDettaglio.setIm_riga_divisa(origine.getIm_riga().subtract(origine.getIm_riga_sdoppia()));
        nuovoDettaglio.setPg_banca(origine.getPg_banca());
        nuovoDettaglio.setPg_banca_uo_cds(origine.getPg_banca_uo_cds());
        nuovoDettaglio.setTi_associato_manrev(origine.getTi_associato_manrev());
        nuovoDettaglio.setToBeCreated();
        return nuovoDettaglio;
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
        Documento_genericoBulk doc = (Documento_genericoBulk) getModel();
        return (!super.isInputReadonly() &&
                !isModelVoided() &&
                !isDeleting() &&
                !(doc != null && doc.getStato_cofi() != null && doc.isPagata()) &&
                !(doc.isRiportata()) &&
                !(!isAnnoDiCompetenza() && !doc.isRiportataInScrivania()) &&
                !(doc.getTipo_documento() != null && !doc.getTipo_documento().getFl_utilizzo_doc_generico().booleanValue())) && !this.isSearching();
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

        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.inventaria");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.associaInventario");
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

    public boolean isROBank_ModPag(Documento_generico_rigaBulk riga) {
        if (riga != null && (riga.getStato_cofi().equals(Documento_generico_rigaBulk.STATO_CONTABILIZZATO)))
            return false;
        return isInputReadonly();
    }

    public boolean isROBank(Documento_generico_rigaBulk riga) {
        if (isROBank_ModPag(riga) || isContoEnte())
            return true;
        return isInputReadonly();
    }

    public boolean isContoEnte() {
        return contoEnte;
    }

    public void setContoEnte(boolean contoEnte) {
        this.contoEnte = contoEnte;
    }
    private static final String[] TAB_TESTATA = new String[]{ "tabDocumentoAttivo","Documento Generico","/docamm00/tab_documento_attivo.jsp" };
    private static final String[] TAB_DETTAGLIO = new String[]{ "tabDocumentoAttivoDettaglio","Dettaglio","/docamm00/tab_documento_attivo_dettaglio.jsp" };
    private static final String[] TAB_ACCERTAMENTI = new String[]{ "tabDocumentoGenericoAccertamenti","Accertamenti","/docamm00/tab_documento_generico_accertamenti.jsp" };

    public String[][] getTabs() {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;
        pages.put(i++, TAB_TESTATA);
        pages.put(i++, TAB_DETTAGLIO);
        pages.put(i++, TAB_ACCERTAMENTI);
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

}
