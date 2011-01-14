/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_approvato_dip_areaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_dipartimento;
	private java.lang.Integer pg_dettaglio;
	public Pdg_approvato_dip_areaKey() {
		super();
	}
	public Pdg_approvato_dip_areaKey(java.lang.Integer esercizio, java.lang.String cd_dipartimento, java.lang.Integer pg_dettaglio) {
		super();
		this.esercizio=esercizio;
		this.cd_dipartimento=cd_dipartimento;
		this.pg_dettaglio=pg_dettaglio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_approvato_dip_areaKey)) return false;
		Pdg_approvato_dip_areaKey k = (Pdg_approvato_dip_areaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_dipartimento(), k.getCd_dipartimento())) return false;
		if (!compareKey(getPg_dettaglio(), k.getPg_dettaglio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_dipartimento());
		i = i + calculateKeyHashCode(getPg_dettaglio());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setPg_dettaglio(java.lang.Integer pg_dettaglio)  {
		this.pg_dettaglio=pg_dettaglio;
	}
	public java.lang.Integer getPg_dettaglio() {
		return pg_dettaglio;
	}
}