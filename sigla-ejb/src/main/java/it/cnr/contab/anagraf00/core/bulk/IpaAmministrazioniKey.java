/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class IpaAmministrazioniKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String codAmm;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_AMMINISTRAZIONI
	 **/
	public IpaAmministrazioniKey() {
		super();
	}
	public IpaAmministrazioniKey(java.lang.String codAmm) {
		super();
		this.codAmm=codAmm;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof IpaAmministrazioniKey)) return false;
		IpaAmministrazioniKey k = (IpaAmministrazioniKey) o;
		if (!compareKey(getCodAmm(), k.getCodAmm())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCodAmm());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codAmm]
	 **/
	public void setCodAmm(java.lang.String codAmm)  {
		this.codAmm=codAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codAmm]
	 **/
	public java.lang.String getCodAmm() {
		return codAmm;
	}
}