/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Blt_programma_visiteKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdAccordo;
	private java.lang.String cdProgetto;
	private java.lang.Long pgRecord;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_programma_visiteKey() {
		super();
	}
	public Blt_programma_visiteKey(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Long pgRecord) {
		super();
		this.cdAccordo=cdAccordo;
		this.cdProgetto=cdProgetto;
		this.pgRecord=pgRecord;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Blt_programma_visiteKey)) return false;
		Blt_programma_visiteKey k = (Blt_programma_visiteKey) o;
		if (!compareKey(getCdAccordo(), k.getCdAccordo())) return false;
		if (!compareKey(getCdProgetto(), k.getCdProgetto())) return false;
		if (!compareKey(getPgRecord(), k.getPgRecord())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdAccordo());
		i = i + calculateKeyHashCode(getCdProgetto());
		i = i + calculateKeyHashCode(getPgRecord());
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
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.cdProgetto=cdProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		return cdProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgRecord]
	 **/
	public void setPgRecord(java.lang.Long pgRecord)  {
		this.pgRecord=pgRecord;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgRecord]
	 **/
	public java.lang.Long getPgRecord() {
		return pgRecord;
	}
}