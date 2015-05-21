package it.cnr.contab.docamm00.bp;


import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.MimeTypes;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.docamm00.cmis.CMISDocAmmAspect;
import it.cnr.contab.docamm00.cmis.CMISFileFatturaAttiva;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.PasswordAuthentication;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.bindings.spi.http.Response;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP extends SelezionatoreListaBP {
	private transient final static Log logger = LogFactory.getLog(CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP.class);
	private static final long serialVersionUID = 1L;
	private DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService;
	private boolean utenteNonAbilitatoFirma;
	
	public boolean isUtenteNonAbilitatoFirma() {
		return utenteNonAbilitatoFirma;
	}


	public void setUtenteNonAbilitatoFirma(boolean utenteNonAbilitatoFirma) {
		this.utenteNonAbilitatoFirma = utenteNonAbilitatoFirma;
	}


	@Override
	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		super.init(config, context);
		UserContext userContext = context.getUserContext();
		try {
			setUtenteNonAbilitatoFirma(isUtenteNonAbilitatoFirma(userContext));
		} catch (ComponentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService",DocumentiCollegatiDocAmmService.class);	
	}

	
	public void scaricaDocumentiCollegati(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
		String cds = ((HttpActionContext)actioncontext).getParameter("cds");
		String cdUo = ((HttpActionContext)actioncontext).getParameter("cdUo");
		Long pgFattura = Long.valueOf(((HttpActionContext)actioncontext).getParameter("pgFattura"));
		Folder node = documentiCollegatiDocAmmService.recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
		InputStream is = null;
		if (node == null){
			is = getStreamNewDocument(actioncontext, esercizio, cds, cdUo, pgFattura);
		} else {
			is = documentiCollegatiDocAmmService.getStreamContabile(esercizio, cds, cdUo, pgFattura, Filtro_ricerca_doc_ammVBulk.DOC_ATT_GRUOP);
			if (is == null){
				is = getStreamNewDocument(actioncontext, esercizio, cds, cdUo, pgFattura);
			}
		}
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


	private InputStream getStreamNewDocument(ActionContext actioncontext,
			Integer esercizio, String cds, String cdUo, Long pgFattura)
			throws BusinessProcessException, ComponentException,
			RemoteException, PersistencyException {
		InputStream is;
		FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
				"CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
				FatturaAttivaSingolaComponentSession.class);
		UserContext userContext = actioncontext.getUserContext();
		Fattura_attivaBulk fattura = componentFatturaAttiva.ricercaFatturaByKey(userContext, esercizio.longValue(), cds, cdUo, pgFattura);			
		Document nodeDocument = componentFatturaAttiva.gestioneAllegatiPerFatturazioneElettronica(userContext, fattura);
		is = documentiCollegatiDocAmmService.getResource(nodeDocument);
		return is;
	}
	
	public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP() {
		this("");
	}

	public boolean isUtenteNonAbilitatoFirma(UserContext userContext) throws ApplicationException{
		try {
			return !UtenteBulk.isAbilitatoFirmaFatturazioneElettronica(userContext);
		} catch (ComponentException e) {
			// TODO Auto-generated catch block
			throw new ApplicationException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			throw new ApplicationException(e);
		}
	}

	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[3];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.excel");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.FirmaInvia");
		return toolbar;
	}

	public DocAmmFatturazioneElettronicaComponentSession createComponentSession()
			throws BusinessProcessException {
		return (DocAmmFatturazioneElettronicaComponentSession) createComponentSession(
				"CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession",
				DocAmmFatturazioneElettronicaComponentSession.class);
	}
	
	@SuppressWarnings("unchecked")
	public void firmaOTP(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
		UserContext userContext = context.getUserContext();
		List<OggettoBulk> lista = getSelectedElements(context);
		DocumentiCollegatiDocAmmService cmisService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
		DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();
		
//		FatturaAttivaSingolaComponentSession componentFatturaAttiva = Utility.createFatturaAttivaSingolaComponentSession();
		FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
				"CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
				FatturaAttivaSingolaComponentSession.class);
		
		for (Iterator<OggettoBulk> i = lista.iterator();i.hasNext();){
	    	OggettoBulk docAmm = i.next();
	    	if (docAmm instanceof Fattura_attivaBulk) {
	    		Fattura_attivaBulk fattura = (Fattura_attivaBulk) docAmm;
				logger.info("Processo la fattura");

		    	UnitaOrganizzativaPecBulk unitaOrganizzativaPecBulk = component.getAuthenticatorFromUo(userContext, fattura.getCd_uo_origine());
		    	
		    	if (unitaOrganizzativaPecBulk == null || unitaOrganizzativaPecBulk.getEmailPecProtocollo() == null || unitaOrganizzativaPecBulk.getCodPecProtocollo() == null){
					throw new ApplicationException("Impossibile procedere. Non è stato possibile recuperare l'email della PEC dell'istituto per l'invio della fattura elettronica!");
		    	}
				PasswordAuthentication authentication = new PasswordAuthentication(unitaOrganizzativaPecBulk.getEmailPecProtocollo(), unitaOrganizzativaPecBulk.getCodPecProtocolloInChiaro());
				logger.info("Recuperata Autenticazione PEC");
				File file = creaFileXml(userContext, fattura);

				logger.info("Creato file XML");
	    		fattura = protocollazione(userContext, fattura);
				logger.info("Creato protocollazione");

	    		List<CMISFile> cmisFileCreate = new ArrayList<CMISFile>();
	    		List<CMISFile> cmisFileAnnullati = new ArrayList<CMISFile>();
	    		try {
	    			CMISFile cmisFile = new CMISFileFatturaAttiva(file, fattura, 
	    					"application/xml","FAXA" + fattura.constructCMISNomeFile() + ".xml");
	    			
	    			if (cmisFile!=null) {
	    				//E' previsto solo l'inserimento ma non l'aggiornamento
	    				CMISPath path = cmisFile.getCMISParentPath(cmisService);
	    				try{
	    					Document node = cmisService.restoreSimpleDocument(
	    							cmisFile,
	    							cmisFile.getInputStream(),
	    							cmisFile.getContentType(),
	    							cmisFile.getFileName(), 
	    							path);
	    					logger.info("Salvato file XML sul Documentale");
	    					cmisService.addAspect(node, CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_ANTE_FIRMA.value());
	    					cmisService.makeVersionable(node);
	    					cmisFile.setDocument(node);
	    					cmisFileCreate.add(cmisFile);
	    				} catch (Exception e) {
	    					if (e.getCause() instanceof CmisConstraintException)
	    						throw new ApplicationException("CMIS - File ["+cmisFile.getFileName()+"] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
	    					throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + e.getMessage() + ")");
	    				}
	    				if (cmisFile.getDocument().getContentStreamLength() > 0){
		    				String nomeFile = file.getName();
		    				String nomeFileP7m = nomeFile+".p7m";
		    				String webScriptURL = documentiCollegatiDocAmmService.getRepositoyURL().concat("service/sigla/firma/fatture");
		    	    		String json = "{" +
		    	    				"\"nodeRefSource\" : \"" + cmisFile.getDocument().getProperty(SiglaCMISService.ALFCMIS_NODEREF).getValueAsString() + "\"," +
		    	    				"\"username\" : \"" + firmaOTPBulk.getUserName() + "\"," +
		    	    				"\"password\" : \"" + firmaOTPBulk.getPassword() + "\"," +
		    	    				"\"otp\" : \"" + firmaOTPBulk.getOtp() + "\""
		    	    				+ "}";
		    	    		try {		
		    	    			UrlBuilder url = new UrlBuilder(URIUtil.encodePath(webScriptURL));
		    					logger.info("Prima di firma file XML");
		    	    			Response response = documentiCollegatiDocAmmService.invokePOST(url, MimeTypes.JSON, json.getBytes("UTF-8"));
		    	    			int status = response.getResponseCode();
		    	    			if (status == HttpStatus.SC_NOT_FOUND
		    	    					|| status == HttpStatus.SC_INTERNAL_SERVER_ERROR
		    	    					|| status == HttpStatus.SC_UNAUTHORIZED
		    	    					|| status == HttpStatus.SC_BAD_REQUEST) {
			    					logger.info("Firma Errore");
		    	    				JSONTokener tokenizer = new JSONTokener(new StringReader(response.getErrorContent()));
		    	    			    JSONObject jsonObject = new JSONObject(tokenizer);
		    	    			    String jsonMessage = jsonObject.getString("message");
		    	    				throw new ApplicationException(FirmaOTPBulk.errorMessage(jsonMessage));
		    	    			} else {
			    					logger.info("Firma OK");
		    	    				JSONTokener tokenizer = new JSONTokener(new InputStreamReader(response.getStream()));
		    	    			    JSONObject jsonObject = new JSONObject(tokenizer);
		    	    			    Document nodeSigned = (Document) documentiCollegatiDocAmmService.getNodeByNodeRef(jsonObject.getString("nodeRef"));
			    					logger.info("Recuperato noderef file firmato dal documentale");
		    	    			    InputStream streamSigned = documentiCollegatiDocAmmService.getResource(nodeSigned);
			    					logger.info("Recuperato file firmato dal documentale");
		    	    				try {
		    	    					File fileSigned = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", nomeFileP7m);
		    	    					OutputStream outputStream = new FileOutputStream(fileSigned);
		    	    					IOUtils.copy(streamSigned, outputStream);
		    	    					outputStream.close();
				    					logger.info("Salvato file firmato temporaneo");
				    					if (!fattura.isNotaCreditoDaNonInviareASdi()){
					    					FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean("fatturaPassivaElettronicaService", FatturaPassivaElettronicaService.class);
			    	    			    	fatturaService.inviaFatturaElettronica(authentication.getUserName(), authentication.getPassword(), fileSigned, nomeFileP7m);
			    		    				fattura.setNomeFileInvioSdi(nomeFileP7m);
					    					logger.info("File firmato inviato");
				    					}
		    		    				componentFatturaAttiva.aggiornaFatturaInvioSDI(userContext, fattura);
		    	    				} catch (Exception ex) {
				    					logger.error("Errore nell'invio del file "+ ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()):ex.getMessage());
		    	    					documentiCollegatiDocAmmService.removeAspect(cmisFile.getDocument(),  CMISAspect.CNR_SIGNEDDOCUMENT.value());
	    	    						documentiCollegatiDocAmmService.deleteNode(nodeSigned);
		    	    					throw new ApplicationException("Errore nell'invio della mail PEC per la fatturazione elettronica. Ripetere l'operazione di firma!");
		    	    				}
		    	    			}
		    	    			commitUserTransaction();
		    	    		} catch (HttpException e) {
		    	    			throw new BusinessProcessException(e);
		    	    		} catch (IOException e) {
		    	    			throw new BusinessProcessException(e);
		    	    		} catch (Exception e) {
		    	    			throw new BusinessProcessException(e);
		    	    		}
	    				} else {
	    					logger.error("Errore. Il file XML salvato era vuoto.");
	    					throw new ApplicationException("Errore durante il processo di firma elettronica. Ripetere l'operazione di firma!");
	    				}
	    			}
	    		} catch (Exception e){
	    			//Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
//	    			for (CMISFile cmisFile : cmisFileCreate)
//	    				cmisService.deleteNode(cmisFile.getNode());
	    			for (CMISFile cmisFile : cmisFileAnnullati) {
	    				String cmisFileName = cmisFile.getFileName();
	    				String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".")+1);
	    				String stringToDelete = cmisFileName.substring(cmisFileName.indexOf("-ANNULLATO"));
	    				cmisFile.setFileName(cmisFileName.replace(stringToDelete, "."+cmisFileEstensione));
	    				cmisService.updateProperties(cmisFile, cmisFile.getDocument());
	    				cmisService.removeAspect(cmisFile.getDocument());
	    			}
	    			rollbackUserTransaction();
	    			extracted(e);
	    		}
    			componentFatturaAttiva.gestioneAllegatiPerFatturazioneElettronica(userContext,fattura);
			}
	    }
		setFocusedElement(context, null);
		refresh(context);	
	}

	public Fattura_attivaBulk protocollazione(UserContext userContext,
			Fattura_attivaBulk fattura) throws BusinessProcessException,
			ComponentException, RemoteException, PersistencyException {
//		FatturaAttivaSingolaComponentSession componentFatturaAttiva = Utility.createFatturaAttivaSingolaComponentSession();
		FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
				"CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
				FatturaAttivaSingolaComponentSession.class);

		Long pgProtocollazione = componentFatturaAttiva.callGetPgPerProtocolloIVA(userContext);
		Long pgStampa = componentFatturaAttiva.callGetPgPerStampa(userContext);
		Timestamp dataStampa = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate(); 
		Integer offSet = 0;
		componentFatturaAttiva.preparaProtocollazioneEProtocolla(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
		return componentFatturaAttiva.ricercaFatturaByKey(userContext, new Long(fattura.getEsercizio()), fattura.getCd_cds(), fattura.getCd_unita_organizzativa(), fattura.getPg_fattura_attiva());
	}
	private void extracted(Exception e) throws ApplicationException {
		throw new ApplicationException(e.getMessage());
	}
	/**
	 * DocumentiAmministrativiProtocollabiliBP constructor comment.
	 * @param function java.lang.String
	 */
	public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP(String function) {
		super(function+"Tr");
	}
	
	private File creaFileXml(UserContext userContext, Fattura_attivaBulk fattura) throws Exception {
		try {
			DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();
	
			JAXBElement<FatturaElettronicaType> fatturaType = component.creaFatturaElettronicaType(userContext, fattura);
			String nomeFile = component.recuperoNomeFileXml(userContext, fattura);
			File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",nomeFile);
			FileOutputStream fileOutputStream = new FileOutputStream(file);					
	
			JAXBContext jaxbContext = JAXBContext.newInstance("it.gov.fatturapa.sdi.fatturapa.v1");
			jaxbContext.createMarshaller().marshal(fatturaType, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			
			return file;
		} catch(Exception e) {
			throw new BusinessProcessException(e);
		}
	}
}
