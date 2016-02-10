/*
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Blt_progettiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_accordo;
	private java.lang.String cd_progetto;

	public Blt_progettiKey() {
		super();
	}

	public Blt_progettiKey(java.lang.String cd_accordo, java.lang.String cd_progetto) {
		super();
		this.cd_accordo=cd_accordo;
		this.cd_progetto=cd_progetto;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Blt_progettiKey)) return false;
		Blt_progettiKey k = (Blt_progettiKey) o;
		if (!compareKey(getCd_accordo(), k.getCd_accordo())) return false;
		if (!compareKey(getCd_progetto(), k.getCd_progetto())) return false;
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
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto) {
		this.cd_progetto = cd_progetto;
	}
}