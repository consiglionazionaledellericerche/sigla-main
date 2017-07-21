package it.cnr.contab.gestiva00.core.bulk;

import java.math.BigDecimal;

/**
 * Insert the type's description here.
 * Creation date: (2/24/2003 10:54:31 AM)
 * @author: Roberto Peli
 */
public class Liquidazione_massa_provvisoria_ivaVBulk extends Liquidazione_provvisoria_ivaVBulk {

    public static BigDecimal REPORTID = new BigDecimal(-1000);

/**
 * Liquidazione_massa_ivaVBulk constructor comment.
 */
public Liquidazione_massa_provvisoria_ivaVBulk() {
	super();
}
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Liquidazione_massa_provvisoria_ivaVBulk bulk = (Liquidazione_massa_provvisoria_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setTipo_stampa(TIPO_STAMPA_LIQUIDAZIONE_MASSA_PRV);
	bulk.setId_report(REPORTID);
	return bulk;
}
public boolean isDBManagementRequired() {
	return false;
}
public boolean isRistampabile() {
	return false;
}
}
