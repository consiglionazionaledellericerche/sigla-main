package it.cnr.contab.doccont00.bp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.bindings.spi.http.Response;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.GsonBuilder;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.cmis.MimeTypes;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.intcass.xmlbnl.FlussoOrdinativi;
import it.cnr.contab.doccont00.intcass.xmlbnl.Mandato;
import it.cnr.contab.doccont00.intcass.xmlbnl.Reversale;
import it.cnr.contab.doccont00.intcass.xmlbnl.ObjectFactory;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteFirmaDettaglioBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import org.apache.commons.io.IOUtils;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Distinta Cassiere
 * @version 1.1 by Aurelio D'Amico
 * [08/11/2006] conversione stampa Crystal in Jasper Reports
 */
public class CRUDDistintaCassiereBP extends it.cnr.jada.util.action.SimpleCRUDBP   {
	private final RemoteDetailCRUDController distintaCassDet = new RemoteDetailCRUDController("DistintaCassDet", it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk.class,"distinta_cassiere_detColl","CNRDOCCONT00_EJB_DistintaCassiereComponentSession",this);
	private final RemoteDetailCRUDController distinteCassCollegateDet = new RemoteDetailCRUDController("DistinteCassCollegateDet", it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk.class,"distinte_cassiere_detCollegateColl","CNRDOCCONT00_EJB_DistintaCassiereComponentSession",this);
	private Parametri_cnrBulk parametriCnr;
	public boolean elencoConUo;
	public Boolean flusso;
	public Boolean sepa;
	private String file;
	private Unita_organizzativaBulk uoSrivania;
	protected SiglaCMISService cmisService;
	protected DocumentiContabiliService documentiContabiliService;
	private UtenteFirmaDettaglioBulk firmatarioDistinta;
	protected String controlloCodiceFiscale;
public CRUDDistintaCassiereBP() {
	super("Tn");

}
public CRUDDistintaCassiereBP(String function) {
	super(function+"Tn");

}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	Button[] toolbar = super.createToolbar();
	
	
	if (this.getParametriCnr().getFl_tesoreria_unica().booleanValue() &&!isFlusso()){
		Button[] newToolbarTesoreria = new Button[ toolbar.length + 3];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbarTesoreria[i] = toolbar[i];
		newToolbarTesoreria[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampaProv");
		newToolbarTesoreria[ i +1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.firma");
		newToolbarTesoreria[ i +2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.downloadFirmato");
		return newToolbarTesoreria;
	}
	if (this.getParametriCnr().getFl_tesoreria_unica().booleanValue() &&isFlusso()){
		Button[] newToolbarTesoreria = new Button[ toolbar.length + 4];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbarTesoreria[i] = toolbar[i];
		newToolbarTesoreria[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampaProv");
		newToolbarTesoreria[ i +1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.firma");
		newToolbarTesoreria[ i +2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.downloadnew");
		newToolbarTesoreria[ i +3] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.downloadFirmatoP7m");
		return newToolbarTesoreria;
	}
	else
		if (this.isFlusso()){
			Button[] newToolbar = new Button[ toolbar.length + 2];
			int i;
			for ( i = 0; i < toolbar.length; i++ )
				newToolbar[i] = toolbar[i];
			newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.flusso");
			newToolbar[ i +1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.download");
			return newToolbar;
		}
		else
			return toolbar;
}
/** 
  * Viene richiesta alla component che gestisce la distinta cassiere di calcolare gli importi totali
  *	 della distinta
  */
public void calcolaTotali(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		Distinta_cassiereBulk distinta = ((DistintaCassiereComponentSession)createComponentSession()).calcolaTotali( context.getUserContext(), (Distinta_cassiereBulk)getModel() );
		setModel(context, distinta);
	} catch(Exception e) {
		throw handleException(e);
	}	
}
/** 
  * Viene richiesta alla component che gestisce la distinta cassiere di ricercare mandati e reversali
  */
public RemoteIterator findMandatiEReversali(ActionContext context, CompoundFindClause clauses, V_mandato_reversaleBulk model, Distinta_cassiereBulk contesto) throws it.cnr.jada.action.BusinessProcessException {
	try 
	{
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( context,((DistintaCassiereComponentSession)createComponentSession()).cercaMandatiEReversali(context.getUserContext(),clauses,model, contesto));
	} catch(Exception e) 
	{
		throw handleException(e);
	}
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.RemoteDetailCRUDController getDistintaCassDet() {
	return distintaCassDet;
}
protected void init( it.cnr.jada.action.Config config,ActionContext context) throws BusinessProcessException 
{
try {
	context.getBusinessProcess("/GestioneUtenteBP").removeChild("CRUDDistintaCassiereBP");
	this.setFlusso(new Boolean(config.getInitParameter("flusso")));
	this.setSepa(new Boolean(config.getInitParameter("sepa")));
	setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(),it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context)));
		if (this.getParametriCnr().getFl_tesoreria_unica().booleanValue() &&!isUoDistintaTuttaSac(context))
			throw new ApplicationException("Funzione non abilitata per la uo");
		else 
			isUoDistintaTuttaSac(context);
		cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);	
		documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);	
		
		firmatarioDistinta = ((UtenteComponentSession)createComponentSession("CNRUTENZE00_EJB_UtenteComponentSession",UtenteComponentSession.class)).
					isUtenteAbilitatoFirma(context.getUserContext(), AbilitatoFirma.DIST);
		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		controlloCodiceFiscale = sess.getVal01(context.getUserContext(), "CONTROLLO_CF_FIRMA_DOCCONT");
			
	} catch (ComponentException e) {
		throw handleException(e);
	} catch (RemoteException e) {
		throw handleException(e);
	}
	super.init( config, context );
	
}
/* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
protected void initializePrintBP(AbstractPrintBP bp) 
{
	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
	Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)getModel();

	//ver1.1 (start)
	//printbp.setReportName("/doccont/doccont/distinta_cassiere.rpt");	
	//printbp.setReportParameter(0,distinta.getEsercizio().toString());
	//printbp.setReportParameter(1,distinta.getCd_cds());
	//printbp.setReportParameter(2,distinta.getCd_unita_organizzativa());
	//printbp.setReportParameter(3,distinta.getPg_distinta().toString());
    if(distinta.getPg_distinta_def()!=null)
    	printbp.setReportName("/doccont/doccont/distinta_cassiere.jasper");
    else
    	printbp.setReportName("/doccont/doccont/distinta_cassiere_provvisoria.jasper");
	Print_spooler_paramBulk param = new Print_spooler_paramBulk();
	param.setNomeParam("esercizio");
	param.setValoreParam(distinta.getEsercizio().toString());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	param = new Print_spooler_paramBulk();
	param.setNomeParam("cd_cds");
	param.setValoreParam(distinta.getCd_cds());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	param = new Print_spooler_paramBulk();
	param.setNomeParam("cd_unita_organizzativa");
	param.setValoreParam(distinta.getCd_unita_organizzativa());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	param = new Print_spooler_paramBulk();
	param.setNomeParam("pg_distinta");
	param.setValoreParam(distinta.getPg_distinta().toString());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	//ver1.1 (end)	
}
/**
 *	Abilito il bottone di aggiunta doc. contabili solo se la distinta e' in fase di modifica/inserimento
 * 	e la data di invio è nulla.
 *
 *	isEditable 	= FALSE se la distinta e' in visualizzazione
 *				= TRUE se la distinta e' in modifica/inserimento
 */
public boolean isAddDocContabiliButtonEnabled() 
{
	return isEditable() &&
		   !((Distinta_cassiereBulk)getModel()).isCreateByOtherUo() &&
	       (isInserting() ||
		   (isEditing() && 
		   ((Distinta_cassiereBulk)getModel()).getDt_invio() == null ));
	    
}
/**
 *	Abilito il bottone di delete se la data di invio della distinta è nulla.
 *
 *	isEditable 	= FALSE se la distinta e' in visualizzazione
 *				= TRUE se la distinta e' in modifica/inserimento
 */
public boolean isDeleteButtonEnabled() 
{
	return super.isDeleteButtonEnabled() && 
		   !((Distinta_cassiereBulk)getModel()).isCreateByOtherUo() &&
		   ((Distinta_cassiereBulk)getModel()).getDt_invio() == null ;

}
public boolean isPrintButtonHidden() 
{
	if (((Distinta_cassiereBulk)getModel()).getPg_distinta_def() == null )
		return true;
	return super.isPrintButtonHidden() || isInserting() || isSearching();
}
public boolean isPrintProvButtonHidden() 
{
	if (((Distinta_cassiereBulk)getModel()).getPg_distinta_def() != null )
		return true;
	return super.isPrintButtonHidden() || isInserting() || isSearching();
}
/**
 *	Abilito il bottone di eliminazione doc. contabili solo se la distinta e' in fase di modifica/inserimento
 * 	e la data di invio è nulla.
 *
 *	isEditable 	= FALSE se la distinta e' in visualizzazione
 *				= TRUE se la distinta e' in modifica/inserimento
 */
public boolean isRemoveDocContabiliButtonEnabled() 
{
	return isEditable() &&
			!((Distinta_cassiereBulk)getModel()).isCreateByOtherUo() &&
    		(isInserting() || 
			(isEditing()  && ((Distinta_cassiereBulk)getModel()).getDt_invio() == null)) ;
}
/**
 *	Abilito il bottone di save solo se la distinta e' in fase di modifica/inserimento
 * 	e la data di invio è nulla.
 *
 *	isEditable 	= FALSE se la distinta e' in visualizzazione
 *				= TRUE se la distinta e' in modifica/inserimento
 */
public boolean isSaveButtonEnabled() 
{
	return isEditable() &&
		   !((Distinta_cassiereBulk)getModel()).isCreateByOtherUo() &&
   		   ( isInserting() ||
	       ( isEditing() &&
		   ((Distinta_cassiereBulk)getModel()).getDt_invio() == null)) ;
	    
}
/**
 *	Abilito il bottone di visualizzazione dettagli totali solo se la distinta e' in fase di modifica/inserimento
 *
 *	isEditable 	= FALSE se la distinta e' in visualizzazione
 *				= TRUE se la distinta e' in modifica/inserimento
 */
public boolean isVisualizzaDettagliTotaliButtonEnabled()
{
	return isEditable() && (isInserting() || isEditing());
}	
/**
 *	Abilito il bottone di visualizzazione dettagli totali trasmessi solo se la distinta e' in fase di modifica
 *
 *	isEditable 	= FALSE se la distinta e' in visualizzazione
 *				= TRUE se la distinta e' in modifica
 */
public boolean isVisualizzaDettagliTotaliTrasmessiButtonEnabled()
{
	return isEditable();
}	
protected void resetTabs(ActionContext context) {
	setTab( "tab", "tabDistinta");
}
/**
 * controlla se i mandati di versamento cori/iva accentrati sono stati selezionati per la cancellazione dalla distinta
 */
public void controllaEliminaMandati(ActionContext context) throws BusinessProcessException {

	if (isInserisciMandatiVersamentoCori(context)) {
		it.cnr.jada.util.action.RemoteDetailCRUDController rdc = getDistintaCassDet();
		int[] sel = rdc.getSelectedRows(context);
		V_mandato_reversaleBulk doc;
		// controllo prima il selezionato col focus
		int k = rdc.getSelection().getFocus();
		rdc.setModelIndex(context,k);
		doc = (V_mandato_reversaleBulk) rdc.getModel();
		if (doc!=null&&doc.getVersamento_cori()!=null&&doc.getVersamento_cori().booleanValue())
			throw new it.cnr.jada.action.MessageToUser("Non è possibile eliminare i mandati di versamento CORI/IVA accentrati dalla distinta!"); 
		// controllo poi i selezionati con flag
		for (int i=0;i<sel.length;i++) {
			rdc.setModelIndex(context,sel[i]);
			doc = (V_mandato_reversaleBulk) rdc.getModel();
			if (doc!=null&&doc.getVersamento_cori()!=null&&doc.getVersamento_cori().booleanValue()) {
				throw new it.cnr.jada.action.MessageToUser("Non è possibile eliminare i mandati di versamento CORI/IVA accentrati dalla distinta!"); 
			}
		}
	
	}
}
/**
 * è vero se è stato impostato il flag nei parametri generali FL_VERSAMENTO_CORI
 * che indica se inserire i mandati di versamento CORI in modo obbligatorio e automatico
 */
public boolean isInserisciMandatiVersamentoCori(it.cnr.jada.action.ActionContext context) throws BusinessProcessException {
	if (getParametriCnr()==null) {
		try {
			setParametriCnr(((DistintaCassiereComponentSession)createComponentSession()).parametriCnr(
				context.getUserContext()));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		}
	}

	Calendar cal = Calendar.getInstance();
	int day = cal.get(Calendar.DAY_OF_MONTH);
	
	if (getParametriCnr().getFl_versamenti_cori().booleanValue()&&
		day>=getParametriCnr().getVersamenti_cori_giorno().intValue())
		return(true);
	else
		return(false);
}
/**
 * @return
 */
public Parametri_cnrBulk getParametriCnr() {
	return parametriCnr;
}

/**
 * @param bulk
 */
public void setParametriCnr(Parametri_cnrBulk bulk) {
	parametriCnr = bulk;
}
public boolean isUoDistintaTuttaSac(ActionContext context) throws ComponentException, RemoteException{
	Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	if ( sess.getVal01(context.getUserContext(), new Integer(0), null, "UO_SPECIALE", "UO_DISTINTA_TUTTA_SAC") == null)
		throw new ApplicationException("Configurazione CNR: non sono stati impostati i valori per UO SPECIALE - UO DISTINTA TUTTA SAC");	
	if (sess.getVal01(context.getUserContext(), new Integer(0), null, "UO_SPECIALE", "UO_DISTINTA_TUTTA_SAC").equals(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa()))
	{
		setElencoConUo(true);
		return true;
	}		
	setElencoConUo(false);
	return false;
}
public boolean isElencoConUo() {
	return elencoConUo;
}
public void setElencoConUo(boolean elencoConUo) {
	this.elencoConUo = elencoConUo;
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.RemoteDetailCRUDController getDistinteCassCollegateDet() {
	return distinteCassCollegateDet;
}
public boolean isUoEnte(){
	return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
}
public Unita_organizzativaBulk getUoSrivania() {
	return uoSrivania;
}
public void setUoSrivania(Unita_organizzativaBulk bulk) {
	uoSrivania = bulk;
}
protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
	super.initialize(actioncontext);
	setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext));
	if(this.isEditable()){
		((Distinta_cassiereBulk)this.getModel()).setFl_flusso(flusso);
		((Distinta_cassiereBulk)this.getModel()).setFl_sepa(sepa);
	}		
}

public OggettoBulk createNewSearchBulk(ActionContext context) throws BusinessProcessException {
	Distinta_cassiereBulk fs = (Distinta_cassiereBulk)super.createNewSearchBulk(context);
	this.setFile(null);
	if(this.isEditable()){
		fs.setFl_flusso(isFlusso());
		fs.setFl_sepa(isSepa());
	}		
	return fs;
}

public Boolean isFlusso() {
	return flusso;
}
public void setFlusso(Boolean flusso) {
	this.flusso = flusso;
}
public boolean isScaricaButtonEnabled() {
	if(!isEstraiButtonHidden()&& getFile()!=null)
		return true;
	else
		return false;
}
public boolean isEstraiButtonHidden() {
	 return (((Distinta_cassiereBulk)getModel()).getDt_invio() == null); 
}
public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
	Button[] toolbar = getToolbar();
	if(getFile()!=null){
		HttpServletResponse httpservletresp = (HttpServletResponse)pagecontext.getResponse();
		HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
	    StringBuffer stringbuffer = new StringBuffer();
	    stringbuffer.append(pagecontext.getRequest().getScheme());
	    stringbuffer.append("://");
	    stringbuffer.append(pagecontext.getRequest().getServerName());
	    stringbuffer.append(':');
	    stringbuffer.append(pagecontext.getRequest().getServerPort());
	    stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
	    toolbar[10].setHref("javascript:doPrint('"+stringbuffer+getFile()+ "')");
	}
	super.writeToolbar(pagecontext);
}
public String getFile() {
	return file;
}
public void setFile(String file) {
	this.file = file;
}
public void generaXML(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException{
   try{
	     
	    JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.doccont00.intcass.xmlbnl");
		ObjectFactory obj = new ObjectFactory();
		//creo i file del flusso
		
		// Testata
		FlussoOrdinativi currentFlusso = null;
		currentFlusso = obj.createFlussoOrdinativi();
		
		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		if ( sess.getVal01(context.getUserContext(),it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context), null, "FLUSSO_ORDINATIVI", "CODICE_ABI_BT") == null)
				throw new ApplicationException("Configurazione mancante per flusso Ordinativo");
		Distinta_cassiereBulk distinta =(Distinta_cassiereBulk)this.getModel();
		String CodiceAbi=sess.getVal01(context.getUserContext(),it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context), null, "FLUSSO_ORDINATIVI", "CODICE_ABI_BT");
		currentFlusso.setCodiceABIBT(CodiceAbi);
		currentFlusso.setIdentificativoFlusso(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context).toString()+"-"+distinta.getCd_unita_organizzativa()+"-"+distinta.getPg_distinta_def().toString()+"-I");// Inserito "I" alla fine in caso di gestione Rinvio
		GregorianCalendar gcdi = new GregorianCalendar();
		gcdi.setTimeInMillis(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp().getTime());
		currentFlusso.setDataOraCreazioneFlusso(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(gcdi.get(Calendar.YEAR), gcdi.get(Calendar.MONTH), gcdi.get(Calendar.DAY_OF_MONTH),gcdi.get(Calendar.HOUR_OF_DAY), gcdi.get(Calendar.MINUTE), gcdi.get(Calendar.SECOND))));
		
		ExtCassiereCdsBulk extcas=((DistintaCassiereComponentSession)createComponentSession()).recuperaCodiciCdsCassiere(context.getUserContext(), (Distinta_cassiereBulk)getModel());
		 
		currentFlusso.setCodiceEnte(Formatta(extcas.getCodiceProto(),"D",6,"0"));
		currentFlusso.setDescrizioneEnte(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getDs_unita_organizzativa());
		BancaBulk banca =((DistintaCassiereComponentSession)createComponentSession()).recuperaIbanUo(context.getUserContext(), ((Distinta_cassiereBulk)getModel()).getUnita_organizzativa()); 
		currentFlusso.setCodiceEnteBT(currentFlusso.getCodiceEnte()+"-"+banca.getCodice_iban()+ "-"+extcas.getCodiceSia());
		currentFlusso.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		
		List dettagliRev=((DistintaCassiereComponentSession)createComponentSession()).dettagliDistinta(context.getUserContext(), distinta, it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV);
		// Elaboriamo prima le reversali
		Reversale currentReversale=null;
		for(Iterator i=dettagliRev.iterator();i.hasNext();){
	    	V_mandato_reversaleBulk bulk=(V_mandato_reversaleBulk) i.next();
	    		currentReversale=(Reversale)((DistintaCassiereComponentSession)createComponentSession()).recuperaDatiReversaleFlusso(context.getUserContext(), bulk);
	    		if(bulk.getTi_cc_bi().compareTo(SospesoBulk.TIPO_BANCA_ITALIA)==0){
	    			// bisogna aggiornare l'iban se banca d'italia ma lo posso sapere solo in questo punto 
	    			Liquid_coriComponentSession component = (Liquid_coriComponentSession)this.createComponentSession("CNRCORI00_EJB_Liquid_coriComponentSession",Liquid_coriComponentSession.class );
	    			currentFlusso.setCodiceEnteBT(currentFlusso.getCodiceEnte()+"-"+
	    					component.getContoSpecialeEnteF24(context.getUserContext())+ "-"+extcas.getCodiceSia());
	    		}
	    		currentFlusso.getReversale().add(currentReversale);
	    }
		List dettagliMan=((DistintaCassiereComponentSession)createComponentSession()).dettagliDistinta(context.getUserContext(), distinta, it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN);
		//Mandati
		Mandato currentMandato=null;
		for(Iterator i=dettagliMan.iterator();i.hasNext();){
	    	V_mandato_reversaleBulk bulk=(V_mandato_reversaleBulk) i.next();
	    		currentMandato=(Mandato)((DistintaCassiereComponentSession)createComponentSession()).recuperaDatiMandatoFlusso(context.getUserContext(), bulk);
	    		currentFlusso.getMandato().add(currentMandato);
	    } 
		String fileName =currentFlusso.getIdentificativoFlusso()+".xslt";
		File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",fileName);
		 
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		
		Marshaller jaxbMarshaller = jc.createMarshaller(); 
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );	
		jaxbMarshaller.marshal(currentFlusso, fileOutputStream);
		
		fileOutputStream.flush();
		fileOutputStream.close();
		setFile("/tmp/"+file.getName());	  
		setMessage("Flusso generato");
   } 
   catch (Exception e){
	   throw handleException(e);
   }
 }
public String Formatta(String s, String allineamento,Integer dimensione,String riempimento){
	if (s==null)
		s=riempimento;
	if (s.length()< dimensione){
		if (allineamento.compareTo("D")==0){
			while (s.length()<dimensione)
			 s=riempimento+s;
		   return s.toUpperCase();
		}
		else
		{
			while (s.length()<dimensione)
				 s=s+riempimento;
			return s.toUpperCase();
		}
	}else if (s.length()> dimensione){
		s=s.substring(0,dimensione);
		return s.toUpperCase();
	}
	return s.toUpperCase();
}
public boolean isSignButtonEnabled() {
	if ( super.isDeleteButtonEnabled() &&((Distinta_cassiereBulk)getModel()).getDt_invio() == null )
		return true;
	else 
		return false;
}
public void scaricaDocumento(ActionContext actioncontext) throws Exception {
	Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
	String cds = ((HttpActionContext)actioncontext).getParameter("cds");
	Long numero_documento = Long.valueOf(((HttpActionContext)actioncontext).getParameter("numero_documento"));
	String tipo = ((HttpActionContext)actioncontext).getParameter("tipo");
	InputStream is = documentiContabiliService.getStreamDocumento(esercizio, cds, numero_documento, tipo);
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
public void inviaDistinta(ActionContext context, Distinta_cassiereBulk distinta) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
{	
	try {		
	//Invio al cassiere serve per salvare in definitivo la distinta prima di archiviare la stampa e firmarla		
		 it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession distintaComp = (it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession) createComponentSession();
    	 distinta=distintaComp.inviaDistinta(context.getUserContext(), distinta);	
    	 commitUserTransaction();
         try
         {
             basicEdit(context, getModel(), true);
         }catch(BusinessProcessException businessprocessexception)
         {
             setModel(context, null);
             setDirty(false);
             throw businessprocessexception;
         }
        Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Print_spoolerBulk print = new Print_spoolerBulk();
		print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
		print.setFlEmail(false);
		
		print.setReport("/doccont/doccont/distinta_cassiere.jasper");
		print.setNomeFile("Distinta n. "
					+ distinta.getPg_distinta_def() + ".pdf");
		print.setUtcr(context.getUserContext().getUser());
		print.addParam("cd_cds", distinta.getCd_cds(), String.class);
		print.addParam("cd_unita_organizzativa", distinta.getCd_unita_organizzativa(), String.class);
		print.addParam("esercizio", distinta.getEsercizio().toString(), String.class);
		print.addParam("pg_distinta", distinta.getPg_distinta().toString(), String.class);
		
		Report report = SpringUtil.getBean("printService",
				PrintService.class).executeReport(context.getUserContext(),
				print);
		cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);
		CMISPath cmisPath = distinta.getCMISPath(cmisService);
		
		Document node = cmisService.storePrintDocument(distinta, report, cmisPath);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(ComponentException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch (IOException e) {
		throw handleException(e);
	}
}
public void invia(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
	String webScriptURL = documentiContabiliService.getRepositoyURL().concat("service/sigla/firma/doccont");
	Map<String, String> subjectDN = documentiContabiliService.getCertSubjectDN(firmaOTPBulk.getUserName(), firmaOTPBulk.getPassword());
	if (subjectDN == null)
		throw new ApplicationException("Errore nella lettura dei certificati!\nVerificare Nome Utente e Password!");
	String codiceFiscale = subjectDN.get("SERIALNUMBER").substring(3);
	UtenteBulk utente = ((CNRUserInfo)context.getUserInfo()).getUtente();
	if (controlloCodiceFiscale != null && controlloCodiceFiscale.equalsIgnoreCase("Y") && !utente.getCodiceFiscaleLDAP().equalsIgnoreCase(codiceFiscale)) {
		throw new ApplicationException("Il codice fiscale \"" + codiceFiscale + "\" presente sul certicato di Firma, " +
				"è diverso da quello dell'utente collegato \"" + utente.getCodiceFiscaleLDAP() +"\"!");
	}
	Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)getModel();
	inviaDistinta(context,distinta);
	distinta = (Distinta_cassiereBulk)getModel();
	if(!this.isFlusso()){
		List<String> nodes = new ArrayList<String>();
		
		nodes.addAll(documentiContabiliService.getNodeRefDocumento(distinta.getEsercizio(),distinta.getCd_cds(),distinta.getPg_distinta_def(), "DISTINTA",true));
		
		List dettagliRev=((DistintaCassiereComponentSession)createComponentSession()).dettagliDistinta(context.getUserContext(), distinta, it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV);
		
		for(Iterator i=dettagliRev.iterator();i.hasNext();){
	    	V_mandato_reversaleBulk bulk=(V_mandato_reversaleBulk) i.next();
	    	if (documentiContabiliService.getNodeRefDocumento(bulk, true)!=null)
	    		nodes.addAll(documentiContabiliService.getNodeRefDocumento(bulk, true));
		}
		List dettagliMan=((DistintaCassiereComponentSession)createComponentSession()).dettagliDistinta(context.getUserContext(), distinta, it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN);
		
		for(Iterator i=dettagliMan.iterator();i.hasNext();){
	    	V_mandato_reversaleBulk bulk=(V_mandato_reversaleBulk) i.next();
	    	if (documentiContabiliService.getNodeRefDocumento(bulk, true)!=null)
	    		nodes.addAll(documentiContabiliService.getNodeRefDocumento(bulk, true));
		}
	
		PdfSignApparence pdfSignApparence = new PdfSignApparence();
		pdfSignApparence.setNodes(nodes);
		pdfSignApparence.setUsername(firmaOTPBulk.getUserName());
		pdfSignApparence.setPassword(firmaOTPBulk.getPassword());
		pdfSignApparence.setOtp(firmaOTPBulk.getOtp());
		
		Apparence apparence = new Apparence(
				null, 
				"Rome", "Firma ",
				"per invio all'Istituto cassiere\nFirmato dal " +getTitolo()+"\n"+
						subjectDN.get("GIVENNAME") + " " + subjectDN.get("SURNAME"), 
				300, 40, 1, 550, 80);
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
			} 
		
			documentiContabiliService.inviaDistintaPEC(nodes,this.isSepa());	
			setMessage("Invio effettuato correttamente.");
		
		} catch (HttpException e) {
			throw new BusinessProcessException(e);
		} catch (IOException e) {
			throw new BusinessProcessException(e);
		} catch (Exception e) {
			throw new BusinessProcessException(e);
		} 
	}else{
		generaXML(context);
		File file = new File(System.getProperty("tmp.dir.SIGLAWeb")+getFile());
		CMISFile cmisFile = new CMISFile(file, file.getName());			
			if (cmisFile!=null) {
				//E' previsto solo l'inserimento ma non l'aggiornamento
				CMISPath path = distinta.getCMISPath(cmisService);
				try{
					Document node = cmisService.storeSimpleDocument(
							cmisFile,
							cmisFile.getInputStream(),
							cmisFile.getContentType(),
							cmisFile.getFileName(), 
							path);					
					cmisFile.setDocument(node);					
				} catch (Exception e) {
					if (e.getCause() instanceof CmisConstraintException)
						throw new ApplicationException("CMIS - File ["+cmisFile.getFileName()+"] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
					throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + e.getMessage() + ")");
				}
				if (cmisFile.getDocument().getContentStreamLength() > 0){
    				String nomeFile = file.getName();
    				String nomeFileP7m = nomeFile+".p7m";
    				String webScriptURLp7 = documentiContabiliService.getRepositoyURL().concat("service/sigla/firma/p7m");
    	    		String json = "{" +
    	    				"\"nodeRefSource\" : \"" + cmisFile.getDocument().getProperty(SiglaCMISService.ALFCMIS_NODEREF).getValueAsString() + "\"," +
    	    				"\"username\" : \"" + firmaOTPBulk.getUserName() + "\"," +
    	    				"\"password\" : \"" + firmaOTPBulk.getPassword() + "\"," +
    	    				"\"otp\" : \"" + firmaOTPBulk.getOtp() + "\""
    	    				+ "}";
    	    				
    	    			UrlBuilder url = new UrlBuilder(URIUtil.encodePath(webScriptURLp7));
    					
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
    	    				JSONTokener tokenizer = new JSONTokener(new InputStreamReader(response.getStream()));
    	    			    JSONObject jsonObject = new JSONObject(tokenizer);
    	    			    Document nodeSigned = (Document) documentiContabiliService.getNodeByNodeRef(jsonObject.getString("nodeRef"));
    	    			    InputStream streamSigned = documentiContabiliService.getResource(nodeSigned);	    					
    	    				try {
    	    					File fileSigned = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", nomeFileP7m);
    	    					OutputStream outputStream = new FileOutputStream(fileSigned);
    	    					IOUtils.copy(streamSigned, outputStream);
    	    					outputStream.close();		    				
		    	    		} catch (HttpException e) {
		    	    			throw new BusinessProcessException(e);
		    	    		} catch (IOException e) {
		    	    			throw new BusinessProcessException(e);
		    	    		} catch (Exception e) {
		    	    			throw new BusinessProcessException(e);
		    	    		}
    	    			}
    	    		}
    	    		else {					
    	    			throw new ApplicationException("Errore durante il processo di firma elettronica. Ripetere l'operazione di firma!");
    	    		}
			}
	}
}	
private String getTitolo() {
	if (firmatarioDistinta.getTitoloFirma().equalsIgnoreCase(UtenteFirmaDettaglioBulk.TITOLO_FIRMA_DELEGATO))
		return "";
	return UtenteFirmaDettaglioBulk.titoloKeys.get(firmatarioDistinta.getTitoloFirma()) + "\n";
}
public String getDocumento(){
	Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)getModel();
	if (distinta != null){
		return "Distinta ".
				concat(String.valueOf(distinta.getEsercizio())).
				concat("-").concat(distinta.getCd_unita_organizzativa()==null?"":distinta.getCd_unita_organizzativa()).
				concat("-").concat(String.valueOf(distinta.getPg_distinta_def()));
	}
	return null;
}

public void scaricaDistinta(ActionContext actioncontext) throws Exception {
	Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)getModel();
	if(this.getParametriCnr()!= null && this.getParametriCnr().getFl_tesoreria_unica()){
			CmisObject id = documentiContabiliService.getNodeByPath(distinta.getCMISPath(cmisService).getPath().concat("/").concat(String.valueOf(distinta.getEsercizio())).concat("-").concat(distinta.getCd_unita_organizzativa()).
				concat("-").concat(String.valueOf(distinta.getPg_distinta_def())).
				concat("-I.xslt"));
		InputStream is =null;
		if(id!=null){
			 is = documentiContabiliService.getResource(id);
			if (is != null){
				((HttpActionContext)actioncontext).getResponse().setContentType("text/html");
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
}

public void scaricaDistintaFirmata(ActionContext actioncontext) throws Exception {
	Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)getModel();
	if(this.getParametriCnr()!= null && this.getParametriCnr().getFl_tesoreria_unica()){
		CmisObject id=null;
		if(isFlusso()){
			id = documentiContabiliService.getNodeByPath(distinta.getCMISPath(cmisService).getPath().concat("/").concat(String.valueOf(distinta.getEsercizio())).concat("-").concat(distinta.getCd_unita_organizzativa()).
				concat("-").concat(String.valueOf(distinta.getPg_distinta_def())).
				concat("-I.xslt.p7m"));
		}else 
			id = documentiContabiliService.getNodeByPath(distinta.getCMISPath(cmisService).getPath().concat("/").concat("Distinta n. ").concat(String.valueOf(distinta.getPg_distinta_def())).
					concat(".pdf"));
		InputStream is =null; 
		if(id!=null){
			 is = documentiContabiliService.getResource(id);
			if (is != null){
				if(isFlusso()){
					((HttpActionContext)actioncontext).getResponse().setCharacterEncoding("UTF-8");
					((HttpActionContext)actioncontext).getResponse().setContentType("text/html");
				}
				else
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
	 }
}
public Boolean isSepa() {
	return sepa;
}
public void setSepa(Boolean sepa) {
	this.sepa = sepa;
}
public String getDownloadUrl() throws BusinessProcessException
{
	try {
	Distinta_cassiereBulk distinta =(Distinta_cassiereBulk)this.getModel();
	String path=null;
	CmisObject id=null;
	if(distinta !=null && this.getParametriCnr()!= null && this.getParametriCnr().getFl_tesoreria_unica() && distinta.getPg_distinta_def()!=null && distinta.getFl_flusso()){
		File fileSigned = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/"+String.valueOf(distinta.getEsercizio())+"-"+distinta.getCd_unita_organizzativa()+
				"-"+String.valueOf(distinta.getPg_distinta_def())+"-I.xslt.p7m");
		
		id = documentiContabiliService.getNodeByPath(distinta.getCMISPath(cmisService).getPath().concat("/").concat(String.valueOf(distinta.getEsercizio())).concat("-").concat(distinta.getCd_unita_organizzativa()).
				concat("-").concat(String.valueOf(distinta.getPg_distinta_def())).
				concat("-I.xslt.p7m"));
		 InputStream streamSigned = documentiContabiliService.getResource(id);	    					
		 OutputStream outputStream = new FileOutputStream(fileSigned);
		 IOUtils.copy(streamSigned, outputStream);
		 outputStream.close();	
	
		 return fileSigned.getAbsolutePath();
	}	
	else
		return null;
	 } catch (ApplicationException e) {
		 throw handleException(e);
	 } catch(java.rmi.RemoteException e) {
			throw handleException(e);
	 } catch(ComponentException e) {
			throw handleException(e);
	} catch(javax.ejb.EJBException e) {
			throw handleException(e);
	} catch (IOException e) {
			throw handleException(e);
	}		
	
	}	 

}


