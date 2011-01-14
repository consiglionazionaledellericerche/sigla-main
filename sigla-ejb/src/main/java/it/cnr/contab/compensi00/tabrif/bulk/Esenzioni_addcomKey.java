/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Esenzioni_addcomKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_catastale;
	private java.sql.Timestamp dt_inizio_validita;
	public Esenzioni_addcomKey() {
		super();
	}
	public Esenzioni_addcomKey(java.lang.String cd_catastale, java.sql.Timestamp dt_inizio_validita) {
		super();
		this.cd_catastale=cd_catastale;
		this.dt_inizio_validita=dt_inizio_validita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Esenzioni_addcomKey)) return false;
		Esenzioni_addcomKey k = (Esenzioni_addcomKey) o;
		if (!compareKey(getCd_catastale(), k.getCd_catastale())) return false;
		if (!compareKey(getDt_inizio_validita(), k.getDt_inizio_validita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_catastale());
		i = i + calculateKeyHashCode(getDt_inizio_validita());
		return i;
	}
	public void setCd_catastale(java.lang.String cd_catastale)  {
		this.cd_catastale=cd_catastale;
	}
	public java.lang.String getCd_catastale() {
		return cd_catastale;
	}
	public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita)  {
		this.dt_inizio_validita=dt_inizio_validita;
	}
	public java.sql.Timestamp getDt_inizio_validita() {
		return dt_inizio_validita;
	}
}