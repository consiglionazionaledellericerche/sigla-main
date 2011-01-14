/*
* Created by Generator 1.0
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_modulo_speseKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_centro_responsabilita;
	private java.lang.Integer pg_progetto;
	private java.lang.Integer id_classificazione;
	private java.lang.String cd_cds_area;
	public Pdg_modulo_speseKey() {
		super();
	}
	public Pdg_modulo_speseKey(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer id_classificazione, java.lang.String cd_cds_area) {
		super();
		this.esercizio=esercizio;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
		this.pg_progetto=pg_progetto;
		this.id_classificazione=id_classificazione;
		this.cd_cds_area=cd_cds_area;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_modulo_speseKey)) return false;
		Pdg_modulo_speseKey k = (Pdg_modulo_speseKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_centro_responsabilita(), k.getCd_centro_responsabilita())) return false;
		if (!compareKey(getPg_progetto(), k.getPg_progetto())) return false;
		if (!compareKey(getId_classificazione(), k.getId_classificazione())) return false;
		if (!compareKey(getCd_cds_area(), k.getCd_cds_area())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_centro_responsabilita());
		i = i + calculateKeyHashCode(getPg_progetto());
		i = i + calculateKeyHashCode(getId_classificazione());
		i = i + calculateKeyHashCode(getCd_cds_area());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setPg_progetto(java.lang.Integer pg_progetto)  {
		this.pg_progetto=pg_progetto;
	}
	public java.lang.Integer getPg_progetto () {
		return pg_progetto;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	public java.lang.Integer getId_classificazione () {
		return id_classificazione;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
	}
	public java.lang.String getCd_cds_area () {
		return cd_cds_area;
	}
}