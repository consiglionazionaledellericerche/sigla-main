package it.cnr.contab.doccont00.action;

import java.util.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

public class ListaDistinteEmesseAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
public ListaDistinteEmesseAction() {
	super();
}
/**
 * Metodo utilizzato per gestire l'invio di una distinta.
 */
public Forward doInviaDistinta(ActionContext context) {

	try {

		ListaDistinteEmesseBP bp = (ListaDistinteEmesseBP)context.getBusinessProcess();
		bp.setSelection( context );		
		Collection distinte_emesse = (Collection)bp.getSelectedElements(context);
		bp.inviaDistinta( context, distinte_emesse );
		bp.setMessage("Invio effettuato");
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
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
