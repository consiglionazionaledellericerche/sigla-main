/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Parametri_livelli_epKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	public Parametri_livelli_epKey() {
		super();
	}
	public Parametri_livelli_epKey(java.lang.Integer esercizio) {
		super();
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Parametri_livelli_epKey)) return false;
		Parametri_livelli_epKey k = (Parametri_livelli_epKey) o;
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