/*
* Created by Generator 1.0
* Date 21/04/2006
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_variazione_riga_gestKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_variazione_pdg;
	private java.lang.Integer pg_riga;
	public Pdg_variazione_riga_gestKey() {
		super();
	}
	public Pdg_variazione_riga_gestKey(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Integer pg_riga) {
		super();
		this.esercizio=esercizio;
		this.pg_variazione_pdg=pg_variazione_pdg;
		this.pg_riga=pg_riga;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_variazione_riga_gestKey)) return false;
		Pdg_variazione_riga_gestKey k = (Pdg_variazione_riga_gestKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_variazione_pdg(), k.getPg_variazione_pdg())) return false;
		if (!compareKey(getPg_riga(), k.getPg_riga())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_variazione_pdg());
		i = i + calculateKeyHashCode(getPg_riga());
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
	public void setPg_riga(java.lang.Integer pg_riga)  {
		this.pg_riga=pg_riga;
	}
	public java.lang.Integer getPg_riga () {
		return pg_riga;
	}
}