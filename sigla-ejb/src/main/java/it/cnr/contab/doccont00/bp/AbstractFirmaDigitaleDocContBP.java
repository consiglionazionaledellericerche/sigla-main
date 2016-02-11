package it.cnr.contab.doccont00.bp;

import it.cnr.contab.cmis.MimeTypes;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.intcass.bulk.Apparence;
import it.cnr.contab.doccont00.intcass.bulk.PdfSignApparence;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteFirmaDettaglioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Log;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.bindings.impl.CmisBindingsHelper;
import org.apache.chemistry.opencmis.client.bindings.spi.http.Output;
import org.apache.chemistry.opencmis.client.bindings.spi.http.Response;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.GsonBuilder;

public abstract class AbstractFirmaDigitaleDocContBP extends ConsultazioniBP {
	private static final long serialVersionUID = 1L;
	protected SiglaCMISService cmisService;
	protected DocumentiContabiliService documentiContabiliService;
	private UtenteFirmaDettaglioBulk firmatario;
	private static final String ZIP_CONTENT = "service/zipper/zipContent";
	protected String controlloCodiceFiscale;
	private static final Log log = Log.getInstance(AbstractFirmaDigitaleDocContBP.class);
	
	public AbstractFirmaDigitaleDocContBP() {
		super();
	}

	public AbstractFirmaDigitaleDocContBP(String s) {
		super(s);
	}

	@Override
	public void setMultiSelection(boolean flag) {
		super.setMultiSelection(flag);
	}
	
	public void setColumnSet(ActionContext actioncontext, String statoTrasmissione) {
		String columnSetName = "firmaBase";
		if (statoTrasmissione.equalsIgnoreCase(StatoTrasmissione.ALL))
			columnSetName = "all";
		else if (!statoTrasmissione.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO))
			columnSetName = "firmaPredisposta";
		else if (statoTrasmissione.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
			columnSetName = "firmaEseguita";		
		setColumns(getBulkInfo().getColumnFieldPropertyDictionary(columnSetName));
		Unita_organizzativaBulk uoScrivania = CNRUserInfo.getUnita_organizzativa(actioncontext);			
		if (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0 ||
				uoScrivania.isUoCds()) {
			OrderedHashtable columns = (OrderedHashtable) getColumns();			
			columns = (OrderedHashtable) columns.clone();
			ColumnFieldProperty cd_unita_organizzativa = getBulkInfo().getColumnFieldProperty("cds_uo", "cd_unita_organizzativa"),
					cd_cds = getBulkInfo().getColumnFieldProperty("cds_uo", "cd_cds");
			if (cd_unita_organizzativa != null)
				columns.putFirst("cd_unita_organizzativa", getBulkInfo().getColumnFieldProperty("cds_uo", "cd_unita_organizzativa"));
			if (cd_cds != null && uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0)
				columns.putFirst("cd_cds", getBulkInfo().getColumnFieldProperty("cds_uo", "cd_cds"));
			setColumns(columns);
			table.setColumns(columns);			
		}		
	}
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] baseToolbar = super.createToolbar();
		Button[] toolbar = new Button[baseToolbar.length + 8];
		int i = 0;
		for (Button button : baseToolbar) {
			toolbar[i++] = button;
		}		
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.refresh");
		toolbar[i-1].setSeparator(true);
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.deletesign");
		toolbar[i-1].setSeparator(true);
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.predisponi");		
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.zipdocumenti");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.pdfdocumenti");
		toolbar[i-1].setSeparator(true);
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.detail");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.sign");
		toolbar[i-1].setSeparator(true);
		return toolbar;
	}
	
	@Override
	protected void init(Config config, ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			setBulkClassName(config.getInitParameter("bulkClassName"));
			OggettoBulk model = (OggettoBulk)getBulkInfo().getBulkClass().newInstance();
			((StatoTrasmissione)model).setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
			setModel(actioncontext, model);
			cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);	
			documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);	
			super.init(config, actioncontext);
			setColumnSet(actioncontext, MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);

			firmatario = ((UtenteComponentSession)createComponentSession("CNRUTENZE00_EJB_UtenteComponentSession",UtenteComponentSession.class)).
				isUtenteAbilitatoFirma(actioncontext.getUserContext(), getAbilitatoFirma());
			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
			controlloCodiceFiscale = sess.getVal01(actioncontext.getUserContext(), "CONTROLLO_CF_FIRMA_DOCCONT");
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (InstantiationException e) {
			throw handleException(e);
		} catch (IllegalAccessException e) {
			throw handleException(e);
		} catch (ClassNotFoundException e) {
			throw handleException(e);
		}
	}

	protected abstract AbilitatoFirma getAbilitatoFirma();
	
	@Override
	public abstract void openIterator(ActionContext actioncontext)
			throws BusinessProcessException;
	
	public it.cnr.jada.util.RemoteIterator find(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (getFindclause() != null)
				compoundfindclause.addChild(getFindclause());
			return EJBCommonServices.openRemoteIterator(context, 
					getComponentSession().cerca(context.getUserContext(), compoundfindclause, oggettobulk));
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	@Override
	public OggettoBulk createEmptyModelForFreeSearch(ActionContext context)
			throws BusinessProcessException {
		OggettoBulk bulk = super.createEmptyModelForFreeSearch(context);
		((StatoTrasmissione)bulk).setStato_trasmissione(((StatoTrasmissione)getModel()).getStato_trasmissione());
		return bulk;
	}
	
	protected CRUDComponentSession getComponentSession() {
		return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
	}
	
	public boolean isPredisponiButtonEnabled() {
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
		return oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
	}

	public boolean isDetailButtonEnabled() {
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
		return (!oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO));
	}

	public boolean isSignButtonEnabled() {
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
		if (firmatario == null)
			return false;		
		return oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO);
	}

	public boolean isDeleteButtonHidden() {
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();		
		if (oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO))
			return false;
		return true;
	}
	public boolean isDeleteSignButtonHidden() {
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
		if (firmatario == null)
			return true;
		if (oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
			return false;
		return true;		
	}

	public boolean isPdfDocumentiButtonHidden(){
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();		
		if (oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO))
			return false;
		return true;		
	}

	public boolean isZipDocumentiButtonHidden(){
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();		
		if (!oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO) &&
				!oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO))
			return false;
		return true;		
	}
	
	@SuppressWarnings("unchecked")
	public void eliminaPredisposizione(ActionContext context) throws BusinessProcessException {
		try {
			List<StatoTrasmissione> selectedElements = getSelectedElements(context);
			if (selectedElements == null || selectedElements.isEmpty())
					throw new ApplicationException("Selezionare almeno un elemento!");
			EJBCommonServices.closeRemoteIterator(getIterator());
			for (StatoTrasmissione v_mandato_reversaleBulk : selectedElements) {				
				aggiornaStato(context, MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO, v_mandato_reversaleBulk);
			}
			setMessage("Cancellazione effettuata correttamente.");
		} catch (ApplicationException e) {
			setMessage(e.getMessage());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}	
	}		
	public abstract void predisponiPerLaFirma(ActionContext actioncontext) throws BusinessProcessException;

	protected abstract void aggiornaStato(ActionContext actioncontext, String stato, StatoTrasmissione...bulks) throws ComponentException, RemoteException;
	
	@SuppressWarnings("unchecked")
	public void scaricaDocumenti(ActionContext actioncontext) throws Exception {
		setSelection(actioncontext);
		List<StatoTrasmissione> selectelElements = getSelectedElements(actioncontext);
		if (selectelElements == null || selectelElements.isEmpty()){
			((HttpActionContext)actioncontext).getResponse().setStatus(HttpStatus.SC_NO_CONTENT);
			return;
		}
		StatoTrasmissione oggettobulk = (StatoTrasmissione) getModel();
		if (oggettobulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO)) {
			PDFMergerUtility ut = new PDFMergerUtility();
			ut.setDestinationStream(new ByteArrayOutputStream());
			for (StatoTrasmissione cons : selectelElements) {
				InputStream isToAdd = documentiContabiliService.getStreamDocumento(
						cons.getEsercizio().intValue(), cons.getCd_cds(), cons.getPg_documento_cont(), cons.getCd_tipo_documento_cont());
				if (isToAdd != null)
					ut.addSource(isToAdd);
			}
			ut.mergeDocuments();
			InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
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
		} else {
			List<String> nodes = new ArrayList<String>();
			for (StatoTrasmissione cons : selectelElements) {
				nodes.addAll(documentiContabiliService.getNodeRefDocumento(cons, true));
			}						
	        UrlBuilder url = new UrlBuilder(cmisService
	                .getBaseURL().concat(ZIP_CONTENT));
	        url.addParameter("destination", cmisService.getNodeByPath("/User Homes/sigla").getPropertyValue("alfcmis:nodeRef"));
	        url.addParameter("filename", "Documenti contabili.zip");
	        url.addParameter("noaccent", true);
	        url.addParameter("getParent", true);
	        url.addParameter("download", false);
	        final JSONObject json = new JSONObject();
	        json.put("nodes", nodes);	                       
	        cmisService.getSiglaBindingSession().put(SessionParameter.READ_TIMEOUT, -1);
			Response resZipContent = CmisBindingsHelper.getHttpInvoker(cmisService.getSiglaBindingSession()).invokePOST(url, MimeTypes.JSON.mimetype(),
					new Output() {
						public void write(OutputStream out) throws Exception {
	            			out.write(json.toString().getBytes());
	            		}
	        		}, cmisService.getSiglaBindingSession());		
	        if (resZipContent.getResponseCode() != HttpStatus.SC_OK) {
				((HttpActionContext)actioncontext).getResponse().setStatus(HttpStatus.SC_NO_CONTENT);
				return;
	        }
	        JSONObject jsonObject = new JSONObject(IOUtils.toString(resZipContent.getStream()));
			InputStream is = ((Document)cmisService.getNodeByNodeRef(jsonObject.getString("nodeRef"))).getContentStream().getStream();
			if (is != null){
				((HttpActionContext)actioncontext).getResponse().setContentType("application/zip");
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
	}	
	public void scaricaDocumento(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
		String cds = ((HttpActionContext)actioncontext).getParameter("cds");
		Long numero_documento = Long.valueOf(((HttpActionContext)actioncontext).getParameter("numero_documento"));
		String tipo = ((HttpActionContext)actioncontext).getParameter("tipo");
		InputStream is = documentiContabiliService.getStreamDocumento(esercizio, cds, numero_documento, tipo);
		if (is == null) {
			log.error("CMIS Object not found: " + esercizio + cds + numero_documento + tipo);
			is = this.getClass().getResourceAsStream("/cmis/404.pdf");
		}
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

	@SuppressWarnings("unchecked")
	public void sign(ActionContext actioncontext, FirmaOTPBulk firmaOTPBulk) throws Exception {
		List<StatoTrasmissione> selectelElements = getSelectedElements(actioncontext);
		if (selectelElements == null || selectelElements.isEmpty()){
			throw new ApplicationException("Selezionare almeno un documento contabile!");
		}
		EJBCommonServices.closeRemoteIterator(getIterator());
		addSomethingToSelectedElements(actioncontext, selectelElements);
		executeSign(actioncontext, selectelElements, firmaOTPBulk);
	}
	
	protected void executeSign(ActionContext actioncontext, List<StatoTrasmissione> selectelElements, FirmaOTPBulk firmaOTPBulk) throws Exception{
		EJBCommonServices.closeRemoteIterator(getIterator());		
		String webScriptURL = documentiContabiliService.getRepositoyURL().concat("service/sigla/firma/doccont");
		Map<String, String> subjectDN = documentiContabiliService.getCertSubjectDN(firmaOTPBulk.getUserName(), firmaOTPBulk.getPassword());
		if (subjectDN == null)
			throw new ApplicationException("Errore nella lettura dei certificati!\nVerificare Nome Utente e Password!");
		String codiceFiscale = subjectDN.get("SERIALNUMBER").substring(3);
		UtenteBulk utente = ((CNRUserInfo)actioncontext.getUserInfo()).getUtente();
		if (controlloCodiceFiscale != null && controlloCodiceFiscale.equalsIgnoreCase("Y") && utente.getCodiceFiscaleLDAP() != null && !codiceFiscale.equalsIgnoreCase(utente.getCodiceFiscaleLDAP())) {
			throw new ApplicationException("Il codice fiscale \"" + codiceFiscale + "\" presente sul certicato di Firma, " +
					"è diverso da quello dell'utente collegato \"" + utente.getCodiceFiscaleLDAP() +"\"!");
		}
		List<String> nodes = new ArrayList<String>();
		for (StatoTrasmissione bulk : selectelElements) {
			nodes.addAll(documentiContabiliService.getNodeRefDocumento(bulk, true));
		}		
		PdfSignApparence pdfSignApparence = new PdfSignApparence();
		pdfSignApparence.setNodes(nodes);
		pdfSignApparence.setUsername(firmaOTPBulk.getUserName());
		pdfSignApparence.setPassword(firmaOTPBulk.getPassword());
		pdfSignApparence.setOtp(firmaOTPBulk.getOtp());
		
		Apparence apparence = new Apparence(
				null, 
				"Rome", "Firma documento contabile",
				getTitoloFirma() + "\n" + getTitolo() + 
						subjectDN.get("GIVENNAME") + " " + subjectDN.get("SURNAME"), 
				0, 40, 1, 300, 80);
		pdfSignApparence.setApparence(apparence);
		String json = new GsonBuilder().create().toJson(pdfSignApparence);
		try {		
			UrlBuilder url = new UrlBuilder(URIUtil.encodePath(webScriptURL));
			Response response = documentiContabiliService.invokePOST(url, MimeTypes.JSON, json.getBytes("UTF-8"));
			int status = response.getResponseCode();
			if (status == HttpStatus.SC_NOT_FOUND
					|| status == HttpStatus.SC_INTERNAL_SERVER_ERROR
					|| status == HttpStatus.SC_UNAUTHORIZED
					|| status == HttpStatus.SC_BAD_REQUEST) {
				JSONTokener tokenizer = new JSONTokener(new StringReader(response.getErrorContent()));
			    JSONObject jsonObject = new JSONObject(tokenizer);
			    String jsonMessage = jsonObject.getString("message");
				throw new ApplicationException(FirmaOTPBulk.errorMessage(jsonMessage));
			} else {
				aggiornaStato(actioncontext, MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA, selectelElements.toArray(new StatoTrasmissione[selectelElements.size()]));
			}
			setMessage("Firma effettuata correttamente.");
		} catch (HttpException e) {
			throw new BusinessProcessException(e);
		} catch (IOException e) {
			throw new BusinessProcessException(e);
		} catch (Exception e) {
			throw new BusinessProcessException(e);
		} 		
	}
	
	protected void addSomethingToSelectedElements(ActionContext actioncontext, List<StatoTrasmissione> selectelElements) throws BusinessProcessException{		
	}

	protected abstract String getTitoloFirma();
	private String getTitolo() {
		if (firmatario.getTitoloFirma().equalsIgnoreCase(UtenteFirmaDettaglioBulk.TITOLO_FIRMA_DELEGATO))
			return "";
		return UtenteFirmaDettaglioBulk.titoloKeys.get(firmatario.getTitoloFirma()) + "\n";
	}

	public void invia(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
		
	}
}