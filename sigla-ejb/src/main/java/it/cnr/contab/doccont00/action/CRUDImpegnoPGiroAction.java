package it.cnr.contab.doccont00.action;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione dell'
 * Impegno Partita di Giro.
 */
public class CRUDImpegnoPGiroAction extends CRUDAbstractObbligazioneAction {
public CRUDImpegnoPGiroAction() {
	super();
}
public Forward doBlankSearchFind_creditore(ActionContext context,ObbligazioneBulk obbligazione) 
{
	try 
	{
		obbligazione.setCreditore(new TerzoBulk());
		obbligazione.getCreditore().setAnagrafico( new AnagraficoBulk());
		
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la validazione di nuovo terzo creato
 	 * @param context <code>ActionContext</code> in uso.
	 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code> 
	 * @param terzo Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCrea_creditore(ActionContext context, ObbligazioneBulk obbligazione, TerzoBulk terzo) 
{
	try 
	{
		if (terzo != null )
		{
			obbligazione.validateTerzo( terzo);
			obbligazione.setCreditore( terzo );
		}	
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la selezione di un'unità organizzativa
 *
 */
public Forward doCambiaUnitaOrganizzativa(ActionContext context) 
{
	try 
	{ 	
		fillModel( context );
		SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk)bp.getModel();
		imp_pgiro.setElemento_voce( new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk() );
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDBP bp = getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
