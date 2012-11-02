package it.cnr.contab.pdg00.bp;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.CMISRelationship;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.cmis.AllegatoPdGVariazioneDocumentBulk;
import it.cnr.contab.pdg00.bulk.cmis.PdgVariazioneDocument;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg00.service.PdgVariazioniService;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
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
import java.net.SocketException;
import java.util.Iterator;
import java.util.UUID;

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
	private CMISService cmisService;
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
		pdgVariazioniService = SpringUtil.getBean("pdgVariazioniService",
				PdgVariazioniService.class);
		cmisService = SpringUtil.getBean("cmisService",
				CMISService.class);		
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
		return archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().hasAspect(CMISAspect.CNR_SIGNEDDOCUMENT.value());
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
						pdgVariazioniService.getPdgVariazioneDocument(archiviaStampaPdgVariazioneBulk.getPdg_variazioneForPrint()));
			if (archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().isSignedDocument())
				archiviaStampaPdgVariazioneBulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED);
			else
				archiviaStampaPdgVariazioneBulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED);
			archiviaStampaPdgVariazioneBulk.getArchivioAllegati().clear();
			ListNodePage<Node> allegati = cmisService.getRelationship(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().getId(), 
					CMISRelationship.VARPIANOGEST_ALLEGATIVARBILANCIO);
			for (Node node : allegati) {
				if (node.hasAspect(CMISAspect.SYS_ARCHIVED.value()))
					continue;
				AllegatoPdGVariazioneDocumentBulk allegato = AllegatoPdGVariazioneDocumentBulk.construct(node);
				allegato.setContentType(node.getContentType());
				allegato.setNome(node.getName());
				allegato.setDescrizione(node.getDescription());
				allegato.setTitolo(node.getTitle());
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
			CMISPath cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
			Node node = cmisService.storePrintDocument(archiviaStampaPdgVariazioneBulk, report, cmisPath);
			archiviaAllegati(actioncontext, node);
			archiviaStampaPdgVariazioneBulk.setPdgVariazioneDocument(PdgVariazioneDocument.construct(node));
			setModel(actioncontext, archiviaStampaPdgVariazioneBulk);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
		
	}

	private CMISPath getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk){
		CMISPath cmisPath = SpringUtil.getBean("cmisPathVariazioniAlPianoDiGestione",CMISPath.class);
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, archiviaStampaPdgVariazioneBulk.getEsercizio().toString(), 
				"Esercizio :"+archiviaStampaPdgVariazioneBulk.getEsercizio().toString(), 
				"Esercizio :"+archiviaStampaPdgVariazioneBulk.getEsercizio().toString());
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, archiviaStampaPdgVariazioneBulk.getCd_cds()+" - "+archiviaStampaPdgVariazioneBulk.getDs_cds(), 
				archiviaStampaPdgVariazioneBulk.getDs_cds(), 
				archiviaStampaPdgVariazioneBulk.getDs_cds());
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, 
				"CdR "+archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita()+
				" Variazione "+ lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(),5,'0'), 
				null, 
				null);
		return cmisPath;
	}
	
	public static String lpad(double d, int size, char pad) {
        return lpad(Double.toString(d), size, pad);
	}

	public static String lpad(long l, int size, char pad) {
        return lpad(Long.toString(l), size, pad);
	}

	public static String lpad(String s, int size, char pad) {
        StringBuilder builder = new StringBuilder();
        while (builder.length() + s.length() < size) {
                builder.append(pad);
        }
        builder.append(s);
        return builder.toString();
	}
	
	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		archiviaAllegati(actioncontext, null);
	}

	private void archiviaAllegati(ActionContext actioncontext, Node pdgVariazioneDocumentNode) throws BusinessProcessException{
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		if (pdgVariazioneDocumentNode == null)
			pdgVariazioneDocumentNode = archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode();
		CMISPath cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
		for (AllegatoPdGVariazioneDocumentBulk allegato : archiviaStampaPdgVariazioneBulk.getArchivioAllegati()) {
			if (allegato.isToBeCreated()){
				try {
					Node node = cmisService.storeSimpleDocument(allegato, 
							new FileInputStream(allegato.getFile()),
							allegato.getContentType(),
							allegato.getNome(), cmisPath);
					cmisService.createRelationship(pdgVariazioneDocumentNode.getId(), 
							node.getId(), CMISRelationship.VARPIANOGEST_ALLEGATIVARBILANCIO);
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					handleException(e);
				}
			}else if (allegato.isToBeUpdated()) {
				try {
					if (allegato.getFile() != null)
						cmisService.updateContent(allegato.getNode().getId(), 
								new FileInputStream(allegato.getFile()),
								allegato.getContentType());
					cmisService.updateProperties(allegato, allegato.getNode());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					handleException(e);
				}
			}
		}
		for (Iterator<AllegatoPdGVariazioneDocumentBulk> iterator = archiviaStampaPdgVariazioneBulk.getArchivioAllegati().deleteIterator(); iterator.hasNext();) {
			AllegatoPdGVariazioneDocumentBulk allegato = iterator.next();
			if (allegato.isToBeDeleted()){
				cmisService.deleteNode(allegato.getNode());
				allegato.setCrudStatus(OggettoBulk.NORMAL);
			}
		}
	}

	@Override
	public void delete(ActionContext actioncontext)
			throws BusinessProcessException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		pdgVariazioniService.deleteNode(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode());
		for (AllegatoPdGVariazioneDocumentBulk allegato : archiviaStampaPdgVariazioneBulk.getArchivioAllegati()) {
			pdgVariazioniService.deleteNode(allegato.getNode());
		}
	}

	public String getNomeFile(){
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk)getModel();
		if (archiviaStampaPdgVariazioneBulk != null &&
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument() != null &&
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode() != null)
			return archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().getName();
		return null;
	}
	
	public String getNomeAllegato(){
		AllegatoPdGVariazioneDocumentBulk allegato = (AllegatoPdGVariazioneDocumentBulk)getCrudArchivioAllegati().getModel();
		if (allegato != null && allegato.getNode() != null)
			return allegato.getNode().getName();
		return null;
	}
	
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException {
		AllegatoPdGVariazioneDocumentBulk allegato = (AllegatoPdGVariazioneDocumentBulk)getCrudArchivioAllegati().getModel();
		InputStream is = cmisService.getResource(allegato.getNode());
		((HttpActionContext)actioncontext).getResponse().setContentLength(allegato.getNode().getContentLength().intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(allegato.getNode().getContentType());
		((HttpActionContext)actioncontext).getResponse().setContentLength(allegato.getNode().getContentLength().intValue());
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
		InputStream is = pdgVariazioniService.getResource(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode());
		((HttpActionContext)actioncontext).getResponse().setContentLength(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().getContentLength().intValue());		
		((HttpActionContext)actioncontext).getResponse().setContentType(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().getContentType());
		((HttpActionContext)actioncontext).getResponse().setContentLength(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().getContentLength().intValue());
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
