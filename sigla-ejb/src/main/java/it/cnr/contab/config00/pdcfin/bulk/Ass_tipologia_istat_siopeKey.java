/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/06/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_tipologia_istat_siopeKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer pg_tipologia;
	private java.lang.Integer esercizio_siope;
	private java.lang.String ti_gestione_siope;
	private java.lang.String cd_siope;
	public Ass_tipologia_istat_siopeKey() {
		super();
	}
	public Ass_tipologia_istat_siopeKey(java.lang.Integer pg_tipologia, java.lang.Integer esercizio_siope, java.lang.String ti_gestione_siope, java.lang.String cd_siope) {
		super();
		this.pg_tipologia=pg_tipologia;
		this.esercizio_siope=esercizio_siope;
		this.ti_gestione_siope=ti_gestione_siope;
		this.cd_siope=cd_siope;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_tipologia_istat_siopeKey)) return false;
		Ass_tipologia_istat_siopeKey k = (Ass_tipologia_istat_siopeKey) o;
		if (!compareKey(getPg_tipologia(), k.getPg_tipologia())) return false;
		if (!compareKey(getEsercizio_siope(), k.getEsercizio_siope())) return false;
		if (!compareKey(getTi_gestione_siope(), k.getTi_gestione_siope())) return false;
		if (!compareKey(getCd_siope(), k.getCd_siope())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_tipologia());
		i = i + calculateKeyHashCode(getEsercizio_siope());
		i = i + calculateKeyHashCode(getTi_gestione_siope());
		i = i + calculateKeyHashCode(getCd_siope());
		return i;
	}
	public void setPg_tipologia(java.lang.Integer pg_tipologia)  {
		this.pg_tipologia=pg_tipologia;
	}
	public java.lang.Integer getPg_tipologia() {
		return pg_tipologia;
	}
	public void setEsercizio_siope(java.lang.Integer esercizio_siope)  {
		this.esercizio_siope=esercizio_siope;
	}
	public java.lang.Integer getEsercizio_siope() {
		return esercizio_siope;
	}
	public void setTi_gestione_siope(java.lang.String ti_gestione_siope)  {
		this.ti_gestione_siope=ti_gestione_siope;
	}
	public java.lang.String getTi_gestione_siope() {
		return ti_gestione_siope;
	}
	public void setCd_siope(java.lang.String cd_siope)  {
		this.cd_siope=cd_siope;
	}
	public java.lang.String getCd_siope() {
		return cd_siope;
	}
}