/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Obbligazione_pluriennaleKey extends OggettoBulk implements KeyedPersistent {
	private Long idObbligazionePlur;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: OBBLIGAZIONE_PLURIENNALE
	 **/
	public Obbligazione_pluriennaleKey() {
		super();
	}
	public Obbligazione_pluriennaleKey(Long idObbligazionePlur) {
		super();
		this.idObbligazionePlur=idObbligazionePlur;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Obbligazione_pluriennaleKey)) return false;
		Obbligazione_pluriennaleKey k = (Obbligazione_pluriennaleKey) o;
		if (!compareKey(getIdObbligazionePlur(), k.getIdObbligazionePlur())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getIdObbligazionePlur());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Chiave Primaria.]
	 **/
	public void setIdObbligazionePlur(Long idObbligazionePlur)  {
		this.idObbligazionePlur=idObbligazionePlur;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Chiave Primaria.]
	 **/
	public Long getIdObbligazionePlur() {
		return idObbligazionePlur;
	}
}