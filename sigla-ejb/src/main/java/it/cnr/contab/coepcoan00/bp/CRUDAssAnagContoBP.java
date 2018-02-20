package it.cnr.contab.coepcoan00.bp;

import it.cnr.jada.util.jsp.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Associazione Anagrafica Conto
 */
public class CRUDAssAnagContoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDAssAnagContoBP constructor comment.
 */
public CRUDAssAnagContoBP() {
	super();
}
/**
 * CRUDAssAnagContoBP constructor comment.
 * @param function java.lang.String
 */
public CRUDAssAnagContoBP(String function) {
	super(function);
}
/**
 *	Non abilito il bottone di cancellazione
 */

public boolean isDeleteButtonHidden() 
{
	return true;
}
}
