package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * Creation date: (12/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cdsKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private Integer esercizio;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private String cd_cds;

	public Parametri_cdsKey() {
		super();
	}
	public Parametri_cdsKey(String cd_cds,Integer esercizio) {
		super();
		this.cd_cds = cd_cds;
		this.esercizio = esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Parametri_cdsKey))
			return false;
		Parametri_cdsKey k = (Parametri_cdsKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds()))
			return false;
		if (!compareKey(getEsercizio(), k.getEsercizio()))
			return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getCd_cds())
			+ calculateKeyHashCode(getEsercizio());
	}

	/**
	 * Getter dell'attributo esercizio
	 */
	public Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	/**
	 * Getter dell'attributo cds
	 */
	public String getCd_cds() {
		return cd_cds;
	}

	/**
	 * Setter dell'attributo cds
	 */
	public void setCd_cds(String string) {
		cd_cds = string;
	}

}
