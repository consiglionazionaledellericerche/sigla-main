package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_repertorio_rappKey extends Incarichi_archivioBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_repertorio;
	private java.lang.Long progressivo_riga;
	public Incarichi_repertorio_rappKey() {
		super();
	}
	public Incarichi_repertorio_rappKey(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super();
		this.esercizio=esercizio;
		this.pg_repertorio=pg_repertorio;
		this.progressivo_riga=progressivo_riga;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_repertorio_rappKey)) return false;
		Incarichi_repertorio_rappKey k = (Incarichi_repertorio_rappKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_repertorio(), k.getPg_repertorio())) return false;
		if (!compareKey(getProgressivo_riga(), k.getProgressivo_riga())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_repertorio());
		i = i + calculateKeyHashCode(getProgressivo_riga());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio)  {
		this.pg_repertorio=pg_repertorio;
	}
	public java.lang.Long getPg_repertorio() {
		return pg_repertorio;
	}
	public void setProgressivo_riga(java.lang.Long progressivo_riga)  {
		this.progressivo_riga=progressivo_riga;
	}
	public java.lang.Long getProgressivo_riga() {
		return progressivo_riga;
	}
}