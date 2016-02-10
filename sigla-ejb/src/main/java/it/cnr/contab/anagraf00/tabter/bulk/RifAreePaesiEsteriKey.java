/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class RifAreePaesiEsteriKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_area_estera;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RIF_AREE_PAESI_ESTERI
	 **/
	public RifAreePaesiEsteriKey() {
		super();
	}
	public RifAreePaesiEsteriKey(java.lang.String cd_area_estera) {
		super();
		this.cd_area_estera=cd_area_estera;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof RifAreePaesiEsteriKey)) return false;
		RifAreePaesiEsteriKey k = (RifAreePaesiEsteriKey) o;
		if (!compareKey(getCd_area_estera(), k.getCd_area_estera())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_area_estera());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAreaEstera]
	 **/
	public void setCd_area_estera(java.lang.String cd_area_estera)  {
		this.cd_area_estera=cd_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAreaEstera]
	 **/
	public java.lang.String getCd_area_estera() {
		return cd_area_estera;
	}
}