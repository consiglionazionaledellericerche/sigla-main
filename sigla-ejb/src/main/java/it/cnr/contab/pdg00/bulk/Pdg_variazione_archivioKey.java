/*
* Created by Generator 1.0
* Date 21/07/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Pdg_variazione_archivioKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_variazione_pdg;
	private java.lang.Long progressivo_riga;
	public Pdg_variazione_archivioKey() {
		super();
	}
	public Pdg_variazione_archivioKey(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Long progressivo_riga) {
		super();
		this.esercizio=esercizio;
		this.pg_variazione_pdg=pg_variazione_pdg;
		this.progressivo_riga=progressivo_riga;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_variazione_archivioKey)) return false;
		Pdg_variazione_archivioKey k = (Pdg_variazione_archivioKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_variazione_pdg(), k.getPg_variazione_pdg())) return false;
		if (!compareKey(getProgressivo_riga(), k.getProgressivo_riga())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_variazione_pdg());
		i = i + calculateKeyHashCode(getProgressivo_riga());
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
	public void setProgressivo_riga(java.lang.Long progressivo_riga)  {
		this.progressivo_riga=progressivo_riga;
	}
	public java.lang.Long getProgressivo_riga () {
		return progressivo_riga;
	}
}