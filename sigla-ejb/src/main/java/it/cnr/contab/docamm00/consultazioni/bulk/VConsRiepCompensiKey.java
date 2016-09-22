/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VConsRiepCompensiKey extends OggettoBulk implements KeyedPersistent {
	// PG_COMPENSO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_compenso;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	public VConsRiepCompensiKey() {
		super();
	}

	public VConsRiepCompensiKey(java.lang.String cd_cds,
			java.lang.String cd_unita_organizzativa,
			java.lang.Integer esercizio, java.lang.Long pg_compenso) {
		super();
		this.cd_cds = cd_cds;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.esercizio = esercizio;
		this.pg_compenso = pg_compenso;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VConsRiepCompensiKey))
			return false;
		VConsRiepCompensiKey k = (VConsRiepCompensiKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds()))
			return false;
		if (!compareKey(getCd_unita_organizzativa(), k
				.getCd_unita_organizzativa()))
			return false;
		if (!compareKey(getEsercizio(), k.getEsercizio()))
			return false;
		if (!compareKey(getPg_compenso(), k.getPg_compenso()))
			return false;
		return true;
	}

	/*
	 * Getter dell'attributo cd_cds
	 */
	public java.lang.String getCd_cds() {
		return cd_cds;
	}

	/*
	 * Getter dell'attributo cd_unita_organizzativa
	 */
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	/*
	 * Getter dell'attributo esercizio
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	/*
	 * Getter dell'attributo pg_compenso
	 */
	public java.lang.Long getPg_compenso() {
		return pg_compenso;
	}

	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getCd_cds())
				+ calculateKeyHashCode(getCd_unita_organizzativa())
				+ calculateKeyHashCode(getEsercizio())
				+ calculateKeyHashCode(getPg_compenso());
	}

	/*
	 * Setter dell'attributo cd_cds
	 */
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}

	/*
	 * Setter dell'attributo cd_unita_organizzativa
	 */
	public void setCd_unita_organizzativa(
			java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}

	/*
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	/*
	 * Setter dell'attributo pg_compenso
	 */
	public void setPg_compenso(java.lang.Long pg_compenso) {
		this.pg_compenso = pg_compenso;
	}
}