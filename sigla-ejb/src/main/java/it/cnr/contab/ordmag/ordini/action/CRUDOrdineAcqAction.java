package it.cnr.contab.ordmag.ordini.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.persistence.PersistenceException;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.CRUDBP;

public class CRUDOrdineAcqAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDOrdineAcqAction() {
        super();
    }
///**
// * Gestisce una richiesta di ricerca del searchtool "sospeso"
// *
// * @param context	L'ActionContext della richiesta
// * @param doc	L'OggettoBulk padre del searchtool
// * @param sospesoTrovato	L'OggettoBulk selezionato dall'utente
// * @return Il Forward alla pagina di risposta
// * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
// */
public Forward doBringBackSearchFindUnitaMisura(ActionContext context,
	OrdineAcqRigaBulk riga,
	UnitaMisuraBulk unitaMisura) 
	throws java.rmi.RemoteException {

	riga.setUnitaMisura(unitaMisura);
	((CRUDBP)context.getBusinessProcess()).setDirty(true);
		
	try{
		if (unitaMisura!=null && riga.getBeneServizio() != null && riga.getBeneServizio().getUnitaMisura() != null && unitaMisura.getCdUnitaMisura().equals(riga.getBeneServizio().getUnitaMisura().getCdUnitaMisura())) {
			riga.setCoefConv(BigDecimal.ONE);
		} else {
			riga.setCoefConv(null);
		}
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doBringBackSearchFindBeneServizio(ActionContext context,
		OrdineAcqRigaBulk riga,
		Bene_servizioBulk bene) 
		throws java.rmi.RemoteException {

		riga.setBeneServizio(bene);
		((CRUDBP)context.getBusinessProcess()).setDirty(true);
		if (bene != null){
			riga.setDsBeneServizio(bene.getDs_bene_servizio());
			if (bene.getUnitaMisura() != null){
				riga.setUnitaMisura(bene.getUnitaMisura());
				riga.setCoefConv(BigDecimal.ONE);
			}
			if (bene.getTipoGestione() != null){
				riga.setDspTipoConsegna(bene.getTipoGestione());
			}
		}
//		try{
//			if (riga.getUnitaMisura()!=null && riga.getUnitaMisura().getCdUnitaMisura()!=null && riga.getBeneServizio() != null && riga.getBeneServizio().getUnitaMisura() != null && riga.getUnitaMisura().getCdUnitaMisura().equals(riga.getBeneServizio().getUnitaMisura().getCdUnitaMisura())) {
//				riga.setCoefConv(BigDecimal.ONE);
//			} else {
//				riga.setCoefConv(null);
//			}
//			return context.findDefaultForward();
//
//		} catch(Exception e) {
//			return handleException(context,e);
//		}
		return context.findDefaultForward();
}
public Forward doBringBackSearchFindUnitaOperativaOrd(ActionContext context,
		OrdineAcqBulk ordine,
		UnitaOperativaOrdBulk uop) 
		throws java.rmi.RemoteException {

		ordine.setUnitaOperativaOrd(uop);
		((CRUDBP)context.getBusinessProcess()).setDirty(true);
		if (uop != null){
			CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)context.getBusinessProcess();

			try {
				OrdineAcqComponentSession h = (OrdineAcqComponentSession)bp.createComponentSession();
				h.completaOrdine(context.getUserContext(), ordine);
				try {
					bp.setModel(context,ordine);
				} catch (BusinessProcessException e) {
				}
			} catch (BusinessProcessException e) {
				return handleException(context,e);
			} catch (java.rmi.RemoteException e) {
				return handleException(context,e);
			} catch (PersistenceException e) {
				return handleException(context,e);
			} catch (PersistencyException e) {
				return handleException(context,e);
			} catch (ComponentException e) {
				return handleException(context,e);
			}
		}
//		try{
//			if (riga.getUnitaMisura()!=null && riga.getUnitaMisura().getCdUnitaMisura()!=null && riga.getBeneServizio() != null && riga.getBeneServizio().getUnitaMisura() != null && riga.getUnitaMisura().getCdUnitaMisura().equals(riga.getBeneServizio().getUnitaMisura().getCdUnitaMisura())) {
//				riga.setCoefConv(BigDecimal.ONE);
//			} else {
//				riga.setCoefConv(null);
//			}
//			return context.findDefaultForward();
//
//		} catch(Exception e) {
//			return handleException(context,e);
//		}
		return context.findDefaultForward();
}
public Forward doTab(ActionContext context,String tabName,String pageName) 
{
	try
	{
		fillModel( context );
		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)getBusinessProcess(context);
		OrdineAcqBulk ordine = (OrdineAcqBulk)bp.getModel();

		if(ordine == null)
			return super.doTab( context, tabName, pageName );

		if( bp.isEditable() && !bp.isSearching() && bp.getTab( tabName ).equalsIgnoreCase("tabRichiestaUop"))
		{
		}	
        if ("tabOrdineAllegati".equalsIgnoreCase(bp.getTab(tabName))) {
			fillModel( context );
		}
		return super.doTab( context, tabName, pageName );		
	}
	catch(Throwable e) 
	{
		return handleException(context,e);
	}	
}
@Override
public Forward doSalva(ActionContext actioncontext) throws RemoteException {
	try
	{
		fillModel(actioncontext);
		gestioneSalvataggio(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}
private void gestioneSalvataggio(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
	getBusinessProcess(actioncontext).save(actioncontext);
	postSalvataggio(actioncontext);
}

public Forward doSalvaDefinitivo(ActionContext actioncontext) throws RemoteException {
	try
	{
		CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		OrdineAcqBulk ordine = (OrdineAcqBulk) bp.getModel();
		if (ordine.isDefinitivo()){
			ordine.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
		}
		if (ordine.isInserito()){
			try {
				OrdineAcqComponentSession h = (OrdineAcqComponentSession)bp.createComponentSession();
				if (h.isUtenteAbilitatoValidazioneOrdine(actioncontext.getUserContext(), ordine)){
//					ordine.setStato(OrdineAcqBulk.STATO_INVIATA_ORDINE);
				} else {
				 	ordine.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
				}
				try {
					bp.setModel(actioncontext,ordine);
				} catch (BusinessProcessException e) {
				}
			} catch (java.rmi.RemoteException e) {
				return handleException(actioncontext,e);
			} catch (PersistenceException e) {
				return handleException(actioncontext,e);
			}

		}
		gestioneSalvataggio(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}
protected void postSalvataggio(ActionContext context) throws BusinessProcessException {
    CRUDOrdineAcqBP bp= (CRUDOrdineAcqBP) getBusinessProcess(context);
	bp.gestionePostSalvataggio(context);
}
}
