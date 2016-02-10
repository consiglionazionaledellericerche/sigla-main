/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Geco_dipartimentiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cod_dip;
	private java.lang.Long esercizio;
	public Geco_dipartimentiKey() {
		super();
	}
	public Geco_dipartimentiKey(java.lang.String cod_dip, java.lang.Long esercizio) {
		super();
		this.cod_dip=cod_dip;
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Geco_dipartimentiKey)) return false;
		Geco_dipartimentiKey k = (Geco_dipartimentiKey) o;
		if (!compareKey(getCod_dip(), k.getCod_dip())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCod_dip());
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setCod_dip(java.lang.String cod_dip)  {
		this.cod_dip=cod_dip;
	}
	public java.lang.String getCod_dip() {
		return cod_dip;
	}
	public java.lang.Long getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Long esercizio) {
		this.esercizio = esercizio;
	}
}