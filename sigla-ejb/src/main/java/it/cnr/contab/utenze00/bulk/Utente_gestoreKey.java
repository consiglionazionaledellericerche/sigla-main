/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/05/2009
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Utente_gestoreKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_utente;
	private java.lang.String cd_gestore;
	public Utente_gestoreKey() {
		super();
	}
	public Utente_gestoreKey(java.lang.String cd_utente, java.lang.String cd_gestore) {
		super();
		this.cd_utente=cd_utente;
		this.cd_gestore=cd_gestore;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Utente_gestoreKey)) return false;
		Utente_gestoreKey k = (Utente_gestoreKey) o;
		if (!compareKey(getCd_utente(), k.getCd_utente())) return false;
		if (!compareKey(getCd_gestore(), k.getCd_gestore())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_utente());
		i = i + calculateKeyHashCode(getCd_gestore());
		return i;
	}
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}
	public java.lang.String getCd_utente() {
		return cd_utente;
	}
	public void setCd_gestore(java.lang.String cd_gestore)  {
		this.cd_gestore=cd_gestore;
	}
	public java.lang.String getCd_gestore() {
		return cd_gestore;
	}
}