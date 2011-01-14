/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class OrganoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_organo;
	public OrganoKey() {
		super();
	}
	public OrganoKey(java.lang.String cd_tipo_organo) {
		super();
		this.cd_organo=cd_tipo_organo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof OrganoKey)) return false;
		OrganoKey k = (OrganoKey) o;
		if (!compareKey(getCd_organo(), k.getCd_organo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_organo());
		return i;
	}
	public void setCd_organo(java.lang.String cd_tipo_organo)  {
		this.cd_organo=cd_tipo_organo;
	}
	public java.lang.String getCd_organo () {
		return cd_organo;
	}
}