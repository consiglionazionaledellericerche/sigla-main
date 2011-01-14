/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_procedura_annoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_procedura;
	private java.lang.Integer esercizio_limite;
	public Incarichi_procedura_annoKey() {
		super();
	}
	public Incarichi_procedura_annoKey(java.lang.Integer esercizio, java.lang.Long pg_procedura, java.lang.Integer esercizio_limite) {
		super();
		this.esercizio=esercizio;
		this.pg_procedura=pg_procedura;
		this.esercizio_limite=esercizio_limite;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_procedura_annoKey)) return false;
		Incarichi_procedura_annoKey k = (Incarichi_procedura_annoKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_procedura(), k.getPg_procedura())) return false;
		if (!compareKey(getEsercizio_limite(), k.getEsercizio_limite())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_procedura());
		i = i + calculateKeyHashCode(getEsercizio_limite());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_procedura(java.lang.Long pg_procedura)  {
		this.pg_procedura=pg_procedura;
	}
	public java.lang.Long getPg_procedura() {
		return pg_procedura;
	}
	public void setEsercizio_limite(java.lang.Integer esercizio_limite)  {
		this.esercizio_limite=esercizio_limite;
	}
	public java.lang.Integer getEsercizio_limite() {
		return esercizio_limite;
	}
}