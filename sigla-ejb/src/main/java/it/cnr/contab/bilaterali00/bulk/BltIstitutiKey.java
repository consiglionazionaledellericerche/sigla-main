/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/07/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class BltIstitutiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCentroResponsabilita;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_ISTITUTI
	 **/
	public BltIstitutiKey() {
		super();
	}
	public BltIstitutiKey(java.lang.String cdCentroResponsabilita) {
		super();
		this.cdCentroResponsabilita=cdCentroResponsabilita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof BltIstitutiKey)) return false;
		BltIstitutiKey k = (BltIstitutiKey) o;
		if (!compareKey(getCdCentroResponsabilita(), k.getCdCentroResponsabilita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCentroResponsabilita());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCentroResponsabilita]
	 **/
	public void setCdCentroResponsabilita(java.lang.String cdCentroResponsabilita)  {
		this.cdCentroResponsabilita=cdCentroResponsabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCentroResponsabilita]
	 **/
	public java.lang.String getCdCentroResponsabilita() {
		return cdCentroResponsabilita;
	}
}