package it.cnr.contab.doccont00.bp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
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
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
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
	private String file;
	private Unita_organizzativaBulk uoSrivania;
	
public CRUDDistintaCassiereBP() {
	super("Tn");

}
public CRUDDistintaCassiereBP(String function) {
	super(function+"Tn");

}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	Button[] toolbar = super.createToolbar();
	
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
	context.getBusinessProcess("/GestioneUtenteBP").removeChild("CRUDDistintaCassiereBP");
	this.setFlusso(new Boolean(config.getInitParameter("flusso")));
	try {
		isUoDistintaTuttaSac(context);
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

	printbp.setReportName("/doccont/doccont/distinta_cassiere.jasper");
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
	if(this.isEditable())
		((Distinta_cassiereBulk)this.getModel()).setFl_flusso(flusso);
}

public OggettoBulk createNewSearchBulk(ActionContext context) throws BusinessProcessException {
	Distinta_cassiereBulk fs = (Distinta_cassiereBulk)super.createNewSearchBulk(context);
	this.setFile(null);
	if(this.isEditable())
		fs.setFl_flusso(isFlusso());
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
}
