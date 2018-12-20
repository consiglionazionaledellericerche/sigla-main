/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class SIOPEPlusRisultatoKey extends OggettoBulk implements KeyedPersistent {
	private Long id;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SIOPE_PLUS_RISULTATO
	 **/
	public SIOPEPlusRisultatoKey() {
		super();
	}
	public SIOPEPlusRisultatoKey(Long id) {
		super();
		this.id=id;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof SIOPEPlusRisultatoKey)) return false;
		SIOPEPlusRisultatoKey k = (SIOPEPlusRisultatoKey) o;
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
	public void setId(Long id)  {
		this.id=id;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Chiave Primaria.]
	 **/
	public Long getId() {
		return id;
	}
}