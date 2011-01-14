package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;

import java.util.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

public class ListaSospesiCNRAction extends ConsultazioniAction {
public ListaSospesiCNRAction() {
	super();
}
/**
 * Metodo utilizzato per gestire la modifica di un sospeso di entratata CNR
 */
public Forward doEditaSospeso(ActionContext context) 
{
	
	try 
	{
		ListaSospesiCNRBP listaBp = (ListaSospesiCNRBP) context.getBusinessProcess();
		SospesoBulk sospeso = (SospesoBulk) listaBp.getFocusedElement(context);;
			
		CRUDSospesoCNRBP bp = (CRUDSospesoCNRBP)context.createBusinessProcess("CRUDSospesoCNRBP");
		bp.setEditable( true );
		bp.edit( context, sospeso );
//		context.addHookForward("close",this,"doRefreshLista");				
		return context.addBusinessProcess(bp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
/*
	try 
	{
		fillModel(context);
		ListaSospesiEntrataCNRBP bp = (ListaSospesiEntrataCNRBP)context.getBusinessProcess();
		bp.cambiaStatoAssociatoACds( context );
		bp.setMessage("Lo stato dei sospesi e' stato aggiornato");
		return context.findDefaultForward();
	}
	catch ( Exception e )
	{
		return handleException( context, e )	;
	}	
	*/	
}
public Forward doSelection(ActionContext context,String name) {
	try 
	{
//		fillModel(context);
		AbstractSelezionatoreBP bp = (AbstractSelezionatoreBP)context.getBusinessProcess();
		bp.setFocus(context);
		bp.setSelection(context);
		return context.findDefaultForward();
	}
	catch(Exception e) 
	{
		return handleException(context,e);
	}
}


}
