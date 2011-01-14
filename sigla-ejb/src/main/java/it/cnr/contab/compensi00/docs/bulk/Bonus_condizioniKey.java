/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Bonus_condizioniKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_condizione;
	public Bonus_condizioniKey() {
		super();
	}
	public Bonus_condizioniKey(java.lang.Integer esercizio, java.lang.String cd_condizione) {
		super();
		this.esercizio=esercizio;
		this.cd_condizione=cd_condizione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Bonus_condizioniKey)) return false;
		Bonus_condizioniKey k = (Bonus_condizioniKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_condizione(), k.getCd_condizione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_condizione());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_condizione(java.lang.String cd_condizione)  {
		this.cd_condizione=cd_condizione;
	}
	public java.lang.String getCd_condizione() {
		return cd_condizione;
	}
}