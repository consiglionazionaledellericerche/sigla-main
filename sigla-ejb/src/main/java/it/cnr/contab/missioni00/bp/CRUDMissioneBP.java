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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.coepcoan00.bp.CRUDScritturaPDoppiaBP;
import it.cnr.contab.coepcoan00.bp.EconomicaAvereDetailCRUDController;
import it.cnr.contab.coepcoan00.bp.EconomicaDareDetailCRUDController;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.docamm00.bp.IDocAmmEconomicaBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.CambioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.bp.IValidaDocContBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.missioni00.docs.bulk.*;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.service.MissioniCMISService;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.DetailedException;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.upload.UploadedFile;

import javax.servlet.ServletException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

        /*
         * Insert the type's description here.
         * Creation date: (07/02/2002 13.21.21)
         * @author: Paola sala
         */

public class CRUDMissioneBP extends AllegatiCRUDBP<AllegatoMissioneBulk, MissioneBulk>
        implements IDefferedUpdateSaldiBP, IDocumentoAmministrativoSpesaBP, IValidaDocContBP, IDocAmmEconomicaBP {
    private final SimpleDetailCRUDController tappaController = new SimpleDetailCRUDController("Tappa", Missione_tappaBulk.class, "tappeMissioneColl", this) {
        @Override
        public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {
            super.writeHTMLToolbar(context, reset, find, delete, false);

            // Aggiungo alla table delle tappe il bottone di fine configurazione tappa
            it.cnr.jada.util.jsp.JSPUtils.toolbarButton(context,
                    HttpActionContext.isFromBootstrap(context) ? "fa fa-lock":"img/import16.gif",
                    isViewing() ? null : "javascript:submitForm('doFineConfigurazioneTappa')",
                    false,
                    "Fine Configurazione",
                    "btn-sm btn-title btn-outline-primary",
                    HttpActionContext.isFromBootstrap(context));
            super.closeButtonGROUPToolbar(context);
        }
    };
    private final CRUDMissione_spesaController spesaController = new CRUDMissione_spesaController("Spesa", Missione_dettaglioBulk.class, "speseMissioneColl", this) {
        @Override
        public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {
            super.writeHTMLToolbar(context, reset, find, delete, false);
            // Aggiungo alla table delle spese il bottone di fine inserimento spese
            it.cnr.jada.util.jsp.JSPUtils.toolbarButton(context,
                    HttpActionContext.isFromBootstrap(context) ? "fa fa-lock":"img/import16.gif",
                    isViewing() ? null : "javascript:submitForm('doFineInserimentoSpese')",
                    false,
                    "Fine Inserimento Spese",
                    "btn-title btn-outline-primary",
                    HttpActionContext.isFromBootstrap(context));
            super.closeButtonGROUPToolbar(context);
        }

        @Override
        public OggettoBulk removeDetail(int i) {
            List list = getDetails();
            Missione_dettaglioBulk dettaglio = (Missione_dettaglioBulk) list.get(i);
            if (dettaglio.isDettaglioMissioneFromGemis()) {
                throw new it.cnr.jada.action.MessageToUser("Dettaglio non eliminabile in quanto proveniente dalla Gestione automatica delle Missioni");
            } else {
                BulkList<AllegatoMissioneDettaglioSpesaBulk> listaDettagliAllegati = dettaglio.getDettaglioSpesaAllegati();
                if (listaDettagliAllegati != null && !listaDettagliAllegati.isEmpty()) {
                    int k;
                    for (k = 0; k < listaDettagliAllegati.size(); k++) {
                        AllegatoMissioneDettaglioSpesaBulk all = listaDettagliAllegati.get(k);
                        all.setToBeDeleted();
                    }
                }
            }
            return super.removeDetail(i);
        }

    };

    private final SimpleDetailCRUDController diariaController = new SimpleDetailCRUDController("Diaria", Missione_dettaglioBulk.class, "diariaMissioneColl", this);
    private final SimpleDetailCRUDController rimborsoController = new SimpleDetailCRUDController("Rimborso", Missione_dettaglioBulk.class, "rimborsoMissioneColl", this);
    private final SimpleDetailCRUDController consuntivoController = new SimpleDetailCRUDController("Consuntivo", Missione_dettaglioBulk.class, "speseMissioneColl", this);
    private boolean editingTappa = false;
    private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
    //	Variabili usate per la gestione del "RIPORTA" documento ad esercizio precedente/successivo
    private boolean annoSolareInScrivania = true;
    private boolean riportaAvantiIndietro = false;
    private boolean carryingThrough = false;
    private boolean ribaltato;
    private MissioniCMISService missioniCMISService;

    private final SimpleDetailCRUDController dettaglioSpesaAllegatiController = new SimpleDetailCRUDController("AllegatiDettaglioSpesa", AllegatoMissioneDettaglioSpesaBulk.class, "dettaglioSpesaAllegati", spesaController) {
        @Override
        protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
            AllegatoMissioneDettaglioSpesaBulk allegato = (AllegatoMissioneDettaglioSpesaBulk) oggettobulk;
            UploadedFile file = ((it.cnr.jada.action.HttpActionContext) actioncontext).getMultipartParameter("main.Spesa.AllegatiDettaglioSpesa.file");
            if (!(file == null || file.getName().equals(""))) {
                allegato.setFile(file.getFile());
                allegato.setContentType(file.getContentType());
                allegato.setNome(allegato.parseFilename(file.getName()));
                allegato.setAspectName(MissioniCMISService.ASPECT_MISSIONE_SIGLA_DETTAGLIO);
                allegato.setToBeUpdated();
                getParentController().setDirty(true);
            }
            oggettobulk.validate();
            super.validate(actioncontext, oggettobulk);
        }

        @Override
        public OggettoBulk removeDetail(int i) {
            if (!getModel().isNew()) {
                List list = getDetails();
                AllegatoMissioneDettaglioSpesaBulk all = (AllegatoMissioneDettaglioSpesaBulk) list.get(i);
                if (isPossibileCancellazioneDettaglioAllegato(all)) {
                    return super.removeDetail(i);
                } else {
                    return null;
                }
            }
            return super.removeDetail(i);
        }

        @Override
        public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
            int add = super.addDetail(oggettobulk);
            AllegatoMissioneDettaglioSpesaBulk all = (AllegatoMissioneDettaglioSpesaBulk) oggettobulk;
            all.setIsDetailAdded(true);
            return add;
        }
    };
    private final CollapsableDetailCRUDController movimentiDare = new EconomicaDareDetailCRUDController(this);
    private final CollapsableDetailCRUDController movimentiAvere = new EconomicaAvereDetailCRUDController(this);
    private boolean attivaEconomicaParallela = false;

    /**
     * CRUDMissioneBP constructor comment.
     */
    public CRUDMissioneBP() {
        this("Tr");
    }

    /**
     * CRUDMissioneBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDMissioneBP(String function) {
        super(function + "Tr");
        setTab("tab", "tabTestata");                // Mette il fuoco sul primo TAB
        setTab("tabDettaglioSpese", "tabDettaglioSpesa");
    }

    /**
     * Il metodo gestisce la creazione di una nuova tappa.
     * Il metodo verifica se la configurazione delle tappe può essere modificata.
     * Se i dettagli di diaria sono già stati creati li cancello
     * Se i dettagli del rimborso sono già stati creati li cancello
     */


    public void addTappa(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException, it.cnr.jada.comp.ApplicationException {
        MissioneBulk missione = (MissioneBulk) getModel();

        // Verifico se posso aggiungere una nuova tappa
        missione.isConfigurazioneTappeModificabile();
        // Se non ho spese ma ho la diaria quest'ultima dovra' essere cancellata per poter proseguire
        if ((missione.getDiariaMissioneColl() != null) && (!missione.getDiariaMissioneColl().isEmpty()))
            missione = cancellaDiariaPerModificaConfigurazioneTappe(context);

        //Se ho il rimborso lo cancello
        if ((missione.getRimborsoMissioneColl() != null) && (!missione.getRimborsoMissioneColl().isEmpty()))
            missione = cancellaRimborsoPerModificaConfigurazioneTappe(context);

        // Chiama il metodo 'addToTappeMissioneColl' che fa le opportune inizializzazioni
        getTappaController().add(context);

        setNazioneDivisaCambioItalia(context);

        // Inizio modalita' inserimento tappa
        editingTappa = true;

        // Di default la tappa e' in STATUS_NOT_CONFIRMED
    }

    /**
     * Il metodo gestisce l'abilitazione dei bottoni dell'obbligazione.
     * I bottoni della scadenza della missione vengono disabilitati :
     * - se la missione non e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare l' obbligazione non deve essere stata riportata
     * (isEditable)
     * - se la missione richiede l'associazione con un'obbligazione
     */

    public boolean areBottoniObbligazioneAbilitati() {
        MissioneBulk missione = (MissioneBulk) getModel();

        return missione != null &&
                !isSearching() &&
                !isViewing() &&
                missione.isEditable() &&
                missione.isObbligazioneObbligatoria();
    }

    /**
     * Il metodo gestisce l'abilitazione dei campi relatici all'anticipo
     * I campi dell'anticipo sono disabilitati se :
     * - se la missione e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare e l' obbligazione è stata riportata
     * (isEditable)
     * - se la missione e' associata ad un compenso pagato
     * - se la missione è associata ad un anticipo pagato
     */

    public boolean areCampiAnticipoReadonly() {
        MissioneBulk missione = (MissioneBulk) getModel();

        return (missione == null || isViewing() ||
                !missione.isEditable() || (missione.isMissioneConCompenso() && missione.getCompenso().isPagato()) ||
                missione.isAnticipoRimborsato());
    }

    /**
     * Il metodo stabilisce se il documento da visualizzare deve essere aperto in visualizzazione
     * o in modifica.
     * Il metodo avvisa l'utente con messaggi che descrivono un particolare stato del documento.
     */

    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        super.basicEdit(context, bulk, doInitializeForEdit);

        MissioneBulk missione = (MissioneBulk) getModel();

        if (getStatus() != VIEW && missione != null) {
            if (missione.isAnnullato() || missione.isCancellataLogicamente()) {
                setMessage("Missione annullata.");
                setStatus(VIEW);
            }
        }
    }


    /**
     * Il metodo stabilisce le condizioni generali per cui i bottoni di riporta avanti/indietro
     * della missione non devono essere visualizzati.
     * Essi sono visibili se :
     * - l'anno di scrivania e' uguale  aquello solare (isAnnoSolareInScrivania)
     * - l'esercizio di scrivania e/o quello successivo non sono aperti (isRiportaAvantiIndietro)
     * - la missione non è definitiva
     * - la missione non ha obbligazione
     * - la missione è pagata
     * - la missione è cancellata logicamente
     */

    protected boolean basicRiportaButtonHidden() {
        MissioneBulk missione = (MissioneBulk) getModel();

        return isAnnoSolareInScrivania() || !isRiportaAvantiIndietro() ||
                isDeleting() || !isEditing() || !missione.isMissioneDefinitiva() ||
                !missione.isMissioneConObbligazione() || missione.isPagata() ||
                missione.isCancellataLogicamente() || missione.isAnnullato();
    }

    /**
     * Il metodo gestisce la cancellazione fisica del compenso associato alla missione
     */

    public void cancellaCompensoPhisically(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();

            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            missione = component.cancellaCompensoPhisically(context.getUserContext(), missione);
            setModel(context, missione);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo gestisce la cancellazione dei dettagli di spesa e diaria della missione
     */

    public void cancellaDettagliMissione(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        missione.cancellaSpese();
        getDettaglioSpesaAllegatiController().reset(context);
        getSpesaController().reset(context);

        cancellaDiaria(context);
        missione = (MissioneBulk) getModel();
        getDiariaController().reset(context);

        cancellaRimborso(context);
        missione = (MissioneBulk) getModel();
        getRimborsoController().reset(context);

        missione.setSpeseInserite(false);
    }

    /**
     * Il metodo gestisce la cancellazione fisica dei dettagli di diaria della missione
     */

    public void cancellaDiaria(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();

            if (missione.getDiariaMissioneColl() == null || missione.getDiariaMissioneColl().isEmpty())
                return;

            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            missione = component.cancellaDiariaPhisically(context.getUserContext(), missione);

            setModel(context, missione);
            ((MissioneBulk) getModel()).cancellaDiaria();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo gestisce la cancellazione fisica dei dettagli di diaria della missione
     */

    public void cancellaRimborso(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();

            if (missione.getRimborsoMissioneColl() == null || missione.getRimborsoMissioneColl().isEmpty())
                return;

            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            missione = component.cancellaRimborsoPhisically(context.getUserContext(), missione);

            setModel(context, missione);
            ((MissioneBulk) getModel()).cancellaRimborso();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo cancella i dettagli della diaria a seguito della modifica della configurazione delle tappe
     */

    public MissioneBulk cancellaDiariaPerModificaConfigurazioneTappe(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        cancellaDiaria(context);

        setMessage("Diaria cancellata !");
        missione = (MissioneBulk) getModel();
        missione.setSpeseInserite(false);

        return missione;
    }

    /**
     * Il metodo cancella i dettagli della quota di rimborso a seguito della modifica della configurazione delle tappe
     */

    public MissioneBulk cancellaRimborsoPerModificaConfigurazioneTappe(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        cancellaRimborso(context);

        setMessage("Rimborso cancellato !");
        missione = (MissioneBulk) getModel();
        missione.setSpeseInserite(false);

        return missione;
    }

    /**
     * Il metodo cancella le tappe della missione a seguito della modifica della data inizio o fine della missione
     */

    public void cancellaTappeMissione(ActionContext context) {
        MissioneBulk missione = (MissioneBulk) getModel();

        missione.cancellaTappe();
        missione.setTappeMissioneHash(null);
        getTappaController().reset(context);

        missione.setTappeConfigurate(false);
    }

    /**
     * Il metodo cancella fisicamente le tappe inserite in tabella altrimenti,
     * essendo dt_inizio_tappa parte della chiave, non sarebbe possibile :
     * - modificare dt_inizio_tappa (ora) di una tappa gia' salvata
     * - cancellare una tappa gia' presente in tabella la cui data di inizio
     * e' cambiata o e' stata resettata
     * Predispongo le tappe all'inserimento e inizializzo le liste dei dettagli di
     * spesa/diaria perche' essi sono stati cancellati automaticamente dalla cancellazione
     * delle tappe (Constraint).
     */

    public void cancellaTappePhisically(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            missione = component.cancellaTappePhisically(context.getUserContext(), missione);
            setModel(context, missione);

            missione.ripristinaCrudStatusFigli();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo verifica la validità delle data/ora inizio/fine missione ogni volta che esse vengono
     * modificate.
     * Se i controlli non vengono superati ripristino la missione prima della modifica
     */

    public void checkValiditaInizioFineMissione(ActionContext context) throws BusinessProcessException, MessageToUser {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();

            if (missione.getDt_inizio_missione() != null &&
                    missione.getDt_inizio_missione().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp()))
                throw new it.cnr.jada.action.MessageToUser("La Data/Ora inizio missione non puo' essere futura !");

            // 	La missione non puo' iniziare alle ore 00:00
            if (missione.getDt_inizio_missione() != null) {
                java.util.GregorianCalendar inizioMissione = (java.util.GregorianCalendar) missione.getGregorianCalendar(missione.getDt_inizio_missione()).clone();
                if ((inizioMissione.get(java.util.GregorianCalendar.HOUR_OF_DAY) == 23) && (inizioMissione.get(java.util.GregorianCalendar.MINUTE) == 59))
                    throw new it.cnr.jada.action.MessageToUser("L' ora di inizio missione non e' valida !");
            }

            // 	La missione non puo' finire alle ore 23:59
            if (missione.getDt_fine_missione() != null) {
                java.util.GregorianCalendar fineMissione = (java.util.GregorianCalendar) missione.getGregorianCalendar(missione.getDt_fine_missione()).clone();
                if ((fineMissione.get(java.util.GregorianCalendar.HOUR_OF_DAY) == 0) && (fineMissione.get(java.util.GregorianCalendar.MINUTE) == 0))
                    throw new it.cnr.jada.action.MessageToUser("L' ora di fine missione non e' valida !");
            }

            if ((missione.getDt_inizio_missione() == null) || (missione.getDt_fine_missione() == null))
                return;        // se non ho ancora inserito entrambe le date non faccio controlli

            missione.checkValiditaInizioFineMissione();
        } catch (javax.ejb.EJBException e) {
            throw new BusinessProcessException(e);
        }
    }

    /**
     * Ogni qualvolta si conferma una spesa, tale metodo esegue la ricerca per i searchTool non completi
     */

    public void completaSearchToolSpesa(ActionContext context, MissioneBulk missione, Missione_dettaglioBulk spesa) throws BusinessProcessException, it.cnr.jada.bulk.ValidationException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, java.rmi.RemoteException, java.sql.SQLException {
        if (spesa.getTipo_spesa().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL) {
            spesa.getTipo_spesa().setCd_ti_spesa(spesa.getCd_ti_spesa());

            completeSearchTool(context, spesa, spesa.getBulkInfo().getFieldProperty("find_tipo_spesa"));

            spesa.setPercentuale_maggiorazione(spesa.getTipo_spesa().getPercentuale_maggiorazione());
            spesa.setDs_ti_spesa(spesa.getTipo_spesa().getDs_ti_spesa());
            spesa.impostaTipologiaSpesa();

            if (spesa.isRimborsoKm()) {
                missione = inizializzaDivisaCambioPerRimborsoKm(context, spesa);
                setModel(context, missione);
                spesa = (Missione_dettaglioBulk) getSpesaController().getModel();
            }
        }

        if (spesa.isPasto() && spesa.getTipo_pasto() != null && spesa.getTipo_pasto().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL) {
            spesa.getTipo_pasto().setCd_ti_pasto(spesa.getCd_ti_pasto());
            completeSearchTool(context, spesa, spesa.getBulkInfo().getFieldProperty("find_tipo_pasto"));
        }

        if (spesa.isRimborsoKm() && spesa.getTipo_auto() != null && spesa.getTipo_auto().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL &&
                spesa.getTi_auto() != null) {
            spesa.getTipo_auto().setTi_auto(spesa.getTi_auto());
            completeSearchTool(context, spesa, spesa.getBulkInfo().getFieldProperty("find_tipo_auto"));
        }

        if (spesa.getDivisa_spesa().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL) {
            completeSearchTool(context, spesa, spesa.getBulkInfo().getFieldProperty("find_divisa_spesa"));
            setCambioSpesaDefault(context, spesa);
        }
        if (!spesa.isPasto())
            spesa.setTipo_pasto(null);

        if (!spesa.isRimborsoKm())
            spesa.setTipo_auto(null);

        if (!spesa.isTrasporto())
            spesa.setIm_maggiorazione_euro(new java.math.BigDecimal(0));
    }

    /**
     * Ogni qualvolta si conferma una tappa, tale metodo esegue la ricerca per i searchTool non completi
     */

    public Missione_tappaBulk completaSearchToolTappa(ActionContext context, Missione_tappaBulk tappa) throws BusinessProcessException, it.cnr.jada.bulk.ValidationException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException, it.cnr.jada.persistency.PersistencyException {
        if (tappa.getNazione() != null && tappa.getNazione().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL) {
            completeSearchTool(context, tappa, tappa.getBulkInfo().getFieldProperty("find_nazione"));
            if (tappa.getNazione() != null && tappa.getPg_nazione() != null) {
                setDivisaCambioTappaEstera(context, tappa);
                tappa = (Missione_tappaBulk) getTappaController().getModel();
            }
        }

        return tappa;
    }

    /**
     * Il metodo verifica che la spesa che si sta inserendo sia corretta.
     * <p>
     * Se l'utente ha selezionato piu' giorni faccio i controlli, le conversioni
     * delle valute, i calcoli degli importi (trasporto, rimborso km) ....
     * sulla spesa del primo giorno selezionato e con importo pari all'importo
     * inserito dall'utente e diviso per il numero di giorni selezionati.
     * Al termine creo n-1 istanze di spesa uguali a quella processata (del primo giorno)
     * e le aggiungo alla collection dei dettagli assegnando ogni volta un giorno diverso
     * e un progressivo diverso
     */

    public void confermaSpesa(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();
        Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) getSpesaController().getModel();

        //	Salvo i totali in modo tal che in caso di errore posso
        //	ripristinarli
        java.math.BigDecimal kmTot = spesa.getChilometri();
        java.math.BigDecimal importoSpesaTot = spesa.getIm_spesa_divisa();
        java.math.BigDecimal baseMaggTot = spesa.getIm_base_maggiorazione();

        try {
            //	Assegno alla spesa i primo giorno tra quelli selezionati.
            //	I controlli e le ricerche dei cambi verrano fatte in base a questa data
            spesa.setDt_inizio_tappa(missione.getPrimoGiornoSpesaSelezionato());
            if (spesa.getDt_inizio_tappa() == null)
                throw new it.cnr.jada.comp.ApplicationException("Selezionare almeno un giorno!");

            completaSearchToolSpesa(context, missione, spesa);

            spesa.validaSpesa();

            //	Se l'utente ha selezionato piu' giorni :
            //		- se la spesa non e' un rimborso km, assegno alla spesa l'importo giornaliero.
            //		- se la spesa e' un trasporto assegno anche l'importo base maggiorazione
            //		  giornaliero
            //		- se la spesa e' un rimborso km, assegno alla spesa il numero di km giornalieri
            if (missione.isSelezioneGiorniSpesaMultipla()) {
                int nGiorni = missione.getCollectionGiorniSpeseSelezionati().size();

                if (!spesa.isRimborsoKm()) {
                    java.math.BigDecimal importoGiornaliero = spesa.getIm_spesa_divisa().divide(new java.math.BigDecimal(nGiorni), java.math.BigDecimal.ROUND_HALF_UP);
                    if (importoGiornaliero.compareTo(new java.math.BigDecimal(0)) < 1)
                        throw new it.cnr.jada.comp.ApplicationException("L'importo giornaliero della spesa e' nullo!");

                    spesa.setIm_spesa_divisa(importoGiornaliero);

                    if (spesa.isTrasporto())
                        spesa.setIm_base_maggiorazione(spesa.getIm_spesa_divisa());
                } else {
                    java.math.BigDecimal kmGiornalieri = spesa.getChilometri().divide(new java.math.BigDecimal(nGiorni), java.math.BigDecimal.ROUND_HALF_UP);
                    if (kmGiornalieri.compareTo(new java.math.BigDecimal(0)) < 1)
                        throw new it.cnr.jada.comp.ApplicationException("Il numero giornaliero dei chilometri e' nullo!");

                    spesa.setChilometri(kmGiornalieri);
                }
            }

            //	Calcolo gli importi della spesa (importi auto, trasporto...)
            //	Se necessario converto tali importi
            // 	Verifico che gli importi rispettino i massimali (spesa, pasto..)
            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            missione = component.validaMassimaliSpesa(context.getUserContext(), missione, spesa);

            /***** ripristino la spesa essendo stata modificata dalla comonent ***/
            setModel(context, missione);
            spesa = (Missione_dettaglioBulk) getSpesaController().getModel();
            /*********************************************************************/

            spesa.setSpesaIniziale(null);
            spesa.setStatus(Missione_dettaglioBulk.STATUS_CONFIRMED);
            if (spesa.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL)
                spesa.setToBeUpdated();

            //	Se l'utente ha selezionato piu' giorni imputo la spesa
            //	inserita del primo giorno anche	agli altri giorni selezionati
            if (missione.isSelezioneGiorniSpesaMultipla())
                missione.imputaSpesaSuPiuGiorni(spesa);

            // Fine modalita' modifica/inserimento dettaglio spesa
            getSpesaController().setEditingSpesa(false);

            missione.setSpeseInserite(false);

            // Ripulisco la selezione dei giorni
            missione.setCollectionGiorniSpeseSelezionati(null);
        } catch (Throwable e) {
            spesa.setChilometri(kmTot);
            spesa.setIm_spesa_divisa(importoSpesaTot);
            spesa.setIm_base_maggiorazione(baseMaggTot);

            setModel(context, missione);

            throw handleException(e);
        }
    }

    /**
     * Il metodo gestisce la conferma dell'inserimento/modifica di una tappa.
     * Vengono controllati i dati della tappa, vengono completati i searchTool e nel caso di comune
     * proprio o altro vengono inizializzati nazione e divisa con i valori di default
     */

    public void confermaTappa(ActionContext context) throws BusinessProcessException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException, javax.ejb.EJBException, it.cnr.jada.bulk.ValidationException, it.cnr.jada.persistency.PersistencyException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();

            // Resetto l'inizio e fine di tutte le tappe
            missione.resettaInizioFineTappe();

            tappa = completaSearchToolTappa(context, tappa);

            tappa.validaTappa();

            // Se ho selezionato comune proprio o comune altro la divisa deve essere
            // quella di default (EURO)
            String cd_euro = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), new Integer(0), "*", "CD_DIVISA", "EURO");
            if ((!tappa.getFl_comune_estero().booleanValue()) && (!tappa.getCd_divisa_tappa().equals(cd_euro)))
                throw new it.cnr.jada.bulk.ValidationException("La divisa non e' valida !");

            tappa.setTappaIniziale(null);
            tappa.setStatus(Missione_tappaBulk.STATUS_CONFIRMED);

            // Fine modalita' modifica/inserimento tappa
            editingTappa = false;
        } catch (it.cnr.jada.bulk.ValidationException e) {
            throw new MessageToUser(e.getMessage());
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
            setModel(context,
                    ((MissioneComponentSession) createComponentSession()).creaConBulk(context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
            setUserConfirm(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo aggiunge alla normale toolbar del CRUD i bottoni di "riporto avanti/indietro".
     */
    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = super.createToolbar();
        Button[] newToolbar = new Button[toolbar.length + 3];
        int i;
        for (i = 0; i < toolbar.length; i++)
            newToolbar[i] = toolbar[i];
        newToolbar[i] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaAvanti");
        newToolbar[i + 1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaIndietro");
        newToolbar[i + 2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.salvaProvvisorio");
        newToolbar = IDocAmmEconomicaBP.addPartitario(newToolbar, attivaEconomicaParallela, isEditing(), getModel());
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
     * Tale metodo predispone la spesa per un'eventuale modifica.
     * La modifica puo' avvenire solo se non ho ancora inserito la diaria.
     */

    public void editaSpesa(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) getSpesaController().getModel();

        spesa.setSpesaIniziale((Missione_dettaglioBulk) spesa.clone());

        //	Nella lista dei giorni seleziono il giorno della spesa
        //	che si sta per modificare
        if (missione.getCollectionGiorniSpeseSelezionati() == null)
            missione.setCollectionGiorniSpeseSelezionati(new java.util.Vector());
        missione.getCollectionGiorniSpeseSelezionati().add(spesa.getDt_inizio_tappa());

        // Inizio modalita' modifica dettaglio spesa
        getSpesaController().setEditingSpesa(true);

        // Dovrebbe essere in STATUS_CONFIRMED
    }

    /**
     * tale metodo verifica se la configurazione delle tappe puo' essere modificat.
     * In caso positivo predispone la tappa per la modifica.
     */

    public void editaTappa(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.comp.ApplicationException {
        MissioneBulk missione = (MissioneBulk) getModel();

        // Verifico se posso modificare una tappa
        missione.isConfigurazioneTappeModificabile();
        // Se non ho spese ma ho la diaria quest'ultima dovra' essere cancellata per poter proseguire
        if ((missione.getDiariaMissioneColl() != null) && (!missione.getDiariaMissioneColl().isEmpty()))
            missione = cancellaDiariaPerModificaConfigurazioneTappe(context);

        //Se ho il rimborso lo cancello
        if ((missione.getRimborsoMissioneColl() != null) && (!missione.getRimborsoMissioneColl().isEmpty()))
            missione = cancellaRimborsoPerModificaConfigurazioneTappe(context);

        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();

        tappa.setTappaIniziale((Missione_tappaBulk) tappa.clone());

        // Inizio modalita' modifica tappa
        editingTappa = true;

        // Dovrebbe essere in STATUS_CONFIRMED
    }

    /**
     * Tale metodo gestisce la ricerca degli inquadramenti sempre che sia gia' stato selezionato un Tipo
     * Rapporto
     */

    public void findInquadramenti(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            missione.setRif_inquadramento(null);
            missione.setInquadramenti(null);

            if (missione.getTipo_rapporto() != null) {
                MissioneComponentSession component = (MissioneComponentSession) createComponentSession();
                java.util.Collection coll = component.findInquadramenti(context.getUserContext(), missione);

                if (coll == null || coll.isEmpty())
                    setMessage("Inquadramenti non disponibili !");
                else
                    missione.setInquadramenti(coll);
            }
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public boolean isDiariaEditable(UserContext context) throws BusinessProcessException, RemoteException {
        try {
            Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();

            //if (tappa.getFl_comune_estero().booleanValue())
            //   return true;
            if (tappa.getDt_inizio_tappa() != null) {
                MissioneComponentSession component = (MissioneComponentSession) createComponentSession();
                if (component.isDiariaEditable(context, tappa))
                    return true;
                else
                    return false;
            }
            return false;
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        }
    }

    public boolean isRimborsoEditable(UserContext context) throws BusinessProcessException, RemoteException {
        try {
            Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();
            java.sql.Timestamp data_inizio_rimborso_miss_estero;
            java.sql.Timestamp data_fine_rimborso_miss_estero;

            java.util.GregorianCalendar gci = getGregorianCalendar();
            java.util.GregorianCalendar gcf = getGregorianCalendar();
            gci.setTime(tappa.getMissione().getDt_inizio_missione());
            gcf.setTime(tappa.getMissione().getDt_fine_missione());
            gci.add(java.util.Calendar.DAY_OF_YEAR, +1);

            if (isRimborsoVisible(context)) {
                if (tappa.getDt_inizio_tappa() != null) {
                    Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
                    if (sess.getDt01(context, new Integer(0), "*", "RIMBORSO_MISS_ESTERO", "PERIODO_VALIDITA") == null ||
                            sess.getDt02(context, new Integer(0), "*", "RIMBORSO_MISS_ESTERO", "PERIODO_VALIDITA") == null)
                        throw new ApplicationException("Configurazione CNR: non è stato impostato il periodo di validità per i rimborsi esteri (RIMBORSO_MISS_ESTERO - PERIODO_VALIDITA)");
                    data_inizio_rimborso_miss_estero = sess.getDt01(context, new Integer(0), "*", "RIMBORSO_MISS_ESTERO", "PERIODO_VALIDITA");
                    data_fine_rimborso_miss_estero = sess.getDt02(context, new Integer(0), "*", "RIMBORSO_MISS_ESTERO", "PERIODO_VALIDITA");

                    //solo se è estera, non c'è diaria, la missione dura più di 24 ore e la tappa è compresa nel periodo di validità
                    if (tappa.getFl_comune_estero().booleanValue() &&
                            tappa.getFl_no_diaria().booleanValue() &&
                            gcf.compareTo(gci) >= 0 &&
                            !(tappa.getDt_inizio_tappa().compareTo(data_inizio_rimborso_miss_estero) < 0) &&
                            tappa.getDt_inizio_tappa().compareTo(data_fine_rimborso_miss_estero) < 0) {
                        return true;
                    } else {
                        return (false);
                    }
                }
                return false;
            }
            return false;
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        }
    }

    public boolean isRimborsoVisible(UserContext context) throws BusinessProcessException, RemoteException {
        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();
        if (tappa != null && tappa.getFl_comune_estero() != null) {
            //solo se è estera è visibile
            if (tappa.getFl_comune_estero().booleanValue() && tappa.getFl_no_diaria().booleanValue())
                return true;
            return false;
        } else {
            return (false);
        }

    }

    /**
     * Tale metodo gestisce la ricerca degli Inquadramenti e dei Tipi Trattamento sempre che sia gia' stato selezionato
     * un Tipo Rapporto
     */

    public void findInquadramentiETipiTrattamento(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            missione.setRif_inquadramento(null);
            missione.setInquadramenti(null);
            missione.setTipo_trattamento(null);
            missione.setTipi_trattamento(null);

            if (missione.getTipo_rapporto() != null) {
                MissioneComponentSession component = (MissioneComponentSession) createComponentSession();
                missione = component.findInquadramentiETipiTrattamento(context.getUserContext(), missione);

                if ((missione.getInquadramenti() == null) || (missione.getInquadramenti().isEmpty()))
                    setMessage("Impossibile proseguire. Inquadramenti non disponibili !");
                if ((missione.getTipi_trattamento() == null) || (missione.getTipi_trattamento().isEmpty()))
                    setMessage("Tipi Trattamento non disponibili !");

                setModel(context, missione);
            }
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Effettua una operazione di ricerca delle obbligazioni
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param filtro      modello che fa da contesto alla ricerca (il modello del FormController padre del
     *                    controller che ha scatenato la ricerca)
     * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
     **/
    public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MissioneComponentSession sess = (MissioneComponentSession) createComponentSession();
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
     **/
    public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
        try {
            MissioneComponentSession sess = (MissioneComponentSession) createComponentSession();
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
     * Tale metodo gestisce la ricerca degi Tipi Rapporto
     */

    public void findTipiRapporto(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            missione.setTipi_rapporto(null);
            missione.setTipo_rapporto(null);

            MissioneComponentSession component = (MissioneComponentSession) createComponentSession();
            java.util.Collection coll = component.findTipi_rapporto(context.getUserContext(), missione);

            if (coll == null || coll.isEmpty())
                setMessage("Tipi Rapporto non disponibili !");
            else
                missione.setTipi_rapporto(coll);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Tale metodo gestisce la ricerca degli Tipi Trattamento sempre che sia gia' stato selezionato un Tipo
     * Rapporto
     */

    public void findTipiTrattamento(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            missione.setTipo_trattamento(null);
            missione.setTipi_trattamento(null);

            if (missione.getTipo_rapporto() != null) {
                MissioneComponentSession component = (MissioneComponentSession) createComponentSession();
                java.util.Collection coll = component.findTipi_trattamento(context.getUserContext(), missione);

                if (coll == null || coll.isEmpty())
                    setMessage("Tipi Trattamento non disponibili !");
                else
                    missione.setTipi_trattamento(coll);
            }
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Il metodo gestisce la selezione del bottone "fine configurazione tappe", in particolare modo :
     * - la creazione delle tappe che l'utente non ha configurato facendole ereditare dalla precedente,
     * - inizializza correttamente la data/ora di inizio/fine tappa di ogni tappa
     *
     * @throws ParseException
     */

    public void fineConfigurazioneTappa(ActionContext context) throws it.cnr.jada.bulk.ValidationException, ParseException, BusinessProcessException {
        try {
            java.sql.Timestamp data_fine_diaria_miss_estero;
            Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
            data_fine_diaria_miss_estero = sess.getDt01(context.getUserContext(), new Integer(0), "*", "DIARIA_MISS_ESTERO", "DATA_FINE");

            MissioneBulk missione = (MissioneBulk) getModel();

            missione.resettaInizioFineTappe();
            missione.generaTappeNonConfigurate(data_fine_diaria_miss_estero);
            missione.setInizioFineTappe(data_fine_diaria_miss_estero);
            missione.setTappeConfigurate(true);
        } catch (ComponentException e1) {
            throw handleException(e1);
        } catch (RemoteException e1) {
            throw handleException(e1);
        }
    }

    /**
     * Il metodo gestisce la generazione dei dettagli di diaria.
     * Confermate le tappe e le spese si salva temporaneamente la missione e si	genera la diaria
     */

    public MissioneBulk generaDiaria(ActionContext context, MissioneBulk missione) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException, it.cnr.jada.bulk.ValidationException {
        missione.setSalvataggioTemporaneo(true);

        // 	Salvo la missione
        try {
            salvaTemporaneamente(context);
            missione = (MissioneBulk) getModel();
        } finally {
            missione.setSalvataggioTemporaneo(false);
            missione.setSpeseInserite(false);
        }

        // 	Genero la diaria
        try {
            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            return (component.generaDiaria(context.getUserContext(), missione));
        } catch (it.cnr.jada.comp.ComponentException e) {
            ricaricaMissioneInModifica(context);
            missione = (MissioneBulk) getModel();
            missione.setSpeseInserite(false);
            throw handleException(e);
        }
    }

    public MissioneBulk generaRimborso(ActionContext context, MissioneBulk missione) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException, it.cnr.jada.bulk.ValidationException {
        try {
            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            return (component.generaRimborso(context.getUserContext(), missione));
        } catch (it.cnr.jada.comp.ComponentException e) {
            ricaricaMissioneInModifica(context);
            missione = (MissioneBulk) getModel();
            missione.setSpeseInserite(false);
            throw handleException(e);
        }
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     */

    public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {

        return null;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     * Tale metodo ritorna la missione
     */
    public IDocumentoAmministrativoBulk getBringBackDocAmm() {
        return getDocumentoAmministrativoCorrente();
    }

    /**
     * Il  metodo restituisce il Controller del "Consuntivo"
     */

    public final it.cnr.jada.util.action.SimpleDetailCRUDController getConsuntivoController() {
        return consuntivoController;
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldiBP
     * Tale metodo ritorna la missione
     */

    public it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
        return (it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi) getModel();
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferedUpdateSaldiBP
     * Tale metodo ritorna il BP corrente
     */

    public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {
        return (IDefferedUpdateSaldiBP) this;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     */
    public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {
        return null;
    }

    /**
     * Tale metodo ritorna il Controller della "Diaria"
     */

    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDiariaController() {
        return diariaController;
    }

    /**
     * Tale metodo ritorna il Controller del "Rimborso"
     */

    public final it.cnr.jada.util.action.SimpleDetailCRUDController getRimborsoController() {
        return rimborsoController;
    }

    /**
     * Metodo richiesto dall' interfaccia IValidaDocContBP
     */

    public it.cnr.jada.bulk.OggettoBulk getDocAmmModel() {
        return getModel();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     * Tale metodo ritorna la missione
     */
    public IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {

        return (IDocumentoAmministrativoBulk) getModel();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP
     * Tale metodo ritorna la scadenza associata alla missione
     */

    public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
        return ((MissioneBulk) getModel()).getObbligazione_scadenzario();
    }

    /**
     * Tale metodo ritorna il Controller della "Spesa"
     */
    public final CRUDMissione_spesaController getSpesaController() {
        return spesaController;
    }

    /**
     * Tale metodo ritorna il Controller della "Tappa"
     */
    public final SimpleDetailCRUDController getTappaController() {
        return tappaController;
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
     * creare missioni
     */

    protected void init(Config config, ActionContext context) throws BusinessProcessException {
        try {
            attivaEconomicaParallela = Utility.createConfigurazioneCnrComponentSession().isAttivaEconomicaParallela(context.getUserContext());
            verificoUnitaENTE(context);
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
        MissioneBulk missione = (MissioneBulk) super.initializeModelForEdit(context, bulk);
        missione.setDataInizioObbligoRegistroUnico(getDataInizioObbligoRegistroUnico(context));
        return initializePerRiporta(context, missione);
    }

    /**
     * Il metodo inizializza il BP per la ricerca libera  di un documento
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */
    public it.cnr.jada.bulk.OggettoBulk initializeModelForFreeSearch(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) super.initializeModelForFreeSearch(context, bulk);

        return initializePerRiporta(context, missione);
    }

    /**
     * Il metodo inizializza il BP per la creazione di un documento
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */
    public it.cnr.jada.bulk.OggettoBulk initializeModelForInsert(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) super.initializeModelForInsert(context, bulk);
        missione.setDataInizioObbligoRegistroUnico(getDataInizioObbligoRegistroUnico(context));
        return initializePerRiporta(context, missione);
    }

    /**
     * Il metodo inizializza il BP per la ricerca di un documento
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili
     */
    public it.cnr.jada.bulk.OggettoBulk initializeModelForSearch(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) super.initializeModelForSearch(context, bulk);

        return initializePerRiporta(context, missione);
    }

    /**
     * Il metodo inizializza degli attributi del BP utili per la gestione del riporta dei documenti
     * contabili :
     * - annoSolareInScrivania --> TRUE se l'anno solare coincide con quello di scrivania
     * - riportaAvantiIndietro --> TRUE se esercizio di scrivania e quello successivo sono aperti
     * Il metodo inizializza anche degli attributi della missione :
     * - annoSolare
     * - esercizioScrivania
     */
    public it.cnr.jada.bulk.OggettoBulk initializePerRiporta(ActionContext context, MissioneBulk missione) throws BusinessProcessException {
        try {
            java.sql.Timestamp tsOdierno = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            java.util.GregorianCalendar tsOdiernoGregorian = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
            tsOdiernoGregorian.setTime(tsOdierno);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            missione.setAnnoSolare(tsOdiernoGregorian.get(java.util.GregorianCalendar.YEAR));
            missione.setEsercizioScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue());


            setAnnoSolareInScrivania(missione.getAnnoSolare() == missione.getEsercizioScrivania());

            setRibaltato(initRibaltato(context));

            if (!isAnnoSolareInScrivania()) {
                String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
                try {
                    DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession) createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);

                    boolean esercizioScrivaniaAperto = session.verificaStatoEsercizio(context.getUserContext(), new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(cds, new Integer(missione.getEsercizioScrivania())));
                    boolean esercizioSuccessivoAperto = session.verificaStatoEsercizio(context.getUserContext(), new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(cds, new Integer(missione.getEsercizioScrivania() + 1)));
                    setRiportaAvantiIndietro(esercizioScrivaniaAperto && esercizioSuccessivoAperto && isRibaltato());
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
        return missione;
    }

    /**
     * Il metodo inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri
     */
    protected void initializePrintBP(it.cnr.jada.util.action.AbstractPrintBP bp) {
        it.cnr.contab.reports.bp.OfflineReportPrintBP printbp = (it.cnr.contab.reports.bp.OfflineReportPrintBP) bp;
        MissioneBulk missione = (MissioneBulk) getModel();
        printbp.setReportName("/docamm/docamm/vpg_missione.jasper");
        Print_spooler_paramBulk param;

        param = new Print_spooler_paramBulk();
        param.setNomeParam("aCd_cds");
        param.setValoreParam(missione.getCd_cds());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("aCd_uo");
        param.setValoreParam(missione.getCd_unita_organizzativa());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("aEs");
        param.setValoreParam(missione.getEsercizio().toString());
        param.setParamType("java.lang.Integer");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("aPg_da");
        param.setValoreParam(missione.getPg_missione().toString());
        param.setParamType("java.lang.Long");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("aPg_a");
        param.setValoreParam(missione.getPg_missione().toString());
        param.setParamType("java.lang.Long");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("aCd_terzo");
        param.setValoreParam(missione.getCd_terzo().toString());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);
    }

    /**
     * Il metodo inizializza la divisa e il cambio della spesa con quelli di default (EURO)
     */

    public MissioneBulk inizializzaDivisaCambioPerRimborsoKm(ActionContext context, Missione_dettaglioBulk aSpesa) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException, it.cnr.jada.bulk.ValidationException {
        MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
        return (component.inizializzaDivisaCambioPerRimborsoKm(context.getUserContext(), aSpesa));
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
     * Il metodo determina se il bottone di aggiornamento manuale della obbligazione è abilitato o meno.
     * Il metodo disabilita i bottoni della scadenza della missione se l'associazione con la scadenza non
     * e' obbligaztoria, cioe' se :
     * - se la missione + compenso
     * - se la missione ha un anticipo associato con un importo maggiore di quello della missione stessa
     * Se la missione non e' modificabile, il bottone "Aggiorna manuale" deve essere abilitato e deve aprire
     * l'obbligazione in visualizzazione
     */

    public boolean isBottoneObbligazioneAggiornaManualeAbilitato() {
        MissioneBulk missione = (MissioneBulk) getModel();

        return (missione != null && !isSearching() && missione.isObbligazioneObbligatoria());
    }

    /**
     * Il metodo determina se il campo "Cambio" della tappa e' abilitato o meno.
     * Il campo e' abilitato solo se la tappa e' estera con valuta estera
     */

    public boolean isCambioTappaReadOnly() {
        if (!isEditingTappa())
            return true;

        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();

        if (tappa != null &&
                tappa.getFl_comune_estero() != null &&
                !tappa.getFl_comune_estero().booleanValue())
            return true;

        if (tappa != null &&
                tappa.getFl_comune_estero() != null &&
                tappa.getFl_comune_estero().booleanValue() &&
                tappa.getCd_divisa_tappa() != null && tappa.getCd_divisa_tappa().compareTo(it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_EURO) == 0)
            return true;

        return false;

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
     * Il metodo stabilisce se abilitare o meno il bottone di conferma del dettaglio di spesa.
     * Il metodo abilita :
     * - solo la spesa e' in fase di modifica/inserimento
     * - se la missione non e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare l' obbligazione non deve essere stata riportata
     * (isEditable)
     */

    public boolean isConfermaSpesaButtonEnabled() throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        return (getSpesaController().getModel() != null && getSpesaController().isEditingSpesa() &&
                missione != null && missione.isEditable());
    }

    /**
     * Il metodo stabilisce se abilitare o meno il bottone di conferma della tappa.
     * Il metodo abilita :
     * - solo la tappa e' in fase di modifica/inserimento
     * - se la missione non e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare l' obbligazione non deve essere stata riportata
     * (isEditable)
     */

    public boolean isConfermaTappaButtonEnabled() throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        return (getTappaController().getModel() != null && isEditingTappa() &&
                missione != null && missione.isEditable());
    }

    /**
     * Il metodo determina se abilitare o meno il campo "data ingresso/uscita".
     * Il metodo abilita solo se sono all'estero
     */

    public boolean isDataIngressoAbilitata() {
        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();
        boolean flg = false;
        if ((tappa != null) && (tappa.getFl_comune_estero() != null) && (tappa.getFl_comune_estero().booleanValue()))
            flg = true;

        return !(flg && isEditingTappa());
    }

    /**
     * Il metodo determina se abilitare o meno il bottone di cancellazione della missione.
     * Le condizioni generali per l'abilitazione sono che :
     * - la missione non sia pagata
     * - non ho alcuna tappa ne dettaglio di spesa in fase di modifica/inserimento
     * Inoltre se l'esercizio della missione è diverso dal quello solare :
     * - se l'esercizio di scrivania è uguale da quello solare il bottone è abilitato se la scadenza è riportata
     * - se l'esercizio di scrivania è diverso da quello solare il bottone è abilitato se la scadenza non è riportata
     */

    public boolean isDeleteButtonEnabled() {
        MissioneBulk missione = (MissioneBulk) getModel();

        if (missione == null)
            return super.isDeleteButtonEnabled();

        boolean flg = super.isDeleteButtonEnabled() &&
                !isEditingTappa() &&
                !getSpesaController().isEditingSpesa() &&
                !missione.isPagata();

        //	Chiusura : se carico una missione con esercizio precedente a quello solare :
        //	- esercizio scrivania != anno solare e obbligazione riportata --> disabilito
        //	- esercizio scrivania != anno solare e obbligazione non riportata --> abilito
        //	- esercizio scrivania = anno solare e obbligazione riportata --> abilito
        //	- esercizio scrivania = anno solare e obbligazione non riportata --> disabilito
        //if(flg && missione.getEsercizio().intValue()!=missione.getAnnoSolare())
        //{
        //if(missione.getEsercizioScrivania()==missione.getAnnoSolare())
        //return missione.isRiportata();
        //else
        //return !missione.isRiportata();
        //}


        // Gennaro Borriello - (05/11/2004 12.23.28)
        // Modif. relativa alla nuova gestione di isRiportata()
        if (flg) {
            if (missione.getEsercizio().intValue() == missione.getEsercizioScrivania())
                return !missione.isRiportata();
            else
                return missione.isRiportataInScrivania();
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
     * Tale metodo ritorna TRUE se la tappa e' in fase di modifica/inserimento
     * cioe' se e' stato selezionato il bottone per editare la tappa
     */

    public boolean isEditingTappa() {
        return editingTappa;
    }

    /**
     * Tale metodo gestisce l'abilitazione o meno del bottone di edit della spesa.
     * Il metodo abilita solo se :
     * - il dettaglio di spesa  non e' gia' in fase di modifica
     * - se la missione non e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare l' obbligazione non deve essere stata riportata
     * (isEditable)
     */

    public boolean isEditSpesaButtonEnabled() throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        return (getSpesaController().getModel() != null && !getSpesaController().isEditingSpesa() &&
                missione != null && missione.isEditable() && !isViewing());
    }

    /**
     * Tale metodo gestisce l'abilitazione o meno del bottone di edit della tappa.
     * Il metodo abilita solo se :
     * - la tappa non e' gia' in fase di modifica
     * - se la missione non e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare l' obbligazione non deve essere stata riportata
     * (isEditable)
     */
    public boolean isEditTappaButtonEnabled() throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        return (getTappaController().getModel() != null && !isEditingTappa() &&
                missione != null && missione.isEditable() && !isViewing());
    }

    /**
     * Il  metodo disabilito la ricerca guidata in caso entrassi dal Fondo Economale
     */

    public boolean isFreeSearchButtonHidden() {
        return super.isFreeSearchButtonHidden() || isSpesaBP();
    }

    /**
     * Il metodo è stato reimplementato perche' la missione risulta readonly anche :
     * - se la missione e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare e l' obbligazione è stata riportata
     * (isEditable)
     */
    public boolean isInputReadonly() {
        MissioneBulk missione = (MissioneBulk) getModel();
        if (missione == null)
            return super.isInputReadonly();
        return super.isInputReadonly() || !missione.isEditable();
    }

    /**
     * Il metodo è stato sovrascritto per consentire all'utente di modificare lo stato della liquidazione
     * quando il documento non risulta essere modificabile
     */
    public void writeFormInput(javax.servlet.jsp.JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3) throws java.io.IOException {
        MissioneBulk missione = null;
        if (getModel() != null)
            missione = (MissioneBulk) getModel();
        if (missione != null &&
                missione.isRiportataInScrivania() &&
                !missione.isPagata() &&
                isInputReadonly() &&
                s1.equals("stato_liquidazione")) {
            getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, "onChange=\"submitForm('doSelezionaStatoLiquidazione')\"", getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
        } else
            super.writeFormInput(jspwriter, s, s1, flag, s2, s3);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     */
    public boolean isManualModify() {
        return true;
    }

    /**
     * Il metodo abilita il campo nazione della tappa solo se la tappa e' estera
     */

    public boolean isNazioneReadOnly() {
        if (!isEditingTappa())
            return true;

        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();
        if (tappa != null && tappa.getFl_comune_estero() != null && !tappa.getFl_comune_estero().booleanValue())
            return true;

        return false;
    }

    /**
     * Il  metodo abilita il bottone nuovo solo se non ho alcuna tappa ne dettaglio di spesa in fase
     * di modifica/inserimento
     */

    public boolean isNewButtonEnabled() {
        MissioneBulk missione = (MissioneBulk) getModel();

        if (missione == null)
            return super.isNewButtonEnabled();

        return super.isNewButtonEnabled() &&
                !isEditingTappa() &&
                !getSpesaController().isEditingSpesa();
    }

    /**
     * Il metodo nasconde il bottone di stampa se la missione e' stata salvata in modo temporaneo
     */
    public boolean isPrintButtonHidden() {
        MissioneBulk missione = (MissioneBulk) getModel();
        if (missione == null)
            return super.isPrintButtonHidden();

        return (super.isPrintButtonHidden() ||
                !(missione.getPg_missione() != null && missione.getPg_missione().compareTo(new Long(0)) > 0
                        && missione.getCd_cds() != null && missione.getCd_unita_organizzativa() != null
                        && missione.getCd_terzo() != null && missione.getEsercizio() != null));
    }

    /**
     * Il metodo stabilisce se il bottone di salva provvisorio debba essere
     * abilitato o meno.
     * Il bottone è abilitato se ho appena riportato indietro il documento o se la scadenza non risulta riportata.
     */
    public boolean isSalvaProvvisorioButtonEnabled() {
        MissioneBulk missione = (MissioneBulk) getModel();
        return !missione.isMissioneDefinitiva() && (missione.isCompensoObbligatorio() || (missione.isObbligazioneObbligatoria() && !missione.isMissioneConObbligazione()));

    }

    /**
     * Il metodo stabilisce se il bottone di riporta avanti debba essere visualizzato
     */
    public boolean isSalvaProvvisorioButtonHidden() {
        return !isSalvaProvvisorioButtonEnabled();
    }

    /**
     * Il metodo stabilisce se il bottone di riporto avanti del documento contabile debba essere
     * abilitato o meno.
     * Il bottone è abilitato se ho appena riportato indietro il documento o se la scadenza non risulta riportata.
     */
    public boolean isRiportaAvantiButtonEnabled() {
        MissioneBulk missione = (MissioneBulk) getModel();
        return isCarryingThrough() || !missione.isRiportata();
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
     * Il bottone è abilitato se non ho modificato la missione e se non l'ho appena riportato indietro
     */

    public boolean isRiportaIndietroButtonEnabled() {
        MissioneBulk missione = (MissioneBulk) getModel();

        return isEditing() && !isDeleting() && !isDirty() && !isCarryingThrough();
    }

    /**
     * Il metodo stabilisce se il bottone di riporta indietro del documento contabile debba essere visualizzato
     */
    public boolean isRiportaIndietroButtonHidden() {
        MissioneBulk missione = (MissioneBulk) getModel();

        return basicRiportaButtonHidden() || (missione != null && !(missione.isRiportata() || isCarryingThrough()));
    }

    /**
     * Il metodo stabilisce se il bottone di salvataggio della missione debba essere abilitato.
     * E' abilitato :
     * - se la missione non e' pagata (isEditable)
     * - se l'esercizio di scrivania e' diverso da quello solare, l' obbligazione non deve essere stata riportata
     * (isEditable)
     * - non devo essere in modalità modifica/inserimento di una spesa/tappa
     */
    public boolean isSaveButtonEnabled() {
        MissioneBulk missione = (MissioneBulk) getModel();

        if (missione == null)
            return super.isSaveButtonEnabled();

        return super.isSaveButtonEnabled() &&
                (missione.isEditable() || carryingThrough
                        // Consentire salvataggio
                        || (!missione.isEditable() && missione.getCrudStatus() != 5))
                &&
                !isEditingTappa() &&
                !getSpesaController().isEditingSpesa();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     * Il metodo ritorna TRUE se la missione e' stata aperta da Fondo Economale
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
     * Il metodo abilito il bottone di Undo del dettaglio di spesa solo se questo e' in fase di modifica/inserimento
     */

    public boolean isUndoSpesaButtonEnabled() throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        return getSpesaController().getModel() != null && getSpesaController().isEditingSpesa() &&
                missione != null && missione.isEditable();
    }

    /**
     * Il  metodo abilito il bottone di Undo della tappa solo se questa e' in fase di modifica/inserimento
     */

    public boolean isUndoTappaButtonEnabled() throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        return getTappaController().getModel() != null && isEditingTappa() &&
                missione != null && missione.isEditable();
    }

    /**
     * Il metodo gestisce la cancellazione delle tappe.
     * Il metodo effettua gli opportuni controlli per poter cancellare una tappa
     * La cancellazione delle tappe implica la cancellazione della diaria.
     * Se l'utente cancella una tappa occorre reinizializzare le date di inizio/fine di tutte le tappe
     * per forzare una nuova riconfigurazione delle tappe
     */

    public void removeTappa(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException, it.cnr.jada.comp.ApplicationException {
        MissioneBulk missione = (MissioneBulk) getModel();

        // Verifico se posso cancellare una tappa
        missione.isConfigurazioneTappeModificabile();
        // Se non ho spese ma ho la diaria quest'ultima dovra' essere cancellata per poter proseguire
        if ((missione.getDiariaMissioneColl() != null) && (!missione.getDiariaMissioneColl().isEmpty()))
            missione = cancellaDiariaPerModificaConfigurazioneTappe(context);

        //Se ho il rimborso lo cancello
        if ((missione.getRimborsoMissioneColl() != null) && (!missione.getRimborsoMissioneColl().isEmpty()))
            missione = cancellaRimborsoPerModificaConfigurazioneTappe(context);

        // Chiama il metodo 'removeFromTappeMissioneColl'
        getTappaController().remove(context);
        missione.setTappeConfigurate(false);

        // Reinizializzo le date inizio/fine di tutte le tappe
        missione.resettaInizioFineTappe();
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
     * Il metodo ricarica una missione in modifica
     */

    public void ricaricaMissioneInModifica(ActionContext context) throws BusinessProcessException {
        try {
            basicEdit(context, getModel(), true);
        } catch (BusinessProcessException e) {
            setModel(context, null);
            setDirty(false);
            throw e;
        }
    }

    /**
     * Il metodo gestisce il 'riporto avanti' del documento contabile della missione
     */
    public void riportaAvanti(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException {
        try {
            RiportoDocAmmComponentSession session = (RiportoDocAmmComponentSession) createComponentSession("CNRDOCAMM00_EJB_RiportoDocAmmComponentSession", RiportoDocAmmComponentSession.class);

            MissioneBulk missioneRiportata = (MissioneBulk) session.riportaAvanti(context.getUserContext(),
                    (IDocumentoAmministrativoBulk) getModel(),
                    getUserConfirm());
            setModel(context, missioneRiportata);
        } catch (Exception e) {
            throw handleException(e);
        } finally {
            setUserConfirm(null);
        }
    }

    /**
     * Il metodo gestisce il 'riporto indietro' del documento contabile della missione
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
            MissioneBulk missioneRiportata = (MissioneBulk) session.riportaIndietro(context.getUserContext(),
                    (IDocumentoAmministrativoBulk) getModel());
            basicEdit(context, missioneRiportata, true);
            setDirty(true);
        } catch (Throwable t) {
            setCarryingThrough(false);
            rollbackUserTransaction();
            throw handleException(t);
        }
    }

    /**
     * Il metodo gestisce il ripristino della riselezione del Tipo Trattamento
     */

    public void ripristinaSelezioneTipoTrattamento(it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipoTrattamento) {
        MissioneBulk missione = (MissioneBulk) getModel();

        // ripristino la selezione del Tipo Trattamento
        if (tipoTrattamento != null) {
            for (java.util.Iterator i = missione.getTipi_trattamento().iterator(); i.hasNext(); ) {
                it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk aTrattamento = (it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk) i.next();
                if (aTrattamento.getCd_trattamento().equals(tipoTrattamento.getCd_trattamento()))
                    missione.setTipo_trattamento(aTrattamento);
            }
        }
    }

    /**
     * Annulla le modifiche apportate alla missione e ritorna al savepoint impostato in precedenza
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Rollback to savePoint
     * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata
     * Post: Tutte le modifiche effettuate sulla missione sino al savepoint impostato vengono annullate
     *
     * @param    context            il Context che ha generato la richiesta
     * @param    savePointName    il nome del savePoint
     */
    public void rollbackToSavePoint(ActionContext context, String savePointName) throws BusinessProcessException {
        try {
            MissioneComponentSession sess = (MissioneComponentSession) createComponentSession();
            sess.rollbackToSavePoint(context.getUserContext(), savePointName);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo effettua un salvataggio temporaneo della missione e ricarica la stessa in modifica
     */
    public void salvaTemporaneamente(ActionContext context) throws it.cnr.jada.bulk.ValidationException, BusinessProcessException {
        completeSearchTools(context, this);
        validate(context);

        MissioneBulk missione = (MissioneBulk) getModel();
        if (missione.getPg_missione() == null)
            create(context);
        else
            update(context);

        // Ricarico la missione
        ricaricaMissioneInModifica(context);
    }

    /**
     * Il metodo gestisce la selezione delle spese relative al giorno selezionato nel Tab
     * dei consuntivi
     */

    public void selezionaDettagliConsuntivo(ActionContext context) {
        MissioneBulk missione = (MissioneBulk) getModel();
        java.util.Vector speseDaSelezionare = new java.util.Vector();

        // Elimino selezioni dalla table del consuntivo
        getConsuntivoController().reset(context);

        if ((missione.getSpeseMissioneColl() == null) || (missione.getSpeseMissioneColl().size() == 0) ||
                (missione.getGiorno_consuntivo() == null)) {
            missione.inizializzaGiornoConsuntivo();    // azzero i consuntivi del giorno
            return;
        }

        // Cerco le spese del giorno selezionato nel Tab del consuntivo
        for (java.util.Iterator i = missione.getSpeseMissioneColl().iterator(); i.hasNext(); ) {
            Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) i.next();
            if (spesa.getDt_inizio_tappa().equals(missione.getGiorno_consuntivo()))
                speseDaSelezionare.add(spesa);
        }

        // Seleziono, se esistono, le spese del giorno selezionato
        if ((speseDaSelezionare != null) && (speseDaSelezionare.size() > 0))
            getConsuntivoController().setSelection(speseDaSelezionare.elements());
    }

    /**
     * Il metodo gestisce l'inizializzazione del cambio della spesa nel caso la divisa selezionata
     * risulti uguale a quella inserita nella configurazione della tappa dello stesso giorno.
     * In un caso simile l'applicazione propone di default il cambio di tale tappa, altrimenti propone
     * il cambio letto da tabella.
     * (Se l'utente ha selezionato 'n' giorni l'applicazione cerca il cambio valido il primo giorno
     * di quelli selezionati)
     */

    public void setCambioSpesaDefault(ActionContext context, Missione_dettaglioBulk spesa) throws java.sql.SQLException, java.rmi.RemoteException, BusinessProcessException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        MissioneBulk missione = spesa.getMissione();

        java.sql.Timestamp primoGiorno = missione.getPrimoGiornoSpesaSelezionato();
        if (primoGiorno == null)
            return;

        // Propongo quella della tappa
        Missione_tappaBulk tappa = (Missione_tappaBulk) missione.getTappeMissioneHash().get(primoGiorno);
        if (tappa == null)
            return;
        if ((tappa.getCd_divisa_tappa() != null) && (tappa.getCd_divisa_tappa().equals(spesa.getCd_divisa_spesa()))) {
            spesa.setCambio_spesa(tappa.getCambio_tappa());
            return;
        }

        // Propongo quella de db
        MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
        CambioBulk cambio = component.findCambio(context.getUserContext(), spesa.getDivisa_spesa(), primoGiorno);
        if (cambio == null)
            spesa.setCambio_spesa(null);
        else {
            spesa.setCambio_spesa(cambio.getCambio());
            spesa.setCambio_spesa(spesa.getCambio_spesa().setScale(4, java.math.BigDecimal.ROUND_HALF_UP));
        }
        return;
    }

    /**
     * Il metodo gestisce la selezione della divisa e del cambio relativi alla nazione selezionata per
     * la tappa.
     * Il metodo inizializza :
     * - la divisa con quella letta da tabella MISSIONE_DIARIA per la stessa nazione, per l'inquadramento
     * della missione e valida alla data di inizio missione. Tale divisa non sara' modificabile.
     * - il cambio della divisa trovata valido alla data inizio missione. Tale cambio può essere modificato.
     */

    public void setDivisaCambioTappaEstera(ActionContext context, Missione_tappaBulk tappa) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException {
        MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
        MissioneBulk missione = (MissioneBulk) getModel();

        missione = component.setDivisaCambio(context.getUserContext(), missione, tappa);

        setModel(context, missione);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBP.
     */

    public void setIsDeleting(boolean newIsDeleting) {
    }

    /**
     * Il metodo gestisce la selezione della nazione, divisa e cambio di default
     * (Italia, Euro) nel caso l'utente abbia selezionato comune proprio o altro.
     * (Tali valori non sono modificabili)
     */

    public void setNazioneDivisaCambioItalia(ActionContext context) throws BusinessProcessException {
        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();

        /************* selezionato comune estero *******************/
        if ((tappa.getFl_comune_estero() != null) && (tappa.getFl_comune_estero().booleanValue())) {
            tappa.setNazione(new NazioneBulk());
            tappa.setDivisa_tappa(new DivisaBulk());
            tappa.setCambio_tappa(null);
            return;
        }

        /************* selezionato comune altro o proprio **********/

        if ((tappa.getNazione() != null) && (tappa.getNazione().getTi_nazione() != null) &&
                (tappa.getNazione().getTi_nazione().equals(NazioneBulk.ITALIA)))
            return;        // ITALIA e' gia' stata selezionata

        try {
            // inizializzo nazione ITALIA
            MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
            MissioneBulk missione = (MissioneBulk) getModel();
            missione = component.setNazioneDivisaCambioItalia(context.getUserContext(), missione, tappa);

            setModel(context, missione);
        } catch (Throwable e) {
            throw handleException(e);
        }
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
            MissioneComponentSession sess = (MissioneComponentSession) createComponentSession();
            sess.setSavePoint(context.getUserContext(), savePointName);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo gestisce l'annulla di una modifica/inserimento di una spesa
     */

    public void undoSpesa(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();
        Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) getSpesaController().getModel();

        int index = getSpesaController().getDetails().indexOf(spesa);

        // Ripristino i dati iniziali (prima della modifica) della spesa
        if (spesa.getSpesaIniziale() != null)
            spesa = spesa.getSpesaIniziale();

        // Se la spesa non e' STATUS_CONFIRMED significa che sto annullando
        // una spesa in fase di creazione, quindi non devo ripristinare niente,
        // devo solo cancellarla dalla lista dei dettagli delle spese
        if (spesa.getStatus() == spesa.STATUS_NOT_CONFIRMED) {
            spesa.setToBeDeleted();
            missione.getSpeseMissioneColl().remove(index);

            // Altrimenti rimane abilitato il bottone di modifica
            getSpesaController().setModelIndex(context, -1);
        } else
            getSpesaController().getDetails().set(index, spesa);

        // Fine modalita' modifica/inserimento dettaglio di spesa
        getSpesaController().setEditingSpesa(false);
        resyncChildren(context);
        getFieldValidationMap().clearAll(getSpesaController().getInputPrefix());
        // Ripulisco la selezione dei giorni
        missione.setCollectionGiorniSpeseSelezionati(null);
    }

    /**
     * Il metodo gestisce l'annulla di una modifica/inserimento di una tappa
     */
    public void undoTappa(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();
        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();

        int index = getTappaController().getDetails().indexOf(tappa);

        // Ripristino i dati iniziali (prima della modifica) della tappa
        if (tappa.getTappaIniziale() != null)
            tappa = tappa.getTappaIniziale();

        // Se la tappa non e' STATUS_CONFIRMED significa che sto annullando
        // una tappa in fase di creazione, quindi non devo ripristinare niente,
        // devo solo cancellarla dalla lista delle tappe
        if (tappa.getStatus() == tappa.STATUS_NOT_CONFIRMED) {
            tappa.setToBeDeleted();
            missione.getTappeMissioneColl().remove(index);

            // Altrimenti rimane abilitato il bottone di modifica
            getTappaController().setModelIndex(context, -1);
        } else
            getTappaController().getDetails().set(index, tappa);

        // Fine modalita' modifica/inserimento tappa
        editingTappa = false;
        resyncChildren(context);
        getFieldValidationMap().clearAll(getSpesaController().getInputPrefix());
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
                    ((MissioneComponentSession) createComponentSession()).modificaConBulk(context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
            archiviaAllegati(context);
            archiviaAllegatiMissioneDettagli();
            setUserConfirm(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }


    private void archiviaAllegatiMissioneDettagli() throws ApplicationException, BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();
        for (Missione_dettaglioBulk dettaglio : missione.getDettagliMissioneColl()) {
            for (AllegatoMissioneDettaglioSpesaBulk allegato : dettaglio.getDettaglioSpesaAllegati()) {
                if (allegato.isToBeCreated()) {
                    try {
                        missioniCMISService.storeSimpleDocument(allegato,
                                new FileInputStream(allegato.getFile()),
                                allegato.getContentType(),
                                allegato.getNome(), getCMISPathDettaglio(dettaglio, true));
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                    } catch (FileNotFoundException e) {
                        throw handleException(e);
                    } catch (StorageException _ex) {
                        throw new ApplicationException("Il file " + allegato.getNome() + " già esiste!");
                    }
                } else if (allegato.isToBeUpdated()) {
                    try {
                        if (allegato.getFile() != null)
                            if (!isDocumentoDettaglioProvenienteDaGemis(allegato)) {
                                missioniCMISService.updateStream(allegato.getStorageKey(),
                                        new FileInputStream(allegato.getFile()),
                                        allegato.getContentType());
                                missioniCMISService.updateProperties(allegato,
                                        missioniCMISService.getStorageObjectBykey(allegato.getStorageKey()));
                                allegato.setCrudStatus(OggettoBulk.NORMAL);
                            } else {
                                throw new ApplicationException("Aggiornamento non possibile! Documento proveniente dalla procedura Missioni");
                            }
                    } catch (FileNotFoundException e) {
                        throw handleException(e);
                    }
                }
            }
            for (Iterator<AllegatoMissioneDettaglioSpesaBulk> iterator = dettaglio.getDettaglioSpesaAllegati().deleteIterator(); iterator.hasNext(); ) {
                AllegatoMissioneDettaglioSpesaBulk allegato = iterator.next();
                if (allegato.isToBeDeleted()) {
                    if (!isDocumentoDettaglioProvenienteDaGemis(allegato)) {
                        missioniCMISService.delete(allegato.getStorageKey());
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                    } else {
                        throw new ApplicationException("Cancellazione non possibile! Documento proveniente dalla procedura Missioni");
                    }
                }
            }
        }
        //TODO Verificare cancellazione della folder parent....temporaneamente rimosso codice
        if (!missione.isSalvataggioTemporaneo()) {
            for (Iterator<Missione_dettaglioBulk> iterator = missione.getDettagliMissioneColl().deleteIterator(); iterator.hasNext(); ) {
                Missione_dettaglioBulk dettaglio = iterator.next();
                for (Iterator<AllegatoMissioneDettaglioSpesaBulk> iteratorAll = dettaglio.getDettaglioSpesaAllegati().iterator(); iteratorAll.hasNext(); ) {
                    AllegatoMissioneDettaglioSpesaBulk allegato = iteratorAll.next();
                    if (allegato.isToBeDeleted()) {
                        missioniCMISService.delete(allegato.getStorageKey());
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                    }
                }
            }
        }
    }

    /**
     * Il metodo aggiorno la linea di attivita dell'anticipo collegato a missione inizializzadola con quella
     * selezionata nel compenso (servira' per fare il rimborso)
     */

    public void updateAnticipo(ActionContext context, MissioneBulk missione) throws BusinessProcessException {
        try {
            missione.getAnticipo().setLattPerRimborso(missione.getCompenso().getLineaAttivita());
            missione.getAnticipo().setCd_linea_attivita(missione.getCompenso().getCd_linea_attivita_genrc());
            missione.getAnticipo().setCd_centro_responsabilita(missione.getCompenso().getCd_cdr_genrc());
            missione.getAnticipo().setToBeUpdated();

            ((MissioneComponentSession) createComponentSession()).updateAnticipo(context.getUserContext(), missione);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo aggiorna il compenso associato a missione
     */
    public void updateCompenso(ActionContext context, MissioneBulk missione) throws it.cnr.jada.action.BusinessProcessException {
        try {
            missione = (MissioneBulk) ((MissioneComponentSession) createComponentSession()).updateCompenso(context.getUserContext(), missione);
            ((MissioneBulk) getModel()).setCompenso(missione.getCompenso());
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Il metodo valida la data di registrazione inserita dall'utente.
     * La data di registrazione non deve essere futura e deve essere nell'esercizio in corso
     */

    public void validaDataRegistrazione(ActionContext context) throws it.cnr.jada.comp.ApplicationException, BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();

        if (missione.getDt_registrazione() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire la data di registrazione !");

        try {
            java.util.Calendar calendar = missione.getDateCalendar(missione.getDt_registrazione());
            int annoDataRegistrazione = calendar.get(java.util.Calendar.YEAR);

            if (annoDataRegistrazione != missione.getEsercizio().intValue())
                throw new it.cnr.jada.comp.ApplicationException("La Data registrazione deve essere nell'esercizio in corso");

            if (missione.getDt_registrazione().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
                throw new it.cnr.jada.comp.ApplicationException("La data di registrazione non deve essere successiva alla data odierna !");
        } catch (Throwable e) {
            throw handleException(e);
        }

        // 	Non e' necessario fare il "setModel()" perche' sulla Component non ho modificato
        //	la missione ma ho solo fatto delle validazioni
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
     * Il metodo si occupare di fare le opportune validazioni del terzo inserito dall'utente
     */
    public int validaTerzo(ActionContext context, boolean aBool) throws BusinessProcessException {
        try {
            MissioneComponentSession sess = (MissioneComponentSession) createComponentSession();
            return sess.validaTerzo(context.getUserContext(), (MissioneBulk) getModel(), aBool);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
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

    public boolean isTerzoCervellone(UserContext userContext, MissioneBulk missione) throws BusinessProcessException {

        try {

            MissioneComponentSession sess = (MissioneComponentSession) createComponentSession();
            return sess.isTerzoCervellone(userContext, missione);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public void validaScadenze(MissioneBulk missione, Obbligazione_scadenzarioBulk newScad) throws it.cnr.jada.comp.ApplicationException {

        Vector scadCanc = missione.getDocumentiContabiliCancellati();
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

    public void setDiariaSiNo(ActionContext context) throws BusinessProcessException {
        MissioneBulk missione = (MissioneBulk) getModel();
        Missione_tappaBulk tappa = (Missione_tappaBulk) getTappaController().getModel();
        if (tappa.getDt_inizio_tappa() != null) {
            try {
                java.sql.Timestamp data_fine_diaria_miss_estero;
                Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
                data_fine_diaria_miss_estero = sess.getDt01(context.getUserContext(), new Integer(0), "*", "DIARIA_MISS_ESTERO", "DATA_FINE");

                MissioneComponentSession component = (MissioneComponentSession) createComponentSession();
                Parametri_cnrBulk parametriCnr = component.parametriCnr(context.getUserContext());

                //Calendar cal = Calendar.getInstance();
                //cal.setTime(tappa.getDt_inizio_tappa());
                //int year = cal.get(Calendar.YEAR);
                if (tappa.getFl_comune_estero().booleanValue()) {
                    if (!(tappa.getDt_inizio_tappa().compareTo(data_fine_diaria_miss_estero) > 0)) {
                        tappa.setFl_no_diaria(new Boolean(false));
                    } else {
                        tappa.setFl_no_diaria(new Boolean(true));
                    }
                } else {
                    if (!parametriCnr.getFl_diaria_miss_italia().booleanValue())
					/*&&
					year == it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue())
					*/ {
                        tappa.setFl_no_diaria(new Boolean(true));
                    } else {
                        tappa.setFl_no_diaria(new Boolean(false));
                    }
                }
                setModel(context, missione);
            } catch (Throwable e) {
                throw handleException(e);
            }
        }
    }

    protected java.util.GregorianCalendar getGregorianCalendar() {

        java.util.GregorianCalendar gc = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();

        gc.set(java.util.Calendar.HOUR, 0);
        gc.set(java.util.Calendar.MINUTE, 0);
        gc.set(java.util.Calendar.SECOND, 0);
        gc.set(java.util.Calendar.MILLISECOND, 0);
        gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

        return gc;
    }

    public boolean isRimborsoValidoPerDurataTappeEstere(ActionContext context) throws BusinessProcessException {
        try {
            MissioneBulk missione = (MissioneBulk) getModel();
            BigDecimal numeroMinuti = new BigDecimal("0");
            BigDecimal numeroMinutiLimite = new BigDecimal("1440");

            if ((missione.getTappeMissioneColl() == null) || (missione.getTappeMissioneColl().size() == 0)) {
                return true;
            }

            for (java.util.Iterator i = missione.getTappeMissioneColl().iterator(); i.hasNext(); ) {
                Missione_tappaBulk tappa = (Missione_tappaBulk) i.next();

                if (tappa.isEstera() && tappa.getFl_rimborso()) {
                    MissioneComponentSession component = (MissioneComponentSession) createComponentSession("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
                    numeroMinuti = numeroMinuti.add(component.calcolaMinutiTappa(context.getUserContext(), tappa));
                }

            }
            if (numeroMinuti.compareTo(numeroMinutiLimite) < 0)
                return false;
            else
                return true;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    private java.sql.Timestamp getDataInizioObbligoRegistroUnico(it.cnr.jada.action.ActionContext context) throws BusinessProcessException {
        try {
            return Utility.createConfigurazioneCnrComponentSession().
                    getDt01(context.getUserContext(), new Integer(0), null, "REGISTRO_UNICO_FATPAS", "DATA_INIZIO");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public MissioniCMISService getMissioniCMISService() {
        return missioniCMISService;
    }

    public void setMissioniCMISService(MissioniCMISService missioniCMISService) {
        this.missioniCMISService = missioniCMISService;
    }

    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        missioniCMISService = SpringUtil.getBean("missioniCMISService",
                MissioniCMISService.class);
    }

    @Override
    public void delete(ActionContext actioncontext) throws BusinessProcessException {
    	
        if (Optional.ofNullable(getModel())
                .filter(MissioneBulk.class::isInstance)
                .map(MissioneBulk.class::cast)
                .map(el -> {
                	try {
                		return el.isMissioneFromGemis() &&  !el.isAbilitatoCancellazioneMissioneFromGemis(actioncontext.getUserContext());
					} catch (Exception e) {
						throw new DetailedRuntimeException(e);
					}
                })
                .orElse(Boolean.FALSE))
            throw handleException(new ApplicationException("Missione non eliminabile in quanto proveniente da un flusso approvato."));
        MissioneBulk missioneBulk = (MissioneBulk)getModel(); 
        if (missioneBulk.isMissioneFromGemis()){
            for (AllegatoGenericoBulk allegato : missioneBulk.getArchivioAllegati()) {
            	allegato.setDaNonEliminare(true);
            }
        }

        super.delete(actioncontext);
    }

    @Override
    protected String getStorePath(MissioneBulk missioneBulk, boolean create) throws BusinessProcessException {
        if (missioneBulk.isMissioneFromGemis() && missioneBulk.getIdFolderRimborsoMissione() != null) {
            return missioniCMISService.getCMISPathFromFolderRimborso(missioneBulk);
        } else {
            if (create) {
                final String primaryPath = Arrays.asList(
                        SpringUtil.getBean(StorePath.class).getPathMissioni(),
                        missioneBulk.getCd_unita_organizzativa(),
                        "Rimborso Missione",
                        Optional.ofNullable(missioneBulk.getEsercizio())
                                .map(esercizio -> "Anno ".concat(String.valueOf(esercizio)))
                                .orElse("0")
                ).stream().collect(
                        Collectors.joining(StorageDriver.SUFFIX)
                );
                return missioniCMISService.createFolderMissioneSiglaIfNotPresent(primaryPath, missioneBulk);
            }
        }
        return null;
    }

    protected String getCMISPathDettaglio(Missione_dettaglioBulk dettaglioBulk, boolean create) throws BusinessProcessException {
        if (dettaglioBulk.isMissioneFromGemis() && dettaglioBulk.isDettaglioMissioneFromGemis()) {
            return missioniCMISService.getCMISPathFromFolderDettaglio(dettaglioBulk);
        } else {
            if (create) {
                final String primaryPath = Arrays.asList(
                        SpringUtil.getBean(StorePath.class).getPathMissioni(),
                        dettaglioBulk.getCd_unita_organizzativa(),
                        "Rimborso Missione",
                        Optional.ofNullable(dettaglioBulk.getEsercizio())
                                .map(esercizio -> "Anno ".concat(String.valueOf(esercizio)))
                                .orElse("0")
                ).stream().collect(
                        Collectors.joining(StorageDriver.SUFFIX)
                );
                try {
                    String path = null;
                    if (dettaglioBulk.isMissioneFromGemis()) {
                        path = missioniCMISService.getCMISPathFromFolderRimborso(dettaglioBulk.getMissione());
                    } else {
                        path = missioniCMISService.createFolderMissioneSiglaIfNotPresent(primaryPath, dettaglioBulk.getMissione());
                    }
                    return missioniCMISService.createFolderDettaglioIfNotPresent(path, dettaglioBulk);
                } catch (ApplicationException e) {
                    throw handleException(e);
                }
            }
            return null;
        }
    }

    @Override
    protected Class<AllegatoMissioneBulk> getAllegatoClass() {
        return AllegatoMissioneBulk.class;
    }

    @Override
    public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {

        MissioneBulk allegatoParentBulk = (MissioneBulk) oggettobulk;
        try {
            if (allegatoParentBulk.getIdRimborsoMissione() != null) {
                List<StorageObject> files = missioniCMISService.getFilesOrdineMissione(allegatoParentBulk);
                if (files != null) {
                    for (StorageObject storageObject : files) {
                        if (missioniCMISService.hasAspect(storageObject, StoragePropertyNames.SYS_ARCHIVED.value()))
                            continue;
                        if (excludeChild(storageObject))
                            continue;
                        if (storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).equals(StoragePropertyNames.CMIS_DOCUMENT.value())) {
                            AllegatoMissioneBulk allegato = (AllegatoMissioneBulk) Introspector.newInstance(getAllegatoClass(), storageObject);
                            allegato.setContentType(storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                            allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                            allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                            allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                            completeAllegato(allegato, storageObject);
                            allegato.setCrudStatus(OggettoBulk.NORMAL);
                            allegatoParentBulk.addToArchivioAllegati(allegato);
                        }
                    }
                }
                List<StorageObject> filesRimborso = missioniCMISService.getFilesRimborsoMissione(allegatoParentBulk);
                if (filesRimborso != null) {
                    for (StorageObject storageObject : filesRimborso) {
                        if (missioniCMISService.hasAspect(storageObject, StoragePropertyNames.SYS_ARCHIVED.value()))
                            continue;
                        if (excludeChild(storageObject))
                            continue;
                        recuperoAllegatiDettaglioMissioneSigla(allegatoParentBulk, storageObject);
                        if (storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).equals(StoragePropertyNames.CMIS_DOCUMENT.value())) {
                            AllegatoMissioneBulk allegato = (AllegatoMissioneBulk) Introspector.newInstance(getAllegatoClass(), storageObject);
                            allegato.setContentType(storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                            allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                            allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                            allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                            completeAllegato(allegato, storageObject);
                            allegato.setCrudStatus(OggettoBulk.NORMAL);
                            allegatoParentBulk.addToArchivioAllegati(allegato);
                        }
                    }
                }
                if (allegatoParentBulk.getIdFlusso() != null) {
                    StorageObject storageObject = missioniCMISService.recuperoFlows(allegatoParentBulk.getIdFlusso());
                    if (storageObject != null) {
                        AllegatoMissioneBulk allegato = (AllegatoMissioneBulk) Introspector.newInstance(getAllegatoClass(), storageObject);
                        allegato.setContentType(storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                        allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                        allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                        allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                        allegato.setAspectName(AllegatoMissioneBulk.FLUSSO_RIMBORSO);
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                        allegatoParentBulk.addToArchivioAllegati(allegato);
                    }
                }
                if (allegatoParentBulk.getIdFlussoOrdineMissione() != null) {
                    StorageObject storageObject = missioniCMISService.recuperoFlows(allegatoParentBulk.getIdFlussoOrdineMissione());
                    if (storageObject != null) {
                        AllegatoMissioneBulk allegato = (AllegatoMissioneBulk) Introspector.newInstance(getAllegatoClass(), storageObject);
                        allegato.setContentType(storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                        allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                        allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                        allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                        allegato.setAspectName(AllegatoMissioneBulk.FLUSSO_ORDINE);
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                        allegatoParentBulk.addToArchivioAllegati(allegato);
                    }
                }
            } else {
                List<StorageObject> files = missioniCMISService.getFilesMissioneSigla(allegatoParentBulk);
                if (files != null) {
                    for (StorageObject storageObject : files) {
                        if (missioniCMISService.hasAspect(storageObject, StoragePropertyNames.SYS_ARCHIVED.value()))
                            continue;
                        if (excludeChild(storageObject))
                            continue;

                        recuperoAllegatiDettaglioMissioneSigla(allegatoParentBulk, storageObject);

                        if (storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).equals(StoragePropertyNames.CMIS_DOCUMENT.value())) {
                            AllegatoMissioneBulk allegato = (AllegatoMissioneBulk) Introspector.newInstance(getAllegatoClass(), storageObject);
                            allegato.setContentType(storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                            allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                            allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                            allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                            completeAllegato(allegato, storageObject);
                            allegato.setCrudStatus(OggettoBulk.NORMAL);
                            allegatoParentBulk.addToArchivioAllegati(allegato);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | DetailedException e) {
            throw handleException(e);
        }
        return oggettobulk;
    }

    private void recuperoAllegatiDettaglioMissioneSigla(MissioneBulk allegatoParentBulk, StorageObject storageObject)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).equals(StoragePropertyNames.CMIS_FOLDER.value())) {
            String prop = storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value());
            if (prop.equals("F:missioni_rimborso_dettaglio:main")) {
                BigInteger rigaString = storageObject.getPropertyValue("missioni_rimborso_dettaglio:riga");
                List<StorageObject> children = missioniCMISService.getChildren(storageObject.getKey());
                if (children != null) {
                    for (StorageObject doc : children) {
                        if (doc.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).equals(StoragePropertyNames.CMIS_DOCUMENT.value())) {
                            AllegatoMissioneDettaglioSpesaBulk allegato = (AllegatoMissioneDettaglioSpesaBulk) Introspector.newInstance(AllegatoMissioneDettaglioSpesaBulk.class, doc);
                            allegato.setContentType(doc.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                            allegato.setNome(doc.getPropertyValue(StoragePropertyNames.NAME.value()));
                            allegato.setDescrizione(doc.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                            allegato.setTitolo(doc.getPropertyValue(StoragePropertyNames.TITLE.value()));
                            allegato.setCrudStatus(OggettoBulk.NORMAL);

                            for (java.util.Iterator i = allegatoParentBulk.getSpeseMissioneColl().iterator(); i.hasNext(); ) {
                                Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) i.next();
                                if (spesa.getPg_riga().compareTo(rigaString.longValue()) == 0) {
                                    spesa.addToDettaglioSpesaAllegati(allegato);
                                }
                            }
                        }
                    }
                }
            } else if (prop.equals("F:missioni_dettaglio_sigla:main")) {
                BigInteger riga = storageObject.getPropertyValue("missioni_dettaglio_sigla:riga");
                List<StorageObject> children = missioniCMISService.getChildren(storageObject.getKey());
                if (children != null) {
                    for (StorageObject doc : children) {
                        if (doc.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).equals(StoragePropertyNames.CMIS_DOCUMENT.value())) {
                            AllegatoMissioneDettaglioSpesaBulk allegato = (AllegatoMissioneDettaglioSpesaBulk) Introspector.newInstance(AllegatoMissioneDettaglioSpesaBulk.class, doc);
                            allegato.setContentType(doc.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                            allegato.setNome(doc.getPropertyValue(StoragePropertyNames.NAME.value()));
                            allegato.setDescrizione(doc.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                            allegato.setTitolo(doc.getPropertyValue(StoragePropertyNames.TITLE.value()));
                            allegato.setCrudStatus(OggettoBulk.NORMAL);

                            for (java.util.Iterator i = allegatoParentBulk.getSpeseMissioneColl().iterator(); i.hasNext(); ) {
                                Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) i.next();
                                if (spesa.getPg_riga().compareTo(riga.longValue()) == 0) {
                                    spesa.addToDettaglioSpesaAllegati(allegato);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void completeAllegato(AllegatoMissioneBulk allegato, StorageObject storageObject) throws ApplicationException {
        allegato.setAspectName(Optional.ofNullable(storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                .map(list -> list.stream().filter(
                        o -> AllegatoMissioneBulk.aspectNamesKeys.get(o) != null
                        ).findAny().orElse(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA)
                ).orElse(null));
        super.completeAllegato(allegato, storageObject);
    }


    @Override
    public String getAllegatiFormName() {
        super.getAllegatiFormName();
        return "allegatiMissione";
    }

    public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
        AllegatoMissioneBulk allegato = (AllegatoMissioneBulk) getCrudArchivioAllegati().getModel();
        InputStream is = missioniCMISService.getResource(allegato.getStorageKey());
        final StorageObject storageObject = missioniCMISService.getStorageObjectBykey(allegato.getStorageKey());
        ((HttpActionContext) actioncontext).getResponse().setContentLength(
                storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue()
        );
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

    public String getNomeAllegatoDettaglio() throws ApplicationException {
        OggettoBulk bulk = getModel();

        AllegatoMissioneDettaglioSpesaBulk dettaglio = (AllegatoMissioneDettaglioSpesaBulk) getDettaglioSpesaAllegatiController().getModel();
        if (dettaglio != null) {
            return dettaglio.getNome();
        }
        return "";
    }

    public void scaricaGiustificativiCollegati(ActionContext actioncontext) throws Exception {
        AllegatoMissioneDettaglioSpesaBulk dettaglio = (AllegatoMissioneDettaglioSpesaBulk) getDettaglioSpesaAllegatiController().getModel();
        if (dettaglio != null) {
            StorageObject storageObject = missioniCMISService.getStorageObjectBykey(dettaglio.getStorageKey());
            if (storageObject != null) {
                InputStream is = missioniCMISService.getResource(storageObject);
                ((HttpActionContext) actioncontext).getResponse().setContentLength(
                        storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue()
                );
                ((HttpActionContext) actioncontext).getResponse().setContentType(
                        storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value())
                );
                OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
                ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
                byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
                int buflength;
                while ((buflength = is.read(buffer)) > 0) {
                    os.write(buffer, 0, buflength);
                }
                is.close();
                os.flush();
            } else {
                throw new it.cnr.jada.action.MessageToUser("Giustificativi non presenti sul documentale per la riga selezionata");
            }
        } else {
            throw new it.cnr.jada.action.MessageToUser("Giustificativi non presenti sul documentale per la riga selezionata");
        }
    }

    @Override
    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
        return Optional.ofNullable(allegato)
                    .filter(AllegatoMissioneBulk.class::isInstance)
                    .map(AllegatoMissioneBulk.class::cast)
                    .map(allegatoGenericoBulk -> !isDocumentoProvenienteDaGemis(allegatoGenericoBulk))
                    .orElse(Boolean.TRUE);
    }

    private Boolean isDocumentoProvenienteDaGemis(AllegatoGenericoBulk allegato) {
        return Optional.ofNullable(allegato)
                .flatMap(allegatoGenericoBulk -> Optional.ofNullable(allegatoGenericoBulk.getStorageKey()))
                .map(key -> missioniCMISService.getStorageObjectBykey(key))
                .map(storageObject -> !missioniCMISService.hasAspect(storageObject, MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA))
                .orElse(false);
    }

    private Boolean isDocumentoDettaglioProvenienteDaGemis(AllegatoGenericoBulk allegato) {
        return Optional.ofNullable(allegato)
                .flatMap(allegatoGenericoBulk -> Optional.ofNullable(allegatoGenericoBulk.getStorageKey()))
                .map(key -> missioniCMISService.getStorageObjectBykey(key))
                .map(storageObject -> missioniCMISService.hasAspect(storageObject, MissioniCMISService.ASPECT_MISSIONE_RIMOBORSO_DETTAGLIO_SCONTRINI))
                .orElse(false);
    }

    protected Boolean isPossibileCancellazioneDettaglioAllegato(AllegatoGenericoBulk allegato) {
            return Optional.ofNullable(allegato)
                    .filter(AllegatoMissioneDettaglioSpesaBulk.class::isInstance)
                    .map(AllegatoMissioneDettaglioSpesaBulk.class::cast)
                    .map(allegatoGenericoBulk -> !isDocumentoDettaglioProvenienteDaGemis(allegatoGenericoBulk))
                    .orElse(Boolean.TRUE);
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>scadenzarioDettaglio</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglioSpesaAllegatiController() {
        return dettaglioSpesaAllegatiController;
    }

    @Override
    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
        if (allegato.getFile() != null) {
            if (isDocumentoProvenienteDaGemis(allegato)) {
                setMessage("Aggiornamento non possibile! Documento proveniente dalla procedura Missioni");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
        MissioneBulk missione = (MissioneBulk) allegatoParentBulk;
        if (!missione.isSalvataggioTemporaneo()) {
            super.gestioneCancellazioneAllegati(allegatoParentBulk);
        }
    }

    private static final String[] TAB_TESTATA = new String[]{ "tabTestata","Testata","/missioni00/tab_missione_testata.jsp" };
    private static final String[] TAB_ANAGRAFICO = new String[]{ "tabAnagrafico","Anagrafico","/missioni00/tab_missione_anagrafico.jsp" };
    private static final String[] TAB_CONFIGURAZIONE_TAPPE = new String[]{ "tabConfigurazioneTappe","Configurazione tappe","/missioni00/tab_missione_configurazione_tappa.jsp" };
    private static final String[] TAB_DETTAGLIO_SPESE = new String[]{ "tabDettaglioSpese","Dettaglio spese","/missioni00/tab_missione_dettaglio_spese.jsp" };
    private static final String[] TAB_DETTAGLIO_DIARIA = new String[]{ "tabDettaglioDiaria","Dettaglio diaria","/missioni00/tab_missione_dettaglio_diaria.jsp" };
    private static final String[] TAB_DETTAGLIO_RIMBORSO = new String[]{ "tabDettaglioRimborso","Dettaglio rimborso","/missioni00/tab_missione_dettaglio_rimborso.jsp" };
    private static final String[] TAB_OBBLIGAZIONE = new String[]{ "tabObbligazione","Documenti associati","/missioni00/tab_missione_obbligazione.jsp" };
    private static final String[] TAB_CONSUNTIVO = new String[]{ "tabConsuntivo","Consuntivo","/missioni00/tab_missione_consuntivo.jsp" };
    private static final String[] TAB_ALLEGATI = new String[]{ "tabAllegati","Allegati","/missioni00/tab_missione_allegati.jsp" };

    public String[][] getTabs() {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;
        pages.put(i++, TAB_TESTATA);
        pages.put(i++, TAB_ANAGRAFICO);
        pages.put(i++, TAB_CONFIGURAZIONE_TAPPE);
        pages.put(i++, TAB_DETTAGLIO_SPESE);
        pages.put(i++, TAB_DETTAGLIO_DIARIA);
        pages.put(i++, TAB_DETTAGLIO_RIMBORSO);
        pages.put(i++, TAB_OBBLIGAZIONE);
        pages.put(i++, TAB_CONSUNTIVO);
        final Optional<MissioneBulk> optionalMissioneBulk = Optional.ofNullable(getModel())
                .filter(MissioneBulk.class::isInstance)
                .map(MissioneBulk.class::cast);
        if (optionalMissioneBulk
                .filter(missioneBulk -> Optional.ofNullable(missioneBulk.getPg_missione()).isPresent())
                .filter(missioneBulk -> missioneBulk.getPg_missione().compareTo(new Long(0)) > 0)
                .isPresent()
                ) {
            pages.put(i++, TAB_ALLEGATI);
        }
        if (attivaEconomicaParallela && optionalMissioneBulk
                .map(missioneBulk -> !Optional.ofNullable(missioneBulk.getFl_associato_compenso()).orElse(Boolean.TRUE))
                .orElse(Boolean.FALSE)
        ) {
            pages.put(i++, CRUDScritturaPDoppiaBP.TAB_ECONOMICA);
        }
        String[][] tabs = new String[i][3];
        for (int j = 0; j < i; j++)
            tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
        return tabs;
    }

    @Override
    public CollapsableDetailCRUDController getMovimentiDare() {
        return movimentiDare;
    }

    @Override
    public CollapsableDetailCRUDController getMovimentiAvere() {
        return movimentiAvere;
    }

}
