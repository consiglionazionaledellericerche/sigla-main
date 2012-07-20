/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Blt_regole_diariaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdAccordo;
	private java.lang.Integer pgRegola;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_REGOLE_DIARIA
	 **/
	public Blt_regole_diariaKey() {
		super();
	}
	public Blt_regole_diariaKey(java.lang.String cdAccordo, java.lang.Integer pgRegola) {
		super();
		this.cdAccordo=cdAccordo;
		this.pgRegola=pgRegola;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Blt_regole_diariaKey)) return false;
		Blt_regole_diariaKey k = (Blt_regole_diariaKey) o;
		if (!compareKey(getCdAccordo(), k.getCdAccordo())) return false;
		if (!compareKey(getPgRegola(), k.getPgRegola())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdAccordo());
		i = i + calculateKeyHashCode(getPgRegola());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.cdAccordo=cdAccordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		return cdAccordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgRegola]
	 **/
	public void setPgRegola(java.lang.Integer pgRegola)  {
		this.pgRegola=pgRegola;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgRegola]
	 **/
	public java.lang.Integer getPgRegola() {
		return pgRegola;
	}
}