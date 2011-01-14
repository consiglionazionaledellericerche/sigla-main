/*
* Creted by Generator 1.0
* Date 24/02/2005
*/
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Commessa_spesaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_progetto;
	private java.lang.Integer esercizio;
	public Commessa_spesaKey() {
		super();
	}
	public Commessa_spesaKey(java.lang.Long pg_progetto, java.lang.Integer esercizio) {
		super();
		this.pg_progetto=pg_progetto;
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Commessa_spesaKey)) return false;
		Commessa_spesaKey k = (Commessa_spesaKey) o;
		if (!compareKey(getPg_progetto(), k.getPg_progetto())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_progetto());
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setPg_progetto(java.lang.Long pg_progetto)  {
		this.pg_progetto=pg_progetto;
	}
	public java.lang.Long getPg_progetto () {
		return pg_progetto;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
}