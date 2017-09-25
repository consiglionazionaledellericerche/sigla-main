/*
 * Created on Apr 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StorageException;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.servlet.ServletException;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigAnagContrattoBP extends SimpleCRUDBP {

	private static final long serialVersionUID = 1L;

	private ContrattoBulk contratto;
	private String tipoAccesso;
	protected ContrattoService contrattoService;
	protected Date dataStipulaParametri;
	protected Boolean flagPubblicaContratto;
	private SimpleDetailCRUDController crudAssUO = new SimpleDetailCRUDController( "Associazione UO", Ass_contratto_uoBulk.class, "associazioneUO", this);
	private SimpleDetailCRUDController crudAssUODisponibili = new SimpleDetailCRUDController( "Associazione UO Disponibili", Unita_organizzativaBulk.class, "associazioneUODisponibili", this);

	@SuppressWarnings("serial")
	private SimpleDetailCRUDController crudArchivioAllegati = new SimpleDetailCRUDController( "ArchivioAllegati", AllegatoContrattoDocumentBulk.class, "archivioAllegati", this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.ArchivioAllegati.file");
			if ( allegato.getType() == null )
				throw new ValidationException("Attenzione: valorizzare il Tipo.");
			if ( allegato.getNome() == null ) {
				if ((file == null || file.getName().equals("")) && !allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO))
					throw new ValidationException("Attenzione: selezionare un File da caricare.");
				if (((file == null || file.getName().equals("")) && allegato.getLink() == null) && 
						allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO))
					throw new ValidationException("Attenzione: selezionare un File da caricare oppure valorizzare il Link al Progetto.");
			}else{
				if ((!allegato.isContentStreamPresent() && (allegato.getLink() == null&&allegato.getFile() == null)) && 
						allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO))
					throw new ValidationException("Attenzione: selezionare un File da caricare oppure valorizzare il Link al Progetto.");
			}
			
			if (!(file == null || file.getName().equals(""))) {
				allegato.setFile(file.getFile());
				allegato.setContentType(file.getContentType());
				allegato.setNome(file.getName());
			}
			if (allegato.isContentStreamPresent())
				allegato.setToBeUpdated();
			getParentController().setDirty(true);
			super.validate(actioncontext, oggettobulk);
		}

		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			super.validateForDelete(actioncontext, oggettobulk);
		}
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			return super.removeDetail(oggettobulk, i);
		}
		public boolean isShrinkable() {
			return super.isShrinkable() && isAllegatiEnabled();
		};
		public boolean isGrowable() {
			return super.isGrowable();// && isAllegatiEnabled();			
		};
	};
	
	public CRUDConfigAnagContrattoBP()
	{
		super();
	}

	public CRUDConfigAnagContrattoBP(String s)
	{
		super(s);
	}

	public CRUDConfigAnagContrattoBP(String function, ContrattoBulk contratto, String tipoAccesso) {
		super(function);
		setContratto(contratto);
		setTipoAccesso(tipoAccesso);
	}

	public boolean isPublishHidden(){
		if (isSearching() || isInserting())
			return false;
		if (getModel()!=null){
			ContrattoBulk contratto = (ContrattoBulk) getModel();
			if (contratto.isProvvisorio())
				return false;
			if (contratto.isCessato())
				return true;
		}
		return isPublishCRUDButtonHidden();
	}

	public boolean isPublishCRUDButtonHidden(){
		/*if (isSearching())
			return true;		
		if (getModel()!=null){
			ContrattoBulk contratto = (ContrattoBulk) getModel();
			if ((contratto.isPassivo() || contratto.isAttivo_e_Passivo()) &&
					contratto.isDefinitivo() && flagPubblicaContratto.booleanValue() &&
					(!contratto.getDt_stipula().before(dataStipulaParametri)) &&
					!contratto.getFl_pubblica_contratto()
				&& (contratto.getTipo_contratto() != null && 
				    contratto.getTipo_contratto().getFl_pubblica_contratto() != null  &&
				   contratto.getTipo_contratto().getFl_pubblica_contratto().booleanValue())) 
				return false;
		}*/
		return true;
	}
	
	public boolean isAllegatiEnabled(){
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		if (isEditing() && 
				contratto != null && 
				contratto.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL){
			if (contratto.isDefinitivo()){
				if (flagPubblicaContratto.booleanValue()){
				   if (contratto.getFl_pubblica_contratto() == null || contratto.getFl_pubblica_contratto())
					   return false;
				   else			
					  return (!contratto.getDt_stipula().before(dataStipulaParametri));
				}else
					return false;
			}
		}
		return true;
	}
	
	public boolean isContrattoDefinitivo(){
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		if (contratto != null)
			return contratto.isRODefinitivo();
		return false;
	}
	
	private it.cnr.contab.config00.bulk.Parametri_cnrBulk getParametri_cnrBulk(it.cnr.jada.action.ActionContext context) throws ComponentException, RemoteException, EJBException{
		return Utility.createParametriCnrComponentSession().
			getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext()));
	}
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);
		ContrattoBulk contratto= (ContrattoBulk)getModel();
		try {
			dataStipulaParametri = getParametri_cnrBulk(context).getData_stipula_contratti();
			flagPubblicaContratto=getParametri_cnrBulk(context).getFl_pubblica_contratto();
		} catch (Exception e) {
			throw handleException(e);
		}
		if (getStatus()!=VIEW){
			if (contratto.getUnita_organizzativa() != null && contratto.getUnita_organizzativa().getCd_unita_organizzativa() != null &&
			    !contratto.getUnita_organizzativa().getCd_unita_organizzativa().equals(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()))){
					setStatus(VIEW);					
					getCrudAssUO().setEnabled(false);		
					getCrudAssUODisponibili().setEnabled(false);	    	
			    }
			else{
				getCrudAssUO().setEnabled(true);  							
				getCrudAssUODisponibili().setEnabled(true);
			}
			if (contratto!=null && contratto.isCancellatoLogicamente()) {
				setStatus(VIEW);
				getCrudAssUODisponibili().setEnabled(false);
			}			
		}else{
			getCrudAssUODisponibili().setEnabled(false);
		}
			getCrudArchivioAllegati().setEnabled(true);
	}	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUO() {
		return crudAssUO;
	}
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		openForm(context,action,target,"multipart/form-data");
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUO(SimpleDetailCRUDController controller) {
		crudAssUO = controller;
	}
	protected void resetTabs(it.cnr.jada.action.ActionContext context) {
		setTab("tab","tabTestata");
	}
	public boolean isVisualizzaDocContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doc_cont_spe()==null)
		  return false;
		else if (contratto.getTot_doc_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDocContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doc_cont_etr()==null)
		  return false;
		else if (contratto.getTot_doc_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	
	public boolean isVisualizzaDocammContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_docamm_cont_etr()==null)
		  return false;
		else if (contratto.getTot_docamm_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDocammContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_docamm_cont_spe()==null)
		  return false;
		else if (contratto.getTot_docamm_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDoccontContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doccont_cont_etr()==null)
		  return false;
		else if (contratto.getTot_doccont_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDoccontContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doccont_cont_spe()==null)
		  return false;
		else if (contratto.getTot_doccont_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaCommessaButtonEnabled(){
		return isVisualizzaDocContSpeButtonEnabled()||isVisualizzaDocContEtrButtonEnabled();
	}	
	/**
	 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
	 * @return toolbar Toolbar in uso
	 */

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[11];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.undoBringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.definitiveSave");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.publish");

		return toolbar;
	}
	/**
	 * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 *
	 * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 */
	public boolean isSalvaDefinitivoButtonEnabled() {

		return isEditing() && 
				getModel() != null && 
				getModel().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				((ContrattoBulk)getModel()).isProvvisorio();
	}
	/**
	 * Gestione del salvataggio definitivo di un contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @throws BusinessProcessException	
	 */
	public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			archiviaAllegati(context, (ContrattoBulk) getModel());
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		try {
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			ContrattoBulk contratto = comp.salvaDefinitivo(context.getUserContext(), (ContrattoBulk)getModel());
			edit(context,contratto);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	public void pubblicaContratto(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		try {
			archiviaAllegati(context, contratto);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		try {
			StorageObject folder = contrattoService.getFolderContratto((ContrattoBulk)getModel());
			if (!contratto.isAllegatoContrattoPresent())
				throw handleException(new ApplicationException("Bisogna allegare il file del Contratto!"));
			contratto.setFl_pubblica_contratto(Boolean.TRUE);
			contratto.setToBeUpdated();
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			comp.modificaConBulk(context.getUserContext(), contratto);
			if (folder != null){
				contrattoService.updateProperties(contratto, folder); 
				contrattoService.addAspect(folder, "P:sigla_contratti_aspect:stato_definitivo");
				contrattoService.addConsumer(folder,"GROUP_CONTRATTI");
				contrattoService.setInheritedPermission(
						contrattoService.getStorageObjectByPath(contrattoService.getCMISPathFolderContratto(contratto)),
						Boolean.FALSE);
			}
			edit(context,contratto);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}

	public void controllaCancellazioneAssociazioneUo(ActionContext context,Ass_contratto_uoBulk ass_contratto_uo) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			comp.controllaCancellazioneAssociazioneUo(context.getUserContext(), ass_contratto_uo);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void initializzaUnita_Organizzativa(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ContrattoBulk contratto = (ContrattoBulk)getModel();
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			setModel(context, comp.initializzaUnita_Organizzativa(context.getUserContext(), (ContrattoBulk)getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUODisponibili() {
		return crudAssUODisponibili;
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUODisponibili(SimpleDetailCRUDController controller) {
		crudAssUODisponibili = controller;
	}
	
	public SimpleDetailCRUDController getCrudArchivioAllegati() {
		return crudArchivioAllegati;
	}

	public void setCrudArchivioAllegati(
			SimpleDetailCRUDController crudArchivioAllegati) {
		this.crudArchivioAllegati = crudArchivioAllegati;
	}

	public boolean isDeleteButtonEnabled()
	{
		return isEditable() && isEditing();
	}
	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		contrattoService = SpringUtil.getBean("contrattoService",
				ContrattoService.class);		
		super.initialize(actioncontext);
		try {
			if (Optional.ofNullable(getTipoAccesso())
					.filter(tipoAccesso -> tipoAccesso.equals("V"))
					.isPresent()) {
				ContrattoBulk contratto = getContratto();
				setModel(actioncontext, contratto);
				cerca(actioncontext);
			}
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public void cerca(ActionContext actioncontext) throws RemoteException, InstantiationException, RemoveException, BusinessProcessException
	{
		try
		{
			fillModel(actioncontext);
			OggettoBulk oggettobulk = getModel();
			RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk);
			if(remoteiterator == null || remoteiterator.countElements() == 0)
			{
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				return;
			}
			if(remoteiterator.countElements() == 1)
			{
				OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				if(oggettobulk1 != null) {
					edit(actioncontext, oggettobulk1);
				}
				return;
			}
			else {
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				//reset(actioncontext);
				setStatus(SEARCH);
			}
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}

	@Override
	public void delete(ActionContext actioncontext)
			throws BusinessProcessException {
		super.delete(actioncontext);
	}
	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		super.update(actioncontext);
		try {
			archiviaAllegati(actioncontext, (ContrattoBulk) getModel());
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		StorageObject folder;
		try {
			folder = contrattoService.getFolderContratto((ContrattoBulk) getModel());
			if (folder != null)
				contrattoService.updateProperties(getModel(), folder);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
	}
	
	@Override
	public void create(ActionContext actioncontext)
			throws BusinessProcessException {
		super.create(actioncontext);
		try {
			archiviaAllegati(actioncontext, (ContrattoBulk) getModel());
		} catch (ApplicationException e) {
			throw handleException(e);
		}
	}

	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		ContrattoBulk contratto = (ContrattoBulk)super.initializeModelForEdit(actioncontext, oggettobulk);
		StorageObject folder;
		try {
			folder = contrattoService.getFolderContratto(contratto);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		if (folder != null){
			List<StorageObject> children = contrattoService.getChildren(folder.getKey());
			for (StorageObject child : children) {
				AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
				allegato.setContentType(child.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
				allegato.setNome(child.getPropertyValue("sigla_contratti_attachment:original_name"));
				allegato.setDescrizione(child.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
				allegato.setTitolo(child.getPropertyValue(StoragePropertyNames.TITLE.value()));
				allegato.setType(child.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
				allegato.setLink((String) child.getPropertyValue("sigla_contratti_aspect_link:url"));
				allegato.setCrudStatus(OggettoBulk.NORMAL);
				contratto.addToArchivioAllegati(allegato);
			}
		}
		return contratto;
	}

	public String getNomeAllegato() throws ApplicationException{
		AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk)getCrudArchivioAllegati().getModel();
		return Optional.ofNullable(allegato)
				.map(AllegatoContrattoDocumentBulk::getNodeId)
				.map(key -> contrattoService.getStorageObjectBykey(key))
				.map(storageObject -> storageObject.<String>getPropertyValue(StoragePropertyNames.NAME.value()))
				.orElse(null);

	}
	
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk)getCrudArchivioAllegati().getModel();
		StorageObject storageObject = contrattoService.getStorageObjectBykey(allegato.getNodeId());
		InputStream is = contrattoService.getResource(storageObject);
		((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
		OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
		((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
		byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
		int buflength;
		while ((buflength = is.read(buffer)) > 0) {
			os.write(buffer,0,buflength);
		}
		is.close();
		os.flush();
	}

	private void archiviaAllegati(ActionContext actioncontext, ContrattoBulk contratto) throws BusinessProcessException, ApplicationException{
		Optional.ofNullable(contrattoService.getStorageObjectByPath(contrattoService.getCMISPathFolderContratto(contratto)))
				.orElseGet(() -> {
					StorageObject parentStorageObject = contrattoService.getStorageObjectByPath(
							contrattoService.getCMISPath(contratto), true
					);
					return contrattoService.getStorageObjectBykey(
							contrattoService.createFolderIfNotPresent(parentStorageObject.getPath(),
									contratto.getCMISFolderName(), null, null,
									contratto
							));
				});
		try {
			crudArchivioAllegati.validate(actioncontext);
		} catch (ValidationException e1) {
			throw handleException(e1);
		}
		for (Iterator<AllegatoContrattoDocumentBulk> iterator = contratto.getArchivioAllegati().deleteIterator(); iterator.hasNext();) {
			AllegatoContrattoDocumentBulk allegato = iterator.next();
			if (allegato.isToBeDeleted()){
				contrattoService.delete(allegato.getNodeId());
				allegato.setCrudStatus(OggettoBulk.NORMAL);
			}
		}
		for (AllegatoContrattoDocumentBulk allegato : contratto.getArchivioAllegati()) {
			if (allegato.isToBeCreated()){
				try {
					StorageObject storageObject = Optional.ofNullable(allegato.getFile())
							.map(file -> {
								try {
									return contrattoService.storeSimpleDocument(
											allegato,
											new FileInputStream(file),
											allegato.getContentType(),
											allegato.getDocumentName(),
											contrattoService.getCMISPath(allegato),
											true);
								} catch (FileNotFoundException e) {
									throw new StorageException(StorageException.Type.GENERIC, e);
								}
							}).orElseGet(() -> {
								return contrattoService.storeSimpleDocument(
										allegato,
										null,
										allegato.getContentType(),
										allegato.getDocumentName(), contrattoService.getCMISPath(allegato),
										true);
							});
					if (contratto.isDefinitivo() && !allegato.getType().equals(AllegatoContrattoDocumentBulk.GENERICO))
						contrattoService.costruisciAlberaturaAlternativa(allegato, storageObject);

					allegato.setCrudStatus(OggettoBulk.NORMAL);
					allegato.setNodeId(storageObject.getKey());
				} catch (StorageException e) {
					if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
						throw new ApplicationException("CMIS - File ["+allegato.getNome()+"] gia' presente. Inserimento non possibile!");
					throw handleException(e);
				}
			}else if (allegato.isToBeUpdated()) {
				try {
					if (allegato.getFile() != null)
						contrattoService.updateStream(allegato.getNodeId(),
								new FileInputStream(allegato.getFile()),
								allegato.getContentType());
					contrattoService.updateProperties(allegato, contrattoService.getStorageObjectBykey(allegato.getNodeId()));
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					throw handleException(e);
				}catch (StorageException e) {
					throw new ApplicationException("CMIS - File ["+allegato.getNome()+"] gia' presente. Inserimento non possibile!");
				}
			}
		}
	}

	public Boolean getFlagPubblicaContratto() {
		return flagPubblicaContratto;
	}

	public void setFlagPubblicaContratto(Boolean flagPubblicaContratto) {
		this.flagPubblicaContratto = flagPubblicaContratto;
	}

	public ContrattoBulk getContratto() {
		return contratto;
	}

	public void setContratto(ContrattoBulk contratto) {
		this.contratto = contratto;
	}

	public String getTipoAccesso() {
		return tipoAccesso;
	}

	public void setTipoAccesso(String tipoAccesso) {
		this.tipoAccesso = tipoAccesso;
	}
}
