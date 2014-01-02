package it.cnr.contab.doccont00.action;

import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
public class CRUDDistintaCassiereAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDDistintaCassiereAction() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "distintaCassDet"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doAddToCRUDMain_DistintaCassDet(ActionContext context)
{
	try 
	{
		CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP)getBusinessProcess(context);
		RicercaMandatoReversaleBP ricercaBP = (RicercaMandatoReversaleBP) context.createBusinessProcess( "RicercaMandatoReversaleBP",  new Object[]{ "MTh" });
		if (bp.isUoDistintaTuttaSac(context))
			ricercaBP.setSearchResultColumnSet("elencoConUo");
		else
			ricercaBP.setSearchResultColumnSet("default");
		
        context.addHookForward("bringback", this, "doBringBackDettaglioDistinta");
		return context.addBusinessProcess(ricercaBP);		
	
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackDettaglioDistinta(ActionContext context)
{
	try
	{
		CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP)context.getBusinessProcess();	
		bp.getDistintaCassDet().reset(context);
		bp.setDirty(true);
		bp.calcolaTotali( context );
		return context.findDefaultForward();
	}
	catch (Exception e )
	{
		return handleException( context, e );
	}	
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione di tutti gli elementi dal controller "distintaCassDet"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveAllFromCRUDMain_DistintaCassDet(ActionContext context)
{
	try 
	{
		CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess( context );
		bp.getDistintaCassDet().removeAll(context);
		bp.calcolaTotali( context );
		return context.findDefaultForward();		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "distintaCassDet"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doRemoveFromCRUDMain_DistintaCassDet(ActionContext context)
{
	try 
	{
		CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess( context );
		bp.controllaEliminaMandati(context);
		bp.getDistintaCassDet().remove(context);
		bp.calcolaTotali( context );
		return context.findDefaultForward();		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doVisualizzaDettagliTotali(ActionContext context)
{
	try 
	{
		fillModel( context );
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getBusinessProcess( context ).getModel();
		ViewDettaglioTotaliBP view = (ViewDettaglioTotaliBP) context.createBusinessProcess( "ViewDettaglioTotaliBP");
		view.setModel( context, distinta );
		return context.addBusinessProcess(view);
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doVisualizzaDettagliTotaliTrasmessi(ActionContext context)
{
	try 
	{
		fillModel( context );
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getBusinessProcess( context ).getModel();
		ViewDettaglioTotaliBP view = (ViewDettaglioTotaliBP) context.createBusinessProcess( "ViewDettaglioTotaliTrasmessiBP");
		view.setModel( context, distinta );
		return context.addBusinessProcess(view);
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}
public Forward doOnCheckIbanFailed( ActionContext context, int option) 
{
	if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) 
	{
		try 
		{
			CRUDBP bp = getBusinessProcess(context);
			((Distinta_cassiereBulk)bp.getModel()).setCheckIbanEseguito( true );
			doSalva(context);
}
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	return context.findDefaultForward();
}
public Forward doEstrai(ActionContext context) {
	try {
		CRUDBP bp = getBusinessProcess(context);
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfermaEstrai");
		return doConfermaEstrai(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doConfermaEstrai(ActionContext context,int option) {
	try {
		if (option == OptionBP.YES_BUTTON) {
			CRUDDistintaCassiereBP bp = (CRUDDistintaCassiereBP) getBusinessProcess( context );
			bp.generaXML(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
