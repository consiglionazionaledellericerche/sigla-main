/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/05/2010
 */
package it.cnr.contab.config00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ServizioPecKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdServizio;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SERVIZIO_PEC
	 **/
	public ServizioPecKey() {
		super();
	}
	public ServizioPecKey(java.lang.String cdServizio) {
		super();
		this.cdServizio=cdServizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ServizioPecKey)) return false;
		ServizioPecKey k = (ServizioPecKey) o;
		if (!compareKey(getCdServizio(), k.getCdServizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdServizio());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdServizio]
	 **/
	public void setCdServizio(java.lang.String cdServizio)  {
		this.cdServizio=cdServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdServizio]
	 **/
	public java.lang.String getCdServizio() {
		return cdServizio;
	}
}