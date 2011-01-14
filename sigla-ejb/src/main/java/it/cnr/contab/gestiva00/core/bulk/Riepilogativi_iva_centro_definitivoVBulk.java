package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public class Riepilogativi_iva_centro_definitivoVBulk extends Riepilogativi_iva_centroVBulk {

/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Riepilogativi_iva_centro_definitivoVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Riepilogativi_iva_centroVBulk bulk = (Riepilogativi_iva_centroVBulk)super.initializeForSearch(bp, context);
	
	bulk.setTipo_report(DEFINITIVO);
	
	return bulk;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:35:44 AM)
 * @return java.lang.Integer
 */
public boolean isPageNumberRequired() {
	return true;
}
}
