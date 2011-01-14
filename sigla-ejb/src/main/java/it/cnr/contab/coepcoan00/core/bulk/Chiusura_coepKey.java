/*
* Created by Generator 1.0
* Date 17/05/2005
*/
package it.cnr.contab.coepcoan00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Chiusura_coepKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	public Chiusura_coepKey() {
		super();
	}
	public Chiusura_coepKey(java.lang.String cd_cds, java.lang.Integer esercizio) {
		super();
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Chiusura_coepKey)) return false;
		Chiusura_coepKey k = (Chiusura_coepKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
}