/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Geco_dipartimento_sacKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cod_dip;
	public Geco_dipartimento_sacKey() {
		super();
	}
	public Geco_dipartimento_sacKey(java.lang.String cod_dip) {
		super();
		this.cod_dip=cod_dip;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Geco_dipartimento_sacKey)) return false;
		Geco_dipartimento_sacKey k = (Geco_dipartimento_sacKey) o;
		if (!compareKey(getCod_dip(), k.getCod_dip())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCod_dip());
		return i;
	}
	public void setCod_dip(java.lang.String cod_dip)  {
		this.cod_dip=cod_dip;
	}
	public java.lang.String getCod_dip() {
		return cod_dip;
	}
}