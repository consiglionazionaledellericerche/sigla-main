package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Accertamento Residuo.
 */
public class CRUDAccertamentoPGiroResiduoBP extends CRUDAccertamentoPGiroBP {
/**
 * CRUDAccertamentoResiduoBP constructor comment.
 */
public CRUDAccertamentoPGiroResiduoBP() {
	super();
}
/**
 * CRUDAccertamentoResiduoBP constructor comment.
 * @param function java.lang.String
 */
public CRUDAccertamentoPGiroResiduoBP(String function) {
	super(function);
}
/*
 *	Disabilito il bottone di cancellazione documento
 */

public boolean isDeleteButtonEnabled() 
{
	return false;	
}
/*
 *	Disabilito il bottone di creazione documento
 */

public boolean isNewButtonEnabled() 
{
	return false;	
}
/**
 * Inzializza il ricevente nello stato di SEARCH.
 * @param context Il contesto dell'azione
 */
public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		rollbackUserTransaction();
		setModel( context, createEmptyModelForSearch(context) );
		setStatus(SEARCH);
		setDirty(false);
		resetChildren( context );
		resetTabs(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
}
