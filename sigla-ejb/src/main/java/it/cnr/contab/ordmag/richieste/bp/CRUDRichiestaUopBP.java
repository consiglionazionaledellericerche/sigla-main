package it.cnr.contab.ordmag.richieste.bp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.SecondaryType;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaDettaglioBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.ejb.RichiestaUopComponentSession;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.upload.UploadedFile;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDRichiestaUopBP 
extends AllegatiCRUDBP<AllegatoRichiestaBulk, RichiestaUopBulk>  {

	public boolean isInputReadonly() 
	{
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		if(richiesta == null)
			return super.isInputReadonly();
		return 	super.isInputReadonly() || (richiesta.getStato() != null && !richiesta.isInserita());
	}

	private final SimpleDetailCRUDController righe= new RichiestaUopRigaCRUDController("Righe", RichiestaUopRigaBulk.class, "righeRichiestaColl", this){
		@Override
		public OggettoBulk removeDetail(int i) {
			List list = getDetails();
			RichiestaUopRigaBulk dettaglio =(RichiestaUopRigaBulk)list.get(i);
			BulkList<AllegatoRichiestaDettaglioBulk> listaDettagliAllegati = dettaglio.getDettaglioAllegati();
			if (listaDettagliAllegati != null && !listaDettagliAllegati.isEmpty()){
				int k;
				for ( k = 0; k < listaDettagliAllegati.size(); k++ ){
					AllegatoRichiestaDettaglioBulk all = listaDettagliAllegati.get(k);
					all.setToBeDeleted();
				}
			}
			return super.removeDetail(i);
		}

	};

	private final SimpleDetailCRUDController dettaglioAllegatiController = new SimpleDetailCRUDController("AllegatiDettaglio", AllegatoRichiestaDettaglioBulk.class,"dettaglioAllegati",righe)
	{
		@Override
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			AllegatoRichiestaDettaglioBulk allegato = (AllegatoRichiestaDettaglioBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.Righe.AllegatiDettaglio.file");
			if (!(file == null || file.getName().equals(""))) {
				allegato.setFile(file.getFile());
				allegato.setContentType(file.getContentType());
				allegato.setNome(allegato.parseFilename(file.getName()));
				allegato.setAspectName(RichiesteCMISService.ASPECT_RICHIESTA_ORDINI_DETTAGLIO);
				allegato.setToBeUpdated();
				getParentController().setDirty(true);
			}
			oggettobulk.validate();		
			super.validate(actioncontext, oggettobulk);
		}
		@Override
		public OggettoBulk removeDetail(int i) {
			if (!getModel().isNew()){	
				List list = getDetails();
				AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)list.get(i);
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
			AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)oggettobulk;
			all.setIsDetailAdded(true);
			return add;
		}
	};	


	private RichiesteCMISService richiesteCMISService;
	public CRUDRichiestaUopBP() {
		super();

		setTab();

		//	dettaglioObbligazioneController = new RichiestaUopRigaCRUDController("DettaglioObbligazioni",Documento_generico_rigaBulk.class,"documento_generico_obbligazioniHash",obbligazioniController) {
		//
		//			public java.util.List getDetails() {
		//
		//				RichiestaUopBulk doc= (RichiestaUopBulk)CRUDRichiestaUopBP.this.getModel();
		//				java.util.Vector lista = new java.util.Vector();
		//				if (doc != null) {
		//					java.util.Hashtable h = doc.getDocumento_generico_obbligazioniHash();
		//					if (h != null && getParentModel() != null)
		//						lista = (java.util.Vector)h.get(getParentModel());
		//				}
		//				return lista;
		//			}
		//		};
		//	

	}
	protected void setTab() {
		setTab("tab","tabRichiestaUop");
		setTab("tabRichiestaUopDettaglio","tabRichiestaDettaglio");
	}
	public CRUDRichiestaUopBP(String function) throws BusinessProcessException{
		super(function+"Tr");

		setTab();

	}

	public void create(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try { 
			getModel().setToBeCreated();
			setModel(
					context,
					((RichiestaUopComponentSession)createComponentSession()).creaConBulk(
							context.getUserContext(),
							getModel()));
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public final SimpleDetailCRUDController getRighe() {
		return righe;
	}
	/**
	 * Imposta il valore della proprietà 'userConfirm'
	 *
	 * @param newUserConfirm	Il valore da assegnare a 'userConfirm'
	 */
	public void update(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try {
			getModel().setToBeUpdated();
			setModel(
					context,
					((RichiestaUopComponentSession)createComponentSession()).modificaConBulk(
							context.getUserContext(),
							getModel()));
			archiviaAllegati(context, null);
			archiviaAllegatiDettaglio();
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	private void archiviaAllegatiDettaglio() throws ApplicationException, BusinessProcessException {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		for (Object oggetto : richiesta.getRigheRichiestaColl()) {
			RichiestaUopRigaBulk dettaglio = (RichiestaUopRigaBulk)oggetto;
			for (AllegatoRichiestaDettaglioBulk allegato : dettaglio.getDettaglioAllegati()) {
				if (allegato.isToBeCreated()){
					try {
						Document node = cmisService.storeSimpleDocument(allegato, 
								new FileInputStream(allegato.getFile()),
								allegato.getContentType(),
								allegato.getNome(), getCMISPathDettaglio(dettaglio, true));
						allegato.setCrudStatus(OggettoBulk.NORMAL);
					} catch (FileNotFoundException e) {
						throw handleException(e);
					} catch(CmisContentAlreadyExistsException _ex) {
						throw new ApplicationException("Il file " + allegato.getNome() +" già esiste!");
					}
				}else if (allegato.isToBeUpdated()) {
					try {
						if (allegato.getFile() != null)
							//							if (!isDocumentoDettaglioProvenienteDaGemis(allegato)){
							cmisService.updateContent(allegato.getDocument(cmisService).getId(), 
									new FileInputStream(allegato.getFile()),
									allegato.getContentType());
						cmisService.updateProperties(allegato, allegato.getDocument(cmisService));
						allegato.setCrudStatus(OggettoBulk.NORMAL);
						//							} else {
						//								throw new ApplicationException("Aggiornamento non possibile! Documento proveniente dalla procedura Missioni");
						//							}
					} catch (FileNotFoundException e) {
						throw handleException(e);
					}
				}
			}
			for (Iterator<AllegatoRichiestaDettaglioBulk> iterator = dettaglio.getDettaglioAllegati().deleteIterator(); iterator.hasNext();) {
				AllegatoRichiestaDettaglioBulk allegato = iterator.next();
				if (allegato.isToBeDeleted()){
					cmisService.deleteNode(allegato.getDocument(cmisService));
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				}
			}
		}

		for (Iterator<RichiestaUopRigaBulk> iterator = richiesta.getRigheRichiestaColl().deleteIterator(); iterator.hasNext();) {
			RichiestaUopRigaBulk dettaglio = iterator.next();
			Folder folder = null;
			for (Iterator<AllegatoRichiestaDettaglioBulk> iteratorAll = dettaglio.getDettaglioAllegati().iterator(); iteratorAll.hasNext();) {
				AllegatoRichiestaDettaglioBulk allegato = iteratorAll.next();
				if (allegato.isToBeDeleted()){
					Document doc = allegato.getDocument(cmisService);
					List<Folder> list = doc.getParents();
					for (Iterator<Folder> iteratorFolder = list.iterator(); iteratorFolder.hasNext();) {
						folder = iteratorFolder.next();
					}
					cmisService.deleteNode(doc);
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				}
			}
			if (folder != null){
				cmisService.deleteNode(folder);
			}
		}
	}
	@Override
	protected CMISPath getCMISPath(RichiestaUopBulk allegatoParentBulk, boolean create) throws BusinessProcessException{
		return richiesteCMISService.getCMISPath(allegatoParentBulk, create);
	}

	protected CMISPath getCMISPathDettaglio(RichiestaUopRigaBulk dettaglioBulk, boolean create) throws BusinessProcessException{
		try {
			CMISPath cmisPath = null;
			cmisPath = SpringUtil.getBean("cmisPathRichieste",CMISPath.class);

			if (create) {
				cmisPath = richiesteCMISService.createFolderIfNotPresent(cmisPath, dettaglioBulk.getCdUnitaOperativa(), dettaglioBulk.getCdUnitaOperativa(), dettaglioBulk.getCdUnitaOperativa());
				cmisPath = richiesteCMISService.createFolderIfNotPresent(cmisPath, dettaglioBulk.getCdNumeratore(), dettaglioBulk.getCdNumeratore(), dettaglioBulk.getCdNumeratore());
				cmisPath = richiesteCMISService.createFolderIfNotPresent(cmisPath, "Anno "+dettaglioBulk.getEsercizio().toString(), "Anno "+dettaglioBulk.getEsercizio().toString(), "Anno "+dettaglioBulk.getEsercizio().toString());
				cmisPath = richiesteCMISService.createFolderRichiestaIfNotPresent(cmisPath, dettaglioBulk.getRichiestaUop());
				cmisPath = richiesteCMISService.createFolderDettaglioIfNotPresent(cmisPath, dettaglioBulk);
			} else {
				try {
					richiesteCMISService.getNodeByPath(cmisPath);
				} catch (CmisObjectNotFoundException _ex) {
					return null;
				}
			}			
			return cmisPath;
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		}
	}
	@Override
	protected Class<AllegatoRichiestaBulk> getAllegatoClass() {
		return AllegatoRichiestaBulk.class;
	}
	public RichiesteCMISService getRichiesteCMISService() {
		return richiesteCMISService;
	}
	public void setRichiesteCMISService(RichiesteCMISService richiesteCMISService) {
		this.richiesteCMISService = richiesteCMISService;
	}
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		richiesteCMISService = SpringUtil.getBean("richiesteCMISService",
				RichiesteCMISService.class);	
	}
	@Override
	public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {

		RichiestaUopBulk allegatoParentBulk = (RichiestaUopBulk)oggettobulk;
		try {
			ItemIterable<CmisObject> files = richiesteCMISService.getFilesRichiesta(allegatoParentBulk);
			if (files != null){
				for (CmisObject cmisObject : files) {
					if (richiesteCMISService.hasAspect(cmisObject, CMISAspect.SYS_ARCHIVED.value()))
						continue;
					if (richiesteCMISService.hasAspect(cmisObject, RichiesteCMISService.ASPECT_STAMPA_RICHIESTA_ORDINI))
						continue;
					if (excludeChild(cmisObject))
						continue;

					richiesteCMISService.recuperoAllegatiDettaglioRichiesta(allegatoParentBulk, cmisObject);

					if (cmisObject.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) {
						Document document = (Document) cmisObject;
						AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk) Introspector.newInstance(getAllegatoClass(), document);
						allegato.setContentType(document.getContentStreamMimeType());
						allegato.setNome(cmisObject.getName());
						allegato.setDescrizione((String)document.getPropertyValue(SiglaCMISService.PROPERTY_DESCRIPTION));
						allegato.setTitolo((String)document.getPropertyValue(SiglaCMISService.PROPERTY_TITLE));
						completeAllegato(allegato);
						allegato.setCrudStatus(OggettoBulk.NORMAL);
						allegatoParentBulk.addToArchivioAllegati(allegato);					
					}
				}
			}
		} catch (ApplicationException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (NoSuchMethodException e) {
			throw handleException(e);
		} catch (IllegalAccessException e) {
			throw handleException(e);
		} catch (InstantiationException e) {
			throw handleException(e);
		} catch (InvocationTargetException e) {
			throw handleException(e); 
		}
		return oggettobulk;	
	}
	@Override
	protected void completeAllegato(AllegatoRichiestaBulk allegato) throws ApplicationException {
		for (SecondaryType secondaryType : allegato.getDocument(richiesteCMISService).getSecondaryTypes()) {
			if (AllegatoRichiestaBulk.aspectNamesKeys.get(secondaryType.getId()) != null){
				allegato.setAspectName(secondaryType.getId());
				break;
			}
		}
		super.completeAllegato(allegato);
	}

	@Override
	public String getAllegatiFormName() {
		super.getAllegatiFormName();
		return "allegatiRichiesta";
	}
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk)getCrudArchivioAllegati().getModel();
		Document node = allegato.getDocument(richiesteCMISService);
		InputStream is = richiesteCMISService.getResource(node);
		((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(node.getContentStreamLength()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(node.getContentStreamMimeType());
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
	public String getNomeAllegatoDettaglio() throws ApplicationException{
		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
		if (dettaglio!= null){
			return dettaglio.getNome();
		}
		return "";
	}
	public void scaricaDocumentoDettaglioCollegato(ActionContext actioncontext) throws Exception {
		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
		if (dettaglio!= null){
			Document document = dettaglio.getDocument(richiesteCMISService);
			if (document != null){
				InputStream is = richiesteCMISService.getResource(document);
				((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(document.getContentStreamLength()).intValue());
				((HttpActionContext)actioncontext).getResponse().setContentType(document.getContentStreamMimeType());
				OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
				((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
				byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
				int buflength;
				while ((buflength = is.read(buffer)) > 0) {
					os.write(buffer,0,buflength);
				}
				is.close();
				os.flush();
			} else {
				throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
			}
		} else {
			throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
		}
	}
	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		return true;
	}
	protected Boolean isPossibileCancellazioneDettaglioAllegato(AllegatoGenericoBulk allegato) {
		return true;
	}
	public SimpleDetailCRUDController getDettaglioAllegatiController() {
		return dettaglioAllegatiController;
	}
	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato){
		return true;
	}
	@Override
	protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)allegatoParentBulk;
		super.gestioneCancellazioneAllegati(allegatoParentBulk);
	}
	public void gestionePostSalvataggio(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try {
			RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel(); 
			((RichiestaUopComponentSession)createComponentSession()).gestioneStampaRichiesta(context.getUserContext(), richiesta);
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public void stampaRichiesta(ActionContext actioncontext) throws Exception {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		InputStream is = richiesteCMISService.getStreamRichiesta(richiesta);
		if (is != null){
			((HttpActionContext)actioncontext).getResponse().setContentType("application/pdf");
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
	}
	public boolean isStampaRichiestaButtonHidden() {

		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		return (richiesta == null || richiesta.getNumero() == null);
	}

	public boolean isSalvaDefinitivoButtonHidden() {

		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		return (richiesta == null || richiesta.getNumero() == null || !richiesta.isInserita());
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[8];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.salvaDefinitivo");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");

		return toolbar;
	}
}
