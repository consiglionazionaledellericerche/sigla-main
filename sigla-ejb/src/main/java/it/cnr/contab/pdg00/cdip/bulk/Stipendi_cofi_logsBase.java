/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.persistency.Keyed;
public class Stipendi_cofi_logsBase extends Stipendi_cofi_logsKey implements Keyed {
	public Stipendi_cofi_logsBase() {
		super();
	}
	public Stipendi_cofi_logsBase(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.Long pg_esecuzione) {
		super(esercizio, mese, pg_esecuzione);
	}
}