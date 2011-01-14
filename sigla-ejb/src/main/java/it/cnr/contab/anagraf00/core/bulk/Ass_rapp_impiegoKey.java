/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/01/2009
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_rapp_impiegoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_rapp_impiego_sti;
	public Ass_rapp_impiegoKey() {
		super();
	}
	public Ass_rapp_impiegoKey(java.lang.String cd_rapp_impiego_sti) {
		super();
		this.cd_rapp_impiego_sti=cd_rapp_impiego_sti;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_rapp_impiegoKey)) return false;
		Ass_rapp_impiegoKey k = (Ass_rapp_impiegoKey) o;
		if (!compareKey(getCd_rapp_impiego_sti(), k.getCd_rapp_impiego_sti())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_rapp_impiego_sti());
		return i;
	}
	public void setCd_rapp_impiego_sti(java.lang.String cd_rapp_impiego_sti)  {
		this.cd_rapp_impiego_sti=cd_rapp_impiego_sti;
	}
	public java.lang.String getCd_rapp_impiego_sti() {
		return cd_rapp_impiego_sti;
	}
}