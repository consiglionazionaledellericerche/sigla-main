/*
* Created by Generator 1.0
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_contrattazione_speseKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_centro_responsabilita;
	private java.lang.Integer pg_progetto;
	private java.lang.Integer  pg_dettaglio;
	public Pdg_contrattazione_speseKey() {
		super();
	}
	public Pdg_contrattazione_speseKey(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer pg_dettaglio) {
		super();
		this.esercizio=esercizio;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
		this.pg_progetto=pg_progetto;
		this.pg_dettaglio=pg_dettaglio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_contrattazione_speseKey)) return false;
		Pdg_contrattazione_speseKey k = (Pdg_contrattazione_speseKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_centro_responsabilita(), k.getCd_centro_responsabilita())) return false;
		if (!compareKey(getPg_progetto(), k.getPg_progetto())) return false;
		if (!compareKey(getPg_dettaglio(), k.getPg_dettaglio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_centro_responsabilita());
		i = i + calculateKeyHashCode(getPg_progetto());
		i = i + calculateKeyHashCode(getPg_dettaglio());
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
	public void setPg_dettaglio(java.lang.Integer pg_dettaglio)  {
		this.pg_dettaglio=pg_dettaglio;
	}
	public java.lang.Integer getPg_dettaglio () {
		return pg_dettaglio;
	}
}