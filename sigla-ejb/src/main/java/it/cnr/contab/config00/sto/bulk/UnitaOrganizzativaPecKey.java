/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/06/2010
 */
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class UnitaOrganizzativaPecKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUnitaOrganizzativa;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_ORGANIZZATIVA_PEC
	 **/
	public UnitaOrganizzativaPecKey() {
		super();
	}
	public UnitaOrganizzativaPecKey(java.lang.String cdUnitaOrganizzativa) {
		super();
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof UnitaOrganizzativaPecKey)) return false;
		UnitaOrganizzativaPecKey k = (UnitaOrganizzativaPecKey) o;
		if (!compareKey(getCdUnitaOrganizzativa(), k.getCdUnitaOrganizzativa())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUnitaOrganizzativa());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
}