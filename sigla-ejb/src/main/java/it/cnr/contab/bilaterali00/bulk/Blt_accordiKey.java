/*
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Blt_accordiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_accordo;

	public Blt_accordiKey() {
		super();
	}

	public Blt_accordiKey(java.lang.String cd_accordo) {
		super();
		this.cd_accordo=cd_accordo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Blt_accordiKey)) return false;
		Blt_accordiKey k = (Blt_accordiKey) o;
		if (!compareKey(getCd_accordo(), k.getCd_accordo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_accordo());
		return i;
	}
	public java.lang.String getCd_accordo() {
		return cd_accordo;
	}
	public void setCd_accordo(java.lang.String cd_accordo) {
		this.cd_accordo = cd_accordo;
	}
}