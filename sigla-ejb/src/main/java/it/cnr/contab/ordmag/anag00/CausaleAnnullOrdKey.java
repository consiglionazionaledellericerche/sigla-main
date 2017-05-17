/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class CausaleAnnullOrdKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCausaleAnnull;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CAUSALE_ANNULL_ORD
	 **/
	public CausaleAnnullOrdKey() {
		super();
	}
	public CausaleAnnullOrdKey(java.lang.String cdCausaleAnnull) {
		super();
		this.cdCausaleAnnull=cdCausaleAnnull;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof CausaleAnnullOrdKey)) return false;
		CausaleAnnullOrdKey k = (CausaleAnnullOrdKey) o;
		if (!compareKey(getCdCausaleAnnull(), k.getCdCausaleAnnull())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCausaleAnnull());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCausaleAnnull]
	 **/
	public void setCdCausaleAnnull(java.lang.String cdCausaleAnnull)  {
		this.cdCausaleAnnull=cdCausaleAnnull;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCausaleAnnull]
	 **/
	public java.lang.String getCdCausaleAnnull() {
		return cdCausaleAnnull;
	}
}