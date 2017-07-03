/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class TipoOrdineKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdTipoOrdine;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_ORDINE
	 **/
	public TipoOrdineKey() {
		super();
	}
	public TipoOrdineKey(java.lang.String cdTipoOrdine) {
		super();
		this.cdTipoOrdine=cdTipoOrdine;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof TipoOrdineKey)) return false;
		TipoOrdineKey k = (TipoOrdineKey) o;
		if (!compareKey(getCdTipoOrdine(), k.getCdTipoOrdine())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdTipoOrdine());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOrdine]
	 **/
	public void setCdTipoOrdine(java.lang.String cdTipoOrdine)  {
		this.cdTipoOrdine=cdTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOrdine]
	 **/
	public java.lang.String getCdTipoOrdine() {
		return cdTipoOrdine;
	}
}