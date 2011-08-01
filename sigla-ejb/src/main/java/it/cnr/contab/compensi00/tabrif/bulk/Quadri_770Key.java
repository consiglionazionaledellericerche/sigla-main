/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2011
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Quadri_770Key extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_quadro;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: QUADRI_770
	 **/
	public Quadri_770Key() {
		super();
	}
	public Quadri_770Key(java.lang.Integer esercizio, java.lang.String cd_quadro) {
		super();
		this.esercizio=esercizio;
		this.cd_quadro=cd_quadro;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Quadri_770Key)) return false;
		Quadri_770Key k = (Quadri_770Key) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_quadro(), k.getCd_quadro())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_quadro());
		return i;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.String getCd_quadro() {
		return cd_quadro;
	}
	public void setCd_quadro(java.lang.String cd_quadro) {
		this.cd_quadro = cd_quadro;
	}

}