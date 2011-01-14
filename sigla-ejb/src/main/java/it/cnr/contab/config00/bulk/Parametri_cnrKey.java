package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * Creation date: (09/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cnrKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	public Parametri_cnrKey() {
		super();
	}
	public Parametri_cnrKey(java.lang.Integer esercizio) {
		super();
		this.esercizio = esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Parametri_cnrKey))
			return false;
		Parametri_cnrKey k = (Parametri_cnrKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio()))
			return false;
		return true;
	}
	/**
	 * Getter dell'attributo esercizio
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getEsercizio());
	}
}
