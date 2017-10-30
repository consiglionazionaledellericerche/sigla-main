/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class TipoArticoloKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdTipoArticolo;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_ARTICOLO
	 **/
	public TipoArticoloKey() {
		super();
	}
	public TipoArticoloKey(java.lang.String cdTipoArticolo) {
		super();
		this.cdTipoArticolo=cdTipoArticolo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof TipoArticoloKey)) return false;
		TipoArticoloKey k = (TipoArticoloKey) o;
		if (!compareKey(getCdTipoArticolo(), k.getCdTipoArticolo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdTipoArticolo());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoArticolo]
	 **/
	public void setCdTipoArticolo(java.lang.String cdTipoArticolo)  {
		this.cdTipoArticolo=cdTipoArticolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoArticolo]
	 **/
	public java.lang.String getCdTipoArticolo() {
		return cdTipoArticolo;
	}
}