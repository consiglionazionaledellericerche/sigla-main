/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2019
 */
package it.cnr.contab.siope.plus.bulk;
import it.cnr.contab.util.IdKey;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class SIOPEPlusEsitoKey extends OggettoBulk implements KeyedPersistent, IdKey {
	private java.lang.Long id;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SIOPE_PLUS_ESITO
	 **/
	public SIOPEPlusEsitoKey() {
		super();
	}
	public SIOPEPlusEsitoKey(java.lang.Long id) {
		super();
		this.id=id;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof SIOPEPlusEsitoKey)) return false;
		SIOPEPlusEsitoKey k = (SIOPEPlusEsitoKey) o;
		if (!compareKey(getId(), k.getId())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Chiave Primaria.]
	 **/
	public void setId(java.lang.Long id)  {
		this.id=id;
	}

	@Override
	public String getKeyName() {
		return "id";
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Chiave Primaria.]
	 **/
	public java.lang.Long getId() {
		return id;
	}
}