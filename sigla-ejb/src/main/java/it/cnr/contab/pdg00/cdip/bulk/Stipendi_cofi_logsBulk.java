/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Stipendi_cofi_logsBulk extends Stipendi_cofi_logsBase {
	public Stipendi_cofi_logsBulk() {
		super();
	}
	public Stipendi_cofi_logsBulk(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.Long pg_esecuzione) {
		super(esercizio, mese, pg_esecuzione);
	}
}