package it.cnr.contab.doccont00.action;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione dell'
 * Accertamento Partita di Giro.
 */
public class CRUDAccertamentoPGiroAction extends CRUDAbstractAccertamentoAction {
public CRUDAccertamentoPGiroAction() {
	super();
}
public Forward doBlankSearchFind_debitore(ActionContext context,AccertamentoBulk accertamento) 
{
	try 
	{
		accertamento.setDebitore(new TerzoBulk());
		accertamento.getDebitore().setAnagrafico( new AnagraficoBulk());
		
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la validazione di nuovo terzo creato
 	 * @param context <code>ActionContext</code> in uso.
	 * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code> 
	 * @param terzo Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCrea_debitore(ActionContext context, AccertamentoBulk accertamento, TerzoBulk terzo) 
{
	try 
	{
		if (terzo != null )
		{
			accertamento.validateTerzo( terzo);
			accertamento.setDebitore( terzo );
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
		AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk)bp.getModel();
		accert_pgiro.setCapitolo( new it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk() );
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
