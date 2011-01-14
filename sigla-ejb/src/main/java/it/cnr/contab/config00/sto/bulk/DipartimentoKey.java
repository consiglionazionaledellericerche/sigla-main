/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class DipartimentoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_dipartimento;
	public DipartimentoKey() {
		super();
	}
	public DipartimentoKey(java.lang.String cd_dipartimento) {
		super();
		this.cd_dipartimento=cd_dipartimento;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof DipartimentoKey)) return false;
		DipartimentoKey k = (DipartimentoKey) o;
		if (!compareKey(getCd_dipartimento(), k.getCd_dipartimento())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_dipartimento());
		return i;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getCd_dipartimento () {
		return cd_dipartimento;
	}
}