/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_prestazioneKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_prestazione;
	public Tipo_prestazioneKey() {
		super();
	}
	public Tipo_prestazioneKey(java.lang.String cd_tipo_prestazione) {
		super();
		this.cd_tipo_prestazione=cd_tipo_prestazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_prestazioneKey)) return false;
		Tipo_prestazioneKey k = (Tipo_prestazioneKey) o;
		if (!compareKey(getCd_tipo_prestazione(), k.getCd_tipo_prestazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_prestazione());
		return i;
	}
	public void setCd_tipo_prestazione(java.lang.String cd_tipo_prestazione)  {
		this.cd_tipo_prestazione=cd_tipo_prestazione;
	}
	public java.lang.String getCd_tipo_prestazione() {
		return cd_tipo_prestazione;
	}
}