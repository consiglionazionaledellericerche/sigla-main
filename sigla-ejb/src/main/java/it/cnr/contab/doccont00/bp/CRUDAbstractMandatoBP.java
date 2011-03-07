package it.cnr.contab.doccont00.bp;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;

import java.rmi.RemoteException;
import java.util.*;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Mandato
 */

public abstract class CRUDAbstractMandatoBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final CRUDSospesoController sospesiSelezionati = new CRUDSospesoController("SospesiSelezionati",Sospeso_det_uscBulk.class,"sospeso_det_uscColl",this);
	private final SimpleDetailCRUDController reversaliMan = new SimpleDetailCRUDController("Reversali",V_ass_doc_contabiliBulk.class,"doc_contabili_collColl",this);			

public CRUDAbstractMandatoBP() {}
public CRUDAbstractMandatoBP( String function ) 
{
	super(function);
}
/**
 * Aggiunge un nuovo sospeso alla lista dei Sospeso associati ad un Mandato
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */
public void aggiungiSospesi(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try
	{
		MandatoBulk mandato = (MandatoBulk)getModel();
		HookForward caller = (HookForward)context.getCaller();
		List sospesi = (List) caller.getParameter("selectedElements");

		if ( sospesi == null )
			return;
			
		mandato.validaSospesi( sospesi );
		
		SospesoBulk sospeso;
		Sospeso_det_uscBulk sdu;
		for ( Iterator i = sospesi.iterator() ;i.hasNext() ;) 
		{
			sospeso = (SospesoBulk) i.next();
			sdu = mandato.addToSospeso_det_uscColl( sospeso );
			sdu.setToBeCreated();
			sdu.setUser(context.getUserInfo().getUserid());
		}
		setModel( context, mandato );
		resyncChildren( context );
	} catch(Exception e) {
		throw handleException(e);
	}		
}
/**
 *	Metodo per disabilitare tutti i campi, nel caso il mandato sia stato annullato ( come se fosse in stato di visualizzazione )
 */
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW){
		MandatoBulk mandato = (MandatoBulk)getModel();
		if ( mandato != null && !mandato.getCd_uo_origine().equals( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa( context ).getCd_unita_organizzativa()))
		{
			setStatus(VIEW);
			setMessage("Mandato creato dall'Unità Organizzativa " + mandato.getCd_uo_origine() + ". Non consentita la modifica.");
		}
		else if ( mandato != null && mandato.getStato().equals( mandato.STATO_MANDATO_ANNULLATO ) )
		{
			setStatus(VIEW);
			setMessage("Mandato annullato. Non consentita la modifica.");
		}
		else if ( mandato != null && !mandato.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO) &&
				  !mandato.isMandatoAccreditamento()) {
			setStatus(VIEW);
			setMessage("Mandato inserito in distinta. Non consentita la modifica.");
		}
	}
}
/**
 * Metodo utilizzato per gestire il caricamento dei sospesi.
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
	 *
	 * @exception <code>BusinessProcessException</code>
 */

public it.cnr.jada.util.RemoteIterator cercaSospesi(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		MandatoComponentSession session = (MandatoComponentSession) createComponentSession();
		return session.cercaSospesi( context.getUserContext(), null, (MandatoBulk) getModel() );
		
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}

	/*
	try 
	{
		MandatoComponentSession session = (MandatoComponentSession) createComponentSession();
		MandatoBulk mandato = session.listaSospesi( context.getUserContext(), (MandatoBulk) getModel() );
		setModel( context, mandato );
		resyncChildren( context );
	} catch(Exception e) {
		throw handleException(e);
	}
	*/
	
}
/**
 * Gestisce l'annullamento di un Mandato.
 * @param context contesto dell'azione
 */
public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	delete( context, null );
}
/**
 * Gestisce l'annullamento di un Mandato.
 * @param context contesto dell'azione
 * @param param il parametro che indica se il controllo sul compenso e' necessario
 */
public void delete(ActionContext context, CompensoOptionRequestParameter param) throws it.cnr.jada.action.BusinessProcessException {
	int crudStatus = getModel().getCrudStatus();
	try {
			validate(context);
			getModel().setToBeUpdated();
			setModel( context, ((MandatoComponentSession) createComponentSession()).annullaMandato(context.getUserContext(),(MandatoBulk)getModel(), param));
			setStatus(VIEW);			
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
}

/**
 * Metodo con cui si ottiene il valore della variabile <code>reversaliMan</code>
 * di tipo <code>SimpleDetailCRUDController</code>.
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getReversaliMan() {
	return reversaliMan;
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>sospesiSelezionati</code>
 * di tipo <code>CRUDSospesoController</code>.
 * @return it.cnr.contab.doccont00.bp.CRUDSospesoController
 */
public final CRUDSospesoController getSospesiSelezionati() {
	return sospesiSelezionati;
}
/* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
protected void initializePrintBP(AbstractPrintBP bp) 
{
	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
	printbp.setReportName("/doccont/doccont/vpg_man_rev_ass.jasper");
	MandatoBulk mandato = (MandatoBulk)getModel();
	Print_spooler_paramBulk param;

	param = new Print_spooler_paramBulk();
	param.setNomeParam("aCd_cds");
	param.setValoreParam(mandato.getCd_cds());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("aEs");
	param.setValoreParam(mandato.getEsercizio().toString());
	param.setParamType("java.lang.Integer");
	printbp.addToPrintSpoolerParam(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("aPg_da");
	param.setValoreParam(mandato.getPg_mandato().toString());
	param.setParamType("java.lang.Long");
	printbp.addToPrintSpoolerParam(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("aPg_a");
	param.setValoreParam(mandato.getPg_mandato().toString());
	param.setParamType("java.lang.Long");
	printbp.addToPrintSpoolerParam(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("aDt_da");
	param.setValoreParam(OfflineReportPrintBP.DATE_FORMAT.format(mandato.getDt_emissione()));
	param.setParamType("java.util.Date");
	printbp.addToPrintSpoolerParam(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("aDt_a");
	param.setValoreParam(OfflineReportPrintBP.DATE_FORMAT.format(mandato.getDt_emissione()));
	param.setParamType("java.util.Date");
	printbp.addToPrintSpoolerParam(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("aCd_terzo");
	param.setValoreParam("%");
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
}
/**
 *	Abilito il bottone di caricamento dei sospesi solo se il mandato non è stato
 *  pagato o annullato e se e' a regolamento sospeso.
 *
 *	return boolean	= FALSE se il mandato e' stato pagato o annullato oppure se e' di regolarizzazione 
 *					= TRUE se il mandato non e' stato pagato o annullato e se non e' di regolarizzazione 
 */
public boolean isCaricaSospesiButtonEnabled() {
	return
	
	MandatoBulk.TIPO_REGOLAM_SOSPESO.equals(((MandatoBulk)getModel()).getTi_mandato()) &&	
	/*!((MandatoBulk)getModel()).isPagato() && */
	!((MandatoBulk)getModel()).isAnnullato() ;
}
/**
 *	Abilito il bottone di cancellazione documento solo se non è stato pagato o
 *  annullato.
 */
public boolean isDeleteButtonEnabled() {
	return (super.isDeleteButtonEnabled() ||
			(!isEditing() && 
			 getModel()!=null && 
			 getModel() instanceof MandatoBulk && 
			 ((MandatoBulk)getModel()).getStato_trasmissione() !=null && 
			 !((MandatoBulk)getModel()).getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO))) &&
	//!((MandatoBulk)getModel()).isPagato() && 
	!((MandatoBulk)getModel()).isAnnullato() ;
}
public boolean isPrintButtonHidden() 
{
	return super.isPrintButtonHidden() || isInserting() || isSearching();
}
/**
 *	Abilito il bottone di rimozione dei sospesi solo se il mandato non è stato
 *  pagato o annullato.
 *
 *	isEditable 	= FALSE se il mandato e' stato pagato o annullato
 *				= TRUE se il mandato non e' stato pagato o annullato
 */
public boolean isRimuoviSospesiButtonEnabled() {
	return /*!((MandatoBulk)getModel()).isPagato() && */
	       !((MandatoBulk)getModel()).isAnnullato() ;
}
/**
 *	Abilito il bottone di salvataggio solo se il mandato non è stato annullato.
 *
 *	isEditable 	= FALSE se il mandato e' stato pagato o annullato
 *				= TRUE se il mandato non e' stato pagato o annullato
 */
public boolean isSaveButtonEnabled() {
	return super.isSaveButtonEnabled() && !((MandatoBulk)getModel()).isAnnullato() ;
}
public void esistonoPiuModalitaPagamento(ActionContext context)throws it.cnr.jada.action.BusinessProcessException {
	try {
		((MandatoComponentSession) createComponentSession()).esistonoPiuModalitaPagamento(context.getUserContext(),(MandatoBulk)getModel());
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	} catch (RemoteException e) {
		throw new BusinessProcessException(e);
	}
}
public boolean isDipendenteDaConguaglio(it.cnr.jada.action.ActionContext context,MandatoBulk mandato) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return ((MandatoComponentSession) createComponentSession()).isDipendenteDaConguaglio(context.getUserContext(),mandato);
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}

}
}
