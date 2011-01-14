/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class PrivilegioKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_privilegio;
	public PrivilegioKey() {
		super();
	}
	public PrivilegioKey(java.lang.String cd_privilegio) {
		super();
		this.cd_privilegio=cd_privilegio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof PrivilegioKey)) return false;
		PrivilegioKey k = (PrivilegioKey) o;
		if (!compareKey(getCd_privilegio(), k.getCd_privilegio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_privilegio());
		return i;
	}
	public void setCd_privilegio(java.lang.String cd_privilegio)  {
		this.cd_privilegio=cd_privilegio;
	}
	public java.lang.String getCd_privilegio() {
		return cd_privilegio;
	}
}