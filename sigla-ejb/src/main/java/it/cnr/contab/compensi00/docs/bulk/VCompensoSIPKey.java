/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 12/06/2008
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VCompensoSIPKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// pg_compenso DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_compenso;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	public VCompensoSIPKey() {
		super();
	}
	public VCompensoSIPKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso) {
		super();
		this.cd_cds = cd_cds;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.esercizio = esercizio;
		this.pg_compenso = pg_compenso;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof VCompensoSIPKey)) return false;
		VCompensoSIPKey k = (VCompensoSIPKey)o;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getPg_compenso(),k.getPg_compenso())) return false;
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
		return
			calculateKeyHashCode(getCd_cds())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getPg_compenso());
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
	 * Setter dell'attributo pg_compenso
	 */
	public void setPg_compenso(java.lang.Long pg_compenso) {
		this.pg_compenso = pg_compenso;
	}
}