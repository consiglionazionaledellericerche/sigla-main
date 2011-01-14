/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_repertorio_annoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_repertorio;
	private java.lang.Integer esercizio_limite;
	public Incarichi_repertorio_annoKey() {
		super();
	}
	public Incarichi_repertorio_annoKey(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Integer esercizio_limite) {
		super();
		this.esercizio=esercizio;
		this.pg_repertorio=pg_repertorio;
		this.esercizio_limite=esercizio_limite;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_repertorio_annoKey)) return false;
		Incarichi_repertorio_annoKey k = (Incarichi_repertorio_annoKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_repertorio(), k.getPg_repertorio())) return false;
		if (!compareKey(getEsercizio_limite(), k.getEsercizio_limite())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_repertorio());
		i = i + calculateKeyHashCode(getEsercizio_limite());
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
	public void setEsercizio_limite(java.lang.Integer esercizio_limite)  {
		this.esercizio_limite=esercizio_limite;
	}
	public java.lang.Integer getEsercizio_limite() {
		return esercizio_limite;
	}
}