/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/12/2015
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class DistintaCassiere1210Key extends OggettoBulk implements KeyedPersistent {
	@StorageProperty(name="doccont:esercizioDoc")
	private java.lang.Integer esercizio;
	@StorageProperty(name="doccont:numDoc")
	private java.lang.Long pgDistinta;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DISTINTA_CASSIERE_1210
	 **/
	public DistintaCassiere1210Key() {
		super();
	}
	public DistintaCassiere1210Key(java.lang.Integer esercizio, java.lang.Long pgDistinta) {
		super();
		this.esercizio=esercizio;
		this.pgDistinta=pgDistinta;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof DistintaCassiere1210Key)) return false;
		DistintaCassiere1210Key k = (DistintaCassiere1210Key) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgDistinta(), k.getPgDistinta())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgDistinta());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio di riferimento]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio di riferimento]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Progressivo della distinta]
	 **/
	public void setPgDistinta(java.lang.Long pgDistinta)  {
		this.pgDistinta=pgDistinta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Progressivo della distinta]
	 **/
	public java.lang.Long getPgDistinta() {
		return pgDistinta;
	}
}