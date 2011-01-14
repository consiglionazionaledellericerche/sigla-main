/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Repertorio_limitiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_tipo_limite;
	public Repertorio_limitiKey() {
		super();
	}
	public Repertorio_limitiKey(java.lang.Integer esercizio, java.lang.String cd_tipo_limite) {
		super();
		this.esercizio=esercizio;
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Repertorio_limitiKey)) return false;
		Repertorio_limitiKey k = (Repertorio_limitiKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_tipo_limite(), k.getCd_tipo_limite())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_tipo_limite());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite)  {
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public java.lang.String getCd_tipo_limite() {
		return cd_tipo_limite;
	}
}