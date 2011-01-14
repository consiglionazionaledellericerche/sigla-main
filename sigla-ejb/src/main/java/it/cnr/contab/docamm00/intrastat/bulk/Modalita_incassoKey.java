/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Modalita_incassoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_modalita_incasso;
	public Modalita_incassoKey() {
		super();
	}
	public Modalita_incassoKey(java.lang.Integer esercizio, java.lang.String cd_modalita_incasso) {
		super();
		this.esercizio=esercizio;
		this.cd_modalita_incasso=cd_modalita_incasso;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Modalita_incassoKey)) return false;
		Modalita_incassoKey k = (Modalita_incassoKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_modalita_incasso(), k.getCd_modalita_incasso())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_modalita_incasso());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_modalita_incasso(java.lang.String cd_modalita_incasso)  {
		this.cd_modalita_incasso=cd_modalita_incasso;
	}
	public java.lang.String getCd_modalita_incasso() {
		return cd_modalita_incasso;
	}
}