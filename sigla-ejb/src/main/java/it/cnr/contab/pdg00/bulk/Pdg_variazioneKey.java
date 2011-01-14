/*
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_variazioneKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_variazione_pdg;
	public Pdg_variazioneKey() {
		super();
	}
	public Pdg_variazioneKey(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg) {
		super();
		this.esercizio=esercizio;
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_variazioneKey)) return false;
		Pdg_variazioneKey k = (Pdg_variazioneKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_variazione_pdg(), k.getPg_variazione_pdg())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_variazione_pdg());
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
}