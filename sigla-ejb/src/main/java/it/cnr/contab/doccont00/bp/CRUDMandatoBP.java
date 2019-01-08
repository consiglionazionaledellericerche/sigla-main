package it.cnr.contab.doccont00.bp;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Mandato
 */

public class CRUDMandatoBP extends CRUDAbstractMandatoBP {
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
    };
    private final SimpleDetailCRUDController siopeCupCollegati = new SimpleDetailCRUDController("siopeCupCollegati", MandatoSiopeCupIBulk.class, "mandatoSiopeCupColl", codiciSiopeCollegati) {
        public void validate(ActionContext context, OggettoBulk model) throws ValidationException {
            validateSiopeCupCollegati(context, model);
        }
    };
    private boolean siope_attiva = false;
    private boolean cup_attivo = false;
    private boolean siope_cup_attivo = false;
    private boolean tesoreria_unica = false;
    private boolean supervisore = false;

    public CRUDMandatoBP() {
        super();
        setTab("tab", "tabMandato");
    }

    public CRUDMandatoBP(String function) {
        super(function);
        setTab("tab", "tabMandato");
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
        Button[] toolbar = new Button[9];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.printpdf");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.contabile");
        return toolbar;
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
        super.save(context);
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

//		if (Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), ((MandatoBulk)this.getModel()).getEsercizio()).getFl_cup().booleanValue() &&
//				Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(),  ((MandatoBulk)this.getModel()).getEsercizio()).getFl_siope_cup().booleanValue()){
//			
//			 if (((MandatoBulk)this.getModel()).getDt_emissione()!=null){
//					Timestamp dataLimite=Utility.createConfigurazioneCnrComponentSession().getDt01(actioncontext.getUserContext(), "DATA_LIMITE_CUP_SIOPE_CUP");
//					if( ((MandatoBulk)this.getModel()).getDt_emissione().after(dataLimite))
//						setSiope_cup_attivo(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(),CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_siope_cup().booleanValue());
//					else
//						setCup_attivo(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(),CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_cup().booleanValue());
//			 }
//		}else{
            setCup_attivo(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_cup().booleanValue());
            setSiope_cup_attivo(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_siope_cup().booleanValue());
            setTesoreria_unica(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())).getFl_tesoreria_unica().booleanValue());
            setSupervisore(Utility.createUtenteComponentSession().isSupervisore(actioncontext.getUserContext()));
//		}
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
        return !isInputReadonly() &&
                getStatus() != VIEW &&
                ((MandatoBulk) getModel()).getStato_trasmissione() != null &&
                ((MandatoBulk) getModel()).getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
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

    public boolean isSupervisore() {
        return supervisore;
    }

    public void setSupervisore(boolean supervisore) {
        this.supervisore = supervisore;
    }


}