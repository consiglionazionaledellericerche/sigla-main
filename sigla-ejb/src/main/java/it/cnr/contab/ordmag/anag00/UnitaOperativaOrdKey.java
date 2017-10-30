/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class UnitaOperativaOrdKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUnitaOperativa;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_OPERATIVA_ORD
	 **/
	public UnitaOperativaOrdKey() {
		super();
	}
	public UnitaOperativaOrdKey(java.lang.String cdUnitaOperativa) {
		super();
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof UnitaOperativaOrdKey)) return false;
		UnitaOperativaOrdKey k = (UnitaOperativaOrdKey) o;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
}