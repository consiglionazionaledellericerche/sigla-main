/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Scadenzario_dottoratirataKey extends OggettoBulk implements KeyedPersistent {
	private Long id;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public Scadenzario_dottoratirataKey() {
		super();
	}
	public Scadenzario_dottoratirataKey(Long id) {
		super();
		this.id=id;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Scadenzario_dottoratirataKey)) return false;
		Scadenzario_dottoratirataKey k = (Scadenzario_dottoratirataKey) o;
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