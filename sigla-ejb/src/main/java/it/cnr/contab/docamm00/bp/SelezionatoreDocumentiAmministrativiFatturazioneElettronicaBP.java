package it.cnr.contab.docamm00.bp;


import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.SessionCredential;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.docamm00.cmis.CMISDocAmmAspect;
import it.cnr.contab.docamm00.cmis.CMISFileFatturaAttiva;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP extends SelezionatoreListaBP {
	private static final long serialVersionUID = 1L;
	private DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService;
	private boolean contabiliEnabled;
	
	@Override
	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		super.init(config, context);
		documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService",DocumentiCollegatiDocAmmService.class);	
	}

	public boolean isContabiliEnabled(){
		return contabiliEnabled;
	}
	
	public void scaricaDocumentiCollegati(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
		String cds = ((HttpActionContext)actioncontext).getParameter("cds");
		String cdUo = ((HttpActionContext)actioncontext).getParameter("cdUo");
		Long pgFattura = Long.valueOf(((HttpActionContext)actioncontext).getParameter("pgFattura"));
		InputStream is = documentiCollegatiDocAmmService.getStreamContabile(esercizio, cds, cdUo, pgFattura, Filtro_ricerca_doc_ammVBulk.DOC_ATT_GRUOP);
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
	
	public SelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP() {
		this("");
	}

	public boolean isUtenteNonAbilitatoFirma(){
		return false;
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
		for (Iterator<OggettoBulk> i = lista.iterator();i.hasNext();){
	    	OggettoBulk docAmm = i.next();
	    	if (docAmm instanceof Fattura_attivaBulk) {
	    		Fattura_attivaBulk fattura = (Fattura_attivaBulk) docAmm;
				File file = creaFileXml(userContext, fattura);

				List<CMISFile> cmisFileCreate = new ArrayList<CMISFile>();
	    		List<CMISFile> cmisFileAnnullati = new ArrayList<CMISFile>();
	    		try {
	    			CMISFile cmisFile = new CMISFileFatturaAttiva(file, fattura, 
	    					"application/xml","FAXA" + fattura.constructCMISNomeFile() + ".xml");
	    			
	    			if (cmisFile!=null) {
	    				//E' previsto solo l'inserimento ma non l'aggiornamento
	    				CMISPath path = cmisFile.getCMISParentPath(cmisService);
	    				try{
	    					Node node = cmisService.restoreSimpleDocument(cmisFile, 
	    							cmisFile.getInputStream(),
	    							cmisFile.getContentType(),
	    							cmisFile.getFileName(), 
	    							path);
	    					cmisService.addAspect(node, CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_ANTE_FIRMA.value());
	    					cmisService.makeVersionable(node);
	    					cmisFile.setNode(node);
	    					cmisFileCreate.add(cmisFile);
	    				} catch (Exception e) {
	    					if (e.getCause() instanceof CmisConstraintException)
	    						throw new ApplicationException("CMIS - File ["+cmisFile.getFileName()+"] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
	    					throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + e.getMessage() + ")");
	    				}
	    	    		String webScriptURL = documentiCollegatiDocAmmService.getRepositoyURL().concat("/sigla/firma/fatture");
	    	    		String json = "{" +
	    	    				"\"nodeRefSource\" : \"" + cmisFile.getNode().getVersionSeriesId() + "\"," +
	    	    				"\"username\" : \"" + firmaOTPBulk.getUserName() + "\"," +
	    	    				"\"password\" : \"" + firmaOTPBulk.getPassword() + "\"," +
	    	    				"\"otp\" : \"" + firmaOTPBulk.getOtp() + "\""
	    	    				+ "}";
	    	    		PostMethod method = null;
	    	    		try {		
	    	    			method = new PostMethod(URIUtil.encodePath(webScriptURL));
	    	    			method.setRequestEntity(new StringRequestEntity(json, "application/json", "UTF-8"));
	    	    			HttpClient client = documentiCollegatiDocAmmService.getHttpClient();
	    	    			client.getState().setCredentials(AuthScope.ANY, 
	    	    					((SessionCredential)documentiCollegatiDocAmmService.getCredentials()).getUsernamePasswordCredentials());
	    	    			client.getParams().setAuthenticationPreemptive(true);
	    	    			client.executeMethod(method);
	    	    			int status = method.getStatusCode();
	    	    			if (status == HttpStatus.SC_NOT_FOUND
	    	    					|| status == HttpStatus.SC_INTERNAL_SERVER_ERROR
	    	    					|| status == HttpStatus.SC_UNAUTHORIZED
	    	    					|| status == HttpStatus.SC_BAD_REQUEST) {
	    	    				JSONTokener tokenizer = new JSONTokener(new InputStreamReader(method.getResponseBodyAsStream()));
	    	    			    JSONObject jsonObject = new JSONObject(tokenizer);
	    	    			    String jsonMessage = jsonObject.getString("message");
	    	    				throw new ApplicationException(FirmaOTPBulk.errorMessage(jsonMessage));
	    	    			}
	    	    		} catch (HttpException e) {
	    	    			throw new BusinessProcessException(e);
	    	    		} catch (IOException e) {
	    	    			throw new BusinessProcessException(e);
	    	    		} catch (Exception e) {
	    	    			throw new BusinessProcessException(e);
	    	    		} finally {
	    	    			if (method != null)
	    	    				method.releaseConnection();
	    	    		}
	    			}
	    		} catch (Exception e){
	    			//Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
	    			for (CMISFile cmisFile : cmisFileCreate)
	    				cmisService.deleteNode(cmisFile.getNode());
	    			for (CMISFile cmisFile : cmisFileAnnullati) {
	    				String cmisFileName = cmisFile.getFileName();
	    				String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".")+1);
	    				String stringToDelete = cmisFileName.substring(cmisFileName.indexOf("-ANNULLATO"));
	    				cmisFile.setFileName(cmisFileName.replace(stringToDelete, "."+cmisFileEstensione));
	    				cmisService.updateProperties(cmisFile, cmisFile.getNode());
	    				cmisService.removeAspect(cmisFile.getNode());
	    			}
	    			extracted(e);
	    		}
			}
	    }
		setFocusedElement(context, null);
		refresh(context);	
	}
	private void extracted(Exception e) throws ApplicationException {
		throw new ApplicationException(e.getMessage());
	}
	/**
	 * DocumentiAmministrativiProtocollabiliBP constructor comment.
	 * @param function java.lang.String
	 */
	public SelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP(String function) {
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