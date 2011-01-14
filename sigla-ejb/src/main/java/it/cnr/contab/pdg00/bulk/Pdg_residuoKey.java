/*
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_residuoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_centro_responsabilita;
	public Pdg_residuoKey() {
		super();
	}
	public Pdg_residuoKey(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita) {
		super();
		this.esercizio=esercizio;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_residuoKey)) return false;
		Pdg_residuoKey k = (Pdg_residuoKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_centro_responsabilita(), k.getCd_centro_responsabilita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_centro_responsabilita());
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
}