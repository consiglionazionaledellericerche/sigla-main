/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/09/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tematica_attivitaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tematica_attivita;
	public Tematica_attivitaKey() {
		super();
	}
	public Tematica_attivitaKey(java.lang.String cd_tematica_attivita) {
		super();
		this.cd_tematica_attivita=cd_tematica_attivita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tematica_attivitaKey)) return false;
		Tematica_attivitaKey k = (Tematica_attivitaKey) o;
		if (!compareKey(getCd_tematica_attivita(), k.getCd_tematica_attivita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tematica_attivita());
		return i;
	}
	public void setCd_tematica_attivita(java.lang.String cd_tematica_attivita)  {
		this.cd_tematica_attivita=cd_tematica_attivita;
	}
	public java.lang.String getCd_tematica_attivita() {
		return cd_tematica_attivita;
	}
}