/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/12/2012
 */
package it.cnr.contab.config00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class CigKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCig;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CIG
	 **/
	public CigKey() {
		super();
	}
	public CigKey(java.lang.String cdCig) {
		super();
		this.cdCig=cdCig;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof CigKey)) return false;
		CigKey k = (CigKey) o;
		if (!compareKey(getCdCig(), k.getCdCig())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCig());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCig]
	 **/
	public void setCdCig(java.lang.String cdCig)  {
		this.cdCig=cdCig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCig]
	 **/
	public java.lang.String getCdCig() {
		return cdCig;
	}
}