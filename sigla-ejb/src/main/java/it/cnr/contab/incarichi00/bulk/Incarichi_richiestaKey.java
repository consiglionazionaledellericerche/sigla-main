/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_richiestaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_richiesta;
	public Incarichi_richiestaKey() {
		super();
	}
	public Incarichi_richiestaKey(java.lang.Integer esercizio, java.lang.Long pg_richiesta) {
		super();
		this.esercizio=esercizio;
		this.pg_richiesta=pg_richiesta;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_richiestaKey)) return false;
		Incarichi_richiestaKey k = (Incarichi_richiestaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_richiesta(), k.getPg_richiesta())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_richiesta());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_richiesta(java.lang.Long pg_richiesta)  {
		this.pg_richiesta=pg_richiesta;
	}
	public java.lang.Long getPg_richiesta() {
		return pg_richiesta;
	}
}