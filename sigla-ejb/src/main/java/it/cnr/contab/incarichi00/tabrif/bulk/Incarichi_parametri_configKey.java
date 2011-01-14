/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Incarichi_parametri_configKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_config;

	public Incarichi_parametri_configKey() {
		super();
	}
	public Incarichi_parametri_configKey(java.lang.String cd_config) {
		super();
		this.cd_config=cd_config;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_parametri_configKey)) return false;
		Incarichi_parametri_configKey k = (Incarichi_parametri_configKey) o;
		if (!compareKey(getCd_config(), k.getCd_config())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_config());
		return i;
	}

	public java.lang.String getCd_config() {
		return cd_config;
	}
	public void setCd_config(java.lang.String cd_config) {
		this.cd_config = cd_config;
	}
}