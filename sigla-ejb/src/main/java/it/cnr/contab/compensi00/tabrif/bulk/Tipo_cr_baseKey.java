/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_cr_baseKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_contributo_ritenuta;
	public Tipo_cr_baseKey() {
		super();
	}
	public Tipo_cr_baseKey(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta) {
		super();
		this.esercizio=esercizio;
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_cr_baseKey)) return false;
		Tipo_cr_baseKey k = (Tipo_cr_baseKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_contributo_ritenuta(), k.getCd_contributo_ritenuta())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_contributo_ritenuta());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta)  {
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
}