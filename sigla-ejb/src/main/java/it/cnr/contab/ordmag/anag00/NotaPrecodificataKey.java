/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class NotaPrecodificataKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdNotaPrecodificata;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NOTA_PRECODIFICATA
	 **/
	public NotaPrecodificataKey() {
		super();
	}
	public NotaPrecodificataKey(java.lang.String cdCds, java.lang.String cdNotaPrecodificata) {
		super();
		this.cdCds=cdCds;
		this.cdNotaPrecodificata=cdNotaPrecodificata;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof NotaPrecodificataKey)) return false;
		NotaPrecodificataKey k = (NotaPrecodificataKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdNotaPrecodificata(), k.getCdNotaPrecodificata())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdNotaPrecodificata());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNotaPrecodificata]
	 **/
	public void setCdNotaPrecodificata(java.lang.String cdNotaPrecodificata)  {
		this.cdNotaPrecodificata=cdNotaPrecodificata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNotaPrecodificata]
	 **/
	public java.lang.String getCdNotaPrecodificata() {
		return cdNotaPrecodificata;
	}
}