/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 28/05/2008
 */
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Var_stanz_resKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_variazione;
	public Var_stanz_resKey() {
		super();
	}
	public Var_stanz_resKey(java.lang.Integer esercizio, java.lang.Long pg_variazione) {
		super();
		this.esercizio=esercizio;
		this.pg_variazione=pg_variazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Var_stanz_resKey)) return false;
		Var_stanz_resKey k = (Var_stanz_resKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_variazione(), k.getPg_variazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_variazione());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_variazione(java.lang.Long pg_variazione)  {
		this.pg_variazione=pg_variazione;
	}
	public java.lang.Long getPg_variazione() {
		return pg_variazione;
	}
}