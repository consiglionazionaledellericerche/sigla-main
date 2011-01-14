package it.cnr.contab.doccont00.bp;


import java.rmi.RemoteException;
import java.util.*;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
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
	private Unita_organizzativaBulk uoSrivania;

public CRUDDistintaCassiereBP() {
	super("Tn");

}
public CRUDDistintaCassiereBP(String function) {
	super(function+"Tn");

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
}
}
