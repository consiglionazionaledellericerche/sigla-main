/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_repertorioKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_repertorio;
	public Incarichi_repertorioKey() {
		super();
	}
	public Incarichi_repertorioKey(java.lang.Integer esercizio, java.lang.Long pg_repertorio) {
		super();
		this.esercizio=esercizio;
		this.pg_repertorio=pg_repertorio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_repertorioKey)) return false;
		Incarichi_repertorioKey k = (Incarichi_repertorioKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_repertorio(), k.getPg_repertorio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_repertorio());
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
}