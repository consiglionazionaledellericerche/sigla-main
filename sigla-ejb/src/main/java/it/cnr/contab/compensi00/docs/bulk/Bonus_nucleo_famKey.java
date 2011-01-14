/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Bonus_nucleo_famKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_bonus;
	private java.lang.String cf_componente_nucleo;
	public Bonus_nucleo_famKey() {
		super();
	}
	public Bonus_nucleo_famKey(java.lang.Integer esercizio, java.lang.Long pg_bonus, java.lang.String cf_componente_nucleo) {
		super();
		this.esercizio=esercizio;
		this.pg_bonus=pg_bonus;
		this.cf_componente_nucleo=cf_componente_nucleo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Bonus_nucleo_famKey)) return false;
		Bonus_nucleo_famKey k = (Bonus_nucleo_famKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_bonus(), k.getPg_bonus())) return false;
		if (!compareKey(getCf_componente_nucleo(), k.getCf_componente_nucleo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_bonus());
		i = i + calculateKeyHashCode(getCf_componente_nucleo());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_bonus(java.lang.Long pg_bonus)  {
		this.pg_bonus=pg_bonus;
	}
	public java.lang.Long getPg_bonus() {
		return pg_bonus;
	}
	public void setCf_componente_nucleo(java.lang.String cf_componente_nucleo)  {
		this.cf_componente_nucleo=cf_componente_nucleo;
	}
	public java.lang.String getCf_componente_nucleo() {
		return cf_componente_nucleo;
	}
}