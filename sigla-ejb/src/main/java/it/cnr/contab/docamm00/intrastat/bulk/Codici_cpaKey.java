/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Codici_cpaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long id_cpa;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CODICI_CPA
	 **/
	public Codici_cpaKey() {
		super();
	}
	public Codici_cpaKey(java.lang.Long id_cpa) {
		super();
		this.id_cpa=id_cpa;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Codici_cpaKey)) return false;
		Codici_cpaKey k = (Codici_cpaKey) o;
		if (!compareKey(getId_cpa(), k.getId_cpa())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_cpa());
		return i;
	}
	
	public void setId_cpa(java.lang.Long id_cpa)  {
		this.id_cpa=id_cpa;
	}
	public java.lang.Long getId_cpa() {
		return id_cpa;
	}
}