/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class CupKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCup;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CUP
	 **/
	public CupKey() {
		super();
	}
	public CupKey(java.lang.String cdCup) {
		super();
		this.cdCup=cdCup;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof CupKey)) return false;
		CupKey k = (CupKey) o;
		if (!compareKey(getCdCup(), k.getCdCup())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCup());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.cdCup=cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		return cdCup;
	}
}