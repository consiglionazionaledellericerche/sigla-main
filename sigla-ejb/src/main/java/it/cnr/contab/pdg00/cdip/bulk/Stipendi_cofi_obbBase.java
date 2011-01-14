/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 02/10/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.persistency.Keyed;
public class Stipendi_cofi_obbBase extends Stipendi_cofi_obbKey implements Keyed {
	public Stipendi_cofi_obbBase() {
		super();
	}
	public Stipendi_cofi_obbBase(java.lang.Integer esercizio, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione) {
		super(esercizio, cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione);
	}
}