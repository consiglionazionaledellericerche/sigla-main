/*
* Created by Generator 1.0
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Var_stanz_res_rigaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_variazione;
	private java.lang.Long pg_riga;
	public Var_stanz_res_rigaKey() {
		super();
	}
	public Var_stanz_res_rigaKey(java.lang.Integer esercizio, java.lang.Long pg_variazione, java.lang.Long pg_riga) {
		super();
		this.esercizio=esercizio;
		this.pg_variazione=pg_variazione;
		this.pg_riga=pg_riga;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Var_stanz_res_rigaKey)) return false;
		Var_stanz_res_rigaKey k = (Var_stanz_res_rigaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_variazione(), k.getPg_variazione())) return false;
		if (!compareKey(getPg_riga(), k.getPg_riga())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_variazione());
		i = i + calculateKeyHashCode(getPg_riga());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setPg_variazione(java.lang.Long pg_variazione)  {
		this.pg_variazione=pg_variazione;
	}
	public java.lang.Long getPg_variazione () {
		return pg_variazione;
	}
	public void setPg_riga(java.lang.Long pg_riga)  {
		this.pg_riga=pg_riga;
	}
	public java.lang.Long getPg_riga () {
		return pg_riga;
	}
}