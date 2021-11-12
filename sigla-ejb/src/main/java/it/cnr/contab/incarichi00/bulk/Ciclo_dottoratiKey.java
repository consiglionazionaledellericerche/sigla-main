/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ciclo_dottoratiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long id;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CICLO_DOTTORATI
	 **/
	public Ciclo_dottoratiKey() {
		super();
	}
	public Ciclo_dottoratiKey(java.lang.Long id) {
		super();
		this.id=id;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ciclo_dottoratiKey)) return false;
		Ciclo_dottoratiKey k = (Ciclo_dottoratiKey) o;
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Chiave Primaria.]
	 **/
	public java.lang.Long getId() {
		return id;
	}
}