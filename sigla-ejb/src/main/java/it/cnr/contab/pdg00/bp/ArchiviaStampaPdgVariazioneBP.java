package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.storage.AllegatoPdGVariazioneDocumentBulk;
import it.cnr.contab.pdg00.bulk.storage.PdgVariazioneDocument;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg00.service.PdgVariazioniService;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.SiglaStorageService;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
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
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

public class ArchiviaStampaPdgVariazioneBP extends SimpleCRUDBP{
	private SimpleDetailCRUDController crudArchivioAllegati = new SimpleDetailCRUDController( "ArchivioAllegati", AllegatoPdGVariazioneDocumentBulk.class, "archivioAllegati", this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			AllegatoPdGVariazioneDocumentBulk allegato = (AllegatoPdGVariazioneDocumentBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.ArchivioAllegati.file");
			if ( allegato.getNome() == null ) {
				if (file == null || file.getName().equals(""))
					throw new ValidationException("Attenzione: selezionare un File da caricare.");
			}
			if (!(file == null || file.getName().equals(""))) {
				allegato.setFile(file.getFile());
				allegato.setContentType(file.getContentType());
				allegato.setNome(allegato.parseFilename(file.getName()));
				allegato.setToBeUpdated();
				getParentController().setDirty(true);
			}
			validaAllegatoNomeFile(actioncontext, allegato);
			super.validate(actioncontext, oggettobulk);
		}
		
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			super.validateForDelete(actioncontext, oggettobulk);
		}
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			return super.removeDetail(oggettobulk, i);
		}
		public boolean isShrinkable() {
			return super.isShrinkable() && !isSigned();
		};
		public boolean isGrowable() {
			return super.isGrowable() && !isSigned();			
		};
	};
	
	private static final long serialVersionUID = -3132138853583406225L;
	private PdgVariazioniService pdgVariazioniService;
	public ArchiviaStampaPdgVariazioneBP() {
		super();
	}

	protected void validaAllegatoNomeFile(ActionContext actioncontext, AllegatoPdGVariazioneDocumentBulk allegato) throws ValidationException{
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		for (AllegatoPdGVariazioneDocumentBulk result : archiviaStampaPdgVariazioneBulk.getArchivioAllegati()) {
			if (!result.equals(allegato) && result.getNome().equals(allegato.getNome()))
				throw new ValidationException("Attenzione file gi� presente!");
		}
	}

	public ArchiviaStampaPdgVariazioneBP(String s) {
		super(s);
	}

	
	public PdgVariazioniService getPdgVariazioniService() {
		return pdgVariazioniService;
	}

	public SimpleDetailCRUDController getCrudArchivioAllegati() {
		return crudArchivioAllegati;
	}
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		openForm(context,action,target,"multipart/form-data");
	}

	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		pdgVariazioniService = SpringUtil.getBean(PdgVariazioniService.class);
		super.initialize(actioncontext);
	}
	
	@Override
	public boolean isDeleteButtonEnabled() {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		return super.isDeleteButtonEnabled() &&
		!isSigned();
	}
	
	@Override
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled() && !isSigned();
	}
	
	public boolean isSigned(){
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		if (archiviaStampaPdgVariazioneBulk == null || 
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument() == null)
			return false;
		return archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject().
				<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(StoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext arg0,
			OggettoBulk arg1) throws BusinessProcessException {
		return arg1.initializeForInsert(this, arg0);
	}
	
	@Override
	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)oggettobulk;
		try{
			return EJBCommonServices.openRemoteIterator(actioncontext,
					((PdGVariazioniComponentSession)createComponentSession()).
						cercaVariazioniForDocumentale(actioncontext.getUserContext(), compoundfindclause, 
									archiviaStampaPdgVariazioneBulk.getPdg_variazioneForPrint(), 
									archiviaStampaPdgVariazioneBulk.getTiSigned(),Boolean.TRUE));
		}catch(Exception ex){
			throw handleException(ex);
		}
	}
	
	@Override
	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
			OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		try{
			ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)oggettobulk1;
			return EJBCommonServices.openRemoteIterator(actioncontext,
					((PdGVariazioniComponentSession)createComponentSession()).
						cercaVariazioniForDocumentale(actioncontext.getUserContext(), compoundfindclause, 
									oggettobulk, 
									archiviaStampaPdgVariazioneBulk.getTiSigned(),
									Boolean.FALSE));
		}catch(Exception ex){
			throw handleException(ex);
		}
	}
	
	@Override
	public OggettoBulk initializeModelForFreeSearch(
			ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		return initializeModelForSearch(actioncontext, oggettobulk);
	}
	
	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)oggettobulk;
		archiviaStampaPdgVariazioneBulk.setPdg_variazioneForPrint(new Pdg_variazioneBulk());
		return super.initializeModelForSearch(actioncontext, oggettobulk);
	}

	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = null;
		if (oggettobulk instanceof ArchiviaStampaPdgVariazioneBulk){
			archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)oggettobulk;
		}else{
			archiviaStampaPdgVariazioneBulk = new ArchiviaStampaPdgVariazioneBulk();
			archiviaStampaPdgVariazioneBulk.setToBeUpdated();
			archiviaStampaPdgVariazioneBulk.setPdg_variazioneForPrint((Pdg_variazioneBulk) oggettobulk);
		}
		try {
			if (archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument() == null)
				archiviaStampaPdgVariazioneBulk.setPdgVariazioneDocument(
						pdgVariazioniService.getPdgVariazioneDocument(archiviaStampaPdgVariazioneBulk));
			if (archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().isSignedDocument())
				archiviaStampaPdgVariazioneBulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED);
			else
				archiviaStampaPdgVariazioneBulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED);
			archiviaStampaPdgVariazioneBulk.getArchivioAllegati().clear();
			List<StorageObject> allegati = pdgVariazioniService.getRelationship(
					archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject().getKey(),
					StoragePropertyNames.R_VARPIANOGEST_ALLEGATIVARBILANCIO.value());
			for (StorageObject storageObject : allegati) {
				if (pdgVariazioniService.hasAspect(storageObject, StoragePropertyNames.SYS_ARCHIVED.value()))
					continue;
				AllegatoPdGVariazioneDocumentBulk allegato = AllegatoPdGVariazioneDocumentBulk.construct(storageObject);
				allegato.setContentType(storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
				allegato.setNome(storageObject.<String>getPropertyValue(StoragePropertyNames.NAME.value()));
				allegato.setDescrizione(storageObject.<String>getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
				allegato.setTitolo(storageObject.<String>getPropertyValue(StoragePropertyNames.TITLE.value()));
				allegato.setCrudStatus(OggettoBulk.NORMAL);
				archiviaStampaPdgVariazioneBulk.addToArchivioAllegati(allegato);
			}
		} catch (DetailedException e) {
			handleException(e);
		}
		return archiviaStampaPdgVariazioneBulk;
	}
	
	@Override
	public void create(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
			if (archiviaStampaPdgVariazioneBulk.getPdg_variazioneForPrint() == null ||
					archiviaStampaPdgVariazioneBulk.getPdg_variazioneForPrint().getEsercizio() == null)
				throw new ApplicationException("Valorizzare la Variazione al PdG!");
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.setFlEmail(false);
			print.setReport("/cnrpreventivo/pdg/stampa_variazioni_pdg.jasper");
			print.setNomeFile("Variazione al PdG n. "
					+ archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
					+ " CdR proponente "
					+ archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() + ".pdf");
			print.setUtcr(actioncontext.getUserContext().getUser());
			print.addParam("Esercizio", archiviaStampaPdgVariazioneBulk.getPdg_variazioneForPrint().getEsercizio(), Integer.class);
			print.addParam("Variazione", archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg().intValue(), Integer.class);
			Report report = SpringUtil.getBean("printService",
					PrintService.class).executeReport(actioncontext.getUserContext(),
					print);
			String cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
			StorageObject storageObject = pdgVariazioniService.
					storeSimpleDocument(archiviaStampaPdgVariazioneBulk, report.getInputStream(), report.getContentType(), report.getName(), cmisPath);
			archiviaAllegati(actioncontext, storageObject);
			archiviaStampaPdgVariazioneBulk.setPdgVariazioneDocument(PdgVariazioneDocument.construct(storageObject));
			setModel(actioncontext, archiviaStampaPdgVariazioneBulk);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
		
	}

	private String getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) throws ApplicationException{
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(),
				Optional.ofNullable(archiviaStampaPdgVariazioneBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				archiviaStampaPdgVariazioneBulk.getCd_cds()+" - "+archiviaStampaPdgVariazioneBulk.getDs_cds(),
				"CdR "+archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita()+
						" Variazione "+ Utility.lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(),5,'0')
		).stream().collect(
				Collectors.joining(SiglaStorageService.SUFFIX)
		);
	}
	
	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			archiviaAllegati(actioncontext, null);
		} catch (ApplicationException e) {
			handleException(e);
		}
	}

	private void archiviaAllegati(ActionContext actioncontext, StorageObject pdgVariazioneDocumentNode) throws BusinessProcessException, ApplicationException{
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		if (pdgVariazioneDocumentNode == null)
			pdgVariazioneDocumentNode = archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject();
		String cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
		for (AllegatoPdGVariazioneDocumentBulk allegato : archiviaStampaPdgVariazioneBulk.getArchivioAllegati()) {
			if (allegato.isToBeCreated()){
				try {
					StorageObject node = pdgVariazioniService.storeSimpleDocument(allegato,
							new FileInputStream(allegato.getFile()),
							allegato.getContentType(),
							allegato.getNome(), cmisPath);
					pdgVariazioniService.createRelationship(pdgVariazioneDocumentNode.getKey(),
							node.getKey(), StoragePropertyNames.R_VARPIANOGEST_ALLEGATIVARBILANCIO.value());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					handleException(e);
				}
			}else if (allegato.isToBeUpdated()) {
				try {
					if (allegato.getFile() != null)
						pdgVariazioniService.updateStream(allegato.getDocument().getKey(),
								new FileInputStream(allegato.getFile()),
								allegato.getContentType());
					pdgVariazioniService.updateProperties(allegato, allegato.getDocument());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					handleException(e);
				}
			}
		}
		for (Iterator<AllegatoPdGVariazioneDocumentBulk> iterator = archiviaStampaPdgVariazioneBulk.getArchivioAllegati().deleteIterator(); iterator.hasNext();) {
			AllegatoPdGVariazioneDocumentBulk allegato = iterator.next();
			if (allegato.isToBeDeleted()){
				pdgVariazioniService.delete(allegato.getDocument());
				allegato.setCrudStatus(OggettoBulk.NORMAL);
			}
		}
	}

	@Override
	public void delete(ActionContext actioncontext)
			throws BusinessProcessException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		pdgVariazioniService.delete(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject());
		for (AllegatoPdGVariazioneDocumentBulk allegato : archiviaStampaPdgVariazioneBulk.getArchivioAllegati()) {
			pdgVariazioniService.delete(allegato.getDocument());
		}
	}

	public String getNomeFile(){
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		if (archiviaStampaPdgVariazioneBulk != null &&
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument() != null &&
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject() != null)
			return archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject().<String>getPropertyValue(StoragePropertyNames.NAME.value());
		return null;
	}
	
	public String getNomeAllegato(){
		AllegatoPdGVariazioneDocumentBulk allegato = (AllegatoPdGVariazioneDocumentBulk)getCrudArchivioAllegati().getModel();
		if (allegato != null && allegato.getDocument() != null)
			return allegato.getDocument().<String>getPropertyValue(StoragePropertyNames.NAME.value());
		return null;
	}
	
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException {
		AllegatoPdGVariazioneDocumentBulk allegato = (AllegatoPdGVariazioneDocumentBulk)getCrudArchivioAllegati().getModel();
		InputStream is = pdgVariazioniService.getResource(allegato.getDocument());
		((HttpActionContext)actioncontext).getResponse().setContentLength(allegato.getDocument().<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(allegato.getDocument().<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
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
		
	public void scaricaFile(ActionContext actioncontext) throws IOException,
			ServletException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		InputStream is = pdgVariazioniService.getResource(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject());
		((HttpActionContext)actioncontext).getResponse().setContentLength(
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject().<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getStorageObject().<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
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
