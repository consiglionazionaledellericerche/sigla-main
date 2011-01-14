/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Accertamento_modificaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_modifica;
	public Accertamento_modificaKey() {
		super();
	}
	public Accertamento_modificaKey(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica) {
		super();
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
		this.pg_modifica=pg_modifica;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Accertamento_modificaKey)) return false;
		Accertamento_modificaKey k = (Accertamento_modificaKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_modifica(), k.getPg_modifica())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_modifica());
		return i;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_modifica(java.lang.Long pg_modifica)  {
		this.pg_modifica=pg_modifica;
	}
	public java.lang.Long getPg_modifica() {
		return pg_modifica;
	}
}