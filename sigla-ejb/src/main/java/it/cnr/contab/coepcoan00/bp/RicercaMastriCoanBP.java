package it.cnr.contab.coepcoan00.bp;

import it.cnr.jada.action.*;

public class RicercaMastriCoanBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * RicercaMastriCoanBP constructor comment.
 */
public RicercaMastriCoanBP() {
	super();
}
/**
 * RicercaMastriCoanBP constructor comment.
 * @param function java.lang.String
 */
public RicercaMastriCoanBP(String function) {
	super(function);
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	super.init( config, context );
	setStatus(SEARCH);
	try
	{
		resetForSearch( context );		
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
//	super.init(config,context);
}	
/**
 *	Disabilito il bottone di cancellazione.
 */

public boolean isDeleteButtonHidden() {
	return true;
}
/**
 *	Disabilito il bottone di "Nuovo".
 */

public boolean isNewButtonHidden() {
	return true;
}
/**
 *	Disabilito il bottone di "Salva".
 */

public boolean isSaveButtonHidden() {
	return true;
}
}
