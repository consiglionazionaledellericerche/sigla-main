/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_attivitaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_attivita;
	public Tipo_attivitaKey() {
		super();
	}
	public Tipo_attivitaKey(java.lang.String cd_tipo_attivita) {
		super();
		this.cd_tipo_attivita=cd_tipo_attivita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_attivitaKey)) return false;
		Tipo_attivitaKey k = (Tipo_attivitaKey) o;
		if (!compareKey(getCd_tipo_attivita(), k.getCd_tipo_attivita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_attivita());
		return i;
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita)  {
		this.cd_tipo_attivita=cd_tipo_attivita;
	}
	public java.lang.String getCd_tipo_attivita() {
		return cd_tipo_attivita;
	}
}