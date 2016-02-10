/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class IpaServFattKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String codAmm;
	private java.lang.String codOu;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_SERV_FATT
	 **/
	public IpaServFattKey() {
		super();
	}
	public IpaServFattKey(java.lang.String codAmm, java.lang.String codOu) {
		super();
		this.codAmm=codAmm;
		this.codOu=codOu;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof IpaServFattKey)) return false;
		IpaServFattKey k = (IpaServFattKey) o;
		if (!compareKey(getCodAmm(), k.getCodAmm())) return false;
		if (!compareKey(getCodOu(), k.getCodOu())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCodAmm());
		i = i + calculateKeyHashCode(getCodOu());
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codOu]
	 **/
	public void setCodOu(java.lang.String codOu)  {
		this.codOu=codOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codOu]
	 **/
	public java.lang.String getCodOu() {
		return codOu;
	}
}