package it.cnr.contab.fondecon00.bp;

import it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (6/4/2002 10:31:43 AM)
 * @author: Roberto Peli
 */
public class AssociazioniMandatiCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
/**
 * AssociazioniMandatiCRUDController constructor comment.
 * @param name java.lang.String
 * @param modelClass java.lang.Class
 * @param listPropertyName java.lang.String
 * @param parent it.cnr.jada.util.action.FormController
 */
public AssociazioniMandatiCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
/**
 * AssociazioniMandatiCRUDController constructor comment.
 * @param name java.lang.String
 * @param modelClass java.lang.Class
 * @param listPropertyName java.lang.String
 * @param parent it.cnr.jada.util.action.FormController
 * @param multiSelection boolean
 */
public AssociazioniMandatiCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
	super(name, modelClass, listPropertyName, parent, multiSelection);
}
public boolean isGrowable() {
	
	Fondo_economaleBulk fondo = (Fondo_economaleBulk)getParentModel();
	return	super.isGrowable() &&
			(fondo.getCrudStatus() == OggettoBulk.NORMAL ||
			 fondo.getCrudStatus() == OggettoBulk.TO_BE_UPDATED) &&
			!fondo.isChiuso() &&
			!fondo.isOnlyForClose();
}
public boolean isShrinkable() {
	return super.isShrinkable() && isGrowable();
}
}
