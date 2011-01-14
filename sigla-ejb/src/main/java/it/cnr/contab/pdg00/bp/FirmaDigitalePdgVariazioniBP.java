package it.cnr.contab.pdg00.bp;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.CMISRelationship;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.ServizioPecBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.cmis.AllegatoPdGVariazioneDocumentBulk;
import it.cnr.contab.pdg00.bulk.cmis.AllegatoPdGVariazioneSignedDocument;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.pdg00.service.PdgVariazioniService;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.firma.DatiPEC;
import it.cnr.jada.firma.FirmaInfos;
import it.cnr.jada.firma.NotSignedEnvelopeException;
import it.cnr.jada.firma.Verifica;
import it.cnr.jada.firma.bp.SendPecMail;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;
import javax.servlet.ServletException;

/**
 * Insert the type's description here. Creation date: (11/04/2002 17:28:04)
 * 
 * @author: CNRADM
 */
public class FirmaDigitalePdgVariazioniBP extends
		it.cnr.jada.util.action.SelezionatoreListaBP implements FirmaInfos {
	private boolean signEnabled;
	private boolean signFile;
	private boolean uploadFile;
	private DatiPEC datiPEC = new DatiPEC();
	public static String PEC_PROTOCOLLO = "PROTOCOLLO";
	public static String PEC_BILANCIO = "BILANCIO";
	private PdgVariazioniService pdgVariazioniService;

	/**
	 * SpoolerStatusBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public FirmaDigitalePdgVariazioniBP() {
		super();
		table.setMultiSelection(true);
		setBulkInfo(BulkInfo.getBulkInfo(ArchiviaStampaPdgVariazioneBulk.class));
	}

	public PdGVariazioniComponentSession createComponentSession()
			throws BusinessProcessException {
		return (PdGVariazioniComponentSession) createComponentSession(
				"CNRPDG00_EJB_PdGVariazioniComponentSession",
				PdGVariazioniComponentSession.class);
	}

	@Override
	protected void setFocusedElement(ActionContext actioncontext, Object obj)
			throws BusinessProcessException {
		if (obj != null) {
			ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk) getModel();
			bulk.setPdg_variazioneForPrint((Pdg_variazioneBulk) obj);
			try {
				bulk.setPdgVariazioneDocument(pdgVariazioniService
						.getPdgVariazioneDocument(bulk
								.getPdg_variazioneForPrint()));
			} catch (DetailedException e) {
				handleException(e);
			}
			super.setFocusedElement(actioncontext, bulk);
		} else
			super.setFocusedElement(actioncontext, obj);
	}

	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {

		if (isUploadFile())
			openForm(context,action,target,"multipart/form-data");
		else
			super.openForm(context, action, target);
	}
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[6];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.refresh");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.sign");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.upload");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.printSigned");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.installa");
		toolbar[i-1].setSeparator(true);
		return toolbar;
	}

	public void writeToolbar(javax.servlet.jsp.PageContext pageContext)
			throws java.io.IOException, javax.servlet.ServletException {
	Button[] toolbar = getToolbar();
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		if (bulk != null) {
			toolbar[1]
					.setHref("doPrint('"
							+ JSPUtils
									.buildAbsoluteUrl(
											pageContext,
											null,
											"genericdownload/"
													+ bulk
															.getPdgVariazioneDocument()
															.getNode()
															.getName()
													+ "?methodName=scaricaFile&it.cnr.jada.action.BusinessProcess="
													+ getPath()) + "')");
			Node nodeSignedFile = getNodeFileFirmato(bulk
					.getPdgVariazioneDocument()
					.getNode());
			String signedFileName = null;
			if (nodeSignedFile!=null)
				signedFileName=getNodeFileFirmato(bulk
					.getPdgVariazioneDocument()
					.getNode()).getName();
			if (signedFileName!=null)
				toolbar[4]
					.setHref("doPrint('"
							+ JSPUtils
									.buildAbsoluteUrl(
											pageContext,
											null,
											"genericdownload/"
													+ signedFileName
													+ "?methodName=scaricaFileFirmato&it.cnr.jada.action.BusinessProcess="
													+ getPath()) + "')");
		}
		else {
			toolbar[1].setHref(null);
			toolbar[4].setHref(null);
		}
		writeToolbar(pageContext.getOut(), toolbar);
	}

	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		super.init(config, context);
		setMultiSelection(false);
		pdgVariazioniService = SpringUtil.getBean("pdgVariazioniService",
				PdgVariazioniService.class);
		ArchiviaStampaPdgVariazioneBulk bulk = new ArchiviaStampaPdgVariazioneBulk();
		bulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED);
		setModel(context, bulk);
		refresh(context);
	}

	public boolean isPrintButtonEnabled() {
		return getFocusedElement() != null;
	}

	public boolean isUploadButtonEnabled() {
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		return getFocusedElement() != null
				&& !isUploadFile()
				&& !bulk.getPdgVariazioneDocument().getNode().hasAspect(
						CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}

	public boolean isInviaButtonEnabled() {
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		return getFocusedElement() != null
				&& !bulk.getPdgVariazioneDocument().getNode().hasAspect(
						CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}

	public boolean isPrintSignedButtonEnabled() {
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		return getFocusedElement() != null && bulk.getPdgVariazioneDocument().getNode().hasAspect(
				CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}

	public boolean isSignButtonEnabled() {
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		return getFocusedElement() != null
				&& !bulk.getPdgVariazioneDocument().getNode().hasAspect(
						CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}

	public boolean isInstButtonEnabled() {
		return true;
	}

	public void refresh(ActionContext context) throws BusinessProcessException {
		try {
			setIterator(context, EJBCommonServices.openRemoteIterator(context,
					createComponentSession().cercaVariazioniForDocumentale(
							context.getUserContext(),
							null,
							new Pdg_variazioneBulk(),
							((ArchiviaStampaPdgVariazioneBulk) getModel())
									.getTiSigned(), Boolean.TRUE)));
		} catch (Throwable e) {
			throw new BusinessProcessException(e);
		}
		super.refresh(context);
	}

	public void scaricaFile(ActionContext actioncontext) throws IOException,
			ServletException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		InputStream is = pdgVariazioniService
				.getResource(archiviaStampaPdgVariazioneBulk
						.getPdgVariazioneDocument().getNode());
		((HttpActionContext) actioncontext).getResponse().setContentType(
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument()
						.getNode().getContentType());
		((HttpActionContext) actioncontext).getResponse().setContentLength(
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument()
						.getNode().getContentLength().intValue());
		OutputStream os = ((HttpActionContext) actioncontext).getResponse()
				.getOutputStream();
		int nextChar;
		while ((nextChar = is.read()) != -1)
			os.write(nextChar);
		os.write('\n');
		os.flush();
	}

	public File scaricaSuFile(ActionContext actioncontext, File filePath) throws IOException,
		ServletException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		InputStream is = pdgVariazioniService
				.getResource(archiviaStampaPdgVariazioneBulk
						.getPdgVariazioneDocument().getNode());
		((HttpActionContext) actioncontext).getResponse().setContentType(
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument()
						.getNode().getContentType());
		((HttpActionContext) actioncontext).getResponse().setContentLength(
				archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument()
						.getNode().getContentLength().intValue());
		File fout = new File(filePath.getParent()+File.separator+
		archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument()
				.getNode().getName());
		FileOutputStream os = new FileOutputStream(fout);
		int nextChar;
		while ((nextChar = is.read()) != -1)
			os.write(nextChar);
		os.flush();
		return fout;
	}

	public void scaricaFileFirmato(ActionContext actioncontext)
			throws IOException, ServletException, ApplicationException {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		Node firmato=getNodeFileFirmato(archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode());
		InputStream is = pdgVariazioniService.getResource(firmato);
		((HttpActionContext) actioncontext).getResponse().setContentType(
				firmato.getContentType());
		((HttpActionContext) actioncontext).getResponse().setContentLength(
				firmato.getContentLength().intValue());
		OutputStream os = ((HttpActionContext) actioncontext).getResponse()
				.getOutputStream();
		int nextChar;
		while ((nextChar = is.read()) != -1)
			os.write(nextChar);
		os.write('\n');
		os.flush();
	}

	public Node getNodeFileFirmato(Node nodePdf) {

		ListNodePage<Node> firmati = pdgVariazioniService
				.getRelationshipFromTarget(nodePdf.getId(),
						CMISRelationship.CNR_SIGNEDDOCUMENT);
		if (firmati.size()==1)
			return firmati.get(0);
		else
			return null;
		
	}
	
	public void sign(ActionContext context) throws BusinessProcessException {
		try {
			ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
			caricaDatiPEC(context);
			setSignEnabled(true);
			setSignFile(true);
		} catch (Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void installa(ActionContext context) throws BusinessProcessException {
		try {
			ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
			setSignEnabled(true);
		} catch (Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public boolean isSignEnabled() {
		return signEnabled;
	}

	public void setSignEnabled(boolean signEnabled) {
		this.signEnabled = signEnabled;
	}

	public boolean isSignFile() {
		return signFile;
	}

	public void setSignFile(boolean signFile) {
		this.signFile = signFile;
	}

	public String getFileName() {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		if (archiviaStampaPdgVariazioneBulk != null)
			return archiviaStampaPdgVariazioneBulk.getPdgVariazioneDocument().getNode().getName().replace(" ", "_");
		else
			return (null);
	}

	public String getDownloadFile(javax.servlet.jsp.PageContext pageContext) {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		File file = null;
		File fileDir = null;
		try {
			fileDir = File.createTempFile("var", "pdg", new File(System
					.getProperty("tmp.dir.SIGLAWeb")
					+ "/tmp/"));
			fileDir.delete();
			if (!fileDir.mkdir())
				throw new BusinessProcessException(
						"Directory già esistente, riprovare a generare la firma!");

			file = new File(fileDir, getFileName());
			InputStream is = pdgVariazioniService
					.getResource(archiviaStampaPdgVariazioneBulk
							.getPdgVariazioneDocument().getNode());
			OutputStream out = new FileOutputStream(file);
			int c;
			while ((c = is.read()) != -1) {
				out.write(c);
			}
			is.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			handleException(e);
		} catch (BusinessProcessException e) {
			handleException(e);
		}
		String dirName = new File(fileDir.getName()).getName();
		// ripuliamo il path
		String fileName = new File(file.getName()).getName();
		return JSPUtils.buildAbsoluteUrl(pageContext, null, "tmp/" + dirName
				+ "/" + fileName);
	}

	public String getPgStampa() {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		if (archiviaStampaPdgVariazioneBulk != null)
			return ""
					+ archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
							.longValue();
		else
			return null;
	}

	public String getUploadFile(javax.servlet.jsp.PageContext pageContext) {
		String actionExtension = pageContext.getServletConfig()
				.getInitParameter("extension");
		if (actionExtension == null)
			actionExtension = ".do";
		String path = getDefaultAction() + actionExtension;
		return JSPUtils.buildAbsoluteUrl(pageContext, null, path);
	}

	public String descrizione() {
		return null;
	}

	public DatiPEC datiPEC() {
		return datiPEC;
	}

	public void caricaDatiPEC(ActionContext context) throws ValidationException {
		ArchiviaStampaPdgVariazioneBulk pbulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		String cdServizioPEC = null;
		cdServizioPEC = PEC_BILANCIO;

		boolean testMode = true;
		Parametri_enteBulk parametriEnte = null;
		try {
			parametriEnte = Utility.createParametriEnteComponentSession()
					.getParametriEnte(context.getUserContext());
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		// PEC del protocollo
		ServizioPecBulk servizioPecProtocollo = null;
		try {
			servizioPecProtocollo = Utility
					.createParametriEnteComponentSession().getServizioPec(
							context.getUserContext(), PEC_PROTOCOLLO);
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		if (servizioPecProtocollo == null)
			throw new ValidationException(
					"Non è presente l'email per l'invio della posta certificata per l'ufficio del Protocollo. Impossibile procedere!");
		// PEC derivata dalla stampa specifica
		ServizioPecBulk servizioPec = null;
		try {
			servizioPec = Utility.createParametriEnteComponentSession()
					.getServizioPec(context.getUserContext(), cdServizioPEC);
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		if (servizioPec == null)
			throw new ValidationException(
					"L'ufficio di competenza per l'invio della posta certificata non è definito. Impossibile procedere!");
		if (!parametriEnte.getTipo_db()
				.equals(Parametri_enteBulk.DB_PRODUZIONE)) 
			testMode = true;
		else
			testMode = false;
			
		CdsBulk cds = null;
		Unita_organizzativaBulk uo = null;
		CdrBulk cdr = null;
		try {
			cds = Utility.createParametriEnteComponentSession().getCds(
					context.getUserContext(),
					CNRUserContext.getCd_cds((CNRUserContext) context
							.getUserContext()));
			uo = Utility.createParametriEnteComponentSession().getUo(
					context.getUserContext(),
					CNRUserContext
							.getCd_unita_organizzativa((CNRUserContext) context
									.getUserContext()));
			cdr = Utility.createParametriEnteComponentSession().getCdr(
					context.getUserContext(),
					CNRUserContext.getCd_cdr((CNRUserContext) context
							.getUserContext()));
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}

		String cdUoPec = null;
		UnitaOrganizzativaPecBulk uoPec = null;

		if (cds.getCd_tipo_unita().equals(
				Tipo_unita_organizzativaHome.TIPO_UO_SAC)) {
			datiPEC.setUo(CNRUserContext
					.getCd_unita_organizzativa((CNRUserContext) context
							.getUserContext()));
			datiPEC.setDsUo(uo.getDs_unita_organizzativa());
			cdUoPec = uo.getCd_unita_organizzativa();
			uoPec = getUoPec(context, cdUoPec);
			datiPEC.setCds("000000");
			datiPEC.setDsCds("Consiglio Nazionale delle Ricerche");
			datiPEC.setSiglaCds("CNR");
			datiPEC.setCdsDest("000000");
			datiPEC.setDsCdsDest("Consiglio Nazionale delle Ricerche");
			datiPEC.setSiglaCdsDest("CNR");

		} else {
			datiPEC.setUo(uo.getCd_cds());
			datiPEC.setDsUo(uo.getDs_unita_organizzativa());
			cdUoPec = cds.getCd_unita_organizzativa();
			uoPec = getUoPec(context, cdUoPec);
			datiPEC.setCds(cds.getCd_unita_organizzativa());
			datiPEC.setDsCds(cds.getDs_unita_organizzativa());
			datiPEC.setSiglaCds(uoPec.getSigla().toUpperCase());
			datiPEC.setCdsDest("000000");
			datiPEC.setDsCdsDest("Consiglio Nazionale delle Ricerche");
			datiPEC.setSiglaCdsDest("CNR");
		}

		if (!pbulk.getDs_variazione().equals(""))
			datiPEC.setOggetto("Variazione al PdG n. "+pbulk.getPg_variazione_pdg()+"-"+pbulk.getEsercizio()+" CdR proponente "+pbulk.getCd_centro_responsabilita()+", "+pbulk.getDs_variazione());
		else
			datiPEC.setOggetto("Variazione al PdG n. "+pbulk.getPg_variazione_pdg()+"-"+pbulk.getEsercizio()+" CdR proponente "+pbulk.getCd_centro_responsabilita()+", "+descrizione());

		datiPEC.setOggetto(datiPEC.getOggetto().replace("\"","'").replaceAll("[^a-zA-Z0-9\\\\/. ,'_^-]"," "));
		datiPEC.setDenominazioneServizio(servizioPec.getDsServizio());
		
		if (testMode) {
			datiPEC.setEmailProtocollo(servizioPecProtocollo.getEmailTest());
			datiPEC.setEmailServizio(servizioPec.getEmailTest());
		} else {
			datiPEC.setEmailProtocollo(servizioPecProtocollo.getEmail());
			datiPEC.setEmailProtocollo2(servizioPecProtocollo.getEmail2());
			datiPEC.setEmailProtocollo3(servizioPecProtocollo.getEmail3());
			datiPEC.setEmailServizio(servizioPec.getEmail());
			datiPEC.setEmailServizio2(servizioPec.getEmail2());
			datiPEC.setEmailServizio3(servizioPec.getEmail3());
			datiPEC.setEmailIstituto(uoPec.getEmailPec());
			datiPEC.setEmailIstituto2(uoPec.getEmailPecDirettore());
		}
	}

	private UnitaOrganizzativaPecBulk getUoPec(ActionContext context, String cdUoPec) throws ValidationException {
		UnitaOrganizzativaPecBulk uoPec = null;
		try {
			uoPec = Utility
					.createParametriEnteComponentSession()
					.getUnitaOrganizzativaPec(context.getUserContext(), cdUoPec);
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		if (uoPec == null || (uoPec.getEmailPec()==null && uoPec.getEmailPecDirettore()==null))
			throw new ValidationException("L'indirizzo email di posta certificata per la unità organizzativa "+cdUoPec+" non è definito. Impossibile procedere!");
		return uoPec;
	}
	
	public void persist(ActionContext context, String signFileRicevuto)
			throws Exception {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		File file = new File(signFileRicevuto);
		Verifica.verificaBustaFirmata(file);

		AllegatoPdGVariazioneSignedDocument allegato = new AllegatoPdGVariazioneSignedDocument();
		allegato.setFile(file);
		// allegato.setTitolo(titolo);
		allegato.setDescrizione("Busta firmata: "
				+ archiviaStampaPdgVariazioneBulk.getDs_variazione());
		allegato.setNome(file.getName());
		allegato.setContentType("application/p7m");
		CMISPath cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
		Node node = pdgVariazioniService.storeSimpleDocument(allegato,
				new FileInputStream(allegato.getFile()), allegato
						.getContentType(), allegato.getNome(), cmisPath);
		Node pdgVariazioneDocumentNode = archiviaStampaPdgVariazioneBulk
				.getPdgVariazioneDocument().getNode();
		pdgVariazioniService.createRelationship(pdgVariazioneDocumentNode
				.getId(), node.getId(),
				CMISRelationship.VARPIANOGEST_ALLEGATIVARBILANCIO);

		pdgVariazioniService.addAspect(archiviaStampaPdgVariazioneBulk
				.getPdgVariazioneDocument().getNode(),
				CMISAspect.CNR_SIGNEDDOCUMENT.value());

		try {
			List<String> lista = datiPEC.emailListTotale();
			SendPecMail.sendMail(datiPEC.getOggetto(), datiPEC.getOggetto(), file,
					lista, datiPEC);
		} catch (Exception ex) {
			pdgVariazioniService.removeAspect(archiviaStampaPdgVariazioneBulk
					.getPdgVariazioneDocument().getNode(),
					CMISAspect.CNR_SIGNEDDOCUMENT.value());
			pdgVariazioniService.deleteNode(node);
			throw new ApplicationException("Errore nell'invio della mail PEC al protocollo informatico. Ripetere l'operazione di firma!");
		}

		pdgVariazioniService.createRelationship(node.getId(),
				pdgVariazioneDocumentNode.getId(),
				CMISRelationship.CNR_SIGNEDDOCUMENT);

		setFocusedElement(context, null);
		refresh(context);
	}

	public void persistUploadedFile(ActionContext context, UploadedFile ufile)
		throws Exception {
		ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk = (ArchiviaStampaPdgVariazioneBulk) getFocusedElement();
		File file = ufile.getFile();
		File fOutput=null;
		try {
			fOutput = Verifica.estraiFile(file);
		} catch (NotSignedEnvelopeException ex) {
			throw new ValidationException(
					"Il file non ha il formato richiesto \"busta di firma digitale\" p7m");
		}
		Verifica.verificaBustaFirmata(file);
		File archivedFileOutput = scaricaSuFile(context, ufile.getFile());
		if (!Verifica.fileContentsEquals(fOutput, archivedFileOutput))
			throw new ValidationException(
				"Il file inviato non corrisponde alla variazione selezionata. Impossibile procedere!");		

		File fileNew = new File(file.getParent(), getFileName()+".p7m");
		Verifica.fileCopy(file, fileNew);
		Verifica.verificaBustaFirmata(fileNew);
		
		AllegatoPdGVariazioneSignedDocument allegato = new AllegatoPdGVariazioneSignedDocument();
		allegato.setFile(fileNew);
		// allegato.setTitolo(titolo);
		allegato.setDescrizione("Busta firmata: "
				+ archiviaStampaPdgVariazioneBulk.getDs_variazione());
		allegato.setNome(fileNew.getName());
		allegato.setContentType("application/p7m");
		CMISPath cmisPath = getCMISPath(archiviaStampaPdgVariazioneBulk);
		Node node = pdgVariazioniService.storeSimpleDocument(allegato,
				new FileInputStream(allegato.getFile()), allegato
						.getContentType(), allegato.getNome(), cmisPath);
		Node pdgVariazioneDocumentNode = archiviaStampaPdgVariazioneBulk
				.getPdgVariazioneDocument().getNode();
		pdgVariazioniService.createRelationship(pdgVariazioneDocumentNode
				.getId(), node.getId(),
				CMISRelationship.VARPIANOGEST_ALLEGATIVARBILANCIO);
		
		pdgVariazioniService.addAspect(archiviaStampaPdgVariazioneBulk
				.getPdgVariazioneDocument().getNode(),
				CMISAspect.CNR_SIGNEDDOCUMENT.value());
		
		try {
			List<String> lista = datiPEC.emailListTotale();
			SendPecMail.sendMail(datiPEC.getOggetto(), datiPEC.getOggetto(), fileNew,
					lista, datiPEC);
		} catch (Exception ex) {
			pdgVariazioniService.removeAspect(archiviaStampaPdgVariazioneBulk
					.getPdgVariazioneDocument().getNode(),
					CMISAspect.CNR_SIGNEDDOCUMENT.value());
			pdgVariazioniService.deleteNode(node);
			throw new ApplicationException("Errore nell'invio della mail PEC al protocollo informatico. Ripetere l'operazione di firma!");
		}

		pdgVariazioniService.createRelationship(node.getId(),
				pdgVariazioneDocumentNode.getId(),
				CMISRelationship.CNR_SIGNEDDOCUMENT);

		setFocusedElement(context, null);
		refresh(context);
	}
	
	private CMISPath getCMISPath(
			ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) {
		CMISPath cmisPath = SpringUtil.getBean(
				"cmisPathVariazioniAlPianoDiGestione", CMISPath.class);
		cmisPath = pdgVariazioniService.createFolderIfNotPresent(cmisPath,
				archiviaStampaPdgVariazioneBulk.getEsercizio().toString(),
				"Esercizio :"
						+ archiviaStampaPdgVariazioneBulk.getEsercizio()
								.toString(), "Esercizio :"
						+ archiviaStampaPdgVariazioneBulk.getEsercizio()
								.toString());
		cmisPath = pdgVariazioniService.createFolderIfNotPresent(cmisPath,
				archiviaStampaPdgVariazioneBulk.getCd_cds() + " - "
						+ archiviaStampaPdgVariazioneBulk.getDs_cds(),
				archiviaStampaPdgVariazioneBulk.getDs_cds(),
				archiviaStampaPdgVariazioneBulk.getDs_cds());
		cmisPath = pdgVariazioniService.createFolderIfNotPresent(cmisPath,
				"CdR "
						+ archiviaStampaPdgVariazioneBulk
								.getCd_centro_responsabilita()
						+ " Variazione "
						+ lpad(archiviaStampaPdgVariazioneBulk
								.getPg_variazione_pdg(), 5, '0'), null, null);
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

	public void rendiPersistente(String file) {
		// TODO Auto-generated method stub

	}

	public String tipoPersistenza() {
		return TIPO_PERSISTENZA_ESTERNA;
	}

	public void setUploadFile(boolean uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean isUploadFile() {
		return uploadFile;
	}
}
