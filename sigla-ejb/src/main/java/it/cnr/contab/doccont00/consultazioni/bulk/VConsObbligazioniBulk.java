/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class VConsObbligazioniBulk extends VConsObbligazioniBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONS_OBBLIGAZIONI
	 **/
	public VConsObbligazioniBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONS_OBBLIGAZIONI
	 **/
	public VConsObbligazioniBulk(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Integer esercizioOriginale, java.lang.Long pgObbligazione) {
		super(cdCds, esercizio, esercizioOriginale, pgObbligazione);
	}
}