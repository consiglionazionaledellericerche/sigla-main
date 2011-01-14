/*
* Created by Generator 1.0
* Date 12/05/2005
*/
package it.cnr.contab.anagraf00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Codici_rapporti_inpsKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_rapporto_inps;
	public Codici_rapporti_inpsKey() {
		super();
	}
	public Codici_rapporti_inpsKey(java.lang.String cd_rapporto_inps) {
		super();
		this.cd_rapporto_inps=cd_rapporto_inps;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Codici_rapporti_inpsKey)) return false;
		Codici_rapporti_inpsKey k = (Codici_rapporti_inpsKey) o;
		if (!compareKey(getCd_rapporto_inps(), k.getCd_rapporto_inps())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_rapporto_inps());
		return i;
	}
	public void setCd_rapporto_inps(java.lang.String cd_rapporto_inps)  {
		this.cd_rapporto_inps=cd_rapporto_inps;
	}
	public java.lang.String getCd_rapporto_inps () {
		return cd_rapporto_inps;
	}
}