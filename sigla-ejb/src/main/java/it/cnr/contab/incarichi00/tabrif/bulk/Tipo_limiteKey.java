/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_limiteKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_limite;
	public Tipo_limiteKey() {
		super();
	}
	public Tipo_limiteKey(java.lang.String cd_tipo_limite) {
		super();
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_limiteKey)) return false;
		Tipo_limiteKey k = (Tipo_limiteKey) o;
		if (!compareKey(getCd_tipo_limite(), k.getCd_tipo_limite())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_limite());
		return i;
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite)  {
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public java.lang.String getCd_tipo_limite() {
		return cd_tipo_limite;
	}
}