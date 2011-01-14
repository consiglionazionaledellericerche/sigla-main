/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_incaricoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_incarico;
	public Tipo_incaricoKey() {
		super();
	}
	public Tipo_incaricoKey(java.lang.String cd_tipo_incarico) {
		super();
		this.cd_tipo_incarico=cd_tipo_incarico;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_incaricoKey)) return false;
		Tipo_incaricoKey k = (Tipo_incaricoKey) o;
		if (!compareKey(getCd_tipo_incarico(), k.getCd_tipo_incarico())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_incarico());
		return i;
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico)  {
		this.cd_tipo_incarico=cd_tipo_incarico;
	}
	public java.lang.String getCd_tipo_incarico() {
		return cd_tipo_incarico;
	}
}