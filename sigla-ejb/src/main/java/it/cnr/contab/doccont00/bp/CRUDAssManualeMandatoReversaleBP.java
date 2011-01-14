package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (20/11/2002 12.28.04)
 * @author: Roberto Fantino
 */
public class CRUDAssManualeMandatoReversaleBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final SimpleDetailCRUDController reversaliAssociateCRUDController = new SimpleDetailCRUDController("reversaliAssociateCRUDController", ReversaleIBulk.class, "reversaliAssociate", this);
	private final SimpleDetailCRUDController reversaliDisponibiliCRUDController = new SimpleDetailCRUDController("reversaliDisponibiliCRUDController", ReversaleIBulk.class, "reversaliDisponibili", this);
/**
 * CRUDMandatoReversaliBP constructor comment.
 */
public CRUDAssManualeMandatoReversaleBP() {
	super();
}
/**
 * CRUDMandatoReversaliBP constructor comment.
 * @param function java.lang.String
 */
public CRUDAssManualeMandatoReversaleBP(String function) {
	super(function);
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2002 17.34.50)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final SimpleDetailCRUDController getReversaliAssociateCRUDController() {
	return reversaliAssociateCRUDController;
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2002 17.34.50)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final SimpleDetailCRUDController getReversaliDisponibiliCRUDController() {
	return reversaliDisponibiliCRUDController;
}
/**
 *	Nascondo il bottone di cancellazione
 */
public boolean isDeleteButtonHidden() {
	return true;
}
/**
 *	Nascondo il bottone Nuovo
 */
public boolean isNewButtonHidden() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2002 12.30.16)
 * @return boolean
 */
public boolean isROMandato() {
	return !isSearching();
}
public void reset(ActionContext context) throws BusinessProcessException {
	super.resetForSearch(context);
}
}
