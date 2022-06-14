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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.CDRComponentSession;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.missioni00.bp.CRUDAnticipoBP;
import it.cnr.contab.missioni00.bp.CRUDMissioneBP;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.util.*;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Obbligazione
 */

public class CRUDObbligazioneBP extends CRUDVirtualObbligazioneBP {
    private final SimpleDetailCRUDController capitoliDiSpesaCds = new SimpleDetailCRUDController("CapitoliDiSpesaCds", Voce_fBulk.class, "capitoliDiSpesaCdsColl", this);
    private final SimpleDetailCRUDController centriDiResponsabilita = new SimpleDetailCRUDController("CentriDiResponsabilita", CdrBulk.class, "cdrColl", this);
    private final SimpleDetailCRUDController lineeDiAttivita = new SimpleDetailCRUDController("LineeDiAttivita", V_pdg_obbligazione_speBulk.class, "lineeAttivitaColl", this);
    private final SimpleDetailCRUDController nuoveLineeDiAttivita = new SimpleDetailCRUDController("NuoveLineeDiAttivita", Linea_attivitaBulk.class, "nuoveLineeAttivitaColl", this);
    private final CRUDScadenzeController scadenzario = new CRUDScadenzeController("Scadenzario", Obbligazione_scadenzarioBulk.class, "obbligazione_scadenzarioColl", this);
    private final SimpleDetailCRUDController scadenzarioDettaglio = new SimpleDetailCRUDController("ScadenzarioDettaglio", Obbligazione_scad_voceBulk.class, "obbligazione_scad_voceColl", scadenzario);
    private final SimpleDetailCRUDController aggregatoPerCdr = new SimpleDetailCRUDController("AggregatoPerCdr", Obbligazione_scad_voce_aggregatoBulk.class, "cdrAggregatoColl", this);
    private final SimpleDetailCRUDController aggregatoPerCapitolo = new SimpleDetailCRUDController("AggregatoPerCapitolo", Obbligazione_scad_voce_aggregatoBulk.class, "capitoliAggregatoColl", this);

    private final SimpleDetailCRUDController crudObbligazione_pluriennale = new SimpleDetailCRUDController("ObbligazioniPluriennali", Obbligazione_pluriennaleBulk.class, "obbligazioniPluriennali", this);

    public SimpleDetailCRUDController getCrudObbligazione_pluriennale() {
        return crudObbligazione_pluriennale;
    }

    // "editingScadenza" viene messo a True solo quando si modifica una scadenza (bottone "editing scadenza")
    private boolean editingScadenza = false;
    private boolean bringBackWithoutCommit = false;
    private java.util.List vociSelezionate;
    private boolean siope_attiva = false;
    private boolean incarichi_repertorio_attiva = false;
    private boolean flNuovoPdg = false;
    private boolean enableVoceNext = false;
    private boolean variazioneAutomaticaEnabled = false;

    private byte[] bringBackClone = null;

    public CRUDObbligazioneBP() {
        super();
        setTab("tab", "tabObbligazione");
        setTab("tabScadenzario", "tabScadenza");
    }

    public CRUDObbligazioneBP(String function) {
        super(function);

        setTab("tab", "tabObbligazione");
        setTab("tabScadenzario", "tabScadenza");
    }

    private boolean attivaImpegnoPluriennale = false;

    public boolean isAttivaImpegnoPluriennale() {
        return attivaImpegnoPluriennale;
    }

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        try {
            attivaImpegnoPluriennale = Utility.createConfigurazioneCnrComponentSession().isImpegnoPluriennaleAttivo(actioncontext.getUserContext());
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
        super.init(config, actioncontext);
    }

    /**
     * Predispongo l'inserimento di una nuova scadenza dopo la selezione del bottone Nuovo della table Scadenze
     *
     * @param context Il contesto dell'azione
     */

    public void addScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        editingScadenza = true;            // inizio modalita' inserimento scadenza
        setTab("tab", "tabScadenzario");
        setTab("tabScadenzario", "tabScadenza");
        getScadenzario().add(context);


    }

    /**
     * Gestisce l'annullamento dell'imputazione finanziaria
     *
     * @param context Il contesto dell'azione
     */

    public void annullaImputazioneFinanziariaCapitoli(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            annullaImputazioneFinanziariaCdr(context);
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            obbligazione.setCapitoliDiSpesaCdsColl(Collections.EMPTY_LIST);
            obbligazione.setCapitoliDiSpesaCdsSelezionatiColl(Collections.EMPTY_LIST);
            getCapitoliDiSpesaCds().getSelection().clear();
            setModel(context, obbligazione);
            resyncChildren(context);
            obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_TESTATA_CONFERMATA);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce l'annullamento dell'imputazione finanziaria
     *
     * @param context Il contesto dell'azione
     */

    private void annullaImputazioneFinanziariaCdr(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            annullaImputazioneFinanziariaLatt(context);
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            obbligazione.setCdrColl(Collections.EMPTY_LIST);
            obbligazione.setCdrSelezionatiColl(Collections.EMPTY_LIST);
            getCentriDiResponsabilita().getSelection().clear();
            setModel(context, obbligazione);
            resyncChildren(context);
            obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_CAPITOLI_CONFERMATI);

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce l'annullamento dell'imputazione finanziaria
     *
     * @param context Il contesto dell'azione
     */

    private void annullaImputazioneFinanziariaDettagli(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            Obbligazione_scad_voceBulk osv;
            Obbligazione_scadenzarioBulk os;
            for (Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); ) {
                os = (Obbligazione_scadenzarioBulk) i.next();
                for (int index = os.getObbligazione_scad_voceColl().size() - 1; index >= 0; index--) {
                    osv = (Obbligazione_scad_voceBulk) os.getObbligazione_scad_voceColl().get(index);
                    osv.setToBeDeleted();
                    os.getObbligazione_scad_voceColl().remove(index);
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Valida il contratto riportato.
     *
     * @param context Il contesto dell'azione
     */
    public void validaContratto(it.cnr.jada.action.ActionContext context, ContrattoBulk contratto) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.action.MessageToUser {
        try {
            ObbligazioneBulk obbligazione = ((ObbligazioneBulk) getModel());
            ((ObbligazioneComponentSession) createComponentSession()).validaContratto(context.getUserContext(), obbligazione, contratto, null);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce l'annullamento dell'imputazione finanziaria
     *
     * @param context Il contesto dell'azione
     */

    private void annullaImputazioneFinanziariaLatt(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            obbligazione.setLineeAttivitaColl(Collections.EMPTY_LIST);
            obbligazione.setLineeAttivitaSelezionateColl(Collections.EMPTY_LIST);
            obbligazione.setNuoveLineeAttivitaColl(new BulkList());
            getLineeDiAttivita().getSelection().clear();
            annullaImputazioneFinanziariaDettagli(context);
            setModel(context, obbligazione);
            resyncChildren(context);
            obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_CDR_CONFERMATI);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo per disabilitare tutti i campi, nel caso l'obbligazione pgiro sia stato cancellato ( come se fosse in stato di visualizzazione )
     */
    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

        super.basicEdit(context, bulk, doInitializeForEdit);

        if (getStatus() != VIEW) {
            ObbligazioneBulk obb = (ObbligazioneBulk) getModel();
            if (obb != null && obb.getDt_cancellazione() != null) {
                setStatus(VIEW);
//			if ( obb.getDt_cancellazione() != null )
                setMessage("L'impegno è stata cancellato. Non consentita la modifica.");
/*			else if ( "Y".equals(obb.getRiportato()) )
				setMessage("L'obbligazione è stata riportata all'esercizio successivo. Non consentita la modifica.");*/

            }
        }
    }

    /**
     * Gestisce il cambio del flag imputazione finanziaria automatica o manuale
     * dell'obbligazione.
     *
     * @param context Il contesto dell'azione
     */

    public void cambiaFl_calcolo_automatico(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ObbligazioneBulk obbligazione = ((ObbligazioneBulk) getModel());
            if (obbligazione.getFl_calcolo_automatico().booleanValue())
                obbligazione = ((ObbligazioneComponentSession) createComponentSession()).generaDettagliScadenzaObbligazione(context.getUserContext(), obbligazione, null);
            setModel(context, obbligazione);
            resyncChildren(context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce il caricamento dei capitoli di spesa.
     *
     * @param context Il contesto dell'azione
     */

    public void caricaCapitoliDiSpesaCDS(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            annullaImputazioneFinanziariaCapitoli(context);
            ObbligazioneBulk obbligazione = ((ObbligazioneComponentSession) createComponentSession()).listaCapitoliPerCdsVoce(context.getUserContext(), (ObbligazioneBulk) getModel());
            setModel(context, obbligazione);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce il caricamento dei centri di responsabilità.
     *
     * @param context Il contesto dell'azione
     */

    public void caricaCentriDiResponsabilita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            annullaImputazioneFinanziariaCapitoli(context);
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            if (this.isFlNuovoPdg()) {
                annullaImputazioneFinanziariaCdr(context);
                obbligazione.setCapitoliDiSpesaCdsSelezionatiColl(Arrays.asList(obbligazione.getElemento_voce()));
                CDRComponentSession sessCDR = (CDRComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", CDRComponentSession.class);
                List<V_assestatoBulk> listaAssestato = ((ObbligazioneComponentSession) createComponentSession()).listaAssestatoSpese(context.getUserContext(), obbligazione);
                if (!listaAssestato.isEmpty()) {
                    obbligazione.setCdrColl(new ArrayList<CdrBulk>());
                    for (V_assestatoBulk v_assestatoBulk : listaAssestato) {
                        CdrBulk cdrBulk = (CdrBulk) sessCDR.findByPrimaryKey(context.getUserContext(), new CdrBulk(v_assestatoBulk.getCd_centro_responsabilita()));
                        if (!obbligazione.getCdrColl().contains(cdrBulk))
                            obbligazione.getCdrColl().add(cdrBulk);
                    }
                }
            } else {
                obbligazione = ((ObbligazioneComponentSession) createComponentSession()).listaCapitoliPerCdsVoce(context.getUserContext(), obbligazione);
                annullaImputazioneFinanziariaCdr(context);
                Vector capitoli = new Vector(obbligazione.getCapitoliDiSpesaCdsColl());
                if (capitoli.size() == 0)
                    throw new MessageToUser("Nessun capitolo selezionato");
                obbligazione = ((ObbligazioneBulk) getModel());
                obbligazione.setCapitoliDiSpesaCdsSelezionatiColl(capitoli);
                Vector cdr = ((ObbligazioneComponentSession) createComponentSession()).listaCdrPerCapitoli(context.getUserContext(), obbligazione);
                obbligazione.setCdrColl(cdr);
            }
            obbligazione.setLineeAttivitaColl(Collections.EMPTY_LIST);
//		setModel( obbligazione );
            obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_CAPITOLI_CONFERMATI);
            resyncChildren(context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce il caricamento delle linee di attività.
     *
     * @param context Il contesto dell'azione
     */

    public void caricaLineeAttivita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ObbligazioneBulk obbligazione = ((ObbligazioneComponentSession) createComponentSession()).listaCapitoliPerCdsVoce(context.getUserContext(), (ObbligazioneBulk) getModel());
            annullaImputazioneFinanziariaLatt(context);
            Vector capitoli = new Vector(obbligazione.getCapitoliDiSpesaCdsColl());
            Vector cdr = new Vector(getCentriDiResponsabilita().getSelectedModels(context));
            if (cdr.size() == 0)
                throw new MessageToUser("E' necessario selezionare almeno un CdR");
            obbligazione = ((ObbligazioneBulk) getModel());
            //obbligazione.setCapitoliDiSpesaCdsSelezionatiColl( capitoli );
            obbligazione.setCdrSelezionatiColl(cdr);
            Vector lineeAttivita = ((ObbligazioneComponentSession) createComponentSession()).listaLineeAttivitaPerCapitoliCdr(context.getUserContext(), obbligazione);
            obbligazione.setLineeAttivitaColl(lineeAttivita);
            obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_CDR_CONFERMATI);
//		setModel( obbligazione );
            resyncChildren(context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per la conferma dei dati selezionati o immessi, relativi
     * alle linee di attività.
     *
     * @param context Il contesto dell'azione
     */

    public void confermaLineeAttivita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ObbligazioneBulk obbligazione = ((ObbligazioneBulk) getModel());
            Vector latt = new Vector(getLineeDiAttivita().getSelectedModels(context));
            obbligazione.setLineeAttivitaSelezionateColl(latt);
            BulkList nuoveLatt = new BulkList(getNuoveLineeDiAttivita().getDetails());
            for (Iterator i = nuoveLatt.iterator(); i.hasNext(); ) {
                ((Linea_attivitaBulk) i.next()).validate();
            }
            obbligazione.setNuoveLineeAttivitaColl(nuoveLatt);

            ObbligazioneBulk newObbligazione = ((ObbligazioneComponentSession) createComponentSession()).generaDettagliScadenzaObbligazione(context.getUserContext(), obbligazione, null);

            try {
                ((ObbligazioneComponentSession) createComponentSession()).validaImputazioneFinanziaria(context.getUserContext(), newObbligazione);
                obbligazione = newObbligazione;
            } catch (Exception e) {
                obbligazione.setLineeAttivitaSelezionateColl(Collections.EMPTY_LIST);
                obbligazione.setNuoveLineeAttivitaColl(new BulkList());
                obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_CDR_CONFERMATI);
                throw handleException(e);
            }

            //		getLineeDiAttivita().setSelection( ((Vector)obbligazione.getLineeAttivitaSelezionateColl()).elements());
            setModel(context, obbligazione);
            obbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_LATT_CONFERMATE);
            resyncChildren(context);
        } catch (ValidationException e) {
            throw new MessageToUser(e.getMessage());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per la conferma dei dati selezionati o immessi, relativi
     * alla scadenza.
     *
     * @param obbligazione obbligazione
     */
    protected void validaScadenza(ObbligazioneBulk obbligazione, Obbligazione_scadenzarioBulk ob_scadenzario) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ob_scadenzario.validate();
            obbligazione.validaNuoveLatt();

            //se provengo da BP dei doc amm imposto il flag fromDocAmm a true
            if (IDocumentoAmministrativoBP.class.isAssignableFrom(getParent().getClass())) {
                IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP) getParent();
                obbligazione.setFromDocAmm(true);
                obbligazione.updateScadenzeFromDocAmm(docAmmBP.getDocumentoAmministrativoCorrente().getObbligazioniHash());
            } else
                obbligazione.setFromDocAmm(false);

        } catch (ValidationException e) {
            throw new MessageToUser(e.getMessage());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per la conferma dei dati selezionati o immessi, relativi
     * alla scadenza.
     *
     * @param context Il contesto dell'azione
     */
    public void confermaScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            Obbligazione_scadenzarioBulk ob_scadenzario = (Obbligazione_scadenzarioBulk) getScadenzario().getModel();
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            validaScadenza(obbligazione, ob_scadenzario);

            ObbligazioneComponentSession session = (ObbligazioneComponentSession) createComponentSession();
            obbligazione = session.verificaScadenzarioObbligazione(context.getUserContext(), ob_scadenzario);
            setModel(context, obbligazione);
            editingScadenza = false;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Gestisce la copia dell'obbligazione.
     *
     * @param context Il contesto dell'azione
     */

    public void copiaObbligazione(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ObbligazioneBulk nuovaObbligazione = (ObbligazioneBulk) ((ObbligazioneBulk) getModel()).clona(this, context);
            nuovaObbligazione = (ObbligazioneBulk) createComponentSession().inizializzaBulkPerInserimento(context.getUserContext(), nuovaObbligazione);
            nuovaObbligazione.setUser(context.getUserInfo().getUserid());
            setModel(context, nuovaObbligazione);
            nuovaObbligazione.setInternalStatus(ObbligazioneBulk.INT_STATO_LATT_CONFERMATE);
            setStatus(INSERT);
            resyncChildren(context);
            setTab("tab", "tabObbligazione");
            setTab("tabScadenzario", "tabScadenza");
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per creare una toolbar applicativa personalizzata.
     *
     * @return newToolbar La nuova toolbar creata
     */

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {

        Button[] toolbar = super.createToolbar();
        Button[] newToolbar = new Button[toolbar.length + 3];
        for (int i = 0; i < toolbar.length; i++)
            newToolbar[i] = toolbar[i];
        newToolbar[toolbar.length] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.copia");
        newToolbar[toolbar.length + 1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.creamandato");
        newToolbar[toolbar.length + 2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.modimpegno");
        return newToolbar;
    }

    public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        int crudStatus = getModel().getCrudStatus();
        try {
            ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
            if (obbligazione.getStato_obbligazione().equals(ObbligazioneBulk.STATO_OBB_PROVVISORIO)) {
                ((ObbligazioneComponentSession) createComponentSession()).cancellaObbligazioneProvvisoria(context.getUserContext(), (ObbligazioneBulk) getModel());
                reset(context);
                setMessage("Cancellazione effettuata");
            }
            // si tratta di un annullamento
            else if (obbligazione.getStato_obbligazione().equals(ObbligazioneBulk.STATO_OBB_DEFINITIVO)) {
                obbligazione = ((ObbligazioneComponentSession) createComponentSession()).stornaObbligazioneDefinitiva(context.getUserContext(), (ObbligazioneBulk) getModel());
                setModel(context, obbligazione);
                setMessage("Annullamento effettuato");
            } else // stato = STORNATA
                throw new BusinessProcessException("Lo stato dell'impegno non ne consente la cancellazione/storno");

        } catch (Exception e) {
            getModel().setCrudStatus(crudStatus);
            throw handleException(e);
        }
    }

    /**
     * Gestisce un comando "Edit".
     *
     * @param context <code>ActionContext</code> in uso.
     * @param bulk    OggettoBulk in uso
     * @return <code>Forward</code>
     * @throws <code>BusinessProcessException</code>
     */

    public void edit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean initializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
        editingScadenza = false;
        super.edit(context, bulk, initializeForEdit);
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        getCapitoliDiSpesaCds().setSelection(
                Optional.ofNullable(obbligazione.getCapitoliDiSpesaCdsSelezionatiColl())
                        .filter(collection -> !collection.isEmpty())
                        .filter(Vector.class::isInstance)
                        .map(Vector.class::cast)
                        .map(vector -> vector.elements())
                        .orElse(Collections.emptyEnumeration())
        );
        getCentriDiResponsabilita().setSelection(
                Optional.ofNullable(obbligazione.getCdrSelezionatiColl())
                        .filter(collection -> !collection.isEmpty())
                        .filter(Vector.class::isInstance)
                        .map(Vector.class::cast)
                        .map(vector -> vector.elements())
                        .orElse(Collections.emptyEnumeration())
        );
        getLineeDiAttivita().setSelection(
                Optional.ofNullable(obbligazione.getLineeAttivitaSelezionateColl())
                        .filter(collection -> !collection.isEmpty())
                        .filter(Vector.class::isInstance)
                        .map(Vector.class::cast)
                        .map(vector -> vector.elements())
                        .orElse(Collections.emptyEnumeration())
        );

    }

    /**
     * @param context <code>ActionContext</code> in uso.
     * @throws <code>BusinessProcessException</code>
     */

    public void editaScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        editingScadenza = true;                // Inizio modalita' modifica scadenza
        setTab("tab", "tabScadenzario");
        setTab("tabScadenzario", "tabScadenza");
        Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) getScadenzario().getModel();
        Obbligazione_scadenzarioBulk scadIniziale = new Obbligazione_scadenzarioBulk();
        scadIniziale.setIm_scadenza(scad.getIm_scadenza());
        scadIniziale.setDt_scadenza(scad.getDt_scadenza());
        scadIniziale.setDs_scadenza(scad.getDs_scadenza());
        scad.setScadenza_iniziale(scadIniziale);

    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>aggregatoPerCapitolo</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getAggregatoPerCapitolo() {
        return aggregatoPerCapitolo;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>aggregatoPerCdr</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getAggregatoPerCdr() {
        return aggregatoPerCdr;
    }

    public OggettoBulk getBringBackModel() {

        Obbligazione_scadenzarioBulk scadenzaSelezionata = (Obbligazione_scadenzarioBulk) scadenzario.getModel();
        if (scadenzaSelezionata == null)
            throw new MessageToUser("E' necessario selezionare uno scadenziario", ERROR_MESSAGE);

        if (getParent() != null) {
            if (getParent() instanceof IDocumentoAmministrativoBP) {
                IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP) getParent();
                Obbligazione_scadenzarioBulk scadCorrente = docAmmBP.getObbligazione_scadenziario_corrente();
                if (scadCorrente != null) {
                    if ((docAmmBP instanceof CRUDNotaDiCreditoBP ||
                            docAmmBP instanceof CRUDNotaDiDebitoBP ||
                            docAmmBP instanceof CRUDNotaDiCreditoAttivaBP) &&
                            !scadCorrente.equalsByPrimaryKey(scadenzaSelezionata))
                        throw new MessageToUser("La scadenza che si sta tentando di riportare NON è corretta! Selezionare \"" + scadCorrente.getDs_scadenza() + "\"", ERROR_MESSAGE);
                    if (docAmmBP instanceof CRUDFatturaPassivaIBP) {
                        CRUDFatturaPassivaIBP fatturaPassivaBP = (CRUDFatturaPassivaIBP) docAmmBP;
                        Fattura_passiva_IBulk fatturaPassiva = (Fattura_passiva_IBulk) fatturaPassivaBP.getModel();
                        if (!scadCorrente.equalsByPrimaryKey(scadenzaSelezionata)) {
                            if (fatturaPassiva.hasStorni() || fatturaPassiva.hasAddebiti() || fatturaPassivaBP.isDeleting())
                                throw new MessageToUser("Non è possibile selezionare una diversa scadenza da quella associata al documento amministrativo, perché esso ha degli addebiti o degli storni associati! Selezionare la scadenza \"" + scadCorrente.getDs_scadenza() + "\".", ERROR_MESSAGE);
                            if (scadenzaSelezionata.getIm_associato_doc_amm() != null &&
                                    scadenzaSelezionata.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0 &&
                                    !fatturaPassivaBP.isDeleting()) {
                                if (!new Fattura_passiva_IBulk(
                                        scadenzaSelezionata.getCd_cds(),
                                        scadenzaSelezionata.getObbligazione().getCd_unita_organizzativa(),
                                        scadenzaSelezionata.getEsercizio_doc_passivo(),
                                        scadenzaSelezionata.getPg_doc_passivo()
                                ).equalsByPrimaryKey(fatturaPassiva))
                                    throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                            }
                        }
                    } else if (docAmmBP instanceof CRUDNotaDiCreditoAttivaBP) {
                        CRUDNotaDiCreditoAttivaBP ncaBP = (CRUDNotaDiCreditoAttivaBP) docAmmBP;
                        Nota_di_credito_attivaBulk nca = (Nota_di_credito_attivaBulk) ncaBP.getModel();
                        if (scadenzaSelezionata.getIm_associato_doc_amm() != null &&
                                scadenzaSelezionata.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0 &&
                                !ncaBP.isDeleting()) {
                            if (!new Nota_di_credito_attivaBulk(
                                    scadenzaSelezionata.getCd_cds(),
                                    scadenzaSelezionata.getObbligazione().getCd_unita_organizzativa(),
                                    scadenzaSelezionata.getEsercizio_doc_passivo(),
                                    scadenzaSelezionata.getPg_doc_passivo()
                            ).equalsByPrimaryKey(nca))
                                throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                        }
                    } else if (docAmmBP instanceof CRUDDocumentoGenericoPassivoBP) {
                        if (!scadCorrente.equalsByPrimaryKey(scadenzaSelezionata)) {
                            CRUDDocumentoGenericoPassivoBP docGenPassivoBP = (CRUDDocumentoGenericoPassivoBP) docAmmBP;
                            Documento_genericoBulk docGenPassivo = (Documento_genericoBulk) docGenPassivoBP.getModel();
                            if (scadenzaSelezionata.getIm_associato_doc_amm() != null &&
                                    scadenzaSelezionata.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0) {
                                String cd_cds_doc_gen = (docGenPassivo.isFlagEnte()) ?
                                        docGenPassivo.getCd_cds() :
                                        scadenzaSelezionata.getObbligazione().getCd_cds_origine();
                                String cd_uo_docgen = (docGenPassivo.isFlagEnte()) ?
                                        docGenPassivo.getCd_unita_organizzativa() :
                                        scadenzaSelezionata.getObbligazione().getCd_uo_origine();
                                if (!new Documento_genericoBulk(
                                        cd_cds_doc_gen,
                                        scadenzaSelezionata.getCd_tipo_documento_amm(),
                                        cd_uo_docgen,
                                        scadenzaSelezionata.getEsercizio_doc_passivo(),
                                        scadenzaSelezionata.getPg_doc_passivo()
                                ).equalsByPrimaryKey(docGenPassivo))
                                    throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
            if (//getParent() instanceof CRUDFatturaPassivaIBP ||
                    getParent() instanceof CRUDDocumentoGenericoPassivoBP) {
                if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(scadenzaSelezionata.getIm_scadenza()) == 0)
                    throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" con importo 0.", ERROR_MESSAGE);
            }
//		if (getParent() instanceof RicercaObbligazioniBP) {
//			RicercaObbligazioniBP ricercaBP = (RicercaObbligazioniBP)getParent();
//			if (ricercaBP.getParent() != null && ricercaBP.getParent() instanceof CRUDFatturaPassivaIBP &&
//				new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(scadenzaSelezionata.getIm_scadenza()) == 0)
//					throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" con importo 0.",ERROR_MESSAGE);
//		}

            if (getParent() instanceof IDocumentoAmministrativoBP &&
                    scadenzaSelezionata.getIm_associato_doc_amm() != null &&
                    scadenzaSelezionata.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0) {
                IDocumentoAmministrativoBP docAmmBP = (IDocumentoAmministrativoBP) getParent();
                if (docAmmBP instanceof CRUDMissioneBP) {
                    CRUDMissioneBP missioneBP = (CRUDMissioneBP) docAmmBP;
                    MissioneBulk missione = (MissioneBulk) missioneBP.getModel();
                    if (!new MissioneBulk(scadenzaSelezionata.getCd_cds(), scadenzaSelezionata.getObbligazione().getCd_unita_organizzativa(), scadenzaSelezionata.getEsercizio_doc_passivo(), scadenzaSelezionata.getPg_doc_passivo()).equalsByPrimaryKey(missione))
                        throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                }
                if (docAmmBP instanceof CRUDAnticipoBP) {
                    CRUDAnticipoBP anticipoBP = (CRUDAnticipoBP) docAmmBP;
                    AnticipoBulk anticipo = (AnticipoBulk) anticipoBP.getModel();
                    if (!new AnticipoBulk(scadenzaSelezionata.getCd_cds(), scadenzaSelezionata.getObbligazione().getCd_unita_organizzativa(), scadenzaSelezionata.getEsercizio_doc_passivo(), scadenzaSelezionata.getPg_doc_passivo()).equalsByPrimaryKey(anticipo))
                        throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                }
                if (docAmmBP instanceof it.cnr.contab.compensi00.bp.CRUDCompensoBP) {
                    it.cnr.contab.compensi00.bp.CRUDCompensoBP compensoBP = (it.cnr.contab.compensi00.bp.CRUDCompensoBP) docAmmBP;
                    CompensoBulk compenso = (CompensoBulk) compensoBP.getModel();
                    if (!new CompensoBulk(scadenzaSelezionata.getCd_cds(), scadenzaSelezionata.getObbligazione().getCd_unita_organizzativa(), scadenzaSelezionata.getEsercizio_doc_passivo(), scadenzaSelezionata.getPg_doc_passivo()).equalsByPrimaryKey(compenso))
                        throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                }
            }
        }
        return scadenzario.getModel();
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>capitoliDiSpesaCds</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCapitoliDiSpesaCds() {
        return capitoliDiSpesaCds;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>centriDiResponsabilita</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCentriDiResponsabilita() {
        return centriDiResponsabilita;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>lineeDiAttivita</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getLineeDiAttivita() {
        return lineeDiAttivita;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>nuoveLineeDiAttivita</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getNuoveLineeDiAttivita() {
        return nuoveLineeDiAttivita;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>scadenzario</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getScadenzario() {
        return scadenzario;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>scadenzarioDettaglio</code>
     * di tipo <code>SimpleDetailCRUDController</code>.
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getScadenzarioDettaglio() {
        return scadenzarioDettaglio;
    }

    /**
     * Inizializza il modello per la modifica.
     *
     * @param context Il contesto dell'azione
     * @param bulk    L'oggetto bulk in uso
     * @return Oggetto Bulk L'oggetto bulk inizializzato
     */
    public OggettoBulk initializeModelForEdit(ActionContext context, OggettoBulk bulk) throws BusinessProcessException {
        try {
            it.cnr.jada.ejb.CRUDComponentSession compSession = (getUserTransaction() == null) ?
                    createComponentSession() :
                    getVirtualComponentSession(context, false); //responsabilità setSafePoint(true) demandata all'init del bp
            OggettoBulk oggettobulk = compSession.inizializzaBulkPerModifica(
                    context.getUserContext(),
                    bulk.initializeForEdit(this, context));


            oggettobulk = initializeModelForEditAllegati(context, oggettobulk);
            ((ObbligazioneBulk) oggettobulk).caricaAnniResidui(context);
            return oggettobulk;
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }
//
//	Abilito il bottone di ANNULLA RIPORTA documento solo se non ho scadenze in fase di modifica/inserimento
//

    public boolean isBringbackButtonEnabled() {
        return super.isBringbackButtonEnabled() && !isEditingScadenza();
    }

    /**
     * Abilito il bottone di calcolo della disponibilità di cassa dei due anni
     * successivi a quello corrente dell'obbligazione, quando il campo "Voce del
     * piano" è stato valorizzato.
     */
    public boolean isCalcolaDispCassaAnniSuccessiviButtonEnabled() throws it.cnr.jada.action.BusinessProcessException {
//	return ((ObbligazioneBulk)getModel()).getElemento_voce().getCrudStatus() == ObbligazioneBulk.NORMAL;
        return true;
    }

    /**
     * Abilito il bottone di conferma della scadenza solo se questa e' in fase di modifica/inserimento
     * <p>
     * isEditable 	= FALSE se l'obbligazione e' in visualizzazione
     * = TRUE se l'obbligazione e' in modifica/inserimento
     */


    public boolean isConfermaScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException {
        return (isEditable() && getScadenzario().getModel() != null && isEditingScadenza());
    }

    /**
     * Abilito il bottone di copia dell'obbligazione solo se questa e' in fase di modifica/inserimento
     * <p>
     * isEditable 	= FALSE se l'obbligazione e' in visualizzazione
     * = TRUE se l'obbligazione e' in modifica/inserimento
     */

    public boolean isCopiaObbligazioneButtonEnabled() {
        return isEditable() && isEditing() && !isEditingScadenza();
    }

    /**
     * Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
     */

    public boolean isCopiaObbligazioneButtonHidden() {

        return isEditOnly();
    }

    /**
     * Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
     */

    public boolean isDeleteButtonEnabled() {
        return super.isDeleteButtonEnabled() && !isEditingScadenza() && !ObbligazioneBulk.STATO_OBB_STORNATO.equals(((ObbligazioneBulk) getModel()).getStato_obbligazione());
    }

    /**
     * Ritorna TRUE se la scadenza e' in fase di modifica/inserimento
     * cioe' se e' stato selezionato il bottone per editare la scadenza.
     */

    public boolean isEditingScadenza() {
        return editingScadenza;
    }

    /**
     * Abilito il bottone di edit solo se la scadenza non e' gia' in fase di modifica
     * <p>
     * isEditable 	= FALSE se l'accertamento e' in visualizzazione
     * = TRUE se l'accertamento e' in modifica/inserimento
     */

    public boolean isEditScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException {
        return (isEditable() && getScadenzario().getModel() != null && !isEditingScadenza());
    }

    /**
     * Abilito il bottone di salvataggio documento solo se non ho scadenze in fase di modifica/inserimento
     */

    public boolean isSaveButtonEnabled() {
        return super.isSaveButtonEnabled() && !isEditingScadenza() && !ObbligazioneBulk.STATO_OBB_STORNATO.equals(((ObbligazioneBulk) getModel()).getStato_obbligazione());
    }
//
//	Abilito il bottone di RIPORTA documento solo se non ho scadenze in fase di modifica/inserimento
//

    public boolean isUndoBringBackButtonEnabled() {
        return super.isUndoBringBackButtonEnabled() && !isEditingScadenza();
    }

    /**
     * Abilito il bottone di Undo della scadenza solo se questa e' in fase di modifica/inserimento
     * <p>
     * isEditable 	= FALSE se l'accertamento e' in visualizzazione
     * = TRUE se l'accertamento e' in modifica/inserimento
     */


    public boolean isUndoScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException {
        return (isEditable() && getScadenzario().getModel() != null && isEditingScadenza());
    }

    /**
     * Verifica se il bottone di Visualizzazione delle Spese del Cdr è abilitato.
     *
     * @return TRUE    Il bottone di Visualizzazione delle Spese del Cdr è abilitato
     * FALSE 	Il bottone di Visualizzazione delle Spese del Cdr non è abilitato
     */
    public boolean isVisualizzaSpeseCdrButtonEnabled() {
        //return getModel.getInternalStatus() >= INT_STATO_CDR_CONFERMATI ;
        return getCentriDiResponsabilita().getModel() != null;


    }

    /**
     * Inzializza il ricevente nello stato di INSERT.
     */
    public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        editingScadenza = false;
        super.reset(context);
    }

    /**
     * Inzializza il ricevente nello stato di SEARCH.
     */
    public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        editingScadenza = false;
        super.resetForSearch(context);
    }

    /* Metodo per riportare il fuoco sul tab iniziale */
    protected void resetTabs(ActionContext context) {
        setTab("tab", "tabObbligazione");
    }

    /**
     * Gestisce un comando "Salva"
     *
     * @param context Il contesto dell'azione
     */
    public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.bulk.ValidationException {
        super.save(context);

        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        getCapitoliDiSpesaCds().setSelection(((Vector) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl()).elements());
        getCentriDiResponsabilita().setSelection(((Vector) obbligazione.getCdrSelezionatiColl()).elements());
        getLineeDiAttivita().setSelection(((Vector) obbligazione.getLineeAttivitaSelezionateColl()).elements());
    }

    /**
     * Metodo per selezionare la scadenza dell'obbligazione.
     *
     * @param scadenza La scadenza dell'obbligazione
     * @param context  Il contesto dell'azione
     */
    public void selezionaScadenza(Obbligazione_scadenzarioBulk scadenza, ActionContext context) {

        getScadenzario().setModelIndex(context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(getScadenzario().getDetails(), scadenza));
        setTab("tab", "tabScadenzario");
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void undoScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        editingScadenza = false;        // Fine modalita' modifica/inserimento scadenza

        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) getScadenzario().getModel();

        //ripristino i dati iniziali
        if (scadenza.getScadenza_iniziale() != null) {
            scadenza.setIm_scadenza(scadenza.getScadenza_iniziale().getIm_scadenza());
            scadenza.setDt_scadenza(scadenza.getScadenza_iniziale().getDt_scadenza());
            scadenza.setDs_scadenza(scadenza.getScadenza_iniziale().getDs_scadenza());
        }

        // Nel caso l'utente decida di fare un Undo di un inserimento non confermato
        // rimuovo il dettaglio

        int index = getScadenzario().getModelIndex();

        if (scadenza.getStatus() == Obbligazione_scadenzarioBulk.STATUS_NOT_CONFIRMED) {
            scadenza.setToBeDeleted();
            obbligazione.getObbligazione_scadenzarioColl().remove(index);
            getScadenzario().setModelIndex(context, -1);            // altrimenti rimane abilitato il bottone di modifica
            resyncChildren(context);
        }
        getFieldValidationMap().clearAll(getScadenzario().getInputPrefix());

    }

    /**
     * Viene richiesta alla component che gestisce l'obbligazione di verificare la validità
     * della nuova Linea di Attività
     */
    public void validaNuovaLineaAttivita(ActionContext context, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) throws ValidationException {
        try {
            if (latt != null) {
                nuovaLatt.getObbligazione().validateNuovaLineaAttivita(nuovaLatt, latt);
                ((ObbligazioneComponentSession) createComponentSession()).verificaNuovaLineaAttivita(context.getUserContext(), latt);
                nuovaLatt.setLinea_att(latt);
            }

        } catch (Throwable e) {
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Verifica la validità dell'obbligazione.
     *
     * @param context Il contesto dell'azione
     */
    public void validate(ActionContext context) throws ValidationException {

        super.validate(context);
        if (isEditOnly() && !isSaveOnBringBack())
            try {
                ((ObbligazioneComponentSession) createComponentSession()).verificaObbligazione(context.getUserContext(), (ObbligazioneBulk) getModel());
            } catch (Throwable e) {
                throw new ValidationException(e.getMessage());
            }
    }

    /**
     * Viene richiesta alla component che gestisce i compensi di verificare la validità
     * dell'Obbligazione per i doc. amministrativi
     */
    public void verificaObbligazionePerDocAmm(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        return;
    }

    public void verificaTestataObbligazione(ActionContext context) throws BusinessProcessException, it.cnr.jada.bulk.ValidationException {
        getModel().validate();
        try {
            ((ObbligazioneComponentSession) createComponentSession()).verificaTestataObbligazione(context.getUserContext(), (ObbligazioneBulk) getModel());
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    public void riportaSelezioneVoci(ActionContext context, java.util.List vociList) throws BusinessProcessException, it.cnr.jada.bulk.ValidationException {
        try {
            getModel().validate();
            annullaImputazioneFinanziariaCapitoli(context);
            ObbligazioneBulk obbligazione = ((ObbligazioneComponentSession) createComponentSession()).riportaSelezioneVoci(context.getUserContext(), (ObbligazioneBulk) getModel(), vociList);
            setModel(context, obbligazione);
            getCapitoliDiSpesaCds().setSelection(((Vector) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl()).elements());
            getCentriDiResponsabilita().setSelection(((Vector) obbligazione.getCdrSelezionatiColl()).elements());
            getLineeDiAttivita().setSelection(((Vector) obbligazione.getLineeAttivitaSelezionateColl()).elements());
            setDirty(true);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
     * Se l'obbligazione non ha scadenze associate ne crea una di importo totale
     * Se l'obbligazione ha scadenze associate non effettua alcuna operazione
     */
    public void creaScadenzaResiduale(ActionContext context) throws BusinessProcessException {
        try {
            ObbligazioneBulk obbligazione = ((ObbligazioneBulk) getModel());
            if (!obbligazione.hasDettagli()) {
                obbligazione = ((ObbligazioneComponentSession) createComponentSession()).creaScadenzaResiduale(context.getUserContext(), obbligazione);
                setModel(context, obbligazione);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public java.util.List getVociSelezionate() {
        return vociSelezionate;
    }

    public void setVociSelezionate(java.util.List list) {
        vociSelezionate = list;
    }

    public boolean isCreaMandatoButtonHidden() {
        /*
         * 12/04/2006 Pagano
         * Nascosto il tasto in attesa di completare la funzionalità
         *
         */
        return true;
    }

    public boolean isCreaMandatoButtonEnabled() {
        return true;
    }

    public boolean isModImpResButtonHidden() {
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        return !obbligazione.isObbligazioneResiduo();
    }

    public boolean isModImpResButtonEnabled() {
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        return obbligazione.isObbligazioneResiduo() && !(isBringBack());
    }

    public boolean modificaObbligazioneResProprie(it.cnr.jada.action.ActionContext context, StringBuffer errControllo) throws BusinessProcessException {
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        String errore;
        if (obbligazione.isObbligazioneResiduo()) {

            try {
                errore = ((ObbligazioneResComponentSession) createComponentSession()).controllaDettagliScadenzaObbligazione(context.getUserContext(), obbligazione, null);
            } catch (Exception e) {
                throw handleException(e);
            }
            errControllo.append(errore);
            return true;
        }
        return false;
    }

    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        try {
            Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext()));
            setSiope_attiva(parCnr.getFl_siope().booleanValue());
            setIncarichi_repertorio_attiva(true);
            setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
            if (parCnr.isEnableVoceNext()) {
                Parametri_cnrBulk parCnrNewAnno = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext()) + 1);
                setEnableVoceNext(parCnrNewAnno != null);
            }
            setVariazioneAutomaticaEnabled(Utility.createConfigurazioneCnrComponentSession().isVariazioneAutomaticaSpesa(actioncontext.getUserContext()));
        } catch (Throwable throwable) {
            throw new BusinessProcessException(throwable);
        }
    }

    private boolean isSiope_attiva() {
        return siope_attiva;
    }

    private void setSiope_attiva(boolean siope_attiva) {
        this.siope_attiva = siope_attiva;
    }

    public boolean isFlNuovoPdg() {
        return flNuovoPdg;
    }

    private void setFlNuovoPdg(boolean flNuovoPdg) {
        this.flNuovoPdg = flNuovoPdg;
    }

    public boolean isROElemento_voce() {
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        if (isSiope_attiva())
            return obbligazione.isROElemento_voce() || obbligazione.isAssociataADocCont();
        return obbligazione.isROElemento_voce();
    }

    public boolean isROFindElemento_voce() {
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        if (isSiope_attiva())
            return obbligazione.isAssociataADocCont();
        return false;
    }

    public boolean isRoCampiResiduoProprio() {
        ObbligazioneBulk obbligazione = (ObbligazioneBulk) getModel();
        if (obbligazione == null || (obbligazione != null && !obbligazione.isObbligazioneResiduo()) || this.getStatus() == SEARCH)
            return false;
        return obbligazione != null && obbligazione.isObbligazioneResiduo() && this.getStatus() != INSERT;
    }

    public boolean isIncarichi_repertorio_attiva() {
        return incarichi_repertorio_attiva;
    }

    private void setIncarichi_repertorio_attiva(boolean incarichi_repertorio_attiva) {
        this.incarichi_repertorio_attiva = incarichi_repertorio_attiva;
    }

    /**
     * Valida il contratto riportato.
     *
     * @param context Il contesto dell'azione
     */
    public void validaIncaricoRepertorio(it.cnr.jada.action.ActionContext context, Incarichi_repertorioBulk incarico) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.action.MessageToUser {
        try {
            ObbligazioneBulk obbligazione = ((ObbligazioneBulk) getModel());
            ((ObbligazioneComponentSession) createComponentSession()).validaIncaricoRepertorio(context.getUserContext(), obbligazione, incarico, null);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public void caricaTerzoDiversi(it.cnr.jada.action.ActionContext context) throws BusinessProcessException {
        try {
            it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = ((Configurazione_cnrComponentSession) EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getConfigurazione(context.getUserContext(), null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_TERZO_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_CODICE_DIVERSI_IMPEGNI);
            if (config == null || config.getIm01() == null)
                return;

            RemoteIterator remoteiterator = find(context, null, new TerzoBulk(new Integer(config.getIm01().intValue())));
            if (remoteiterator != null && remoteiterator.countElements() == 1) {
                TerzoBulk terzo = (TerzoBulk) remoteiterator.nextElement();
                EJBCommonServices.closeRemoteIterator(context, remoteiterator);
                ((ObbligazioneBulk) getModel()).setCreditore(terzo);
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isEnableVoceNext() {
        return enableVoceNext;
    }

    private void setEnableVoceNext(boolean enableVoceNext) {
        this.enableVoceNext = enableVoceNext;
    }

    public boolean isVariazioneAutomaticaEnabled() {
        return variazioneAutomaticaEnabled;
    }

    private void setVariazioneAutomaticaEnabled(boolean variazioneAutomaticaEnabled) {
        this.variazioneAutomaticaEnabled = variazioneAutomaticaEnabled;
    }

    public boolean isElementoVoceNewVisible() {
        return this.isEnableVoceNext() && getModel() != null &&
                ((ObbligazioneBulk) getModel()).isEnableVoceNext();
    }

    public String[][] getTabs() {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;

        pages.put(i++, new String[]{"tabObbligazione", "Impegni", "/doccont00/tab_obbligazione.jsp"});
        pages.put(i++, new String[]{"tabImputazioneFin", "Imputazione Finanziaria", "/doccont00/tab_imputazione_fin_obbligazione.jsp"});
        pages.put(i++, new String[]{"tabScadenzario", "Scadenzario", "/doccont00/tab_scadenzario_obbligazione.jsp"});
        pages.put(i++, new String[]{"tabCdrCapitoli", "Cdr", "/doccont00/tab_cdr_capitoli.jsp"});

        if(attivaImpegnoPluriennale) {
            if (!isSearching()) {
                    pages.put(i++, new String[]{"tabObbligazioniPluriennali", "Obbligazioni Pluriennali", "/doccont00/tab_obb_pluriennali.jsp"});
            }
        }


        String[][] tabs = new String[i][3];

        for (int j = 0; j < i; j++)
            tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
        return tabs;
    }
}
