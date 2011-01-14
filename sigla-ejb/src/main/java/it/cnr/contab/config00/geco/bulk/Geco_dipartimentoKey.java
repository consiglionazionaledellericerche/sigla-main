/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Geco_dipartimentoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cod_dip;
	public Geco_dipartimentoKey() {
		super();
	}
	public Geco_dipartimentoKey(java.lang.String cod_dip) {
		super();
		this.cod_dip=cod_dip;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Geco_dipartimentoKey)) return false;
		Geco_dipartimentoKey k = (Geco_dipartimentoKey) o;
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