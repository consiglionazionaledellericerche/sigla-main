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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.AllegatoAccertamentoBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoResiduoComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

/**
 * Business Process che gestisce le attivit di CRUD per l'entita' Accertamento Residuo.
 */
public class CRUDAccertamentoResiduoBP extends CRUDAccertamentoBP {

	boolean scadenzaModificabile = false;
	boolean scadenzaModificata = false;
	boolean statusOriginarioSaveButtonEnabled = false;
	boolean isEditableOriginario = false;
	boolean isContrattoEsistente = false;
	boolean isStatoModificabile = true;
	Date dataVisibilitaStato;
	/**
	 * CRUDAccertamentoResiduoBP constructor comment.
	 */
	public CRUDAccertamentoResiduoBP() {
		super();
	}
	/**
	 * CRUDAccertamentoResiduoBP constructor comment.
	 * @param function java.lang.String
	 */
	public CRUDAccertamentoResiduoBP(String function) {
		super(function+"Tr");
	}
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
	{
		super.basicEdit(context, bulk, doInitializeForEdit);
		setScadenzaModificata(false);
		setContrattoEsistente(((AccertamentoBulk)getModel()).getContratto()!=null);
		if (getStatus()!=VIEW && isEditable())
			setScadenzaModificabile(true);
		setStatusAndEditableMap();
		isStatoModificabile = ((AccertamentoResiduoBulk)getModel()).getStato() == null;
	}
	/**
	 * Metodo utilizzato per la conferma dei dati selezionati o immessi, relativi
	 * alla scadenza.
	 * @param context Il contesto dell'azione
	 */
	public void confermaScadenza(ActionContext context)	throws BusinessProcessException {
		Accertamento_scadenzarioBulk as = ((Accertamento_scadenzarioBulk)getScadenzario().getModel());
		if (as.getScadenza_iniziale()==null||(as.getIm_scadenza()!=null&&as.getScadenza_iniziale().getIm_scadenza().compareTo(as.getIm_scadenza())!=0))
			setScadenzaModificata(true);
			
		super.confermaScadenza(context);
	}
	public boolean isEditScadenzaButtonEnabled() throws BusinessProcessException {
		return (isScadenzaModificabile() && getScadenzario().getModel() != null && !isEditingScadenza() && isScadenzaModificabilePerVariazione());
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP#init(it.cnr.jada.action.Config, it.cnr.jada.action.ActionContext)
	 */
	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		super.init(config, context);
		setEditableOriginario(isEditable());
		setStatusOriginarioSaveButtonEnabled(super.isSaveButtonEnabled());
		setModel( context, createEmptyModelForSearch(context) );
		setStatus(SEARCH);
	}
	/**
	 * Inizializza il modello per la modifica.
	 * @param context Il contesto dell'azione
	 * @param bulk L'oggetto bulk in uso
	 * @return Oggetto Bulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
		try {
			AccertamentoBulk oggettobulk = (AccertamentoBulk)super.initializeModelForEdit(context, bulk);

			if (isStatoVisibile()) {
				BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
				try	{
					RemoteIterator ri = this.find(context, new CompoundFindClause(), new AccertamentoBulk(), oggettobulk, "allEqualsAccertamenti");
					ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
					while (ri.hasMoreElements()) {
						AccertamentoBulk currAccertamento = (AccertamentoBulk) ri.nextElement();
						if (currAccertamento.getEsercizio().compareTo(oggettobulk.getEsercizio())<=0) {
							currAccertamento = (AccertamentoBulk)initializeModelForEditAllegati(context, currAccertamento);
							for (AllegatoGenericoBulk allegatoGenericoBulk : currAccertamento.getArchivioAllegati())
								((AllegatoAccertamentoBulk)allegatoGenericoBulk).setEsercizioDiAppartenenza(currAccertamento.getEsercizio());
							archivioAllegati.addAll(currAccertamento.getArchivioAllegati());
						}
					}
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
				}catch(java.rmi.RemoteException ex){
					throw handleException(ex);
				}
	
				((AllegatoParentBulk)oggettobulk).setArchivioAllegati(archivioAllegati);
			}
			
			oggettobulk.caricaAnniResidui(context);

			return oggettobulk;
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}
	
	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		if (allegato instanceof AllegatoAccertamentoBulk && this.getModel()!=null && !allegato.isToBeCreated()) {
			if (isROStato() || ((AllegatoAccertamentoBulk)allegato).getEsercizioDiAppartenenza()==null || this.getModel()==null ||
				!((AllegatoAccertamentoBulk)allegato).getEsercizioDiAppartenenza().equals(((AccertamentoBulk)this.getModel()).getEsercizio()))
				return false;
		}
		return super.isPossibileCancellazione(allegato);
	}
	
	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
		if (allegato instanceof AllegatoAccertamentoBulk && this.getModel()!=null) {
			if (isROStato() || ((AllegatoAccertamentoBulk)allegato).getEsercizioDiAppartenenza()==null || this.getModel()==null ||
				!((AllegatoAccertamentoBulk)allegato).getEsercizioDiAppartenenza().equals(((AccertamentoBulk)this.getModel()).getEsercizio()))
				return false;
		}
		return super.isPossibileModifica(allegato);
	}
	/**
	 * Completa, dove possibile, i searchtool presenti nella form, effettuando le ricerche necessarie per
	 * tutti i searchtool con valore null e attributo completeOnSave a true.
	 * Per ogni searchtool abilitato viene effettuato una ricerca; se il risultato della ricerca
	 * contiene un unico elemento viene assegnato alla property del searchtool, altrimenti viene generata una
	 * eccezione di validazione.
	 * @param context l'ActionContext da cui proviene la richiesta
	 * @param controller il FormController da controllare
	 */
	public void completeSearchTools(ActionContext context,FormController controller) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
	{
		OggettoBulk model = controller.getModel();
		for (java.util.Enumeration e = controller.getBulkInfo().getFieldProperties();e.hasMoreElements();)
			completeSearchTool(context,model,(FieldProperty)e.nextElement());
	}

	/*
	 *	Nascondo il bottone di cancellazione documento
	 */
	public boolean isDeleteButtonHidden() {
		return true;
	}
	/*
	 *	Nascondo il bottone di creazione documento
	 */
	public boolean isNewButtonHidden() {
		return true;
	}
	/**
	 * @return
	 */
	public boolean isScadenzaModificabile() {
		return scadenzaModificabile;
	}
	/**
	 *	Abilito il bottone di salvataggio documento solo se non ho scadenze in fase di modifica/inserimento
	 */
	public boolean isSaveButtonEnabled() {
		return isStatusOriginarioSaveButtonEnabled() && !isEditingScadenza();
	}
	/**
	 * @param b
	 */
	private void setScadenzaModificabile(boolean b) {
		scadenzaModificabile = b;
	}
	/**
	 * @return
	 */
	public boolean isStatusOriginarioSaveButtonEnabled() {
		return statusOriginarioSaveButtonEnabled;
	}

	/**
	 * @param b
	 */
	private void setStatusOriginarioSaveButtonEnabled(boolean b) {
		statusOriginarioSaveButtonEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isScadenzaModificata() {
		return scadenzaModificata;
	}

	/**
	 * @param b
	 */
	private void setScadenzaModificata(boolean b) {
		scadenzaModificata = b;
	}
	private void setStatusAndEditableMap(int status){
		setStatus(status==VIEW?VIEW:EDIT);
		setEditable(status==VIEW?false:true);
	}	
	public void setStatusAndEditableMap(){
		if (getModel()!=null && ((AccertamentoBulk)getModel()).isAccertamentoResiduo()) {
			setStatusAndEditableMap(VIEW);
			if (getTab( "tab" )!=null) {
				if (getTab( "tab" ).equalsIgnoreCase("tabScadenziario")) {
					if ( isScadenzaModificabile() && !((AccertamentoBulk)getModel()).isDocRiportato())
						setStatusAndEditableMap(EDIT);
				} else if (getTab( "tab" ).equalsIgnoreCase("tabAllegati")) {
					if (!isROStato())
						setStatusAndEditableMap(EDIT);
					else if (!isStatoModificabile) {
						setStatusAndEditableMap(EDIT);
						getArchivioAllegati().setShrinkable(false);
						getArchivioAllegati().setGrowable(true);		
						getArchivioAllegati().setReadonlyOnEdit(true);
					}
				} else if (getTab( "tab" ).equalsIgnoreCase("tabVincoli")) {
					if (!isROStato() || ((AccertamentoResiduoBulk)getModel()).isStatoDubbio() || ((AccertamentoResiduoBulk)getModel()).isStatoGiudizialmenteControverso())
						setStatusAndEditableMap(EDIT);
				}
			}
		}
	} 
	
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.FormBP#setTab(java.lang.String, java.lang.String)
	 */
	public void setTab(String s, String s1) {
		super.setTab(s, s1);
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDAccertamentoBP#save(it.cnr.jada.action.ActionContext)
	 */
	public void save(ActionContext context) throws BusinessProcessException, ValidationException {
		setStatusAndEditableMap(EDIT);
		try {
			super.save(context);
		} catch(BusinessProcessException e) {
			setStatusAndEditableMap();
			throw e;
		} catch(ValidationException e) {
			setStatusAndEditableMap();
			throw e;
		}
		setStatusAndEditableMap();
	}
	public boolean isEditableOriginario() {
		return isEditableOriginario;
	}
	public void setEditableOriginario(boolean isEditableOriginario) {
		this.isEditableOriginario = isEditableOriginario;
	}
	public boolean isContrattoEnabledOnView() {
		return getModel()!=null && 
		       isEditableOriginario() &&
		       !isContrattoEsistente();
	}
	public boolean isFlagAutomaticoEnabledOnView() {
		return getModel()!=null && 
		       isEditableOriginario();
	}
	public boolean isContrattoEsistente() {
		return isContrattoEsistente;
	}
	public void setContrattoEsistente(boolean isContrattoEsistente) {
		this.isContrattoEsistente = isContrattoEsistente;
	}
	public boolean isROImporto() {
		// Se provengo da BP dei doc amm non deve essere mai modificabile
		if ( IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass()))
			return true;

		if (getStatus()!=VIEW || isScadenzaModificabile())
			return false;
		return true;
	}
	public boolean isROStato() {
		boolean roStato = isROImporto();
		if (getModel()!=null && !isStatoModificabile)
			roStato = true;		
		return roStato;
	}

	public void cancellaAccertamentoModTemporanea(ActionContext context, Accertamento_modificaBulk obbMod) throws BusinessProcessException {
		try {
			if (obbMod!=null && obbMod.isTemporaneo())
				((AccertamentoResiduoComponentSession)createComponentSession()).cancellaAccertamentoModTemporanea(context.getUserContext(), obbMod);
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
	protected it.cnr.jada.util.jsp.Button[] createToolbar() 
	{
			
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 1 ];
		for ( int i = 0; i< toolbar.length; i++ )
			newToolbar[ i ] = toolbar[ i ];
		newToolbar[ toolbar.length ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.modaccertamento");
		return newToolbar;
	}
	public boolean isRiportaAvantiButtonEnabled() 
	{
		AccertamentoBulk doc = ((AccertamentoBulk)getModel());
		
		return !isRiportaAvantiButtonHidden() &&
					(isEditing() || isScadenzaModificabile()) &&
					doc != null &&
					!((AccertamentoBulk)doc).isDocRiportato();

	}
	public boolean isRiportaIndietroButtonEnabled() 
	{
		AccertamentoBulk doc = ((AccertamentoBulk)getModel());
		
		return !isRiportaIndietroButtonHidden() &&
					(isEditing() || isScadenzaModificabile()) &&
					!isDirty() &&
					doc != null &&
					((AccertamentoBulk)doc).isDocRiportato();
	}
	/**
	 * Metodo per selezionare la scadenza dell'accertamento.
	 * @param scadenza La scadenza dell'accertamento
	 * @param context Il contesto dell'azione
	 */
	public void selezionaScadenza(Accertamento_scadenzarioBulk scadenza, it.cnr.jada.action.ActionContext context) {
		super.selezionaScadenza(scadenza, context);
		setStatusAndEditableMap();
	}
	public boolean isAnnullaScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException 
	{
		return ( getScadenzario().getModel() != null && !isEditingScadenza() && isEditable() && ((AccertamentoBulk)getModel()).getDt_cancellazione() == null
				&& ((AccertamentoBulk)getModel()).isAccertamentoResiduo() && isScadenzaAzzerabile());
	}
	/**
	 * La scadenza  azzerabile per creazione di movimento di variazione (accertamento_modifica) se:
	 * - se usato in doc amm. e questo  di tipo generico attivo
	 * - l'esercizio del doc amm  inferiore a quello dell'accertamento
	 * - il doc. amm. non  stato inserito in reversale
	 * @return
	 */
	public boolean isScadenzaAzzerabile() {
		Accertamento_scadenzarioBulk as = ((Accertamento_scadenzarioBulk)getScadenzario().getModel());
		if (as == null)
			return false;
		if (as.getCd_tipo_documento_amm()!=null && !as.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_E))
			return false;
		if (as.getEsercizio_doc_attivo()!=null && !(as.getEsercizio_doc_attivo().compareTo(as.getEsercizio())<0))
			return false;
		if (as.getPg_reversale()!=null)
			return false;
		return true;
	}
	/**
	 * La scadenza  modificabile per creazione di movimento di variazione (accertamento_modifica) se:
	 * - se non usato in doc amm.
	 * @return
	 */
	public boolean isScadenzaModificabilePerVariazione() {

		// Se provengo da BP dei doc amm deve essere modificabile sempre
		if ( IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass()))
			return true;

		Accertamento_scadenzarioBulk as = ((Accertamento_scadenzarioBulk)getScadenzario().getModel());
		if (as == null)
			return false;
		if (as.getPg_doc_attivo()!=null)
			return false;
		return true;
	}
	
	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		super.initialize(actioncontext);
		Configurazione_cnrComponentSession confCNR = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		try {
			String mesegiorno = confCNR.getVal01(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext()), 
					"DATA", "RIACCERTAMENTO_RESIDUI", "STATO");
			if (mesegiorno != null)
				dataVisibilitaStato = new SimpleDateFormat("dd/MM/yyyy").parse(mesegiorno + "/" + CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} catch (ParseException e) {
			throw new BusinessProcessException(e);		
		}		
	}
	
	public boolean isStatoVisibile() {
		if (dataVisibilitaStato == null)
			return false;
		boolean statoVisible = EJBCommonServices.getServerDate().after(dataVisibilitaStato);
		if (statoVisible) {
			if (this.isSearching())
				return true;
			else if (this.getModel()!=null && this.getModel() instanceof AccertamentoResiduoBulk) {
				if (((AccertamentoResiduoBulk)this.getModel()).getStato()!=null)
					return true;
				if (((AccertamentoResiduoBulk)this.getModel()).getImportoNonIncassato().compareTo(BigDecimal.ZERO)==0)
					return false;
			}
		}
		return statoVisible;
	}
	
	@Override
	protected String getStorePath(AccertamentoBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegatoParentBulk.getUnita_organizzativa())
						.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
						.orElse(""),
				"Riaccertamento dei residui",
				Optional.ofNullable(allegatoParentBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				allegatoParentBulk.getCd_uo_origine() + "-" + allegatoParentBulk.getEsercizio_originale() + allegatoParentBulk.getPg_accertamento()
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public String [][] getTabs() {
		TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
		int i=0;
		pages.put(i++, new String[]{ "tabAccertamento","Accertamento","/doccont00/tab_accertamento.jsp" });
		pages.put(i++, new String[]{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_accertamento.jsp" });
		pages.put(i++, new String[]{ "tabScadenziario","Scadenziario","/doccont00/tab_scadenziario_accertamento.jsp" });
		if (isStatoVisibile())
			pages.put(i++, new String[]{ "tabAllegati","Allegati","/util00/tab_allegati.jsp" });
		if (getModel() != null) {
			AccertamentoResiduoBulk doc = ((AccertamentoResiduoBulk)getModel());
			if (doc.isStatoInesigibile() || doc.isStatoParzialmenteInesigibile() || doc.isStatoDubbio() || doc.isStatoGiudizialmenteControverso() ||
					(doc.getPdgVincoliColl()!=null && !doc.getPdgVincoliColl().isEmpty()))
				pages.put(i++, new String[]{ "tabVincoli","Spese Vincolate","/doccont00/tab_accertamento_vincoli.jsp" });
		}
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++)
			tabs[j]=new String[]{pages.get(j)[0],pages.get(j)[1],pages.get(j)[2]};
		return tabs;
	}
	@Override
	public void validate(ActionContext context) throws ValidationException {
		super.validate(context);
		if (getModel() != null) {
			AccertamentoResiduoBulk doc = ((AccertamentoResiduoBulk)getModel());
			if (doc.isStatoDilazionato() || doc.isStatoIncerto() || doc.isStatoDubbio() || doc.isStatoGiudizialmenteControverso() ||
				doc.isStatoInesigibile() || doc.isStatoParzialmenteInesigibile()) {
				doc.getArchivioAllegati().stream()
					.map(AllegatoAccertamentoBulk.class::cast)
					.filter(e->e.isNew() || e.getEsercizioDiAppartenenza().equals(CNRUserContext.getEsercizio(context.getUserContext())))
					.findAny()
					.orElseThrow(()->new ValidationException("Inserire almeno un allegato per l'esercizio "+CNRUserContext.getEsercizio(context.getUserContext())+"!"));
			}
		}
	}

    public String getAllegatiFormName() {
    	if (this.getCrudArchivioAllegati().getModel()!=null && !this.getCrudArchivioAllegati().getModel().isNew())
    		if (!isPossibileModifica((AllegatoGenericoBulk)this.getCrudArchivioAllegati().getModel()))
    			return "readonly";
    	return "archivioAllegati";
    }
}
