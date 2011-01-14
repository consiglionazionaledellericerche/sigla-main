package it.cnr.contab.doccont00.bp;

import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoResiduoComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Accertamento Residuo.
 */
public class CRUDAccertamentoResiduoBP extends CRUDAccertamentoBP {

	boolean scadenzaModificabile = false;
	boolean scadenzaModificata = false;
	boolean statusOriginarioSaveButtonEnabled = false;
	boolean isEditableOriginario = false;
	boolean isContrattoEsistente = false;

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
			OggettoBulk oggettobulk = super.initializeModelForEdit(context, bulk);
		
			((AccertamentoBulk)oggettobulk).caricaAnniResidui(context);

			return oggettobulk;
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
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
			if (getTab( "tab" )!=null && getTab( "tab" ).equalsIgnoreCase("tabScadenziario") && isScadenzaModificabile() && !((AccertamentoBulk)getModel()).isDocRiportato()) 
				setStatusAndEditableMap(EDIT);
			else
				setStatusAndEditableMap(VIEW);
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
	 * La scadenza è azzerabile per creazione di movimento di variazione (accertamento_modifica) se:
	 * - se usato in doc amm. e questo è di tipo generico attivo
	 * - l'esercizio del doc amm è inferiore a quello dell'accertamento
	 * - il doc. amm. non è stato inserito in reversale
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
	 * La scadenza è modificabile per creazione di movimento di variazione (accertamento_modifica) se:
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
}
