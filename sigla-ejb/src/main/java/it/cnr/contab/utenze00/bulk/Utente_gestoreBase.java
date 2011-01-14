/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/05/2009
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Utente_gestoreBase extends Utente_gestoreKey implements Keyed {
	public Utente_gestoreBase() {
		super();
	}
	public Utente_gestoreBase(java.lang.String cd_utente, java.lang.String cd_gestore) {
		super(cd_utente, cd_gestore);
	}
}