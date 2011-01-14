/*
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_pdg_variazione_cdrKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_variazione_pdg;
	private java.lang.String cd_centro_responsabilita;
	public Ass_pdg_variazione_cdrKey() {
		super();
	}
	public Ass_pdg_variazione_cdrKey(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.String cd_centro_responsabilita) {
		super();
		this.esercizio=esercizio;
		this.pg_variazione_pdg=pg_variazione_pdg;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_pdg_variazione_cdrKey)) return false;
		Ass_pdg_variazione_cdrKey k = (Ass_pdg_variazione_cdrKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_variazione_pdg(), k.getPg_variazione_pdg())) return false;
		if (!compareKey(getCd_centro_responsabilita(), k.getCd_centro_responsabilita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_variazione_pdg());
		i = i + calculateKeyHashCode(getCd_centro_responsabilita());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
	public java.lang.Long getPg_variazione_pdg () {
		return pg_variazione_pdg;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
}