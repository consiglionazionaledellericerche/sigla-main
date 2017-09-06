package it.cnr.contab.ordmag.ordini.bp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.SecondaryType;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.ObbligazioniCRUDController;
import it.cnr.contab.docamm00.bp.VoidableBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Voidable;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.contab.ordmag.ordini.service.OrdineAcqCMISService;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDOrdineAcqBP 
extends AllegatiCRUDBP<AllegatoRichiestaBulk, OrdineAcqBulk> implements IDocumentoAmministrativoBP, VoidableBP, IDefferedUpdateSaldiBP  {

	private final SimpleDetailCRUDController dettaglioObbligazioneController;
	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean carryingThrough = false;
	protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
	private boolean isDeleting = false;
	public boolean isInputReadonly() 
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		if(ordine == null)
			return super.isInputReadonly();
		return 	super.isInputReadonly() || !ordine.isStatoInserito() ;
	}

	private final SimpleDetailCRUDController righe= new OrdineAcqRigaCRUDController("Righe", OrdineAcqRigaBulk.class, "righeOrdineColl", this){
		@Override
		public OggettoBulk removeDetail(int i) {
			List list = getDetails();
			OrdineAcqRigaBulk dettaglio =(OrdineAcqRigaBulk)list.get(i);
//			BulkList<AllegatoRichiestaDettaglioBulk> listaDettagliAllegati = dettaglio.getDettaglioAllegati();
//			if (listaDettagliAllegati != null && !listaDettagliAllegati.isEmpty()){
//				int k;
//				for ( k = 0; k < listaDettagliAllegati.size(); k++ ){
//					AllegatoRichiestaDettaglioBulk all = listaDettagliAllegati.get(k);
//					all.setToBeDeleted();
//				}
//			}
			return super.removeDetail(i);
		}

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int index = super.addDetail(oggettobulk);
//			OrdineAcqConsegnaBulk consegna = new OrdineAcqConsegnaBulk();
//			getConsegne().addDetail(consegna);
//			OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)oggettobulk;
//			riga.getRigheConsegnaColl().add(consegna);
			return index;
		}

	};

	private final SimpleDetailCRUDController consegne = new SimpleDetailCRUDController("Consegne",OrdineAcqConsegnaBulk.class,"righeConsegnaColl",righe){

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int index = super.addDetail(oggettobulk);
			return index;
		}
		
	};

	private final ObbligazioniCRUDController obbligazioniController = new ObbligazioniCRUDController(
			"Obbligazioni",
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class,
			"ordineObbligazioniHash", this);

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getConsegne() {
		return consegne;
	}
	
	
	//	private final SimpleDetailCRUDController dettaglioAllegatiController = new SimpleDetailCRUDController("AllegatiDettaglio", AllegatoRichiestaDettaglioBulk.class,"dettaglioAllegati",righe)
//	{
//		@Override
//		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
//			AllegatoRichiestaDettaglioBulk allegato = (AllegatoRichiestaDettaglioBulk)oggettobulk;
//			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.Righe.AllegatiDettaglio.file");
//			if (!(file == null || file.getName().equals(""))) {
//				allegato.setFile(file.getFile());
//				allegato.setContentType(file.getContentType());
//				allegato.setNome(allegato.parseFilename(file.getName()));
//				allegato.setAspectName(RichiesteCMISService.ASPECT_RICHIESTA_ORDINI_DETTAGLIO);
//				allegato.setToBeUpdated();
//				getParentController().setDirty(true);
//			}
//			oggettobulk.validate();		
//			super.validate(actioncontext, oggettobulk);
//		}
//		@Override
//		public OggettoBulk removeDetail(int i) {
//			if (!getModel().isNew()){	
//				List list = getDetails();
//				AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)list.get(i);
//				if (isPossibileCancellazioneDettaglioAllegato(all)) {
//					return super.removeDetail(i);
//				} else {
//					return null;
//				}
//			}
//			return super.removeDetail(i);
//		}
//		@Override
//		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
//			int add = super.addDetail(oggettobulk);
//			AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)oggettobulk;
//			all.setIsDetailAdded(true);
//			return add;
//		}
//	};	
//

	private OrdineAcqCMISService ordineAcqCMISService;

	public CRUDOrdineAcqBP() {
		this(OrdineAcqRigaBulk.class);
	}
	protected void setTab() {
		setTab("tab","tabOrdineAcq");
		setTab("tabOrdineAcqDettaglio","tabOrdineDettaglio");
	}

	public CRUDOrdineAcqBP(Class dettObbligazioniControllerClass) {
		super("Tr");

		setTab();
		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", dettObbligazioniControllerClass,
				"ordineObbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				OrdineAcqBulk ordine = (OrdineAcqBulk) CRUDOrdineAcqBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (ordine != null) {
					java.util.Hashtable h = ordine
							.getOrdineObbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			public boolean isGrowable() {

				return super.isGrowable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}

			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};

	}

	/**
	 * CRUDAnagraficaBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public CRUDOrdineAcqBP(String function)
			throws BusinessProcessException {
		super(function + "Tr");

		setTab();
		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", OrdineAcqRigaBulk.class,
				"ordineObbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				OrdineAcqBulk ordine = (OrdineAcqBulk) CRUDOrdineAcqBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (ordine != null) {
					java.util.Hashtable h = ordine
							.getOrdineObbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			public boolean isGrowable() {

				return super.isGrowable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}

			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};

	}

	
	
	
	public void create(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try { 
			getModel().setToBeCreated();
			setModel(
					context,
					((OrdineAcqComponentSession)createComponentSession()).creaConBulk(
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
					((OrdineAcqComponentSession)createComponentSession()).modificaConBulk(
							context.getUserContext(),
							getModel()));
			archiviaAllegati(context, null);
//			archiviaAllegatiDettaglio();
		} catch(Exception e) {
			throw handleException(e);
		}
	}
//	private void archiviaAllegatiDettaglio() throws ApplicationException, BusinessProcessException {
//		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
//		for (Object oggetto : richiesta.getRigheRichiestaColl()) {
//			RichiestaUopRigaBulk dettaglio = (RichiestaUopRigaBulk)oggetto;
//			for (AllegatoRichiestaDettaglioBulk allegato : dettaglio.getDettaglioAllegati()) {
//				if (allegato.isToBeCreated()){
//					try {
//						Document node = cmisService.storeSimpleDocument(allegato, 
//								new FileInputStream(allegato.getFile()),
//								allegato.getContentType(),
//								allegato.getNome(), getCMISPathDettaglio(dettaglio, true));
//						allegato.setCrudStatus(OggettoBulk.NORMAL);
//					} catch (FileNotFoundException e) {
//						throw handleException(e);
//					} catch(CmisContentAlreadyExistsException _ex) {
//						throw new ApplicationException("Il file " + allegato.getNome() +" già esiste!");
//					}
//				}else if (allegato.isToBeUpdated()) {
//					try {
//						if (allegato.getFile() != null)
//							//							if (!isDocumentoDettaglioProvenienteDaGemis(allegato)){
//							cmisService.updateContent(allegato.getDocument(cmisService).getId(), 
//									new FileInputStream(allegato.getFile()),
//									allegato.getContentType());
//						cmisService.updateProperties(allegato, allegato.getDocument(cmisService));
//						allegato.setCrudStatus(OggettoBulk.NORMAL);
//						//							} else {
//						//								throw new ApplicationException("Aggiornamento non possibile! Documento proveniente dalla procedura Missioni");
//						//							}
//					} catch (FileNotFoundException e) {
//						throw handleException(e);
//					}
//				}
//			}
//			for (Iterator<AllegatoRichiestaDettaglioBulk> iterator = dettaglio.getDettaglioAllegati().deleteIterator(); iterator.hasNext();) {
//				AllegatoRichiestaDettaglioBulk allegato = iterator.next();
//				if (allegato.isToBeDeleted()){
//					cmisService.deleteNode(allegato.getDocument(cmisService));
//					allegato.setCrudStatus(OggettoBulk.NORMAL);
//				}
//			}
//		}
//
//		for (Iterator<RichiestaUopRigaBulk> iterator = richiesta.getRigheRichiestaColl().deleteIterator(); iterator.hasNext();) {
//			RichiestaUopRigaBulk dettaglio = iterator.next();
//			Folder folder = null;
//			for (Iterator<AllegatoRichiestaDettaglioBulk> iteratorAll = dettaglio.getDettaglioAllegati().iterator(); iteratorAll.hasNext();) {
//				AllegatoRichiestaDettaglioBulk allegato = iteratorAll.next();
//				if (allegato.isToBeDeleted()){
//					Document doc = allegato.getDocument(cmisService);
//					List<Folder> list = doc.getParents();
//					for (Iterator<Folder> iteratorFolder = list.iterator(); iteratorFolder.hasNext();) {
//						folder = iteratorFolder.next();
//					}
//					cmisService.deleteNode(doc);
//					allegato.setCrudStatus(OggettoBulk.NORMAL);
//				}
//			}
//			if (folder != null){
//				cmisService.deleteNode(folder);
//			}
//		}
//	}
	@Override
	protected CMISPath getCMISPath(OrdineAcqBulk allegatoParentBulk, boolean create) throws BusinessProcessException{
		return ordineAcqCMISService.getCMISPath(allegatoParentBulk, create);
	}

//	protected CMISPath getCMISPathDettaglio(OrdineAcqRigaBulk dettaglioBulk, boolean create) throws BusinessProcessException{
//		try {
//			CMISPath cmisPath = null;
//			cmisPath = SpringUtil.getBean("cmisPathRichieste",CMISPath.class);
//
//			if (create) {
//				cmisPath = ordineAcqCMISService.createFolderIfNotPresent(cmisPath, dettaglioBulk.getCdUnitaOperativa(), dettaglioBulk.getCdUnitaOperativa(), dettaglioBulk.getCdUnitaOperativa());
//				cmisPath = ordineAcqCMISService.createFolderIfNotPresent(cmisPath, dettaglioBulk.getCdNumeratore(), dettaglioBulk.getCdNumeratore(), dettaglioBulk.getCdNumeratore());
//				cmisPath = ordineAcqCMISService.createFolderIfNotPresent(cmisPath, "Anno "+dettaglioBulk.getEsercizio().toString(), "Anno "+dettaglioBulk.getEsercizio().toString(), "Anno "+dettaglioBulk.getEsercizio().toString());
//				cmisPath = ordineAcqCMISService.createFolderRichiestaIfNotPresent(cmisPath, dettaglioBulk.getOrdineAcq());
//				cmisPath = ordineAcqCMISService.createFolderDettaglioIfNotPresent(cmisPath, dettaglioBulk);
//			} else {
//				try {
//					ordineAcqCMISService.getNodeByPath(cmisPath);
//				} catch (CmisObjectNotFoundException _ex) {
//					return null;
//				}
//			}			
//			return cmisPath;
//		} catch (ComponentException e) {
//			throw new BusinessProcessException(e);
//		}
//	}
	@Override
	protected Class<AllegatoRichiestaBulk> getAllegatoClass() {
		return AllegatoRichiestaBulk.class;
	}
	public OrdineAcqCMISService getOrdineAcqCMISService() {
		return ordineAcqCMISService;
	}
	public void setRichiesteCMISService(OrdineAcqCMISService ordineAcqCMISService) {
		this.ordineAcqCMISService = ordineAcqCMISService;
	}
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		ordineAcqCMISService = SpringUtil.getBean("ordineAcqCMISService",
				OrdineAcqCMISService.class);	
	}
	@Override
	public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {

		OrdineAcqBulk allegatoParentBulk = (OrdineAcqBulk)oggettobulk;
//		try {
//			ItemIterable<CmisObject> files = ordineAcqCMISService.getFilesOrdine(allegatoParentBulk);
//			if (files != null){
//				for (CmisObject cmisObject : files) {
//					if (ordineAcqCMISService.hasAspect(cmisObject, CMISAspect.SYS_ARCHIVED.value()))
//						continue;
//					if (ordineAcqCMISService.hasAspect(cmisObject, RichiesteCMISService.ASPECT_STAMPA_RICHIESTA_ORDINI))
//						continue;
//					if (excludeChild(cmisObject))
//						continue;
//
////					ordineAcqCMISService.recuperoAllegatiDettaglioRichiesta(allegatoParentBulk, cmisObject);
//
//					if (cmisObject.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) {
//						Document document = (Document) cmisObject;
//						AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk) Introspector.newInstance(getAllegatoClass(), document);
//						allegato.setContentType(document.getContentStreamMimeType());
//						allegato.setNome(cmisObject.getName());
//						allegato.setDescrizione((String)document.getPropertyValue(SiglaCMISService.PROPERTY_DESCRIPTION));
//						allegato.setTitolo((String)document.getPropertyValue(SiglaCMISService.PROPERTY_TITLE));
//						completeAllegato(allegato);
//						allegato.setCrudStatus(OggettoBulk.NORMAL);
//						allegatoParentBulk.addToArchivioAllegati(allegato);					
//					}
//				}
//			}
//		} catch (ApplicationException e) {
//			throw handleException(e);
//		} catch (ComponentException e) {
//			throw handleException(e);
//		} catch (NoSuchMethodException e) {
//			throw handleException(e);
//		} catch (IllegalAccessException e) {
//			throw handleException(e);
//		} catch (InstantiationException e) {
//			throw handleException(e);
//		} catch (InvocationTargetException e) {
//			throw handleException(e); 
//		}
		return oggettobulk;	
	}
	@Override
	protected void completeAllegato(AllegatoRichiestaBulk allegato) throws ApplicationException {
		for (SecondaryType secondaryType : allegato.getDocument(ordineAcqCMISService).getSecondaryTypes()) {
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
		return "allegatiOrdine";
	}
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk)getCrudArchivioAllegati().getModel();
		Document node = allegato.getDocument(ordineAcqCMISService);
		InputStream is = ordineAcqCMISService.getResource(node);
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
//	public String getNomeAllegatoDettaglio() throws ApplicationException{
//		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
//		if (dettaglio!= null){
//			return dettaglio.getNome();
//		}
//		return "";
//	}
//	public void scaricaDocumentoDettaglioCollegato(ActionContext actioncontext) throws Exception {
//		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
//		if (dettaglio!= null){
//			Document document = dettaglio.getDocument(richiesteCMISService);
//			if (document != null){
//				InputStream is = richiesteCMISService.getResource(document);
//				((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(document.getContentStreamLength()).intValue());
//				((HttpActionContext)actioncontext).getResponse().setContentType(document.getContentStreamMimeType());
//				OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
//				((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
//				byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
//				int buflength;
//				while ((buflength = is.read(buffer)) > 0) {
//					os.write(buffer,0,buflength);
//				}
//				is.close();
//				os.flush();
//			} else {
//				throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
//			}
//		} else {
//			throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
//		}
//	}
	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		return true;
	}
	protected Boolean isPossibileCancellazioneDettaglioAllegato(AllegatoGenericoBulk allegato) {
		return true;
	}
//	public SimpleDetailCRUDController getDettaglioAllegatiController() {
//		return dettaglioAllegatiController;
//	}
	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato){
		return true;
	}
	@Override
	protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
		OrdineAcqBulk ordine = (OrdineAcqBulk)allegatoParentBulk;
		super.gestioneCancellazioneAllegati(allegatoParentBulk);
	}
	public void gestionePostSalvataggio(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try {
			OrdineAcqBulk ordine = (OrdineAcqBulk)getModel(); 
//			((OrdineAcqComponentSession)createComponentSession()).gestioneStampaOrdine(context.getUserContext(), ordine);
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public void stampaRichiesta(ActionContext actioncontext) throws Exception {
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		InputStream is = ordineAcqCMISService.getStreamOrdine(ordine);
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
	public boolean areBottoniObbligazioneAbilitati()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();

		return 	ordine != null && 
				!isSearching() && 
				!isViewing() ;
	}
	public boolean isBottoneObbligazioneAggiornaManualeAbilitato()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();

		return(	ordine != null && !isSearching() && 
				!isViewing() );
	}
	public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {

		if (isDeleting() && getParent() != null)
			return getDefferedUpdateSaldiParentBP()
					.getDefferedUpdateSaldiBulk();
		return (IDefferUpdateSaldi) getDocumentoAmministrativoCorrente();
	}

	public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

		if (isDeleting() && getParent() != null)
			return ((IDefferedUpdateSaldiBP) getParent())
					.getDefferedUpdateSaldiParentBP();
		return this;
	}

	public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {

		if (deleteManager == null)
			deleteManager = new it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk();
		else
			deleteManager.reset();
		return deleteManager;
	}

	@Override
	public boolean isModelVoided() {
		return !isSearching() && getModel() != null
				&& ((Voidable) getModel()).isAnnullato();
	}
	public it.cnr.jada.util.RemoteIterator findObbligazioni(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			return ((OrdineAcqComponentSession)createComponentSession()).cercaObbligazioni(userContext, filtro);

		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(
			it.cnr.jada.action.ActionContext actionContext,
			it.cnr.jada.persistency.sql.CompoundFindClause clauses,
			it.cnr.jada.bulk.OggettoBulk bulk,
			it.cnr.jada.bulk.OggettoBulk context, java.lang.String property)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
					actionContext, ((OrdineAcqComponentSession)createComponentSession()).cerca(actionContext.getUserContext(),
							clauses, bulk, context, property));
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}
	@Override
	public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {
		return null;
	}
	@Override
	public IDocumentoAmministrativoBulk getBringBackDocAmm() {
		return getDocumentoAmministrativoCorrente();
	}

	@Override
	public IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {
		return (IDocumentoAmministrativoBulk) getModel();
	}

	@Override
	public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
		if (getObbligazioniController() == null)
			return null;
		return (Obbligazione_scadenzarioBulk) getObbligazioniController()
				.getModel();
	}
	@Override
	public boolean isAutoGenerated() {
		return false;
	}
	@Override
	public boolean isDeleting() {
		return isDeleting;
	}
	@Override
	public boolean isManualModify() {
		return true;
	}
	@Override
	public void setIsDeleting(boolean newIsDeleting) {
		isDeleting = newIsDeleting;
	}
	@Override
	public void validaObbligazionePerDocAmm(ActionContext actionContext, OggettoBulk bulk)
			throws BusinessProcessException {
		return;
	}
	public ObbligazioniCRUDController getObbligazioniController() {
		return obbligazioniController;
	}
	public void setDeleteManager(it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager) {
		this.deleteManager = deleteManager;
	}
	public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
		return userConfirm;
	}
	public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm) {
		this.userConfirm = userConfirm;
	}
	public boolean isCarryingThrough() {
		return carryingThrough;
	}
	public void setCarryingThrough(boolean carryingThrough) {
		this.carryingThrough = carryingThrough;
	}
	public SimpleDetailCRUDController getDettaglioObbligazioneController() {
		return dettaglioObbligazioneController;
	}
}
