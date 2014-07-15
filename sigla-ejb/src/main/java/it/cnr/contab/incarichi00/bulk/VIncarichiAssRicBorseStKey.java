/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VIncarichiAssRicBorseStKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pgRepertorio;
	public VIncarichiAssRicBorseStKey() {
		super();
	}
	public VIncarichiAssRicBorseStKey(java.lang.Integer esercizio, java.lang.Long pgRepertorio) {
		super();
		this.esercizio=esercizio;
		this.pgRepertorio=pgRepertorio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VIncarichiAssRicBorseStKey)) return false;
		VIncarichiAssRicBorseStKey k = (VIncarichiAssRicBorseStKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgRepertorio(), k.getPgRepertorio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgRepertorio());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPgRepertorio(java.lang.Long pgRepertorio)  {
		this.pgRepertorio=pgRepertorio;
	}
	public java.lang.Long getPgRepertorio() {
		return pgRepertorio;
	}
}