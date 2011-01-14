package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * Creation date: (24/02/2005)
 * @author Tilde
 * @version 1.0
 */
public class FirmeKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
    // TIPO      VARCHAR(3)	  NOT NULL (PK)
	private java.lang.Integer esercizio;
	private String tipo;

	public FirmeKey() {
		super();
	}
	public FirmeKey(java.lang.Integer esercizio, String tipo) {
		super();
		this.esercizio = esercizio;
		this.tipo = tipo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FirmeKey))
			return false;
		FirmeKey k = (FirmeKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio()))
			return false;
		if (!compareKey(getTipo(), k.getTipo()))
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
	/**
	 * Getter dell'attributo tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * Setter dell'attributo tipo
	 */
	public void setTipo(String string) {
		tipo = string;
	}

	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getEsercizio())+
		       calculateKeyHashCode(getTipo());
	}

}
