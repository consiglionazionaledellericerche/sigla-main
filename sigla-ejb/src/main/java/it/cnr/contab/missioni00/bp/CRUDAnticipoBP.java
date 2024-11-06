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

package it.cnr.contab.missioni00.bp;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.coepcoan00.bp.CRUDScritturaPDoppiaBP;
import it.cnr.contab.coepcoan00.bp.EconomicaAvereDetailCRUDController;
import it.cnr.contab.coepcoan00.bp.EconomicaDareDetailCRUDController;
import it.cnr.contab.docamm00.bp.IDocAmmEconomicaBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.bp.IValidaDocContBP;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.RimborsoBulk;
import it.cnr.contab.missioni00.ejb.AnticipoComponentSession;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Insert the type's description here.
 * Creation date: (21/05/2002 12.24.28)
 *
 * @author: Paola sala
 */
public class CRUDAnticipoBP extends it.cnr.jada.util.action.SimpleCRUDBP implements
		IDefferedUpdateSaldiBP, IDocumentoAmministrativoSpesaBP, IValidaDocContBP, IDocAmmEconomicaBP {
    private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;

    //	Variabili usate per la gestione del "RIPORTA" documento ad esercizio precedente/successivo
    private boolean annoSolareInScrivania = true;
    private boolean riportaAvantiIndietro = false;
    private boolean carryingThrough = false;
    private boolean ribaltato;
    private final CollapsableDetailCRUDController movimentiDare = new EconomicaDareDetailCRUDController(this);
    private final CollapsableDetailCRUDController movimentiAvere = new EconomicaAvereDetailCRUDController(this);
    private boolean attivaEconomicaParallela = false;

	/**
     * CRUDAnticipoBP constructor comment.
     */
    public CRUDAnticipoBP() {
        this("Tr");
    }

    /**
     * CRUDAnticipoBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDAnticipoBP(String function) {
        super(function + "Tr");
        setTab("tab", "tabAnagrafico");                // Mette il fuoco sul primo TAB
    }

	@Override
	protected void resetTabs(ActionContext actioncontext) {
		super.resetTabs(actioncontext);
		setTab("tab", "tabAnagrafico");                // Mette il fuoco sul primo TAB
		setTab("tabEconomica", "tabDare");
	}

	/**
     * Il metodo gestisce l'abilitazione o meno dei bottoni dell'obbligazione.
     * Abilitazione :
     * - l'anticipo non deve essere pagato ne' associato a missione ne' rimborsato (isEditable)
     * - se l'esercizio di scrivania e quello dell'anticipo sono diversi da quello solare l' obbligazione
     * non deve essere stata riportata (isROPerChiusura)
     */

    public boolean areBottoniObbligazioneAbilitati() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        return (anticipo != null && anticipo.isEditable() &&
                !isSearching() && !isViewing() && !anticipo.isROPerChiusura());
    }

    /**
     * Il metodo stabilisce se il documento da visualizzare deve essere aperto in visualizzazione
     * o in modifica.
     * Il metodo avvisa l'utente con messaggi che descrivono un particolare stato del documento.
     */

    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        super.basicEdit(context, bulk, doInitializeForEdit);

        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        if (getStatus() != VIEW && anticipo != null) {
            if (anticipo.isCancellatoLogicamente() || anticipo.isCancellatoLogicamente()) {
                setMessage("Anticipo annullato.");
                setStatus(VIEW);
                return;
            }

            if (anticipo.isAnticipoConMissione())
                setMessage("Anticipo legato a missione.");

            if (anticipo.hasRimborso())
                setMessage("Anticipo rimborsato. Non consentita la modifica.");
        }
    }


    /**
     * Il metodo stabilisce le condizioni generali per cui i bottoni di riporta avanti/indietro
     * dell'anticipo o del rimborso non devono essere visualizzati.
     * Per l'anticipo NON sono visibili :
     * - l'anno di scrivania e' uguale  aquello solare (isAnnoSolareInScrivania)
     * - se l'esercizio di scrivania e/o quello successivo non sono aperti (isRiportaAvantiIndietro)
     * - se l'anticipo e' stato pagato
     * - se l'anticipo e' satto cancellato
     * Per il rimborso NON sono visibili :
     * - l'anno di scrivania e' uguale  aquello solare (isAnnoSolareInScrivania)
     * - se l'esercizio di scrivania e/o quello successivo non sono aperti (isRiportaAvantiIndietro)
     * - se l'anticipo e' satto cancellato
     */

    protected boolean basicRiportaButtonHidden() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        if (!anticipo.hasRimborso())
            return isAnnoSolareInScrivania() || !isRiportaAvantiIndietro() ||
                    isDeleting() || !isEditing() || anticipo.isPagata() ||
                    anticipo.isCancellatoLogicamente() || anticipo.isAnnullato();

        return isAnnoSolareInScrivania() || !isRiportaAvantiIndietro() ||
                isDeleting() || !isEditing() || anticipo.isCancellatoLogicamente() || anticipo.isAnnullato();
    }

    /**
     * Il metodo gestisce la creazione del Rimborso previo salvataggio dell'anticipo
     */

    public void creaRimborsoCompleto(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            completeSearchTools(context, this);
            validate(context);

            saveChildren(context);
            update(context);
            ((AnticipoComponentSession) createComponentSession()).creaRimborsoCompleto(context.getUserContext(), (AnticipoBulk) getModel());
            setMessage("Creazione rimborso eseguita in modo corretto.");
            commitUserTransaction();
            try {
                basicEdit(context, getModel(), true);
            } catch (BusinessProcessException e) {
                setModel(context, null);
                setDirty(false);
                throw e;
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo gestisce l'operazione di CREATE.
     * Tale metodo e' stato reimplementato per gestire il passaggio dei parametri dei saldi per il doc contabile
     * una volta che l'utente ha deciso di eseguire lo sfondamento di cassa.
     * Vedi action metodo 'onCheckDisponibiltaDiCassaFailed'
     */

    public void create(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            getModel().setToBeCreated();
            setModel(
                    context,
                    ((AnticipoComponentSession) createComponentSession()).creaConBulk(
                            context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
            setUserConfirm(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo aggiunge alla normale toolbar del CRUD i bottoni di "crea rimborso" e "riporto avanti/indietro".
     */
    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = super.createToolbar();
        Button[] newToolbar = new Button[toolbar.length + 3];
        int i;
        for (i = 0; i < toolbar.length; i++)
            newToolbar[i] = toolbar[i];
        newToolbar[i] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.creaRimborso");
        newToolbar[i + 1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaAvanti");
        newToolbar[i + 2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaIndietro");
        return newToolbar;
    }

    /**
     * Tale metodo e' stato reimplementato per poter reinizializzare la proprietà usata per gestire il
     * "riporta indietro" del documento contabile
     */
    public void edit(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        setCarryingThrough(false);
        super.edit(context, bulk, doInitializeForEdit);
    }

    /**
     * Effettua una operazione di ricerca delle obbligazioni
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param filtro      modello che fa da contesto alla ricerca (il modello del FormController padre del
     *                    controller che ha scatenato la ricerca)
     * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
     */
    public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.action.BusinessProcessException {

        try {

            AnticipoComponentSession sess = (AnticipoComponentSession) createComponentSession();
            return sess.cercaObbligazioni(userContext, filtro);

        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    /**
     * Effettua una operazione di ricerca delle obbligazioni
     *
     * @param actionContext contesto dell'azione in corso
     * @param clauses       Albero di clausole da utilizzare per la ricerca
     * @param bulk          prototipo del modello di cui si effettua la ricerca
     * @param context       modello che fa da contesto alla ricerca (il modello del FormController padre del
     *                      controller che ha scatenato la ricerca)
     * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
     */
    public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {

        try {

            AnticipoComponentSession sess = (AnticipoComponentSession) createComponentSession();
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
     * Il metodo valida la data di registrazione inserita dall'utente e inizializza le date di competenza
     * con la stessa data.
     */

    public void gestisciCambioDataRegistrazione(ActionContext context, AnticipoBulk anticipo) throws BusinessProcessException {
        try {
            anticipo.setDt_a_competenza_coge(anticipo.getDt_registrazione());
            anticipo.setDt_da_competenza_coge(anticipo.getDt_registrazione());

            if (anticipo.getDt_registrazione() == null) {
                anticipo.setCambio(null);
                return;
            }

            // Faccio altri controlli sulla data di registrazione e carico il
            // Cambio alla data
            AnticipoComponentSession component = (AnticipoComponentSession) createComponentSession("CNRMISSIONI00_EJB_AnticipoComponentSession", AnticipoComponentSession.class);
            anticipo = component.gestisciCambioDataRegistrazione(context.getUserContext(), anticipo);
            setModel(context, anticipo);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     */

    public it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {
        return null;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     * Tale metodo ritorna l'anticipo corrente
     */

    public IDocumentoAmministrativoBulk getBringBackDocAmm() {
        return getDocumentoAmministrativoCorrente();
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldiBP
     * Tale metodo ritorna l'anticipo corrente
     */
    public it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
        return (it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi) getModel();
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferedUpdateSaldiBP
     * Tale metodo ritorna il BP corrente
     */

    public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {
        return this;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     */

    public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {
        return null;
    }

    /**
     * Metodo richiesto dall' interfaccia IValidaDocContBP
     * Il metodo ritorna l'anticipo corrente
     */

    public it.cnr.jada.bulk.OggettoBulk getDocAmmModel() {
        return getModel();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     * Il metodo ritorna l'anticipo corrente
     */

    public IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {
        return (IDocumentoAmministrativoBulk) getModel();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     * Il metodo ritorna la scadenza associata all'anticipo
     */

    public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
        return ((AnticipoBulk) getModel()).getScadenza_obbligazione();
    }

    /**
     * Il metodo ritorna la scelta dell'utente di proseguire o meno con lo sfondamento di cassa
     */
    public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
        return userConfirm;
    }

    /**
     * Tale metodo imposta il valore dell'attributo 'userConfirm'
     */
    public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter newUserConfirm) {
        userConfirm = newUserConfirm;
    }

    /**
     * Il metodo inzializza il BP
     * Il metodo verifica se l'unita' organizzativa e' di tipo ENTE. Se cosi' fosse non e' consentito
     * creare anticipi
     */
    protected void init(Config config, ActionContext context) throws BusinessProcessException {
        try {
            if (isEditable()) {
                verificoUnitaENTE(context);
            }
            attivaEconomicaParallela = Utility.createConfigurazioneCnrComponentSession().isAttivaEconomicaParallela(context.getUserContext());
        } catch (Throwable e) {
            throw handleException(e);
        }
        super.init(config, context);
    }

    /**
     * Il metodo inizializza il BP per la modifica di un documento
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */

    public it.cnr.jada.bulk.OggettoBulk initializeModelForEdit(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        AnticipoBulk anticipo = (AnticipoBulk) super.initializeModelForEdit(context, bulk);

        return initializePerRiporta(context, anticipo);
    }

    /**
     * Il metodo inizializza il BP per la ricerca libera di un documento
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */

    public it.cnr.jada.bulk.OggettoBulk initializeModelForFreeSearch(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        AnticipoBulk anticipo = (AnticipoBulk) super.initializeModelForFreeSearch(context, bulk);

        return initializePerRiporta(context, anticipo);
    }

    /**
     * Il metodo inizializza il BP per la creazione del socumento amministrativo
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */
    public it.cnr.jada.bulk.OggettoBulk initializeModelForInsert(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        AnticipoBulk anticipo = (AnticipoBulk) super.initializeModelForInsert(context, bulk);

        return initializePerRiporta(context, anticipo);
    }

    /**
     * Il metodo inizializza il BP per la ricerca di un documento
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */
    public it.cnr.jada.bulk.OggettoBulk initializeModelForSearch(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        AnticipoBulk anticipo = (AnticipoBulk) super.initializeModelForSearch(context, bulk);

        return initializePerRiporta(context, anticipo);
    }

    /**
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili :
     * - annoSolareInScrivania --> TRUE se l'anno solare coincide con quello di scrivania
     * - riportaAvantiIndietro --> TRUE se esercizio di scrivania e quello successivo sono aperti
     * Il metodo inizializza anche degli attributi dell'anticipo :
     * - annoSolare
     * - esercizioScrivania
     */
    public it.cnr.jada.bulk.OggettoBulk initializePerRiporta(ActionContext context, AnticipoBulk anticipo) throws BusinessProcessException {
        try {
            java.sql.Timestamp tsOdierno = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
            tsOdiernoGregorian.setTime(tsOdierno);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            anticipo.setAnnoSolare(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
            anticipo.setEsercizioScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue());

            setAnnoSolareInScrivania(anticipo.getAnnoSolare() == anticipo.getEsercizioScrivania());

            setRibaltato(initRibaltato(context));

            if (!isAnnoSolareInScrivania()) {
                String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
                try {
                    DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);

                    boolean esercizioScrivaniaAperto = session.verificaStatoEsercizio(context.getUserContext(), new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(cds, new Integer(anticipo.getEsercizioScrivania())));
                    boolean esercizioSuccessivoAperto = session.verificaStatoEsercizio(context.getUserContext(), new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(cds, new Integer(anticipo.getEsercizioScrivania() + 1)));
                    setRiportaAvantiIndietro(esercizioScrivaniaAperto && esercizioSuccessivoAperto && isRibaltato() && isRibaltato());
                } catch (Throwable t) {
//				handleException(t);
                    throw new BusinessProcessException(t);
                }
            } else
                setRiportaAvantiIndietro(false);
        } catch (javax.ejb.EJBException e) {
            setAnnoSolareInScrivania(false);
            throw new BusinessProcessException(e);
        }
        return anticipo;
    }

    /**
     * Il metodo ritorna il valore della proprietà 'annoSolareInScrivania'
     */

    public boolean isAnnoSolareInScrivania() {
        return annoSolareInScrivania;
    }

    /**
     * Tale metodo imposta il valore dell'attributo 'annoSolareInScrivania'
     */
    public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
        annoSolareInScrivania = newAnnoSolareInScrivania;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     */

    public boolean isAutoGenerated() {
        return false;
    }

    /**
     * Il metodo stabilisce se il bottone di 'Aggiorna manuale' della scadenza è abilitato o meno.
     * Se l'obbligazione non puo' essere modificata, il bottone "Aggiorna manuale" è comunque abilitato
     * per consentire all'utente di aprire la obbligazione in visualizzazione
     */

    public boolean isBottoneObbligazioneAggiornaManualeAbilitato() {
        return (getModel() != null && !isSearching());
    }

    /**
     * Il metodo ritorna il valore della proprietà 'carryingThrough'
     */
    public boolean isCarryingThrough() {
        return carryingThrough;
    }

    /**
     * Tale metodo imposta il valore dell'attributo 'carryigThrough'
     */
    public void setCarryingThrough(boolean newCarryingThrough) {
        carryingThrough = newCarryingThrough;
    }

    /**
     * Il metodo stabilisce se il bottone di creazione del rimborso e' abilitato o meno.
     * Le condizioni generali per l'abilitazione sono che l'anticipo sia pagato e non rimborsato.
     * Inoltre se l'esercizio dell'anticipo è uguale a quello di scrivania :
     * - se anticipo riportato --> non creo rimborso
     * - se anticipo non riportato --> creo rimborso
     */

    public boolean isCreaRimborsoButtonEnabled() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        boolean flg = false;
        flg = isEditing() && !anticipo.hasRimborso() && anticipo.isPagata();

        if (flg && anticipo.getEsercizio().intValue() == anticipo.getEsercizioScrivania())
            flg = !anticipo.isRiportata();

            //Gennaro Borriello/Luisa Farinella - (03/11/2004 19.04.48)
            // Fix sul controllo dello "Stato Riportato"
        else if (flg && anticipo.getEsercizio().intValue() != anticipo.getEsercizioScrivania())
            flg = anticipo.isRiportataInScrivania();

        return flg;
    }

    /**
     * Il metodo stabilisce se il bottone di cancellazione dell'anticipo e' abilitato o meno.
     * Le condizioni generali per l'abilitazione sono che l'anticipo non sia pagato ne' associato a missione
     * ne' rimborsato (isEditable)
     * Inoltre se l'esercizio dell'anticipo è diverso dal quello solare :
     * - se l'esercizio di scrivania è uguale da quello solare il bottone è abilitato se la scadenza è riportata
     * - se l'esercizio di scrivania è diverso da quello solare il bottone è abilitato se la scadenza non è riportata
     */

    public boolean isDeleteButtonEnabled() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        if (anticipo == null)
            return super.isDeleteButtonEnabled();

        boolean flg = super.isDeleteButtonEnabled() && anticipo.isEditable();

        //	Chiusura : se carico un anticipo con esercizio precedente a quello solare :
        //	- esercizio scrivania != anno solare e obbligazione riportata --> disabilito
        //	- esercizio scrivania != anno solare e obbligazione non riportata --> abilito
        //	- esercizio scrivania = anno solare e obbligazione riportata --> abilito
        //	- esercizio scrivania = anno solare e obbligazione non riportata --> disabilito
        //if(flg && anticipo.getEsercizio().intValue()!=anticipo.getAnnoSolare())
        //{
        //if(anticipo.getEsercizioScrivania()==anticipo.getAnnoSolare())
        //return anticipo.isRiportata();
        //else
        //return !anticipo.isRiportata();
        //}


        // Gennaro Borriello - (05/11/2004 12.23.28)
        // Modif. relativa alla nuova gestione di isRiportata()
        if (flg) {
            if (anticipo.getEsercizio().intValue() == anticipo.getEsercizioScrivania())
                return !anticipo.isRiportata();
            else
                return anticipo.isRiportataInScrivania();
        }

        return flg;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     */

    public boolean isDeleting() {
        return false;
    }

    /**
     * Il metodo disabilita il bottone di 'ricerca guidata' in caso l'utente entrasse dal Fondo Economale
     */

    public boolean isFreeSearchButtonHidden() {
        return super.isFreeSearchButtonHidden() || isSpesaBP();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     */
    public boolean isManualModify() {
        return true;
    }

    /**
     * Il metodo stabilisce se il bottone di riporto avanti del documento contabile debba essere
     * abilitato o meno.
     * Per l'anticipo è abilitato se ho appena riportato indietro il documento o se la scadenza non risulta riportata.
     * Per il rimborso è abilitato se la scadenza non risulta riportata.
     */
    public boolean isRiportaAvantiButtonEnabled() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();
        if (!anticipo.hasRimborso())
            return isCarryingThrough() || !anticipo.isRiportata();

        RimborsoBulk rimborso = anticipo.getRimborso();
        return !rimborso.isRiportata();
    }

    /**
     * Il metodo stabilisce se il bottone di riporta avanti debba essere visualizzato
     */
    public boolean isRiportaAvantiButtonHidden() {
        return basicRiportaButtonHidden();
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'riportaAvantiIndietro'
     * 'riportaAvantiIndietro' --> TRUE se esercizio di scrivania e quello successivo sono aperti
     */
    public boolean isRiportaAvantiIndietro() {
        return riportaAvantiIndietro;
    }

    /**
     * Tale metodo imposta il valore dell'attributo 'riportaAvantiIndietro'
     */
    public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
        riportaAvantiIndietro = newRiportaAvantiIndietro;
    }

    /**
     * Il metodo stabilisce se il bottone di riporto indietro del documento contabile debba essere
     * abilitato o meno.
     * Per l'anticipo è abilitato se non ho modificato l'anticipo e se non l'ho appena riportato indietro
     */
    public boolean isRiportaIndietroButtonEnabled() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();
        if (!anticipo.hasRimborso())
            return isEditing() && !isDeleting() && !isDirty() && !isCarryingThrough();

        return isEditing() && !isDeleting() && !isDirty();
    }

    /**
     * Il metodo stabilisce se il bottone di riporta indietro del documento contabile debba essere visualizzato
     */
    public boolean isRiportaIndietroButtonHidden() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        if (!anticipo.hasRimborso())
            return basicRiportaButtonHidden() || (anticipo != null && !(anticipo.isRiportata() || isCarryingThrough()));

        return basicRiportaButtonHidden() || (anticipo.getRimborso() != null && !anticipo.getRimborso().isRiportata());
    }

    /**
     * Il metodo stabilisce se il bottone di salvataggio dell'anticipo debba essere abilitato.
     * E' abilitato :
     * - se l'anticipo non e' associato a missione
     * - se l'anticipo non e' satto rimborsato
     * - se l'esercizio di scrivania e quello dell'anticipo sono diversi da quello solare l' obbligazione
     * non deve essere stata riportata (isROPerChiusura)
     */
    public boolean isSaveButtonEnabled() {
        AnticipoBulk anticipo = (AnticipoBulk) getModel();

        if (anticipo == null)
            return super.isSaveButtonEnabled();

        return super.isSaveButtonEnabled() &&
                !anticipo.isAnticipoConMissione() &&
                !anticipo.hasRimborso() &&
                //!anticipo.isROPerChiusura()
                // GB-LF-MB (08/11/2004 11.35.04)
                // Modif. relativa alla nuova gestione di isRiportata()
                (!anticipo.isROPerChiusura()
                        ||
                        carryingThrough);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     * Il metodo ritorna TRUE se l'anticipo e' stata aperta da Fondo Economale
     */

    public boolean isSpesaBP() {
        return getParent() != null && (getParent() instanceof it.cnr.contab.fondecon00.bp.FondoSpesaBP);
    }

    /**
     * Il metodo satbilisce se il bottone Annulla debba essere abilitato
     */
    public boolean isUndoBringBackButtonEnabled() {
        return super.isUndoBringBackButtonEnabled() || isViewing();
    }

    /**
     * Tale metodo e' stato reimplementato per poter reinizializzare l'attributo usato per gestire il
     * "riporta indietro" del documento contabile
     */

    public void reset(ActionContext context) throws BusinessProcessException {
        super.reset(context);
        setCarryingThrough(false);
    }

    /**
     * Tale metodo e' stato reimplementato per poter reinizializzare l'attributo usato per gestire il
     * "riporta indietro" del documento contabile
     */

    public void resetForSearch(ActionContext context) throws BusinessProcessException {
        setCarryingThrough(false);
        super.resetForSearch(context);
    }

    /**
     * Il metodo gestisce il 'riporto avanti' del documento contabile dell'anticipo
     */
    public void riportaAvanti(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException {
        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) createComponentSession("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);

            AnticipoBulk anticipoRiportato = (AnticipoBulk) session.riportaAvanti(context.getUserContext(),
                    (IDocumentoAmministrativoBulk) getModel(),
                    getUserConfirm());
            setModel(context, anticipoRiportato);
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    /**
     * Il metodo gestisce il 'riporto indietro' del documento contabile dell'anticipo
     */

    public void riportaIndietro(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        if (isDirty()) {
            setMessage("Il documento è stato modificato! Operazione annullata.");
            return;
        }

        rollbackUserTransaction();
        setCarryingThrough(true);

        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) createComponentSession("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);
            AnticipoBulk anticipoRiportato = (AnticipoBulk) session.riportaIndietro(context.getUserContext(),
                    (IDocumentoAmministrativoBulk) getModel());
            basicEdit(context, anticipoRiportato, true);
            setDirty(true);
        } catch (Throwable t) {
            setCarryingThrough(false);
            rollbackUserTransaction();
            throw handleException(t);
        }
    }

    /**
     * Il metodo gestisce il 'riporto avanti' del documento contabile del rimborso
     */

    public void riportaRimborsoAvanti(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        if (isDirty()) {
            setMessage("Il documento è stato modificato! Operazione annullata.");
            return;
        }

        rollbackUserTransaction();

        try {
            AnticipoBulk anticipo = (AnticipoBulk) getModel();
            ((AnticipoComponentSession) createComponentSession()).riportaRimborsoAvanti(context.getUserContext(), anticipo);
            commitUserTransaction();

            super.basicEdit(context, anticipo, true);
        } catch (Throwable t) {
            rollbackUserTransaction();
            throw handleException(t);
        }
    }

    /**
     * Il metodo gestisce il 'riporto indietro' del documento contabile del rimborso
     */

    public void riportaRimborsoIndietro(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        if (isDirty()) {
            setMessage("Il documento è stato modificato! Operazione annullata.");
            return;
        }

        rollbackUserTransaction();

        try {
            AnticipoBulk anticipo = (AnticipoBulk) getModel();
            ((AnticipoComponentSession) createComponentSession()).riportaRimborsoIndietro(context.getUserContext(), anticipo);
            commitUserTransaction();

            super.basicEdit(context, anticipo, true);
        } catch (Throwable t) {
            rollbackUserTransaction();
            throw handleException(t);
        }
    }

    /**
     * Annulla le modifiche apportate all'anticipo e ritorna al savepoint impostato in precedenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Rollback to savePoint
     * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata
     * Post: Tutte le modifiche effettuate sull'anticipo sino al savepoint impostato vengono annullate
     *
     * @param    context            il Context che ha generato la richiesta
     * @param    savePointName    il nome del savePoint
     */
    public void rollbackToSavePoint(ActionContext context, String savePointName) throws BusinessProcessException {
        try {
            AnticipoComponentSession sess = (AnticipoComponentSession) createComponentSession();
            sess.rollbackToSavePoint(context.getUserContext(), savePointName);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo gestisce il 'riporto avanti' del documento contabile dell'anticipo.
     * Il metodo salva l'anticipo, invoca la procedura per riportare avanti il documento contabile e committa
     */

    public void salvaRiportandoAvanti(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException {
        AnticipoBulk anticipoTemp = (AnticipoBulk) getModel();

        try {
            setSavePoint(context, "RIPORTA_AVANTI");
            completeSearchTools(context, this);
            validate(context);
            saveChildren(context);

            update(context);
            riportaAvanti(context);
        } catch (Throwable e) {
            rollbackToSavePoint(context, "RIPORTA_AVANTI");
            setModel(context, anticipoTemp);

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

    /**
     * Tale metodo e' stato reimplementato per poter reinizializzare l'attributo usato per gestire il
     * "riporta indietro" del documento contabile
     */
    public void save(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException {
        validate(context);
        super.save(context);
        setCarryingThrough(false);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     */

    public void setIsDeleting(boolean newIsDeleting) {
    }

    /**
     * Pre-post-conditions:
     * <p>
     * Nome: Imposta savePoint
     * Pre:  Una richiesta di impostare un savepoint e' stata generata
     * Post: Un savepoint e' stato impostato in modo che le modifiche apportate
     * al doc. amministrativo vengono consolidate
     *
     * @param    context            il Context che ha generato la richiesta
     * @param    savePointName    il nome del savePoint
     */
    public void setSavePoint(ActionContext context, String savePointName) throws BusinessProcessException {
        try {
            AnticipoComponentSession sess = (AnticipoComponentSession) createComponentSession();
            sess.setSavePoint(context.getUserContext(), savePointName);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    /**
     * Tale metodo e' stato reimplementato per gestire il passaggio dei parametri dei saldi per il doc contabile
     * una volta che l'utente ha deciso di eseguire lo sfondamento di cassa.
     * Vedi action metodo 'onCheckDisponibiltaDiCassaFailed'
     */
    public void update(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            getModel().setToBeUpdated();
            setModel(context,
                    ((AnticipoComponentSession) createComponentSession()).modificaConBulk(context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
            setUserConfirm(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     * Tale metodo si occupa di validare un'obbligazione appena creata o modificata.
     * Il metodo viene chiamato sul riporta dell'Obbligazione in modo da validare istantaneamente l'oggetto creato.
     */
    public void validaObbligazionePerDocAmm(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        return;
    }

    /**
     * Il metodo verifica se l'unita' organizzativa e' di tipo ENTE
     */

    public void verificoUnitaENTE(ActionContext context) throws it.cnr.jada.comp.ApplicationException {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        if (unita_organizzativa.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0)
            throw new it.cnr.jada.comp.ApplicationException("Funzione non consentita per utente abilitato a " + unita_organizzativa.getCd_unita_organizzativa());
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

    public void validaScadenze(AnticipoBulk anticipo, Obbligazione_scadenzarioBulk newScad) throws it.cnr.jada.comp.ApplicationException {

        Vector scadCanc = anticipo.getDocumentiContabiliCancellati();
        if (scadCanc != null) {
            Iterator it = scadCanc.iterator();

            while (it.hasNext()) {
                Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) it.next();
                if (scad.getObbligazione() instanceof ObbligazioneResBulk) {
                    if (scad.getObbligazione().equalsByPrimaryKey(newScad.getObbligazione()) && ((ObbligazioneResBulk) scad.getObbligazione()).getObbligazione_modifica() != null
                            && ((ObbligazioneResBulk) scad.getObbligazione()).getObbligazione_modifica().getPg_modifica() != null) {
                        throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'impegno residuo " + scad.getPg_obbligazione() + " poichè è stata effettuata una modifica in questo documento amministrativo!");
                    }
                }
            }
        }
    }

    private static final String[] TAB_ANAGRAFICO = new String[]{ "tabAnagrafico","Anagrafico","/missioni00/tab_anticipo_anagrafico.jsp" };
    private static final String[] TAB_ANTICIPO = new String[]{ "tabAnticipo","Anticipo","/missioni00/tab_anticipo.jsp" };
    private static final String[] TAB_RIMBORSO = new String[]{ "tabRimborsoAnticipo","Rimborso","/missioni00/tab_rimborso_anticipo.jsp" };

    public String[][] getTabs() {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;
        pages.put(i++, TAB_ANAGRAFICO);
        pages.put(i++, TAB_ANTICIPO);
        pages.put(i++, TAB_RIMBORSO);
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
