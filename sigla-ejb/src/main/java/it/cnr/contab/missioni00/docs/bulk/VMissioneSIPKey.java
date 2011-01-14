/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 12/06/2008
 */
package it.cnr.contab.missioni00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VMissioneSIPKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// PG_MISSIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_missione;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	public VMissioneSIPKey() {
		super();
	}
	public VMissioneSIPKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_missione) {
		super();
		this.cd_cds = cd_cds;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.esercizio = esercizio;
		this.pg_missione = pg_missione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof VMissioneSIPKey)) return false;
		VMissioneSIPKey k = (VMissioneSIPKey)o;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getPg_missione(),k.getPg_missione())) return false;
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
	 * Getter dell'attributo pg_missione
	 */
	public java.lang.Long getPg_missione() {
		return pg_missione;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_cds())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getPg_missione());
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
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	/* 
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	/* 
	 * Setter dell'attributo pg_missione
	 */
	public void setPg_missione(java.lang.Long pg_missione) {
		this.pg_missione = pg_missione;
	}
}