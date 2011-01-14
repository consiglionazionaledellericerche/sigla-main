/*
* Created by Generator 1.0
* Date 20/02/2006
*/
package it.cnr.contab.config00.esercizio.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Esercizio_baseKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	public Esercizio_baseKey() {
		super();
	}
	public Esercizio_baseKey(java.lang.Integer esercizio) {
		super();
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Esercizio_baseKey)) return false;
		Esercizio_baseKey k = (Esercizio_baseKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
}