package it.cnr.contab.ordmag.richieste.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;

public class CRUDRichiestaUopAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDRichiestaUopAction() {
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
	RichiestaUopRigaBulk riga,
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
		RichiestaUopRigaBulk riga,
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
		CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)getBusinessProcess(context);
		RichiestaUopBulk richiesta = (RichiestaUopBulk)bp.getModel();

		if(richiesta == null)
			return super.doTab( context, tabName, pageName );

		if( bp.isEditable() && !bp.isSearching() && bp.getTab( tabName ).equalsIgnoreCase("tabRichiestaUop"))
		{
		}	
        if ("tabRichiestaAllegati".equalsIgnoreCase(bp.getTab(tabName))) {
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
		CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)getBusinessProcess(actioncontext);
		fillModel(actioncontext);
		RichiestaUopBulk richiesta = (RichiestaUopBulk) bp.getModel();
		if (richiesta.isDefinitiva()){
			richiesta.setStato(RichiestaUopBulk.STATO_INVIATA_ORDINE);
		}
		if (richiesta.isInserita()){
			richiesta.setStato(RichiestaUopBulk.STATO_DEFINITIVA);
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
    CRUDRichiestaUopBP bp= (CRUDRichiestaUopBP) getBusinessProcess(context);
	bp.gestionePostSalvataggio(context);
}
}
